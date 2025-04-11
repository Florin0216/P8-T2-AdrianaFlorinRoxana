CREATE TABLE cases_agents
(
    agent_id BIGINT NOT NULL,
    case_id  BIGINT NOT NULL
);

ALTER TABLE cases_agents
    ADD CONSTRAINT fk_casage_on_agents FOREIGN KEY (agent_id) REFERENCES agents (agent_id);

ALTER TABLE cases_agents
    ADD CONSTRAINT fk_casage_on_case_files FOREIGN KEY (case_id) REFERENCES case_files (case_id);