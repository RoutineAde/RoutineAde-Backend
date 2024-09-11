package org.routineade.RoutineAdeServer.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.dto.firebase.FCMNotificationRequest;
import org.routineade.RoutineAdeServer.repository.UserRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FCMNotificationService {

    private final FirebaseMessaging firebaseMessaging;
    private final UserRepository userRepository;

    public void sendNotificationByToken(FCMNotificationRequest request) {
        Optional<User> user = userRepository.findById(request.targetUserId());

        if (user.isPresent()) {
            if (user.get().getFirebaseToken() != null) {
                Notification notification = Notification.builder()
                        .setTitle(request.title())
                        .setBody(request.body())
                        .build();

                Message message = Message.builder()
                        .setToken(user.get().getFirebaseToken())
                        .setNotification(notification)
                        .build();

                try {
                    firebaseMessaging.send(message);
                } catch (FirebaseMessagingException e) {
                    throw new IllegalArgumentException("푸시 알림 보내기를 실패했습니다.");
                }
            } else {
                throw new IllegalArgumentException("서버에 저장된 해당 유저의 Firebase Token이 존재하지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("해당 유저가 존재하지 않습니다.");
        }
    }

}
