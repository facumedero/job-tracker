package com.facumedero.jobtracker.service;

import com.facumedero.jobtracker.domain.ApplicationStatus;
import com.facumedero.jobtracker.domain.JobApplication;
import com.facumedero.jobtracker.dto.JobApplicationRequest;
import com.facumedero.jobtracker.dto.JobApplicationResponse;
import com.facumedero.jobtracker.repository.JobApplicationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test unitario puro: sin contexto de Spring, con el repositorio simulado con Mockito.
 */
@ExtendWith(MockitoExtension.class)
class JobApplicationServiceTest {

    @Mock
    private JobApplicationRepository repository;

    @InjectMocks
    private JobApplicationService service;

    @Test
    void create_usesDefaultStatusAndDateWhenMissing() {
        when(repository.save(any(JobApplication.class))).thenAnswer(invocation -> invocation.getArgument(0));
        JobApplicationRequest request = new JobApplicationRequest("Acme", "Java Dev", null, null, null, null, null);

        JobApplicationResponse response = service.create(request);

        assertThat(response.status()).isEqualTo(ApplicationStatus.APPLIED);
        assertThat(response.appliedAt()).isEqualTo(LocalDate.now());
        assertThat(response.company()).isEqualTo("Acme");
    }

    @Test
    void findById_throwsNotFoundWhenMissing() {
        when(repository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void updateStatus_changesTheStatus() {
        JobApplication existing = new JobApplication("Acme", "Java Dev", ApplicationStatus.APPLIED, LocalDate.of(2026, 11, 1));
        when(repository.findById(1L)).thenReturn(Optional.of(existing));

        JobApplicationResponse response = service.updateStatus(1L, ApplicationStatus.TECHNICAL);

        assertThat(response.status()).isEqualTo(ApplicationStatus.TECHNICAL);
    }

    @Test
    void delete_throwsNotFoundAndDoesNotDeleteWhenMissing() {
        when(repository.existsById(5L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(5L)).isInstanceOf(NotFoundException.class);

        verify(repository, never()).deleteById(any());
    }
}
