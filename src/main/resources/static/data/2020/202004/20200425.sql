COMMENT ON TABLE usuario_usrp IS '2020-04-25';
COMMENT ON TABLE usuariosesion_ussp IS '2020.04.25.00';

ALTER TABLE documentoplantilla_dplp
	DROP COLUMN cdpl_color;

CREATE index IF NOT EXISTS ix_pedidoventacaracteristica_valoropcion ON pedidoventacaracteristica_pvcp USING btree (cpvc_valoropcion);

CREATE index IF NOT EXISTS ix_documentorelaciongestor_documentoprincipal ON documentorelaciongestor_drgp USING btree (cdrg_documentoprincipal);

CREATE index IF NOT EXISTS ix_pedidoventa_nombre ON pedidoventa_pdvp USING btree (cpdv_nombre);

CREATE index IF NOT EXISTS ix_pedidoventa_plantilla ON pedidoventa_pdvp USING btree (cpdv_plantilla);

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, cpvd_motivo, bpvd_propiedadboolean) 
	VALUES('PROP_134' , 'C', 'OPCIONAL', 'PERMISO_CAMPO_OPCIONAL', 'www.softwareparati.com', 'PERMISOS', 'Este campo no es obligatorio registrarlo para llenar el formulario', TRUE);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, cpvd_motivo, bpvd_propiedadboolean) 
	VALUES('PROP_135' , 'C', 'BLOQUEAR', 'PERMISO_CAMPO_BLOQUEAR', 'www.softwareparati.com', 'PERMISOS', 'Este campo esta bloqueado', true);

insert into cambio_cmbp (ccmb_llave,ccmb_nombre,ccmb_motivo,dcmb_fecha) values ('SC_20200425','SC_20200425','Definir los campos opcionales',now());

INSERT INTO propiedad_ppdp(cppd_llave, cppd_campo, cppd_valor, cppd_propiedadvalor, dppd_fechadefinicion, cppd_motivo, cppd_cambiocreacion, cppd_tipo)
select 
'134' || substring(cdpc_llave,4,32), cdpc_llave, '1', 'PROP_134', now(), 'Este campo no es obligatorio registrarlo para llenar el formulario', 'SC_20200425', 'C'
from documentoplantillacaracteristica_dpcp where cdpc_estado = 'A' 
and cdpc_llave not in (select cppd_campo from propiedad_ppdp where cppd_propiedadvalor = 'PROP_103' and cppd_estado = 'A');

delete from propiedad_ppdp where cppd_propiedadvalor = 'PROP_103';

delete from propiedadvalordefinido_pvdp where cpvd_llave = 'PROP_103';

INSERT INTO propiedad_ppdp(cppd_llave, cppd_campo, cppd_valor, cppd_propiedadvalor, dppd_fechadefinicion, cppd_motivo, cppd_cambiocreacion, cppd_tipo)
select 
 replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''), cdpc_llave, '1', 'PROP_135', now(), 'Este campo esta bloqueado', 'SC_20200425', 'C'
from documentoplantillacaracteristica_dpcp where cdpc_estado = 'A' 
and cdpc_llave not in (select cppd_campo from propiedad_ppdp where cppd_propiedadvalor = 'PROP_106' and cppd_estado = 'A' and cppd_rol is null);

delete from propiedad_ppdp where cppd_propiedadvalor = 'PROP_106';

delete from propiedadvalordefinido_pvdp where cpvd_llave = 'PROP_106';