-- user/user     echo -n "user" | shasum -a 256 -- neaktualny prikaz
-- admin/admin 
INSERT INTO cb_user (id, username, password, enabled) VALUES (1, 'admin', '$2a$10$y5c8a9Ri8Gre3653AjDjtuZJhtRG9mux2YVm/lHpXw19FIfTK3/Tu', true);
INSERT INTO cb_user (id, username, password, enabled) VALUES (2, 'user', '$2a$10$E.nhuDgeQfFpI74XzI7j/ejt7TnANbe011iVO2tw09xEeTMM.LNb.', true);
INSERT INTO cb_role (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO cb_role (id, name) VALUES (2, 'ROLE_USER');
INSERT INTO cb_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO cb_user_role (user_id, role_id) VALUES (2, 2);

-- pg_dumpall -O -x -a --inserts --no-comments -l recipes -U user   > dump.sql
INSERT INTO public.cb_category VALUES (1, 'Polievky');
INSERT INTO public.cb_category VALUES (2, 'Pečivo');
INSERT INTO public.cb_category VALUES (3, 'Hlavné jedlá');
INSERT INTO public.cb_category VALUES (4, 'Dezerty');
INSERT INTO public.cb_category VALUES (5, 'Prílohy');
INSERT INTO public.cb_unit_category VALUES (1, 'Hmotnosť');
INSERT INTO public.cb_unit_category VALUES (2, 'Objem');
INSERT INTO public.cb_unit_category VALUES (3, 'Množstvo');
INSERT INTO public.cb_unit VALUES (1, 'kg', 'Kilogram', 1);
INSERT INTO public.cb_unit VALUES (2, 'g', 'Gram', 1);
INSERT INTO public.cb_unit VALUES (3, 'dkg', 'Dekagram', 1);
INSERT INTO public.cb_unit VALUES (4, 'L', 'Liter', 2);
INSERT INTO public.cb_unit VALUES (5, 'ml', 'Mililiter', 2);
INSERT INTO public.cb_unit VALUES (6, 'dc', 'Deciliter', 2);
INSERT INTO public.cb_unit VALUES (7, 'cl', 'Centiliter', 2);
INSERT INTO public.cb_unit VALUES (8, 'ks', 'Kus', 3);
INSERT INTO public.cb_unit VALUES (9, 'p.l.', 'Polievková lyžica', 3);
INSERT INTO public.cb_unit VALUES (10, 'č.l.', 'Čajová lyžička', 3);
INSERT INTO public.cb_unit VALUES (11, 'šl.', 'Šálka', 3);
SELECT pg_catalog.setval('public.cb_category_seq', 5, true);
SELECT pg_catalog.setval('public.cb_ingredient_seq', 1, false);
SELECT pg_catalog.setval('public.cb_role_seq', 1, false);
SELECT pg_catalog.setval('public.cb_section_seq', 1, false);
SELECT pg_catalog.setval('public.cb_unit_category_seq', 3, true);
SELECT pg_catalog.setval('public.cb_unit_seq', 11, true);
SELECT pg_catalog.setval('public.cb_user_seq', 1, false);