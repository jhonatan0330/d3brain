/**
	SE ingresa una tabla de disponibilidad de producto, si existe registro activo mira la disponibilidad si no entonces todos
	Arreglo los tipo numero con el identificador de las formulas
*/

COMMENT ON TABLE usuario_usrp IS '2018-01-30';


CREATE TABLE productodisponibilidadtiempo_pdtp (
	cpdt_llave character varying(32) NOT NULL,
	cpdt_producto character varying(32) NOT NULL,
	dpdt_fechainicial timestamp with time zone NOT NULL,
	dpdt_fechafinal timestamp with time zone NOT NULL,
	cpdt_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);


ALTER TABLE productodisponibilidadtiempo_pdtp
	ADD CONSTRAINT pk_productodisponibilidadtiempo_pdtp PRIMARY KEY (cpdt_llave);

ALTER TABLE productodisponibilidadtiempo_pdtp
	ADD CONSTRAINT fk_productodisponibilidadtiempoproducto FOREIGN KEY (cpdt_producto) REFERENCES producto_prop(cpro_llave);


update plantillacampoparametro_pcpp set cpcp_key = 'NUMERO_FORMULA'  where cpcp_campo  in (select cdpc_llave from documentoplantillacaracteristica_dpcp where cdpc_formato  = 'N') and cpcp_key = 'BASICA';
