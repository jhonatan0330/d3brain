COMMENT ON TABLE usuario_usrp IS '2020-03-27';

ALTER TABLE tarifa_tarp
	ALTER COLUMN ctar_producto DROP NOT NULL;

ALTER TABLE tarifario_trfp
	DROP COLUMN ctrf_producto,
	ADD COLUMN btrf_productoopcional boolean DEFAULT false NOT NULL;
