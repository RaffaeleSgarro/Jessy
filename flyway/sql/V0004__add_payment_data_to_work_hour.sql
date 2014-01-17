alter table worked_hour add column payment_id int constraint fk_work_hour__payment_id__references__payment__id references payment(id) on delete set null;
