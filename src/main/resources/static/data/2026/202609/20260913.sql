COMMENT ON TABLE usuario_usrp IS '2026-09-13';

CREATE TABLE consumounidad_cup(
        ccup_llave character varying(32) NOT NULL,
        ncup_saldo NUMERIC(18,6) NOT NULL DEFAULT 0,
        dcup_fechaactualizacion timestamp with time zone,
        ccup_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_consumounidad_cup PRIMARY KEY (ccup_llave)
    );

CREATE TABLE movimientoconsumo_mcop(
        cmco_llave character varying(32) NOT NULL,
        cmco_tipo character varying(1) NOT NULL,
        dmco_fecharegistro timestamp with time zone NOT NULL,
        dmco_fechaevento timestamp with time zone NOT NULL,
        mmco_cantidad NUMERIC(18,6) NOT NULL DEFAULT 0,
        mmco_saldoinicial NUMERIC(18,6) NOT NULL DEFAULT 0,
        mmco_saldofinal NUMERIC(18,6) NOT NULL DEFAULT 0,
        cmco_anterior character varying(32),
        cmco_siguiente character varying(32),
        cmco_referencia character varying(32),
        cmco_estado character varying(1) NOT NULL DEFAULT 'A',
        CONSTRAINT PK_movimientoconsumo_mcop PRIMARY KEY (cmco_llave)
    );

