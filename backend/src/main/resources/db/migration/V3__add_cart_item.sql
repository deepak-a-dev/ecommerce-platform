CREATE TABLE cart_item (
                           cart_item_id INT NOT NULL AUTO_INCREMENT,
                           user_id      INT NOT NULL,
                           product_id   INT NOT NULL,
                           quantity     INT NOT NULL,
                           PRIMARY KEY (cart_item_id),
                           CONSTRAINT uq_cart_user_product UNIQUE (user_id, product_id),
                           CONSTRAINT fk_cartitem_user    FOREIGN KEY (user_id)    REFERENCES user_info (user_id),
                           CONSTRAINT fk_cartitem_product FOREIGN KEY (product_id) REFERENCES product (product_id)
) ENGINE = InnoDB;