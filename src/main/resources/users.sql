INSERT INTO kata.role (name) VALUES ('ROLE_USER');
INSERT INTO kata.role (name) VALUES ('ROLE_ADMIN');

INSERT INTO kata.users (email, name, last_Name, age, password) VALUES ( 'user@mail.com', 'user', 'user', 22, '$2a$12$r/UgXbidDftGfwuZlXVd4eECHJcp1bviAVvcsQPAp0HVtwEBFU3sq');
INSERT INTO kata.users (email, name, last_Name, age, password) VALUES ( 'admin@mail.com', 'admin', 'admin', 33,'$2a$12$VtePqSbEBG0gNKa99GxTJOZxqetVIvuf/vKxHkk7tblicWhLljW5K');


INSERT INTO kata.users_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO kata.users_roles (user_id, role_id) VALUES (2, 1);
INSERT INTO kata.users_roles (user_id, role_id) VALUES (2, 2);