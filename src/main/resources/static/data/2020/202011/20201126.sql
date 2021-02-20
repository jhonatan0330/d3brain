

COMMENT ON TABLE usuario_usrp IS '2020-11-26';
COMMENT ON TABLE usuariosesion_ussp IS '2020.11.26.00';

ALTER TABLE pedidoventacaracteristica_pvcp
	DROP COLUMN cpvc_plantilla;

ALTER TABLE pedidoventa_pdvp
	ADD column if not EXISTS npdv_historico integer;
	
ALTER TABLE pedidoventacaracteristica_pvcp ALTER COLUMN mpvc_valornumero DROP NOT NULL;
ALTER TABLE pedidoventacaracteristica_pvcp ALTER COLUMN mpvc_valornumero DROP DEFAULT;

--CREATE INDEX ix_pedidoventacaracteristica_valornumero ON public.pedidoventacaracteristica_pvcp USING btree (mpvc_valornumero);