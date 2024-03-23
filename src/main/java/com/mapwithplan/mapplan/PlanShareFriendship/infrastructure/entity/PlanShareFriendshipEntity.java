package com.mapwithplan.mapplan.PlanShareFriendship.infrastructure.entity;

import com.mapwithplan.mapplan.PlanShareFriendship.domain.PlanShareFriendship;
import com.mapwithplan.mapplan.common.timeutils.domain.BaseTime;
import com.mapwithplan.mapplan.common.timeutils.entity.BaseTimeEntity;
import com.mapwithplan.mapplan.friendship.domain.Friendship;
import com.mapwithplan.mapplan.friendship.infrastructure.entity.FriendshipEntity;
import com.mapwithplan.mapplan.plan.domain.Plan;
import com.mapwithplan.mapplan.plan.infrastructure.entity.PlanEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "plan_share_friendship")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PlanShareFriendshipEntity extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_share_friendship_id")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "friendship_id")
    private FriendshipEntity friendshipEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id")
    private PlanEntity planEntity;


    @Builder
    public PlanShareFriendshipEntity(LocalDateTime createdAt, LocalDateTime modifiedAt, Long id, FriendshipEntity friendshipEntity, PlanEntity planEntity) {
        super(createdAt, modifiedAt);
        this.id = id;
        this.friendshipEntity = friendshipEntity;
        this.planEntity = planEntity;
    }


    public PlanShareFriendship toModel(){
        return PlanShareFriendship.builder()
                .id(id)
                .plan(planEntity.toModel())
                .friendship(friendshipEntity.toModel())
                .createdAt(getCreatedAt())
                .modifiedAt(getModifiedAt())
                .build();
    }


    public static PlanShareFriendshipEntity from(PlanShareFriendship planShareFriendship){

        return PlanShareFriendshipEntity.builder()
                .id(planShareFriendship.getId())
                .friendshipEntity(FriendshipEntity.from(planShareFriendship.getFriendship()))
                .planEntity(PlanEntity.from(planShareFriendship.getPlan()))
                .createdAt(planShareFriendship.getCreatedAt())
                .modifiedAt(planShareFriendship.getModifiedAt())
                .build();
    }

}
