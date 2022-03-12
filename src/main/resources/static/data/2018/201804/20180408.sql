COMMENT ON TABLE usuario_usrp IS '2018-04-08';
COMMENT ON TABLE usuariosesion_ussp IS '2018.04.08.00';
/**
* Todos los roles no deben tener tipo rango
*/
update documentoplantillarol_dprp  SET  bdpr_rangofiltro = false where cdpr_plantilla  in (select cdpl_llave from documentoplantilla_dplp  where cdpl_tipo  = 'L') and bdpr_rangofiltro = true;
/**
* Las sedes se deben poder filtrar por el nombre
*/
update documentoplantillacaracteristica_dpcp set bdpc_filtro = true  where cdpc_llave in (select cdpl_descripcion from documentoplantilla_dplp) and bdpc_filtro = false;