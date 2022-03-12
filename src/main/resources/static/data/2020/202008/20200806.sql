COMMENT ON TABLE usuario_usrp IS '2020-08-06';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_codigo, cpvd_nombre, cpvd_ayuda,  cpvd_grupo, bpvd_textoculto) 
	VALUES('PROP_146' , 'A', 'DECISION_SQL', 'FUNCION SQL DECISION', 'www.softwareparati.com', 'REQUISITO', true);

update propiedadvalordefinido_pvdp set bpvd_pidefechas = true, bpvd_solicitamotivo = true 
	where cpvd_llave = 'PROP_140';

update procesodecisionrespuesta_pdrp set cpdr_estado = 'I' where cpdr_decision in (
	select cpdc_llave from procesodecision_pdcp where cpdc_estado = 'I');

ALTER TABLE procesotransicion_ptrp
	DROP CONSTRAINT fk_procesotransiciondecision;

ALTER TABLE procesoestado_pesp
	ADD COLUMN cpes_tipo character varying(1);

update procesoestado_pesp set cpes_tipo = 'E';

ALTER TABLE procesoestado_pesp
	alter COLUMN cpes_tipo SET NOT NULL;

INSERT INTO procesoestado_pesp (cpes_llave, npes_avance, cpes_nombre, cpes_proceso, cpes_estadodocumento, cpes_codigo, cpes_tipo, cpes_estado) 
	select cpdc_llave, npdc_avance, cpdc_pregunta, cpdc_proceso, 'A', 'D', 'D',cpdc_estado from procesodecision_pdcp;

INSERT INTO cambio_cmbp(ccmb_llave,  ccmb_nombre,  ccmb_motivo,  dcmb_fecha)
	VALUES('SC_20200806',  'SC_20200806',  'Union de las decisiones con los estados',  now());

INSERT INTO propiedad_ppdp (cppd_llave, cppd_campo, cppd_valor, cppd_propiedadvalor, dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion,  cppd_tipo ) 
	select cpdc_llave, cpdc_llave, cpdc_cuerpo, 'PROP_146', now(), now(), 'SC_20200806', 'A' from procesodecision_pdcp where cpdc_estado = 'A';

update procesotransicion_ptrp set cptr_estadollegada = cptr_decision where cptr_decision is not null;

ALTER TABLE procesotransicion_ptrp
	DROP COLUMN cptr_decision;

INSERT INTO procesotransicion_ptrp (cptr_llave, cptr_nombre, cptr_proceso, cptr_estadopartida, cptr_estadollegada)
select cpdr_llave, cpdr_respuesta, 
	(select cpdc_proceso from procesodecision_pdcp where cpdc_llave= cpdr_decision),
	cpdr_decision, cpdr_estadollegada
from procesodecisionrespuesta_pdrp where cpdr_estado = 'A';

DROP TABLE procesodecisionrespuesta_pdrp;

DROP TABLE procesodecision_pdcp;

ALTER TABLE procesoestado_pesp 
	DROP COLUMN cpes_codigo;