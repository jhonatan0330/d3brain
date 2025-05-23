COMMENT ON TABLE usuario_usrp IS '2025-05-22';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo)
    SELECT 'PROP_285' ,  'W', 'CATALOGO CONTABLE TIPO', 'API_ACCOUNT_CATALOG', 'REQUISITO'
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_285');
    
INSERT INTO public.webservice_wbsp
	(cwbs_llave, cwbs_nombre, cwbs_codigo, cwbs_template, cwbs_url, cwbs_estado)
VALUES('d44b00a32a044b728f3c339d5879414c', 'CONT A. PARAMETROS', 'CONT_A', '.', '.', 'A');