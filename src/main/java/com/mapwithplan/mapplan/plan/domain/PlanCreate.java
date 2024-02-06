package com.mapwithplan.mapplan.plan.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mapwithplan.mapplan.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PlanCreate {

    private String title;

    private LocalDateTime appointmentDate;

    private String content;

    private String location;

    private String category;

    @Builder
    public PlanCreate(@JsonProperty("title") String title,
                      @JsonProperty("appointmentDate") LocalDateTime appointmentDate,
                      @JsonProperty("content") String content,
                      @JsonProperty("location") String location,
                      @JsonProperty("category") String category) {
        this.title = title;
        this.appointmentDate = appointmentDate;
        this.content = content;
        this.location = location;
        this.category = category;
    }
}
