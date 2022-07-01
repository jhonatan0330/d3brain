COMMENT ON TABLE usuario_usrp IS '2018-04-26';

COMMENT ON TABLE usuariosesion_ussp IS '2018.04.26.00';  

ALTER TABLE documentoplantilla_dplp DROP CONSTRAINT fk_documentoplantillaconsecutivoescrito;

ALTER TABLE documentoplantilla_dplp DROP CONSTRAINT fk_documentoplantilladescripcion;

ALTER TABLE documentoplantilla_dplp DROP CONSTRAINT fk_documentoplantillafecha;

ALTER TABLE documentorelaciongestor_drgp DROP CONSTRAINT fk_documentorelaciongestorestadofinal;

ALTER TABLE documentorelaciongestor_drgp DROP CONSTRAINT fk_documentorelaciongestorestadoinicial;

ALTER TABLE pedidoventa_pdvp DROP CONSTRAINT fk_pedidoventaestadoexpediente;

CREATE TABLE proceso_prcp (
	cprc_llave character varying(32) NOT NULL,
	cprc_nombre character varying(100) NOT NULL,
	cprc_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

INSERT INTO proceso_prcp (cprc_llave, cprc_nombre) SELECT cdpl_llave, cdpl_nombre FROM documentoplantilla_dplp where cdpl_llave in (select cexe_plantilla from expedienteestado_exep );

CREATE TABLE procesoestado_pesp (
	cpes_llave character varying(32) NOT NULL,
	npes_nivel integer DEFAULT 0 NOT NULL,
	cpes_nombre character varying(100) NOT NULL,
	cpes_proceso character varying(32) NOT NULL,
	cpes_estadodocumento character varying(1) NOT NULL,
	bpes_ocultarfiltrofecha boolean DEFAULT false NOT NULL,
	cpes_color character varying(7),
	bpes_modificable boolean DEFAULT false NOT NULL,
	cpes_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

INSERT INTO procesoestado_pesp (cpes_llave, npes_nivel, cpes_nombre, cpes_proceso, cpes_estadodocumento, bpes_ocultarfiltrofecha, cpes_color, bpes_modificable, cpes_estado)
select cexe_llave, nexe_nivel, cexe_nombre, cexe_plantilla, cexe_estadodocumento, bexe_ocultarfiltrofecha, cexe_color, bexe_modificable, cexe_estado from expedienteestado_exep;



CREATE TABLE procesotransicion_ptrp (
	cptr_llave character varying(32) NOT NULL,
	cptr_nombre character varying(100) NOT NULL,
	cptr_proceso character varying(32) NOT NULL,
	cptr_estadopartida character varying(32),
	cptr_estadollegada character varying(32) NOT NULL,
	cptr_plantilla character varying(32) NOT NULL,
	cptr_campo character varying(32) NOT NULL,
	bptr_documentador boolean DEFAULT false NOT NULL,
	bptr_rapida boolean DEFAULT false NOT NULL,
	cptr_plantillacomplemento character varying(32),
	cptr_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

--INSERT INTO procesotransicion_ptrp(cptr_llave, cptr_nombre, cptr_proceso, cptr_estadopartida, cptr_estadollegada, cptr_plantilla, cptr_campo, bptr_documentador, bptr_rapida, cptr_plantillacomplemento, cptr_estado)
--select cext_llave, cext_nombre, cext_maquinaestados, cext_estadopartida, cext_estadollegada, cext_plantilla, cext_campo, bext_documentador, bext_rapida, cext_plantillacomplemento, cext_estado from expedientetransicion_extp;

--DROP TABLE expedientetransicion_extp;

--DROP TABLE expedienteestado_exep;

CREATE TABLE plantillaparametro_pprp (
	cppr_llave character varying(32) NOT NULL,
	cppr_plantilla character varying(32) NOT NULL,
	mppr_valorminimo numeric(18,6) DEFAULT 0 NOT NULL,
	mppr_valormaximo numeric(18,6) DEFAULT 0 NOT NULL,
	cppr_valorsubtotal character varying(32),
	cppr_valortotal character varying(32),
	cppr_tarifario character varying(32),
	cppr_afectasaldo character varying(1),
	cppr_campoafectasaldo character varying(32),
	cppr_descripcion character varying(32),
	cppr_consecutivoescrito character varying(32),
	cppr_fecha character varying(32),
	bppr_ordenadoxnombre boolean DEFAULT false NOT NULL,
	cppr_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

INSERT INTO plantillaparametro_pprp (cppr_llave, cppr_plantilla, cppr_descripcion, cppr_consecutivoescrito, cppr_fecha, bppr_ordenadoxnombre)
select cdpl_llave, cdpl_llave, cdpl_descripcion, cdpl_consecutivoescrito, cdpl_fecha, bdpl_ordenadoxnombre from documentoplantilla_dplp;

update plantillaparametro_pprp set mppr_valorminimo = (select mdpc_valorminimo from documentoplantillacosto_dpcp where cdpc_plantilla = cppr_plantilla) where cppr_plantilla in (select cdpc_plantilla from documentoplantillacosto_dpcp where cdpc_estado  ='A'); 
update plantillaparametro_pprp set mppr_valormaximo = (select mdpc_valormaximo from documentoplantillacosto_dpcp where cdpc_plantilla = cppr_plantilla) where cppr_plantilla in (select cdpc_plantilla from documentoplantillacosto_dpcp where cdpc_estado  ='A'); 
update plantillaparametro_pprp set cppr_valorsubtotal = (select cdpc_valorsubtotal from documentoplantillacosto_dpcp where cdpc_plantilla = cppr_plantilla) where cppr_plantilla in (select cdpc_plantilla from documentoplantillacosto_dpcp where cdpc_estado  ='A'); 
update plantillaparametro_pprp set cppr_valortotal = (select cdpc_valortotal from documentoplantillacosto_dpcp where cdpc_plantilla = cppr_plantilla) where cppr_plantilla in (select cdpc_plantilla from documentoplantillacosto_dpcp where cdpc_estado  ='A'); 
update plantillaparametro_pprp set cppr_tarifario = (select cdpc_tarifario from documentoplantillacosto_dpcp where cdpc_plantilla = cppr_plantilla) where cppr_plantilla in (select cdpc_plantilla from documentoplantillacosto_dpcp where cdpc_estado  ='A'); 
update plantillaparametro_pprp set cppr_afectasaldo = (select cdpc_afectasaldo from documentoplantillacosto_dpcp where cdpc_plantilla = cppr_plantilla) where cppr_plantilla in (select cdpc_plantilla from documentoplantillacosto_dpcp where cdpc_estado  ='A'); 
update plantillaparametro_pprp set cppr_campoafectasaldo = (select cdpc_campoafectasaldo from documentoplantillacosto_dpcp where cdpc_plantilla = cppr_plantilla) where cppr_plantilla in (select cdpc_plantilla from documentoplantillacosto_dpcp where cdpc_estado  ='A'); 

ALTER TABLE documentoplantillarol_dprp
	DROP COLUMN bdpr_anular,
	ADD COLUMN bdpr_totalvisiblerender boolean DEFAULT false NOT NULL;

update documentoplantillarol_dprp set bdpr_totalvisiblerender = (select bdpc_totalvisiblerender from documentoplantillacosto_dpcp where cdpr_plantilla = cdpc_plantilla) where cdpr_plantilla in (select cdpc_plantilla from documentoplantillacosto_dpcp where cdpc_estado  ='A');

DROP TABLE documentoplantillacosto_dpcp;

ALTER TABLE documentoplantilla_dplp
	DROP COLUMN cdpl_fecha,
	DROP COLUMN cdpl_consecutivoescrito,
	DROP COLUMN cdpl_descripcion,
	DROP COLUMN bdpl_ordenadoxnombre;

ALTER TABLE documentoplantilla_dplp
	RENAME COLUMN cdpl_maquinaestados TO cdpl_proceso;

ALTER TABLE pedidoventadinero_pvdp
	DROP COLUMN dpvd_fechapago;

CREATE OR REPLACE FUNCTION upsert_reporte(llave character, nombre character, codigo character, formato character, variables character, texto character) RETURNS void
    LANGUAGE plpgsql
    AS $$ 
DECLARE 
BEGIN 
    UPDATE reportebase_rpbp SET crpb_nombre = nombre, crpb_jaspertext = texto WHERE crpb_codigo = llave; 
    /*IF NOT FOUND THEN 
    INSERT INTO consecutivo_conp(ccon_llave, ccon_nombre, ccon_prefijo, mcon_numeroinicial, mcon_numerofinal, mcon_numeroactual)
    	VALUES (llave, llave, llave || '-', 100, 99999999, 100);
	INSERT INTO documentoplantilla_dplp(cdpl_llave, cdpl_nombre, cdpl_consecutivo, cdpl_imagen, cdpl_codigo, cdpl_tipo, cdpl_proceso)
    	VALUES (llave, nombre,  llave, 'http://golyat.cloud/imagenes/modulo.png', llave, 'R', 'SIMPLE');
	INSERT INTO reportebase_rpbp (crpb_llave, crpb_nombre, crpb_jaspertext, crpb_plantilla) values (llave, nombre, texto, llave);
	INSERT INTO documentoplantillarol_dprp(cdpr_llave, cdpr_plantilla, cdpr_rol, bdpr_iniciorapido, bdpr_rangofiltro, bdpr_crear)
    	select substring(llave ||crac_llave,1,32), llave, crac_llave, true, true, true from rolacceso_racp where brac_permisoscompletos = true; 
    END IF;*/
END; 
$$;


INSERT INTO proceso_prcp ( cprc_llave, cprc_nombre) VALUES ('SIMPLE', 'SIMPLE');
INSERT INTO procesoestado_pesp ( cpes_llave, cpes_nombre, npes_nivel, cpes_proceso, cpes_estadodocumento, bpes_ocultarfiltrofecha, cpes_color, bpes_modificable) 
	VALUES ('SIMPLE_ACTIVO', 'ACTIVO', 0, 'SIMPLE', 'A', TRUE, NULL, TRUE);
INSERT INTO procesoestado_pesp ( cpes_llave, cpes_nombre, npes_nivel, cpes_proceso, cpes_estadodocumento, bpes_ocultarfiltrofecha, cpes_color, bpes_modificable)
	VALUES ('SIMPLE_INACTIVO', 'INACTIVO', 1, 'SIMPLE', 'I', TRUE, NULL, TRUE);
INSERT INTO consecutivo_conp(ccon_llave, ccon_nombre) VALUES ('ANULACION', 'ANULACION');
INSERT INTO documentoplantilla_dplp(cdpl_llave, cdpl_tipo, cdpl_codigo, cdpl_nombre, cdpl_consecutivo, cdpl_imagen, cdpl_proceso)
    VALUES ('ANULAR', 'F', 'ANULAR', 'ANULAR', 'ANULACION', '', 'SIMPLE');
INSERT INTO documentoplantillacaracteristica_dpcp(cdpc_llave, cdpc_plantilla, cdpc_formato, cdpc_nombre, cdpc_codigo, ndpc_orden, bdpc_obligatorio)
    VALUES ('ANULAR_1', 'ANULAR', 'Z', 'DOCUMENTO', 'DOCUMENTO', 1, TRUE);
INSERT INTO documentoplantillacaracteristica_dpcp(cdpc_llave, cdpc_plantilla, cdpc_formato, cdpc_nombre, cdpc_codigo, ndpc_orden)
    VALUES ('ANULAR_2', 'ANULAR', 'T', 'MOTIVO', 'MOTIVO', 2);
INSERT INTO plantillacampoparametro_pcpp(cpcp_llave, cpcp_campo, cpcp_key, cpcp_valor)VALUES ('ANULAR_2', 'ANULAR_2', 'BASICA', 'HTML');
INSERT INTO procesotransicion_ptrp ( cptr_llave, cptr_nombre, cptr_proceso, cptr_estadopartida, cptr_estadollegada, cptr_plantilla, cptr_campo, bptr_documentador)
	VALUES('SIMPLE_ANULAR', 'ANULAR','SIMPLE', 'SIMPLE_ACTIVO', 'SIMPLE_INACTIVO', 'ANULAR', 'ANULAR_1', TRUE);
	
ALTER TABLE plantillaparametro_pprp
	ADD CONSTRAINT pk_plantillaparametro_pprp PRIMARY KEY (cppr_llave);

ALTER TABLE proceso_prcp
	ADD CONSTRAINT pk_proceso_prcp PRIMARY KEY (cprc_llave);

ALTER TABLE procesoestado_pesp
	ADD CONSTRAINT pk_procesoestado_pesp PRIMARY KEY (cpes_llave);

ALTER TABLE procesotransicion_ptrp
	ADD CONSTRAINT pk_procesotransicion_ptrp PRIMARY KEY (cptr_llave);

ALTER TABLE documentoplantilla_dplp
	ADD CONSTRAINT fk_documentoplantillaproceso FOREIGN KEY (cdpl_proceso) REFERENCES proceso_prcp(cprc_llave);

ALTER TABLE documentorelaciongestor_drgp
	ADD CONSTRAINT fk_documentorelaciongestorestadofinal FOREIGN KEY (cdrg_estadofinal) REFERENCES public.procesoestado_pesp(cpes_llave);

ALTER TABLE documentorelaciongestor_drgp
	ADD CONSTRAINT fk_documentorelaciongestorestadoinicial FOREIGN KEY (cdrg_estadoinicial) REFERENCES public.procesoestado_pesp(cpes_llave);

ALTER TABLE pedidoventa_pdvp
	ADD CONSTRAINT fk_pedidoventaestadoexpediente FOREIGN KEY (cpdv_estadoexpediente) REFERENCES public.procesoestado_pesp(cpes_llave);

ALTER TABLE plantillaparametro_pprp
	ADD CONSTRAINT fk_plantillaparametrocampoafectasaldo FOREIGN KEY (cppr_campoafectasaldo) REFERENCES public.documentoplantillacaracteristica_dpcp(cdpc_llave);

ALTER TABLE plantillaparametro_pprp
	ADD CONSTRAINT fk_plantillaparametroconsecutivoescrito FOREIGN KEY (cppr_consecutivoescrito) REFERENCES public.documentoplantillacaracteristica_dpcp(cdpc_llave);

ALTER TABLE plantillaparametro_pprp
	ADD CONSTRAINT fk_plantillaparametrodescripcion FOREIGN KEY (cppr_descripcion) REFERENCES public.documentoplantillacaracteristica_dpcp(cdpc_llave);

ALTER TABLE plantillaparametro_pprp
	ADD CONSTRAINT fk_plantillaparametrofecha FOREIGN KEY (cppr_fecha) REFERENCES public.documentoplantillacaracteristica_dpcp(cdpc_llave);

ALTER TABLE plantillaparametro_pprp
	ADD CONSTRAINT fk_plantillaparametroplantilla FOREIGN KEY (cppr_plantilla) REFERENCES public.documentoplantilla_dplp(cdpl_llave);

ALTER TABLE plantillaparametro_pprp
	ADD CONSTRAINT fk_plantillaparametrovalorsubtotal FOREIGN KEY (cppr_valorsubtotal) REFERENCES public.documentoplantillacaracteristica_dpcp(cdpc_llave);

ALTER TABLE plantillaparametro_pprp
	ADD CONSTRAINT fk_plantillaparametrovalortotal FOREIGN KEY (cppr_valortotal) REFERENCES public.documentoplantillacaracteristica_dpcp(cdpc_llave);

ALTER TABLE procesoestado_pesp
	ADD CONSTRAINT fk_procesoestadoproceso FOREIGN KEY (cpes_proceso) REFERENCES proceso_prcp(cprc_llave);

--select * from procesoestado_pesp order by cpes_proceso

 
ALTER TABLE procesotransicion_ptrp
	ADD CONSTRAINT fk_procesotransicioncampo FOREIGN KEY (cptr_campo) REFERENCES public.documentoplantillacaracteristica_dpcp(cdpc_llave);

ALTER TABLE procesotransicion_ptrp
	ADD CONSTRAINT fk_procesotransicionestadollegada FOREIGN KEY (cptr_estadollegada) REFERENCES public.procesoestado_pesp(cpes_llave);

ALTER TABLE procesotransicion_ptrp
	ADD CONSTRAINT fk_procesotransicionestadopartida FOREIGN KEY (cptr_estadopartida) REFERENCES public.procesoestado_pesp(cpes_llave);

ALTER TABLE procesotransicion_ptrp
	ADD CONSTRAINT fk_procesotransicionplantilla FOREIGN KEY (cptr_plantilla) REFERENCES public.documentoplantilla_dplp(cdpl_llave);

ALTER TABLE procesotransicion_ptrp
	ADD CONSTRAINT fk_procesotransicionproceso FOREIGN KEY (cptr_proceso) REFERENCES public.proceso_prcp(cprc_llave);


--select * from documentoplantilla_dplp  where cdpl_llave = 'b4d3cfd9d8874275bebe9f8d87847fed'
--select * from expedientetransicion_extp  order by cext_maquinaestados 


CREATE OR REPLACE FUNCTION public.movimiento_descripcion(id_documento character varying)
  RETURNS character varying AS
$BODY$ 
DECLARE plantilla_campo_descripcion character varying;
DECLARE id_documento_principal character varying;
DECLARE descripcion_anidada character varying;
BEGIN 
    IF id_documento IS NULL THEN 
        RETURN NULL;
    END IF;
    SELECT cppr_descripcion INTO plantilla_campo_descripcion FROM plantillaparametro_pprp, pedidoventa_pdvp where cpdv_plantilla = cppr_plantilla and cpdv_llave = id_documento;
    CASE WHEN plantilla_campo_descripcion IS NOT NULL THEN 
	RETURN (select cpvc_valortext from campo_documento where cdrc_documento = id_documento and cpvc_campo = plantilla_campo_descripcion);
    ELSE
        SELECT cdrg_documentoprincipal INTO id_documento_principal FROM documentorelaciongestor_drgp WHERE cdrg_documentomodificador = id_documento;
	CASE WHEN id_documento_principal IS  NULL THEN 
	    RETURN NULL;
        ELSE
            SELECT movimiento_descripcion(id_documento_principal) INTO descripcion_anidada;
            IF descripcion_anidada IS NULL THEN
		RETURN (select cpdv_nombre from pedidoventa_pdvp pcd where cpdv_llave = id_documento_principal);
            ELSE
		RETURN '(' || (select cpdv_nombre from pedidoventa_pdvp pcd where cpdv_llave = id_documento_principal) ||') '|| descripcion_anidada;
	    END IF;
        END CASE;
    END CASE;
END; 
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION public.movimiento_descripcion(character varying)
  OWNER TO postgres;

ALTER TABLE plantillaparametro_pprp
	DROP COLUMN mppr_valorminimo,
	DROP COLUMN mppr_valormaximo;

INSERT INTO plantillacampoparametro_pcpp (cpcp_llave, cpcp_campo, cpcp_key, cpcp_valor)
SELECT substring( 'PGE' || cdpc_llave, 0,32), cdpc_llave, 'PROCESO_GESTIONAR_ESTADOS', 'TRUE' FROM documentoplantillacaracteristica_dpcp  where bdpc_editable = false and bdpc_modificable = false and cdpc_formato = 'Z';


ALTER TABLE procesotransicion_ptrp
	ADD COLUMN cptr_afectasaldo character varying(1);

update procesotransicion_ptrp set cptr_afectasaldo = (select cppr_afectasaldo from plantillaparametro_pprp  where cppr_plantilla  = cptr_plantilla);

ALTER TABLE plantillaparametro_pprp
	DROP CONSTRAINT fk_plantillaparametrocampoafectasaldo;

ALTER TABLE plantillaparametro_pprp
	DROP COLUMN cppr_afectasaldo,
	DROP COLUMN cppr_campoafectasaldo;
