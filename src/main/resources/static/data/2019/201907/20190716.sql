COMMENT ON TABLE usuario_usrp IS '2019-07-16';
COMMENT ON TABLE usuariosesion_ussp IS '2019.07.16.00';

DROP TABLE if exists mensajeplantilla_mplp;

CREATE TABLE mensajeprocesotransicion_mptp (
	cmpt_llave character varying(32) NOT NULL,
	cmpt_transicion character varying(32) NOT NULL,
	cmpt_texto character varying(4000) NOT NULL,
	bmpt_creador boolean DEFAULT false NOT NULL,
	bmpt_asignado boolean DEFAULT false NOT NULL,
	cmpt_host character varying(1) NOT NULL,
	cmpt_usuario character varying(1) NOT NULL,
	cmpt_clave character varying(1) NOT NULL,
	cmpt_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

CREATE TABLE mensajetransiciondestino_mtdp (
	cmtd_llave character varying(32) NOT NULL,
	cmtd_usuario character varying(32) NOT NULL,
	cmtd_mensaje character varying(32) NOT NULL,
	cmtd_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

ALTER TABLE mensaje_msjp
	DROP COLUMN bmsj_leido,
	DROP COLUMN bmsj_enviado,
	ADD COLUMN cmsj_titulo character varying(100) NOT NULL,
	ADD COLUMN dmsj_leido timestamp with time zone,
	ADD COLUMN dmsj_correoenviado timestamp with time zone;

ALTER TABLE mensajeprocesotransicion_mptp
	ADD CONSTRAINT pk_mensajeprocesotransicion_mptp PRIMARY KEY (cmpt_llave);

ALTER TABLE mensajetransiciondestino_mtdp
	ADD CONSTRAINT pk_mensajetransiciondestino_mtdp PRIMARY KEY (cmtd_llave);

ALTER TABLE mensaje_msjp
	ADD CONSTRAINT fk_mensajedocumento FOREIGN KEY (cmsj_documento) REFERENCES public.pedidoventa_pdvp(cpdv_llave);

ALTER TABLE mensaje_msjp
	ADD CONSTRAINT fk_mensajetemplate FOREIGN KEY (cmsj_template) REFERENCES public.mensajeprocesotransicion_mptp(cmpt_llave);

ALTER TABLE mensajeprocesotransicion_mptp
	ADD CONSTRAINT fk_mensajeprocesotransiciontransicion FOREIGN KEY (cmpt_transicion) REFERENCES public.procesotransicion_ptrp(cptr_llave);

ALTER TABLE mensajetransiciondestino_mtdp
	ADD CONSTRAINT fk_mensajetransiciondestinomensaje FOREIGN KEY (cmtd_mensaje) REFERENCES public.mensajeprocesotransicion_mptp(cmpt_llave);

ALTER TABLE mensajetransiciondestino_mtdp
	ADD CONSTRAINT fk_mensajetransiciondestinousuario FOREIGN KEY (cmtd_usuario) REFERENCES public.usuario_usrp(cusr_llave);
