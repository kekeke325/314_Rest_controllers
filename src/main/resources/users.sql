INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

INSERT INTO users (age, email, name, last_name, password) VALUES (42, 'user@mail.com', 'user', 'user', '$2a$12$r/UgXbidDftGfwuZlXVd4eECHJcp1bviAVvcsQPAp0HVtwEBFU3sq');
INSERT INTO users (age, email, name, last_name, password) VALUES (33, 'admin@mail.com', 'admin', 'admin', '$2a$12$VtePqSbEBG0gNKa99GxTJOZxqetVIvuf/vKxHkk7tblicWhLljW5K');

INSERT INTO users_roles (user_id, role_id) VALUES (1, 1);
INSERT INTO users_roles (user_id, role_id) VALUES (2, 1);
INSERT INTO users_roles (user_id, role_id) VALUES (2, 2);

