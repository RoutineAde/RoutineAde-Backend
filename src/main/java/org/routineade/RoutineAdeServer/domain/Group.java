package org.routineade.RoutineAdeServer.domain;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.routineade.RoutineAdeServer.domain.common.Category;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "`group`")
public class Group {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(nullable = false)
    private Long groupId;

    @Column(columnDefinition = "varchar(10)", nullable = false)
    private String groupTitle;

    @Column(nullable = false)
    private Boolean isPublic;

    @Column
    private Integer groupPassword;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category groupCategory;

    @Column(nullable = false)
    private Integer maxMember;

    @Column(columnDefinition = "varchar(100)", nullable = false)
    private String description;

    @Column(nullable = false)
    private Long createdUserId;

    @OneToMany(mappedBy = "group", cascade = ALL)
    private List<GroupRoutine> groupRoutines = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = ALL)
    private List<GroupChatting> groupChattings = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = ALL)
    private List<GroupMember> groupMembers = new ArrayList<>();

    @Builder
    public Group(String groupTitle, Boolean isPublic, Integer groupPassword, Category groupCategory, Integer maxMember,
                 String description, Long createdUserId) {
        this.groupTitle = groupTitle;
        this.isPublic = isPublic;
        this.groupPassword = groupPassword;
        this.groupCategory = groupCategory;
        this.maxMember = maxMember;
        this.description = description;
        this.createdUserId = createdUserId;
    }
}
