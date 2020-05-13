-- pg_dumpall -O -x -s --inserts --no-comments -l recipes -U user   > dump.sql

CREATE TABLE public.cb_category (
    id bigint NOT NULL,
    name character varying(255) NOT NULL
);

CREATE SEQUENCE public.cb_category_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.cb_ingredient (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    sort_number integer,
    value integer NOT NULL,
    unit_id bigint NOT NULL,
    section_id bigint NOT NULL
);

CREATE SEQUENCE public.cb_ingredient_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.cb_picture (
    id bigint NOT NULL,
    data oid,
    name character varying(255),
    section_id bigint NOT NULL,
    recipe_id bigint NOT NULL
);

CREATE TABLE public.cb_recipe (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    description character varying(255),
    modified timestamp without time zone,
    title character varying(100) NOT NULL,
    creator_id bigint NOT NULL,
    modifier_id bigint
);

CREATE TABLE public.cb_recipe_category (
    recipe_id bigint NOT NULL,
    category_id bigint NOT NULL
);

CREATE TABLE public.cb_recipe_recipe (
    recipe_id bigint NOT NULL,
    associated_recipe_id bigint NOT NULL
);

CREATE TABLE public.cb_role (
    id bigint NOT NULL,
    name character varying(255) NOT NULL
);

CREATE SEQUENCE public.cb_role_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.cb_section (
    id bigint NOT NULL,
    method character varying(2000) NOT NULL,
    name character varying(255),
    sort_number integer,
    recipe_id bigint NOT NULL
);

CREATE SEQUENCE public.cb_section_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.cb_source (
    id bigint NOT NULL,
    url character varying(255) NOT NULL,
    recipe_id bigint NOT NULL
);

CREATE SEQUENCE public.cb_source_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.cb_unit (
    id bigint NOT NULL,
    abbreviation character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    unit_category_id bigint NOT NULL
);

CREATE TABLE public.cb_unit_category (
    id bigint NOT NULL,
    name character varying(255) NOT NULL
);

CREATE SEQUENCE public.cb_unit_category_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE SEQUENCE public.cb_unit_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

CREATE TABLE public.cb_user (
    id bigint NOT NULL,
    enabled boolean NOT NULL,
    first_name character varying(255),
    last_name character varying(255),
    password character varying(255) NOT NULL,
    username character varying(255) NOT NULL
);

CREATE TABLE public.cb_user_role (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);

CREATE SEQUENCE public.cb_user_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER TABLE ONLY public.cb_category
    ADD CONSTRAINT cb_category_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.cb_ingredient
    ADD CONSTRAINT cb_ingredient_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.cb_picture
    ADD CONSTRAINT cb_picture_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.cb_recipe
    ADD CONSTRAINT cb_recipe_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.cb_role
    ADD CONSTRAINT cb_role_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.cb_section
    ADD CONSTRAINT cb_section_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.cb_source
    ADD CONSTRAINT cb_source_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.cb_unit_category
    ADD CONSTRAINT cb_unit_category_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.cb_unit
    ADD CONSTRAINT cb_unit_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.cb_user
    ADD CONSTRAINT cb_user_pkey PRIMARY KEY (id);

ALTER TABLE ONLY public.cb_user_role
    ADD CONSTRAINT cb_user_role_pkey PRIMARY KEY (user_id, role_id);

ALTER TABLE ONLY public.cb_role
    ADD CONSTRAINT uk_am6w77st7nncoap8gm05jx456 UNIQUE (name);

ALTER TABLE ONLY public.cb_unit
    ADD CONSTRAINT ukmamh9vjvof98qbnx037y1ew9h UNIQUE (name, abbreviation);

ALTER TABLE ONLY public.cb_picture
    ADD CONSTRAINT fk6fy3p82gtfk59au09dyyub9na FOREIGN KEY (section_id) REFERENCES public.cb_section(id);

ALTER TABLE ONLY public.cb_user_role
    ADD CONSTRAINT fk7gk8v6sw7k1h7e2cepo2jmhhp FOREIGN KEY (role_id) REFERENCES public.cb_role(id);

ALTER TABLE ONLY public.cb_recipe_recipe
    ADD CONSTRAINT fk8jn96ajm5ca878s49ic9h9qr6 FOREIGN KEY (recipe_id) REFERENCES public.cb_recipe(id);

ALTER TABLE ONLY public.cb_ingredient
    ADD CONSTRAINT fkblob62qr747viwc3yf6mernwi FOREIGN KEY (section_id) REFERENCES public.cb_section(id);

ALTER TABLE ONLY public.cb_picture
    ADD CONSTRAINT fkbm0f40htk2rloqs2m369bqp9s FOREIGN KEY (recipe_id) REFERENCES public.cb_recipe(id);

ALTER TABLE ONLY public.cb_recipe_category
    ADD CONSTRAINT fkbybwg1o0l8tn8bh79nwyg0j1x FOREIGN KEY (category_id) REFERENCES public.cb_category(id);

ALTER TABLE ONLY public.cb_recipe_category
    ADD CONSTRAINT fkd6mcf6ijgeu1v65h9adegr0vc FOREIGN KEY (recipe_id) REFERENCES public.cb_recipe(id);

ALTER TABLE ONLY public.cb_unit
    ADD CONSTRAINT fkfu4rab2hovq1ida1trqfahmw3 FOREIGN KEY (unit_category_id) REFERENCES public.cb_unit_category(id);

ALTER TABLE ONLY public.cb_recipe
    ADD CONSTRAINT fkjdl6vh0bgms6vr63499l0drvg FOREIGN KEY (modifier_id) REFERENCES public.cb_user(id);

ALTER TABLE ONLY public.cb_user_role
    ADD CONSTRAINT fkl6ddy6j2ki5q0seroge5590rt FOREIGN KEY (user_id) REFERENCES public.cb_user(id);

ALTER TABLE ONLY public.cb_recipe
    ADD CONSTRAINT fkmrx4yyywcppxw41olxv74wo8v FOREIGN KEY (creator_id) REFERENCES public.cb_user(id);

ALTER TABLE ONLY public.cb_source
    ADD CONSTRAINT fko0nqrrbf88pcnpcygxc3a3v08 FOREIGN KEY (recipe_id) REFERENCES public.cb_recipe(id);

ALTER TABLE ONLY public.cb_recipe_recipe
    ADD CONSTRAINT fkoqvv3g34ipbqs56tpv0dvmbd5 FOREIGN KEY (associated_recipe_id) REFERENCES public.cb_recipe(id);

ALTER TABLE ONLY public.cb_section
    ADD CONSTRAINT fkqhtsisrcjovh8jteob7hik41u FOREIGN KEY (recipe_id) REFERENCES public.cb_recipe(id);

ALTER TABLE ONLY public.cb_ingredient
    ADD CONSTRAINT fkr0w899l0hvi1fp3yqq51licjr FOREIGN KEY (unit_id) REFERENCES public.cb_unit(id);
