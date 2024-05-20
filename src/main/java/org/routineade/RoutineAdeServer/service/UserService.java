package org.routineade.RoutineAdeServer.service;

import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public User getUserOrException(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new RuntimeException("해당 ID를 가진 유저가 없습니다."));
    }

}
