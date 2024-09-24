package org.routineade.RoutineAdeServer.domain;

import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
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

    @Column(columnDefinition = "varchar(120)")
    private String content;

    @Column(columnDefinition = "varchar(255)")
    private String image;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public GroupChatting(String content, String image, Group group, User user) {
        this.content = content;
        this.image = image;
        this.createdDate = LocalDateTime.now();
        this.group = group;
        this.user = user;
    }

    public void setUserNull() {
        this.user = null;
    }

}
