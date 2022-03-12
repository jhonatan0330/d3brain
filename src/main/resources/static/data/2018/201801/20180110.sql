
COMMENT ON TABLE usuario_usrp IS '2018-01-10';

COMMENT ON TABLE usuariosesion_ussp IS '2018.01.10.11';

CREATE TABLE plantillacampoparametro_pcpp (
	cpcp_llave character varying(32) NOT NULL,
	cpcp_campo character varying(32) NOT NULL,
	cpcp_key character varying(100) NOT NULL,
	cpcp_valor character varying(100) NOT NULL,
	cpcp_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

ALTER TABLE plantillacampoparametro_pcpp
	ADD CONSTRAINT pk_plantillacampoparametro_pcpp PRIMARY KEY (cpcp_llave);

ALTER TABLE plantillacampoparametro_pcpp
	ADD CONSTRAINT fk_plantillacampoparametrocampo FOREIGN KEY (cpcp_campo) REFERENCES documentoplantillacaracteristica_dpcp(cdpc_llave);

INSERT INTO plantillacampoparametro_pcpp (cpcp_llave, cpcp_campo, cpcp_key, cpcp_valor)
	SELECT cdpc_llave, cdpc_llave, 'BASICA', cdpc_tipoparametro FROM documentoplantillacaracteristica_dpcp WHERE cdpc_tipoparametro IS NOT NULL AND cdpc_estado = 'A';

ALTER TABLE documentoplantillacaracteristica_dpcp DROP COLUMN cdpc_tipoparametro;

INSERT INTO plantillacampoparametro_pcpp (cpcp_llave, cpcp_campo, cpcp_key, cpcp_valor)
	select substring('-1' || cpcc_llave, 0, 32), cpcc_campo, 'AUTOLOAD', 'TRUE' from plantillacampocomplemento_pccp where bpcc_autoload = true;
	
INSERT INTO plantillacampoparametro_pcpp (cpcp_llave, cpcp_campo, cpcp_key, cpcp_valor)
	select substring('-2' || cpcc_llave, 0, 32), cpcc_campo, 'MULTIPLE', 'TRUE' from plantillacampocomplemento_pccp where bpcc_multiple = true;

INSERT INTO plantillacampoparametro_pcpp (cpcp_llave, cpcp_campo, cpcp_key, cpcp_valor)
	select substring('-3' || cpcc_llave, 0, 32), cpcc_campo, 'CAMPO_HEREDADO', cpcc_campoheredado from plantillacampocomplemento_pccp where cpcc_campoheredado is not null;

INSERT INTO plantillacampoparametro_pcpp (cpcp_llave, cpcp_campo, cpcp_key, cpcp_valor)
	select substring('-4' || substring(cpcc_documentoauxiliar, 0, 5) || cpcc_llave, 0, 32), cpcc_campo, 'PLANTILLA_AUXILIAR', cpcc_documentoauxiliar from plantillacampocomplemento_pccp where cpcc_documentoauxiliar is not null;

DROP TABLE plantillacampocomplemento_pccp;

ALTER TABLE documentoplantillapermiso_dppp
	DROP COLUMN cdpp_tipoparametro,
	DROP COLUMN bdpp_recursoplantilla;

ALTER TABLE documentoplantilla_dplp
	ADD COLUMN cdpl_tipo character varying(1);

update documentoplantilla_dplp  set cdpl_tipo = 'R' where cdpl_imagen is null;

update documentoplantilla_dplp  set cdpl_imagen = 'http://colombiansofture.com/imagenes/modulo.png' where cdpl_imagen is null;

update documentoplantilla_dplp  set cdpl_tipo = 'F' where cdpl_tipo is null;

ALTER TABLE documentoplantilla_dplp
	ALTER COLUMN cdpl_tipo SET NOT NULL;

UPDATE reportebase_rpbp SET crpb_variables = 'JASPERTIPO=XLS' WHERE crpb_exportarformato = '2' and crpb_variables is null;

UPDATE reportebase_rpbp SET crpb_variables = crpb_variables || '&JASPERTIPO=XLS' WHERE crpb_exportarformato = '2' and crpb_variables is not null;

ALTER TABLE reportebase_rpbp
	DROP COLUMN crpb_exportarformato;
