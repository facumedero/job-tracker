package com.facumedero.jobtracker.dto;

import com.facumedero.jobtracker.domain.ApplicationStatus;
import jakarta.validation.constraints.NotNull;

public record StatusUpdateRequest(@NotNull ApplicationStatus status) {
}
