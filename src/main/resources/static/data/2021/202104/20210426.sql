COMMENT ON TABLE usuario_usrp IS '2021-04-26';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, cpvd_origencategoria, bpvd_propiedadboolean) 
	VALUES('PROP_165' , 'C', 'AUTOLOAD', 'AUTOLOAD', 'REQUISITO', 'J', true);

INSERT INTO cambio_cmbp(ccmb_llave,  ccmb_nombre,  ccmb_motivo,  dcmb_fecha)
	VALUES('SC_20210426',  'SC_20210426',  'Autoload para los campos tipo producto',  now());

--Campos filtro
INSERT INTO propiedad_ppdp (cppd_llave, cppd_campo, cppd_valor, cppd_propiedadvalor, 
	dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_tipo)
select 
	replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''),
	cdpc_llave, '1', 'PROP_165', now(), now(), 'SC_20210426', 'C'
from  documentoplantillacaracteristica_dpcp 
where cdpc_formato = 'J' and cdpc_estado = 'A'
