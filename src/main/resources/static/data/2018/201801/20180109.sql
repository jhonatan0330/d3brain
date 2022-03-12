/**
* Creo la tabla del dinero separando el dinero de la tabla de pedidoventa_pdvp
* Los reportes los vinculo a un solo documento
*/
COMMENT ON TABLE usuario_usrp IS '2018-01-09';

COMMENT ON TABLE usuariosesion_ussp IS '2018.01.09.03';

update pedidoventa_pdvp set npdv_version = 1 where npdv_version = 0;

DROP VIEW IF EXISTS campo_usuario;

delete from documentorelacionexpediente_dexp  where cdex_expedientedetalle in ( select cpdv_llave from pedidoventa_pdvp where cpdv_plantilla  = 'FORMATO_EXPORTAR') ;
delete from documentopermisodocumento_dpdp where cdpd_plantillaorigen = 'FORMATO_EXPORTAR';
delete from documentopermisodocumento_dpdp where cdpd_plantilladestino = 'FORMATO_EXPORTAR';
delete from documentoplantillarol_dprp  where cdpr_plantilla  = 'FORMATO_EXPORTAR';
delete from pedidoventacaracteristica_pvcp  where cpvc_campo in (select cdpc_llave from documentoplantillacaracteristica_dpcp  where cdpc_plantilla  = 'FORMATO_EXPORTAR');
delete from pedidoventa_pdvp where cpdv_plantilla  = 'FORMATO_EXPORTAR';
delete from plantillacampocomplemento_pccp where cpcc_documentoauxiliar = 'FORMATO_EXPORTAR';
delete from plantillacampocomplemento_pccp  where cpcc_campo  in(select cdpc_llave from documentoplantillacaracteristica_dpcp  where cdpc_plantilla  = 'FORMATO_EXPORTAR');
delete from documentoplantillacaracteristica_dpcp  where cdpc_plantilla  = 'FORMATO_EXPORTAR';
delete from documentoplantilla_dplp where cdpl_llave  = 'FORMATO_EXPORTAR';
delete from pedidoventacaracteristica_pvcp  where cpvc_campo  in (select cdpc_llave from documentoplantillacaracteristica_dpcp where cdpc_codigo = 'jasperTipo');
delete from documentoplantillacaracteristica_dpcp where cdpc_codigo = 'jasperTipo';

delete from pedidoventa_pdvp where cpdv_plantilla  in (select crpb_llave from reportebase_rpbp  where crpb_estado  = 'I') and cpdv_plantilla != '2';
delete from documentoplantillarol_dprp where cdpr_plantilla in (select crpb_llave from reportebase_rpbp  where crpb_estado  = 'I') and cdpr_plantilla != '2';
delete from documentoplantilla_dplp where cdpl_llave in (select crpb_llave from reportebase_rpbp  where crpb_estado  = 'I') and cdpl_llave != '2';
delete from reporte_repp  where crep_base  in (select crpb_llave from reportebase_rpbp where crpb_estado  = 'I');
delete from reportebase_rpbp where crpb_estado  = 'I';

delete from pedidoventa_pdvp where cpdv_plantilla  in (select crpb_llave from reportebase_rpbp  where crpb_codigo  is not null) and cpdv_plantilla != '2';
delete from documentoplantillarol_dprp where cdpr_plantilla in (select crpb_llave from reportebase_rpbp  where crpb_codigo  is not null) and cdpr_plantilla != '2';
delete from documentoplantilla_dplp where cdpl_llave in (select crpb_llave from reportebase_rpbp  where crpb_codigo  is not null) and cdpl_llave != '2';




CREATE TABLE pedidoventadinero_pvdp (
	cpvd_llave character varying(32) NOT NULL,
	cpvd_documento character varying(32) NOT NULL,
	mpvd_valorsubtotal numeric(18,6) DEFAULT 0 NOT NULL,
	mpvd_valortotal numeric(18,6) DEFAULT 0 NOT NULL,
	mpvd_saldo numeric(18,6) DEFAULT 0 NOT NULL,
	dpvd_fechapago timestamp with time zone,
	cpvd_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

INSERT INTO pedidoventadinero_pvdp(cpvd_llave, cpvd_documento, mpvd_valorsubtotal, mpvd_valortotal, mpvd_saldo,dpvd_fechapago)
select cpdv_llave, cpdv_llave, mpdv_valorsubtotal, mpdv_valortotal, mpdv_saldo,dpdv_fechapago from pedidoventa_pdvp ;

ALTER TABLE pedidoventa_pdvp
	DROP COLUMN mpdv_saldo,
	DROP COLUMN dpdv_fechapago,
	DROP COLUMN mpdv_valorsubtotal,
	DROP COLUMN mpdv_valortotal;

ALTER TABLE reportebase_rpbp
	ADD COLUMN crpb_plantilla character varying(32);

ALTER TABLE pedidoventadinero_pvdp
	ADD CONSTRAINT pk_pedidoventadinero_pvdp PRIMARY KEY (cpvd_llave);

ALTER TABLE pedidoventadinero_pvdp
	ADD CONSTRAINT fk_pedidoventadinerodocumento FOREIGN KEY (cpvd_documento) REFERENCES pedidoventa_pdvp(cpdv_llave);

ALTER TABLE reportebase_rpbp
	ADD CONSTRAINT fk_reportebaseplantilla FOREIGN KEY (crpb_plantilla) REFERENCES documentoplantilla_dplp(cdpl_llave);

update reportebase_rpbp set crpb_codigo = replace(crpb_codigo,'PEDIDO_VENTA_MOVIMIENTO_', '' ) where crpb_codigo is not null;
update reportebase_rpbp set crpb_codigo = replace(crpb_codigo,'PEDIDO_VENTA_', '' ) where crpb_codigo is not null;

update reportebase_rpbp set crpb_plantilla = (select crpb_codigo from documentoplantilla_dplp  where cdpl_llave = crpb_codigo) where crpb_codigo is not null;
update reportebase_rpbp set crpb_plantilla = (select crpb_llave from documentoplantilla_dplp  where cdpl_llave = crpb_llave) where crpb_plantilla is null;

update documentoplantilla_dplp set cdpl_estado = 'A' where cdpl_reporte is not null;

ALTER TABLE documentoplantilla_dplp DROP COLUMN cdpl_reporte;

delete from pedidoventadinero_pvdp  where mpvd_valorsubtotal= 0 and mpvd_valortotal = 0 and dpvd_fechapago is null;

update documentoplantillarol_dprp set bdpr_listable = true, bdpr_crear= true, bdpr_vertodos = false where cdpr_plantilla  in (select cdpl_llave from documentoplantilla_dplp  where cdpl_imagen  is null);