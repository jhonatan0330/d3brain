
COMMENT ON TABLE usuario_usrp IS '2018-01-31';

ALTER TABLE documentoplantillacosto_dpcp
	ADD COLUMN bdpc_totalvisiblerender boolean DEFAULT false NOT NULL;

ALTER TABLE documentoplantillapermiso_dppp
	ADD COLUMN bdpp_visiblerender boolean DEFAULT false NOT NULL;

update documentoplantillapermiso_dppp set bdpp_visiblerender =(select bdpc_visiblerender from documentoplantillacaracteristica_dpcp where cdpc_llave = cdpp_caracteristica);

update documentoplantillacaracteristica_dpcp  set bdpc_visiblerender  = false where cdpc_llave in (select cdpl_descripcion from documentoplantilla_dplp);
update documentoplantillacaracteristica_dpcp  set bdpc_visiblerender  = false where cdpc_llave in (select cdpl_consecutivoescrito from documentoplantilla_dplp);