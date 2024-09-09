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
import org.routineade.RoutineAdeServer.domain.Routine;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.domain.common.Category;
import org.routineade.RoutineAdeServer.dto.routine.RoutineCategoryStatisticsInfo;
import org.routineade.RoutineAdeServer.dto.routine.RoutinesByUserProfileGetResponse;
import org.routineade.RoutineAdeServer.dto.user.UserProfileGetResponse;
import org.routineade.RoutineAdeServer.dto.user.UserRoutineCalenderStatisticsGetResponse;
import org.routineade.RoutineAdeServer.dto.user.UserRoutineCategoryStatisticsGetResponse;
import org.routineade.RoutineAdeServer.dto.user.UserRoutineCompletionStatistics;
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
    private static final String BASIC_PROFILE_IMAGE = "https://routineade-ducket.s3.ap-northeast-2.amazonaws.com/Basic_Pofile.png";

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

    @Transactional(readOnly = true)
    public UserProfileGetResponse getUserProfile(User user) {
        return UserProfileGetResponse.of(user);
    }

    public void updateUserProfile(User user, String nickname, String intro, MultipartFile image) {
        if (!user.getNickname().equals(nickname) && userRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }
        user.updateInfo(image == null ? user.getProfileImage() : saveAndGetImage(image), nickname, intro);
    }

    private String saveAndGetImage(MultipartFile image) {
        String uploadName = UUID.randomUUID() + image.getOriginalFilename();
        s3Service.uploadFileToS3(image, uploadName);
        return s3Service.getFileURLFromS3(uploadName).toString();
    }

}
