CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
                                     name VARCHAR(255) NOT NULL,
                                     email VARCHAR(512) NOT NULL,
                                     CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS requests (
                                        id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
                                        description VARCHAR(512),
                                        requestor_id BIGINT NOT NULL,
                                        FOREIGN KEY (requestor_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS items (
                                     id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
                                     name VARCHAR(255) NOT NULL,
                                     description VARCHAR(512) NOT NULL,
                                     owner_id BIGINT NOT NULL,
                                     FOREIGN KEY (owner_id) REFERENCES users (id),
                                     is_available BOOLEAN,
                                     request_id BIGINT NOT NULL,
                                    FOREIGN KEY (request_id) REFERENCES requests (id)
);

CREATE TABLE IF NOT EXISTS bookings (
                                        id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
                                        start_date TIMESTAMP WITHOUT TIME ZONE,
                                        end_date TIMESTAMP WITHOUT TIME ZONE,
                                        item_id BIGINT NOT NULL,
                                        FOREIGN KEY (item_id) REFERENCES items (id),
                                        booker_id BIGINT NOT NULL,
                                        FOREIGN KEY (booker_id) REFERENCES users (id),
                                        status VARCHAR(64)



);

CREATE TABLE IF NOT EXISTS comments (
                                        id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
                                        text_comment VARCHAR(512),
                                        item_id BIGINT REFERENCES items (id),
                                        author_id BIGINT REFERENCES users (id),
                                        created TIMESTAMP WITHOUT TIME ZONE
);