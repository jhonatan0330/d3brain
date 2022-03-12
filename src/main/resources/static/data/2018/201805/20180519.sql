
COMMENT ON TABLE usuario_usrp IS '2018-05-19';

CREATE OR REPLACE FUNCTION public.upsert_reporte(
    llave character,
    nombre character,
    codigo character,
    formato character,
    variables character,
    texto character)
  RETURNS void LANGUAGE plpgsql AS '
DECLARE 
BEGIN 
    UPDATE reportebase_rpbp SET crpb_jaspertext = texto WHERE crpb_codigo = llave; 
END; 
';