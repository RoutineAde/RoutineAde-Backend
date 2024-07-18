package org.routineade.RoutineAdeServer.service;

import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.domain.UserEmotion;
import org.routineade.RoutineAdeServer.repository.UserEmotionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserEmotionService {

    private final UserEmotionRepository userEmotionRepository;

    @Transactional(readOnly = true)
    public UserEmotion getUserEmotionByDate(User user, LocalDate date) {
        return userEmotionRepository.findByUserAndCreatedDate(user, date).orElse(null);
    }

}
