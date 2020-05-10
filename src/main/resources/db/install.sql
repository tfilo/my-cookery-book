create sequence cb_category_seq;

create sequence cb_ingredient_seq;

create sequence cb_role_seq;

create sequence cb_section_seq;

create sequence cb_unit_category_seq;

create sequence cb_unit_seq;

create sequence cb_user_seq;

create table cb_category
(
    id   bigint       not null,
    name varchar(255) not null,
    constraint cb_category_pkey
        primary key (id)
);

create table cb_role
(
    id   bigint       not null,
    name varchar(255) not null,
    constraint cb_role_pkey
        primary key (id),
    constraint uk_am6w77st7nncoap8gm05jx456
        unique (name)
);

create table cb_unit_category
(
    id   bigint       not null,
    name varchar(255) not null,
    constraint cb_unit_category_pkey
        primary key (id)
);

create table cb_unit
(
    id               bigint  not null,
    name             integer not null,
    unit_category_id bigint  not null,
    constraint cb_unit_pkey
        primary key (id),
    constraint fkfu4rab2hovq1ida1trqfahmw3
        foreign key (unit_category_id) references cb_unit_category
);

create table cb_user
(
    id         bigint       not null,
    enabled    boolean      not null,
    first_name varchar(255),
    last_name  varchar(255),
    password   varchar(255) not null,
    username   varchar(255) not null,
    constraint cb_user_pkey
        primary key (id)
);

create table cb_recipe
(
    id          bigint       not null,
    created     timestamp    not null,
    modified    timestamp,
    source      varchar(255),
    subtitle    varchar(255),
    title       varchar(255) not null,
    creator_id  bigint       not null,
    modifier_id bigint,
    constraint cb_recipe_pkey
        primary key (id),
    constraint fkmrx4yyywcppxw41olxv74wo8v
        foreign key (creator_id) references cb_user,
    constraint fkjdl6vh0bgms6vr63499l0drvg
        foreign key (modifier_id) references cb_user
);

create table cb_recipe_category
(
    recipe_id   bigint not null,
    category_id bigint not null,
    constraint fkbybwg1o0l8tn8bh79nwyg0j1x
        foreign key (category_id) references cb_category,
    constraint fkd6mcf6ijgeu1v65h9adegr0vc
        foreign key (recipe_id) references cb_recipe
);

create table cb_recipe_recipe
(
    recipe_id            bigint not null,
    associated_recipe_id bigint not null,
    constraint fkoqvv3g34ipbqs56tpv0dvmbd5
        foreign key (associated_recipe_id) references cb_recipe,
    constraint fk8jn96ajm5ca878s49ic9h9qr6
        foreign key (recipe_id) references cb_recipe
);

create table cb_section
(
    id          bigint        not null,
    method      varchar(2000) not null,
    name        varchar(255),
    sort_number integer,
    recipe_id   bigint        not null,
    constraint cb_section_pkey
        primary key (id),
    constraint fkqhtsisrcjovh8jteob7hik41u
        foreign key (recipe_id) references cb_recipe
);

create table cb_ingredient
(
    id          bigint       not null,
    name        varchar(255) not null,
    sort_number integer,
    value       integer      not null,
    unit_id     bigint       not null,
    section_id  bigint       not null,
    constraint cb_ingredient_pkey
        primary key (id),
    constraint fkr0w899l0hvi1fp3yqq51licjr
        foreign key (unit_id) references cb_unit,
    constraint fkblob62qr747viwc3yf6mernwi
        foreign key (section_id) references cb_section
);

create table cb_user_role
(
    user_id bigint not null,
    role_id bigint not null,
    constraint cb_user_role_pkey
        primary key (user_id, role_id),
    constraint fk7gk8v6sw7k1h7e2cepo2jmhhp
        foreign key (role_id) references cb_role,
    constraint fkl6ddy6j2ki5q0seroge5590rt
        foreign key (user_id) references cb_user
);

-- user/user     echo -n "user" | shasum -a 256 -- neaktualny prikaz
-- admin/admin 
INSERT INTO cb_user (id, username, password, enabled) VALUES (1, 'admin', '$2a$10$y5c8a9Ri8Gre3653AjDjtuZJhtRG9mux2YVm/lHpXw19FIfTK3/Tu', true);
INSERT INTO cb_user (id, username, password, enabled) VALUES (2, 'user', '$2a$10$E.nhuDgeQfFpI74XzI7j/ejt7TnANbe011iVO2tw09xEeTMM.LNb.', true);

INSERT INTO cb_role (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO cb_role (id, name) VALUES (2, 'ROLE_USER');

INSERT INTO cb_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO cb_user_role (user_id, role_id) VALUES (2, 2);
