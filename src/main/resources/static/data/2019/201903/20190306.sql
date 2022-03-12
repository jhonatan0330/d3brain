
COMMENT ON TABLE usuario_usrp IS '2019-03-06';
COMMENT ON TABLE usuariosesion_ussp IS '2019.03.06.00';

ALTER TABLE actividad_actp
	DROP COLUMN nact_tiempo,
	DROP COLUMN dact_fechafin;

ALTER TABLE procesoestado_pesp
	ADD COLUMN cpes_rol character varying(32),
	ADD COLUMN cpes_encargado character varying(32);

ALTER TABLE procesoestado_pesp
	ADD CONSTRAINT fk_procesoestadoencargado FOREIGN KEY (cpes_encargado) REFERENCES pedidoventa_pdvp(cpdv_llave);

ALTER TABLE procesoestado_pesp
	ADD CONSTRAINT fk_procesoestadorol FOREIGN KEY (cpes_rol) REFERENCES rolacceso_racp(crac_llave);

update procesoestado_pesp set cpes_rol = (select cpea_rol from procesoestadoactividad_peap where cpea_estado = 'A' and cpea_estadoproceso = cpes_llave)
	where cpes_llave in ( select cpea_estadoproceso from procesoestadoactividad_peap where cpea_estado = 'A');

update procesoestado_pesp set cpes_encargado = (select cpea_encargado from procesoestadoactividad_peap where cpea_estado = 'A' and cpea_estadoproceso = cpes_llave)
	where cpes_llave in ( select cpea_estadoproceso from procesoestadoactividad_peap where cpea_estado = 'A');
	
DROP TABLE procesoestadoactividad_peap;