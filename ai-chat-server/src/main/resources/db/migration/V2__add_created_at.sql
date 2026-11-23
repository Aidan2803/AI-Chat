ALTER TABLE conversation ADD COLUMN IF NOT EXISTS created_at timestamp with time zone;
UPDATE conversation SET created_at = now() WHERE created_at IS NULL;
ALTER TABLE conversation ALTER COLUMN created_at SET NOT NULL;

ALTER TABLE message ADD COLUMN IF NOT EXISTS created_at timestamp with time zone;
UPDATE message SET created_at = now() WHERE created_at IS NULL;
ALTER TABLE message ALTER COLUMN created_at SET NOT NULL;