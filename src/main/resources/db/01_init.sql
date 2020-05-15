-- pg_dumpall -O -x -s --inserts --no-comments -l recipes -U user   > dump.sql

CREATE TABLE public.cb_category (
    id bigint NOT NULL,
    name character varying(255) NOT NULL
);


--
-- Name: cb_category_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.cb_category_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: cb_ingredient; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cb_ingredient (
    id bigint NOT NULL,
    name character varying(255) NOT NULL,
    sort_number integer,
    value real NOT NULL,
    unit_id bigint NOT NULL,
    section_id bigint NOT NULL
);


--
-- Name: cb_ingredient_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.cb_ingredient_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: cb_picture; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cb_picture (
    id bigint NOT NULL,
    data oid NOT NULL,
    name character varying(255),
    section_id bigint,
    recipe_id bigint
);


--
-- Name: cb_recipe; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cb_recipe (
    id bigint NOT NULL,
    created timestamp without time zone NOT NULL,
    description character varying(255),
    modified timestamp without time zone,
    title character varying(80) NOT NULL,
    creator_id bigint NOT NULL,
    modifier_id bigint
);


--
-- Name: cb_recipe_category; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cb_recipe_category (
    recipe_id bigint NOT NULL,
    category_id bigint NOT NULL
);


--
-- Name: cb_recipe_recipe; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cb_recipe_recipe (
    recipe_id bigint NOT NULL,
    associated_recipe_id bigint NOT NULL
);


--
-- Name: cb_role; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cb_role (
    id bigint NOT NULL,
    name character varying(255) NOT NULL
);


--
-- Name: cb_role_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.cb_role_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: cb_section; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cb_section (
    id bigint NOT NULL,
    method jsonb NOT NULL,
    name character varying(255),
    sort_number integer,
    recipe_id bigint NOT NULL
);


--
-- Name: cb_section_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.cb_section_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: cb_source; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cb_source (
    id bigint NOT NULL,
    url character varying(1000) NOT NULL,
    recipe_id bigint NOT NULL
);


--
-- Name: cb_source_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.cb_source_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: cb_unit; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cb_unit (
    id bigint NOT NULL,
    abbreviation character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    unit_category_id bigint NOT NULL
);


--
-- Name: cb_unit_category; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cb_unit_category (
    id bigint NOT NULL,
    name character varying(255) NOT NULL
);


--
-- Name: cb_unit_category_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.cb_unit_category_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: cb_unit_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.cb_unit_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: cb_user; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cb_user (
    id bigint NOT NULL,
    enabled boolean NOT NULL,
    first_name character varying(255),
    last_name character varying(255),
    password character varying(255) NOT NULL,
    username character varying(255) NOT NULL
);


--
-- Name: cb_user_role; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.cb_user_role (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);


--
-- Name: cb_user_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.cb_user_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: cb_category cb_category_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cb_category
    ADD CONSTRAINT cb_category_pkey PRIMARY KEY (id);


--
-- Name: cb_ingredient cb_ingredient_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cb_ingredient
    ADD CONSTRAINT cb_ingredient_pkey PRIMARY KEY (id);


--
-- Name: cb_picture cb_picture_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cb_picture
    ADD CONSTRAINT cb_picture_pkey PRIMARY KEY (id);


--
-- Name: cb_recipe cb_recipe_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cb_recipe
    ADD CONSTRAINT cb_recipe_pkey PRIMARY KEY (id);


--
-- Name: cb_role cb_role_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cb_role
    ADD CONSTRAINT cb_role_pkey PRIMARY KEY (id);


--
-- Name: cb_section cb_section_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cb_section
    ADD CONSTRAINT cb_section_pkey PRIMARY KEY (id);


--
-- Name: cb_source cb_source_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cb_source
    ADD CONSTRAINT cb_source_pkey PRIMARY KEY (id);


--
-- Name: cb_unit_category cb_unit_category_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cb_unit_category
    ADD CONSTRAINT cb_unit_category_pkey PRIMARY KEY (id);


--
-- Name: cb_unit cb_unit_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cb_unit
    ADD CONSTRAINT cb_unit_pkey PRIMARY KEY (id);


--
-- Name: cb_user cb_user_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cb_user
    ADD CONSTRAINT cb_user_pkey PRIMARY KEY (id);


--
-- Name: cb_user_role cb_user_role_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cb_user_role
    ADD CONSTRAINT cb_user_role_pkey PRIMARY KEY (user_id, role_id);


--
-- Name: cb_role uk_am6w77st7nncoap8gm05jx456; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cb_role
    ADD CONSTRAINT uk_am6w77st7nncoap8gm05jx456 UNIQUE (name);


--
-- Name: cb_unit ukmamh9vjvof98qbnx037y1ew9h; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cb_unit
    ADD CONSTRAINT ukmamh9vjvof98qbnx037y1ew9h UNIQUE (name, abbreviation);


--
-- Name: cb_picture fk6fy3p82gtfk59au09dyyub9na; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cb_picture
    ADD CONSTRAINT fk6fy3p82gtfk59au09dyyub9na FOREIGN KEY (section_id) REFERENCES public.cb_section(id);


--
-- Name: cb_user_role fk7gk8v6sw7k1h7e2cepo2jmhhp; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cb_user_role
    ADD CONSTRAINT fk7gk8v6sw7k1h7e2cepo2jmhhp FOREIGN KEY (role_id) REFERENCES public.cb_role(id);


--
-- Name: cb_recipe_recipe fk8jn96ajm5ca878s49ic9h9qr6; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cb_recipe_recipe
    ADD CONSTRAINT fk8jn96ajm5ca878s49ic9h9qr6 FOREIGN KEY (recipe_id) REFERENCES public.cb_recipe(id);


--
-- Name: cb_ingredient fkblob62qr747viwc3yf6mernwi; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cb_ingredient
    ADD CONSTRAINT fkblob62qr747viwc3yf6mernwi FOREIGN KEY (section_id) REFERENCES public.cb_section(id);


--
-- Name: cb_picture fkbm0f40htk2rloqs2m369bqp9s; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cb_picture
    ADD CONSTRAINT fkbm0f40htk2rloqs2m369bqp9s FOREIGN KEY (recipe_id) REFERENCES public.cb_recipe(id);


--
-- Name: cb_recipe_category fkbybwg1o0l8tn8bh79nwyg0j1x; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cb_recipe_category
    ADD CONSTRAINT fkbybwg1o0l8tn8bh79nwyg0j1x FOREIGN KEY (category_id) REFERENCES public.cb_category(id);


--
-- Name: cb_recipe_category fkd6mcf6ijgeu1v65h9adegr0vc; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cb_recipe_category
    ADD CONSTRAINT fkd6mcf6ijgeu1v65h9adegr0vc FOREIGN KEY (recipe_id) REFERENCES public.cb_recipe(id);


--
-- Name: cb_unit fkfu4rab2hovq1ida1trqfahmw3; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cb_unit
    ADD CONSTRAINT fkfu4rab2hovq1ida1trqfahmw3 FOREIGN KEY (unit_category_id) REFERENCES public.cb_unit_category(id);


--
-- Name: cb_recipe fkjdl6vh0bgms6vr63499l0drvg; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cb_recipe
    ADD CONSTRAINT fkjdl6vh0bgms6vr63499l0drvg FOREIGN KEY (modifier_id) REFERENCES public.cb_user(id);


--
-- Name: cb_user_role fkl6ddy6j2ki5q0seroge5590rt; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cb_user_role
    ADD CONSTRAINT fkl6ddy6j2ki5q0seroge5590rt FOREIGN KEY (user_id) REFERENCES public.cb_user(id);


--
-- Name: cb_recipe fkmrx4yyywcppxw41olxv74wo8v; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cb_recipe
    ADD CONSTRAINT fkmrx4yyywcppxw41olxv74wo8v FOREIGN KEY (creator_id) REFERENCES public.cb_user(id);


--
-- Name: cb_source fko0nqrrbf88pcnpcygxc3a3v08; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cb_source
    ADD CONSTRAINT fko0nqrrbf88pcnpcygxc3a3v08 FOREIGN KEY (recipe_id) REFERENCES public.cb_recipe(id);


--
-- Name: cb_recipe_recipe fkoqvv3g34ipbqs56tpv0dvmbd5; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cb_recipe_recipe
    ADD CONSTRAINT fkoqvv3g34ipbqs56tpv0dvmbd5 FOREIGN KEY (associated_recipe_id) REFERENCES public.cb_recipe(id);


--
-- Name: cb_section fkqhtsisrcjovh8jteob7hik41u; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cb_section
    ADD CONSTRAINT fkqhtsisrcjovh8jteob7hik41u FOREIGN KEY (recipe_id) REFERENCES public.cb_recipe(id);


--
-- Name: cb_ingredient fkr0w899l0hvi1fp3yqq51licjr; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cb_ingredient
    ADD CONSTRAINT fkr0w899l0hvi1fp3yqq51licjr FOREIGN KEY (unit_id) REFERENCES public.cb_unit(id);


--
-- PostgreSQL database dump complete
--

--
-- PostgreSQL database cluster dump complete
--
