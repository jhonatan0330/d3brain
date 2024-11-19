COMMENT ON TABLE usuario_usrp IS '2024-11-17';


INSERT INTO documentoplantillacaracteristica_dpcp
(cdpc_llave, cdpc_plantilla, ndpc_orden,  cdpc_nombre, cdpc_codigo, cdpc_formato)
select replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''), dd.cdpl_llave, 9, 'DESCRIPCION', 'DESCRIPCION', 'T' from propiedad_ppdp pp
	inner join documentoplantilla_dplp dd on (dd.cdpl_llave = cppd_campo and dd.cdpl_estado = 'A')
where cppd_propiedadvalor = 'PROP_144' and cppd_estado = 'A';

INSERT INTO documentoplantillacaracteristica_dpcp
(cdpc_llave, cdpc_plantilla, ndpc_orden,  cdpc_nombre, cdpc_codigo, cdpc_formato)
select replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''), dd.cdpl_llave, 10, 'BASE', 'BASE', 'Z' from propiedad_ppdp pp
	inner join documentoplantilla_dplp dd on (dd.cdpl_llave = cppd_campo and dd.cdpl_estado = 'A')
where cppd_propiedadvalor = 'PROP_144' and cppd_estado = 'A';

INSERT INTO pedidoventacaracteristica_pvcp
(cpvc_llave, cpvc_documento, cpvc_valortext, cpvc_campo, cpvc_transaccionregistro)
select replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''), cpro_documento, cpro_descripcion,
(select cdpc_llave from documentoplantillacaracteristica_dpcp dd where dd.cdpc_plantilla = cpdv_plantilla and cdpc_codigo = 'DESCRIPCION'), cpdv_transaccion
from producto_prop pp 
	inner join pedidoventa_pdvp on (cpdv_llave = pp.cpro_documento)
where cpro_estado = 'A' and cpro_descripcion is not null;

INSERT INTO pedidoventacaracteristica_pvcp
(cpvc_llave, cpvc_documento, cpvc_valoropcion, cpvc_valortext, cpvc_campo, cpvc_transaccionregistro)
select replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''), pp.cpro_documento, 
(select a.cpro_documento from producto_prop a where a.cpro_llave = pp.cpro_productobase),
(select descripcion(a.cpro_documento) from producto_prop a where a.cpro_llave = pp.cpro_productobase),
(select cdpc_llave from documentoplantillacaracteristica_dpcp dd where dd.cdpc_plantilla = cpdv_plantilla and cdpc_codigo = 'BASE'), cpdv_transaccion
from producto_prop pp 
	inner join pedidoventa_pdvp on (cpdv_llave = pp.cpro_documento)
where pp.cpro_estado = 'A' and pp.cpro_productobase is not null;

INSERT INTO cambio_cmbp(ccmb_llave,  ccmb_nombre,  ccmb_motivo,  dcmb_fecha)
	VALUES('SC_20241117',  'SC_20241117',  'Productos con propiedades de descripcion y base',  now());

INSERT INTO propiedad_ppdp
(cppd_llave, cppd_campo, cppd_valor, cppd_propiedadvalor, dppd_fechadefinicion, dppd_fechaimplementacion,  cppd_cambiocreacion, cppd_tipo)
select replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''), 
	dc.cdpc_llave, '1', 'PROP_134', now(), now(), 'SC_20241117', 'C' 
from propiedad_ppdp pp
	inner join documentoplantilla_dplp dd on (dd.cdpl_llave = cppd_campo and dd.cdpl_estado = 'A')
	inner join documentoplantillacaracteristica_dpcp dc on (dc.cdpc_plantilla = dd.cdpl_llave and dc.cdpc_codigo = 'DESCRIPCION')
where cppd_propiedadvalor = 'PROP_144' and cppd_estado = 'A';

INSERT INTO propiedad_ppdp
(cppd_llave, cppd_campo, cppd_valor, cppd_propiedadvalor, dppd_fechadefinicion, dppd_fechaimplementacion,  cppd_cambiocreacion, cppd_tipo)
select replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''), 
	dc.cdpc_llave, '1', 'PROP_134', now(), now(), 'SC_20241117', 'C' 
from propiedad_ppdp pp
	inner join documentoplantilla_dplp dd on (dd.cdpl_llave = cppd_campo and dd.cdpl_estado = 'A')
	inner join documentoplantillacaracteristica_dpcp dc on (dc.cdpc_plantilla = dd.cdpl_llave and dc.cdpc_codigo = 'BASE')
where cppd_propiedadvalor = 'PROP_144' and cppd_estado = 'A';

ALTER TABLE productoinventariodescuento_pidp ADD cpid_documento varchar(32);

ALTER TABLE productoinventario_pinp ADD cpin_documento varchar(32);



INSERT INTO documentoplantillacaracteristica_dpcp
(cdpc_llave, cdpc_plantilla, ndpc_orden,  cdpc_nombre, cdpc_codigo, cdpc_formato)
select replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''), dd.cdpl_llave, 12, 'COMPOSICION', 'COMPOSICION', 'Z' from categoriaproducto_cprp cc
	inner join documentoplantilla_dplp dd on (dd.cdpl_llave = cc.ccpr_plantilla)
where ccpr_estado = 'A' and bcpr_composicion = true;


INSERT INTO propiedad_ppdp
(cppd_llave, cppd_campo, cppd_valor, cppd_propiedadvalor, dppd_fechadefinicion, dppd_fechaimplementacion,  cppd_cambiocreacion, cppd_tipo)
select replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''), 
	dc.cdpc_llave, '1', 'PROP_32', now(), now(), 'SC_20241117', 'C'  
from categoriaproducto_cprp cc
	inner join documentoplantilla_dplp dd on (dd.cdpl_llave = cc.ccpr_plantilla)
	inner join documentoplantillacaracteristica_dpcp dc on (dc.cdpc_plantilla = dd.cdpl_llave and dc.cdpc_codigo = 'COMPOSICION')
where ccpr_estado = 'A' and bcpr_composicion = true;


INSERT INTO propiedad_ppdp
(cppd_llave, cppd_campo, cppd_valor, cppd_propiedadvalor, dppd_fechadefinicion, dppd_fechaimplementacion,  cppd_cambiocreacion, cppd_tipo)
select replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''), 
	dc.cdpc_llave, '1', 'PROP_33', now(), now(), 'SC_20241117', 'C'  
from categoriaproducto_cprp cc
	inner join documentoplantilla_dplp dd on (dd.cdpl_llave = cc.ccpr_plantilla)
	inner join documentoplantillacaracteristica_dpcp dc on (dc.cdpc_plantilla = dd.cdpl_llave and dc.cdpc_codigo = 'COMPOSICION')
where ccpr_estado = 'A' and bcpr_composicion = true;

INSERT INTO documentoplantillacaracteristica_dpcp
(cdpc_llave, cdpc_plantilla, ndpc_orden,  cdpc_nombre, cdpc_codigo, cdpc_formato)
select replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''), dd.cdpl_llave, 11, 'INVENTARIO', 'INVENTARIO', 'Z'
from categoriaproducto_cprp cc
	inner join documentoplantilla_dplp dd on (dd.cdpl_llave = cc.ccpr_plantilla)
where ccpr_estado = 'A' and bcpr_inventarios = true;


INSERT INTO propiedad_ppdp
(cppd_llave, cppd_campo, cppd_valor, cppd_propiedadvalor, dppd_fechadefinicion, dppd_fechaimplementacion,  cppd_cambiocreacion, cppd_tipo)
select replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''), 
	dc.cdpc_llave, '1', 'PROP_32', now(), now(), 'SC_20241117', 'C'  
from categoriaproducto_cprp cc
	inner join documentoplantilla_dplp dd on (dd.cdpl_llave = cc.ccpr_plantilla)
	inner join documentoplantillacaracteristica_dpcp dc on (dc.cdpc_plantilla = dd.cdpl_llave and dc.cdpc_codigo = 'INVENTARIO')
where ccpr_estado = 'A' and bcpr_composicion = true;


INSERT INTO propiedad_ppdp
(cppd_llave, cppd_campo, cppd_valor, cppd_propiedadvalor, dppd_fechadefinicion, dppd_fechaimplementacion,  cppd_cambiocreacion, cppd_tipo)
select replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''), 
	dc.cdpc_llave, '1', 'PROP_33', now(), now(), 'SC_20241117', 'C'  
from categoriaproducto_cprp cc
	inner join documentoplantilla_dplp dd on (dd.cdpl_llave = cc.ccpr_plantilla)
	inner join documentoplantillacaracteristica_dpcp dc on (dc.cdpc_plantilla = dd.cdpl_llave and dc.cdpc_codigo = 'INVENTARIO')
where ccpr_estado = 'A' and bcpr_composicion = true;

