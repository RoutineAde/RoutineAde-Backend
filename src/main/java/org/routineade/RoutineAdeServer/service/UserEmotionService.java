package org.routineade.RoutineAdeServer.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.domain.UserEmotion;
import org.routineade.RoutineAdeServer.dto.userEmotion.UserEmotionCreateRequest;
import org.routineade.RoutineAdeServer.repository.UserEmotionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserEmotionService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy.MM.dd");
    private final UserEmotionRepository userEmotionRepository;

    @Transactional(readOnly = true)
    public UserEmotion getUserEmotionByDate(User user, LocalDate date) {
        return getUserEmotionOrException(user, date);
    }

    public void createUserEmotion(User user, UserEmotionCreateRequest request) {
        LocalDate date = LocalDate.parse(request.date(), DATE_FORMATTER);

        if (LocalDate.now().isBefore(date)) {
            throw new RuntimeException("미래날짜에 미리 감정을 등록할 수 없습니다!");
        }

        if (getUserEmotionOrException(user, date) != null) {
            throw new RuntimeException("유저가 이미 해당 날짜에 감정을 등록했습니다!");
        }

        UserEmotion userEmotion = UserEmotion.builder()
                .user(user)
                .emotion(request.userEmotion())
                .createdDate(date)
                .build();

        userEmotionRepository.save(userEmotion);
    }

    private UserEmotion getUserEmotionOrException(User user, LocalDate date) {
        return userEmotionRepository.findByUserAndCreatedDate(user, date).orElse(null);
    }

}
