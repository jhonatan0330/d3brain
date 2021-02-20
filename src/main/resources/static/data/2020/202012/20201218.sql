
COMMENT ON TABLE usuario_usrp IS '2020-12-18';
COMMENT ON TABLE usuariosesion_ussp IS '2020.12.18.00';

CREATE TABLE reporteejecucion_rejp (
	crej_llave character varying(32) NOT NULL,
	crej_reporte character varying(32) NOT NULL,
	crej_documento character varying(32),
	drej_fechainicio timestamp with time zone NOT NULL,
	drej_fechafin timestamp with time zone NOT NULL,
	crej_error character varying(4000),
	crej_usuario character varying(32),
	crej_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

ALTER TABLE reporteejecucion_rejp
	ADD CONSTRAINT pk_reporteejecucion_rejp PRIMARY KEY (crej_llave);

ALTER TABLE reporteejecucion_rejp
	ADD CONSTRAINT fk_reporteejecuciondocumento FOREIGN KEY (crej_documento) REFERENCES pedidoventa_pdvp(cpdv_llave);

ALTER TABLE reporteejecucion_rejp
	ADD CONSTRAINT fk_reporteejecucionreporte FOREIGN KEY (crej_reporte) REFERENCES reportebase_rpbp(crpb_llave);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre,  cpvd_grupo, bpvd_multiple, bpvd_solicitamotivo) 
	VALUES('PROP_159' , 'T', 'GENERA_DOCUMENTO_FUNCION_SQL', 'FUNCION PARA GENERAR UN CAMPO EN DOCUMENTO', 'REQUISITO', true, true);
