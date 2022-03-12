COMMENT ON TABLE usuario_usrp IS '2019-10-28';
COMMENT ON TABLE usuariosesion_ussp IS '2019.10.28.00';

ALTER TABLE mensajeplantillacorreo_mplp
	ADD COLUMN cmpl_titulo character varying(100);

update mensajeplantillacorreo_mplp set cmpl_titulo = '{D_CODE}';

ALTER TABLE mensajeplantillacorreo_mplp
	ALTER COLUMN cmpl_titulo SET NOT NULL;

delete from mensaje_msjp;

ALTER TABLE mensaje_msjp
	DROP COLUMN cmsj_mensaje,
	ADD COLUMN cmsj_parametros character varying(4000) NOT NULL;
	
delete from productoinventariodescuento_pidp where cpid_producto = cpid_productodescontar;

update documentoplantilla_dplp set cdpl_codigo = replace(cdpl_codigo, ' ', '_');
update documentoplantilla_dplp set cdpl_codigo = replace(cdpl_codigo, '-', '_');
update documentoplantillacaracteristica_dpcp set cdpc_codigo = replace(cdpc_codigo, '-', '_');
update documentoplantillacaracteristica_dpcp set cdpc_codigo = replace(cdpc_codigo, ' ', '_');