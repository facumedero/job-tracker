package com.facumedero.jobtracker.dto;

import com.facumedero.jobtracker.domain.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Cuerpo de las peticiones de alta y edición.
 * Si {@code status} o {@code appliedAt} no vienen, se usan valores por defecto en el servicio.
 */
public record JobApplicationRequest(
        @NotBlank @Size(max = 120) String company,
        @NotBlank @Size(max = 120) String title,
        ApplicationStatus status,
        @Size(max = 60) String source,
        @Size(max = 500) String url,
        @Size(max = 2000) String notes,
        LocalDate appliedAt
) {
}
