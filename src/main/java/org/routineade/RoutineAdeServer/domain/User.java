package org.routineade.RoutineAdeServer.domain;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false)
    private Long userId;

    @Column(columnDefinition = "varchar(100)", nullable = false)
    private String email;

    @Column(columnDefinition = "varchar(255)", nullable = false)
    private String profileImage;

    @Column(columnDefinition = "varchar(10)", nullable = false)
    private String nickname;

    @Column(columnDefinition = "varchar(20)")
    private String intro;

    @Column(columnDefinition = "boolean default true", nullable = false)
    private Boolean isPublic;

    @OneToMany(mappedBy = "user", cascade = ALL)
    private List<Alarm> alarms = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL)
    private List<GroupMember> groupMembers = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL)
    private List<BanGroupMember> banGroupMembers = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL)
    private List<CompletionRoutine> completionRoutines = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL)
    private List<GroupChatting> groupChattings = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = ALL)
    private List<UserEmotion> userEmotions = new ArrayList<>();

    @Builder
    public User(String email, String profileImage, String nickname, String intro) {
        this.email = email;
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.intro = intro;
    }

    public void updateInfo(String profileImage, String nickname, String intro) {
        this.profileImage = profileImage;
        this.nickname = nickname;
        this.intro = intro;
    }

}
