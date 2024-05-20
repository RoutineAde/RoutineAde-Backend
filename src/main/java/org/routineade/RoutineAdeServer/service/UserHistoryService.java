package org.routineade.RoutineAdeServer.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.domain.UserHistory;
import org.routineade.RoutineAdeServer.dto.user.CreateDailyMoodRequest;
import org.routineade.RoutineAdeServer.repository.UserHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserHistoryService {

    private final UserHistoryRepository userHistoryRepository;

    @Transactional
    public void checkRoutine(User user, LocalDate routineDate, Long routineId) {
        Optional<UserHistory> userHistory = userHistoryRepository.findByUserAndHistoryDate(user,
                String.valueOf(routineDate));

        if (userHistory.isPresent()) {
            userHistory.get().updateFinishedRoutine(String.valueOf(routineId));
        } else {
            UserHistory newUserHistory = UserHistory.builder()
                    .user(user)
                    .historyDate(routineDate.toString())
                    .finishedRoutine(String.valueOf(routineId))
                    .build();

            userHistoryRepository.save(newUserHistory);
        }
    }

    @Transactional
    public void createDailyMood(User user, CreateDailyMoodRequest request) {
        LocalDate date = LocalDate.parse(request.date(), DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        if (date.isAfter(LocalDate.now())) {
            throw new RuntimeException("미래 날짜에 감정을 남길 수 없습니다!");
        }

        Optional<UserHistory> userHistory = userHistoryRepository.findByUserAndHistoryDate(user, String.valueOf(date));

        if (userHistory.isPresent()) {
            if (userHistory.get().getDailyMood() != null) {
                throw new RuntimeException("감정 등록은 최초 한 번만 가능합니다.");
            }
            userHistory.get().setDailyMood(request.dailyMood());
        } else {
            UserHistory newUserHistory = UserHistory.builder()
                    .user(user)
                    .historyDate(date.toString())
                    .dailyMood(request.dailyMood())
                    .build();

            userHistoryRepository.save(newUserHistory);
        }
    }
}
