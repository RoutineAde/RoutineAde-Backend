//package org.routineade.RoutineAdeServer.service;
//
//import com.google.firebase.messaging.FirebaseMessaging;
//import com.google.firebase.messaging.FirebaseMessagingException;
//import com.google.firebase.messaging.Message;
//import com.google.firebase.messaging.Notification;
//import lombok.RequiredArgsConstructor;
//import org.routineade.RoutineAdeServer.domain.User;
//import org.routineade.RoutineAdeServer.dto.firebase.FCMNotificationRequest;
//import org.springframework.stereotype.Service;
//
//@RequiredArgsConstructor
//@Service
//public class FCMNotificationService {
//
//    private final FirebaseMessaging firebaseMessaging;
//
//    public void sendNotificationByToken(FCMNotificationRequest request) {
//        User user = request.user();
//
//        if (user.getFirebaseToken() != null) {
//            Notification notification = Notification.builder()
//                    .setTitle(request.title())
//                    .setBody(request.body())
//                    .build();
//
//            Message message = Message.builder()
//                    .setToken(user.getFirebaseToken())
//                    .setNotification(notification)
//                    .build();
//
//            try {
//                firebaseMessaging.send(message);
//            } catch (FirebaseMessagingException e) {
//                throw new IllegalArgumentException("푸시 알림 보내기를 실패했습니다.");
//            }
//        } else {
//            throw new IllegalArgumentException("서버에 저장된 해당 유저의 Firebase Token이 존재하지 않습니다.");
//        }
//    }
//
//}
