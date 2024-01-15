COMMENT ON TABLE usuario_usrp IS '2024-01-14';

CREATE SCHEMA account AUTHORIZATION postgres;

CREATE TABLE account.hecho_hch (
	chch_llave varchar(32) NOT NULL,
	chch_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_hecho_hch PRIMARY KEY (chch_llave)
);

ALTER TABLE account.hecho_hch ADD dhch_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE account.hecho_hch ADD chch_creacionusuario varchar(32);
ALTER TABLE account.hecho_hch ADD chch_creacionusuarionombre varchar(200);
ALTER TABLE account.hecho_hch ADD dhch_modificacionfecha timestamptz;


ALTER TABLE account.hecho_hch ADD dhch_fecharegistro timestamp with time zone NOT NULL ;
ALTER TABLE account.hecho_hch ADD dhch_fechaevento timestamp with time zone NOT NULL ;
ALTER TABLE account.hecho_hch ADD chch_dimension varchar(32) NOT NULL ;
ALTER TABLE account.hecho_hch ADD chch_valor varchar(100) NOT NULL ;
ALTER TABLE account.hecho_hch ADD chch_codigo varchar(100) NOT NULL ;
ALTER TABLE account.hecho_hch ADD chch_plantilla varchar(32) NOT NULL ;
ALTER TABLE account.hecho_hch ADD chch_id varchar(100) NOT NULL ;


CREATE TABLE account.valor_vlr (
	cvlr_llave varchar(32) NOT NULL,
	cvlr_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_valor_vlr PRIMARY KEY (cvlr_llave)
);

ALTER TABLE account.valor_vlr ADD dvlr_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE account.valor_vlr ADD cvlr_creacionusuario varchar(32);
ALTER TABLE account.valor_vlr ADD cvlr_creacionusuarionombre varchar(200);
ALTER TABLE account.valor_vlr ADD dvlr_modificacionfecha timestamptz;


ALTER TABLE account.valor_vlr ADD cvlr_dimension varchar(32) NOT NULL ;
ALTER TABLE account.valor_vlr ADD cvlr_valor varchar(100) NOT NULL ;
ALTER TABLE account.valor_vlr ADD cvlr_codigo varchar(100) NOT NULL ;
ALTER TABLE account.valor_vlr ADD cvlr_plantilla varchar(32) NOT NULL ;



CREATE TABLE account.comprobante_cmp (
	ccmp_llave varchar(32) NOT NULL,
	ccmp_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_comprobante_cmp PRIMARY KEY (ccmp_llave)
);

ALTER TABLE account.comprobante_cmp ADD dcmp_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE account.comprobante_cmp ADD ccmp_creacionusuario varchar(32);
ALTER TABLE account.comprobante_cmp ADD ccmp_creacionusuarionombre varchar(200);
ALTER TABLE account.comprobante_cmp ADD dcmp_modificacionfecha timestamptz;


CREATE TABLE account.registro_reg (
	creg_llave varchar(32) NOT NULL,
	creg_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_registro_reg PRIMARY KEY (creg_llave)
);

ALTER TABLE account.registro_reg ADD dreg_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE account.registro_reg ADD creg_creacionusuario varchar(32);
ALTER TABLE account.registro_reg ADD creg_creacionusuarionombre varchar(200);
ALTER TABLE account.registro_reg ADD dreg_modificacionfecha timestamptz;


ALTER TABLE account.registro_reg ADD creg_comprobante varchar(32) NOT NULL ;
ALTER TABLE account.registro_reg ADD creg_cuenta varchar(32) NOT NULL ;
ALTER TABLE account.registro_reg ADD creg_codigo varchar(100) NOT NULL ;
ALTER TABLE account.registro_reg ADD creg_descripcion varchar(200);
ALTER TABLE account.registro_reg ADD dreg_fecha timestamp with time zone NOT NULL ;
ALTER TABLE account.registro_reg ADD mreg_positivo NUMERIC(18,6) NOT NULL DEFAULT 0;
ALTER TABLE account.registro_reg ADD mreg_negativo NUMERIC(18,6) NOT NULL DEFAULT 0;
ALTER TABLE account.registro_reg ADD mreg_valor NUMERIC(18,6) NOT NULL DEFAULT 0;
ALTER TABLE account.registro_reg ADD creg_tercero varchar(32);
ALTER TABLE account.registro_reg ADD creg_terceroid varchar(200);
ALTER TABLE account.registro_reg ADD creg_terceronombre varchar(200);
ALTER TABLE account.registro_reg ADD creg_centrocosto varchar(32);
ALTER TABLE account.registro_reg ADD creg_centrocostoid varchar(200);
ALTER TABLE account.registro_reg ADD creg_centrocostonombre varchar(200);


ALTER TABLE account.comprobante_cmp ADD ccmp_catalogo varchar(32) NOT NULL ;
ALTER TABLE account.comprobante_cmp ADD ccmp_codigo varchar(100) NOT NULL ;
ALTER TABLE account.comprobante_cmp ADD ccmp_tipo varchar(32);
ALTER TABLE account.comprobante_cmp ADD ccmp_tiponombre varchar(100);
ALTER TABLE account.comprobante_cmp ADD ccmp_concepto varchar(200);
ALTER TABLE account.comprobante_cmp ADD dcmp_fechacomprobante timestamp with time zone NOT NULL ;
ALTER TABLE account.comprobante_cmp ADD mcmp_positivo NUMERIC(18,6) NOT NULL DEFAULT 0;
ALTER TABLE account.comprobante_cmp ADD mcmp_negativo NUMERIC(18,6) NOT NULL DEFAULT 0;
ALTER TABLE account.comprobante_cmp ADD mcmp_valor NUMERIC(18,6) NOT NULL DEFAULT 0;


CREATE TABLE account.tipocomprobante_tcm (
	ctcm_llave varchar(32) NOT NULL,
	ctcm_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_tipocomprobante_tcm PRIMARY KEY (ctcm_llave)
);

ALTER TABLE account.tipocomprobante_tcm ADD dtcm_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE account.tipocomprobante_tcm ADD ctcm_creacionusuario varchar(32);
ALTER TABLE account.tipocomprobante_tcm ADD ctcm_creacionusuarionombre varchar(200);
ALTER TABLE account.tipocomprobante_tcm ADD dtcm_modificacionfecha timestamptz;


ALTER TABLE account.tipocomprobante_tcm ADD ctcm_catalogo varchar(32) NOT NULL ;
ALTER TABLE account.tipocomprobante_tcm ADD ctcm_nombre varchar(100) NOT NULL ;
ALTER TABLE account.tipocomprobante_tcm ADD ctcm_codigo varchar(100) NOT NULL ;



CREATE TABLE account.mapa_resultados_rmp (
	crmp_llave varchar(32) NOT NULL,
	crmp_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_mapa_resultados_rmp PRIMARY KEY (crmp_llave)
);

ALTER TABLE account.mapa_resultados_rmp ADD drmp_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE account.mapa_resultados_rmp ADD crmp_creacionusuario varchar(32);
ALTER TABLE account.mapa_resultados_rmp ADD crmp_creacionusuarionombre varchar(200);
ALTER TABLE account.mapa_resultados_rmp ADD drmp_modificacionfecha timestamptz;


ALTER TABLE account.mapa_resultados_rmp ADD crmp_catalogo varchar(32) NOT NULL ;
ALTER TABLE account.mapa_resultados_rmp ADD crmp_cuenta varchar(32) NOT NULL ;
ALTER TABLE account.mapa_resultados_rmp ADD nrmp_nivel int NOT NULL DEFAULT 0;
ALTER TABLE account.mapa_resultados_rmp ADD drmp_fechainicio timestamp with time zone NOT NULL ;
ALTER TABLE account.mapa_resultados_rmp ADD drmp_fechafin timestamp with time zone NOT NULL ;
ALTER TABLE account.mapa_resultados_rmp ADD crmp_periodo varchar(100) NOT NULL ;
ALTER TABLE account.mapa_resultados_rmp ADD nrmp_ano int;
ALTER TABLE account.mapa_resultados_rmp ADD nrmp_mes int;
ALTER TABLE account.mapa_resultados_rmp ADD nrmp_dia int;
ALTER TABLE account.mapa_resultados_rmp ADD nrmp_hora int;
ALTER TABLE account.mapa_resultados_rmp ADD nrmp_minuto int;
ALTER TABLE account.mapa_resultados_rmp ADD nrmp_cantidad int NOT NULL DEFAULT 0;
ALTER TABLE account.mapa_resultados_rmp ADD nrmp_promedio NUMERIC(10,2) NOT NULL DEFAULT 0;
ALTER TABLE account.mapa_resultados_rmp ADD mrmp_saldoanterior NUMERIC(18,6) NOT NULL DEFAULT 0;
ALTER TABLE account.mapa_resultados_rmp ADD mrmp_saldosiguiente NUMERIC(18,6) NOT NULL DEFAULT 0;
ALTER TABLE account.mapa_resultados_rmp ADD mrmp_positivo NUMERIC(18,6) NOT NULL DEFAULT 0;
ALTER TABLE account.mapa_resultados_rmp ADD mrmp_negativo NUMERIC(18,6) NOT NULL DEFAULT 0;
ALTER TABLE account.mapa_resultados_rmp ADD mrmp_valor NUMERIC(18,6) NOT NULL DEFAULT 0;
ALTER TABLE account.mapa_resultados_rmp ADD crmp_tipo varchar(1) NOT NULL ;



CREATE TABLE account.formato_frm (
	cfrm_llave varchar(32) NOT NULL,
	cfrm_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_formato_frm PRIMARY KEY (cfrm_llave)
);

ALTER TABLE account.formato_frm ADD dfrm_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE account.formato_frm ADD cfrm_creacionusuario varchar(32);
ALTER TABLE account.formato_frm ADD cfrm_creacionusuarionombre varchar(200);
ALTER TABLE account.formato_frm ADD dfrm_modificacionfecha timestamptz;


ALTER TABLE account.formato_frm ADD cfrm_catalogo varchar(32) NOT NULL ;
ALTER TABLE account.formato_frm ADD cfrm_plantilla varchar(32) NOT NULL ;


CREATE TABLE account.linea_lin (
	clin_llave varchar(32) NOT NULL,
	clin_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_linea_lin PRIMARY KEY (clin_llave)
);

ALTER TABLE account.linea_lin ADD dlin_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE account.linea_lin ADD clin_creacionusuario varchar(32);
ALTER TABLE account.linea_lin ADD clin_creacionusuarionombre varchar(200);
ALTER TABLE account.linea_lin ADD dlin_modificacionfecha timestamptz;


ALTER TABLE account.linea_lin ADD clin_formato varchar(32) NOT NULL ;
ALTER TABLE account.linea_lin ADD clin_cuenta varchar(32) NOT NULL ;
ALTER TABLE account.linea_lin ADD clin_description varchar(200);
ALTER TABLE account.linea_lin ADD clin_positivo varchar(100);
ALTER TABLE account.linea_lin ADD clin_negativo varchar(100);


CREATE TABLE account.dimension_dim (
	cdim_llave varchar(32) NOT NULL,
	cdim_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_dimension_dim PRIMARY KEY (cdim_llave)
);

ALTER TABLE account.dimension_dim ADD ddim_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE account.dimension_dim ADD cdim_creacionusuario varchar(32);
ALTER TABLE account.dimension_dim ADD cdim_creacionusuarionombre varchar(200);
ALTER TABLE account.dimension_dim ADD ddim_modificacionfecha timestamptz;


ALTER TABLE account.dimension_dim ADD cdim_cuenta varchar(32) NOT NULL ;
ALTER TABLE account.dimension_dim ADD cdim_nombre varchar(100) NOT NULL ;
ALTER TABLE account.dimension_dim ADD cdim_codigo varchar(100) NOT NULL ;
ALTER TABLE account.dimension_dim ADD cdim_campo varchar(32) NOT NULL ;
ALTER TABLE account.dimension_dim ADD cdim_tipo varchar(1) NOT NULL ;


CREATE TABLE account.catalogo_ctg (
	cctg_llave varchar(32) NOT NULL,
	cctg_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_catalogo_ctg PRIMARY KEY (cctg_llave)
);

ALTER TABLE account.catalogo_ctg ADD dctg_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE account.catalogo_ctg ADD cctg_creacionusuario varchar(32);
ALTER TABLE account.catalogo_ctg ADD cctg_creacionusuarionombre varchar(200);
ALTER TABLE account.catalogo_ctg ADD dctg_modificacionfecha timestamptz;


ALTER TABLE account.catalogo_ctg ADD cctg_nombre varchar(100) NOT NULL ;
ALTER TABLE account.catalogo_ctg ADD cctg_codigo varchar(8) NOT NULL ;
ALTER TABLE account.catalogo_ctg ADD dctg_fechainicial timestamp with time zone NOT NULL ;
ALTER TABLE account.catalogo_ctg ADD dctg_fechafinal timestamp with time zone NOT NULL ;
ALTER TABLE account.catalogo_ctg ADD cctg_consecutivo varchar(32);



CREATE TABLE account.cuenta_cue (
	ccue_llave varchar(32) NOT NULL,
	ccue_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_cuenta_cue PRIMARY KEY (ccue_llave)
);

ALTER TABLE account.cuenta_cue ADD dcue_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE account.cuenta_cue ADD ccue_creacionusuario varchar(32);
ALTER TABLE account.cuenta_cue ADD ccue_creacionusuarionombre varchar(200);
ALTER TABLE account.cuenta_cue ADD dcue_modificacionfecha timestamptz;


ALTER TABLE account.cuenta_cue ADD ccue_catalogo varchar(32) NOT NULL ;
ALTER TABLE account.cuenta_cue ADD ccue_wbs varchar(50) NOT NULL ;
ALTER TABLE account.cuenta_cue ADD ccue_nombre varchar(100) NOT NULL ;
ALTER TABLE account.cuenta_cue ADD ccue_codigo varchar(100);
ALTER TABLE account.cuenta_cue ADD ccue_situacion varchar(10) NOT NULL ;
ALTER TABLE account.cuenta_cue ADD ccue_padre varchar(32);
ALTER TABLE account.cuenta_cue ADD ncue_nivel int NOT NULL DEFAULT 0;
ALTER TABLE account.cuenta_cue ADD ccue_tipo varchar(1) NOT NULL ;
ALTER TABLE account.cuenta_cue ADD ccue_naturaleza varchar(1) NOT NULL ;
ALTER TABLE account.cuenta_cue ADD ccue_plantilla varchar(32);
ALTER TABLE account.cuenta_cue ADD ccue_campo varchar(32);

