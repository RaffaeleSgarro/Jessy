create table payroll_processing_batch (
    id int not null generated by default as identity (start with 10000) primary key
  , title varchar(200)
  , start_date date not null
  , end_date date not null
  , document clob
);
