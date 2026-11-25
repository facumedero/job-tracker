package com.facumedero.jobtracker.repository;

import com.facumedero.jobtracker.domain.ApplicationStatus;
import com.facumedero.jobtracker.domain.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data genera la implementación en tiempo de ejecución a partir de los nombres de los métodos.
 */
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {

    List<JobApplication> findAllByOrderByAppliedAtDescIdDesc();

    List<JobApplication> findByStatusOrderByAppliedAtDescIdDesc(ApplicationStatus status);
}
