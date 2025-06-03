COMMENT ON TABLE usuario_usrp IS '2025-05-29';

ALTER TABLE public.productoinventario_pinp DROP CONSTRAINT fk_productoinventariobodega;

ALTER TABLE public.deduccionproducto_dprp DROP CONSTRAINT fk_deduccionproductobodega;

ALTER TABLE public.trazabilidadproductoinventario_tpip DROP CONSTRAINT fk_trazabilidadproductoinventariobodega;

update productoinventario_pinp
set cpin_bodega = (select bb.cbod_documento from bodega_bodp bb where bb.cbod_llave = cpin_bodega);

update deduccionproducto_dprp
set cdpr_bodega = (select bb.cbod_documento from bodega_bodp bb where bb.cbod_llave = cdpr_bodega);

update trazabilidadproductoinventario_tpip
set ctpi_bodega = (select bb.cbod_documento from bodega_bodp bb where bb.cbod_llave = ctpi_bodega);

ALTER TABLE bodega_bodp RENAME TO bodega_bodp_old;

update propiedadvalordefinido_pvdp pp 
set cpvd_estado = 'I'
where cpvd_llave in ('PROP_05','PROP_145');

INSERT INTO cambio_cmbp(ccmb_llave,  ccmb_nombre,  ccmb_motivo,  dcmb_fecha)
	VALUES('SC_20250529',  'SC_20250529',  'Retirara propiedades de homologacionde bodega',  now());

update propiedad_ppdp pp  
set cppd_cambioeliminacion = 'SC_20250529', cppd_estado = 'I'
where cppd_propiedadvalor  in ('PROP_05','PROP_145') and cppd_estado = 'A';
	