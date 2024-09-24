package org.routineade.RoutineAdeServer.service;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.config.S3Service;
import org.routineade.RoutineAdeServer.config.jwt.JwtProvider;
import org.routineade.RoutineAdeServer.config.jwt.UserAuthentication;
import org.routineade.RoutineAdeServer.domain.CompletionRoutine;
import org.routineade.RoutineAdeServer.domain.Group;
import org.routineade.RoutineAdeServer.domain.GroupChatting;
import org.routineade.RoutineAdeServer.domain.Routine;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.domain.common.Category;
import org.routineade.RoutineAdeServer.dto.firebase.UserFirebeseTokenSaveRequest;
import org.routineade.RoutineAdeServer.dto.routine.RoutineCategoryStatisticsInfo;
import org.routineade.RoutineAdeServer.dto.routine.RoutinesByUserProfileGetResponse;
import org.routineade.RoutineAdeServer.dto.user.UserInfosGetResponse;
import org.routineade.RoutineAdeServer.dto.user.UserRoutineCalenderStatisticsGetResponse;
import org.routineade.RoutineAdeServer.dto.user.UserRoutineCategoryStatisticsGetResponse;
import org.routineade.RoutineAdeServer.dto.user.UserRoutineCompletionStatistics;
import org.routineade.RoutineAdeServer.repository.GroupRepository;
import org.routineade.RoutineAdeServer.repository.GroupRoutineRepository;
import org.routineade.RoutineAdeServer.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RoutineService routineService;
    private final S3Service s3Service;
    private final GroupRepository groupRepository;
    private final GroupRoutineRepository groupRoutineRepository;
    //    private final FCMNotificationService fcmNotificationService;
    private static final String BASIC_PROFILE_IMAGE = "https://routineade-ducket.s3.ap-northeast-2.amazonaws.com/BasicProfile.png";

    @Transactional(readOnly = true)
    public User getUserOrException(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new IllegalArgumentException("해당 ID를 가진 유저가 없습니다."));
    }

    @Transactional(readOnly = true)
    public String login(Long userId) {
        User user = getUserOrException(userId);

        UserAuthentication userAuthentication = new UserAuthentication(user.getUserId(), null, null);

        return jwtProvider.generateToken(userAuthentication);
    }

    public Long getOrCreateUser(String email, String profileImage) {
        User user;
        if (!userRepository.existsByEmail(email)) {
            User newUser = User.builder()
                    .email(email)
                    .intro("한 줄 소개")
                    .nickname("닉네임")
                    .profileImage(profileImage == null ? BASIC_PROFILE_IMAGE : profileImage)
                    .build();
            user = userRepository.save(newUser);
        } else {
            user = userRepository.findByEmail(email).get();
        }

        return user.getUserId();
    }

    public void createUserInfo(User user, String nickname, String intro, MultipartFile image) {
        if (userRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }

        user.updateInfo(image == null ? user.getProfileImage() : saveAndGetImage(image), nickname, intro);
    }

    @Transactional(readOnly = true)
    public UserRoutineCategoryStatisticsGetResponse getUserStatistics(User user, String date) {
        YearMonth yearMonth = YearMonth.parse(date, DateTimeFormatter.ofPattern("yyyy.MM"));

        List<Routine> completionRoutines = user.getCompletionRoutines()
                .stream()
                .filter(cr -> YearMonth.from(cr.getCompletionDate()).equals(yearMonth))
                .map(CompletionRoutine::getRoutine)
                .toList();

        List<RoutineCategoryStatisticsInfo> routineCategoryStatisticsInfos = Arrays.stream(Category.values())
                .map(category ->
                        RoutineCategoryStatisticsInfo.of(category.getLabel(),
                                (int) completionRoutines
                                        .stream()
                                        .filter(routine -> routine.getRoutineCategory().equals(category))
                                        .count()
                        )
                )
                .toList();

        return UserRoutineCategoryStatisticsGetResponse.of(
                completionRoutines.size(),
                routineCategoryStatisticsInfos
        );
    }

    @Transactional(readOnly = true)
    public UserRoutineCalenderStatisticsGetResponse getUserCalenderStatistics(User user, String date) {
        YearMonth yearMonth = YearMonth.parse(date, DateTimeFormatter.ofPattern("yyyy.MM"));

        List<CompletionRoutine> completionRoutines = user.getCompletionRoutines()
                .stream()
                .filter(cr -> YearMonth.from(cr.getCompletionDate()).equals(yearMonth))
                .toList();

        return UserRoutineCalenderStatisticsGetResponse.of(
                completionRoutines.size(),
                UserRoutineCompletionStatistics.of(routineService.getUserRoutineCompletionStatisticsByMonth(
                        user, yearMonth, completionRoutines))
        );
    }

    @Transactional(readOnly = true)
    public RoutinesByUserProfileGetResponse getUserProfileRoutines(User user) {
        return routineService.getRoutinesByUserProfile(user);
    }

    public void updateUserProfile(User user, String nickname, String intro, MultipartFile image) {
        if (!user.getNickname().equals(nickname) && userRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }
        user.updateInfo(image == null ? user.getProfileImage() : saveAndGetImage(image), nickname, intro);
    }

    public void saveUserFirebaseToken(User user, UserFirebeseTokenSaveRequest request) {
        if (user.getFirebaseToken() != null) {
            throw new IllegalArgumentException("이미 Firebase Token이 존재합니다.");
        }
        user.updateFirebaseToken(request.token());
    }

    @Transactional(readOnly = true)
    public UserInfosGetResponse getUserInfos(User user) {
        return UserInfosGetResponse.of(user, routineService.getUserPersonalRoutine(user));
    }

    public void deleteUserInfos(User user) {
        for (GroupChatting groupChatting : user.getGroupChattings()) {
            groupChatting.setUserNull();
        }

        for (Group group : groupRepository.findAllByCreatedUserId(user.getUserId())) {
            groupRoutineRepository.deleteAll(group.getGroupRoutines());
            groupRepository.delete(group);
        }

        userRepository.delete(user);
    }

    private String saveAndGetImage(MultipartFile image) {
        String uploadName = UUID.randomUUID() + image.getOriginalFilename();
        s3Service.uploadFileToS3(image, uploadName);
        return s3Service.getFileURLFromS3(uploadName).toString();
    }

//    @Scheduled(cron = "0 0 10 * * *")
//    private void sendRoutineAlarm() {
//        List<User> users = userRepository.findAll();
//
//        for (User user : users) {
//            RoutinesGetResponse routines = routineService.getRoutines(user,
//                    LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy.MM.dd")));
//
//            if (routines.personalRoutines().isEmpty() && routines.groupRoutines().isEmpty()) {
//                continue;
//            }
//
//            List<PersonalRoutineInfo> personalRoutineInfos = routines.personalRoutines().stream()
//                    .flatMap(personalRoutine -> personalRoutine.routines().stream())
//                    .toList();
//
//            for (PersonalRoutineInfo pRoutine : personalRoutineInfos) {
//                String routineTitle = pRoutine.routineTitle();
//
//                // 내용 수정
//                FCMNotificationRequest request = FCMNotificationRequest.of(user, "", "");
//
//                fcmNotificationService.sendNotificationByToken(request);
//            }
//
//            for (GroupRoutinesGetResponse groupRoutine : routines.groupRoutines()) {
//                Group group = groupService.getGroupOrThrowException(groupRoutine.groupId());
//                if (groupMemberService.isUserGroupAlarmEnabled(user, group)) {
//                    List<GroupRoutineInfo> gRoutines = groupRoutine.groupRoutines().stream()
//                            .flatMap(gr -> gr.routines().stream())
//                            .toList();
//
//                    for (GroupRoutineInfo gRoutine : gRoutines) {
//                        String routineTitle = gRoutine.routineTitle();
//
//                        // 내용 수정
//                        FCMNotificationRequest request = FCMNotificationRequest.of(user, "", "");
//
//                        fcmNotificationService.sendNotificationByToken(request);
//                    }
//                }
//            }
//        }
//    }

}
