COMMENT ON TABLE usuario_usrp IS '2020-09-09';

update propiedad_ppdp set cppd_valor = replace (cppd_valor, 'cpro_nombre', 'movimiento_descripcion(cpro_documento)')
where cppd_propiedadvalor ='PROP_138'
and cppd_estado = 'A'
and cppd_valor  like '%cpro_nombre%';

update propiedad_ppdp set cppd_valor = replace (cppd_valor, 'cpro_codigo', '(select cpdv_nombre from pedidoventa_pdvp where cpdv_llave = cpro_documento)') 
where cppd_propiedadvalor ='PROP_138'
and cppd_estado = 'A'
and cppd_valor  like '%cpro_codigo%';

ALTER TABLE producto_prop
	DROP CONSTRAINT producto_prop_cpro_codigo_key;

ALTER TABLE producto_prop
	DROP COLUMN cpro_nombre,
	DROP COLUMN cpro_codigo;
