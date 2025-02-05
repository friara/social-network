-- INSERT INTO Users (id, login, password, role_id, lastName, firstName, patronymic, appointment, birthday, avatarPath) VALUES (01, 'testuser', 'testpassword', 'user', 'aaa', 'aaa', 'aaa', 'aaa', '09.09.2003', ' ');

-- Вставка данных в таблицу roles
INSERT INTO roles (name)
VALUES ('USER');

-- Вставка данных в таблицу users
INSERT INTO users (login, password, role_id, last_name, first_name, patronymic, appointment, birthday, avatar_path)
VALUES ('testuser', 'testpassword', '1', 'Doe', 'John', 'A.', 'Developer', '2000-01-01', 'path/to/avatar.jpg');


