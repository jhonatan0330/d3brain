COMMENT ON TABLE usuario_usrp IS '2023-11-10';

select * from organizar_ultima_gestion();

update propiedadvalordefinido_pvdp set bpvd_multiple = true  where cpvd_llave  = 'PROP_82';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_textoculto) 
	select
	'PROP_247' , 'W', 'API ESTANDAR CODIFICACION', 'API_ENCODE_STANDAR', 'REQUISITO', true
	where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_247');
	
INSERT INTO propiedad_ppdp (cppd_llave, cppd_propiedadvalor, cppd_tipo, cppd_campo, cppd_valor, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion)
	select replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-','')
	,'PROP_247', 'W',ww.cwbs_llave , 'ISO-8859-1', now(), now()
	, (select ccmb_llave  from cambio_cmbp cc order by dcmb_fecha desc limit 1)
	from webservice_wbsp ww where ww.cwbs_url = 'http://localhost:8080/fe/sign' and cwbs_estado = 'A'