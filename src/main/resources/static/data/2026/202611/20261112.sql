COMMENT ON TABLE usuario_usrp IS '2026-11-12';

update propiedadvalordefinido_pvdp
set cpvd_uso_relaciones = 'Campo donde se va a cambiar el valor'
where cpvd_llave in ('PROP_121');

update propiedadvalordefinido_pvdp
set cpvd_uso_relaciones = 'Campo donde se obtiene el valor'
where cpvd_llave in ('PROP_151');