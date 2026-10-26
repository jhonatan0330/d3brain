COMMENT ON TABLE usuario_usrp IS '2026-10-25';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre, cpvd_grupo,  bpvd_textoculto)
    SELECT 'PROP_312' , 'O', 'MAIL_ADMIN_NOTIFICATION', 'CORREO NOTIFICACION ADMINISTRACION',  'REQUISITO',  true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_312');

INSERT INTO propiedad_ppdp
(cppd_llave, cppd_propiedadvalor, cppd_tipo, 
cppd_campo, cppd_valor,  dppd_fechadefinicion)
select  REPLACE(gen_random_uuid()::TEXT, '-', ''), 'PROP_312', 'O', 
corg_llave, cusr_correo , now()
from organizacion_orgp
	inner join usuario_usrp on (corg_usuariosystem = cusr_llave and cusr_estado = 'A')
where corg_principal is null and corg_estado = 'A' AND cusr_correo IS NOT NULL;

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre, cpvd_grupo,  bpvd_textoculto)
    SELECT 'PROP_313' , 'O', 'ADMIN_USER_ORGANIZATION', 'USUARIO AUTOMATICO',  'REQUISITO',  true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_313');

INSERT INTO propiedad_ppdp
(cppd_llave, cppd_propiedadvalor, cppd_tipo, 
cppd_campo, cppd_valor,  dppd_fechadefinicion)
select  REPLACE(gen_random_uuid()::TEXT, '-', ''), 'PROP_313', 'O', 
corg_llave, cusr_llave , now()
from organizacion_orgp
	inner join usuario_usrp on (corg_usuariosystem = cusr_llave and cusr_estado = 'A')
where corg_principal is null and corg_estado = 'A' AND cusr_correo IS NOT NULL;

UPDATE public.propiedadvalordefinido_pvdp SET cpvd_estado='I' WHERE cpvd_llave='PROP_93';
-- MANEJA_DINERO INACTIVO

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre, cpvd_grupo,  bpvd_textoculto)
    SELECT 'PROP_313' , 'O', 'ADMIN_USER_ORGANIZATION', 'USUARIO AUTOMATICO',  'REQUISITO',  true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_313');

INSERT INTO propiedad_ppdp
(cppd_llave, cppd_propiedadvalor, cppd_tipo, 
cppd_campo, cppd_valor,  dppd_fechadefinicion)
select  REPLACE(gen_random_uuid()::TEXT, '-', ''), 'PROP_313', 'O', 
corg_llave, cusr_llave , now()
from organizacion_orgp
	inner join usuario_usrp on (corg_usuariosystem = cusr_llave and cusr_estado = 'A')
where corg_principal is null and corg_estado = 'A' AND cusr_correo IS NOT NULL;


INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo,  cpvd_grupo, bpvd_propiedadboolean)
    SELECT 'PROP_314' , 'E', 'REPORTE PUBLICO ', 'REPORT_PUBLIC', 'REQUISITO', true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_314');

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo,  cpvd_grupo,  bpvd_textoculto)
    SELECT 'PROP_315' , 'E', 'VARIABLES ADICIONALES PARA GENERACION DEL REPORTE', 'REPORT_VARIABLES', 'REQUISITO',  true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_315');

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto)
    SELECT 'PROP_316' , 'T', 'FORMA DE AFECTAR SALDOS DEL PROCESO', 'AFECTA_SALDO', 'REQUISITO',  true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_316');
    
    
INSERT INTO propiedad_ppdp
(cppd_llave, cppd_propiedadvalor, cppd_tipo, 
cppd_campo, cppd_valor,  dppd_fechadefinicion)
select  REPLACE(gen_random_uuid()::TEXT, '-', ''), 'PROP_314', 'E', 
rr.crpb_llave , cusr_llave , now()
from organizacion_orgp
	inner join usuario_usrp on (corg_usuariosystem = cusr_llave and cusr_estado = 'A')
	inner join reportebase_rpbp rr on rr.brpb_publico = true and rr.crpb_estado = 'A'
where corg_principal is null and corg_estado = 'A' AND cusr_correo IS NOT NULL;

INSERT INTO propiedad_ppdp
(cppd_llave, cppd_propiedadvalor, cppd_tipo, 
cppd_campo, cppd_valor,  dppd_fechadefinicion)
select  REPLACE(gen_random_uuid()::TEXT, '-', ''), 'PROP_315', 'E', 
rr.crpb_llave , rr.crpb_variables  , now()
from reportebase_rpbp rr 
where rr.crpb_variables is not null
and rr.crpb_estado = 'A';

INSERT INTO propiedad_ppdp
(cppd_llave, cppd_propiedadvalor, cppd_tipo, 
cppd_campo, cppd_valor,  dppd_fechadefinicion)
select  REPLACE(gen_random_uuid()::TEXT, '-', ''), 'PROP_316', 'T', 
pp.cptr_llave , pp.cptr_afectasaldo  , now()
from procesotransicion_ptrp pp 
where pp.cptr_estado = 'A' and pp.cptr_afectasaldo is not null;

ALTER TABLE public.reportebase_rpbp ALTER COLUMN crpb_descripcion DROP NOT NULL;

ALTER TABLE public.proceso_prcp ALTER COLUMN cprc_objetivo DROP NOT NULL;

ALTER TABLE public.propiedad_ppdp ADD cppd_codigo varchar(200) NULL;

UPDATE public.propiedadvalordefinido_pvdp SET cpvd_estado='I' WHERE cpvd_llave='PROP_89';
-- ROL REPORTE

update propiedad_ppdp 
set cppd_tipo = (select cpvd_origen from propiedadvalordefinido_pvdp where cpvd_llave = cppd_propiedadvalor)
where cppd_llave in (
select pp.cppd_llave  from propiedad_ppdp pp 
	inner join propiedadvalordefinido_pvdp pv on pv.cpvd_llave = pp.cppd_propiedadvalor 
where pp.cppd_tipo != pv.cpvd_origen
);


update propiedad_ppdp 
set cppd_codigo = (select dd.cdpl_codigo from documentoplantilla_dplp dd  where dd.cdpl_llave = cppd_valor )
where cppd_estado = 'A' and cppd_propiedadvalor in (
	select pv.cpvd_llave from propiedadvalordefinido_pvdp pv
	where pv.cpvd_codigo in ('PROCESO_ACCIONES'
		,'PLANTILLA_AUXILIAR'
		,'API_NEW_DOCUMENT'
		,'API_SECONDARY_DOCUMENT'
		,'PLANTILLA_DIFERENCIAS'
		,'REPORT_MODULE_REFERENCE'
		,'COVERAGE_TEMPLATE'
		,'TIPO_PRODUCTO_FORMULARIO_DETALLADO'
		,'PLANTILLA_NUEVO_USUARIO'
		,'PLANTILLA_ACTIVAR'
		,'VINCULO_DATA'
		,'VINCULO_DELETE'
		,'TEMPLATE_VOUCHER'
		,'PLANTILLA_ANULAR'
		,'DETALLE_CATEGORIA')
);

update propiedad_ppdp 
set cppd_codigo = (select dd.cdpc_codigo from documentoplantillacaracteristica_dpcp  dd  where dd.cdpc_llave = cppd_valor )
where cppd_estado = 'A' and cppd_propiedadvalor in (
	select pv.cpvd_llave from propiedadvalordefinido_pvdp pv
	where pv.cpvd_codigo in ('UPDATE_INFORMATIVE_FIELD'
		,'CAMPO_DIFERENCIAS'
		,'DISPONIBILIDAD_CROQUIS'
		,'MODIFICAR_CAMPO'
		,'DEPENDE'
		,'FECHA_MAXIMA_CAMPO'
		,'FECHA_MINIMA_CAMPO'
		,'GENERA_DOCUMENTO_CAMPO'
		,'GENERA_DOCUMENTO_CAMPO_FROM_EXPEDIENTE'
		,'GENERA_DOCUMENTO_DEL_RESULTADO_ITERACION'
		,'GENERA_DOCUMENTO_CAMPO_FROM_GENERADOR'
		,'RELACIONAR_DOCUMENTOS'
		,'RETIRAR_DOCUMENTOS'
		,'PLANTILLA_CARGA_MASIVA_MULTIPLE'
		,'TERCERO'
		,'PERMISO_PLANTILLA_INICIO_RAPIDO'
		,'DESCRIPCION'
		,'CAMPO_EVIDENCIA'
		,'TOTAL'
		,'CONSECUTIVO'
		,'FECHA'
		,'PLANTILLA_FECHA_FINAL'
		,'PLANTILLA_FECHA_INICIO'
		,'PLANTILLA_IMAGEN'
		,'RESPONSABLE'
		,'INFORMATIVE_DATA'
		,'CUENTA_SOBREGIRO'
		,'PRODUCTOS_FUNCION_CAMPO'
		,'PRODUCTOS_TERCERO'
		,'PRODUCTO_CAMPO_VALOR_MINIMO'
		,'PRODUCTO_CAMPO_VALOR_UNITARIO'
		,'PRODUCTO_CAMPO_CANTIDAD'
		,'PRODUCTO_CAMPO_TOTAL'
		,'PERMISO_PLANTILLA_CAMPO_FILTRO'
		,'CELULAR_ROL'
		,'CORREO_ROL')
);

update propiedad_ppdp 
set cppd_codigo = (select dd.crpb_codigo from reportebase_rpbp  dd  where dd.crpb_llave = cppd_valor )
where cppd_estado = 'A' and cppd_propiedadvalor in (
	select pv.cpvd_llave from propiedadvalordefinido_pvdp pv
	where pv.cpvd_codigo in ('REPORTE_ENCABEZADO'
		,'REPORTE_PIE_PAGINA'
		,'REPORTE_EXCEL'
		,'P_SUBREPORT_'
		,'MENSAJE_REPORTE'
		,'REPORTE_ENCABEZADO_EXCEL')
);

update propiedad_ppdp 
set cppd_codigo = (select dd.cwbs_codigo from webservice_wbsp  dd  where dd.cwbs_llave = cppd_valor )
where cppd_estado = 'A' and cppd_propiedadvalor in (
	select pv.cpvd_llave from propiedadvalordefinido_pvdp pv
	where pv.cpvd_codigo in ('MENSAJE'
		,'API_AUTHENTICATION'
		,'API_BASE'
		,'API_TRANSACCION'
		,'API')
);


INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo,  cpvd_grupo, bpvd_propiedadboolean)
    SELECT 'PROP_317' , 'T', 'TRANSICION RAPIDA AL GUARDAR DOCUMENTO', 'TRANSICION_RAPIDA', 'REQUISITO',  true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_317');
    
INSERT INTO propiedad_ppdp
(cppd_llave, cppd_propiedadvalor, cppd_tipo, 
cppd_campo, cppd_valor,  dppd_fechadefinicion)
select  REPLACE(gen_random_uuid()::TEXT, '-', ''), 'PROP_317', 'T', 
pp.cptr_llave , '.'  , now()
from procesotransicion_ptrp pp 
where pp.cptr_estado = 'A' and pp.bptr_rapida = true;	