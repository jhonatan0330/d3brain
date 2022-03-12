COMMENT ON TABLE usuario_usrp IS '2019-10-06';
COMMENT ON TABLE usuariosesion_ussp IS '2019.10.05.00';

ALTER TABLE mensaje_msjp
	DROP CONSTRAINT fk_mensajetemplate;

DROP TABLE mensajetransiciondestino_mtdp;

DROP TABLE mensajeprocesotransicion_mptp;

CREATE TABLE mensajeplantillacorreo_mplp (
	cmpl_nombre character varying(100) NOT NULL,
	cmpl_llave character varying(32) NOT NULL,
	cmpl_texto character varying(4000) NOT NULL,
	cmpl_host character varying(100) NOT NULL,
	cmpl_usuario character varying(100) NOT NULL,
	cmpl_clave character varying(100) NOT NULL,
	cmpl_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

ALTER TABLE mensaje_msjp
	ADD COLUMN cmsj_correoerror character varying(4000);

ALTER TABLE mensajeplantillacorreo_mplp
	ADD CONSTRAINT pk_mensajeplantillacorreo_mplp PRIMARY KEY (cmpl_llave);

ALTER TABLE mensaje_msjp
	ADD CONSTRAINT fk_mensajetemplate FOREIGN KEY (cmsj_template) REFERENCES mensajeplantillacorreo_mplp(cmpl_llave);

update pedidoventa_pdvp set cpdv_textofiltro = null where cpdv_textofiltro = '';

CREATE OR REPLACE VIEW valor_documento AS
	SELECT pedidoventadinero_pvdp.cpvd_documento AS documento,
    pedidoventadinero_pvdp.mpvd_valorsubtotal AS subtotal,
    pedidoventadinero_pvdp.mpvd_valortotal AS total,
    pedidoventadinero_pvdp.mpvd_saldo AS saldo,
    pedidoventadinero_pvdp.dpvd_fecha AS fecha
   FROM public.pedidoventadinero_pvdp
  WHERE ((pedidoventadinero_pvdp.cpvd_estado)::text = 'A'::text);
