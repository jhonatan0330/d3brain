
ALTER TABLE movimiento_movp
	DROP CONSTRAINT fk_movimientocategoria;

ALTER TABLE movimiento_movp
	DROP CONSTRAINT fk_movimientocuentapermisousuario;

ALTER TABLE movimiento_movp
	DROP CONSTRAINT fk_movimientocuentapermisousuariodestino;

ALTER TABLE turno_turp
	DROP CONSTRAINT fk_turnocuentapermiso;

DROP TABLE cuentapermisousuario_cpup;

DROP TABLE catalogo_catp;

ALTER TABLE cuenta_cuep
	ADD COLUMN bcue_validarturno boolean DEFAULT false NOT NULL;

ALTER TABLE movimiento_movp
	DROP COLUMN cmov_categoria,
	DROP COLUMN cmov_cuentadestino,
	DROP COLUMN cmov_cuentapermisousuario,
	DROP COLUMN cmov_cuentapermisousuariodestino,
	ALTER COLUMN cmov_cuenta SET NOT NULL;

ALTER TABLE turno_turp
	DROP COLUMN ctur_cuentapermiso,
	ADD COLUMN ctur_cuenta character varying(32) NOT NULL;

ALTER TABLE turno_turp
	ADD CONSTRAINT fk_turnocuenta FOREIGN KEY (ctur_cuenta) REFERENCES public.cuenta_cuep(ccue_llave);
