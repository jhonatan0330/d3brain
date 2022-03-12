
COMMENT ON TABLE usuario_usrp IS '2019-05-03';

COMMENT ON TABLE usuariosesion_ussp IS '2019.05.03.00';

ALTER TABLE reportebase_rpbp
	ADD COLUMN crpb_subreporte character varying(4000),
	ADD COLUMN crpb_subreporte2 character varying(4000),
	ADD COLUMN crpb_jasperexcel character varying(4000);

update pedidoventacaracteristica_pvcp set cpvc_valortext= 'SI' where mpvc_valornumero = 1 and cpvc_valortext = 'NO' and cpvc_campo in (
	select cdpc_llave from documentoplantillacaracteristica_dpcp  order by cdpc_formato  = 'I');