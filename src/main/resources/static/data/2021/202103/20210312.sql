COMMENT ON TABLE usuario_usrp IS '2021-03-12';

ALTER TABLE actividad_actp
	ADD COLUMN dact_fechaleido timestamp with time zone,
	ALTER COLUMN dact_fechaarrancar DROP NOT NULL,
	ALTER COLUMN dact_fechaterminar DROP NOT NULL;

UPDATE actividad_actp SET dact_fechaarrancar = NULL;
UPDATE actividad_actp SET dact_fechaterminar = NULL;
 

