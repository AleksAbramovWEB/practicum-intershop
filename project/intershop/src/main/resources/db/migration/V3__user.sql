
alter table cart
    add user_id varchar(255) not null;

create index idx_cart__user
    on cart (user_id);


alter table orders
    add user_id varchar(255) not null;

create index idx_order__user
    on orders (user_id);