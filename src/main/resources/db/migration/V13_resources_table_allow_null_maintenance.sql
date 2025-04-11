ALTER TABLE resources
    ALTER COLUMN maintenace_date DROP NOT NULL;

ALTER TABLE resources
    ALTER COLUMN maintenace_time DROP NOT NULL;
