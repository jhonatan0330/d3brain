--Funciones iniciales
CREATE OR REPLACE FUNCTION f_convnl(num numeric)
  RETURNS character varying AS
$BODY$
DECLARE	
	d VARCHAR[];f VARCHAR[];g VARCHAR[];numt VARCHAR;txt VARCHAR;a INTEGER;a1 INTEGER;a2 INTEGER;
	n INTEGER;
	p INTEGER;
	negativo BOOLEAN;
BEGIN
	-- Maximo 999.999.999,99
	IF num > 999999999.99 THEN
		RETURN '---';
	END IF;
	txt = '';
	d = ARRAY[' un',' dos',' tres',' cuatro',' cinco',' seis',' siete',' ocho',' nueve',' diez',' once',' doce',' trece',' catorce',' quince',
		' dieciseis',' diecisiete',' dieciocho',' diecinueve',' veinte',' veintiun',' veintidos', ' veintitres', ' veinticuatro', ' veinticinco',
		' veintiseis',' veintisiete',' veintiocho',' veintinueve'];
	f = ARRAY ['','',' treinta',' cuarenta',' cincuenta',' sesenta',' setenta',' ochenta', ' noventa'];
	g= ARRAY [' ciento',' doscientos',' trescientos',' cuatrocientos',' quinientos',' seiscientos',' setecientos',' ochocientos',' novecientos'];
	numt = LPAD((num::numeric(12,2))::text,12,'0');
	IF strpos(numt,'-') > 0 THEN
	   negativo = TRUE;
	ELSE
	   negativo = FALSE;
	END IF;
	numt = TRANSLATE(numt,'-','0');
	numt = TRANSLATE(numt,'.,','');
	-- Trato 4 grupos: millones, miles, unidades y decimales
	p = 1;
	FOR i IN 1..4 LOOP
		IF i < 4 THEN
			n = substring(numt::text FROM p FOR 3);
		ELSE
			n = substring(numt::text FROM p FOR 2);
		END IF;
		p = p + 3;
		IF i = 4 THEN
			IF txt = '' THEN
				txt = ' cero';
			END IF;
			IF n > 0 THEN
			-- Empieza con los decimales
				txt = txt || ' con';
			END IF;
		END IF;
		-- Centenas 
		IF n > 99 THEN
			a = substring(n::text FROM 1 FOR 1);
			a1 = substring(n::text FROM 2 FOR 2);
			IF a = 1 THEN
				IF a1 = 0 THEN
					txt = txt || ' cien';
				ELSE
					txt = txt || ' ciento';
				END IF;
			ELSE
				txt = txt || g[a];
			END IF;
		ELSE
			a1 = n;
		END IF;
		-- Decenas
		a = a1;
		IF a > 0 THEN
			IF a < 30 THEN
				IF a = 21 AND (i = 3 OR i = 4) THEN
					txt = txt || ' veintiuno';
				ELSIF n = 1 AND i = 2 THEN
					txt = txt; 
				ELSIF a = 1 AND (i = 3 OR i = 4)THEN
					txt = txt || ' uno';
				ELSE
					txt = txt || d[a];
				END IF;
			ELSE
				a1 = substring(a::text FROM 1 FOR 1);
				a2 = substring(a::text FROM 2 FOR 1);
				IF a2 = 1 AND (i = 3 OR i = 4) THEN
						txt = txt || f[a1] || ' y' || ' uno';
				ELSE
					IF a2 <> 0 THEN
						txt = txt || f[a1] || ' y' || d[a2];
					ELSE
						txt = txt || f[a1];
					END IF;
				END IF;
			END IF;
		END IF;
		IF n > 0 THEN
			IF i = 1 THEN
				IF n = 1 THEN
					txt = txt || ' millon';
				ELSE
					txt = txt || ' millones';
				END IF;
			ELSIF i = 2 THEN
				txt = txt || ' mil';
			END IF;		
		END IF;
	END LOOP;
	txt = LTRIM(txt);
	IF negativo = TRUE THEN
	   txt= '-' || txt;
	END IF;
    RETURN txt;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION f_convnl(numeric)
  OWNER TO postgres;

CREATE OR REPLACE VIEW campo_documento AS 
 SELECT pvc.cpvc_llave as cpvc_llave,
    pvc.dpvc_valorfecha as dpvc_valorfecha,
    pvc.mpvc_valornumero as mpvc_valornumero,
    pvc.cpvc_valortext as cpvc_valortext,
    pvc.cpvc_valoropcion as cpvc_valoropcion,
    pvc.cpvc_valorauxiliar as cpvc_valorauxiliar,
    pvc.cpvc_campo as cpvc_campo,
    dpc.cdpc_codigo as cdpf_codigo,
    dpc.cdpc_nombre as cdpf_nombre,
    pvc.cpvc_documento as cdrc_documento
   FROM pedidoventacaracteristica_pvcp pvc,
    documentoplantillacaracteristica_dpcp dpc
  WHERE pvc.cpvc_campo =dpc.cdpc_llave AND pvc.cpvc_estado::text = 'A'::text;

CREATE OR REPLACE FUNCTION movimiento_descripcion(id_documento character varying)
 RETURNS character varying
 LANGUAGE plpgsql
AS $function$
DECLARE plantilla character varying; 
DECLARE plantilla_campo_descripcion character varying;
DECLARE plantilla_campo_descripcion_nivel2 character varying;
DECLARE id_documento_principal character varying;
DECLARE descripcion_anidada character varying;
BEGIN 
    IF id_documento IS NULL THEN 
        RETURN NULL;
    END IF;
    SELECT cpdv_plantilla INTO plantilla FROM pedidoventa_pdvp where cpdv_llave = id_documento;
    SELECT cppd_valor INTO plantilla_campo_descripcion FROM propiedad_ppdp 
    	where cppd_campo = plantilla and cppd_estado = 'A' and cppd_propiedadvalor = (select cpvd_llave from propiedadvalordefinido_pvdp where cpvd_codigo = 'DESCRIPCION' and cpvd_origen = 'L');
    IF plantilla_campo_descripcion IS NOT NULL THEN 
	RETURN (select cpvc_valortext from campo_documento where cdrc_documento = id_documento and cpvc_campo = plantilla_campo_descripcion);
    ELSE
	SELECT cppd_valor INTO plantilla_campo_descripcion_nivel2 FROM propiedad_ppdp 
		where cppd_campo = plantilla and cppd_estado = 'A' and cppd_propiedadvalor = (select cpvd_llave from propiedadvalordefinido_pvdp where cpvd_codigo = 'DESCRIPCION_NIVEL2' and cpvd_origen = 'L');
	IF plantilla_campo_descripcion_nivel2 IS NOT NULL THEN
		SELECT cpvc_valoropcion INTO id_documento_principal FROM campo_documento WHERE cdrc_documento = id_documento and cpvc_campo = plantilla_campo_descripcion_nivel2;
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
	ELSE
		RETURN NULL;
	END IF;
        
    END IF;
END; 
$function$
;
  

CREATE OR REPLACE FUNCTION public.upsert_reporte(
    llave character,
    nombre character,
    codigo character,
    formato character,
    variables character,
    texto character)
  RETURNS void AS
$BODY$ 
DECLARE 
BEGIN 
    UPDATE reportebase_rpbp SET crpb_jaspertext = texto WHERE crpb_codigo = llave; 
    /*IF NOT FOUND THEN 
    INSERT INTO consecutivo_conp(ccon_llave, ccon_nombre, ccon_prefijo, mcon_numeroinicial, mcon_numerofinal, mcon_numeroactual)
    	VALUES (llave, llave, llave || '-', 100, 99999999, 100);
	INSERT INTO documentoplantilla_dplp(cdpl_llave, cdpl_nombre, cdpl_consecutivo, cdpl_imagen, cdpl_codigo, cdpl_tipo, cdpl_proceso)
    	VALUES (llave, nombre,  llave, 'http://colombiansofture.com/imagenes/modulo.png', llave, 'R', 'SIMPLE');
	INSERT INTO reportebase_rpbp (crpb_llave, crpb_nombre, crpb_jaspertext, crpb_plantilla) values (llave, nombre, texto, llave);
	INSERT INTO documentoplantillarol_dprp(cdpr_llave, cdpr_plantilla, cdpr_rol, bdpr_iniciorapido, bdpr_rangofiltro, bdpr_crear)
    	select substring(llave ||crac_llave,1,32), llave, crac_llave, true, true, true from rolacceso_racp where brac_permisoscompletos = true; 
    END IF;*/
END; 
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION public.upsert_reporte(character, character, character, character, character, character)
  OWNER TO postgres;


CREATE OR REPLACE FUNCTION public.dcs_saldo_cero(character varying)
  RETURNS character varying AS
$BODY$
BEGIN

	IF EXISTS (SELECT mpvd_saldo FROM pedidoventadinero_pvdp WHERE cpvd_documento  = $1 AND cpvd_estado = 'A' AND mpvd_saldo !=0) THEN
		RETURN 'N';
	ELSE
		RETURN 'S';
	END IF;

END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION public.dcs_saldo_cero(character varying)
  OWNER TO postgres;
COMMENT ON FUNCTION public.dcs_saldo_cero(character varying) IS '1. PROPOSITO:
Compara el valor final del documento despues de aplicar el valor del documento de la transición.
2. RESPUESTAS:
-N:El saldo del documento es cero
-S:El saldo del documento es diferente cero';
  
CREATE OR REPLACE VIEW vi_valores
AS SELECT pedidoventadinero_pvdp.cpvd_documento AS vi_vlr_documento,
    pedidoventadinero_pvdp.mpvd_valortotal AS vi_vlr_total,
    pedidoventadinero_pvdp.mpvd_saldo AS vi_vlr_saldo,
    pedidoventadinero_pvdp.dpvd_fecha AS vi_vlr_fecha
   FROM pedidoventadinero_pvdp
  WHERE pedidoventadinero_pvdp.cpvd_estado::text = 'A'::text;

  CREATE OR REPLACE FUNCTION migrarcampos(_plantilla character varying, _fecha_maxima timestamp with time zone)
 RETURNS void
 LANGUAGE plpgsql
AS $function$
declare 
	documentos character varying[];
	campos character varying[];
begin
	select array (
		select cpdv_llave from pedidoventa_pdvp 
			where cpdv_plantilla = _plantilla and dpdv_fecha < _fecha_maxima 
			and npdv_historico is null 
			limit 50) 
		into documentos;
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
	INSERT INTO Z_drg_documentorelaciongestor (cdrg_llave, cdrg_documentoprincipal, cdrg_documentomodificador, cdrg_estado, ddrg_fecha, cdrg_estadoinicial, cdrg_estadofinal, cdrg_ubicacion, cdrg_valores, cdrg_usuario, ddrg_cierre, cdrg_nombre)
		SELECT cdrg_llave, cdrg_documentoprincipal, cdrg_documentomodificador, cdrg_estado, ddrg_fecha, cdrg_estadoinicial, cdrg_estadofinal, cdrg_ubicacion, cdrg_valores, cdrg_usuario, ddrg_cierre, cdrg_nombre 
			FROM documentorelaciongestor_drgp where cdrg_documentoprincipal = any(documentos);
	delete from documentorelaciongestor_drgp where cdrg_documentoprincipal = any(documentos);
	delete from pedidoventadinero_pvdp where cpvd_documento = any(documentos);
	delete from documentorelacionexpediente_dexp where cdex_campomaestro = any(campos);
	delete from pedidoventacaracteristica_pvcp where cpvc_llave = any(campos);
	update pedidoventa_pdvp set npdv_historico = 1 where cpdv_llave = any(documentos);
END;
$function$
;