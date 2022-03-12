
COMMENT ON TABLE usuario_usrp IS '2018-11-03';
COMMENT ON TABLE usuariosesion_ussp IS '2018.11.03.00';

ALTER TABLE procesoestadoactividad_peap
	DROP CONSTRAINT fk_procesoestadoactividadestado;

ALTER TABLE modeladonegocio_mngp
	DROP COLUMN dmng_fechavalidacion,
	DROP COLUMN cmng_logcomparacion,
	DROP COLUMN dmng_fechaimplementacion,
	DROP COLUMN cmng_logimplementacion;

ALTER TABLE procesotransicion_ptrp
	DROP COLUMN IF EXISTS cptr_plantillacomplemento;

ALTER TABLE rolacceso_racp
	ALTER COLUMN crac_codigo TYPE character varying(20) /* TYPE change - table: rolacceso_racp original: character varying(3) new: character varying(20) */;

ALTER TABLE documentoplantilla_dplp
	DROP CONSTRAINT IF EXISTS documentoplantilla_dplp_cdpl_codigo_key;
	
ALTER TABLE documentoplantilla_dplp
	ADD CONSTRAINT documentoplantilla_dplp_cdpl_codigo_key UNIQUE (cdpl_codigo);

ALTER TABLE procesoestadoactividad_peap
	ADD CONSTRAINT fk_procesoestadoactividadestadoproceso FOREIGN KEY (cpea_estadoproceso) REFERENCES public.procesoestado_pesp(cpes_llave);

ALTER TABLE actividad_actp
	DROP CONSTRAINT IF EXISTS fk_actividadresponsable;

ALTER TABLE actividad_actp
	ADD CONSTRAINT fk_actividadresponsable FOREIGN KEY (cact_responsable) REFERENCES public.usuario_usrp(cusr_llave);

ALTER TABLE procesoestadoactividad_peap
	DROP CONSTRAINT fk_procesoestadoactividadlider;

ALTER TABLE procesoestadoactividad_peap
	DROP COLUMN cpea_lider,
	ADD COLUMN cpea_encargado character varying(32);

ALTER TABLE procesoestadoactividad_peap
	ADD CONSTRAINT fk_procesoestadoactividadencargado FOREIGN KEY (cpea_encargado) REFERENCES public.pedidoventa_pdvp(cpdv_llave);
