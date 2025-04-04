CREATE TABLE product
(
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    img_path    VARCHAR(2000) NOT NULL,
    price       DECIMAL NOT NULL,
    description TEXT
);

CREATE TABLE cart (
       id BIGSERIAL PRIMARY KEY,
       product_id BIGINT NOT NULL constraint fk_cart__product
           references product
);

create index idx_cart__product
    on cart (product_id);