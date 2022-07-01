COMMENT ON TABLE usuario_usrp IS '2021-06-25';
COMMENT ON TABLE usuariosesion_ussp IS '2021.06.25.00';

ALTER TABLE documentoplantilla_dplp
	ADD COLUMN cdpl_proceso character varying(32);

update documentoplantilla_dplp set cdpl_proceso = 
	(select cptr_proceso from procesotransicion_ptrp where cptr_plantilla = cdpl_llave and cptr_estado = 'A' limit 1);

update documentoplantilla_dplp set cdpl_proceso = 
	(select cprc_llave from proceso_prcp where cprc_estado = 'A' order by nprc_prioridad desc limit 1)
where cdpl_proceso is null;

ALTER TABLE documentoplantilla_dplp
	alter COLUMN cdpl_proceso set not null;

ALTER TABLE proceso_prcp
	ADD COLUMN cprc_imagen character varying(2000);

update proceso_prcp set cprc_imagen = 
(select cdpl_imagen from documentoplantilla_dplp 
	inner join procesotransicion_ptrp on (cdpl_llave = cptr_plantilla and cptr_estado = 'A' and cptr_estadopartida is null)
	where cptr_proceso = cprc_llave and cdpl_estado = 'A' limit 1);

update proceso_prcp set cprc_imagen = 'http://golyat.cloud/imagenes/modulo.png'
where cprc_imagen is null;
