ALTER TABLE conversation ADD COLUMN IF NOT EXISTS last_updated_at timestamp with time zone;
UPDATE conversation SET last_updated_at = now() WHERE last_updated_at IS NULL;
ALTER TABLE conversation ALTER COLUMN last_updated_at SET NOT NULL;
