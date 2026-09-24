COMMENT ON TABLE usuario_usrp IS '2026-09-24';

CREATE TABLE IF NOT EXISTS tenantusuario_tnu (
    ctnu_llave varchar(32) NOT NULL,
    ctnu_usuario varchar(32) NOT NULL,
    ctnu_tenant varchar(32) NOT NULL,
    ctnu_estado varchar(1) DEFAULT 'A'::character varying NOT NULL,

    CONSTRAINT pk_tenantusuario_tnu PRIMARY KEY (ctnu_llave)
);

CREATE INDEX IF NOT EXISTS idx_tnu_usuario ON tenantusuario_tnu(ctnu_usuario, ctnu_estado);
