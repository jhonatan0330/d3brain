COMMENT ON TABLE usuario_usrp IS '2020-11-12';
COMMENT ON TABLE usuariosesion_ussp IS '2020.11.12.00';

ALTER TABLE pedidoventacaracteristica_pvcp
	ADD column if not EXISTs cpvc_plantilla character varying(32);

--Creo una funcion para crear las nuevas funciones con el campo parametros
CREATE OR REPLACE FUNCTION llenar_plantilla_campos() RETURNS void AS 
'select 1;'
LANGUAGE SQL;

SELECT llenar_plantilla_campos();

CREATE INDEX if not exists ix_propiedad_ppdp_campoestado ON propiedad_ppdp USING btree (cppd_campo, cppd_estado);
--select count(*) from pedidoventacaracteristica_pvcp where cpvc_plantilla is null 