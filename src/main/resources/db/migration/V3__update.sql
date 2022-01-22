ALTER TABLE cb_recipe
    ADD COLUMN title_search character varying(80);

ALTER TABLE cb_unit
    ADD COLUMN value_required boolean;

UPDATE cb_recipe SET title_search = title;
UPDATE cb_unit SET value_required = true;

ALTER TABLE cb_recipe ALTER COLUMN title_search SET NOT NULL;
ALTER TABLE cb_unit ALTER COLUMN value_required SET NOT NULL;
ALTER TABLE cb_ingredient ALTER COLUMN value DROP NOT NULL;
