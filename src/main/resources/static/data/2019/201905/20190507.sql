COMMENT ON TABLE usuario_usrp IS '2019-05-07';

ALTER TABLE rodamientoasignacion_rasp
	ADD CONSTRAINT fk_rodamientoasignacionviajefinal FOREIGN KEY (cras_viajefinal) REFERENCES public.viaje_viap(cvia_llave);

ALTER TABLE rodamientoasignacion_rasp
	ADD CONSTRAINT fk_rodamientoasignacionviajeinicial FOREIGN KEY (cras_viajeinicial) REFERENCES public.viaje_viap(cvia_llave);

ALTER TABLE viajeescala_esvp
	ADD CONSTRAINT fk_viajeescalaviaje FOREIGN KEY (cesv_viaje) REFERENCES public.viaje_viap(cvia_llave);

CREATE OR REPLACE FUNCTION sw42_prioridad_a(character varying)
  RETURNS int LANGUAGE plpgsql AS '
BEGIN 
	RETURN 1;
END;
';

CREATE OR REPLACE FUNCTION sw42_prioridad_b(character varying)
  RETURNS int LANGUAGE plpgsql AS '
BEGIN 
	RETURN 1;
END;
';

CREATE OR REPLACE FUNCTION sw42_prioridad_c(character varying)
  RETURNS int LANGUAGE plpgsql AS '
BEGIN 
	RETURN 1;
END;
';