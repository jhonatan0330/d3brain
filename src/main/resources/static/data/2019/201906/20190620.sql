
COMMENT ON TABLE usuario_usrp IS '2019-06-20';
COMMENT ON TABLE usuariosesion_ussp IS '2019.06.20.00';

ALTER TABLE procesoestado_pesp
	DROP CONSTRAINT fk_procesoestadoencargado;

ALTER TABLE procesoestado_pesp
	ADD COLUMN cpes_funcionasignacion character varying(4000);

update procesoestado_pesp set cpes_funcionasignacion = 'BEGIN RETURN (select cusr_llave from usuario_usrp  where cusr_identificacion = ''' || (select cusr_identificacion from usuariorol_erlp, usuario_usrp  where cerl_usuario = cusr_llave and cerl_documento = cpes_encargado) || ''');END;'
where cpes_encargado is not null;


ALTER TABLE procesoestado_pesp
	DROP COLUMN cpes_encargado;

DROP VIEW filtro_documento;

ALTER TABLE pedidoventa_pdvp
	ADD COLUMN cpdv_textofiltro character varying(4000);