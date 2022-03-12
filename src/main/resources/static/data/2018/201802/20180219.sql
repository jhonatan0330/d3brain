COMMENT ON TABLE usuario_usrp IS '2018-02-19';

ALTER TABLE documentorelaciongestor_drgp
	DROP COLUMN bdrg_visiblerender;

ALTER TABLE expedientetransicion_extp
	DROP COLUMN bext_visiblerender,
	ADD COLUMN cext_nombre character varying(100),
	ADD COLUMN cext_maquinaestados character varying(32),
	ADD COLUMN bext_rapida boolean DEFAULT false NOT NULL;

UPDATE expedientetransicion_extp SET cext_nombre= (select cdpl_nombre from documentoplantilla_dplp where cdpl_llave= cext_plantilla);

UPDATE expedientetransicion_extp SET cext_maquinaestados = (select cexe_plantilla from expedienteestado_exep where cexe_llave =  cext_estadopartida);

ALTER TABLE expedientetransicion_extp
	ALTER COLUMN cext_nombre SET NOT NULL;
ALTER TABLE expedientetransicion_extp
	ALTER COLUMN cext_maquinaestados SET NOT NULL;
	
ALTER TABLE expedientetransicion_extp
	ADD CONSTRAINT fk_expedientetransicionmaquinaestados FOREIGN KEY (cext_maquinaestados) REFERENCES documentoplantilla_dplp(cdpl_llave);

update documentoplantilla_dplp set cdpl_tipo = 'P' where cdpl_maquinaestados is not null;

update documentoplantilla_dplp set cdpl_tipo = 'L' where cdpl_llave in (select cdpi_plantilla from documentoplantillaintegracion_dpip ) ;