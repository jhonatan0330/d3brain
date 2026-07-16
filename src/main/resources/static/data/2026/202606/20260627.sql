COMMENT ON TABLE usuario_usrp IS '2026-06-27';

CREATE TABLE tenant_ten (
    cten_llave varchar(32) NOT NULL,
    cten_nombre varchar(255)    NOT NULL,
    cten_url varchar(500)    NOT NULL,
    cten_username varchar(100)    NOT NULL,
    cten_password varchar(100)    NOT NULL,
    cten_estado varchar(1) DEFAULT 'A'::character varying NOT NULL,

    CONSTRAINT pk_tenant_tenp PRIMARY KEY (cten_llave)
);