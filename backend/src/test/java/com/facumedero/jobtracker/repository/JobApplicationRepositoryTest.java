package com.facumedero.jobtracker.repository;

import com.facumedero.jobtracker.domain.ApplicationStatus;
import com.facumedero.jobtracker.domain.JobApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test de la capa JPA con una base H2 en memoria.
 * En producción el esquema lo crea Flyway sobre PostgreSQL; acá Hibernate lo genera a partir de las entidades.
 */
@DataJpaTest(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
class JobApplicationRepositoryTest {

    @Autowired
    private JobApplicationRepository repository;

    @Test
    void findByStatus_returnsOnlyMatchingApplicationsNewestFirst() {
        repository.save(new JobApplication("Acme", "Java Dev", ApplicationStatus.APPLIED, LocalDate.of(2026, 11, 1)));
        repository.save(new JobApplication("Globex", "Full Stack", ApplicationStatus.APPLIED, LocalDate.of(2026, 11, 10)));
        repository.save(new JobApplication("Initech", "Backend", ApplicationStatus.REJECTED, LocalDate.of(2026, 11, 5)));

        List<JobApplication> result = repository.findByStatusOrderByAppliedAtDescIdDesc(ApplicationStatus.APPLIED);

        assertThat(result)
                .extracting(JobApplication::getCompany)
                .containsExactly("Globex", "Acme");
    }

    @Test
    void findAll_ordersByAppliedDateDescending() {
        repository.save(new JobApplication("Acme", "Java Dev", ApplicationStatus.APPLIED, LocalDate.of(2026, 11, 1)));
        repository.save(new JobApplication("Globex", "Full Stack", ApplicationStatus.OFFER, LocalDate.of(2026, 11, 10)));

        List<JobApplication> result = repository.findAllByOrderByAppliedAtDescIdDesc();

        assertThat(result)
                .extracting(JobApplication::getCompany)
                .containsExactly("Globex", "Acme");
    }

    @Test
    void save_assignsIdAndCreatedAt() {
        JobApplication saved = repository.save(
                new JobApplication("Acme", "Java Dev", ApplicationStatus.APPLIED, LocalDate.of(2026, 11, 1)));

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
    }
}
