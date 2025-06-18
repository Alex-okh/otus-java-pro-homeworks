create table phone
(
    id        bigserial not null primary key,
    number    varchar(50),
    client_id int8,
    foreign key (client_id) references client (id)
);

create table address
(
    id     bigserial not null primary key,
    street varchar(50)
);

alter table client
    add column address_id int8,
    add constraint fk_addreess foreign key (address_id) references address (id) on delete cascade;



