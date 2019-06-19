DELETE FROM meals;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
  ('User', 'user@yandex.ru', 'password'),
  ('Admin', 'admin@gmail.com', 'admin');

INSERT INTO user_roles (role, user_id) VALUES
  ('ROLE_USER', 100000),
  ('ROLE_ADMIN', 100001);

INSERT INTO meals (user_id, description, calories, datetime) VALUES
(100000, 'Завтрак', 500, '2019-06-01 09:10:00'),
(100000, 'Обед', 1000, '2019-06-01 14:20:00'),
(100000, 'Ужин', 500, '2019-06-01 21:40:00'),
(100001, 'Завтрак', 1000, '2019-04-01 08:03:00'),
(100001, 'Обед', 500, '2019-04-01 13:30:00'),
(100001, 'Ужин', 510, '2019-04-01 20:15:00');
