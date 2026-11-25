package com.facumedero.jobtracker.service;

import com.facumedero.jobtracker.domain.ApplicationStatus;
import com.facumedero.jobtracker.domain.JobApplication;
import com.facumedero.jobtracker.dto.JobApplicationRequest;
import com.facumedero.jobtracker.dto.JobApplicationResponse;
import com.facumedero.jobtracker.repository.JobApplicationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Lógica de negocio. Spring inyecta el repositorio por constructor (inyección de dependencias).
 * Por defecto las operaciones son de solo lectura; las que escriben se marcan con @Transactional.
 */
@Service
@Transactional(readOnly = true)
public class JobApplicationService {

    private final JobApplicationRepository repository;

    public JobApplicationService(JobApplicationRepository repository) {
        this.repository = repository;
    }

    public List<JobApplicationResponse> findAll(ApplicationStatus status) {
        List<JobApplication> result = (status == null)
                ? repository.findAllByOrderByAppliedAtDescIdDesc()
                : repository.findByStatusOrderByAppliedAtDescIdDesc(status);
        return result.stream().map(JobApplicationResponse::from).toList();
    }

    public JobApplicationResponse findById(Long id) {
        return JobApplicationResponse.from(getOrThrow(id));
    }

    @Transactional
    public JobApplicationResponse create(JobApplicationRequest request) {
        JobApplication entity = new JobApplication(
                request.company(),
                request.title(),
                request.status() != null ? request.status() : ApplicationStatus.APPLIED,
                request.appliedAt() != null ? request.appliedAt() : LocalDate.now());
        entity.setSource(request.source());
        entity.setUrl(request.url());
        entity.setNotes(request.notes());
        return JobApplicationResponse.from(repository.save(entity));
    }

    @Transactional
    public JobApplicationResponse update(Long id, JobApplicationRequest request) {
        JobApplication entity = getOrThrow(id);
        entity.setCompany(request.company());
        entity.setTitle(request.title());
        if (request.status() != null) {
            entity.setStatus(request.status());
        }
        if (request.appliedAt() != null) {
            entity.setAppliedAt(request.appliedAt());
        }
        entity.setSource(request.source());
        entity.setUrl(request.url());
        entity.setNotes(request.notes());
        // La entidad está "managed": al cerrar la transacción JPA detecta los cambios y hace el UPDATE.
        return JobApplicationResponse.from(entity);
    }

    @Transactional
    public JobApplicationResponse updateStatus(Long id, ApplicationStatus status) {
        JobApplication entity = getOrThrow(id);
        entity.setStatus(status);
        return JobApplicationResponse.from(entity);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new NotFoundException("No existe la postulación con id " + id);
        }
        repository.deleteById(id);
    }

    private JobApplication getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("No existe la postulación con id " + id));
    }
}
