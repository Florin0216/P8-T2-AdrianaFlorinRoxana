ALTER TABLE case_files
    DROP CONSTRAINT fk_casefiles_on_agent;

ALTER TABLE case_files
    ADD user_id BIGINT;

ALTER TABLE case_files
    ADD CONSTRAINT FK_CASE_FILES_ON_USER FOREIGN KEY (user_id) REFERENCES users (user_id);

ALTER TABLE case_files
    DROP COLUMN agent_id;