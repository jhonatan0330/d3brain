COMMENT ON TABLE usuario_usrp IS '2022-11-29';

ALTER TABLE mensajeplantillacorreo_mplp
	ALTER COLUMN cmpl_servidor DROP NOT NULL;

update mensajeplantillacorreo_mplp set cmpl_servidor = null;

INSERT INTO cambio_cmbp(ccmb_llave,  ccmb_nombre,  ccmb_motivo,  dcmb_fecha)
	VALUES('SC_20221129',  'SC_20221129',  'Las relaciones deben tener historial',  now());
	
ALTER TABLE relacioninterna_ritp
	ADD COLUMN drit_fechainicio timestamp with time zone,
	ADD COLUMN crit_cambiocreacion character varying(32),
	ADD COLUMN crit_cambioeliminacion character varying(32);

update relacioninterna_ritp set drit_fechainicio = now();

update relacioninterna_ritp set crit_cambiocreacion = 'SC_20221129';

ALTER TABLE relacioninterna_ritp
	ALTER COLUMN drit_fechainicio SET NOT NULL,
	ALTER COLUMN crit_cambiocreacion SET NOT NULL;
	