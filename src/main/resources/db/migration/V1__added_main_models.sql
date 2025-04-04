CREATE TABLE agents
(
    agent_id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    first_name   VARCHAR(20)                             NOT NULL,
    last_name    VARCHAR(20)                             NOT NULL,
    gender       VARCHAR(1)                              NOT NULL,
    rank         VARCHAR(20)                             NOT NULL,
    status       VARCHAR(20)                             NOT NULL,
    email        VARCHAR(30)                             NOT NULL,
    phone_number INTEGER                                 NOT NULL,
    dob          date                                    NOT NULL,
    user_id      BIGINT,
    station_id   BIGINT,
    CONSTRAINT pk_agents PRIMARY KEY (agent_id)
);

CREATE TABLE case_evidences
(
    evidence_id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    evidence_name VARCHAR(20)                             NOT NULL,
    evidence_path VARCHAR(255)                            NOT NULL,
    uploaded_at   time WITHOUT TIME ZONE,
    case_id       BIGINT,
    CONSTRAINT pk_caseevidences PRIMARY KEY (evidence_id)
);

CREATE TABLE case_files
(
    case_id          BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    case_name        VARCHAR(40)                             NOT NULL,
    case_category    VARCHAR(20)                             NOT NULL,
    case_description VARCHAR(150)                            NOT NULL,
    status           VARCHAR(10)                             NOT NULL,
    created_at       TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    updated_at       TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    station_id       BIGINT,
    agent_id         BIGINT,
    CONSTRAINT pk_casefiles PRIMARY KEY (case_id)
);

CREATE TABLE resources
(
    resource_id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    resource_name   VARCHAR(20)                             NOT NULL,
    resource_type   VARCHAR(20)                             NOT NULL,
    status          VARCHAR(20)                             NOT NULL,
    maintenace_date date                                    NOT NULL,
    maintenace_time time WITHOUT TIME ZONE                  NOT NULL,
    agent_id        BIGINT,
    station_id      BIGINT,
    CONSTRAINT pk_resources PRIMARY KEY (resource_id)
);

CREATE TABLE roles
(
    role_id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    role_name VARCHAR(20)                             NOT NULL,
    CONSTRAINT pk_roles PRIMARY KEY (role_id)
);

CREATE TABLE stations
(
    station_id      BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    station_name    VARCHAR(30)                             NOT NULL,
    station_email   VARCHAR(30)                             NOT NULL,
    station_phone   INTEGER                                 NOT NULL,
    station_address VARCHAR(40)                             NOT NULL,
    latitude        DOUBLE PRECISION                        NOT NULL,
    longitude       DOUBLE PRECISION                        NOT NULL,
    CONSTRAINT pk_stations PRIMARY KEY (station_id)
);

CREATE TABLE suspects
(
    suspect_id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    first_name VARCHAR(20)                             NOT NULL,
    last_name  VARCHAR(20)                             NOT NULL,
    dob        date                                    NOT NULL,
    CONSTRAINT pk_suspects PRIMARY KEY (suspect_id)
);

CREATE TABLE users
(
    user_id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    username VARCHAR(20)                             NOT NULL,
    password VARCHAR(20)                             NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (user_id)
);

CREATE TABLE users_roles
(
    role_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL
);

ALTER TABLE agents
    ADD CONSTRAINT uc_agents_user UNIQUE (user_id);

ALTER TABLE agents
    ADD CONSTRAINT FK_AGENTS_ON_STATION FOREIGN KEY (station_id) REFERENCES stations (station_id);

ALTER TABLE agents
    ADD CONSTRAINT FK_AGENTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (user_id);

ALTER TABLE case_evidences
    ADD CONSTRAINT FK_CASEEVIDENCES_ON_CASE FOREIGN KEY (case_id) REFERENCES case_files (case_id);

ALTER TABLE case_files
    ADD CONSTRAINT FK_CASEFILES_ON_AGENT FOREIGN KEY (agent_id) REFERENCES agents (agent_id);

ALTER TABLE case_files
    ADD CONSTRAINT FK_CASEFILES_ON_STATION FOREIGN KEY (station_id) REFERENCES stations (station_id);

ALTER TABLE resources
    ADD CONSTRAINT FK_RESOURCES_ON_AGENT FOREIGN KEY (agent_id) REFERENCES agents (agent_id);

ALTER TABLE resources
    ADD CONSTRAINT FK_RESOURCES_ON_STATION FOREIGN KEY (station_id) REFERENCES stations (station_id);

ALTER TABLE users_roles
    ADD CONSTRAINT fk_userol_on_roles FOREIGN KEY (role_id) REFERENCES roles (role_id);

ALTER TABLE users_roles
    ADD CONSTRAINT fk_userol_on_users FOREIGN KEY (user_id) REFERENCES users (user_id);