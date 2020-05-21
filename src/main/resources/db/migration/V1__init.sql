-- pg_dumpall -O -x -s --inserts --no-comments -l recipes -U user   > dump.sql

CREATE TABLE cb_category (
    id bigint NOT NULL,
    name character varying(80) NOT NULL
);

CREATE SEQUENCE cb_category_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE cb_ingredient (
    id bigint NOT NULL,
    name character varying(80) NOT NULL,
    sort_number integer,
    value real NOT NULL,
    unit_id bigint NOT NULL,
    section_id bigint NOT NULL
);

CREATE SEQUENCE cb_ingredient_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE cb_picture (
    id bigint NOT NULL,
    data oid NOT NULL,
    thumbnail oid NOT NULL,
    title character varying(80),
    uploaded timestamp without time zone,
    recipe_id bigint
);

CREATE SEQUENCE cb_picture_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE cb_recipe (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    description character varying(160),
    modified timestamp without time zone,
    title character varying(80) NOT NULL,
    creator_id bigint NOT NULL,
    modifier_id bigint
);

CREATE SEQUENCE cb_recipe_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


CREATE TABLE cb_recipe_category (
    recipe_id bigint NOT NULL,
    category_id bigint NOT NULL
);

CREATE TABLE cb_recipe_recipe (
    recipe_id bigint NOT NULL,
    associated_recipe_id bigint NOT NULL
);

CREATE TABLE cb_role (
    id bigint NOT NULL,
    name character varying(255) NOT NULL
);

CREATE SEQUENCE cb_role_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE cb_section (
    id bigint NOT NULL,
    method jsonb NOT NULL,
    name character varying(80),
    sort_number integer,
    recipe_id bigint NOT NULL
);

CREATE SEQUENCE cb_section_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE cb_source (
    id bigint NOT NULL,
    url character varying(1000) NOT NULL,
    recipe_id bigint NOT NULL
);

CREATE SEQUENCE cb_source_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE cb_unit (
    id bigint NOT NULL,
    abbreviation character varying(80) NOT NULL,
    name character varying(20) NOT NULL,
    unit_category_id bigint NOT NULL
);

CREATE SEQUENCE cb_unit_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE cb_unit_category (
    id bigint NOT NULL,
    name character varying(80) NOT NULL
);

CREATE SEQUENCE cb_unit_category_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE cb_user (
    id bigint NOT NULL,
    enabled boolean NOT NULL,
    first_name character varying(50),
    last_name character varying(50),
    password character varying(255) NOT NULL,
    username character varying(25) NOT NULL
);

CREATE SEQUENCE cb_user_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE cb_user_role (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);

ALTER TABLE ONLY cb_category
    ADD CONSTRAINT cb_category_pkey PRIMARY KEY (id);

ALTER TABLE ONLY cb_ingredient
    ADD CONSTRAINT cb_ingredient_pkey PRIMARY KEY (id);

ALTER TABLE ONLY cb_picture
    ADD CONSTRAINT cb_picture_pkey PRIMARY KEY (id);

ALTER TABLE ONLY cb_recipe
    ADD CONSTRAINT cb_recipe_pkey PRIMARY KEY (id);

ALTER TABLE ONLY cb_role
    ADD CONSTRAINT cb_role_pkey PRIMARY KEY (id);

ALTER TABLE ONLY cb_section
    ADD CONSTRAINT cb_section_pkey PRIMARY KEY (id);

ALTER TABLE ONLY cb_source
    ADD CONSTRAINT cb_source_pkey PRIMARY KEY (id);

ALTER TABLE ONLY cb_unit_category
    ADD CONSTRAINT cb_unit_category_pkey PRIMARY KEY (id);

ALTER TABLE ONLY cb_unit
    ADD CONSTRAINT cb_unit_pkey PRIMARY KEY (id);

ALTER TABLE ONLY cb_user
    ADD CONSTRAINT cb_user_pkey PRIMARY KEY (id);

ALTER TABLE ONLY cb_user_role
    ADD CONSTRAINT cb_user_role_pkey PRIMARY KEY (user_id, role_id);

ALTER TABLE ONLY cb_role
    ADD CONSTRAINT uk_cb_role_name UNIQUE (name);

ALTER TABLE ONLY cb_unit
    ADD CONSTRAINT uk_cb_unit_name_abbreviation UNIQUE (name, abbreviation);

ALTER TABLE ONLY cb_category
    ADD CONSTRAINT uk_cb_category_name UNIQUE (name);

ALTER TABLE ONLY cb_unit_category
    ADD CONSTRAINT uk_cb_unit_category_name UNIQUE (name);

ALTER TABLE ONLY cb_user
    ADD CONSTRAINT uk_cb_user_username UNIQUE (username);

ALTER TABLE ONLY cb_user_role
    ADD CONSTRAINT fk_cb_user_role_role_id FOREIGN KEY (role_id) REFERENCES cb_role(id);

ALTER TABLE ONLY cb_recipe_recipe
    ADD CONSTRAINT fk_cb_recipe_recipe_recipe_id FOREIGN KEY (recipe_id) REFERENCES cb_recipe(id);

ALTER TABLE ONLY cb_ingredient
    ADD CONSTRAINT fk_cb_ingredient_section_id FOREIGN KEY (section_id) REFERENCES cb_section(id);

ALTER TABLE ONLY cb_picture
    ADD CONSTRAINT fk_cb_picture_recipe_id FOREIGN KEY (recipe_id) REFERENCES cb_recipe(id);

ALTER TABLE ONLY cb_recipe_category
    ADD CONSTRAINT fk_cb_recipe_category_category_id FOREIGN KEY (category_id) REFERENCES cb_category(id);

ALTER TABLE ONLY cb_recipe_category
    ADD CONSTRAINT fk_cb_recipe_category_recipe_id FOREIGN KEY (recipe_id) REFERENCES cb_recipe(id);

ALTER TABLE ONLY cb_unit
    ADD CONSTRAINT fk_cb_unit_unit_category_id FOREIGN KEY (unit_category_id) REFERENCES cb_unit_category(id);

ALTER TABLE ONLY cb_recipe
    ADD CONSTRAINT fk_cb_recipe_modifier_id FOREIGN KEY (modifier_id) REFERENCES cb_user(id);

ALTER TABLE ONLY cb_user_role
    ADD CONSTRAINT fk_cb_user_role_user_id FOREIGN KEY (user_id) REFERENCES cb_user(id);

ALTER TABLE ONLY cb_recipe
    ADD CONSTRAINT fk_cb_recipe_creator_id FOREIGN KEY (creator_id) REFERENCES cb_user(id);

ALTER TABLE ONLY cb_source
    ADD CONSTRAINT fk_cb_source_recipe_id FOREIGN KEY (recipe_id) REFERENCES cb_recipe(id);

ALTER TABLE ONLY cb_recipe_recipe
    ADD CONSTRAINT fk_cb_recipe_recipe_associated_recipe_id FOREIGN KEY (associated_recipe_id) REFERENCES cb_recipe(id);

ALTER TABLE ONLY cb_section
    ADD CONSTRAINT fk_cb_section_recipe_id FOREIGN KEY (recipe_id) REFERENCES cb_recipe(id);

ALTER TABLE ONLY cb_ingredient
    ADD CONSTRAINT fk_cb_ingredient_unit_id FOREIGN KEY (unit_id) REFERENCES cb_unit(id);