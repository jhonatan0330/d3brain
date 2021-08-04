SET PGPASSWORD=buho123
SET BD=desarrollo_logisticpymes
SET SERVER=190.147.99.2

SET /p PGPASSWORD="Clave usuario postgres del servidor. Blanco toma buho123"
SET /p BD="Nombre de la base de datos a copiar. Blanco toma desarrollo_logisticpymes"
SET /p SERVER="Nombre del servidor donde se tiene y se coloca la bd copia. Blanco toma 190.147.99.2

"%POSTGRES_FOLDER%\dropdb" -U postgres -h %SERVER% --if-exists prueba_base
"%POSTGRES_FOLDER%\psql" -U postgres -h %SERVER% -c "SELECT pg_terminate_backend(pg_stat_activity.pid) FROM pg_stat_activity WHERE pg_stat_activity.datname = '%BD%' AND pid <> pg_backend_pid()"
"%POSTGRES_FOLDER%\psql" -U postgres -h %SERVER% -c "CREATE DATABASE prueba_base WITH TEMPLATE %BD% OWNER postgres";
pause