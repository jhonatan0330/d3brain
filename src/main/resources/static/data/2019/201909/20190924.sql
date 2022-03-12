
COMMENT ON TABLE usuario_usrp IS '2019-09-24';
COMMENT ON TABLE usuariosesion_ussp IS '2019.09.23.00';

ALTER TABLE reportebase_rpbp
	ADD COLUMN nrpb_version integer DEFAULT 0 NOT NULL,
	ADD COLUMN crpb_descripcion character varying(100);

update reportebase_rpbp set nrpb_version =  1;

INSERT INTO reportebase_rpbp (crpb_llave, crpb_plantilla, crpb_nombre, crpb_codigo, crpb_jaspertext, nrpb_version,  crpb_estado) 
	select substring('SR1-' || crpb_llave, 0, 32), crpb_plantilla,  'P_SUBREPORT_1_' || crpb_nombre, 'P_SUBREPORT_1', crpb_subreporte, 1, 'I'  
		from reportebase_rpbp where crpb_subreporte is not null;

INSERT INTO propiedad_ppdp (cppd_llave, cppd_tipo, cppd_campo, cppd_key, cppd_valor, cppd_texto) 
	select substring('SR1-' || crpb_llave, 0, 32), 'E', crpb_llave, 'P_SUBREPORT_1', substring('SR1-' || crpb_llave, 0, 32), 'P_SUBREPORT_1_' || crpb_nombre 
		from reportebase_rpbp where crpb_subreporte is not null;

INSERT INTO reportebase_rpbp (crpb_llave, crpb_plantilla, crpb_nombre, crpb_codigo, crpb_jaspertext, nrpb_version,  crpb_estado) 
	select substring('SR2-' || crpb_llave, 0, 32), crpb_plantilla,  'P_SUBREPORT_2_' || crpb_nombre, 'P_SUBREPORT_2', crpb_subreporte2, 1, 'I'  
		from reportebase_rpbp where crpb_subreporte2 is not null;

INSERT INTO propiedad_ppdp (cppd_llave, cppd_tipo, cppd_campo, cppd_key, cppd_valor, cppd_texto) 
	select substring('SR2-' || crpb_llave, 0, 32), 'E', crpb_llave, 'P_SUBREPORT_2', substring('SR2-' || crpb_llave, 0, 32), 'P_SUBREPORT_2_' || crpb_nombre
		from reportebase_rpbp where crpb_subreporte2 is not null;

INSERT INTO reportebase_rpbp (crpb_llave, crpb_plantilla, crpb_nombre, crpb_codigo, crpb_jaspertext, nrpb_version,  crpb_estado) 
	select substring('SREX-' || crpb_llave, 0, 32), crpb_plantilla,  'P_SUBREPORT_EX_' || crpb_nombre, 'P_SUBREPORT_EX', crpb_jasperexcel, 1, 'I'  
		from reportebase_rpbp where crpb_jasperexcel is not null;

INSERT INTO propiedad_ppdp (cppd_llave, cppd_tipo, cppd_campo, cppd_key, cppd_valor, cppd_texto) 
	select substring('SREX-' || crpb_llave, 0, 32), 'E', crpb_llave, 'P_SUBREPORT_EX', substring('SREX-' || crpb_llave, 0, 32), 'P_SUBREPORT_EX_' || crpb_nombre 
		from reportebase_rpbp where crpb_jasperexcel is not null;
	
ALTER TABLE reportebase_rpbp
	DROP COLUMN crpb_subreporte,
	DROP COLUMN crpb_subreporte2,
	DROP COLUMN crpb_jasperexcel;

