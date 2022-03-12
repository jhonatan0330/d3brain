COMMENT ON TABLE usuario_usrp IS '2019-09-30';

COMMENT ON TABLE usuariosesion_ussp IS '2019.09.30.00';

ALTER TABLE documentoplantilla_dplp
	ADD COLUMN cdpl_proceso character varying(32);

ALTER TABLE documentoplantilla_dplp
	ADD CONSTRAINT fk_documentoplantillaproceso FOREIGN KEY (cdpl_proceso) REFERENCES proceso_prcp(cprc_llave);

update documentoplantilla_dplp set cdpl_proceso = (select cptr_proceso from procesotransicion_ptrp where cptr_plantilla = cdpl_llave limit 1);

delete from propiedad_ppdp where cppd_key = 'OMITIR_FECHAS';

INSERT INTO propiedad_ppdp (cppd_llave, cppd_tipo, cppd_campo, cppd_key, cppd_valor)
select substring('OF_' || cpdv_plantilla, 0,32), 'L', cpdv_plantilla, 'SOLICITAR_FECHAS' , 'true' from pedidoventa_pdvp 
where cpdv_plantilla not in (select cdpl_llave from documentoplantilla_dplp where cdpl_tipo in ('L','O'))
group by cpdv_plantilla
having count(*) >= 1000;
