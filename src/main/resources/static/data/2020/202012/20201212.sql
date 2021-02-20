--Para TCM no ejecutar este script
COMMENT ON TABLE usuario_usrp IS '2020-12-12';

CREATE TABLE z_pvd_pedidoventadinero (
	cpvd_llave varchar(32) NOT NULL,
	cpvd_documento varchar(32) NOT NULL,
	mpvd_valortotal numeric(18,6) NOT NULL DEFAULT 0,
	mpvd_saldo numeric(18,6) NOT NULL DEFAULT 0,
	cpvd_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	dpvd_fecha timestamptz NOT NULL,
	CONSTRAINT pk_z_pvd_pedidoventadinero_pvdp PRIMARY KEY (cpvd_llave),
	CONSTRAINT fk_z_pvd_pedidoventadinerodocumento FOREIGN KEY (cpvd_documento) REFERENCES pedidoventa_pdvp(cpdv_llave)
);
CREATE INDEX ix_z_pvd_pedidoventadinero_documento ON z_pvd_pedidoventadinero USING btree (cpvd_documento);

CREATE TABLE Z_drg_documentorelaciongestor (
	cdrg_llave varchar(32) NOT NULL,
	cdrg_documentoprincipal varchar(32) NOT NULL,
	cdrg_documentomodificador varchar(32) NULL,
	cdrg_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	ddrg_fecha timestamptz NOT NULL,
	cdrg_estadoinicial varchar(32) NULL,
	cdrg_estadofinal varchar(32) NULL,
	cdrg_ubicacion varchar(32) NULL,
	cdrg_valores varchar(32) NULL,
	cdrg_usuario varchar(32) NOT NULL,
	ddrg_cierre timestamptz NULL,
	cdrg_nombre varchar(100) NOT NULL,
	CONSTRAINT pk_z_drg_documentorelaciongestor_drgp PRIMARY KEY (cdrg_llave),
	CONSTRAINT fk_z_drg_documentorelaciongestordocumentomodificador FOREIGN KEY (cdrg_documentomodificador) REFERENCES pedidoventa_pdvp(cpdv_llave),
	CONSTRAINT fk_z_drg_documentorelaciongestordocumentoprincipal FOREIGN KEY (cdrg_documentoprincipal) REFERENCES pedidoventa_pdvp(cpdv_llave),
	CONSTRAINT fk_z_drg_documentorelaciongestorestadofinal FOREIGN KEY (cdrg_estadofinal) REFERENCES procesoestado_pesp(cpes_llave),
	CONSTRAINT fk_z_drg_documentorelaciongestorestadoinicial FOREIGN KEY (cdrg_estadoinicial) REFERENCES procesoestado_pesp(cpes_llave),
	CONSTRAINT fk_z_drg_documentorelaciongestorusuario FOREIGN KEY (cdrg_usuario) REFERENCES usuario_usrp(cusr_llave),
	CONSTRAINT fk_z_drg_documentorelaciongestorvalores FOREIGN KEY (cdrg_valores) REFERENCES z_pvd_pedidoventadinero(cpvd_llave)
);
CREATE INDEX ix_z_drg_documentorelaciongestor_documentoprincipal ON Z_drg_documentorelaciongestor USING btree (cdrg_documentoprincipal);

CREATE OR REPLACE FUNCTION migrarcampos(_plantilla character varying, _fecha_maxima timestamp with time zone)
 RETURNS void
 LANGUAGE plpgsql
AS '
declare 
	documentos character varying[];
	campos character varying[];
begin
	select array (
		select cpdv_llave from pedidoventa_pdvp 
			where cpdv_plantilla = _plantilla and dpdv_fecha < _fecha_maxima 
			and npdv_historico is null 
			limit 50) 
		into documentos;
	select array (
		select cpvc_llave from pedidoventacaracteristica_pvcp 
			where cpvc_documento = any(documentos)) 
		into campos;
	INSERT INTO z_pvc_pedidoventacaracteristica (cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, dpvc_valorfecha, cpvc_valoropcion, cpvc_valorauxiliar, mpvc_valornumero, cpvc_transaccionregistro, cpvc_transaccioninactivo, cpvc_estado) 
		select cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, dpvc_valorfecha, cpvc_valoropcion, cpvc_valorauxiliar, mpvc_valornumero, cpvc_transaccionregistro, cpvc_transaccioninactivo, cpvc_estado
	 		from pedidoventacaracteristica_pvcp where cpvc_llave = any(campos);
	INSERT INTO z_dex_documentorelacionexpediente (cdex_llave, cdex_campomaestro, cdex_expedientedetalle, cdex_transaccionregistro, cdex_transaccioninactivo, cdex_estado, mdex_valor)
		SELECT cdex_llave, cdex_campomaestro, cdex_expedientedetalle, cdex_transaccionregistro, cdex_transaccioninactivo, cdex_estado, mdex_valor
			FROM documentorelacionexpediente_dexp where cdex_campomaestro = any(campos);
	INSERT INTO z_pvd_pedidoventadinero(cpvd_llave, cpvd_documento, mpvd_valortotal, mpvd_saldo, cpvd_estado, dpvd_fecha)
		SELECT cpvd_llave, cpvd_documento, mpvd_valortotal, mpvd_saldo, cpvd_estado, dpvd_fecha
			FROM pedidoventadinero_pvdp where cpvd_documento = any(documentos);
	INSERT INTO Z_drg_documentorelaciongestor (cdrg_llave, cdrg_documentoprincipal, cdrg_documentomodificador, cdrg_estado, ddrg_fecha, cdrg_estadoinicial, cdrg_estadofinal, cdrg_ubicacion, cdrg_valores, cdrg_usuario, ddrg_cierre, cdrg_nombre)
		SELECT cdrg_llave, cdrg_documentoprincipal, cdrg_documentomodificador, cdrg_estado, ddrg_fecha, cdrg_estadoinicial, cdrg_estadofinal, cdrg_ubicacion, cdrg_valores, cdrg_usuario, ddrg_cierre, cdrg_nombre 
			FROM documentorelaciongestor_drgp where cdrg_documentoprincipal = any(documentos);
	delete from documentorelaciongestor_drgp where cdrg_documentoprincipal = any(documentos);
	delete from pedidoventadinero_pvdp where cpvd_documento = any(documentos);
	delete from documentorelacionexpediente_dexp where cdex_campomaestro = any(campos);
	delete from pedidoventacaracteristica_pvcp where cpvc_llave = any(campos);
	update pedidoventa_pdvp set npdv_historico = 1 where cpdv_llave = any(documentos);
END;
'
;

DROP FUNCTION str_normalize(text);
