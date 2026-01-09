COMMENT ON TABLE usuario_usrp IS '2026-01-06';

ALTER TABLE documentoplantillacaracteristica_dpcp ADD ddpc_fechacreacion timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE documentoplantillacaracteristica_dpcp ADD cdpc_usuariocreacion varchar(32);
ALTER TABLE documentoplantillacaracteristica_dpcp ADD cdpc_usuarioeliminacion varchar(32);
ALTER TABLE documentoplantillacaracteristica_dpcp ADD ddpc_fechaeliminacion timestamptz;