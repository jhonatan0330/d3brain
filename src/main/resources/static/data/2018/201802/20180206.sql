

COMMENT ON TABLE usuario_usrp IS '2018-02-06';

CREATE OR REPLACE FUNCTION upsert_reporte(llave character, nombre character, codigo character, formato character, variables character, texto character) RETURNS void
    LANGUAGE plpgsql
    AS $$ 
DECLARE 
BEGIN 
    UPDATE reportebase_rpbp SET crpb_nombre = nombre, crpb_jaspertext = texto WHERE crpb_llave = llave; 
    /*IF NOT FOUND THEN 
    INSERT INTO consecutivo_conp(ccon_llave, ccon_nombre, ccon_prefijo, mcon_numeroinicial, mcon_numerofinal, mcon_numeroactual)
    	VALUES (llave, llave, llave || '-', 100, 99999999, 100);
	INSERT INTO documentoplantilla_dplp(cdpl_llave, cdpl_nombre, cdpl_consecutivo, cdpl_imagen, cdpl_codigo, cdpl_tipo, cdpl_estado)
    	VALUES (llave, nombre,  llave, 'http://colombiansofture.com/imagenes/modulo.png', llave, 'R', 'I');
	INSERT INTO reportebase_rpbp (crpb_llave, crpb_nombre, crpb_codigo, crpb_jaspertext, crpb_estado) values (llave, nombre, codigo, texto, 'I');
	INSERT INTO documentoplantillarol_dprp(cdpr_llave, cdpr_plantilla, cdpr_rol, bdpr_listable, bdpr_rangofiltro, bdpr_vertodos)
    	select substring(llave ||crac_llave,1,32), llave, crac_llave, true, true, true from rolacceso_racp where brac_permisoscompletos = true; 
    END IF;*/ 
END; 
$$;
ALTER TABLE reportebase_rpbp
	DROP COLUMN crpb_codigo;
