COMMENT ON TABLE usuario_usrp IS '2024-08-10';

ALTER TABLE tarifa_tarp ADD ctar_documento varchar(32);

ALTER TABLE tarifa_tarp ADD ctar_productodocumento varchar(32);

update tarifa_tarp 
set ctar_productodocumento = (select cpro_documento from producto_prop where cpro_llave  = ctar_producto)
where ctar_producto is not null;

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple)
	SELECT 'PROP_265' , 'L', 'REPORTE DE MODULO', 'REPORT_MODULE_REFERENCE', 'REQUISITO', true
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_265');
	