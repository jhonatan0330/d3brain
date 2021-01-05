select count(*) from pedidoventacaracteristica_pvcp where cpvc_plantilla is null
--Step 1
--Elimino los contrains e index
ALTER TABLE pedidoventacaracteristica_pvcp alter column cpvc_plantilla set not null;
ALTER TABLE documentorelacionexpediente_dexp DROP CONSTRAINT fk_documentorelacionexpedientecampomaestro;
ALTER TABLE pedidoventacaracteristica_pvcp DROP CONSTRAINT fk_pedidoventacaracteristicadocumento;
ALTER TABLE pedidoventacaracteristica_pvcp DROP CONSTRAINT fk_pedidoventacaracteristicatransaccionregistro;
ALTER TABLE pedidoventacaracteristica_pvcp DROP CONSTRAINT fk_pedidoventacaracteristicacampo;
--ALTER TABLE pedidoventacaracteristica_pvcp DROP CONSTRAINT pk_pedidoventacaracteristica_pvcp;--No se si borrarlo
drop index ix_pedidoventacaracteristica_documento;--Los dejo para volver a generarlos con nombre bien
drop index ix_pedidoventacaracteristica_valoropcion;--Los dejo para volver a generarlos con nombre bien

--Step 2
--Create the partitioned table

CREATE TABLE data_pvcp (
	cpvc_llave varchar(32) NOT NULL,
	cpvc_documento varchar(32) NOT NULL,
	cpvc_campo varchar(32) NOT NULL,
	cpvc_valortext varchar(4000) NOT NULL,
	dpvc_valorfecha timestamptz NULL,
	cpvc_valoropcion varchar(32) NULL,
	cpvc_valorauxiliar varchar(32) NULL,
	mpvc_valornumero numeric(24,6) NOT NULL DEFAULT 0,
	cpvc_transaccionregistro varchar(32) NOT NULL,
	cpvc_transaccioninactivo varchar(32) NULL,
	cpvc_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	cpvc_plantilla varchar(32) NOT NULL
)
PARTITION BY LIST(cpvc_plantilla);

--Step 3
--Cambio de nombre las tablas y agrego la actual como una particion

ALTER TABLE pedidoventacaracteristica_pvcp RENAME TO t_pvc_main;--t- es para que traiga los backups
ALTER TABLE data_pvcp ATTACH PARTITION t_pvc_main DEFAULT;
ALTER TABLE data_pvcp RENAME TO pedidoventacaracteristica_pvcp;
--Step 4
--Creo de nuevo los indices
ALTER TABLE pedidoventacaracteristica_pvcp ADD PRIMARY KEY (cpvc_llave, cpvc_plantilla);--Sera necesario crear este
--CREATE UNIQUE INDEX pk_z_main ON z_main USING btree (cpvc_llave);
CREATE INDEX ix_t_pvc_main_valoropcion ON t_pvc_main USING btree (cpvc_valoropcion);
CREATE INDEX ix_t_pvc_main_documento ON t_pvc_main USING btree (cpvc_documento);

--CREATE UNIQUE INDEX uk_pedidoventacaracteristica_llave ON pedidoventacaracteristica_pvcp(cpvc_llave);
--ALTER TABLE documentorelacionexpediente_dexp ADD CONSTRAINT fk_documentorelacionexpedientecampomaestro FOREIGN KEY (cdex_campomaestro) REFERENCES pedidoventacaracteristica_pvcp (cpvc_llave);
--ALTER TABLE pedidoventacaracteristica_pvcp ADD CONSTRAINT fk_pedidoventacaracteristicadocumento FOREIGN KEY (cpvc_documento) REFERENCES pedidoventa_pdvp(cpdv_llave);
--ALTER TABLE pedidoventacaracteristica_pvcp ADD CONSTRAINT fk_pedidoventacaracteristicatransaccionregistro FOREIGN KEY (cpvc_transaccionregistro) REFERENCES documentotransaccion_trap(ctra_llave);
--ALTER TABLE pedidoventacaracteristica_pvcp ADD CONSTRAINT fk_pedidoventacaracteristicacampo FOREIGN KEY (cpvc_campo) REFERENCES documentoplantillacaracteristica_dpcp(cdpc_llave);

CREATE OR REPLACE VIEW campo_documento
AS SELECT pvc.cpvc_llave,
    pvc.dpvc_valorfecha,
    pvc.mpvc_valornumero,
    pvc.cpvc_valortext,
    pvc.cpvc_valoropcion,
    pvc.cpvc_valorauxiliar,
    pvc.cpvc_campo,
    dpc.cdpc_codigo AS cdpf_codigo,
    dpc.cdpc_nombre AS cdpf_nombre,
    pvc.cpvc_documento AS cdrc_documento,
    pvc.cpvc_plantilla AS cpvc_plantilla
   FROM pedidoventacaracteristica_pvcp pvc,
    documentoplantillacaracteristica_dpcp dpc
  WHERE pvc.cpvc_campo::text = dpc.cdpc_llave::text AND pvc.cpvc_estado::text = 'A'::text and pvc.cpvc_plantilla =dpc.cdpc_plantilla ;