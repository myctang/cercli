create table holidays
(
    id         uuid primary key,
    date       date      not null,
    country_id int       not null,
    zone_id    int,
    created_at timestamp not null,
    updated_at timestamp not null
);

create index holidays_date_idx on holidays (date);