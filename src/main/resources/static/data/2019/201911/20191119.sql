COMMENT ON TABLE usuario_usrp IS '2019-11-19';

update documentoplantillacaracteristica_dpcp set cdpc_codigodepende = replace(cdpc_codigodepende, '-','_') where cdpc_codigodepende is not null;
update documentoplantillacaracteristica_dpcp set cdpc_codigodepende = replace(cdpc_codigodepende, ' ','_') where cdpc_codigodepende is not null;

update documentoplantilla_dplp set cdpl_imagen = 'http://golyat.cloud/imagenes/modulo.png' where cdpl_llave = 'RRHH_ANULAR' and cdpl_imagen is null;

INSERT INTO propiedad_ppdp (cppd_llave, cppd_campo, cppd_valor, cppd_propiedadvalor, dppd_fechadefinicion, cppd_motivo) 
VALUES('RRHH_ANULAR_1', 'RRHH_ANULAR_1', '*', 'PROP_37', now(), 'SIN DEFINIR');
