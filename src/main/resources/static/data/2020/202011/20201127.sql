
COMMENT ON TABLE usuario_usrp IS '2020-11-27';

CREATE TABLE z_pvc_pedidoventacaracteristica (
	cpvc_llave varchar(32) NOT NULL,
	cpvc_documento varchar(32) NOT NULL,
	cpvc_campo varchar(32) NOT NULL,
	cpvc_valortext varchar(4000) NOT NULL,
	dpvc_valorfecha timestamptz NULL,
	cpvc_valoropcion varchar(32) NULL,
	cpvc_valorauxiliar varchar(32) NULL,
	mpvc_valornumero numeric(18,6) NULL,
	cpvc_transaccionregistro varchar(32) NOT NULL,
	cpvc_transaccioninactivo varchar(32) NULL,
	cpvc_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_z_pvc_pedidoventacaracteristica PRIMARY KEY (cpvc_llave)
);
CREATE INDEX ix_z_pvc_pedidoventacaracteristica_documento ON z_pvc_pedidoventacaracteristica USING btree (cpvc_documento);
CREATE INDEX ix_z_pvc_pedidoventacaracteristica_valoropcion ON z_pvc_pedidoventacaracteristica USING btree (cpvc_valoropcion);


ALTER TABLE z_pvc_pedidoventacaracteristica ADD CONSTRAINT fk_z_pvc_pedidoventacaracteristicacampo FOREIGN KEY (cpvc_campo) REFERENCES documentoplantillacaracteristica_dpcp(cdpc_llave);
ALTER TABLE z_pvc_pedidoventacaracteristica ADD CONSTRAINT fk_z_pvc_pedidoventacaracteristicadocumento FOREIGN KEY (cpvc_documento) REFERENCES pedidoventa_pdvp(cpdv_llave);
ALTER TABLE z_pvc_pedidoventacaracteristica ADD CONSTRAINT fk_z_pvc_pedidoventacaracteristicatransaccionregistro FOREIGN KEY (cpvc_transaccionregistro) REFERENCES documentotransaccion_trap(ctra_llave);


-- public.documentorelacionexpediente_dexp definition

-- Drop table

-- DROP TABLE documentorelacionexpediente_dexp;

CREATE TABLE z_dex_documentorelacionexpediente (
	cdex_llave varchar(32) NOT NULL,
	cdex_campomaestro varchar(32) NOT NULL,
	cdex_expedientedetalle varchar(32) NOT NULL,
	cdex_transaccionregistro varchar(32) NOT NULL,
	cdex_transaccioninactivo varchar(32) NULL,
	cdex_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	mdex_valor numeric(18,6) NOT NULL DEFAULT 0,
	CONSTRAINT pk_z_dex_documentorelacionexpediente PRIMARY KEY (cdex_llave)
);

CREATE INDEX ix_z_dex_documentorelacionexpediente_campomaestro ON z_dex_documentorelacionexpediente USING btree (cdex_campomaestro);


-- public.documentorelacionexpediente_dexp foreign keys

ALTER TABLE z_dex_documentorelacionexpediente ADD CONSTRAINT fk_z_dex_documentorelacionexpedientecampomaestro FOREIGN KEY (cdex_campomaestro) REFERENCES z_pvc_pedidoventacaracteristica(cpvc_llave);
ALTER TABLE z_dex_documentorelacionexpediente ADD CONSTRAINT fk_z_dex_documentorelacionexpedienteexpedientedetalle FOREIGN KEY (cdex_expedientedetalle) REFERENCES pedidoventa_pdvp(cpdv_llave);
ALTER TABLE z_dex_documentorelacionexpediente ADD CONSTRAINT fk_z_dex_documentorelacionexpedientetransaccioninactivo FOREIGN KEY (cdex_transaccioninactivo) REFERENCES documentotransaccion_trap(ctra_llave);
ALTER TABLE z_dex_documentorelacionexpediente ADD CONSTRAINT fk_z_dex_documentorelacionexpedientetransaccionregistro FOREIGN KEY (cdex_transaccionregistro) REFERENCES documentotransaccion_trap(ctra_llave);