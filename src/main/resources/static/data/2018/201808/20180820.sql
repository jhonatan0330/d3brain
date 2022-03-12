
COMMENT ON TABLE usuario_usrp IS '2018-08-20';
COMMENT ON TABLE usuariosesion_ussp IS '2018.08.20.00';

ALTER TABLE tarifa_tarp
	ADD COLUMN ntar_cantidadminima integer DEFAULT 0 NOT NULL,
	ADD COLUMN ntar_cantidadmaxima integer DEFAULT 0 NOT NULL;

ALTER TABLE tarifario_trfp
	RENAME COLUMN btrf_rangos TO btrf_rangoprecios;

ALTER TABLE tarifario_trfp
	ADD COLUMN ctrf_tiporecurso character varying(32),
	ADD COLUMN btrf_rangocantidad boolean DEFAULT false NOT NULL;


