COMMENT ON TABLE usuario_usrp IS '2019-01-29';

ALTER TABLE productoinventario_pinp
	ADD COLUMN dpin_fechainicial timestamp with time zone;

update productoinventario_pinp set dpin_fechainicial = current_date;

ALTER TABLE productoinventario_pinp
	ALTER COLUMN dpin_fechainicial SET NOT NULL;