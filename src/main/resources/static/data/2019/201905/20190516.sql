
COMMENT ON TABLE usuario_usrp IS '2019-05-16';

ALTER TABLE viaje_viap
	DROP COLUMN cvia_rodamientogeneracion,
	DROP COLUMN cvia_viajesiguienterodamiento,
	DROP COLUMN cvia_cliente,
	DROP COLUMN cvia_contacto,
	ADD COLUMN cvia_nombre character varying(32) NOT NULL;

ALTER TABLE cupoviaje_cvjp
	ADD CONSTRAINT fk_cupoviajeviaje FOREIGN KEY (ccvj_viaje) REFERENCES public.viaje_viap(cvia_llave);


update plantillacampoparametro_pcpp set cpcp_valor = '*' where cpcp_key = 'PROCESO_GESTIONAR_ESTADOS';