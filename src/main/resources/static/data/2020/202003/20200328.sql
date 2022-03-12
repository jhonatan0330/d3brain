COMMENT ON TABLE usuario_usrp IS '2020-03-28';

COMMENT ON TABLE usuariosesion_ussp IS '2020.03.28.00';

ALTER TABLE categoriaproducto_cprp
	ADD COLUMN bcpr_inventarios boolean DEFAULT false NOT NULL,
	ADD COLUMN bcpr_camposadicionales boolean DEFAULT false NOT NULL,
	ADD COLUMN bcpr_composicion boolean DEFAULT false NOT NULL,
	ADD COLUMN bcpr_promociones boolean DEFAULT false NOT NULL;

update categoriaproducto_cprp set bcpr_inventarios = true where ccpr_llave in (select distinct(cpro_categoria) from productoinventario_pinp, producto_prop where cpin_producto = cpro_llave and cpro_estado = 'A' and cpin_estado ='A');
update categoriaproducto_cprp set bcpr_camposadicionales = true where ccpr_llave in (select distinct(cpro_categoria) from productocaracteristica_pcrp, producto_prop where cpcr_base = cpro_llave and cpro_estado = 'A' and cpcr_estado ='A');
update categoriaproducto_cprp set bcpr_composicion = true where ccpr_llave in (select distinct(cpro_categoria) from productoinventariodescuento_pidp, producto_prop where cpid_producto = cpro_llave and cpro_estado = 'A' and cpid_estado ='A');
update categoriaproducto_cprp set bcpr_promociones = true where ccpr_llave in (select distinct(cpro_categoria) from usuariorolproducto_urpp, producto_prop where curp_producto = cpro_llave and cpro_estado = 'A' and curp_estado ='A');

insert into cambio_cmbp (ccmb_llave,ccmb_nombre,ccmb_motivo,dcmb_fecha) values ('SC_20200328','SC_20200328','Mejora de categorias',now());

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, cpvd_origencategoria, cpvd_motivo, bpvd_multiple ) 
	VALUES('PROP_127' , 'C', 'CATEGORIA PRODUCTO', 'DETALLE_CATEGORIA', 'www.softwareparati.com', 'REQUISITO', 'J', 'Consulta los productos de categoria ', true);

INSERT INTO propiedad_ppdp(cppd_llave, cppd_campo, cppd_valor, cppd_texto, cppd_propiedadvalor, dppd_fechadefinicion, cppd_motivo, cppd_cambiocreacion, cppd_tipo)
select cat.cdpc_llave, car.cdpc_llave, cat.cdpc_categoria , ccpr_nombre, 'PROP_127', now(),  'Consulta los productos de categoria ' || ccpr_nombre, 'SC_20200328', 'C' 
from documentoplantillacategoria_dpcp cat, documentoplantillacaracteristica_dpcp car, categoriaproducto_cprp 
where cat.cdpc_plantilla = car.cdpc_plantilla and cat.cdpc_estado = 'A' and car.cdpc_estado = 'A' and car.cdpc_formato = 'J'
and ccpr_llave = cat.cdpc_categoria and ccpr_estado = 'A';

DROP TABLE documentoplantillacategoria_dpcp;

ALTER TABLE categoriaproducto_cprp
	ADD COLUMN ncpr_promocionbase integer DEFAULT 0 NOT NULL;

update categoriaproducto_cprp set ncpr_promocionbase =30 where bcpr_promociones = true;

ALTER TABLE categoriaproducto_cprp
	DROP COLUMN bcpr_promociones;

update propiedadvalordefinido_pvdp set bpvd_textoculto = true where cpvd_llave = 'PROP_41';
update propiedadvalordefinido_pvdp set bpvd_textoculto = true where cpvd_llave = 'PROP_29';
update propiedadvalordefinido_pvdp set bpvd_textoculto = true where cpvd_llave = 'PROP_109';
update propiedadvalordefinido_pvdp set bpvd_textoculto = true where cpvd_llave = 'PROP_54';
update propiedadvalordefinido_pvdp set bpvd_textoculto = true where cpvd_llave = 'PROP_59';
update propiedadvalordefinido_pvdp set bpvd_textoculto = true where cpvd_llave = 'PROP_58';
update propiedadvalordefinido_pvdp set bpvd_textoculto = true where cpvd_llave = 'PROP_90';
update propiedadvalordefinido_pvdp set bpvd_textoculto = true where cpvd_llave = 'PROP_120';
update propiedadvalordefinido_pvdp set bpvd_textoculto = true where cpvd_llave = 'PROP_69';
update propiedadvalordefinido_pvdp set bpvd_textoculto = true where cpvd_llave = 'PROP_74';
