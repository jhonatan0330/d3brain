COMMENT ON TABLE usuario_usrp IS '2026-10-15';

INSERT INTO webservice_wbsp (cwbs_llave, cwbs_nombre, cwbs_estado, cwbs_codigo)
SELECT cmpl_llave, cmpl_nombre, cmpl_estado, 
    'MJS' || LPAD(ROW_NUMBER() OVER (ORDER BY cmpl_llave)::TEXT,3,'0') 
   FROM public.mensajeplantillacorreo_mplp
where cmpl_estado = 'A';

INSERT INTO public.propiedad_ppdp
(cppd_llave, cppd_propiedadvalor, cppd_tipo, cppd_campo, cppd_valor,  dppd_fechadefinicion)
SELECT REPLACE(gen_random_uuid()::TEXT, '-', ''), 'PROP_300', 'W', cmpl_llave, cmpl_texto ,  now()
FROM public.mensajeplantillacorreo_mplp
where cmpl_estado = 'A';

INSERT INTO public.propiedad_ppdp
(cppd_llave, cppd_propiedadvalor, cppd_tipo, cppd_campo, cppd_valor,  dppd_fechadefinicion)
SELECT REPLACE(gen_random_uuid()::TEXT, '-', ''), 'PROP_299', 'W', cmpl_llave, cmpl_titulo ,  now()
FROM public.mensajeplantillacorreo_mplp
where cmpl_estado = 'A';

ALTER TABLE public.mensaje_msjp DROP CONSTRAINT IF EXISTS fk_mensajetemplate;

-- No pase los inactivos
--ALTER TABLE public.mensaje_msjp ADD CONSTRAINT fk_mensajetemplate FOREIGN KEY (cmsj_template) REFERENCES webservice_wbsp(cwbs_llave);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre, cpvd_grupo,  bpvd_textoculto)
    SELECT 'PROP_310' , 'O', 'MAIL_SERVER', 'MAIL SERVER',  'REQUISITO',  true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_310');
    
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre, cpvd_grupo,  bpvd_textoculto)
    SELECT 'PROP_311' , 'O',  'MAIL_SERVER_BACKUP','MAIL SERVER DE RESPALDO', 'REQUISITO',  true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_311');

INSERT INTO public.propiedad_ppdp
(cppd_llave, cppd_propiedadvalor, cppd_tipo, cppd_campo, cppd_valor,  dppd_fechadefinicion)
select  REPLACE(gen_random_uuid()::TEXT, '-', ''), 'PROP_310', 'O', 
(select corg_llave from organizacion_orgp oo where oo.corg_principal is null and oo.corg_estado ='A')
,JSON_BUILD_OBJECT(
    'url',     ss.cser_url,
    'puerto',  ss.cser_puerto::INTEGER,
    'usuario', ss.cser_usuario,
    'clave',   ss.cser_clave,
    'from',    ss.cser_base
)::TEXT , now()
from servidor_serp ss
where ss.cser_tipo = 'E' and ss.cser_estado = 'A';
