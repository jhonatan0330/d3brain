

COMMENT ON TABLE usuario_usrp IS '2019-04-12';

update documentoplantillarol_dprp set bdpr_iniciorapido  = false, bdpr_listable  = false where cdpr_plantilla in (select cdpl_llave from documentoplantilla_dplp  where cdpl_tipo  = 'R');