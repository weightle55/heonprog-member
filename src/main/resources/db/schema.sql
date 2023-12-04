CREATE TABLE IF NOT EXISTS member
(
    profile_id INTEGER NOT NULL AUTO_INCREMENT,
    user_id VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    nick_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (profile_id)
);