package org.routineade.RoutineAdeServer.service;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.config.S3Service;
import org.routineade.RoutineAdeServer.domain.Group;
import org.routineade.RoutineAdeServer.domain.GroupChatting;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.dto.groupChatting.GroupChattingCreateRequest;
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
        GroupChatting groupChatting = GroupChatting.builder()
                .group(group)
                .content(request.content())
                .image(request.image() == null ? null : uploadToS3AndGetURL(request.image()))
                .writerUserId(user.getUserId())
                .build();

        groupChattingRepository.save(groupChatting);
    }

    private String uploadToS3AndGetURL(MultipartFile image) {
        String uploadName = UUID.randomUUID() + image.getOriginalFilename();
        s3Service.uploadFileToS3(image, uploadName);
        return s3Service.getFileURLFromS3(uploadName).toString();
    }

}
