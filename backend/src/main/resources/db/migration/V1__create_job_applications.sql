CREATE TABLE job_applications (
    id          BIGSERIAL PRIMARY KEY,
    company     VARCHAR(120)  NOT NULL,
    title       VARCHAR(120)  NOT NULL,
    status      VARCHAR(20)   NOT NULL,
    source      VARCHAR(60),
    url         VARCHAR(500),
    notes       VARCHAR(2000),
    applied_at  DATE          NOT NULL,
    created_at  TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

CREATE INDEX idx_job_applications_status ON job_applications (status);
