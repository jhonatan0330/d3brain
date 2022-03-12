COMMENT ON TABLE usuario_usrp IS '2020-08-04';

update propiedadvalordefinido_pvdp set bpvd_propiedadboolean = true where cpvd_llave = 'PROP_33';

update propiedad_ppdp set cppd_estado  ='I' 
where cppd_propiedadvalor ='PROP_33' and cppd_estado = 'A'
and (select cdpc_plantilla from documentoplantillacaracteristica_dpcp where cdpc_llave = cppd_valor) is null;


INSERT INTO relacioninterna_ritp (crit_llave, crit_propiedad, crit_plantilla, crit_campo)
select 
cppd_llave, cppd_llave, (select cdpc_plantilla from documentoplantillacaracteristica_dpcp where cdpc_llave = cppd_valor), cppd_valor
from propiedad_ppdp
where cppd_propiedadvalor ='PROP_33' and cppd_estado = 'A'
and cppd_valor not in (select crit_campo from relacioninterna_ritp where crit_propiedad = cppd_llave and crit_estado = 'A');

update propiedad_ppdp prop_crud set cppd_valor = '1', cppd_texto = null
where prop_crud.cppd_propiedadvalor ='PROP_33' and prop_crud.cppd_estado = 'A';

INSERT INTO cambio_cmbp(ccmb_llave,  ccmb_nombre,  ccmb_motivo,  dcmb_fecha)
	VALUES('SC_20200804',  'SC_20200804',  'MEjorar campos de herencia',  now());

insert into propiedad_ppdp (cppd_llave , cppd_campo , cppd_propiedadvalor , cppd_cambiocreacion , cppd_tipo 
	,cppd_valor , cppd_texto , dppd_fechadefinicion , dppd_fechaimplementacion )
select 
replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''), prop_fuente.cppd_campo , 'PROP_36', 'SC_20200804', 'C'
	, prop_fuente.cppd_valor, (select cdpl_nombre from documentoplantilla_dplp where cdpl_llave = prop_fuente.cppd_valor ) ,now(), now()
from propiedad_ppdp prop_fuente where prop_fuente.cppd_propiedadvalor = 'PROP_19' and prop_fuente.cppd_estado = 'A' 
and prop_fuente.cppd_campo in (
	select prop_herencia.cppd_campo from propiedad_ppdp prop_herencia where prop_herencia.cppd_propiedadvalor = 'PROP_33' and prop_herencia.cppd_estado = 'A');

delete from propiedad_ppdp where cppd_llave in (
select  prop_fuente.cppd_llave
from propiedad_ppdp prop_fuente where prop_fuente.cppd_propiedadvalor = 'PROP_19' and prop_fuente.cppd_estado = 'A' 
and prop_fuente.cppd_campo in (
	select prop_herencia.cppd_campo from propiedad_ppdp prop_herencia where prop_herencia.cppd_propiedadvalor = 'PROP_33' and prop_herencia.cppd_estado = 'A')
);

ALTER TABLE tarifa_tarp
	ADD COLUMN mtar_totalminimo numeric(18,6) DEFAULT 0 NOT NULL;
