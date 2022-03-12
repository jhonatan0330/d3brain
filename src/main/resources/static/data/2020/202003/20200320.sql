COMMENT ON TABLE usuario_usrp IS '2020-03-20';
COMMENT ON TABLE usuariosesion_ussp IS '2020.03.20.00';

ALTER TABLE pedidoventa_pdvp
	ADD COLUMN cpdv_transaccion character varying(32);

CREATE INDEX ix_documentotransaccion_documento ON documentotransaccion_trap USING btree (ctra_documento);

update pedidoventa_pdvp set cpdv_transaccion = 
	(select ctra_llave from documentotransaccion_trap where ctra_documento = cpdv_llave limit 1);

ALTER TABLE pedidoventa_pdvp
	ALTER COLUMN cpdv_transaccion SET NOT NULL;
	
ALTER TABLE documentotransaccion_trap
	DROP COLUMN ctra_documento;
	
ALTER TABLE detallepedidoventa_dpvp
	ADD CONSTRAINT fk_detallepedidoventatransaccionregistro FOREIGN KEY (cdpv_transaccionregistro) REFERENCES documentotransaccion_trap(ctra_llave);

ALTER TABLE documentorelaciongestor_drgp
	ADD CONSTRAINT fk_documentorelaciongestorvalores FOREIGN KEY (cdrg_valores) REFERENCES pedidoventadinero_pvdp(cpvd_llave);

ALTER TABLE pedidoventa_pdvp
	ADD CONSTRAINT fk_pedidoventatransaccion FOREIGN KEY (cpdv_transaccion) REFERENCES documentotransaccion_trap(ctra_llave);
