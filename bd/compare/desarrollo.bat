SET PGPASSWORD=buho123
SET /p BD=Coloque el nombre de la bd completo
SET SERVER=190.27.41.78
SET POSTGRES_FOLDER=C:\Program Files\PostgreSQL\12\bin
SET FOLDER_BD=D:\Softure\Space2021\SW42_JAVA\src\main\resources\static\data
SET FOLDER_BD_UTILS=D:\Softure\Space2021\SW42_JAVA\bd\utils

"%POSTGRES_FOLDER%\pg_dump" -U postgres -s -f older.sql -h %SERVER% %BD% >> resultComparativa.txt

"%POSTGRES_FOLDER%\dropdb" -U postgres --if-exists -h %SERVER% comparativa_logisticpymes
"%POSTGRES_FOLDER%\createdb" -U postgres -h %SERVER% comparativa_logisticpymes
"%POSTGRES_FOLDER%\psql" -U postgres -f "%FOLDER_BD%\Postgres8.sql" -h %SERVER% comparativa_logisticpymes >> resultDEV.txt
"%POSTGRES_FOLDER%\psql" -U postgres -f "%FOLDER_BD%\1. Initial_Structure.sql" -h %SERVER% comparativa_logisticpymes >> resultDEV.txt
"%POSTGRES_FOLDER%\psql" -U postgres -f "%FOLDER_BD%\2. Initial_Function.sql" -h %SERVER% comparativa_logisticpymes >> resultDEV.txt
"%POSTGRES_FOLDER%\psql" -U postgres -f "%FOLDER_BD%\3. Initial_Properties.sql" -h %SERVER% comparativa_logisticpymes >> resultDEV.txt
"%POSTGRES_FOLDER%\psql" -U postgres -f "%FOLDER_BD%\4. Initial_Data.sql" -h %SERVER% comparativa_logisticpymes >> resultDEV.txt
"%POSTGRES_FOLDER%\psql" -U postgres -f "%FOLDER_BD%\Initial_Report.sql" -h %SERVER% comparativa_logisticpymes >> resultDEV.txt

"%POSTGRES_FOLDER%\pg_dump" -U postgres -s -f new.sql -h %SERVER% comparativa_logisticpymes >> resultComparativa.txt
java -jar apgdiff-2.4.jar --ignore-start-with older.sql new.sql > upgrade.sql

"%POSTGRES_FOLDER%\psql" -U postgres -t -A -o "exportwithoutHeader.sql" -f "%FOLDER_BD_UTILS%\queryReportes.sql" -h %SERVER% %BD%
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