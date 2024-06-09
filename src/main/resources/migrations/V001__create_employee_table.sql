create table employees
(
    id              uuid primary key,
    state           varchar   not null,
    name            varchar   not null,
    position        varchar   not null,
    email           varchar   not null,
    salary_amount   bigint    not null,
    salary_currency varchar   not null,
    country_id      int       not null,
    zone_id         int,
    created_at      timestamp not null,
    updated_at      timestamp not null
);

create index employees_country_zone_idx on employees (country_id, zone_id);