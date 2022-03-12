COMMENT ON TABLE usuario_usrp IS '2019-07-09';
COMMENT ON TABLE usuariosesion_ussp IS '2019.07.09.00';

CREATE TABLE mensajeplantilla_mplp (
	cmpl_llave character varying(32) NOT NULL,
	cmpl_transicion character varying(32) NOT NULL,
	cmpl_texto character varying(4000) NOT NULL,
	bmpl_creador boolean DEFAULT false NOT NULL,
	bmpl_asignado boolean DEFAULT false NOT NULL,
	cmpl_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

ALTER TABLE mensaje_msjp
	ADD COLUMN cmsj_usuario character varying(32) NOT NULL,
	ADD COLUMN dmsj_fecha timestamp with time zone NOT NULL,
	ADD COLUMN cmsj_documento character varying(32) NOT NULL,
	ADD COLUMN cmsj_template character varying(32) NOT NULL,
	ADD COLUMN cmsj_mensaje character varying(4000) NOT NULL,
	ADD COLUMN bmsj_leido boolean DEFAULT false NOT NULL,
	ADD COLUMN bmsj_enviado boolean DEFAULT false NOT NULL;

ALTER TABLE mensajeplantilla_mplp
	ADD CONSTRAINT pk_mensajeplantilla_mplp PRIMARY KEY (cmpl_llave);

ALTER TABLE mensaje_msjp
	ADD CONSTRAINT fk_mensajeusuario FOREIGN KEY (cmsj_usuario) REFERENCES usuario_usrp(cusr_llave);

ALTER TABLE mensajeplantilla_mplp
	ADD CONSTRAINT fk_mensajeplantillatransicion FOREIGN KEY (cmpl_transicion) REFERENCES procesotransicion_ptrp(cptr_llave);

update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext, 'and cpdv_llave = cdrd_documento', 'and cpdv_llave = cdpv_documento');
update reportebase_rpbp set crpb_jasperexcel = replace(crpb_jasperexcel, 'and cpdv_llave = cdrd_documento', 'and cpdv_llave = cdpv_documento');
update reportebase_rpbp set crpb_subreporte = replace(crpb_subreporte, 'and cpdv_llave = cdrd_documento', 'and cpdv_llave = cdpv_documento');
update reportebase_rpbp set crpb_subreporte2 = replace(crpb_subreporte2, 'and cpdv_llave = cdrd_documento', 'and cpdv_llave = cdpv_documento');

update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext, 'and cdrd_detalle = cdpv_llave', '');
update reportebase_rpbp set crpb_jasperexcel = replace(crpb_jasperexcel, 'and cdrd_detalle = cdpv_llave', '');
update reportebase_rpbp set crpb_subreporte = replace(crpb_subreporte, 'and cdrd_detalle = cdpv_llave', '');
update reportebase_rpbp set crpb_subreporte2 = replace(crpb_subreporte2, 'and cdrd_detalle = cdpv_llave', '');

update reportebase_rpbp set crpb_jaspertext = replace(crpb_jaspertext, 'cppr_estado', 'cppd_estado');
update reportebase_rpbp set crpb_jasperexcel = replace(crpb_jasperexcel, 'cppr_estado', 'cppd_estado');
update reportebase_rpbp set crpb_subreporte = replace(crpb_subreporte, 'cppr_estado', 'cppd_estado');
update reportebase_rpbp set crpb_subreporte2 = replace(crpb_subreporte2, 'cppr_estado', 'cppd_estado');
