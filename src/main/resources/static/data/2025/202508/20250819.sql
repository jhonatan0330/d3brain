 COMMENT ON TABLE usuario_usrp IS '2025-08-19';

CREATE INDEX ix_registroauxiliar_comprobante ON account.registroauxiliar_rax USING btree (crax_comprobante);