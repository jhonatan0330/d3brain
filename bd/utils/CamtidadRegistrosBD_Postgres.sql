select relname, reltuples , pg_size_pretty(pg_total_relation_size((nspname || '.' ||relname)::text)), pg_total_relation_size((nspname || '.' ||relname)::text)
from pg_class c, pg_namespace n
where relnamespace = n.oid and nspname in ('public', 'account', 'config', 'historic', 'learning', 'report', 'task') 
and relname not like 'pk_%'  and relname not like '%_key' and relname not like 'uk_%' 
order by 2 desc;

select cdpl_llave, cdpl_nombre,  count(*) from pedidoventa_pdvp, documentoplantilla_dplp
where cpdv_plantilla =cdpl_llave
group by cdpl_llave, cdpl_nombre
order by 3 desc

select cdpl_llave, cdpl_nombre,  count(*) from pedidoventa_pdvp, documentoplantilla_dplp
where cpdv_plantilla =cdpl_llave and npdv_historico is null
group by cdpl_llave, cdpl_nombre
order by 3 desc

select datname AS db_name, 
       pg_size_pretty(pg_database_size(datname)) as db_size
from pg_database 
order by pg_database_size(datname) desc;

SELECT pg_size_pretty( pg_database_size( current_database() ) ) 
    , pg_database_size( current_database() );

--Tamaño de una tabla

SELECT pg_size_pretty(pg_total_relation_size('pedidoventacaracteristica_pvcp'));

--Funciones
select p.proname as function_name, prosrc  as definicion,*
from pg_proc p
left join pg_namespace n on p.pronamespace = n.oid
where n.nspname not in ('pg_catalog', 'information_schema')
and prosrc like '%prefijo_opcion_3%'
--and proargnames IN ('{documento,cant,pagina,fechaminima,fechamaxima,filtro,codigo_exacto,token}')
and p.proname like 'propiedad_%'


--
SELECT replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-','')


SELECT pid, age(clock_timestamp(), query_start), usename, query, state
FROM pg_stat_activity
WHERE state != 'idle' AND query NOT ILIKE '%pg_stat_activity%'
ORDER BY query_start desc;


-- kill running query
SELECT pg_cancel_backend(186394);

SELECT pg_terminate_backend(186394);




SELECT pg_terminate_backend(pg_stat_activity.pid)
FROM pg_stat_activity
WHERE pg_stat_activity.datname = 'desarrollo_logisticpymes' -- ← change this to your DB
  AND pid <> pg_backend_pid();


