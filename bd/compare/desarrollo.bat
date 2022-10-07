SET PGPASSWORD=buho123
SET /p BD=Coloque el nombre de la bd completo
SET SERVER=190.27.41.78
SET PORT=15432
SET POSTGRES_FOLDER=C:\Users\USER\AppData\Roaming\DBeaverData\drivers\clients\postgresql\win\14
SET FOLDER_BD=C:\Softure\WS\Golyat\SW42_JAVA\src\main\resources\static\data
SET FOLDER_BD_UTILS=C:\Softure\WS\Golyat\SW42_JAVA\bd\utils

"%POSTGRES_FOLDER%\pg_dump" -U postgres -s -f older.sql -h %SERVER% -p %PORT% %BD% >> resultComparativa.txt

"%POSTGRES_FOLDER%\dropdb" -U postgres --if-exists -h %SERVER% -p %PORT% comparativa_logisticpymes
"%POSTGRES_FOLDER%\createdb" -U postgres -h %SERVER% -p %PORT% comparativa_logisticpymes
"%POSTGRES_FOLDER%\psql" -U postgres -f "%FOLDER_BD%\Postgres8.sql" -h %SERVER% -p %PORT% comparativa_logisticpymes >> resultDEV.txt
"%POSTGRES_FOLDER%\psql" -U postgres -f "%FOLDER_BD%\1. Initial_Structure.sql" -h %SERVER% -p %PORT% comparativa_logisticpymes >> resultDEV.txt
"%POSTGRES_FOLDER%\psql" -U postgres -f "%FOLDER_BD%\2. Initial_Function.sql" -h %SERVER% -p %PORT% comparativa_logisticpymes >> resultDEV.txt
"%POSTGRES_FOLDER%\psql" -U postgres -f "%FOLDER_BD%\3. Initial_Properties.sql" -h %SERVER% -p %PORT% comparativa_logisticpymes >> resultDEV.txt
"%POSTGRES_FOLDER%\psql" -U postgres -f "%FOLDER_BD%\4. Initial_Data.sql" -h %SERVER% -p %PORT% comparativa_logisticpymes >> resultDEV.txt
"%POSTGRES_FOLDER%\psql" -U postgres -f "%FOLDER_BD%\Initial_Report.sql" -h %SERVER% -p %PORT% comparativa_logisticpymes >> resultDEV.txt

"%POSTGRES_FOLDER%\pg_dump" -U postgres -s -f new.sql -h %SERVER% -p %PORT% comparativa_logisticpymes >> resultComparativa.txt
java -jar apgdiff-2.4.jar --ignore-start-with older.sql new.sql > upgrade.sql

"%POSTGRES_FOLDER%\psql" -U postgres -t -A -o "exportwithoutHeader.sql" -f "%FOLDER_BD_UTILS%\queryReportes.sql" -h %SERVER% -p %PORT% %BD%
"%POSTGRES_FOLDER%\psql" -U postgres -f "exportwithoutHeader.sql" -h %SERVER% -p %PORT% comparativa_logisticpymes >> resultReport

"%POSTGRES_FOLDER%\dropdb" -U postgres --if-exists -h %SERVER% -p %PORT% comparativa_logisticpymes

pause
del exportwithoutHeader.sql
del resultReport

del older.sql
del new.sql
del resultDEV.txt
del resultComparativa.txt
pause