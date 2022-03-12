COMMENT ON TABLE usuario_usrp IS '2019-11-12';
COMMENT ON TABLE usuariosesion_ussp IS '2019.11.12.00';

ALTER TABLE propiedad_ppdp
	ADD COLUMN cppd_motivo character varying(4000);

update propiedad_ppdp set cppd_motivo = 'SIN DEFINIR';

ALTER TABLE propiedad_ppdp
	ALTER COLUMN cppd_motivo SET NOT NULL;

ALTER TABLE propiedadvalordefinido_pvdp
	DROP COLUMN bpvd_incluirorigen,
	ADD COLUMN cpvd_grupo character varying(100),
	ADD COLUMN bpvd_incluirpreloadorigen boolean DEFAULT false NOT NULL,
	ADD COLUMN cpvd_motivo character varying(4000),
	ADD COLUMN bpvd_textoculto boolean DEFAULT false NOT NULL,
	ADD COLUMN bpvd_necesitadesarrollo boolean DEFAULT false NOT NULL;

update propiedadvalordefinido_pvdp set cpvd_grupo = 'REQUISITO';

ALTER TABLE propiedadvalordefinido_pvdp
	ALTER COLUMN cpvd_grupo set not NULL;

ALTER TABLE relacioninterna_ritp
	ADD CONSTRAINT fk_relacioninternacampo FOREIGN KEY (crit_campo) REFERENCES documentoplantillacaracteristica_dpcp(cdpc_llave);

ALTER TABLE relacioninterna_ritp
	ADD CONSTRAINT fk_relacioninternaplantilla FOREIGN KEY (crit_plantilla) REFERENCES documentoplantilla_dplp(cdpl_llave);
	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda, cpvd_grupo) 
	VALUES('PROP_87' , 'L', 'BENEFICIO', 'BENEFICIO', 'T', 'www.softwareparati.com', 'BENEFICIO');	
