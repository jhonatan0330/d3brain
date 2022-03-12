COMMENT ON TABLE usuario_usrp IS '2019-12-28';

ALTER TABLE movimiento_movp
	DROP CONSTRAINT fk_movimientocategoria;

ALTER TABLE movimiento_movp
	DROP CONSTRAINT fk_movimientocuentapermisousuario;

ALTER TABLE movimiento_movp
	DROP CONSTRAINT fk_movimientocuentapermisousuariodestino;

ALTER TABLE turno_turp
	DROP CONSTRAINT fk_turnocuentapermiso;

ALTER TABLE cuenta_cuep
	ADD COLUMN bcue_validarturno boolean DEFAULT false NOT NULL,
	ADD COLUMN mcue_cierremaximo numeric(18,6) DEFAULT 0 NOT NULL;

update cuenta_cuep set bcue_validarturno = true where ccue_llave in (select ccpu_cuenta from cuentapermisousuario_cpup where bcpu_validarturno = true and ccpu_estado = 'A');

update cuenta_cuep set mcue_cierremaximo = (select coalesce( min(mcpu_cierremaximo), 0) from cuentapermisousuario_cpup where ccpu_cuenta = ccue_llave);

ALTER TABLE turno_turp
	ADD COLUMN ctur_cuenta character varying(32);

update turno_turp set ctur_cuenta = (select ccpu_cuenta from cuentapermisousuario_cpup where ccpu_llave =  ctur_cuentapermiso);

ALTER TABLE turno_turp
	DROP COLUMN ctur_cuentapermiso,
	ALTER COLUMN ctur_cuenta SET NOT NULL;

ALTER TABLE movimiento_movp
	DROP COLUMN cmov_cuentadestino,
	DROP COLUMN cmov_categoria,
	DROP COLUMN cmov_cuentapermisousuario,
	DROP COLUMN cmov_cuentapermisousuariodestino,
	ALTER COLUMN cmov_cuenta SET NOT NULL;

--DROP TABLE cuentapermisousuario_cpup;

--DROP TABLE catalogo_catp;

ALTER TABLE turno_turp
	ADD CONSTRAINT fk_turnocuenta FOREIGN KEY (ctur_cuenta) REFERENCES cuenta_cuep(ccue_llave);