
COMMENT ON TABLE usuario_usrp IS '2020-02-21';

INSERT INTO cambio_cmbp (ccmb_llave,  ccmb_nombre,  ccmb_motivo,  dcmb_fecha)
VALUES('SC_20200221',  'SC_20200221',  'Creacion de guias automaticas y manuales', now());

INSERT INTO propiedad_ppdp (cppd_llave, cppd_motivo, cppd_propiedadvalor, cppd_tipo, 
	cppd_campo, cppd_valor, dppd_fechadefinicion, cppd_cambiocreacion, cppd_codigo) 
select substring('PROP_41' || cppd_llave, 0, 32), cppd_motivo, 'PROP_32', 'C' , cppd_campo, '1', now()
	,'SC_20200221', cppd_codigo
	from propiedad_ppdp where cppd_propiedadvalor = 'PROP_32' and cppd_estado = 'A' and cppd_valor !='OK';

update propiedad_ppdp set cppd_propiedadvalor = 'PROP_41' 
	where cppd_propiedadvalor = 'PROP_32' and cppd_estado = 'A' and cppd_valor !='OK' and cppd_valor !='1';