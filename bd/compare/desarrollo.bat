SET PGPASSWORD=buho123
SET /p BD=Coloque el nombre de la bd completo
SET SERVER=190.147.99.2
SET POSTGRES_FOLDER=C:\Program Files\PostgreSQL\12\bin

"%POSTGRES_FOLDER%\pg_dump" -U postgres -s -f older.sql -h %SERVER% %BD% >> resultComparativa.txt

"%POSTGRES_FOLDER%\dropdb" -U postgres --if-exists -h %SERVER% comparativa_logisticpymes
"%POSTGRES_FOLDER%\createdb" -U postgres -h %SERVER% comparativa_logisticpymes
"%POSTGRES_FOLDER%\psql" -U postgres -f "%WORKSPACE%\LOGISTICPYMES_BD\Postgres8.sql" -h %SERVER% comparativa_logisticpymes >> resultDEV.txt
"%POSTGRES_FOLDER%\psql" -U postgres -f "%WORKSPACE%\LOGISTICPYMES_BD\Initial_Properties.sql" -h %SERVER% comparativa_logisticpymes >> resultDEV.txt
"%POSTGRES_FOLDER%\psql" -U postgres -f "%WORKSPACE%\LOGISTICPYMES_BD\Initial_Data.sql" -h %SERVER% comparativa_logisticpymes >> resultDEV.txt
"%POSTGRES_FOLDER%\psql" -U postgres -f "%WORKSPACE%\LOGISTICPYMES_BD\Initial_Function.sql" -h %SERVER% comparativa_logisticpymes >> resultDEV.txt
"%POSTGRES_FOLDER%\psql" -U postgres -f "%WORKSPACE%\LOGISTICPYMES_BD\Initial_Structure.sql" -h %SERVER% comparativa_logisticpymes >> resultDEV.txt
"%POSTGRES_FOLDER%\psql" -U postgres -f "%WORKSPACE%\LOGISTICPYMES_BD\Initial_Report.sql" -h %SERVER% comparativa_logisticpymes >> resultDEV.txt

"%POSTGRES_FOLDER%\pg_dump" -U postgres -s -f new.sql -h %SERVER% comparativa_logisticpymes >> resultComparativa.txt
java -jar apgdiff-2.4.jar --ignore-start-with older.sql new.sql > upgrade.sql

"%POSTGRES_FOLDER%\psql" -U postgres -t -A -o "exportwithoutHeader.sql" -f "%WORKSPACE%\LOGISTICPYMES_BD\common\queryReportes.sql" -h %SERVER% %BD%
"%POSTGRES_FOLDER%\psql" -U postgres -f "exportwithoutHeader.sql" -h %SERVER% comparativa_logisticpymes >> resultReport

"%POSTGRES_FOLDER%\dropdb" -U postgres --if-exists -h %SERVER% comparativa_logisticpymes

pause
del exportwithoutHeader.sql
del resultReport

del older.sql
del new.sql
del resultDEV.txt
del resultComparativa.txt
pause