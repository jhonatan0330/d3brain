

"%POSTGRES_FOLDER%\dropdb" -U postgres --if-exists prueba_base
"%POSTGRES_FOLDER%\psql" -U postgres -c "SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname = '%BD%' AND pid <> pg_backend_pid()"
"%POSTGRES_FOLDER%\psql" -U postgres -c "CREATE DATABASE prueba_base WITH TEMPLATE %BD% OWNER postgres";