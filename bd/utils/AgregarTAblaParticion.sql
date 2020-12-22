--Step 3
--We need to add the child tables as partitions to the partitioned table using ALTER TABLE ... ATTACH. To do that, first we need to remove child-tables from inheritance hierarchy using NO INHERIT clause. For example,

CREATE TABLE z_AFORO_CREDITO (
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
);

INSERT INTO z_AFORO_CREDITO (cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, dpvc_valorfecha, cpvc_valoropcion, cpvc_valorauxiliar, mpvc_valornumero, cpvc_transaccionregistro, cpvc_transaccioninactivo, cpvc_estado, cpvc_plantilla) 
select cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, dpvc_valorfecha, cpvc_valoropcion, cpvc_valorauxiliar, mpvc_valornumero, cpvc_transaccionregistro, cpvc_transaccioninactivo, cpvc_estado, cpvc_plantilla
 from pedidoventacaracteristica_pvcp where cpvc_plantilla = 'AFORO-CREDITO';

--delete from pedidoventacaracteristica_pvcp where cpvc_plantilla = 'AFORO-CREDITO';
delete from t_pvc_main where cpvc_plantilla = 'AFORO-CREDITO';

ALTER TABLE pedidoventacaracteristica_pvcp ATTACH PARTITION z_AFORO_CREDITO for values in ('AFORO-CREDITO');

CREATE INDEX ix_z_AFORO_CREDITO_documento ON z_AFORO_CREDITO USING btree (cpvc_documento);
CREATE INDEX ix_z_AFORO_CREDITO_valoropcion ON z_AFORO_CREDITO USING btree (cpvc_valoropcion);
CREATE UNIQUE INDEX pk_z_AFORO_CREDITO ON z_AFORO_CREDITO USING btree (cpvc_llave);--Toca crearlo porque se duplica el id

select count(*) from pedidoventacaracteristica_pvcp
where cpvc_plantilla = 'AFORO'
--97605
--Finalize Aggregate  (cost=510392.47..510392.48 rows=1 width=8)
--Finalize Aggregate  (cost=335032.18..335032.20 rows=1 width=8)

CREATE TABLE z_DESTINATARIO (
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
);


INSERT INTO z_DESTINATARIO (cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, dpvc_valorfecha, cpvc_valoropcion, cpvc_valorauxiliar, mpvc_valornumero, cpvc_transaccionregistro, cpvc_transaccioninactivo, cpvc_estado, cpvc_plantilla) 
select cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, dpvc_valorfecha, cpvc_valoropcion, cpvc_valorauxiliar, mpvc_valornumero, cpvc_transaccionregistro, cpvc_transaccioninactivo, cpvc_estado, cpvc_plantilla
 from t_pvc_main where cpvc_plantilla = 'DESTINATARIO';

delete from t_pvc_main where cpvc_plantilla = 'DESTINATARIO';

ALTER TABLE pedidoventacaracteristica_pvcp ATTACH PARTITION z_DESTINATARIO for values in ('DESTINATARIO');

CREATE INDEX ix_z_DESTINATARIO_documento ON z_DESTINATARIO USING btree (cpvc_documento);
CREATE INDEX ix_z_DESTINATARIO_valoropcion ON z_DESTINATARIO USING btree (cpvc_valoropcion);

CREATE UNIQUE INDEX pk_z_DESTINATARIO ON z_DESTINATARIO USING btree (cpvc_llave);--Toca crearlo porque se duplica el id

--select count(*) from z_destinatario 

-----------------------------------------------
CREATE TABLE z_AFORO_CONTRA (
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
);

INSERT INTO z_AFORO_CONTRA (cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, dpvc_valorfecha, cpvc_valoropcion, cpvc_valorauxiliar, mpvc_valornumero, cpvc_transaccionregistro, cpvc_transaccioninactivo, cpvc_estado, cpvc_plantilla) 

select cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, dpvc_valorfecha, cpvc_valoropcion, cpvc_valorauxiliar, mpvc_valornumero, cpvc_transaccionregistro, cpvc_transaccioninactivo, cpvc_estado, cpvc_plantilla
 from pedidoventacaracteristica_pvcp where cpvc_plantilla = 'AFORO-CONTRA'
and cpvc_llave not in (select cpvc_llave from z_AFORO_CONTRA);

delete from t_pvc_main where cpvc_plantilla = 'AFORO-CONTRA';

ALTER TABLE pedidoventacaracteristica_pvcp ATTACH PARTITION z_AFORO_CONTRA for values in ('AFORO-CONTRA');

CREATE INDEX ix_z_AFORO_CONTRA_documento ON z_AFORO_CONTRA USING btree (cpvc_documento);

CREATE INDEX ix_z_AFORO_CONTRA_valoropcion ON z_AFORO_CONTRA USING btree (cpvc_valoropcion);

CREATE UNIQUE INDEX pk_z_AFORO_CONTRA ON z_AFORO_CONTRA USING btree (cpvc_llave);--Toca crearlo porque se duplica el id

--------------------------------------------------------------------------------------------------------------------------

CREATE TABLE z_AFORO (
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
);

INSERT INTO z_AFORO (cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, dpvc_valorfecha, cpvc_valoropcion, cpvc_valorauxiliar, mpvc_valornumero, cpvc_transaccionregistro, cpvc_transaccioninactivo, cpvc_estado, cpvc_plantilla) 
select cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, dpvc_valorfecha, cpvc_valoropcion, cpvc_valorauxiliar, mpvc_valornumero, cpvc_transaccionregistro, cpvc_transaccioninactivo, cpvc_estado, cpvc_plantilla
 from pedidoventacaracteristica_pvcp where cpvc_plantilla = 'AFORO';

delete from t_pvc_main where cpvc_plantilla = 'AFORO';

ALTER TABLE pedidoventacaracteristica_pvcp ATTACH PARTITION z_AFORO for values in ('AFORO');

CREATE INDEX ix_z_AFORO_documento ON z_AFORO USING btree (cpvc_documento);
CREATE INDEX ix_z_AFORO_valoropcion ON z_AFORO USING btree (cpvc_valoropcion);

CREATE UNIQUE INDEX pk_z_AFORO ON z_AFORO USING btree (cpvc_llave);--Toca crearlo porque se duplica el id

-----------------COMISION DE VENDEDORES---------------------

CREATE TABLE z_ENC008 (
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
);

INSERT INTO z_ENC008 (cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, dpvc_valorfecha, cpvc_valoropcion, cpvc_valorauxiliar, mpvc_valornumero, cpvc_transaccionregistro, cpvc_transaccioninactivo, cpvc_estado, cpvc_plantilla) 
select cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, dpvc_valorfecha, cpvc_valoropcion, cpvc_valorauxiliar, mpvc_valornumero, cpvc_transaccionregistro, cpvc_transaccioninactivo, cpvc_estado, cpvc_plantilla
 from pedidoventacaracteristica_pvcp where cpvc_plantilla = 'ENC008';

delete from pedidoventacaracteristica_pvcp where cpvc_plantilla = 'ENC008';

ALTER TABLE pedidoventacaracteristica_pvcp ATTACH PARTITION z_ENC008 for values in ('ENC008');

CREATE INDEX ix_z_ENC008_documento ON z_ENC008 USING btree (cpvc_documento);
CREATE INDEX ix_z_ENC008_valoropcion ON z_ENC008 USING btree (cpvc_valoropcion);

CREATE UNIQUE INDEX pk_z_ENC008 ON z_ENC008 USING btree (cpvc_llave);--Toca crearlo porque se duplica el id

-----------------CLIENTES---------------------

CREATE TABLE z_CLIENTE (
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
);

INSERT INTO z_CLIENTE (cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, dpvc_valorfecha, cpvc_valoropcion, cpvc_valorauxiliar, mpvc_valornumero, cpvc_transaccionregistro, cpvc_transaccioninactivo, cpvc_estado, cpvc_plantilla) 
select cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, dpvc_valorfecha, cpvc_valoropcion, cpvc_valorauxiliar, mpvc_valornumero, cpvc_transaccionregistro, cpvc_transaccioninactivo, cpvc_estado, cpvc_plantilla
 from pedidoventacaracteristica_pvcp where cpvc_plantilla = 'CLIENTE';

delete from z_main where cpvc_plantilla = 'CLIENTE';

ALTER TABLE pedidoventacaracteristica_pvcp ATTACH PARTITION z_CLIENTE for values in ('CLIENTE');

CREATE INDEX ix_z_CLIENTE_documento ON z_CLIENTE USING btree (cpvc_documento);
CREATE INDEX ix_z_CLIENTE_valoropcion ON z_CLIENTE USING btree (cpvc_valoropcion);

CREATE UNIQUE INDEX pk_z_CLIENTE ON z_CLIENTE USING btree (cpvc_llave);--Toca crearlo porque se duplica el id

--	CUMPLIDO	473166
--3e84dc4a8efc407eaa8e40108874f5a2	MANUAL	163570
--CARGUE	CARGUE	48673
--DESPACHO	DESPACHO	44459

-----------------CUMPLIDO---------------------

CREATE TABLE z_CUMPLIDO (
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
);

INSERT INTO z_CUMPLIDO (cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, dpvc_valorfecha, cpvc_valoropcion, cpvc_valorauxiliar, mpvc_valornumero, cpvc_transaccionregistro, cpvc_transaccioninactivo, cpvc_estado, cpvc_plantilla) 
select cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, dpvc_valorfecha, cpvc_valoropcion, cpvc_valorauxiliar, mpvc_valornumero, cpvc_transaccionregistro, cpvc_transaccioninactivo, cpvc_estado, cpvc_plantilla
 from pedidoventacaracteristica_pvcp where cpvc_plantilla = 'CUMPLIDO';

delete from z_main where cpvc_plantilla = 'CUMPLIDO';

ALTER TABLE pedidoventacaracteristica_pvcp ATTACH PARTITION z_CUMPLIDO for values in ('CUMPLIDO');

CREATE INDEX ix_z_CUMPLIDO_documento ON z_CUMPLIDO USING btree (cpvc_documento);
CREATE INDEX ix_z_CUMPLIDO_valoropcion ON z_CUMPLIDO USING btree (cpvc_valoropcion);

CREATE UNIQUE INDEX pk_z_CUMPLIDO ON z_CUMPLIDO USING btree (cpvc_llave);--Toca crearlo porque se duplica el id
