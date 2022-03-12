
COMMENT ON TABLE usuario_usrp IS '2018-02-10';

COMMENT ON TABLE usuariosesion_ussp IS '2018.02.10.01';

CREATE OR REPLACE FUNCTION movimiento_descripcion(id_documento character varying) RETURNS character varying
    LANGUAGE plpgsql
    AS $$ 
DECLARE plantilla_campo_descripcion character varying;
DECLARE id_documento_principal character varying;
DECLARE descripcion_anidada character varying;
BEGIN 
    IF id_documento IS NULL THEN 
        RETURN NULL;
    END IF;
    SELECT cdpl_descripcion INTO plantilla_campo_descripcion FROM documentoplantilla_dplp, pedidoventa_pdvp where cpdv_plantilla = cdpl_llave and cpdv_llave = id_documento;
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
$$;
