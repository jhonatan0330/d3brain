
COMMENT ON TABLE usuario_usrp IS '2019-02-23';
COMMENT ON TABLE usuariosesion_ussp IS '2019.02.23.00';

ALTER TABLE documentoplantilla_dplp DROP CONSTRAINT fk_documentoplantillaproceso;
ALTER TABLE procesotransicion_ptrp DROP CONSTRAINT fk_procesotransicioncampo;

ALTER TABLE propiedadsistema_psip
	ALTER COLUMN cpsi_valor TYPE character varying(4000) /* TYPE change - table: propiedadsistema_psip original: character varying(100) new: character varying(4000) */,
	ALTER COLUMN cpsi_texto TYPE character varying(4000) /* TYPE change - table: propiedadsistema_psip original: character varying(100) new: character varying(4000) */;

ALTER TABLE procesodecisionrespuesta_pdrp ADD CONSTRAINT fk_procesodecisionrespuestaestadollegada FOREIGN KEY (cpdr_estadollegada) REFERENCES public.procesoestado_pesp(cpes_llave);

ALTER TABLE procesotransicion_ptrp DROP COLUMN cptr_campo;

INSERT INTO procesotransicion_ptrp(cptr_llave, cptr_nombre, cptr_proceso, cptr_plantilla, cptr_afectasaldo, cptr_estadollegada)
	select substring('TRAN' || cdpl_llave, 0,32), cdpl_nombre, cdpl_proceso, cdpl_llave
		,(select 'S' where exists ( select cptr_llave from procesotransicion_ptrp  where cptr_proceso  = cdpl_proceso and cptr_estado = 'A' and cptr_afectasaldo is not null))
		,(select cpes_llave from procesoestado_pesp where cpes_proceso = cdpl_proceso and npes_nivel= 0)
	from documentoplantilla_dplp  where cdpl_proceso is not null and cdpl_estado = 'A';

ALTER TABLE documentoplantilla_dplp DROP COLUMN cdpl_proceso;
