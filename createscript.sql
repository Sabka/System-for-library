-------------------------------------------TABLES----------------------------------------------

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

-------------------------------INDICES------------------------------------------

drop index if exists res_dates;
create index res_dates on reservations(date_from, date_to);

drop index if exists ren_dates;
create index ren_dates on rentals(date_from, date_to);

drop index if exists res_copy;
create index res_copy on reservations(copy_id);

drop index if exists ren_copy;
create index ren_copy on rentals(copy_id);

drop index if exists cpy_to_book;
create index cpy_to_book on  copies(book_id);

drop index if exists ren_df;
create index ren_df on rentals(date_from);

drop index if exists ren_dt;
create index ren_dt on rentals(date_to);

drop index if exists ren_ret;
create index ren_ret on rentals(returned);

drop index if exists res_df;
create index res_df on reservations(date_from);

drop index if exists res_dt;
create index res_dt on reservations(date_to);

---------------------------------FUNCTIONS------------------------------------------



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


