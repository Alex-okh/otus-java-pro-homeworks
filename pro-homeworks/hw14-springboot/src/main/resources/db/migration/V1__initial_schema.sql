CREATE TABLE client (
                        id   BIGSERIAL PRIMARY KEY,
                        name VARCHAR(50) NOT NULL
);

CREATE TABLE address (
                         id        BIGSERIAL PRIMARY KEY,
                         street    VARCHAR(50) ,
                         client_id BIGINT NOT NULL UNIQUE,
                         FOREIGN KEY (client_id) REFERENCES client (id) ON DELETE CASCADE
);

CREATE TABLE phone (
                       id        BIGSERIAL PRIMARY KEY,
                       number    VARCHAR(50) NOT NULL,
                       client_id BIGINT NOT NULL,
                       FOREIGN KEY (client_id) REFERENCES client (id) ON DELETE CASCADE
);
