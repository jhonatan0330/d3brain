COMMENT ON TABLE usuario_usrp IS '2020-09-07';
COMMENT ON TABLE usuariosesion_ussp IS '2020.09.07.00';

ALTER TABLE categoriaproducto_cprp
	DROP COLUMN ccpr_descripcion;
ALTER TABLE cuenta_cuep
	DROP COLUMN mcue_sobregiro;

update propiedadvalordefinido_pvdp set cpvd_origencategoria = 'Z' 
	where cpvd_llave in ('PROP_05', 'PROP_06');

update propiedad_ppdp set cppd_tipo = 'C' 
	where cppd_propiedadvalor in ('PROP_05', 'PROP_06');

--Cada campo tiene valor auxiliar el id d la bodega
update pedidoventacaracteristica_pvcp 
set cpvc_valorauxiliar = cpvc_valoropcion
where cpvc_campo in (select cdpc_llave from documentoplantillacaracteristica_dpcp where cdpc_formato = 'D');

--Cambio valor opcion x el id del documento
update pedidoventacaracteristica_pvcp 
set cpvc_valoropcion = (select cbod_documento from bodega_bodp where cbod_llave = cpvc_valoropcion)
where cpvc_campo in (select cdpc_llave from documentoplantillacaracteristica_dpcp where cdpc_formato = 'D');

INSERT INTO cambio_cmbp(ccmb_llave,  ccmb_nombre,  ccmb_motivo,  dcmb_fecha)
	VALUES('SC_20200907',  'SC_20200907',  'Retirar el campo Tipo Bodega',  now());

--Crearles una fuente de datos a la plantilla de bodega
INSERT INTO propiedad_ppdp (cppd_llave, cppd_campo, cppd_valor, cppd_propiedadvalor, 
	dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_tipo)
select 
	replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''),
	cdpc_llave, (select distinct(cpdv_plantilla) 
		from bodega_bodp, pedidoventa_pdvp where cbod_estado = 'A' and cbod_documento = cpdv_llave
		limit 1)
	, 'PROP_19', now(), now(), 'SC_20200907', 'C'
from documentoplantillacaracteristica_dpcp 
where cdpc_formato = 'D';

--Colocarles una propiedad autolaoda para que coja de una vez
INSERT INTO propiedad_ppdp (cppd_llave, cppd_campo, cppd_valor, cppd_propiedadvalor, 
	dppd_fechadefinicion, dppd_fechaimplementacion, cppd_cambiocreacion, cppd_tipo)
select 
	replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, '-',''),
	cdpc_llave, '1'
	, 'PROP_20', now(), now(), 'SC_20200907', 'C'
from documentoplantillacaracteristica_dpcp 
where cdpc_formato = 'D';

--cambiarles el formato a Z
update documentoplantillacaracteristica_dpcp set cdpc_formato = 'Z'
where cdpc_formato = 'D';
--

ALTER TABLE categoriaproducto_cprp
	ALTER COLUMN mcpr_cantidadmaxima TYPE numeric(18,6) /* TYPE change - table: categoriaproducto_cprp original: numeric(16,2) new: numeric(18,6) */;

ALTER TABLE consecutivo_conp
	ALTER COLUMN mcon_numeroinicial TYPE numeric(18,6) /* TYPE change - table: consecutivo_conp original: numeric(16,2) new: numeric(18,6) */,
	ALTER COLUMN mcon_numerofinal TYPE numeric(18,6) /* TYPE change - table: consecutivo_conp original: numeric(16,2) new: numeric(18,6) */,
	ALTER COLUMN mcon_numeroactual TYPE numeric(18,6) /* TYPE change - table: consecutivo_conp original: numeric(16,2) new: numeric(18,6) */;

ALTER TABLE detallecaracteristicaproducto_dcpp
	ALTER COLUMN mdcp_valornumero TYPE numeric(18,6) /* TYPE change - table: detallecaracteristicaproducto_dcpp original: numeric(16,2) new: numeric(18,6) */;

ALTER TABLE detallepedidoventa_dpvp
	ALTER COLUMN mdpv_cantidad TYPE numeric(18,6) /* TYPE change - table: detallepedidoventa_dpvp original: numeric(16,2) new: numeric(18,6) */,
	ALTER COLUMN mdpv_cantidadtotal TYPE numeric(18,6) /* TYPE change - table: detallepedidoventa_dpvp original: numeric(16,2) new: numeric(18,6) */;

ALTER TABLE productoinventario_pinp
	ALTER COLUMN mpin_cantidadactual TYPE numeric(18,6) /* TYPE change - table: productoinventario_pinp original: numeric(16,2) new: numeric(18,6) */,
	ALTER COLUMN mpin_cantidadminima TYPE numeric(18,6) /* TYPE change - table: productoinventario_pinp original: numeric(16,2) new: numeric(18,6) */,
	ALTER COLUMN mpin_cantidadmaxima TYPE numeric(18,6) /* TYPE change - table: productoinventario_pinp original: numeric(16,2) new: numeric(18,6) */;

ALTER TABLE trazabilidadproductoinventario_tpip
	ALTER COLUMN mtpi_cantidadinicial TYPE numeric(18,6) /* TYPE change - table: trazabilidadproductoinventario_tpip original: numeric(16,2) new: numeric(18,6) */,
	ALTER COLUMN mtpi_cantidadfinal TYPE numeric(18,6) /* TYPE change - table: trazabilidadproductoinventario_tpip original: numeric(16,2) new: numeric(18,6) */,
	ALTER COLUMN mtpi_cantidad TYPE numeric(18,6) /* TYPE change - table: trazabilidadproductoinventario_tpip original: numeric(16,2) new: numeric(18,6) */;

