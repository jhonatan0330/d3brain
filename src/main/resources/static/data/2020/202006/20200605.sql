COMMENT ON TABLE usuario_usrp IS '2020-06-05';

DROP INDEX ix_pedidoventa_plantilla;

CREATE INDEX ix_pedidoventa_plantillafecha ON pedidoventa_pdvp USING btree (cpdv_plantilla, dpdv_fecha)