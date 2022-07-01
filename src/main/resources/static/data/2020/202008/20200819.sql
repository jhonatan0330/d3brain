COMMENT ON TABLE usuario_usrp IS '2020-08-19';
COMMENT ON TABLE usuariosesion_ussp IS '2020.08.19.00';


ALTER TABLE gpsdispositivo_gpsp 
	ALTER COLUMN dgps_ultimaconexion DROP NOT NULL;

CREATE OR REPLACE FUNCTION public.movimiento_descripcion(id_documento character varying)
 RETURNS character varying
 LANGUAGE plpgsql AS '
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
    	where cppd_campo = plantilla and cppd_estado = ''A'' and cppd_propiedadvalor = (select cpvd_llave from propiedadvalordefinido_pvdp where cpvd_codigo = ''DESCRIPCION'' and cpvd_origen = ''L'');
    IF plantilla_campo_descripcion IS NOT NULL THEN 
	RETURN (select cpvc_valortext from campo_documento where cdrc_documento = id_documento and cpvc_campo = plantilla_campo_descripcion);
    ELSE
	SELECT cppd_valor INTO plantilla_campo_descripcion_nivel2 FROM propiedad_ppdp 
		where cppd_campo = plantilla and cppd_estado = ''A'' and cppd_propiedadvalor = (select cpvd_llave from propiedadvalordefinido_pvdp where cpvd_codigo = ''DESCRIPCION_NIVEL2'' and cpvd_origen = ''L'');
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

INSERT INTO usuario_usrp(cusr_llave, cusr_identificacion, cusr_nombre, cusr_imagen) VALUES ('PROCESS', 'PROCESS', 'PROCESS', 'http://golyat.cloud/imagenes/avatar.png');
update organizacion_orgp set corg_usuariosystem = 'PROCESS';