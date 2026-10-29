COMMENT ON TABLE usuario_usrp IS '2026-10-27';

update propiedadvalordefinido_pvdp
set cpvd_uso_relaciones = 'CAMPO QUE RELACIONA ESTE REGISTRO EN AL OTRA PLANTILLA'
where cpvd_llave in ('PROP_33');

ALTER TABLE public.cargaarchivo_carp DROP CONSTRAINT fk_cargaarchivoservidor;

update propiedadvalordefinido_pvdp
set cpvd_uso_relaciones = 'Campo para filtrar por el motivo que se coloque'
where cpvd_llave in ('PROP_19');

