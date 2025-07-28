 COMMENT ON TABLE usuario_usrp IS '2025-07-24';

ALTER TABLE public.documentorelacionexpediente_dexp ADD cdex_documentoregistro varchar(32);
ALTER TABLE public.documentorelacionexpediente_dexp ADD cdex_documentoinactivo varchar(32);

ALTER TABLE public.documentorelacionexpediente_dexp ALTER COLUMN cdex_transaccionregistro DROP NOT NULL;

CREATE TABLE pedidoventaubicacion_pvup (
	cpvu_llave varchar(32) NOT NULL,
	cpvu_documento varchar(32) NOT NULL,
	dpvu_fecha timestamptz NOT NULL,
	cpvu_ubicacion varchar(32) NOT NULL,
	cpvu_modificador varchar(32) NOT NULL,
	cpvu_estado varchar(1) DEFAULT 'A'::character varying NOT NULL,
	CONSTRAINT pk_pedidoventaubicacion_pvup PRIMARY KEY (cpvu_llave)
);

ALTER TABLE pedidoventadinero_pvdp ADD cpvd_modificador varchar(32);

--1.----------
update propiedadvalordefinido_pvdp pp
set bpvd_propiedadboolean = true, cpvd_origen = 'A', cpvd_origencategoria = 'E'
where pp.cpvd_llave = 'PROP_133';

--2. Inactivar transiciones inactivas
update propiedad_ppdp
set cppd_estado = 'I'
where cppd_llave in (
select pp.cppd_llave from propiedad_ppdp pp 
	inner join procesotransicion_ptrp pt on (pt.cptr_llave = pp.cppd_campo and pt.cptr_estado = 'I')
where pp.cppd_propiedadvalor = 'PROP_133'
	 and pp.cppd_estado = 'A'
);

--3. Actualizo el campo
update propiedad_ppdp pp 
set cppd_campo =  (select pt.cptr_estadollegada from procesotransicion_ptrp pt where pt.cptr_llave = pp.cppd_campo)
,cppd_tipo = 'E'
where pp.cppd_propiedadvalor = 'PROP_133';
	 
-- 4. Coloco relaciones
INSERT INTO relacioninterna_ritp
(crit_llave, crit_propiedad, crit_plantilla, crit_campo, drit_fechainicio, crit_cambiocreacion)
select 
	replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''),
	pp.cppd_llave
	,(select cdpc_plantilla from documentoplantillacaracteristica_dpcp where cdpc_llave = pp.cppd_valor)
	,pp.cppd_valor 
	,now()
	,pp.cppd_cambiocreacion 
from propiedad_ppdp pp 
	inner join procesotransicion_ptrp pt on (pt.cptr_llave = pp.cppd_campo and pt.cptr_estado = 'A')
	inner join procesoestado_pesp on (cpes_llave = pt.cptr_estadollegada and cpes_estado = 'A' and cpes_tipo = 'E')
where pp.cppd_propiedadvalor = 'PROP_133'
	 and pp.cppd_estado = 'A';
