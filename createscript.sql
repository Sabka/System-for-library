drop table if exists authors cascade;
create table authors
(
    id serial primary key,
    first_name varchar,
    last_name varchar not null
);


drop table if exists stocks cascade;
create table stocks
(
    id serial primary key,
    adress varchar not null
    
);


drop table if exists books cascade;
create table books
(
    id serial primary key,
    title varchar not null
);

drop table if exists book_authors cascade;
create table book_authors
(
    book_id int references books on delete cascade on update cascade,
    author_id int references authors on delete set null on update set null
);

drop table if exists book_categories cascade;
create table book_categories
(
    id serial primary key,
    cat_name varchar not null,
    period int
);


drop table if exists copies cascade;
create table copies
(
    id serial primary key,
    book_id int references books on delete cascade on update cascade,
    state numeric not null,
    available_distantly boolean not null,
    in_library boolean not null,
    category int references book_categories on delete set null on update set null,
    stock_id int references stocks on delete set null  on update set null
);

drop table if exists readers cascade;
create table readers
(
    id serial primary key,
    first_name varchar,
    last_name varchar not null,
    valid_til timestamp not null
);


drop table if exists fees cascade;
create table fees
(
    id serial primary key,
    reader_id int references readers on delete set null  on update set null,
    amount numeric not null,
    closed boolean,
    delay integer
    
);

drop table if exists reservations cascade;
create table reservations
(
    id serial primary key,
    date_from timestamp not null,
    date_to timestamp not null,
    reader_id int references readers on delete set null  on update set null,
    copy_id int references copies on delete cascade  on update cascade,
    rented boolean 
);

drop table if exists rentals cascade;
create table rentals
(
    id serial primary key,
    date_from timestamp not null,
    date_to timestamp not null,
    returned timestamp,
    reader_id int references readers on delete set null  on update set null,
    copy_id int references copies on delete cascade  on update cascade
    
);

drop index if exists res_from;
create index res_from on reservations(date_from);


drop function if exists is_avail_today;
CREATE FUNCTION is_avail_today(b_id integer, t timestamp without time zone) returns table (num bigint) LANGUAGE SQL AS
$$
	select count(id) from copies where book_id = $1 
		and id not in (select copy_id from reservations where date_from <= $2 and date_to >= $2)
        	and id not in (select copy_id from rentals where date_from <= $2 and returned is null);
$$;

drop function if exists num(integer, integer);
CREATE FUNCTION num(b_id integer, year integer) returns table (res bigint) LANGUAGE SQL AS
$$
	select count(i)
	from 
		(select i, is_avail_today(b_id, i) 
		from generate_series
			(TIMESTAMP '2000-01-01 00:00:00'+ INTERVAL '1 year'*(year-2000), 
			TIMESTAMP '2000-12-31 00:00:00'+ INTERVAL '1 year'*(year-2000), 
			INTERVAL '1 day') as seq(i)) as tmp
	where is_avail_today > 0
$$;


DROP FUNCTION if exists current_year_records;
CREATE function current_year_records(y integer)
RETURNS table (copy_id integer,  date_from timestamp, date_to timestamp, typ varchar)
LANGUAGE SQL STABLE AS
$$
    (
    
    select copy_id, date_from, date_to, 'res' from reservations 
    where 
    date_from BETWEEN 
    TIMESTAMP '2000-01-01 00:00:00'+ INTERVAL '1 year'*($1-2000) and TIMESTAMP '2000-12-31 24:00:00'+ INTERVAL '1 year'*($1-2000)
    or 
    date_to BETWEEN 
    TIMESTAMP '2000-01-01 00:00:00'+ INTERVAL '1 year'*($1-2000) and TIMESTAMP '2000-12-31 24:00:00'+ INTERVAL '1 year'*($1-2000)


    UNION  

    select copy_id, date_from, returned, 'renNOTNULL' from rentals
    where returned is not null
    and
    (
    (date_from BETWEEN 
    TIMESTAMP '2000-01-01 00:00:00'+ INTERVAL '1 year'*($1-2000) and TIMESTAMP '2000-12-31 24:00:00'+ INTERVAL '1 year'*($1-2000))
    or 
    (returned BETWEEN 
    TIMESTAMP '2000-01-01 00:00:00'+ INTERVAL '1 year'*($1-2000) and TIMESTAMP '2000-12-31 24:00:00'+ INTERVAL '1 year'*($1-2000))
    )


    UNION

    select copy_id, date_from, returned, 'renNULL' from rentals
    where returned is null
    and date_from >= TIMESTAMP '2000-01-01 00:00:00'+ INTERVAL '1 year'*($1-2000)
    and date_from < TIMESTAMP '2000-12-31 24:00:00'+ INTERVAL '1 year'*($1-2000)
    
    )
    
$$;


drop index if exists res_dates;
create index res_dates on reservations(date_from, date_to);

drop index if exists ren_dates;
create index ren_dates on rentals(date_from, date_to);




-------------------------------------------------------------


create index res_copy on reservations(copy_id);

drop index if exists ren_copy;
create index ren_copy on rentals(copy_id);

drop index if exists cpy_to_book;
create index cpy_to_book on  copies(book_id);

-- vrati 0 ak kopia je volna - vyuziva index na copy id
drop function if exists num_res_ren_violates_cpy(copy_id int, t timestamp) ;
create function num_res_ren_violates_cpy(c_id int, t timestamp) returns int language sql as
$$
    select sum(count)::int
    from

    (
        
       select count(id)::int 
            from rentals
            where copy_id = $1
            and 
            (
                (date_from <= $2 and returned is null)
                or
                (date_from <= $2 and returned > $2 + INTERVAL '1 day' )
            )
        

        union

        
            select count(id)::int 
            from reservations
            where copy_id = $1
            and (date_from <= $2 and date_to > $2 + INTERVAL '1 day' )
       
    ) as tmp
$$;


-- pocet volnych kopii knihy v dany den
drop function num_avail_cpy_of_book(b_id int, t timestamp);
create function num_avail_cpy_of_book(b_id int, t timestamp) returns int language sql as
$$
    select count(id)::int
    from copies 
    where book_id = $1 and num_res_ren_violates_cpy(id, t) = 0;
    
$$;


-- dostanem knihu a rok vratim kolko dni v danom roku bola dostupna
drop function num_days_book_avail(b_id int, year int);
create function num_days_book_avail(b_id int, year int) returns int language sql as
$$
    select count(i)::int
    from generate_series
        (TIMESTAMP '2000-01-01 00:00:00'+ INTERVAL '1 year'*($2-2000),  -- 2021 za vstupny year
        TIMESTAMP '2000-12-31 00:00:00'+ INTERVAL '1 year'*($2-2000), 
        INTERVAL '1 day') as seq(i)
    where num_avail_cpy_of_book($1, i) > 0; 
$$;


create index ren_df on rentals(date_from);

create index ren_dt on rentals(date_to);

create index ren_ret on rentals(returned);

create index res_df on reservations(date_from);

create index res_dt on reservations(date_to);


create index ren_cpy on rentals(copy_id);

create index res_cpy on reservations(copy_id);


