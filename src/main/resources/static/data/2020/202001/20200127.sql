COMMENT ON TABLE usuario_usrp IS '2020-01-27';
COMMENT ON TABLE usuariosesion_ussp IS '2020.01.27.00';

ALTER TABLE productocaracteristica_pcrp
	DROP CONSTRAINT fk_productocaracteristicadocumentoauxiliar;

ALTER TABLE productocaracteristica_pcrp
	ADD COLUMN cpcr_objetivo character varying(4000),
	ADD COLUMN cpcr_imagen character varying(2000);

update productocaracteristica_pcrp set cpcr_objetivo = 'PENDIENTE';

ALTER TABLE productocaracteristica_pcrp
	ALTER COLUMN cpcr_objetivo SET NOT NULL;

INSERT INTO cambio_cmbp (ccmb_llave, ccmb_nombre, ccmb_motivo, dcmb_fecha, dcmb_fechaaplicacion)
	VALUES('SC_20200127', 'SC_20200127', 'Cambiar las estructura de los campos de productos ', now(), now());

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, cpvd_origencategoria, cpvd_motivo) 
	VALUES('PROP_110' , 'C', 'DETALLE_TARIFARIO_PRODUCTO', 'DETALLE_TARIFARIO_PRODUCTO', 'www.softwareparati.com', 'REQUISITO', 'N', 'Se va a calcular el valor teniendo en cuenta el tarifario indicado');
	
INSERT INTO propiedad_ppdp (cppd_llave, cppd_campo, cppd_valor, cppd_texto, cppd_propiedadvalor, dppd_fechadefinicion, cppd_motivo, cppd_cambiocreacion, cppd_tipo, cppd_codigo) 
	select substring('PCTP'|| cpcr_llave, 0, 32), cpcr_llave, cpcr_tipoparametro, 
		(select ctrf_nombre from tarifario_trfp where ctrf_llave = cpcr_tipoparametro), 'PROP_110', now(), 'Tarifario', 
		'SC_20200127', 'C', '75'
	from productocaracteristica_pcrp where cpcr_tipoparametro is not null;

ALTER TABLE productocaracteristica_pcrp DROP COLUMN cpcr_tipoparametro;

INSERT INTO propiedad_ppdp (cppd_llave, cppd_campo, cppd_valor, cppd_propiedadvalor, dppd_fechadefinicion, cppd_motivo, 
	cppd_cambiocreacion, cppd_tipo, cppd_codigo) 
	select substring('PCOB'|| cpcr_llave, 0, 32), cpcr_llave, '1', 
		'PROP_103', now(), 'Campo obligatorio', 'SC_20200127', 'C', '75'
	from productocaracteristica_pcrp where bpcr_obligatorio = true;

ALTER TABLE productocaracteristica_pcrp DROP COLUMN bpcr_obligatorio;

INSERT INTO propiedad_ppdp (cppd_llave, cppd_campo, cppd_valor, cppd_texto, cppd_propiedadvalor, dppd_fechadefinicion, cppd_motivo, cppd_cambiocreacion, cppd_tipo, cppd_codigo) 
	select substring('PCDA'|| cpcr_llave, 0, 32), cpcr_llave, cpcr_documentoauxiliar, 
		(select cdpl_nombre from documentoplantilla_dplp where cdpl_llave = cpcr_documentoauxiliar), 'PROP_19', now(), 'Permite escoger entre las activas', 
		'SC_20200127', 'C', '75'
	from productocaracteristica_pcrp where cpcr_documentoauxiliar is not null;

ALTER TABLE productocaracteristica_pcrp DROP COLUMN cpcr_documentoauxiliar;

INSERT INTO propiedad_ppdp (cppd_llave, cppd_campo, cppd_valor, cppd_propiedadvalor, dppd_fechadefinicion, cppd_motivo, cppd_cambiocreacion, cppd_tipo, cppd_codigo) 
	select substring('PCAL'|| cpcr_llave, 0, 32), cpcr_llave, '1', 
		'PROP_20', now(), 'Campo con opciones cargados automaticamente', 'SC_20200127', 'C', '75'
	from productocaracteristica_pcrp where bpcr_autoload = true;

ALTER TABLE productocaracteristica_pcrp DROP COLUMN bpcr_autoload;

INSERT INTO propiedad_ppdp (cppd_llave, cppd_campo, cppd_valor, cppd_propiedadvalor, dppd_fechadefinicion, cppd_motivo, cppd_cambiocreacion, cppd_tipo, cppd_codigo) 
	select substring('PCVR'|| cpcr_llave, 0, 32), cpcr_llave, '1', 
		'PROP_104', now(), 'Campo visible en el render', 'SC_20200127', 'C', '75'
	from productocaracteristica_pcrp where bpcr_visiblerender = true;

ALTER TABLE productocaracteristica_pcrp DROP COLUMN bpcr_visiblerender;
