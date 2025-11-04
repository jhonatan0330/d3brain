COMMENT ON TABLE usuario_usrp IS '2025-10-31';

ALTER TABLE public.transaccionlog_tlgp ADD ctlg_usuario varchar(32) NULL;

ALTER TABLE public.transaccionlog_tlgp ADD ctlg_entrada varchar(2000) NULL;

ALTER TABLE public.transaccionlog_tlgp ADD ctlg_salida varchar(2000) NULL;