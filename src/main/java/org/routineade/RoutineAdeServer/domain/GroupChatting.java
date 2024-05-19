package org.routineade.RoutineAdeServer.domain;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupChatting {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false)
    private Long groupChattingId;

    @Column(columnDefinition = "varchar(120)", nullable = false)
    private String content;

    @Column
    private String image;

    @Column(nullable = false)
    private Long writerUserId;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @Builder
    public GroupChatting(String content, String image, Long writerUserId, Group group) {
        this.content = content;
        this.image = image;
        this.writerUserId = writerUserId;
        this.group = group;
    }
    
}
