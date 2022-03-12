COMMENT ON TABLE usuario_usrp IS '2020-02-14';

delete from propiedad_ppdp where cppd_propiedadvalor = 'PROP_21';
delete from propiedadvalordefinido_pvdp where cpvd_llave = 'PROP_21';

update usuario_usrp set cusr_imagen = 'http://colombiansofture.com/imagenes/avatar.png' where cusr_imagen is null;

ALTER TABLE mensaje_msjp
	ALTER COLUMN cmsj_titulo TYPE character varying(200);

ALTER TABLE usuario_usrp
	ALTER COLUMN cusr_imagen SET NOT NULL;

update propiedadvalordefinido_pvdp 
set cpvd_nombre = 'CAMBIAR ESTADOS'
where cpvd_llave= 'PROP_85';

update propiedadvalordefinido_pvdp 
set cpvd_nombre = 'CARGAS MASIVAS', bpvd_propiedadboolean = true
where cpvd_llave= 'PROP_84';

update propiedadvalordefinido_pvdp 
set cpvd_nombre = 'ESTADOS POR DEFECTO CONSULTA' 
where cpvd_llave= 'PROP_83';

update propiedadvalordefinido_pvdp 
set cpvd_nombre = 'FILTRO POR CAMPO' 
where cpvd_llave= 'PROP_82';

update propiedadvalordefinido_pvdp 
set cpvd_nombre = 'OCULTAR TOTAL' 
where cpvd_llave= 'PROP_81';

update propiedadvalordefinido_pvdp 
set cpvd_nombre = 'INICIO RAPIDO' 
where cpvd_llave= 'PROP_80';

update propiedadvalordefinido_pvdp 
set cpvd_nombre = 'PERMISO ELIMINAR' 
where cpvd_llave= 'PROP_79';

update propiedadvalordefinido_pvdp 
set cpvd_nombre = 'PERMISO MODIFICAR' 
where cpvd_llave= 'PROP_78';

update propiedadvalordefinido_pvdp 
set cpvd_nombre = 'PERMISO CREAR' 
where cpvd_llave= 'PROP_77';

update propiedadvalordefinido_pvdp 
set cpvd_nombre = 'FUENTE DE DATOS'
where cpvd_llave= 'PROP_19';

update propiedadvalordefinido_pvdp 
set cpvd_nombre = 'RUTA BPM GESTION' , bpvd_multiple = true
where cpvd_llave= 'PROP_37';