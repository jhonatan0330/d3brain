COMMENT ON TABLE usuario_usrp IS '2024-08-31';

ALTER TABLE categoriaproducto_cprp  ADD ccpr_plantilla varchar(32);

update categoriaproducto_cprp 
set ccpr_plantilla = (select cdpl_llave from propiedad_ppdp pp
inner join documentoplantilla_dplp dd on dd.cdpl_llave = pp.cppd_campo 
where cppd_valor = ccpr_llave
and cppd_estado = 'A' and cdpl_estado = 'A')
where ccpr_estado = 'A';

update categoriaproducto_cprp
set ccpr_plantilla = (select distinct (pp2.cpdv_plantilla) from producto_prop pp 
	inner join pedidoventa_pdvp pp2 on pp2.cpdv_llave = pp.cpro_documento 
where pp.cpro_categoria = ccpr_llave)
where ccpr_plantilla is null;


INSERT INTO public.documentoplantilla_dplp
(cdpl_llave, cdpl_codigo, cdpl_nombre,  cdpl_imagen,  cdpl_proceso)
select 
	ccpr_llave, 'C-'|| substring(upper(ccpr_llave), 1,3), 
	'FORMULARIO DETALLE '|| dd.cdpl_nombre , 'https://fs.softwareparati.com/modulo.png', dd.cdpl_proceso 
from productocaracteristica_pcrp pp 
inner join producto_prop pp2 on (cpcr_base = pp2.cpro_llave)
inner join pedidoventa_pdvp pp3 on pp3.cpdv_llave = pp2.cpro_documento  and pp3.cpdv_estado = 'A'
inner join categoriaproducto_cprp cc on cc.ccpr_llave = pp2.cpro_categoria
inner join documentoplantilla_dplp dd on dd.cdpl_llave = cc.ccpr_plantilla
where pp.cpcr_estado = 'A' and pp2.cpro_estado = 'A' and cc.ccpr_estado = 'A'
group by ccpr_llave, dd.cdpl_nombre , cdpl_proceso;

INSERT INTO public.documentoplantillacaracteristica_dpcp
( cdpc_plantilla, cdpc_nombre, cdpc_codigo, ndpc_orden, cdpc_formato, cdpc_llave)
select 
	 cc.ccpr_llave , pp.cpcr_nombre , pp.cpcr_codigo || '-' || pp3.cpdv_nombre , pp.npcr_orden , pp.cpcr_formato , pp.cpcr_llave 
from productocaracteristica_pcrp pp 
inner join producto_prop pp2 on (cpcr_base = pp2.cpro_llave)
inner join pedidoventa_pdvp pp3 on pp3.cpdv_llave = pp2.cpro_documento  and pp3.cpdv_estado = 'A'
inner join categoriaproducto_cprp cc on cc.ccpr_llave = pp2.cpro_categoria
where pp.cpcr_estado = 'A' and pp2.cpro_estado = 'A' and cc.ccpr_estado = 'A';


INSERT INTO public.pedidoventa_pdvp
(cpdv_llave, dpdv_fecharegistro, dpdv_fecha, cpdv_nombre, cpdv_plantilla,  cpdv_funcionario, cpdv_estado, mpdv_consecutivo,  cpdv_transaccion)
select dd2.cdpv_llave , dt.dtra_fecha, dt.dtra_fecha, row_number() over(), pp2.cpro_categoria, ctra_usuario, dd2.cdpv_estado, row_number() over() , dt.ctra_llave
from detallecaracteristicaproducto_dcpp dd 
inner join detallepedidoventa_dpvp dd2 on (dd2.cdpv_llave = dd.cdcp_entidad)
inner join documentotransaccion_trap dt on (dt.ctra_llave = dd2.cdpv_transaccionregistro)
inner join producto_prop pp2 on ( pp2.cpro_llave = dd2.cdpv_producto)
where dd.cdcp_estado = 'A'
group by dd2.cdpv_llave, dt.ctra_llave , pp2.cpro_categoria;

INSERT INTO public.pedidoventacaracteristica_pvcp
(cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, dpvc_valorfecha, cpvc_valoropcion, mpvc_valornumero, cpvc_estado, cpvc_transaccionregistro, cpvc_transaccioninactivo)
select dd.cdcp_llave , dd.cdcp_entidad , dd.cdcp_campo , dd.cdcp_valortext , dd.ddcp_valorfecha , dd.cdcp_valoropcion , dd.mdcp_valornumero, dd.cdcp_estado , dd.cdcp_transaccionregistro, dd.cdcp_transaccioninactivo 
from detallecaracteristicaproducto_dcpp dd 
where dd.cdcp_estado = 'A'  and (cdcp_valortext is not null);

ALTER TABLE detallepedidoventa_dpvp ADD cdpv_detalleid varchar(32) NULL;

update detallepedidoventa_dpvp 
		set cdpv_detalleid = cdpv_llave
		where cdpv_llave in (
		select dd2.cdpv_llave
			from detallecaracteristicaproducto_dcpp dd 
			inner join detallepedidoventa_dpvp dd2 on (dd2.cdpv_llave = dd.cdcp_entidad)
			inner join documentotransaccion_trap dt on (dt.ctra_llave = dd2.cdpv_transaccionregistro)
			inner join producto_prop pp2 on ( pp2.cpro_llave = dd2.cdpv_producto)
			where dd.cdcp_estado = 'A'
		);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo)
	SELECT 'PROP_266' , 'L', 'TIPO PRODUCTO FORMULARIO DETALLADO', 'TIPO_PRODUCTO_FORMULARIO_DETALLADO', 'REQUISITO'
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_266');
	
update propiedad_ppdp pp 
set cppd_campo = (select ccpr_plantilla from producto_prop inner join categoriaproducto_cprp on (ccpr_llave = cpro_categoria) where cpro_estado = 'A' and cpro_llave = cppd_campo)
where pp.cppd_campo in (select cpro_llave from producto_prop where cpro_estado = 'A')
and cppd_estado  = 'A' and cppd_tipo = 'L';


INSERT INTO cambio_cmbp(ccmb_llave,  ccmb_nombre,  ccmb_motivo,  dcmb_fecha)
	VALUES('SC_20240831',  'SC_20240831',  'Actualizacion de formuladrios detalles de productos',  now());

INSERT INTO public.propiedad_ppdp
(cppd_llave, cppd_propiedadvalor, cppd_tipo, cppd_campo, cppd_valor, cppd_texto, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion)
select 
	replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''),
	'PROP_266' , 'L', ccpr_plantilla, ccpr_llave, 'FORMULARIO DETALLE '|| dd.cdpl_nombre,  now(), now(), 'SC_20240831'
from productocaracteristica_pcrp pp 
inner join producto_prop pp2 on (cpcr_base = pp2.cpro_llave)
inner join pedidoventa_pdvp pp3 on pp3.cpdv_llave = pp2.cpro_documento  and pp3.cpdv_estado = 'A'
inner join categoriaproducto_cprp cc on cc.ccpr_llave = pp2.cpro_categoria
inner join documentoplantilla_dplp dd on dd.cdpl_llave = cc.ccpr_plantilla
where pp.cpcr_estado = 'A' and pp2.cpro_estado = 'A' and cc.ccpr_estado = 'A'
group by ccpr_llave, dd.cdpl_nombre;

update  documentoplantillacaracteristica_dpcp dd 
set cdpc_codigo = replace ( cdpc_codigo, '-CAJA', '') 
where cdpc_plantilla in (select cc.ccpr_llave from categoriaproducto_cprp cc where cc.ccpr_estado = 'A');

ALTER TABLE detallecaracteristicaproducto_dcpp RENAME TO detallecaracteristicaproducto_dcpp_old;

ALTER TABLE productocaracteristica_pcrp RENAME TO productocaracteristica_pcrp_old;