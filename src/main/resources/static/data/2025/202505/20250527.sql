COMMENT ON TABLE usuario_usrp IS '2025-05-27';

ALTER TABLE account.comprobante_cmp ADD dcmp_fechaanulacion timestamp with time zone;

update account.comprobante_cmp SET dcmp_fechaanulacion = '1990-01-01';

ALTER TABLE account.pila_stk ADD cstk_accion varchar(1) ;

alter table account.comprobante_cmp DROP constraint if exists uk_comprobante_tipodocumento;

CREATE UNIQUE INDEX uk_comprobante_tipodocumentofechaanulacion ON account.comprobante_cmp USING btree (ccmp_tipo, ccmp_documento, dcmp_fechaanulacion)