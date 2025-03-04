COMMENT ON TABLE usuario_usrp IS '2025-03-03';

update procesotransicion_ptrp
 set bptr_documentador = true
where cptr_estadopartida is null and cptr_estado = 'A' and bptr_documentador = false;
