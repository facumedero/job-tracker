package com.facumedero.jobtracker.web;

import com.facumedero.jobtracker.domain.ApplicationStatus;
import com.facumedero.jobtracker.dto.JobApplicationRequest;
import com.facumedero.jobtracker.dto.JobApplicationResponse;
import com.facumedero.jobtracker.dto.StatusUpdateRequest;
import com.facumedero.jobtracker.service.JobApplicationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/applications")
public class JobApplicationController {

    private final JobApplicationService service;

    public JobApplicationController(JobApplicationService service) {
        this.service = service;
    }

    @GetMapping
    public List<JobApplicationResponse> list(@RequestParam(required = false) ApplicationStatus status) {
        return service.findAll(status);
    }

    @GetMapping("/{id}")
    public JobApplicationResponse get(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<JobApplicationResponse> create(@Valid @RequestBody JobApplicationRequest request) {
        JobApplicationResponse created = service.create(request);
        return ResponseEntity.created(URI.create("/api/applications/" + created.id())).body(created);
    }

    @PutMapping("/{id}")
    public JobApplicationResponse update(@PathVariable Long id, @Valid @RequestBody JobApplicationRequest request) {
        return service.update(id, request);
    }

    @PatchMapping("/{id}/status")
    public JobApplicationResponse updateStatus(@PathVariable Long id, @Valid @RequestBody StatusUpdateRequest request) {
        return service.updateStatus(id, request.status());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
