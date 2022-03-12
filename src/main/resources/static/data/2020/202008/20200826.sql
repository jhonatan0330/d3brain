COMMENT ON TABLE usuario_usrp IS '2020-08-26';

update propiedadvalordefinido_pvdp set cpvd_codigo = 'NUMERO_FUNCION_SQL', bpvd_solicitamotivo =true, bpvd_textoculto = true 
	where cpvd_llave = 'PROP_29';

ALTER TABLE documentorelaciongestor_drgp ADD cdrg_nombre varchar(100) ;

update documentorelaciongestor_drgp 
set cdrg_nombre = (select cdpl_nombre from documentoplantilla_dplp where cdpl_llave = (select cpdv_plantilla from pedidoventa_pdvp where cpdv_llave = cdrg_documentomodificador));

update documentorelaciongestor_drgp 
set cdrg_nombre = (select cdpl_nombre from documentoplantilla_dplp where cdpl_llave = (select cpdv_plantilla from pedidoventa_pdvp where cpdv_llave = cdrg_documentoprincipal))
where cdrg_nombre is null;

ALTER TABLE documentorelaciongestor_drgp ALTER cdrg_nombre SET NOT NULL ;