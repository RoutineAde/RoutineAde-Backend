package org.routineade.RoutineAdeServer.service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.routineade.RoutineAdeServer.config.S3Service;
import org.routineade.RoutineAdeServer.domain.Group;
import org.routineade.RoutineAdeServer.domain.GroupChatting;
import org.routineade.RoutineAdeServer.domain.User;
import org.routineade.RoutineAdeServer.dto.groupChatting.GroupChattingGetByDay;
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

    public void createGroupChatting(Group group, User user, String content, MultipartFile image) {
        if (content != null && image != null) {
            throw new IllegalArgumentException("채팅은 글과 이미지 둘 중 하나만 등록할 수 있습니다!");
        }
        if (content == null && image == null) {
            throw new IllegalArgumentException("채팅은 글과 이미지 둘 중 하나는 등록해야 합니다!");
        }

        GroupChatting groupChatting = GroupChatting.builder()
                .content(content)
                .image(image == null ? null : saveAndGetImage(image))
                .group(group)
                .user(user)
                .build();

        groupChattingRepository.save(groupChatting);
    }

    public GroupChattingGetResponse getGroupChatting(Group group, User user) {
        List<GroupChattingGetByDay> groupChattingByDays = groupChattingRepository.findAllByGroup(group)
                .stream()
                .sorted(Comparator.comparing(GroupChatting::getCreatedDate))
                .collect(Collectors.groupingBy(
                        gc -> gc.getCreatedDate().toLocalDate(),
                        Collectors.mapping(gc -> GroupChattingGetInfo.of(gc, user), Collectors.toList())
                ))
                .entrySet().stream()
                .map(entry -> GroupChattingGetByDay.of(entry.getKey().atStartOfDay(), entry.getValue()))
                .sorted(Comparator.comparing(GroupChattingGetByDay::createdDate))
                .toList();

        return GroupChattingGetResponse.of(groupChattingByDays);
    }

    private String saveAndGetImage(MultipartFile image) {
        String uploadName = UUID.randomUUID() + image.getOriginalFilename();
        s3Service.uploadFileToS3(image, uploadName);
        return s3Service.getFileURLFromS3(uploadName).toString();
    }

}
