COMMENT ON TABLE usuario_usrp IS '2023-03-10';

ALTER TABLE detallepedidoventa_dpvp ADD cdpv_campo varchar(32);
ALTER TABLE detallepedidoventa_dpvp ADD cdpv_nombre varchar(200);

ALTER TABLE z_dpv_detallepedidoventa ADD cdpv_campo varchar(32);
ALTER TABLE z_dpv_detallepedidoventa ADD cdpv_nombre varchar(200);

update detallepedidoventa_dpvp set cdpv_nombre = (select descripcion(cpro_documento) from producto_prop where cpro_llave = cdpv_producto);

