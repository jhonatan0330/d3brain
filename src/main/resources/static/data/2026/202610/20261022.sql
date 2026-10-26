COMMENT ON TABLE usuario_usrp IS '2026-10-22';

update propiedadvalordefinido_pvdp
set cpvd_uso_relaciones = 'CAMPO A DONDE SE VA A RELACIONAR LOS PROCESOS SELECCIONADOS'
where cpvd_llave in ('PROP_152', 'PROP_153');

update propiedadvalordefinido_pvdp
set cpvd_uso_relaciones = 'CAMPO A DONDE SE GUARDA LA EXTRACCION'
where cpvd_llave in ('PROP_192', 'PROP_204', 'PROP_235');

update propiedadvalordefinido_pvdp
set cpvd_uso_relaciones = 'CAMINO PARA LLEGAR A LOS ITEMS'
where cpvd_llave in ('PROP_238');
