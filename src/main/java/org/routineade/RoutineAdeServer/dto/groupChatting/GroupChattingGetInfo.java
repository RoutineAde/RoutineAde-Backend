package org.routineade.RoutineAdeServer.dto.groupChatting;

import static java.util.Locale.KOREAN;

import java.time.format.DateTimeFormatter;
import java.util.Objects;
import org.routineade.RoutineAdeServer.domain.GroupChatting;
import org.routineade.RoutineAdeServer.domain.User;

public record GroupChattingGetInfo(
        boolean isMine,
        Long userId,
        String nickname,
        String profileImage,
        String content,
        String image,
        String createdDate
) {
    public static GroupChattingGetInfo of(GroupChatting groupChatting, User user) {
        return new GroupChattingGetInfo(Objects.equals(groupChatting.getUser(), user),
                groupChatting.getUser().getUserId(), groupChatting.getUser().getNickname(),
                groupChatting.getUser().getProfileImage(), groupChatting.getContent(),
                groupChatting.getImage(),
                groupChatting.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 E", KOREAN)));
    }
}
