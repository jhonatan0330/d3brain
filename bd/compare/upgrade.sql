
DROP FUNCTION decision_1b2ceef4568448eeb2c2826a44d40403(documento character varying, modificador character varying);

DROP FUNCTION decision_423140f343eb4173bce0f47928d2733d(documento character varying, modificador character varying);

DROP FUNCTION decision_7a3b730dbe98404eb60cf53f46e8d5a0(documento character varying, modificador character varying);

DROP FUNCTION decision_7dae229089b141ae851cd0b9c9247974(documento character varying, modificador character varying);

DROP FUNCTION decision_d16ff97affe242c1bb53f3b7cf8d7355(documento character varying, modificador character varying);

DROP FUNCTION decision_d830c61b0a3b4f80a4de7cf11620fcdf(documento character varying, modificador character varying);

DROP FUNCTION enc006(documento character varying);

DROP FUNCTION propiedad_09819ea99a9542f886a57b6193fe9997(documento character varying, modificador character varying, token character varying);

DROP FUNCTION propiedad_12521161434a41baa0fc06b303342567(documento character varying, cant integer, pagina integer, fechaminima timestamp with time zone, fechamaxima timestamp with time zone, filtro character varying, codigo_exacto character varying, token character varying, parametros character varying[]);

DROP FUNCTION propiedad_173fe9fa94c04352a23a6b808c94a84f(documento character varying, cant integer, pagina integer, fechaminima timestamp with time zone, fechamaxima timestamp with time zone, filtro character varying, codigo_exacto character varying, token character varying, parametros character varying[]);

DROP FUNCTION propiedad_2ff68619c00748628fc7970b8a160649(documento character varying, cant integer, pagina integer, fechaminima timestamp with time zone, fechamaxima timestamp with time zone, filtro character varying, codigo_exacto character varying, token character varying, parametros character varying[]);

DROP FUNCTION propiedad_39d4d810a7964334bccc32b61e674c3f(documento character varying);

DROP FUNCTION propiedad_4a41ca63ec67424ebae7957d0fd62187(documento character varying, modificador character varying, token character varying);

DROP FUNCTION propiedad_4d3e2e992626419da0c203b217c228a5(documento character varying, cant integer, pagina integer, fechaminima timestamp with time zone, fechamaxima timestamp with time zone, filtro character varying, codigo_exacto character varying, token character varying, parametros character varying[]);

DROP FUNCTION propiedad_625ac97e723f443eb5544989cf6afac8(documento character varying, modificador character varying, token character varying);

DROP FUNCTION propiedad_63bf6643f37747c093e122d159482cc4(producto character varying, producto_base character varying, parametros character varying[]);

DROP FUNCTION propiedad_696934e185634356b31f66c4f2d7604e(producto character varying, parametros character varying[]);

DROP FUNCTION propiedad_6a972250dd374efb91c00632920153d7(documento character varying, cant integer, pagina integer, fechaminima timestamp with time zone, fechamaxima timestamp with time zone, filtro character varying, codigo_exacto character varying, token character varying, parametros character varying[]);

DROP FUNCTION propiedad_6e798d0cf5604db0804ef01447199ddc(documento character varying, cant integer, pagina integer, fechaminima timestamp with time zone, fechamaxima timestamp with time zone, filtro character varying, codigo_exacto character varying, token character varying, parametros character varying[]);

DROP FUNCTION propiedad_8588e40d6b624d02b637f139cd7b55f9(documento character varying, cant integer, pagina integer, fechaminima timestamp with time zone, fechamaxima timestamp with time zone, filtro character varying, codigo_exacto character varying, token character varying, parametros character varying[]);

DROP FUNCTION propiedad_8b4f7a4e99814165bfbcb0955a72547d(documento character varying);

DROP FUNCTION propiedad_99479de112564f0dbd3d38993d33554b(documento character varying, modificador character varying, token character varying);

DROP FUNCTION propiedad_a05b964b75a443299899cbe498cd2c44(documento character varying, modificador character varying, token character varying);

DROP FUNCTION propiedad_a60721c48ffb444aaaec1abb85449ab1(documento character varying, modificador character varying, token character varying);

DROP FUNCTION propiedad_a87984f086a142eeaf981058f9f632bb(documento character varying, cant integer, pagina integer, fechaminima timestamp with time zone, fechamaxima timestamp with time zone, filtro character varying, codigo_exacto character varying, token character varying, parametros character varying[]);

DROP FUNCTION propiedad_a8a4f83d0cc34caca8e8d608142daa35(documento character varying, cant integer, pagina integer, fechaminima timestamp with time zone, fechamaxima timestamp with time zone, filtro character varying, codigo_exacto character varying, token character varying, parametros character varying[]);

DROP FUNCTION propiedad_b18ca0644af84e038c89374deb1cddf6(documento character varying, cant integer, pagina integer, fechaminima timestamp with time zone, fechamaxima timestamp with time zone, filtro character varying, codigo_exacto character varying, token character varying, parametros character varying[]);

DROP FUNCTION propiedad_c2e35fb6e1a54bea894dcfdd6c113523(producto character varying, producto_base character varying, parametros character varying[]);

DROP FUNCTION propiedad_cabec6aebacc4ee39b5f13dc9ac75208(documento character varying, cant integer, pagina integer, fechaminima timestamp with time zone, fechamaxima timestamp with time zone, filtro character varying, codigo_exacto character varying, token character varying, parametros character varying[]);

DROP FUNCTION propiedad_d9c9f09f69e44bf28945947481b17c6c(documento character varying, cant integer, pagina integer, fechaminima timestamp with time zone, fechamaxima timestamp with time zone, filtro character varying, codigo_exacto character varying, token character varying, parametros character varying[]);

DROP FUNCTION propiedad_e6f5198efa1c47f0aaa739bebedec0c4(documento character varying, cant integer, pagina integer, fechaminima timestamp with time zone, fechamaxima timestamp with time zone, filtro character varying, codigo_exacto character varying, token character varying, parametros character varying[]);

DROP FUNCTION propiedad_f45bd28976c24063b69766b13fb0443f(documento character varying);

DROP FUNCTION propiedad_f5ac3efb8e0b4870ba91e060ff0b8e35(documento character varying, modificador character varying, token character varying);

DROP FUNCTION propiedad_f9b7b822c48f4dc98382aa1d3175be06(documento character varying, modificador character varying, token character varying);

DROP FUNCTION propiedad_fbb00952414b4cd0875da50daa78803d(documento character varying, cant integer, pagina integer, fechaminima timestamp with time zone, fechamaxima timestamp with time zone, filtro character varying, codigo_exacto character varying, token character varying, parametros character varying[]);

CREATE TABLE cargamasiva_cmvp (
	ccmv_llave character varying(32) NOT NULL,
	dcmv_fecha timestamp with time zone NOT NULL,
	ccmv_usuario character varying(32) NOT NULL,
	ccmv_archivo character varying(2000) NOT NULL,
	ccmv_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

CREATE TABLE cargamasivaerror_cmep (
	ccme_llave character varying(32) NOT NULL,
	ccme_carga character varying(32) NOT NULL,
	dcme_fecha timestamp with time zone NOT NULL,
	ccme_error character varying(4000) NOT NULL,
	ccme_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

CREATE TABLE cargamasivaitem_cmip (
	ccmi_llave character varying(32) NOT NULL,
	ccmi_carga character varying(32) NOT NULL,
	ccmi_modelo character varying(4000) NOT NULL,
	ccmi_progreso character varying(1) NOT NULL,
	ccmi_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

CREATE TABLE cargamasivasincronizacion_0p (
	c0_llave character varying(32) NOT NULL,
	c0_item character varying(32) NOT NULL,
	d0_fecha timestamp with time zone NOT NULL,
	c0_documento character varying(32) NOT NULL,
	c0_resultado character varying(4000) NOT NULL,
	c0_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

ALTER TABLE transaccionerror_terp
	ALTER COLUMN cter_usuario SET NOT NULL;

COMMENT ON TABLE usuario_usrp IS '2022-10-19';

COMMENT ON TABLE usuarioautenticacion_uaup IS '2022-11-19';

COMMENT ON TABLE usuariosesion_ussp IS '2022.10.18.00';

CREATE OR REPLACE FUNCTION propiedad_27bceb25b46e4de7bd10aae3454e3457(documento character varying, cant integer, pagina integer, fechaminima timestamp with time zone, fechamaxima timestamp with time zone, filtro character varying, codigo_exacto character varying, token character varying, parametros character varying[]) RETURNS SETOF public.pedidoventa_pdvp
    LANGUAGE plpgsql
    AS $$
begin
return query
select administrador.* from pedidoventa_pdvp administrador
	where 'ADMINISTRADOR' = cpdv_plantilla and cpdv_estado = 'A';
end;
$$;

ALTER TABLE cargamasiva_cmvp
	ADD CONSTRAINT pk_cargamasiva_cmvp PRIMARY KEY (ccmv_llave);

ALTER TABLE cargamasivaerror_cmep
	ADD CONSTRAINT pk_cargamasivaerror_cmep PRIMARY KEY (ccme_llave);

ALTER TABLE cargamasivaitem_cmip
	ADD CONSTRAINT pk_cargamasivaitem_cmip PRIMARY KEY (ccmi_llave);

ALTER TABLE cargamasivasincronizacion_0p
	ADD CONSTRAINT pk_cargamasivasincronizacion_0p PRIMARY KEY (c0_llave);

ALTER TABLE cargamasivaerror_cmep
	ADD CONSTRAINT fk_cargamasivaerrorcarga FOREIGN KEY (ccme_carga) REFERENCES public.cargamasiva_cmvp(ccmv_llave);

ALTER TABLE cargamasivaitem_cmip
	ADD CONSTRAINT fk_cargamasivaitemcarga FOREIGN KEY (ccmi_carga) REFERENCES public.cargamasiva_cmvp(ccmv_llave);
