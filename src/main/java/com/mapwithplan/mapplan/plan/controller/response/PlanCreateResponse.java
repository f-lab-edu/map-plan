package com.mapwithplan.mapplan.plan.controller.response;

import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.plan.domain.Plan;
import com.mapwithplan.mapplan.plan.domain.PlanCreate;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;


/**
 * 일정을 생성한 후 응답에 사용되는 Response 입니다.
 */
@Getter
public class PlanCreateResponse {


    private Long planId;

    private String title;

    private LocalDateTime appointmentDate;

    private String authorName;

    private String content;

    private String location;

    private String category;
    @Builder
    public PlanCreateResponse(Long planId, String title, LocalDateTime appointmentDate, String authorName, String content, String location, String category) {
        this.planId = planId;
        this.title = title;
        this.appointmentDate = appointmentDate;
        this.authorName = authorName;
        this.content = content;
        this.location = location;
        this.category = category;
    }


    public static PlanCreateResponse from(Plan plan){
        return PlanCreateResponse.builder()
                .planId(plan.getId())
                .title(plan.getTitle())
                .appointmentDate(plan.getAppointmentDate())
                .authorName(plan.getAuthor().getName())
                .content(plan.getContent())
                .location(plan.getLocation())
                .category(plan.getCategory())
                .build();
    }
}
