COMMENT ON TABLE usuario_usrp IS '2023-12-12';

ALTER TABLE organizacion_orgp DROP CONSTRAINT fk_organizacionservidor;

ALTER TABLE organizacion_orgp DROP COLUMN corg_servidorcorreo;

ALTER TABLE organizacion_orgp DROP COLUMN borg_sincronizacion;

ALTER TABLE organizacion_orgp ALTER COLUMN corg_servidor TYPE varchar(200);

update organizacion_orgp set corg_servidor  = (select cser_url from servidor_serp where cser_llave  = corg_servidor);