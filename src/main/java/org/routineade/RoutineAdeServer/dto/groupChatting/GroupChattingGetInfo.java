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
        String createdTime
) {
    public static GroupChattingGetInfo of(GroupChatting groupChatting, User user) {
        if (groupChatting.getUser() == null) {
            return new GroupChattingGetInfo(
                    false,
                    null,
                    "(알 수 없음)",
                    "https://routineade-ducket.s3.ap-northeast-2.amazonaws.com/GrayProfile.png",
                    groupChatting.getContent(),
                    groupChatting.getImage(),
                    groupChatting.getCreatedDate().format(DateTimeFormatter.ofPattern("a h:mm", KOREAN)));
        }

        return new GroupChattingGetInfo(
                Objects.equals(groupChatting.getUser(), user),
                groupChatting.getUser().getUserId(), groupChatting.getUser().getNickname(),
                groupChatting.getUser().getProfileImage(), groupChatting.getContent(),
                groupChatting.getImage(),
                groupChatting.getCreatedDate().format(DateTimeFormatter.ofPattern("a h:mm", KOREAN)));
    }
}
