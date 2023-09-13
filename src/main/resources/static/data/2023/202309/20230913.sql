COMMENT ON TABLE usuario_usrp IS '2023-09-13';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_multiple) 
	VALUES('PROP_237' , 'W', 'CODIGOS PARA REEMPLAZAR', 'API_CODE_REPLACE', 'REQUISITO', true);
	
INSERT INTO propiedad_ppdp(cppd_llave, cppd_campo, cppd_valor, cppd_propiedadvalor,
dppd_fechadefinicion, dppd_fechaimplementacion, cppd_motivo, cppd_cambiocreacion, cppd_tipo)
select replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''),
	ww.cwbs_llave,
	'Authorization',
	'PROP_170',
	now(),
	now(),
	'Bearer {{JWT}}',
	pp.cppd_cambiocreacion ,
	'W'
from webservice_wbsp ww 
	inner join propiedad_ppdp pp on (pp.cppd_campo =  ww.cwbs_llave and pp.cppd_estado = 'A')
where ww.cwbs_estado = 'A'
	and pp.cppd_propiedadvalor = 'PROP_195'

update propiedad_ppdp set cppd_texto = 'JWT'
where cppd_propiedadvalor = 'PROP_192' and cppd_texto = 'Authorization=Bearer ';