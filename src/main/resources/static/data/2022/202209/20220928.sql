COMMENT ON TABLE usuario_usrp IS '2022-09-28';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_212' , 'E', 'IMAGEN EN REPORTE', 'REPORTE_IMAGEN', 'REQUISITO', true);
	
update propiedad_ppdp pp 
set cppd_valor = replace(cppd_valor, 'fontName="Arial"', '')
where cppd_propiedadvalor = 'PROP_138'
and cppd_estado = 'A';