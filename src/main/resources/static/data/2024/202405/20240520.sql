COMMENT ON TABLE usuario_usrp IS '2024-05-20';

CREATE INDEX ix_maparesultados_tipo_cuenta ON account.mapa_resultados_rmp USING btree (crmp_tipo, crmp_cuenta);