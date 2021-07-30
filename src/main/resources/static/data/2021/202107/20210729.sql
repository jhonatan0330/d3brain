COMMENT ON TABLE usuario_usrp IS '2021-07-29';

DROP FUNCTION migrarcampos(_plantilla character varying, _fecha_maxima timestamp with time zone);

DROP FUNCTION upsert_reporte(llave character, nombre character, codigo character, formato character, variables character, texto character);

CREATE TABLE z_rej_reporteejecucion (
	crej_llave character varying(32) NOT NULL,
	crej_reporte character varying(32) NOT NULL,
	crej_documento character varying(32),
	drej_fechainicio timestamp with time zone NOT NULL,
	drej_fechafin timestamp with time zone NOT NULL,
	crej_error character varying(4000),
	crej_usuario character varying(32),
	crej_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

CREATE OR REPLACE FUNCTION campo4code(_documento character varying, _code character varying) RETURNS TABLE(cpvc_llave character varying, cpvc_documento character varying, dpvc_valorfecha timestamp with time zone, mpvc_valornumero numeric, cpvc_valortext character varying, cpvc_valoropcion character varying, cpvc_estado character varying, cpvc_campo character varying, cpvc_valorauxiliar character varying, cpvc_transaccionregistro character varying, cpvc_transaccioninactivo character varying)
    LANGUAGE plpgsql
    AS '
declare 
	_documento_actual pedidoventa_pdvp;
begin
	select * into _documento_actual from pedidoventa_pdvp where cpdv_llave = _documento;
	if found then
		return query select
			t.cpvc_llave,
			t.cpvc_documento,
			t.dpvc_valorfecha,
			t.mpvc_valornumero,
			t.cpvc_valortext,
			t.cpvc_valoropcion,
			t.cpvc_estado,
			t.cpvc_campo,
			t.cpvc_valorauxiliar,
			t.cpvc_transaccionregistro,
			t.cpvc_transaccioninactivo
		from campo4code(_documento, _code, _documento_actual.cpdv_plantilla, _documento_actual.npdv_historico) t;
	end if;
	
END;';

CREATE OR REPLACE FUNCTION campo4code(_documento character varying, _code character varying, _plantilla character varying, _historico integer) RETURNS TABLE(cpvc_llave character varying, cpvc_documento character varying, dpvc_valorfecha timestamp with time zone, mpvc_valornumero numeric, cpvc_valortext character varying, cpvc_valoropcion character varying, cpvc_estado character varying, cpvc_campo character varying, cpvc_valorauxiliar character varying, cpvc_transaccionregistro character varying, cpvc_transaccioninactivo character varying)
    LANGUAGE plpgsql
    AS '
declare 
	_campo documentoplantillacaracteristica_dpcp;
begin
	select * into _campo from documentoplantillacaracteristica_dpcp where cdpc_plantilla = _plantilla and cdpc_estado = ''A'' and cdpc_codigo = _code;
	if found then
		return query select
				cpvc_llave,
				cpvc_documento,
				dpvc_valorfecha,
				mpvc_valornumero,
				cpvc_valortext,
				cpvc_valoropcion,
				cpvc_estado,
				cpvc_campo,
				cpvc_valorauxiliar,
				cpvc_transaccionregistro,
				cpvc_transaccioninactivo
			from campo4id(_documento, _campo.cdpc_llave, _historico);
	end if;
END;';

CREATE OR REPLACE FUNCTION campo4documento(_documento character varying, _historico integer) RETURNS TABLE(cpvc_llave character varying, cpvc_documento character varying, dpvc_valorfecha timestamp with time zone, mpvc_valornumero numeric, cpvc_valortext character varying, cpvc_valoropcion character varying, cpvc_estado character varying, cpvc_campo character varying, cpvc_valorauxiliar character varying, cpvc_transaccionregistro character varying, cpvc_transaccioninactivo character varying)
    LANGUAGE plpgsql
    AS '
begin
	if _historico = 0 then
		select npdv_historico into _historico from pedidoventa_pdvp where cpdv_llave = _documento;
	end if;
	if _historico is null then
		return query select
				t.cpvc_llave,
				t.cpvc_documento,
				t.dpvc_valorfecha,
				t.mpvc_valornumero,
				t.cpvc_valortext,
				t.cpvc_valoropcion,
				t.cpvc_estado,
				t.cpvc_campo,
				t.cpvc_valorauxiliar,
				t.cpvc_transaccionregistro,
				t.cpvc_transaccioninactivo
			from pedidoventacaracteristica_pvcp t where t.cpvc_documento = _documento and t.cpvc_estado = ''A'';
	else
		return query select
			z.cpvc_llave,
			z.cpvc_documento,
			z.dpvc_valorfecha,
			z.mpvc_valornumero,
			z.cpvc_valortext,
			z.cpvc_valoropcion,
			z.cpvc_estado,
			z.cpvc_campo,
			z.cpvc_valorauxiliar,
			z.cpvc_transaccionregistro,
			z.cpvc_transaccioninactivo
		from z_pvc_pedidoventacaracteristica z where z.cpvc_documento = _documento and z.cpvc_estado = ''A'';
	end if;
END;';

CREATE OR REPLACE FUNCTION campo4id(_documento character varying, _id_campo character varying, _historico integer) RETURNS TABLE(cpvc_llave character varying, cpvc_documento character varying, dpvc_valorfecha timestamp with time zone, mpvc_valornumero numeric, cpvc_valortext character varying, cpvc_valoropcion character varying, cpvc_estado character varying, cpvc_campo character varying, cpvc_valorauxiliar character varying, cpvc_transaccionregistro character varying, cpvc_transaccioninactivo character varying)
    LANGUAGE plpgsql
    AS '
begin
	if _historico = 0 then
		select npdv_historico into _historico from pedidoventa_pdvp where cpdv_llave = _documento;
	end if;
	if _historico is null then
		return query select
				t.cpvc_llave,
				t.cpvc_documento,
				t.dpvc_valorfecha,
				t.mpvc_valornumero,
				t.cpvc_valortext,
				t.cpvc_valoropcion,
				t.cpvc_estado,
				t.cpvc_campo,
				t.cpvc_valorauxiliar,
				t.cpvc_transaccionregistro,
				t.cpvc_transaccioninactivo
			from pedidoventacaracteristica_pvcp t where t.cpvc_documento = _documento and t.cpvc_campo = _id_campo and t.cpvc_estado = ''A'';
	else
		return query select
			z.cpvc_llave,
			z.cpvc_documento,
			z.dpvc_valorfecha,
			z.mpvc_valornumero,
			z.cpvc_valortext,
			z.cpvc_valoropcion,
			z.cpvc_estado,
			z.cpvc_campo,
			z.cpvc_valorauxiliar,
			z.cpvc_transaccionregistro,
			z.cpvc_transaccioninactivo
		from z_pvc_pedidoventacaracteristica z where z.cpvc_documento = _documento and z.cpvc_campo = _id_campo and z.cpvc_estado = ''A'';
	end if;	
END;';

CREATE OR REPLACE FUNCTION dcs_saldo_cero(character varying) RETURNS character varying
    LANGUAGE plpgsql
    AS '
BEGIN
	IF EXISTS (SELECT mpvd_saldo FROM pedidoventadinero_pvdp WHERE cpvd_documento  = $1 AND cpvd_estado = ''A'' AND mpvd_saldo != 0) THEN
		RETURN ''N'';
	ELSE
		RETURN ''S'';
	END IF;
END;';

CREATE OR REPLACE FUNCTION descripcion(id_documento character varying) RETURNS character varying
    LANGUAGE plpgsql
    AS '
DECLARE 
	_documento_actual pedidoventa_pdvp; 
	plantilla_campo_descripcion character varying;
	plantilla_campo_descripcion_nivel2 character varying;
	id_documento_principal character varying;
	descripcion_anidada character varying;
BEGIN 
    if id_documento IS NULL THEN 
        RETURN NULL;
    END IF;
    SELECT * INTO _documento_actual FROM pedidoventa_pdvp where cpdv_llave = id_documento;
    SELECT cppd_valor INTO plantilla_campo_descripcion FROM propiedad_ppdp where cppd_campo = _documento_actual.cpdv_plantilla and cppd_estado = ''A'' and cppd_propiedadvalor = ''PROP_44'';
    IF plantilla_campo_descripcion IS NOT NULL THEN 
		RETURN (select cpvc_valortext from campo4id ( id_documento , plantilla_campo_descripcion, _documento_actual.npdv_historico));
    ELSE
		SELECT cppd_valor INTO plantilla_campo_descripcion_nivel2 FROM propiedad_ppdp where cppd_campo = _documento_actual.cpdv_plantilla and cppd_estado = ''A'' and cppd_propiedadvalor = ''PROP_45'';
		IF plantilla_campo_descripcion_nivel2 IS NOT NULL THEN
			SELECT cpvc_valoropcion INTO id_documento_principal FROM campo4id (id_documento, plantilla_campo_descripcion_nivel2, _documento_actual.npdv_historico);
			CASE WHEN id_documento_principal IS  NULL THEN 
			    RETURN NULL;
			ELSE
			    SELECT descripcion(id_documento_principal) INTO descripcion_anidada;
			    IF descripcion_anidada IS NULL THEN
					RETURN (select cpdv_nombre from pedidoventa_pdvp pcd where cpdv_llave = id_documento_principal);
			    ELSE
					RETURN ''('' || (select cpdv_nombre from pedidoventa_pdvp pcd where cpdv_llave = id_documento_principal) ||'') ''|| descripcion_anidada;
			    END IF;
			END CASE;
		ELSE
			RETURN NULL;
		END IF;
    END IF;
END;';

CREATE OR REPLACE FUNCTION migrar_campos(_plantilla character varying, _fecha_maxima timestamp with time zone) RETURNS numeric
    LANGUAGE plpgsql
    AS '
declare 
	documentos character varying[];
	campos character varying[];
	v_cnt numeric;
begin
	if
		(select count(*) from procesotransicion_ptrp where cptr_estado = ''A'' and cptr_estadopartida is null and cptr_plantilla = _plantilla) = 0
	then
		select array (
			select cpdv_llave from pedidoventa_pdvp 
				where cpdv_plantilla = _plantilla and dpdv_fecha < _fecha_maxima 
				and npdv_historico is null 
				limit 500) 
			into documentos;
	else
		select array (
			select cpdv_llave from pedidoventa_pdvp 
				where cpdv_plantilla = _plantilla and dpdv_fecha < _fecha_maxima 
				and npdv_historico is null and cpdv_estado != ''A''
				limit 500) 
			into documentos;
	end if;	
	select array (
		select cpvc_llave from pedidoventacaracteristica_pvcp 
			where cpvc_documento = any(documentos)) 
		into campos;
	INSERT INTO z_pvc_pedidoventacaracteristica (cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, dpvc_valorfecha, cpvc_valoropcion, cpvc_valorauxiliar, mpvc_valornumero, cpvc_transaccionregistro, cpvc_transaccioninactivo, cpvc_estado) 
		select cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, dpvc_valorfecha, cpvc_valoropcion, cpvc_valorauxiliar, mpvc_valornumero, cpvc_transaccionregistro, cpvc_transaccioninactivo, cpvc_estado
	 		from pedidoventacaracteristica_pvcp where cpvc_llave = any(campos);
	INSERT INTO z_dex_documentorelacionexpediente (cdex_llave, cdex_campomaestro, cdex_expedientedetalle, cdex_transaccionregistro, cdex_transaccioninactivo, cdex_estado, mdex_valor)
		SELECT cdex_llave, cdex_campomaestro, cdex_expedientedetalle, cdex_transaccionregistro, cdex_transaccioninactivo, cdex_estado, mdex_valor
			FROM documentorelacionexpediente_dexp where cdex_campomaestro = any(campos);
	INSERT INTO z_pvd_pedidoventadinero(cpvd_llave, cpvd_documento, mpvd_valortotal, mpvd_saldo, cpvd_estado, dpvd_fecha)
		SELECT cpvd_llave, cpvd_documento, mpvd_valortotal, mpvd_saldo, cpvd_estado, dpvd_fecha
			FROM pedidoventadinero_pvdp where cpvd_documento = any(documentos);
	INSERT INTO z_drg_documentorelaciongestor (cdrg_llave, cdrg_documentoprincipal, cdrg_documentomodificador, cdrg_estado, ddrg_fecha, cdrg_estadoinicial, cdrg_estadofinal, cdrg_ubicacion, cdrg_valores, cdrg_usuario, ddrg_cierre, cdrg_nombre)
		SELECT cdrg_llave, cdrg_documentoprincipal, cdrg_documentomodificador, cdrg_estado, ddrg_fecha, cdrg_estadoinicial, cdrg_estadofinal, cdrg_ubicacion, cdrg_valores, cdrg_usuario, ddrg_cierre, cdrg_nombre 
			FROM documentorelaciongestor_drgp where cdrg_documentoprincipal = any(documentos);
	INSERT INTO z_rej_reporteejecucion (crej_llave, crej_reporte, crej_documento, drej_fechainicio, drej_fechafin, crej_error, crej_usuario, crej_estado)
		SELECT crej_llave, crej_reporte, crej_documento, drej_fechainicio, drej_fechafin, crej_error, crej_usuario, crej_estado 
			FROM reporteejecucion_rejp where crej_documento = any(documentos);
	delete from reporteejecucion_rejp where crej_documento = any(documentos);
	delete from documentorelaciongestor_drgp where cdrg_documentoprincipal = any(documentos);
	delete from pedidoventadinero_pvdp where cpvd_documento = any(documentos);
	delete from documentorelacionexpediente_dexp where cdex_campomaestro = any(campos);
	delete from pedidoventacaracteristica_pvcp where cpvc_llave = any(campos);
	update pedidoventa_pdvp set npdv_historico = 3 where cpdv_llave = any(documentos);
	GET DIAGNOSTICS v_cnt = ROW_COUNT;
	return v_cnt;
END;';

CREATE OR REPLACE FUNCTION movimiento_descripcion(id_documento character varying) RETURNS character varying
    LANGUAGE plpgsql
    AS '
BEGIN 
    return descripcion(id_documento);
END;';

ALTER TABLE z_rej_reporteejecucion
	ADD CONSTRAINT pk_z_rej_reporteejecucion_rejp PRIMARY KEY (crej_llave);

ALTER TABLE z_rej_reporteejecucion
	ADD CONSTRAINT fk_z_rej_reporteejecuciondocumento FOREIGN KEY (crej_documento) REFERENCES public.pedidoventa_pdvp(cpdv_llave);

ALTER TABLE z_rej_reporteejecucion
	ADD CONSTRAINT fk_z_rej_reporteejecucionreporte FOREIGN KEY (crej_reporte) REFERENCES public.reportebase_rpbp(crpb_llave);
