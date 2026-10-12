COMMENT ON TABLE usuario_usrp IS '2026-10-09';


update propiedadvalordefinido_pvdp
set cpvd_uso_relaciones = 'EXTRACCIONES DE CADA CAMPO DEL FORMULARIO'
where cpvd_llave in ('PROP_171');

update propiedadvalordefinido_pvdp
set cpvd_uso_relaciones = 'EXTRACCIONES DE CADA CAMPO DEL FORMULARIO', cpvd_uso_motivo = 'REGLA DE EXTRACCION DEL SEGUNDO DOCUMENTO'
where cpvd_llave in ('PROP_172');

update propiedadvalordefinido_pvdp
set cpvd_uso_relaciones = 'CAMPOS A INCLUIR EN EL API'
where cpvd_llave in ('PROP_177', 'PROP_174', 'PROP_173');
