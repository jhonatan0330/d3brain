COMMENT ON TABLE usuario_usrp IS '2026-11-02';

update propiedadvalordefinido_pvdp
set cpvd_uso_motivo = 'CODIGO DEL GRUPO EN EL QUERY'
where cpvd_llave in ('PROP_273');


update propiedadvalordefinido_pvdp
set cpvd_uso_relaciones = 'Campo que contiene el proceso que mostrara la ubicación'
where cpvd_llave in ('PROP_133');