
COMMENT ON TABLE usuario_usrp IS '2018-08-06';
COMMENT ON TABLE usuariosesion_ussp IS '2018.08.06.00';

DROP FUNCTION copiar_plantilla(nombre_plantilla_actual character, nombre_plantilla_nueva character);

INSERT INTO usuario_usrp(cusr_llave, cusr_identificacion, cusr_nombre, cusr_imagen) VALUES ('SYSTEM', 'SYSTEM', 'SISTEMA', 'http://colombiansofture.com/imagenes/avatar.png');

ALTER TABLE pedidoventacaracteristica_pvcp
	ADD CONSTRAINT fk_pedidoventacaracteristicausuarioinactivo FOREIGN KEY (cpvc_usuarioinactivo) REFERENCES usuario_usrp(cusr_llave);

ALTER TABLE pedidoventacaracteristica_pvcp
	ADD CONSTRAINT fk_pedidoventacaracteristicausuarioregistro FOREIGN KEY (cpvc_usuarioregistro) REFERENCES usuario_usrp(cusr_llave);

INSERT INTO plantillapropiedad_pprp (cppr_llave, cppr_plantilla, cppr_key, cppr_valor, cppr_texto)
select substring('SUB' || cppr_llave, 0, 32), cppr_plantilla, 'SUBTOTAL', cppr_valorsubtotal, (select cdpc_nombre 
	from documentoplantillacaracteristica_dpcp  where cdpc_llave =  cppr_valorsubtotal) from plantillaparametro_pprp where cppr_valorsubtotal is not null;
INSERT INTO plantillapropiedad_pprp (cppr_llave, cppr_plantilla, cppr_key, cppr_valor, cppr_texto)
select substring('TOT' || cppr_llave, 0, 32), cppr_plantilla, 'TOTAL', cppr_valortotal, (select cdpc_nombre 
	from documentoplantillacaracteristica_dpcp  where cdpc_llave =  cppr_valortotal) from plantillaparametro_pprp where cppr_valortotal is not null;
INSERT INTO plantillapropiedad_pprp (cppr_llave, cppr_plantilla, cppr_key, cppr_valor, cppr_texto)
select substring('DESC' || cppr_llave, 0, 32), cppr_plantilla, 'DESCRIPCION', cppr_descripcion, (select cdpc_nombre 
	from documentoplantillacaracteristica_dpcp  where cdpc_llave =  cppr_descripcion) from plantillaparametro_pprp where cppr_descripcion is not null;
INSERT INTO plantillapropiedad_pprp (cppr_llave, cppr_plantilla, cppr_key, cppr_valor, cppr_texto)
select substring('CONSE' || cppr_llave, 0, 32), cppr_plantilla, 'CONSECUTIVO', cppr_consecutivoescrito, (select cdpc_nombre 
	from documentoplantillacaracteristica_dpcp  where cdpc_llave =  cppr_consecutivoescrito) from plantillaparametro_pprp where cppr_consecutivoescrito is not null;
INSERT INTO plantillapropiedad_pprp (cppr_llave, cppr_plantilla, cppr_key, cppr_valor, cppr_texto)
select substring('FECHA' || cppr_llave, 0, 32), cppr_plantilla, 'FECHA', cppr_fecha, (select cdpc_nombre 
	from documentoplantillacaracteristica_dpcp  where cdpc_llave =  cppr_fecha) from plantillaparametro_pprp where cppr_fecha is not null;
INSERT INTO plantillapropiedad_pprp (cppr_llave, cppr_plantilla, cppr_key, cppr_valor, cppr_texto)
select substring('ORD' || cppr_llave, 0, 32), cppr_plantilla, 'ORDEN', 'N', 'NOMBRE' from plantillaparametro_pprp where bppr_ordenadoxnombre = true;

DROP TABLE documentoplantillainventario_dpip;
DROP TABLE plantillaparametro_pprp;

CREATE TABLE pedidoventaresponsable_pvrp (
	cpvr_llave character varying(32) NOT NULL,
	cpvr_documento character varying(32) NOT NULL,
	cpvr_responsable character varying(32) NOT NULL,
	dpvr_fecharegistro timestamp with time zone NOT NULL,
	cpvr_usuarioregistro character varying(32) NOT NULL,
	dpvr_fechainactivo timestamp with time zone,
	cpvr_usuarioinactivo character varying(32),
	cpvr_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

ALTER TABLE pedidoventaresponsable_pvrp
	ADD CONSTRAINT pk_pedidoventaresponsable_pvrp PRIMARY KEY (cpvr_llave);

ALTER TABLE documentorelaciondetalle_drdp
	ADD CONSTRAINT fk_documentorelaciondetalleusuarioinactivo FOREIGN KEY (cdrd_usuarioinactivo) REFERENCES public.usuario_usrp(cusr_llave);

ALTER TABLE documentorelaciondetalle_drdp
	ADD CONSTRAINT fk_documentorelaciondetalleusuarioregistro FOREIGN KEY (cdrd_usuarioregistro) REFERENCES public.usuario_usrp(cusr_llave);

ALTER TABLE pedidoventaresponsable_pvrp
	ADD CONSTRAINT fk_pedidoventaresponsabledocumento FOREIGN KEY (cpvr_documento) REFERENCES public.pedidoventa_pdvp(cpdv_llave);

ALTER TABLE pedidoventaresponsable_pvrp
	ADD CONSTRAINT fk_pedidoventaresponsableusuarioinactivo FOREIGN KEY (cpvr_usuarioinactivo) REFERENCES public.usuario_usrp(cusr_llave);
