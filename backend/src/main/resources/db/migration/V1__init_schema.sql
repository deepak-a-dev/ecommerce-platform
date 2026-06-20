CREATE TABLE user_info (
                           user_id      INT NOT NULL AUTO_INCREMENT,
                           first_name   VARCHAR(255),
                           last_name    VARCHAR(255),
                           phone_number VARCHAR(20),
                           email        VARCHAR(255),
                           username     VARCHAR(255) UNIQUE,
                           password     VARCHAR(255),
                           role         VARCHAR(20),
                           PRIMARY KEY (user_id)
) ENGINE = InnoDB;

CREATE TABLE product (
                         product_id   INT NOT NULL AUTO_INCREMENT,
                         pro_name     VARCHAR(255),
                         brand_name   VARCHAR(255),
                         pro_category VARCHAR(255),
                         pro_price    DOUBLE,
                         pro_status   BIT,
                         PRIMARY KEY (product_id)
) ENGINE = InnoDB;

CREATE TABLE orders (
                        order_id      INT NOT NULL AUTO_INCREMENT,
                        user_id       INT,
                        date_of_order DATETIME(6),
                        total         DOUBLE,
                        PRIMARY KEY (order_id),
                        CONSTRAINT fk_orders_user FOREIGN KEY (user_id) REFERENCES user_info (user_id)
) ENGINE = InnoDB;

CREATE TABLE order_items (
                             order_item_id INT NOT NULL AUTO_INCREMENT,
                             order_id      INT,
                             product_id    INT,
                             product_name  VARCHAR(255),
                             product_qty   INT,
                             unit_price    DOUBLE,
                             sub_total     DOUBLE,
                             PRIMARY KEY (order_item_id),
                             CONSTRAINT fk_orderitems_order FOREIGN KEY (order_id) REFERENCES orders (order_id),
                             CONSTRAINT fk_orderitems_product FOREIGN KEY (product_id) REFERENCES product (product_id)
) ENGINE = InnoDB;

CREATE TABLE token (
                       id            INT NOT NULL AUTO_INCREMENT,
                       token         VARCHAR(500),
                       is_logged_out BIT,
                       creation      DATETIME(6),
                       expiration    DATETIME(6),
                       user_id       INT,
                       PRIMARY KEY (id),
                       CONSTRAINT fk_token_user FOREIGN KEY (user_id) REFERENCES user_info (user_id)
) ENGINE = InnoDB;

CREATE TABLE forgot_password (
                                 fp_id      INT NOT NULL AUTO_INCREMENT,
                                 otp        INT,
                                 creation   DATETIME(6),
                                 expiration DATETIME(6),
                                 user_id    INT,
                                 PRIMARY KEY (fp_id),
                                 CONSTRAINT fk_fp_user FOREIGN KEY (user_id) REFERENCES user_info (user_id)
) ENGINE = InnoDB;