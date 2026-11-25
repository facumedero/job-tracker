package com.facumedero.jobtracker.dto;

import com.facumedero.jobtracker.domain.ApplicationStatus;
import com.facumedero.jobtracker.domain.JobApplication;

import java.time.Instant;
import java.time.LocalDate;

public record JobApplicationResponse(
        Long id,
        String company,
        String title,
        ApplicationStatus status,
        String source,
        String url,
        String notes,
        LocalDate appliedAt,
        Instant createdAt
) {

    public static JobApplicationResponse from(JobApplication entity) {
        return new JobApplicationResponse(
                entity.getId(),
                entity.getCompany(),
                entity.getTitle(),
                entity.getStatus(),
                entity.getSource(),
                entity.getUrl(),
                entity.getNotes(),
                entity.getAppliedAt(),
                entity.getCreatedAt());
    }
}
