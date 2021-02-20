
COMMENT ON TABLE usuario_usrp IS '2020-09-02';
COMMENT ON TABLE usuariosesion_ussp IS '2020.09.02.00';

ALTER TABLE procesotransicionautomatica_ptap
	DROP column if exists cpta_plantillanombre;

ALTER TABLE producto_prop
	ALTER COLUMN cpro_documento SET NOT NULL;
	
ALTER TABLE bodega_bodp
	DROP COLUMN cbod_nombre,
	DROP COLUMN cbod_codigo;

update propiedad_ppdp set cppd_valor = replace(cppd_valor,'cbod_nombre', 'movimiento_descripcion(cbod_documento)') 
where cppd_propiedadvalor = 'PROP_138'
and cppd_valor like '%cbod_nombre%';

delete from permiso_perp where cper_modulo in (select cmdc_llave from modulocontratado_mdcp where cmdc_modulo in ('Productos','UIProceso')); 
delete from modulocontratado_mdcp where cmdc_modulo in ('Productos','UIProceso'); 
delete from modulo_modp where cmod_llave in ('Productos','UIProceso');

update propiedadvalordefinido_pvdp set cpvd_codigo = 'PRODUCTO_CAMPO_CANTIDAD', cpvd_nombre = 'PRODUCTO CAMPO CANTIDAD' 
where cpvd_llave = 'PROP_150';