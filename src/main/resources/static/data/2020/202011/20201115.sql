COMMENT ON TABLE usuario_usrp IS '2020-11-15';
COMMENT ON TABLE usuariosesion_ussp IS '2020.11.15.00';

DROP FUNCTION sw42_prioridad_a(character varying);

DROP FUNCTION sw42_prioridad_b(character varying);

DROP FUNCTION sw42_prioridad_c(character varying);

ALTER TABLE actividad_actp
	ADD COLUMN dact_fechaarrancar timestamp with time zone,
	ADD COLUMN dact_fechaterminar timestamp with time zone,
	ADD COLUMN dact_fechalimite timestamp with time zone,
	ADD COLUMN nact_duracion integer DEFAULT 0 NOT NULL,
	ADD COLUMN cact_actividadprevia character varying(32),
	ADD COLUMN cact_actividadsiguiente character varying(32);

update actividad_actp set dact_fechaarrancar = dact_fecharegistro;
update actividad_actp set dact_fechaterminar = dact_fechainactivo;
update actividad_actp set dact_fechaterminar = dact_fecharegistro where dact_fechaterminar is null;

ALTER TABLE actividad_actp
	ALTER COLUMN dact_fechaarrancar set not null ,
	ALTER COLUMN dact_fechaterminar set not null;