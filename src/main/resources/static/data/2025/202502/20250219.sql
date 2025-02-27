COMMENT ON TABLE usuario_usrp IS '2025-02-19';

ALTER TABLE pedidoventa_pdvp ADD cpdv_descripcion varchar(4000) NULL;


CREATE OR REPLACE FUNCTION public.descripcion(id_documento character varying)
 RETURNS character varying
 LANGUAGE plpgsql
AS '
	DECLARE _documento_actual pedidoventa_pdvp; 
	DECLARE plantilla_campo_descripcion character varying;
	DECLARE plantilla_campo_descripcion_nivel2 character varying;
	DECLARE id_documento_principal character varying;
	DECLARE descripcion_anidada character varying;
BEGIN 
    if id_documento IS NULL THEN 
        RETURN NULL;
    END IF;
    SELECT * INTO _documento_actual FROM pedidoventa_pdvp where cpdv_llave = id_documento;
    SELECT cppd_valor INTO plantilla_campo_descripcion FROM propiedad_ppdp where cppd_campo = _documento_actual.cpdv_plantilla and cppd_estado = ''A'' and cppd_propiedadvalor = ''PROP_44'';
    IF plantilla_campo_descripcion IS NOT NULL THEN 
		RETURN (select cpvc_valortext from campo4id ( id_documento , plantilla_campo_descripcion, _documento_actual.npdv_historico) limit 1);
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
END; 
'
;


update pedidoventacaracteristica_pvcp 
set cpvc_valortext = trim(cpvc_valortext)
where cpvc_estado = 'A' and cpvc_valortext like ' %';

update historic.z_pvc_pedidoventacaracteristica
set cpvc_valortext = trim(cpvc_valortext)
where cpvc_estado = 'A' and cpvc_valortext like ' %';

