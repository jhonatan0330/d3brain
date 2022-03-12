COMMENT ON TABLE usuario_usrp IS '2018-10-23';

COMMENT ON TABLE usuariosesion_ussp IS '2018.10.23.00';

update documentoplantilla_dplp set cdpl_codigo = substring(cdpl_codigo || '-' || cdpl_llave, 0, 16)
	where cdpl_codigo in (
	select cdpl_codigo from documentoplantilla_dplp
	group by cdpl_codigo
	having count(*)>1);

ALTER TABLE documentoplantilla_dplp
	ADD CONSTRAINT documentoplantilla_dplp_cdpl_codigo_key UNIQUE (cdpl_codigo);
	
ALTER TABLE procesotransicion_ptrp
	DROP COLUMN cptr_plantillacomplemento;
