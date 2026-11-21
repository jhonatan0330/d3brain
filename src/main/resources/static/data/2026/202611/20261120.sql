COMMENT ON TABLE usuario_usrp IS '2026-11-20';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo,  cpvd_grupo, bpvd_propiedadboolean)
    SELECT 'PROP_319' , 'T', 'TRANSICION OCULTA', 'TRANSICION_OCULTA', 'REQUISITO',  true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_319');
    
INSERT INTO propiedad_ppdp
(cppd_llave, cppd_propiedadvalor, cppd_tipo, 
cppd_campo, cppd_valor,  dppd_fechadefinicion)
select  REPLACE(gen_random_uuid()::TEXT, '-', ''), 'PROP_319', 'T', 
pp.cptr_llave, '.', now()
from procesotransicion_ptrp pp where pp.cptr_estado = 'A' and pp.bptr_documentador = false;