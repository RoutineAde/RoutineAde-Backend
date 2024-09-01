package org.routineade.RoutineAdeServer.service;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.config.jwt.JwtProvider;
import org.routineade.RoutineAdeServer.config.jwt.UserAuthentication;
import org.routineade.RoutineAdeServer.domain.CompletionRoutine;
import org.routineade.RoutineAdeServer.domain.Routine;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.domain.common.Category;
import org.routineade.RoutineAdeServer.dto.routine.RoutineCategoryStatisticsInfo;
import org.routineade.RoutineAdeServer.dto.routine.RoutinesByUserProfileGetResponse;
import org.routineade.RoutineAdeServer.dto.user.UserRoutineCalenderStatisticsGetResponse;
import org.routineade.RoutineAdeServer.dto.user.UserRoutineCategoryStatisticsGetResponse;
import org.routineade.RoutineAdeServer.dto.user.UserRoutineCompletionStatistics;
import org.routineade.RoutineAdeServer.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RoutineService routineService;
    private static final String BASIC_PROFILE_IMAGE = "기본 프로필 이미지";

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

}
