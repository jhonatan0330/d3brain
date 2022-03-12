COMMENT ON TABLE usuario_usrp IS '2019-05-02';

COMMENT ON TABLE usuariosesion_ussp IS '2019.05.02.00';

ALTER TABLE procesodecision_pdcp
	ADD COLUMN cpdc_cuerpo character varying(4000);

ALTER TABLE producto_prop
	ADD COLUMN cpro_productobase character varying(32);

update pedidoventacaracteristica_pvcp set cpvc_valortext= 'SI' where mpvc_valornumero = 1 and cpvc_valortext = 'NO' and cpvc_campo in (
	select cdpc_llave from documentoplantillacaracteristica_dpcp  order by cdpc_formato  = 'I');