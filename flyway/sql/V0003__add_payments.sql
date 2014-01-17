create table payment (
    id int primary key generated by default as identity
  , worker_id int constraint fk_payment__worker_id__references__worker__id references worker
  , initiated_at date
  , amount decimal(10,2)
  , description VARCHAR(500)
);