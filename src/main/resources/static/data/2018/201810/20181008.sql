COMMENT ON TABLE usuario_usrp IS '2018-10-08';
COMMENT ON TABLE usuariosesion_ussp IS '2018.10.08.00';

ALTER TABLE proceso_prcp
	DROP COLUMN cprc_descripcion;

ALTER TABLE procesoestado_pesp
	ADD COLUMN cpes_codigo character varying(40);

UPDATE procesoestado_pesp SET cpes_codigo = cpes_nombre;

ALTER TABLE procesoestado_pesp
	ALTER COLUMN cpes_codigo SET NOT NULL;