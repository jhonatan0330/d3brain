
CREATE TABLE learning.article_art (
	cart_llave varchar(32) NOT NULL,
	cart_estado varchar(1) NOT NULL DEFAULT 'A'::character varying,
	CONSTRAINT pk_article_art PRIMARY KEY (cart_llave)
);

ALTER TABLE learning.article_art ADD dart_creacionfecha timestamptz NOT NULL DEFAULT NOW();
ALTER TABLE learning.article_art ADD cart_creacionusuario varchar(32);
ALTER TABLE learning.article_art ADD cart_creacionusuarionombre varchar(200);
ALTER TABLE learning.article_art ADD dart_modificacionfecha timestamptz;


ALTER TABLE learning.article_art ADD cart_titulo varchar(100) NOT NULL ;
ALTER TABLE learning.article_art ADD cart_automatica varchar(120.000);
ALTER TABLE learning.article_art ADD cart_entidadTipo varchar(100) NOT NULL ;
ALTER TABLE learning.article_art ADD cart_entidadId varchar(32) NOT NULL ;
ALTER TABLE learning.article_art ADD cart_documento varchar(32) NOT NULL ;
ALTER TABLE learning.article_art ADD cart_imagen varchar(100);
ALTER TABLE learning.article_art ADD cart_introduccion varchar(100);
ALTER TABLE learning.article_art ADD cart_ayuda varchar(4.000);
