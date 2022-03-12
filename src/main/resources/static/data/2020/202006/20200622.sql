COMMENT ON TABLE usuario_usrp IS '2020-06-22';
COMMENT ON TABLE usuariosesion_ussp IS '2020.06.22.00';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, cpvd_motivo, bpvd_textoculto) 
	VALUES('PROP_138' , 'E', 'JRXML', 'REPORTE_JRXML', 'www.softwareparati.com', 'REQUISITO', 'Esquema del reporte', true);

ALTER TABLE propiedad_ppdp
	ALTER COLUMN cppd_valor TYPE character varying(120000) /* TYPE change - table: propiedad_ppdp original: character varying(4000) new: character varying(120000) */;

insert into cambio_cmbp (ccmb_llave,ccmb_nombre,ccmb_motivo,dcmb_fecha) values ('SC_20200622','SC_20200622','Colocar los jrxml como propiedades',now());

INSERT INTO propiedad_ppdp(cppd_llave, cppd_campo, cppd_propiedadvalor,  dppd_fechadefinicion,  cppd_motivo,  cppd_cambiocreacion, cppd_tipo, cppd_valor)
select replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''), crpb_llave , 'PROP_138',  now(),  'Esquema del reporte',  'SC_20200622',  'E', crpb_jaspertext from reportebase_rpbp;

ALTER TABLE reportebase_rpbp
	DROP COLUMN crpb_jaspertext;