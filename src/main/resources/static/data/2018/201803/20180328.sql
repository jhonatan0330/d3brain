

COMMENT ON TABLE usuario_usrp IS '2018-03-28';

COMMENT ON TABLE usuariosesion_ussp IS '2018.03.28.00';

ALTER TABLE documentoplantilla_dplp
	ADD CONSTRAINT fk_documentoplantillaconsecutivoescrito FOREIGN KEY (cdpl_consecutivoescrito) REFERENCES public.documentoplantillacaracteristica_dpcp(cdpc_llave);

ALTER TABLE documentoplantilla_dplp
	ADD CONSTRAINT fk_documentoplantilladescripcion FOREIGN KEY (cdpl_descripcion) REFERENCES public.documentoplantillacaracteristica_dpcp(cdpc_llave);

ALTER TABLE documentoplantilla_dplp
	ADD CONSTRAINT fk_documentoplantillafecha FOREIGN KEY (cdpl_fecha) REFERENCES public.documentoplantillacaracteristica_dpcp(cdpc_llave);
