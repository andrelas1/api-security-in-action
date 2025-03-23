CREATE TABLE IF NOT EXISTS spaces
(
    space_id INT PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    owner    VARCHAR(30)  NOT NULL
);
CREATE SEQUENCE IF NOT EXISTS space_id_seq;
CREATE TABLE IF NOT EXISTS messages
(
    space_id INT           NOT NULL REFERENCES spaces (space_id),
    msg_id   INT PRIMARY KEY,
    author   VARCHAR(30)   NOT NULL,
    msg_time TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    msg_text VARCHAR(1024) NOT NULL
);
CREATE SEQUENCE IF NOT EXISTS msg_id_seq;
CREATE INDEX IF NOT EXISTS msg_timestamp_idx ON messages (msg_time);
CREATE UNIQUE INDEX IF NOT EXISTS space_name_idx ON spaces (name);

-- create new user for the api only -> this user will have the permissions that allow the usages meant for it and nothing more
CREATE USER natter_api_user PASSWORD 'password';
GRANT SELECT, INSERT ON spaces, messages TO natter_api_user;


CREATE TABLE IF NOT EXISTS users
(
    user_id varchar(30) PRIMARY KEY,
    pw_hash varchar(255) NOT NULL
);
GRANT SELECT, INSERT ON users TO natter_api_user;

CREATE TABLE IF NOT EXISTS audit_log
(
    audit_id   INT          NULL,
    method     varchar(10)  NOT NULL,
    path       varchar(100) NOT NULL,
    user_id    varchar(30)  NULL,
    status     int          NULL,
    audit_time TIMESTAMP    NOT NULL
);
CREATE SEQUENCE IF NOT EXISTS audit_id_seq;
GRANT SELECT, INSERT ON audit_log TO natter_api_user;

CREATE TABLE IF NOT EXISTS permissions
(
    space_id INT         NOT NULL REFERENCES spaces (space_id),
    user_id  VARCHAR(30) NOT NULL REFERENCES users (user_id),
    perms    VARCHAR(3)  NOT NULL,
    PRIMARY KEY (space_id, user_id)
);
GRANT SELECT, INSERT ON permissions TO natter_api_user;
