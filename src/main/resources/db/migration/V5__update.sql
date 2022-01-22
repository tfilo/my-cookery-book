ALTER TABLE cb_section 
DROP COLUMN method;

ALTER TABLE cb_section 
RENAME COLUMN method_text TO method;