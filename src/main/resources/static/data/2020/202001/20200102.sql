COMMENT ON TABLE usuario_usrp IS '2020-01-02';
COMMENT ON TABLE usuariosesion_ussp IS '2020.01.02.00';

DROP TABLE documentopermisodocumento_dpdp;

update propiedadvalordefinido_pvdp set cpvd_origencategoria = 'Z', cpvd_codigo = 'CUENTA_MOVIMIENTO', cpvd_nombre = 'TIPO DE MOVIMIENTO', bpvd_textoculto =true where cpvd_llave = 'PROP_09';
update propiedadvalordefinido_pvdp set cpvd_origencategoria = 'Z', cpvd_nombre = 'ABRIR TURNO CAJA', bpvd_propiedadboolean =true where cpvd_llave = 'PROP_11';
update propiedadvalordefinido_pvdp set cpvd_origencategoria = 'Z', cpvd_nombre = 'CERRAR TURNO CAJA', bpvd_propiedadboolean =true where cpvd_llave = 'PROP_12';

delete from propiedad_ppdp where cppd_propiedadvalor = 'PROP_10';
delete from propiedadvalordefinido_pvdp where cpvd_llave = 'PROP_10';

update propiedad_ppdp set cppd_valor = (select ccat_tipo from catalogo_catp where ccat_llave = cppd_valor) where cppd_propiedadvalor = 'PROP_09' and (select ccat_tipo from catalogo_catp where ccat_llave = cppd_valor) is not null;
update propiedad_ppdp set cppd_valor = (select ccat_tipo from catalogo_catp where ccat_llave = cppd_valor) where cppd_propiedadvalor = 'PROP_12' and (select ccat_tipo from catalogo_catp where ccat_llave = cppd_valor) is not null;

DROP TABLE cupoviaje_cvjp;
DROP TABLE viajeescala_esvp;
DROP TABLE deduccionaplicadadetalle_dadp;
DROP TABLE deduccionaplicadagrupo_dpgp;
DROP TABLE deduccionprogramadaregistro_dprp;
DROP TABLE deduccionprogramadagrupo_dpgp;
DROP TABLE idiomareemplazo_irep;
DROP TABLE rodamientoasignacion_rasp;
DROP TABLE rodamientoasignacionnovedad_ranp;
DROP TABLE rutaescala_rtep;
DROP TABLE vehiculoreserva_vrsp;
DROP TABLE viaje_viap;

update pedidoventacaracteristica_pvcp set cpvc_valoropcion = (select ccue_documento from cuenta_cuep where ccue_llave =(select ccpu_cuenta from cuentapermisousuario_cpup where ccpu_llave = cpvc_valoropcion))
where cpvc_campo in (select cdpc_llave from documentoplantillacaracteristica_dpcp where cdpc_formato = 'C');

update documentoplantillacaracteristica_dpcp set cdpc_formato = 'Z' where cdpc_formato = 'C';

ALTER TABLE puesto_puep	DROP CONSTRAINT fk_puestocampo;

CREATE TABLE organizacion_orgp (
	corg_llave character varying(32) NOT NULL,
	corg_nombre character varying(100) NOT NULL,
	corg_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

CREATE TABLE pedidoventatiempo_pvtp (
	cpvt_llave character varying(32) NOT NULL,
	cpvt_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

ALTER TABLE organizacion_orgp
	ADD CONSTRAINT pk_organizacion_orgp PRIMARY KEY (corg_llave);

ALTER TABLE pedidoventatiempo_pvtp
	ADD CONSTRAINT pk_pedidoventatiempo_pvtp PRIMARY KEY (cpvt_llave);

update propiedadvalordefinido_pvdp set bpvd_textoculto = true where cpvd_llave = 'PROP_28';
update propiedadvalordefinido_pvdp set bpvd_textoculto = true where cpvd_llave = 'PROP_53';

update propiedadvalordefinido_pvdp set cpvd_motivo = 'Permite vincular el link de acceso al curso' where cpvd_llave = 'PROP_53';
update propiedad_ppdp set cppd_motivo = 'Permite vincular el link de acceso al curso' where cppd_propiedadvalor = 'PROP_53';

