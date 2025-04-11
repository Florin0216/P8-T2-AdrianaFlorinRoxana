ALTER TABLE chats
    ADD is_recurring BOOLEAN;

ALTER TABLE chats
    ADD recurrence VARCHAR(255);