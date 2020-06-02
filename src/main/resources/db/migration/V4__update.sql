ALTER TABLE cb_section
    ADD COLUMN method_text TEXT;

UPDATE cb_section SET method_text = '';

ALTER TABLE cb_section ALTER COLUMN method_text SET NOT NULL;

