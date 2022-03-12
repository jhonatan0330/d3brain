
COMMENT ON TABLE usuario_usrp IS '2018-01-15';

COMMENT ON TABLE usuariosesion_ussp IS '2018.01.15.06';

ALTER TABLE pedidoventa_pdvp
	DROP CONSTRAINT fk_pedidoventaproceso;

ALTER TABLE pedidoventa_pdvp
	DROP COLUMN cpdv_proceso;

