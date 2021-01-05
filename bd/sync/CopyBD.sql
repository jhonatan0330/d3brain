DROP DATABASE IF EXISTS prueba_base;

SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'desarrollo_logisticpymes' -- ← change this to your DB
  AND pid <> pg_backend_pid();

CREATE DATABASE prueba_base WITH TEMPLATE desarrollo_logisticpymes OWNER postgres;