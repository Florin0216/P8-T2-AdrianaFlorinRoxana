ALTER TABLE agents_chats
    DROP CONSTRAINT fk_agecha_on_agents;

ALTER TABLE agents_chats
    DROP CONSTRAINT fk_agecha_on_chats;

ALTER TABLE messages
    DROP CONSTRAINT fk_messages_on_agent;

CREATE TABLE users_chats
(
    chat_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL
);

ALTER TABLE messages
    ADD user_id BIGINT;

ALTER TABLE messages
    ADD CONSTRAINT FK_MESSAGES_ON_USER FOREIGN KEY (user_id) REFERENCES users (user_id);

ALTER TABLE users_chats
    ADD CONSTRAINT fk_usecha_on_chats FOREIGN KEY (chat_id) REFERENCES chats (chat_id);

ALTER TABLE users_chats
    ADD CONSTRAINT fk_usecha_on_users FOREIGN KEY (user_id) REFERENCES users (user_id);

DROP TABLE agents_chats CASCADE;

ALTER TABLE messages
    DROP COLUMN agent_id;