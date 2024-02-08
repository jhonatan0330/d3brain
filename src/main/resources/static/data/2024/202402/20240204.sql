COMMENT ON TABLE usuario_usrp IS '2024-02-04';

ALTER TABLE tarifa_tarp ADD dtar_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE tarifa_tarp ADD ctar_creacionusuario varchar(32);
ALTER TABLE tarifa_tarp ADD dtar_modificacionfecha timestamptz;
ALTER TABLE tarifa_tarp ADD ctar_modificacionusuario varchar(32);

--ALTER TABLE tarifa_tarp ALTER COLUMN ctar_creacionusuario NOT NULL;

ALTER TABLE tarifario_trfp ADD dtrf_fechainicial timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE tarifario_trfp ADD dtrf_fechafinal timestamptz;

INSERT INTO modulo_modp (cmod_llave, cmod_nombre, cmod_url)
VALUES('apps.tariff', 'Tarifas', '/tariff');


INSERT INTO permiso_perp (cper_llave, cper_rolacceso, cper_modulo, cper_estado) 
SELECT replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''),
	rr.crac_llave, 'apps.tariff' , 'I'
from rolacceso_racp rr
where rr.crac_estado = 'A';