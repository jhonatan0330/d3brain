COMMENT ON TABLE usuario_usrp IS '2018-07-14';

ALTER TABLE tarifa_tarp DROP CONSTRAINT fk_tarifarecurso;

update tarifa_tarp set ctar_recurso = (select cpdv_llave from pedidoventa_pdvp where cpdv_nombre = (select cusr_identificacion from usuario_usrp  where cusr_llave  = ctar_recurso limit 1) limit 1) where ctar_recurso is not null;

ALTER TABLE tarifa_tarp ADD CONSTRAINT fk_tarifarecurso FOREIGN KEY (ctar_recurso) REFERENCES pedidoventa_pdvp(cpdv_llave);
