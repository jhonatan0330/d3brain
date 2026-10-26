COMMENT ON TABLE usuario_usrp IS '2026-10-21';

update propiedadvalordefinido_pvdp
set cpvd_uso_relaciones = 'CAMPO DE LA PLANTILLA DESTINO QUE GUARDA LA RELACION CON LA PLANTILLA ORIGEN'
where cpvd_llave in ('PROP_288');

update propiedadvalordefinido_pvdp
set cpvd_uso_relaciones = 'VINCULO (CAMPO A COPIAR VALOR)'
where cpvd_llave in ('PROP_96');

update propiedadvalordefinido_pvdp
set cpvd_uso_relaciones = 'CAMPO DE LA PLANTILLA DESTINO QUE GUARDA LA RELACION CON LA PLANTILLA ORIGEN'
where cpvd_llave in ('PROP_292');