drop table if exists authors cascade;
create table authors
(
    id serial primary key,
    first_name varchar,
    last_name varchar
);


drop table if exists books cascade;
create table books
(
    id serial primary key,
    title varchar
);

drop table if exists book_authors cascade;
create table book_authors
(
    book_id int references books,
    author_id int references authors
);

drop table if exists book_categories cascade;
create table book_categories
(
    id serial primary key,
    cat_name varchar,
    period int
);


drop table if exists copies cascade;
create table copies
(
    id serial primary key,
    book_id int references books,
    state numeric,
    available_distantly boolean,
    in_library boolean,
    category int references book_categories
);

drop table if exists readers cascade;
create table readers
(
    id serial primary key,
    first_name varchar,
    last_name varchar,
    valid_til timestamp
);


drop table if exists fees cascade;
create table fees
(
    id serial primary key,
    reader_id int references readers,
    amount numeric,
    closed boolean
    
);

drop table if exists reservations cascade;
create table reservations
(
    id serial primary key,
    date_from timestamp,
    date_to timestamp,
    reader_id int references readers,
    copy_id int references copies
);

drop table if exists rentals cascade;
create table rentals
(
    id serial primary key,
    date_from timestamp,
    date_to timestamp,
    returned timestamp,
    reader_id int references readers,
    copy_id int references copies
    
);




