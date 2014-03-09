delete from payroll_processing_batch;
delete from worker;

insert into worker (id, first_name, last_name) values
  (1, 'Alessandro', 'Magno')
, (2, 'Giulio', 'Cesare')
, (3, 'Foo', null)
, (4, null, 'Bar')
, (5, 'Baz', 'Fool')
;

-- Sample document:
-- [
--  {
--     "periodStart": "Mar 9, 2014 9:34:24 PM",
--     "periodEnd": "Mar 9, 2014 9:34:24 PM",
--     "workerId": "1",
--     "workerDescription": "Alessandro Magno",
--     "lines": [
--       {
--         "description": "Stipendio",
--         "quantity": 40,
--         "rate": 10.5
--       },
--       {
--         "description": "Bonus",
--         "quantity": 1,
--         "rate": 123.22
--       }
--     ]
--   }
-- ]
insert into payroll_processing_batch (id, title, start_date, end_date, document) values
  (1, '2014-Jan', {d '2014-01-01'}, {d '2014-01-31'}, '')
, (2, '2014-Feb', {d '2014-02-01'}, {d '2014-02-28'}, '')
;
