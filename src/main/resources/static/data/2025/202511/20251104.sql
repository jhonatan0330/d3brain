COMMENT ON TABLE usuario_usrp IS '2025-11-04';

ALTER TABLE public.relacioninterna_ritp DROP CONSTRAINT if exists fk_relacioninternacambioeliminacion;
ALTER TABLE public.relacioninterna_ritp DROP CONSTRAINT if exists fk_relacioninternacambiocreacion;

ALTER TABLE public.relacioninterna_ritp ADD crit_usuariocreacion varchar(32);
ALTER TABLE public.relacioninterna_ritp ADD crit_usuarioeliminacion varchar(32);
ALTER TABLE public.relacioninterna_ritp ADD drit_fechaeliminacion timestamptz;

ALTER TABLE public.relacioninterna_ritp ALTER COLUMN crit_cambiocreacion DROP NOT NULL;

ALTER TABLE public.propiedad_ppdp DROP CONSTRAINT if exists fk_propiedadcambioeliminacion;
ALTER TABLE public.propiedad_ppdp DROP CONSTRAINT if exists fk_propiedadcambiocreacion;

ALTER TABLE public.propiedad_ppdp ADD cppd_usuariocreacion varchar(32);
ALTER TABLE public.propiedad_ppdp ADD cppd_usuarioeliminacion varchar(32);
ALTER TABLE public.propiedad_ppdp ADD dppd_fechaeliminacion timestamptz;

ALTER TABLE public.propiedad_ppdp ALTER COLUMN cppd_cambiocreacion DROP NOT NULL;

update propiedad_ppdp 
set cppd_usuariocreacion = (select uu2.cuss_usuario from cambio_cmbp cc
								inner join usuariosesion_ussp uu2 on uu2.cuss_llave = cc.ccmb_sesionactiva 
								where cc.ccmb_llave = cppd_cambiocreacion );

update propiedad_ppdp 
set cppd_usuarioeliminacion = (select uu2.cuss_usuario from cambio_cmbp cc
								inner join usuariosesion_ussp uu2 on uu2.cuss_llave = cc.ccmb_sesionactiva 
								where cc.ccmb_llave = cppd_cambioeliminacion);

update propiedad_ppdp 
set dppd_fechaeliminacion = (select cc.dcmb_fecha from cambio_cmbp cc
								where cc.ccmb_llave = cppd_cambioeliminacion);

update relacioninterna_ritp rr  
set crit_usuariocreacion = (select uu2.cuss_usuario from cambio_cmbp cc
								inner join usuariosesion_ussp uu2 on uu2.cuss_llave = cc.ccmb_sesionactiva 
								where cc.ccmb_llave = crit_cambiocreacion );

update relacioninterna_ritp rr  
set crit_usuarioeliminacion = (select uu2.cuss_usuario from cambio_cmbp cc
								inner join usuariosesion_ussp uu2 on uu2.cuss_llave = cc.ccmb_sesionactiva 
								where cc.ccmb_llave = crit_cambioeliminacion);

update relacioninterna_ritp rr  
set drit_fechaeliminacion = (select cc.dcmb_fecha from cambio_cmbp cc
								where cc.ccmb_llave = crit_cambioeliminacion);

ALTER TABLE public.cambio_cmbp RENAME TO cambio_cmbp_old;
