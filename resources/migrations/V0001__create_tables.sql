create table worker (
    id int not null generated always as identity primary key
  , first_name varchar(100)
  , last_name varchar(100)
  , notes long varchar
  , balance decimal(10, 2) default 0
);

create table payment (
    id int not null generated always as identity
  , worker_id int not null constraint payment_worker_id_fk references worker on delete cascade
  , finalizated_at timestamp
  , amount decimal(10, 2)
  , description long varchar
  , document clob
);

create table attendance_sheet (
    sheet_date date not null constraint attendance_sheet_pk primary key
  , document clob
);