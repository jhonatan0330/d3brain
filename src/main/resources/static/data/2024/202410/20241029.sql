COMMENT ON TABLE usuario_usrp IS '2024-10-29';

truncate account.maparesultados_rmp ;

truncate account.cuenta_cue CASCADE;

truncate table account.catalogo_ctg CASCADE; 

ALTER TABLE account.catalogo_ctg ADD cctg_documento varchar(32) NOT NULL ;

ALTER TABLE account.cuenta_cue ADD ccue_documento varchar(32) NOT NULL ;