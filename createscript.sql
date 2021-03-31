drop table if exists authors cascade;
create table authors
(
    id serial primary key,
    first_name varchar,
    last_name varchar
);


drop table if exists stocks cascade;
create table stocks
(
    id serial primary key,
    adress varchar
    
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
    category int references book_categories,
    stock_id int references stocks
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
    copy_id int references copies,
    rented boolean
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


create index res_from
on reservations(date_from);


CREATE FUNCTION is_avail_today(b_id integer, t timestamp without time zone) returns table (num bigint)
LANGUAGE SQL AS
$$
select count(*) from copies where book_id = $1 and id not in
        (select copy_id from reservations where date_from <= $2 and date_to >= $2)
        and id not in (select copy_id from rentals where date_from <= $2 and returned is null);
$$;


CREATE FUNCTION num(b_id integer, year integer) returns table (res bigint)
LANGUAGE SQL AS
$$
select count(*) from
(select i, is_avail_today(b_id, i)
from generate_series(TIMESTAMP '2000-01-01 00:00:00'+ INTERVAL '1 year'*(year-2000), TIMESTAMP '2000-12-31 00:00:00'+ INTERVAL '1 year'*(year-2000), INTERVAL '1 day') as seq(i)) as tmp
where is_avail_today > 0
$$;












