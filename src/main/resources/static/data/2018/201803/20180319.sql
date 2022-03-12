

COMMENT ON TABLE usuario_usrp IS '2018-03-19';

COMMENT ON TABLE usuariosesion_ussp IS '2018.03.19.10';

ALTER TABLE documentorelaciongestor_drgp
	DROP CONSTRAINT fk_documentorelaciongestorrolfinal;

ALTER TABLE documentorelaciongestor_drgp
	DROP CONSTRAINT fk_documentorelaciongestorubicacionfinal;

ALTER TABLE documentorelaciongestor_drgp
	DROP CONSTRAINT fk_documentorelaciongestorubicacioninicial;

ALTER TABLE pedidoventa_pdvp
	DROP CONSTRAINT fk_pedidoventaubicacionexpediente;

ALTER TABLE documentoplantillarol_dprp
	ADD COLUMN ndpr_orden integer DEFAULT 0 NOT NULL;

ALTER TABLE documentorelaciongestor_drgp
	DROP COLUMN cdrg_ubicacioninicial,
	DROP COLUMN cdrg_rolfinal,
	DROP COLUMN cdrg_ubicacionfinal;

ALTER TABLE pedidoventa_pdvp
	DROP COLUMN cpdv_ubicacionexpediente,
	DROP COLUMN cpdv_origenexpediente;

ALTER TABLE plantillacampoparametro_pcpp
	ADD COLUMN cpcp_texto character varying(100);

update plantillacampoparametro_pcpp set cpcp_texto = (select cdpl_nombre from documentoplantilla_dplp where cdpl_llave = cpcp_valor) where cpcp_texto is null;
update plantillacampoparametro_pcpp set cpcp_texto = (select cdpc_nombre from documentoplantillacaracteristica_dpcp where cdpc_llave = cpcp_valor) where cpcp_texto is null;
update plantillacampoparametro_pcpp set cpcp_texto = (select ccat_nombre from catalogo_catp where ccat_llave = cpcp_valor) where cpcp_texto is null;

ALTER TABLE rolacceso_racp
	ADD COLUMN crac_plantilla character varying(32);

update rolacceso_racp set crac_plantilla =  (select cdpi_plantilla from documentoplantillaintegracion_dpip  where cdpi_rol = crac_llave);

ALTER TABLE rolacceso_racp
	ALTER COLUMN crac_plantilla SET NOT NULL;

DROP TABLE documentoplantillaintegracion_dpip;

ALTER TABLE pedidoventa_pdvp
	ADD CONSTRAINT fk_pedidoventafuncionario FOREIGN KEY (cpdv_funcionario) REFERENCES usuario_usrp(cusr_llave);

ALTER TABLE rolacceso_racp
	ADD CONSTRAINT fk_rolaccesoplantilla FOREIGN KEY (crac_plantilla) REFERENCES documentoplantilla_dplp(cdpl_llave);
