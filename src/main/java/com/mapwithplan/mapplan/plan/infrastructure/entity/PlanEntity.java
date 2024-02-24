package com.mapwithplan.mapplan.plan.infrastructure.entity;

import com.mapwithplan.mapplan.common.timeutils.entity.BaseTimeEntity;
import com.mapwithplan.mapplan.member.infrastructure.entity.MemberEntity;
import com.mapwithplan.mapplan.plan.domain.Plan;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//todo 친구 객체 만들고 일정 참여자 추가
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "Plan")
public class PlanEntity extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity author;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;
    @Column(name = "location")
    private String location;
    @Column(name = "appointmemt_date")
    private LocalDateTime appointmentDate;

    @Column(name = "category")
    private String category;

    @Builder
    public PlanEntity(LocalDateTime createdAt, LocalDateTime modifiedAt, Long id, MemberEntity author, String title,
                      String content, String location, LocalDateTime appointmentDate, String category) {
        super(createdAt, modifiedAt);
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        this.location = location;
        this.appointmentDate = appointmentDate;
        this.category = category;
    }


    public static PlanEntity from(Plan plan){
        return PlanEntity.builder()
                .id(plan.getId())
                .author(MemberEntity.from(plan.getAuthor()))
                .title(plan.getTitle())
                .content(plan.getContent())
                .location(plan.getLocation())
                .appointmentDate(plan.getAppointmentDate())
                .category(plan.getCategory())
                .createdAt(plan.getCreatedAt())
                .modifiedAt(plan.getModifiedAt())
                .build();
    }
    public Plan toModel(){
        return Plan.builder()
                .id(id)
                .author(author.toModel())
                .title(title)
                .content(content)
                .category(category)
                .location(location)
                .appointmentDate(appointmentDate)
                .createdAt(getCreatedAt())
                .modifiedAt(getModifiedAt())
                .build();
    }


}
