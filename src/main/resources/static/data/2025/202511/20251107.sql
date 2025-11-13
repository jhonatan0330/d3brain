COMMENT ON TABLE usuario_usrp IS '2025-11-07';

ALTER TABLE public.propiedad_ppdp ADD nppd_relaciones int4 DEFAULT 0 NOT NULL;

update propiedad_ppdp 
set nppd_relaciones = (select count(*) from relacioninterna_ritp rr where rr.crit_propiedad = cppd_llave and rr.crit_estado = 'A')
where cppd_estado = 'A';

update relacioninterna_ritp rr 
set crit_estado = 'I'
where rr.crit_propiedad in (
	select pp.cppd_llave from propiedad_ppdp pp 
	where pp.cppd_estado = 'A' and pp.cppd_propiedadvalor = 'PROP_19'
	and pp.nppd_relaciones != 0
);