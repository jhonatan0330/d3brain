

COMMENT ON TABLE usuario_usrp IS '2019-04-16';
 
COMMENT ON TABLE usuariosesion_ussp IS '2019.04.16.00';

CREATE INDEX ix_pedidoventadinero_documento
  ON pedidoventadinero_pvdp
  USING btree
  (cpvd_documento);
  
CREATE INDEX ix_documentorelacionexpediente_campomaestro
  ON documentorelacionexpediente_dexp
  USING btree
  (cdex_campomaestro);
