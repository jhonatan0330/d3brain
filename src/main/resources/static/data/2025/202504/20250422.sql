COMMENT ON TABLE usuario_usrp IS '2025-04-22';

ALTER TABLE account.pila_stk ADD CONSTRAINT fk_pilacomprobante FOREIGN KEY (cstk_comprobante) REFERENCES account.comprobante_cmp(ccmp_llave);

ALTER TABLE account.registro_reg ADD CONSTRAINT fk_registrocomprobante FOREIGN KEY (creg_comprobante) REFERENCES account.comprobante_cmp(ccmp_llave);

ALTER TABLE account.registro_reg ADD CONSTRAINT fk_registrocuenta FOREIGN KEY (creg_cuenta) REFERENCES account.cuenta_cue(ccue_llave);

ALTER TABLE account.tipocomprobante_tcm ADD CONSTRAINT fk_tipocomprobantecatalogo FOREIGN KEY (ctcm_catalogo) REFERENCES account.catalogo_ctg(cctg_llave);

ALTER TABLE account.comprobante_cmp ADD CONSTRAINT fk_comprobantecatalogo FOREIGN KEY (ccmp_catalogo) REFERENCES account.catalogo_ctg(cctg_llave);

ALTER TABLE account.comprobante_cmp ADD CONSTRAINT fk_comprobantetipo FOREIGN KEY (ccmp_tipo) REFERENCES account.tipocomprobante_tcm(ctcm_llave);

update propiedad_ppdp pp 
set cppd_valor = (select pcc.cprc_macroproceso from proceso_prcp pcc where pcc.cprc_llave = cppd_valor)
,cppd_texto = (select cprc_nombre from proceso_prcp pc where pc.cprc_llave = (select pcc.cprc_macroproceso from proceso_prcp pcc where pcc.cprc_llave = cppd_valor) )
where pp.cppd_estado = 'A' and pp.cppd_propiedadvalor = 'PROP_107'
  and (select cprc_nombre from proceso_prcp pc where pc.cprc_llave = (select pcc.cprc_macroproceso from proceso_prcp pcc where pcc.cprc_llave = cppd_valor) ) is not null;

  
DROP TABLE account.valor_vlr;
 
DROP TABLE account.linea_lin ;

DROP TABLE account.hecho_hch;

DROP TABLE account.formato_frm;

DROP TABLE account.dimension_dim;


CREATE TABLE account.registroauxiliar_rax (
	crax_llave varchar(32) NOT NULL,
	crax_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_registroauxiliar_rax PRIMARY KEY (crax_llave)
);

ALTER TABLE account.registroauxiliar_rax ADD crax_comprobante varchar(32) NOT NULL ;
ALTER TABLE account.registroauxiliar_rax ADD crax_registro varchar(32) NOT NULL ;
ALTER TABLE account.registroauxiliar_rax ADD crax_cuenta varchar(32) NOT NULL ;
ALTER TABLE account.registroauxiliar_rax ADD crax_auxiliartipo varchar(32) NOT NULL ;
ALTER TABLE account.registroauxiliar_rax ADD crax_auxiliardocumento varchar(32);
ALTER TABLE account.registroauxiliar_rax ADD crax_auxiliarcode varchar(200) NOT NULL ;
ALTER TABLE account.registroauxiliar_rax ADD crax_auxiliarnombre varchar(200) NOT NULL ;

ALTER TABLE account.registroauxiliar_rax ADD drax_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE account.registroauxiliar_rax ADD crax_creacionusuario varchar(32);
ALTER TABLE account.registroauxiliar_rax ADD crax_creacionusuarionombre varchar(200);
ALTER TABLE account.registroauxiliar_rax ADD drax_modificacionfecha timestamptz;