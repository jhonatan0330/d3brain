COMMENT ON TABLE usuario_usrp IS '2019-07-04';

CREATE OR REPLACE FUNCTION movimiento_descripcion(id_documento character varying)
  RETURNS character varying LANGUAGE plpgsql AS '
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
    SELECT cppd_valor INTO plantilla_campo_descripcion FROM propiedad_ppdp where cppd_campo = plantilla and cppd_estado = ''A'' and cppd_key = ''DESCRIPCION'' and cppd_tipo = ''L'';
    IF plantilla_campo_descripcion IS NOT NULL THEN 
	RETURN (select cpvc_valortext from campo_documento where cdrc_documento = id_documento and cpvc_campo = plantilla_campo_descripcion);
    ELSE
	SELECT cppd_valor INTO plantilla_campo_descripcion_nivel2 FROM propiedad_ppdp where cppd_campo = plantilla and cppd_estado = ''A'' and cppd_key = ''DESCRIPCION_NIVEL2'' and cppd_tipo = ''L'';
	IF plantilla_campo_descripcion_nivel2 IS NOT NULL THEN
		SELECT cpvc_valoropcion INTO id_documento_principal FROM campo_documento WHERE cdrc_documento = id_documento and cpvc_campo = plantilla_campo_descripcion_nivel2;
		CASE WHEN id_documento_principal IS  NULL THEN 
		    RETURN NULL;
		ELSE
		    SELECT movimiento_descripcion(id_documento_principal) INTO descripcion_anidada;
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
END; 
';