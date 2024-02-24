package com.mapwithplan.mapplan.plan.controller.response;

import com.mapwithplan.mapplan.member.domain.Member;
import com.mapwithplan.mapplan.plan.domain.Plan;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * 일정 정보 상세를 전달하는 Response 입니다.
 */
@Getter
public class PlanDetailResponse {


    private Long id;

    private String author;

    private String title;

    private String content;

    private String location;

    private LocalDateTime appointmentDate;

    private LocalDateTime createAt;

    private String category;

    @Builder
    public PlanDetailResponse(Long id, String author, String title, String content, String location, LocalDateTime appointmentDate, LocalDateTime createAt, String category) {
        this.id = id;
        this.author = author;
        this.title = title;
        this.content = content;
        this.location = location;
        this.appointmentDate = appointmentDate;
        this.createAt = createAt;
        this.category = category;
    }

    public static PlanDetailResponse from(Plan plan){
        return PlanDetailResponse.builder()
                .author(plan.getAuthor().getName())
                .title(plan.getTitle())
                .content(plan.getContent())
                .location(plan.getLocation())
                .appointmentDate(plan.getAppointmentDate())
                .createAt(plan.getCreatedAt())
                .category(plan.getCategory())
                .build();
    }


}
