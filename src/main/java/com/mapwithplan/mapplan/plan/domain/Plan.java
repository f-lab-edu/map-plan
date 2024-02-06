package com.mapwithplan.mapplan.plan.domain;

import com.mapwithplan.mapplan.common.timeutils.domain.BaseTime;
import com.mapwithplan.mapplan.common.timeutils.service.port.TimeClockHolder;
import com.mapwithplan.mapplan.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Plan extends BaseTime {

    private Long id;

    private Member author;

    private String title;

    private String content;

    private String location;

    private LocalDateTime appointmentDate;

    private String category;

    @Builder
    public Plan(LocalDateTime createdAt, LocalDateTime modifiedAt, Long id, Member author, String title,
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


    public static Plan from(PlanCreate planCreate, Member member,TimeClockHolder clockHolder){
        return Plan.builder()
                .title(planCreate.getTitle())
                .author(member)
                .content(planCreate.getContent())
                .location(planCreate.getLocation())
                .category(planCreate.getCategory())
                .appointmentDate(planCreate.getAppointmentDate())
                .createdAt(clockHolder.clockHold())
                .modifiedAt(clockHolder.clockHold())
                .build();

    }
}
