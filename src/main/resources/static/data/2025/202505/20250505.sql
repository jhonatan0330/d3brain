COMMENT ON TABLE usuario_usrp IS '2025-05-05';

CREATE INDEX ix_maparesulatados_cuentaperiodo
	ON account.maparesultados_rmp
  USING btree
  (crmp_cuenta, crmp_periodo);
  
  
  CREATE INDEX ix_registroauxiliar_registro
	ON account.registroauxiliar_rax
  USING btree
  (crax_registro);