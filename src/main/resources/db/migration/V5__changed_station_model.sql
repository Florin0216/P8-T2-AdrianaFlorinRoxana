ALTER TABLE users
    ALTER COLUMN password TYPE VARCHAR(255) USING (password::VARCHAR(255));

ALTER TABLE stations
    ALTER COLUMN station_address TYPE VARCHAR(100) USING (station_address::VARCHAR(100));

ALTER TABLE stations
    ALTER COLUMN station_email TYPE VARCHAR(50) USING (station_email::VARCHAR(50));

ALTER TABLE stations
    ALTER COLUMN station_email DROP NOT NULL;

ALTER TABLE stations
    ALTER COLUMN station_name TYPE VARCHAR(50) USING (station_name::VARCHAR(50));

ALTER TABLE agents
    ALTER COLUMN status DROP NOT NULL;