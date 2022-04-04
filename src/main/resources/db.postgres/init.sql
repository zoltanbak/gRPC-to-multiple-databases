create database if not exists customer;
use customer;

create extension if not exists "uuid-ossp";
select uuid_generate_v4();

create table customers (
    id uuid default uuid_generate_v4(),
    primary key(id)
);

create table sensors (
    id uuid default uuid_generate_v4(),
    customer_id uuid,
    type varchar(14),
    primary key (id),
    foreign key (customer_id) references customers(id) on delete cascade
);