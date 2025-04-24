COMMENT ON TABLE usuario_usrp IS '2025-04-24';

ALTER TABLE account.catalogo_ctg ADD cctg_plantila varchar(32);

update account.catalogo_ctg set cctg_plantila = (select cpdv_plantilla from pedidoventa_pdvp pp where cpdv_llave = cctg_documento );

ALTER TABLE account.catalogo_ctg alter COLUMN cctg_plantila set not null;

ALTER TABLE public.reporteejecucion_rejp DROP CONSTRAINT fk_reporteejecuciondocumento;
