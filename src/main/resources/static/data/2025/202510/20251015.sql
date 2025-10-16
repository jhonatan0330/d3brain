COMMENT ON TABLE usuario_usrp IS '2025-10-15';
	
ALTER TABLE public.producto_prop DROP CONSTRAINT if exists fk_productocategoria;

update producto_prop set cpro_categoria = (select cpdv_plantilla from pedidoventa_pdvp where cpdv_llave = cpro_documento);