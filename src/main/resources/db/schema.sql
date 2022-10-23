create table if not exists account(
    id serial primary key,
    username varchar not null,
    email varchar unique not null,
    phone varchar not null
);

create table if not exists tickets(
    id serial primary key,
    session_id int,
    row int,
    cell int,
    account_id int references account(id)
);