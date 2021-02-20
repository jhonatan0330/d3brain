COMMENT ON TABLE usuario_usrp IS '2020-11-14';
COMMENT ON TABLE usuariosesion_ussp IS '2020.11.14.00';


--En caso de fallar ejecutar este query las veces que sea necesario
--SELECT llenar_plantilla_campos();

DROP FUNCTION llenar_plantilla_campos();

ALTER TABLE categoriaproducto_cprp
	ALTER COLUMN mcpr_cantidadmaxima TYPE numeric(18,6) /* TYPE change - table: categoriaproducto_cprp original: numeric(16,2) new: numeric(18,6) */;

ALTER TABLE consecutivo_conp
	ALTER COLUMN mcon_numeroinicial TYPE numeric(18,6) /* TYPE change - table: consecutivo_conp original: numeric(16,2) new: numeric(18,6) */,
	ALTER COLUMN mcon_numerofinal TYPE numeric(18,6) /* TYPE change - table: consecutivo_conp original: numeric(16,2) new: numeric(18,6) */,
	ALTER COLUMN mcon_numeroactual TYPE numeric(18,6) /* TYPE change - table: consecutivo_conp original: numeric(16,2) new: numeric(18,6) */;

ALTER TABLE reportebase_rpbp
	ADD COLUMN brpb_publico boolean DEFAULT false NOT NULL;
