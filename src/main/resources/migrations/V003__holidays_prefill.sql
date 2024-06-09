insert into holidays(id, date, country_id, zone_id, created_at, updated_at)
values (uuid(), '2024-06-12', 1, 1, now(), now()),
       (uuid(), '2024-06-12', 1, 2, now(), now()),
       (uuid(), '2024-06-14', 1, null, now(), now()),
       (uuid(), '2024-06-20', 1, null, now(), now()),
       (uuid(), '2024-06-12', 2, null, now(), now()),
       (uuid(), '2024-06-12', 3, null, now(), now());