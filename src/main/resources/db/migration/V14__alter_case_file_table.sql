ALTER TABLE case_files
    ALTER COLUMN case_description TYPE VARCHAR(255) USING (case_description::VARCHAR(255));

ALTER TABLE case_files
    ALTER COLUMN status TYPE VARCHAR(20) USING (status::VARCHAR(20));

ALTER TABLE case_files
    ALTER COLUMN updated_at DROP NOT NULL;