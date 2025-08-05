 COMMENT ON TABLE usuario_usrp IS '2025-07-29';

CREATE INDEX ix_pedidoventaubicacion_documento ON public.pedidoventaubicacion_pvup USING btree (cpvu_documento);
CREATE INDEX ix_detallepedidoventa_documento ON public.detallepedidoventa_dpvp USING btree (cdpv_documento);