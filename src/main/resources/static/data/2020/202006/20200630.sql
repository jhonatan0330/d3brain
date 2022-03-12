COMMENT ON TABLE usuario_usrp IS '2020-06-30';
COMMENT ON TABLE usuariosesion_ussp IS '2020.06.30.00';


ALTER TABLE gpsdispositivo_gpsp
	ADD COLUMN dgps_ultimaconexion timestamp with time zone NOT NULL,
	ADD COLUMN ngps_intervalo integer DEFAULT 0 NOT NULL,
	ADD COLUMN ngps_distancia integer DEFAULT 0 NOT NULL,
	ADD COLUMN ngps_acercamiento integer DEFAULT 0 NOT NULL;

ALTER TABLE gpslocalizacion_gplp
	DROP COLUMN cgpl_direccion;

