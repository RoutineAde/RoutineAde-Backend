package org.routineade.RoutineAdeServer.service;

import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.config.S3Service;
import org.routineade.RoutineAdeServer.domain.Group;
import org.routineade.RoutineAdeServer.domain.GroupChatting;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.dto.groupChatting.GroupChattingCreateRequest;
import org.routineade.RoutineAdeServer.dto.groupChatting.GroupChattingGetInfo;
import org.routineade.RoutineAdeServer.dto.groupChatting.GroupChattingGetResponse;
import org.routineade.RoutineAdeServer.repository.GroupChattingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class GroupChattingService {

    private final GroupChattingRepository groupChattingRepository;
    private final S3Service s3Service;

    public void createGroupChatting(Group group, User user, GroupChattingCreateRequest request) {
        if (request.content() != null && request.image() != null) {
            throw new RuntimeException("채팅은 글과 이미지 둘 중 하나만 등록할 수 있습니다!");
        }
        GroupChatting groupChatting = GroupChatting.builder()
                .content(request.content())
                .image(request.image() == null ? null : saveAndGetImage(request.image()))
                .group(group)
                .user(user)
                .build();

        groupChattingRepository.save(groupChatting);
    }

    public GroupChattingGetResponse getGroupChatting(Group group, User user) {
        List<GroupChatting> groupChattingList = groupChattingRepository.findAllByGroup(group);

        return GroupChattingGetResponse.of(
                groupChattingList.stream().map(gc -> GroupChattingGetInfo.of(gc, user)).toList());
    }

    private String saveAndGetImage(MultipartFile image) {
        String uploadName = UUID.randomUUID() + image.getOriginalFilename();
        s3Service.uploadFileToS3(image, uploadName);
        return s3Service.getFileURLFromS3(uploadName).toString();
    }

}
