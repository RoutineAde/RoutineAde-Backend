package org.routineade.RoutineAdeServer.service;

import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.domain.UserHistory;
import org.routineade.RoutineAdeServer.repository.UserHistoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserHistoryService {

    private final UserHistoryRepository userHistoryRepository;

    @Transactional
    public void checkRoutine(User user, LocalDate today, Long routineId) {
        Optional<UserHistory> userHistory = userHistoryRepository.findByUserAndCreatedDate(user, String.valueOf(today));

        if (userHistory.isPresent()) {
            userHistory.get().updateFinishedRoutine(String.valueOf(routineId));
        } else {
            UserHistory newUserHistory = UserHistory.builder()
                    .user(user)
                    .finishedRoutine(String.valueOf(routineId))
                    .build();

            userHistoryRepository.save(newUserHistory);
        }
    }
}
