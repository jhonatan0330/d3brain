COMMENT ON TABLE usuario_usrp IS '2023-10-30';

alter table permiso_perp drop CONSTRAINT if exists fk_permisomodulo;

DROP TABLE modulocontratado_mdcp;

update permiso_perp set cper_modulo = 'AdministracionLogisticpymes' where cper_modulo = 'ADMINISTRACION'; 

--ALTER TABLE permiso_perp ADD CONSTRAINT fk_permisomodulo FOREIGN KEY (cper_modulo) REFERENCES modulo_modp(cmod_llave)

ALTER TABLE modulo_modp ADD cmod_imagen varchar(2000) NULL;

INSERT INTO modulo_modp(cmod_llave, cmod_nombre, cmod_imagen, cmod_url)
VALUES('apps.maps', 'Mapas', 'heroicons_outline:map', '/maps');

INSERT INTO modulo_modp(cmod_llave, cmod_nombre, cmod_imagen, cmod_url)
VALUES('apps.tasks', 'TO-DO', 'heroicons_outline:check-circle', '/tasks');

INSERT INTO modulo_modp(cmod_llave, cmod_nombre, cmod_imagen, cmod_url)
VALUES('apps.designer', 'Diseñador', 'heroicons_outline:cube-transparent', '/designer');

INSERT INTO modulo_modp(cmod_llave, cmod_nombre, cmod_imagen, cmod_url)
VALUES('apps.academy', 'Academia', 'heroicons_outline:academic-cap', '/academy');

INSERT INTO modulo_modp(cmod_llave, cmod_nombre, cmod_imagen, cmod_url)
VALUES('apps.inventary', 'Inventario', 'heroicons_outline:archive', '/inventory');

INSERT INTO modulo_modp(cmod_llave, cmod_nombre, cmod_imagen, cmod_url)
VALUES('apps.accounting', 'Indicadores', 'heroicons_outline:chart-pie', '/account');



INSERT INTO permiso_perp (cper_llave, cper_rolacceso, cper_modulo, cper_estado) 
SELECT 
	replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''),
	rr.crac_llave ,
	mm.cmod_llave , 'I'
from rolacceso_racp rr
cross join modulo_modp mm
where mm.cmod_llave !='AdministracionLogisticpymes'
	and mm.cmod_llave !='UIVotante'
	and rr.crac_estado = 'A'
