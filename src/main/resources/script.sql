CREATE SCHEMA IF NOT EXISTS epam_gym;

CREATE TABLE IF NOT EXISTS users
(
    id         BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(128) NOT NULL,
    last_name  VARCHAR(128) NOT NULL,
    username   VARCHAR(64)  NOT NULL,
    password   VARCHAR(256) NOT NULL,
    is_active  BOOLEAN      NOT NULL,
    CONSTRAINT username_unique UNIQUE (username)
);

CREATE TABLE IF NOT EXISTS trainee
(
    id            BIGSERIAL PRIMARY KEY,
    date_of_birth DATE,
    address       VARCHAR(256),
    user_id       BIGINT NOT NULL,
    CONSTRAINT fk_trainee_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS training_type
(
    id                 BIGSERIAL PRIMARY KEY,
    training_type_name VARCHAR(128) NOT NULL,
    CONSTRAINT training_type_name_unique UNIQUE (training_type_name)
);

CREATE TABLE IF NOT EXISTS trainer
(
    id             BIGSERIAL PRIMARY KEY,
    specialization BIGINT NOT NULL,
    user_id        BIGINT NOT NULL,
    CONSTRAINT fk_trainer_training_type1 FOREIGN KEY (specialization) REFERENCES training_type (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fk_trainer_user1 FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS training
(
    id                BIGSERIAL PRIMARY KEY,
    trainee_id        BIGINT       NOT NULL,
    trainer_id        BIGINT       NOT NULL,
    training_name     VARCHAR(256) NOT NULL,
    training_type_id  BIGINT       NOT NULL,
    training_date     DATE         NOT NULL,
    training_duration BIGINT       NOT NULL,
    CONSTRAINT fk_training_trainee1 FOREIGN KEY (trainee_id) REFERENCES trainee (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fk_training_trainer1 FOREIGN KEY (trainer_id) REFERENCES trainer (id) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fk_training_training_type1 FOREIGN KEY (training_type_id) REFERENCES training_type (id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

INSERT INTO users (id, first_name, last_name, username, password, is_active)
VALUES (1, 'Eliezer', 'Yudkowsky', 'Eliezer.Yudkowsky', 'pass123', true),
       (2, 'Quirinus', 'Quirrell', 'Quirinus.Quirrell', 'pass456', true),
       (3, 'Harry', 'Potter', 'Harry.Potter', 'asdfioasudf', true),
       (4, 'Ronald', 'Weasley', 'Ronald.Weasley', 'asdfuowurqka', true),
       (5, 'Hermione', 'Granger', 'Hermione.Granger', 'uoqpwrrtpoiqug', true),
       (6, 'Draco', 'Malfoy', 'Draco.Malfoy', 'zxcmvnzxxcvnnm', true);

INSERT INTO trainee (id, date_of_birth, address, user_id)
VALUES (1, '1990-05-15', '123 Main Street', 4),
       (2, '1985-08-20', '456 Elm Street', 5),
       (3, '1993-10-10', '789 Oak Street', 6);

INSERT INTO training_type (id, training_type_name)
VALUES (1, 'Workshop'),
       (2, 'Seminar');

INSERT INTO trainer (id, specialization, user_id)
VALUES (1, 1, 1),
       (2, 2, 2),
       (3, 1, 3);

INSERT INTO training (id, trainee_id, trainer_id, training_name, training_type_id, training_date, training_duration)
VALUES (1, 1, 1, 'Java Basics', 1, '2023-01-10', 3),
       (2, 1, 2, 'Spring Framework', 2, '2023-02-15', 5),
       (3, 3, 3, 'Python Programming', 1, '2023-03-20', 4);


SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('trainee_id_seq', (SELECT MAX(id) FROM trainee));
SELECT setval('training_type_id_seq', (SELECT MAX(id) FROM training_type));
SELECT setval('trainer_id_seq', (SELECT MAX(id) FROM trainer));
SELECT setval('training_id_seq', (SELECT MAX(id) FROM training));

select *
from training t1_0
         join trainer t2_0 on t2_0.id = t1_0.trainer_id
         join users u1_0 on u1_0.id = t2_0.user_id
where u1_0.username = 'Eliezer.Yudkowsky'
  and t1_0.training_name = 'Java Basics';


select t1_0.id, t1_0.trainee_id, t1_0.trainer_id, t1_0.training_date, t1_0.training_duration, t1_0.training_name, t1_0.training_type_id
from training t1_0
         join trainer t2_0 on t2_0.id = t1_0.trainer_id
         join users u1_0 on u1_0.id = t2_0.user_id
where u1_0.username=?
  and t1_0.training_name=?