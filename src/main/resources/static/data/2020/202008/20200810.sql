
COMMENT ON TABLE usuario_usrp IS '2020-08-10';

ALTER TABLE organizacion_orgp
	DROP COLUMN corg_invitado,
	ADD COLUMN corg_usuariosystem character varying(32),
	ADD COLUMN corg_servidorcorreo character varying(32);

ALTER TABLE pedidoventa_pdvp
	ALTER COLUMN cpdv_funcionario SET NOT NULL;

ALTER TABLE servidor_serp
	ADD COLUMN cser_servidorrespaldo character varying(32);
