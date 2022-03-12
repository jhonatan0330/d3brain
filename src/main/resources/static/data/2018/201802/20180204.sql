
/*
Coloco los reportes no listables para que se vean atraves del boton de reportes
*/
update documentoplantillarol_dprp set bdpr_listable = false, bdpr_iniciorapido = true  where cdpr_plantilla  in ( select cdpl_llave from documentoplantilla_dplp  where cdpl_tipo  = 'R');