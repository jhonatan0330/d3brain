
COMMENT ON TABLE usuario_usrp IS '2018-08-16';
COMMENT ON TABLE usuariosesion_ussp IS '2018.08.16.00';

ALTER TABLE documentoplantillarol_dprp
	ADD COLUMN cdpr_campofiltro character varying(32);

update documentoplantillarol_dprp set cdpr_campofiltro = (select cdpc_llave from documentoplantillacaracteristica_dpcp where cdpc_codigodepende  = 'USR' and cdpc_plantilla = cdpr_plantilla);
update documentoplantillarol_dprp set cdpr_campofiltro  = null where cdpr_rol in (select crac_llave from rolacceso_racp  where brac_permisoscompletos = true);
update documentoplantillarol_dprp set bdpr_vertodos = false where cdpr_campofiltro is not null;
update documentoplantillacaracteristica_dpcp set cdpc_codigodepende = null where cdpc_codigodepende  = 'USR';

/*
	
select * 
,(select cdpc_llave from documentoplantillacaracteristica_dpcp campo where campo.cdpc_codigodepende  = 'USR' and campo.cdpc_plantilla = cdpr_plantilla)
from documentoplantillarol_dprp

select * from documentoplantillacaracteristica_dpcp, documentoplantilla_dplp 
where cdpl_llave = cdpc_plantilla and cdpc_estado = 'A' and cdpl_estado = 'A' and cdpl_tipo = 'R'
order by cdpc_plantilla, ndpc_orden

*/