COMMENT ON TABLE usuario_usrp IS '2019-12-13';
COMMENT ON TABLE usuariosesion_ussp IS '2019.12.13.00';

ALTER TABLE cambio_cmbp
	DROP COLUMN bcmb_grabando,
	ADD COLUMN ccmb_sesionactiva character varying(32);

update documentoplantilla_dplp set cdpl_proceso = 'RRHH' where cdpl_proceso is null;

ALTER TABLE documentoplantilla_dplp
	ALTER COLUMN cdpl_proceso SET NOT NULL;

ALTER TABLE propiedad_ppdp
	ADD COLUMN cppd_codigo character varying(32);

UPDATE propiedad_ppdp m
SET    cppd_codigo = sub.rn
FROM  (SELECT cppd_llave, row_number() OVER (ORDER BY dppd_fechadefinicion) AS rn FROM propiedad_ppdp) sub
WHERE  m.cppd_llave = sub.cppd_llave;

ALTER TABLE propiedad_ppdp
	ALTER COLUMN cppd_codigo set not null;

ALTER TABLE propiedad_ppdp
	ADD COLUMN bppd_necesario boolean DEFAULT false NOT NULL,
	ALTER COLUMN cppd_codigo TYPE character varying(20) /* TYPE change - table: propiedad_ppdp original: character varying(32) new: character varying(20) */;
