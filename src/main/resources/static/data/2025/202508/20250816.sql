 COMMENT ON TABLE usuario_usrp IS '2025-08-16';

ALTER TABLE historic.z_dex_documentorelacionexpediente ADD cdex_documentoregistro varchar(32);
ALTER TABLE historic.z_dex_documentorelacionexpediente ADD cdex_documentoinactivo varchar(32);

ALTER TABLE historic.z_dex_documentorelacionexpediente ALTER COLUMN cdex_transaccionregistro DROP NOT NULL;

CREATE TABLE historic.z_pvu_pedidoventaubicacion (
	cpvu_llave varchar(32) NOT NULL,
	cpvu_documento varchar(32) NOT NULL,
	dpvu_fecha timestamptz NOT NULL,
	cpvu_ubicacion varchar(32) NOT NULL,
	cpvu_modificador varchar(32) NOT NULL,
	cpvu_estado varchar(1) DEFAULT 'A'::character varying NOT NULL,
	CONSTRAINT pk_z_pvu_pedidoventaubicacion PRIMARY KEY (cpvu_llave)
);

ALTER TABLE historic.z_pvd_pedidoventadinero ADD cpvd_modificador varchar(32);

ALTER TABLE historic.z_dpv_detallepedidoventa ADD cdpv_detalleid varchar(32) ;