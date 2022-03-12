
COMMENT ON TABLE usuario_usrp IS '2019-11-11';

COMMENT ON TABLE usuariosesion_ussp IS '2019.11.09.00';

delete from mensaje_msjp;

ALTER TABLE mensaje_msjp
	ALTER COLUMN cmsj_titulo TYPE character varying(100);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_formato, cpvd_ayuda) 
	VALUES('PROP_86' , 'S', 'REQUERIMIENTO_TIPO_REPORTE', 'REQUERIMIENTO_TIPO_REPORTE', 'T', 'www.softwareparati.com');

delete from requerimiento_reqp;

ALTER TABLE propiedadvalordefinido_pvdp
	DROP COLUMN bpvd_lazy,
	ADD COLUMN bpvd_incluirorigen boolean DEFAULT false NOT NULL;

ALTER TABLE requerimiento_reqp
	DROP COLUMN creq_avance,
	ADD COLUMN dreq_fechaimplementacion timestamp with time zone,
	ALTER COLUMN creq_clasificacion TYPE character varying(32) /* TYPE change - table: requerimiento_reqp original: character varying(1) new: character varying(32) */;

ALTER TABLE categoriaproducto_cprp
	ADD CONSTRAINT fk_categoriaproductonodosuperior FOREIGN KEY (ccpr_nodosuperior) REFERENCES categoriaproducto_cprp(ccpr_llave);

ALTER TABLE requerimiento_reqp
	ADD CONSTRAINT fk_requerimientoclasificacion FOREIGN KEY (creq_clasificacion) REFERENCES propiedad_ppdp(cppd_llave);
	

ALTER TABLE relacioninterna_ritp
	DROP CONSTRAINT fk_relacioninternacampodestino;

ALTER TABLE relacioninterna_ritp
	DROP CONSTRAINT fk_relacioninternacampoorigen;

ALTER TABLE relacioninterna_ritp
	DROP CONSTRAINT fk_relacioninternaplantilladestino;

ALTER TABLE relacioninterna_ritp
	DROP CONSTRAINT fk_relacioninternaplantillaorigen;

ALTER TABLE relacioninterna_ritp
	DROP CONSTRAINT fk_relacioninternarequerimiento;

DROP TABLE requerimiento_reqp;

ALTER TABLE propiedad_ppdp
	ADD COLUMN dppd_fechadefinicion timestamp with time zone,
	ADD COLUMN dppd_fechaimplementacion timestamp with time zone;

update propiedad_ppdp set dppd_fechadefinicion = now(); 

ALTER TABLE propiedad_ppdp
	ALTER COLUMN dppd_fechadefinicion SET NOT NULL;

delete from relacioninterna_ritp;

ALTER TABLE relacioninterna_ritp
	DROP COLUMN crit_plantillaorigen,
	DROP COLUMN crit_campoorigen,
	DROP COLUMN crit_plantilladestino,
	DROP COLUMN crit_campodestino,
	DROP COLUMN crit_requerimiento,
	ADD COLUMN crit_propiedad character varying(32),
	ADD COLUMN crit_plantilla character varying(32) NOT NULL,
	ADD COLUMN crit_campo character varying(32) NOT NULL;

ALTER TABLE relacioninterna_ritp
	ADD CONSTRAINT fk_relacioninternapropiedad FOREIGN KEY (crit_propiedad) REFERENCES public.propiedad_ppdp(cppd_llave);
