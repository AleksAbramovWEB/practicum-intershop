CREATE TABLE orders
(
    id          BIGSERIAL PRIMARY KEY,
    total_sum   DECIMAL NOT NULL
);

CREATE TABLE order_item (
       id BIGSERIAL PRIMARY KEY,
       order_id BIGINT NOT NULL constraint fk_order_item__orders
           references orders,
       product_id BIGINT NOT NULL constraint fk_order_item__product
           references product,
       count INT NOT NULL,
       total_sum   DECIMAL NOT NULL
);

create index idx_order_item__orders
    on order_item (order_id);

create index idx_order_item__product
    on order_item (product_id);