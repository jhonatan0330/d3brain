COMMENT ON TABLE usuario_usrp IS '2025-09-16';

ALTER TABLE webservice_wbsp 
  ADD CONSTRAINT uk_webservice_codigoestado UNIQUE(cwbs_codigo, cwbs_estado);