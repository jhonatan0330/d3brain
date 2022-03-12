COMMENT ON TABLE usuario_usrp IS '2020-01-31';

ALTER TABLE procesodecision_pdcp
	ADD COLUMN npdc_avance integer DEFAULT 0 NOT NULL;

update propiedadvalordefinido_pvdp 
set cpvd_nombre = 'FUNCION DESTINATARIOS DEL MENSAJE',cpvd_codigo = 'MENSAJE_DESTINATARIOS_SQL'
where cpvd_llave in ('PROP_58', 'PROP_69', 'PROP_74');
