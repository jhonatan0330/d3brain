COMMENT ON TABLE usuario_usrp IS '2023-07-13';

update procesoestado_pesp set cpes_codigo =	cpes_llave where cpes_codigo is null;

update propiedad_ppdp pp set cppd_valor = '1', cppd_texto = null where cppd_propiedadvalor = 'PROP_91' and cppd_estado = 'A';

ALTER TABLE procesotransicion_ptrp ADD cptr_codigo varchar(50) NULL;

update procesotransicion_ptrp set cptr_codigo =	cptr_llave where cptr_codigo is null;

ALTER TABLE procesotransicion_ptrp ALTER cptr_codigo SET NOT NULL;

ALTER TABLE procesoestado_pesp ALTER cpes_codigo SET NOT NULL;