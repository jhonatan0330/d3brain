COMMENT ON TABLE usuario_usrp IS '2021-07-18';

INSERT INTO propiedad_ppdp (
	cppd_llave, cppd_campo, cppd_valor, cppd_texto, 
	cppd_propiedadvalor, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_motivo, cppd_cambiocreacion, 
	cppd_tipo, dppd_fechainicial)
SELECT 
	replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-','')
	,cdpl_llave
	,'15'
	,'00:00:07:00:00'
	,'PROP_185'
	,now()
	,now()
	,'Limpiar cada 15 dias el reporte ' || cdpl_nombre
	,'SC_20210715'
	,'L',  '2021-07-17 04:00:00.000'
from documentoplantilla_dplp
	inner join propiedad_ppdp on (cppd_campo = cdpl_llave and cppd_estado = 'A' and cppd_propiedadvalor = 'PROP_142');