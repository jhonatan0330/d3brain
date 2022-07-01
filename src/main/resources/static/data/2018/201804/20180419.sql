
COMMENT ON TABLE usuario_usrp IS '2018-04-19';

ALTER TABLE reportebase_rpbp
	ADD COLUMN crpb_codigo character varying(16);

update reportebase_rpbp set crpb_codigo = substring(crpb_llave, 0,16);

ALTER TABLE reportebase_rpbp
	ALTER COLUMN crpb_codigo SET NOT NULL;

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
    UPDATE reportebase_rpbp SET crpb_nombre = nombre, crpb_jaspertext = texto WHERE crpb_codigo = llave; 
    /*IF NOT FOUND THEN 
    INSERT INTO consecutivo_conp(ccon_llave, ccon_nombre, ccon_prefijo, mcon_numeroinicial, mcon_numerofinal, mcon_numeroactual)
    	VALUES (llave, llave, llave || '-', 100, 99999999, 100);
	INSERT INTO documentoplantilla_dplp(cdpl_llave, cdpl_nombre, cdpl_consecutivo, cdpl_imagen, cdpl_codigo, cdpl_tipo)
    	VALUES (llave, nombre,  llave, 'http://golyat.cloud/imagenes/modulo.png', llave, 'R');
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

ALTER TABLE documentoplantilla_dplp
	ALTER COLUMN cdpl_codigo TYPE character varying(16) /* TYPE change - table: documentoplantilla_dplp original: character varying(8) new: character varying(16) */;

update  documentoplantillacaracteristica_dpcp set cdpc_codigodepende = 'CAJA'  where cdpc_llave ='CAJA_CIERRE_CUENTA_T';