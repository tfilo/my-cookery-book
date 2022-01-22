CREATE TABLE cb_tag (
    id bigint NOT NULL,
    name character varying(80) NOT NULL
);

CREATE SEQUENCE cb_tag_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE ONLY cb_tag
    ADD CONSTRAINT cb_tag_pkey PRIMARY KEY (id);

ALTER TABLE ONLY cb_tag
    ADD CONSTRAINT uk_cb_tag_name UNIQUE (name);

CREATE TABLE cb_recipe_tag (
    recipe_id bigint NOT NULL,
    tag_id bigint NOT NULL
);

ALTER TABLE ONLY cb_recipe_tag
    ADD CONSTRAINT fk_cb_recipe_tag_tag_id FOREIGN KEY (tag_id) REFERENCES cb_tag(id);

ALTER TABLE ONLY cb_recipe_tag
    ADD CONSTRAINT fk_cb_recipe_tag_recipe_id FOREIGN KEY (recipe_id) REFERENCES cb_recipe(id);

ALTER TABLE cb_recipe
    ADD COLUMN category_id bigint;

UPDATE cb_recipe SET category_id = cb_recipe_category.category_id FROM cb_recipe_category WHERE cb_recipe_category.recipe_id = cb_recipe.id;

ALTER TABLE cb_recipe ALTER COLUMN category_id SET NOT NULL;