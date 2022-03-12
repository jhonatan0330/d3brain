COMMENT ON TABLE usuario_usrp IS '2019-12-07';
COMMENT ON TABLE usuariosesion_ussp IS '2019.12.07.00';

ALTER TABLE documentoplantillapermiso_dppp
	DROP COLUMN cdpp_valordefecto,
	DROP COLUMN cdpp_codigodepende;

ALTER TABLE proceso_prcp
	ADD COLUMN cprc_tipo character varying(1);

update proceso_prcp set cprc_tipo = 'E';

ALTER TABLE proceso_prcp
	ALTER COLUMN cprc_tipo SET NOT NULL;

ALTER TABLE propiedad_ppdp
	ADD COLUMN cppd_tipo character varying(1);

update propiedad_ppdp set cppd_tipo = (select cpvd_origen from propiedadvalordefinido_pvdp where cpvd_llave = cppd_propiedadvalor);

ALTER TABLE propiedad_ppdp
	ALTER COLUMN cppd_tipo SET NOT NULL;
	
ALTER TABLE propiedadvalordefinido_pvdp
	ADD COLUMN bpvd_multiple boolean DEFAULT false NOT NULL;
	
update propiedadvalordefinido_pvdp set bpvd_multiple = true where cpvd_llave = 'PROP_96';

ALTER TABLE propiedadvalordefinido_pvdp
	ADD COLUMN bpvd_propiedadboolean boolean DEFAULT false NOT NULL;

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, cpvd_origencategoria, cpvd_motivo, bpvd_propiedadboolean) 
	VALUES('PROP_97' , 'C', 'FECHA_CON_HORA', 'FECHA_CON_HORA', 'www.softwareparati.com', 'REQUISITO', 'F', 'Incluir al campo la hora', true);
	
INSERT INTO cambio_cmbp (ccmb_llave, ccmb_nombre, ccmb_motivo, dcmb_fecha, dcmb_fechaaplicacion)
	VALUES('SC-20191207', 'SC-SIS-20191207', 'Cambiar la propiedad de fecha para que solicite la hora', now(), now());

INSERT INTO propiedad_ppdp (cppd_llave, cppd_propiedadvalor, cppd_campo, cppd_valor, cppd_motivo,
	 dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_tipo) 
select substring('HORA' || campo.cdpc_llave, 0, 32), 'PROP_97', campo.cdpc_llave, '1',
	'Permite filtrar los documentos por este campo', now(), now(), 'SC-20191207', 'C' 
from documentoplantillacaracteristica_dpcp campo where cdpc_formato = 'F' 
	and cdpc_llave not in (select cppd_campo from propiedad_ppdp where cppd_propiedadvalor = 'PROP_25')
	and cdpc_llave not in (select cppd_campo from propiedad_ppdp where cppd_propiedadvalor = 'PROP_23')
	and cdpc_llave not in (select cppd_campo from propiedad_ppdp where cppd_propiedadvalor = 'PROP_24');

delete from propiedad_ppdp where cppd_propiedadvalor = 'PROP_21';
delete from propiedadvalordefinido_pvdp where cpvd_llave = 'PROP_21';

update propiedadvalordefinido_pvdp set bpvd_propiedadboolean = true where cpvd_llave = 'PROP_94';