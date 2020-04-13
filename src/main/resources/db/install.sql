-- user/user     echo -n "user" | shasum -a 256
-- admin/user 
INSERT INTO cb_user (id, username, password, enabled) VALUES (1, 'admin', '$2a$10$y5c8a9Ri8Gre3653AjDjtuZJhtRG9mux2YVm/lHpXw19FIfTK3/Tu', true);
INSERT INTO cb_user (id, username, password, enabled) VALUES (2, 'user', '$2a$10$E.nhuDgeQfFpI74XzI7j/ejt7TnANbe011iVO2tw09xEeTMM.LNb.', true);

INSERT INTO cb_role (id, name) VALUES (1, 'ROLE_ADMIN');
INSERT INTO cb_role (id, name) VALUES (2, 'ROLE_USER');

INSERT INTO cb_user_role (user_id, role_id) VALUES (1, 1);
INSERT INTO cb_user_role (user_id, role_id) VALUES (2, 2);