ALTER TABLE chats
    DROP COLUMN is_recurring;

ALTER TABLE chats
    ADD is_recurring VARCHAR(255);