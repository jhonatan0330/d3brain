
COMMENT ON TABLE usuario_usrp IS '2019-06-21';
COMMENT ON TABLE usuariosesion_ussp IS '2019.06.21.00';

update plantillacampoparametro_pcpp set cpcp_texto = (select cdpc_codigodepende from documentoplantillacaracteristica_dpcp  where cdpc_llave = cpcp_campo) where cpcp_key ='BODEGA_MOVIMIENTO';
update plantillacampoparametro_pcpp set cpcp_valor ='T', cpcp_texto = 'CANTIDAD' where cpcp_llave ='BOD-FIN-OP-1';
INSERT INTO plantillacampoparametro_pcpp (cpcp_llave, cpcp_campo, cpcp_key, cpcp_valor, cpcp_texto) 
select 'BOD-FIN-OP-11', 'FIN-OP-3', 'BODEGA_MOVIMIENTO', 'SC', 'CANT_AVERIA' from documentoplantillacaracteristica_dpcp where cdpc_llave = 'FIN-OP-3';
