COMMENT ON TABLE usuario_usrp IS '2026-10-26';

update propiedad_ppdp pp
set cppd_valor = '.'
where pp.cppd_propiedadvalor in (
select pp2.cpvd_llave  from propiedadvalordefinido_pvdp pp2 where pp2.bpvd_propiedadboolean =true
);

update documentoplantillacaracteristica_dpcp
set cdpc_imagen = null;

update propiedadvalordefinido_pvdp
set cpvd_uso_relaciones = 'CAMPO DE LA PLANTILLA ORIGEN DONDE SE TOMA EL DATO'
where cpvd_llave in ('PROP_225');