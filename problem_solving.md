# Problem-solving case

## Description
In our Time Off module we have Time Off Categories that you can use to request
the right time for those categories as a team member.

We added a rule that you can’t request two categories at the same time with the same
date range. However there is a case that you can have Work Remotely applicable and
you want to request Annual leave.

Right now the constraint of two categories can’t be overlapping is present since you
can’t request annual leave while we have another category within the same dates.

Tables:
```
Time_off_request
    Id,
    request_category_id,
    employee_id,
    start_date,
    end_date
- Request_category
    id,
    Name
```

## Solution
To solve the problem on the db level, we need to create a new constraint to allow creating new entries
in the `time_off_request` table with overlapping date ranges of two specific `request_category_id`.

The first important point here is that we can't drop an old constraint first and then create a new one in
different transactions, because between dropping and creating constraints it would be possible to insert 
wrong entries. There are 2 solutions to make it safely: 
* Drop and create constraints in one transaction
* Create a new constraint and then drop an old one

Right now a set of allowed overlapping `request_category_id`s contains only one pair (Work Remotely + Annual Leave),
but it's obvious that in future there can be more allowed pairs, so we need to create a constraint with a 
possibility to add new allowed pairs. We can implement it using another table with allowed pairs.

So the result sql code will look the following way

```postgresql
create table allowed_overlapping_category_ids
(
    first_category  int not null,
    second_category int not null
);


create function overlapping_time_off_check()
    returns trigger AS
$func$
begin
    if exists (select * from time_off_request r
               where r.start_date <= new."end_date"
                 and r.end_date >= new."start_date"
                 and r.employee_id = new."employee_id"
                 and (r.request_category_id, new."request_category_id") not in (select first_category, second_category from allowed_overlapping_category_ids)
                 and (new."request_category_id", r.request_category_id) not in (select first_category, second_category from allowed_overlapping_category_ids)) then
        raise exception 'Time off requests dates are overlapping';
    end if;
    return new;
end
$func$  language plpgsql;

create trigger overlapping_check
    before insert on time_off_request
    for each row execute procedure overlapping_time_off_check();
```

### Better solutions
In general spread the application logic over the systems parts is a bad idea, because in this case it will be complicated
to understand the possible flows by just looking at the code. In this task it would be a better solution to implement
this constraint on the application level, by verifying the data in a *Validator class. It provides a way better flexibility
of enhancing the constraint, because on application level we can implement more complicated checks.

So in the best case the solution plan would look the following way:
* Implement the current constraint in application
* Drop the current constraint in the db
* Change the application level constraint using the default application development strategy