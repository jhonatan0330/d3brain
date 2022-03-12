COMMENT ON TABLE usuario_usrp IS '2019-12-04';
COMMENT ON TABLE usuariosesion_ussp IS '2019.12.04.00';

update reportebase_rpbp set crpb_descripcion = 'Pendiente documentar' where crpb_descripcion is null;

ALTER TABLE reportebase_rpbp
	ALTER COLUMN crpb_descripcion TYPE character varying(4000) /* TYPE change - table: reportebase_rpbp original: character varying(100) new: character varying(4000) */,
	ALTER COLUMN crpb_descripcion SET NOT NULL;

ALTER TABLE tarifa_tarp
	ADD COLUMN btar_rangoprecios boolean DEFAULT false NOT NULL;

update tarifa_tarp set btar_rangoprecios = (select btrf_rangoprecios from tarifario_trfp where ctrf_llave = ctar_tarifario);

ALTER TABLE tarifario_trfp
	DROP COLUMN btrf_rangoprecios;

delete from relacioninterna_ritp where crit_propiedad is null;

ALTER TABLE relacioninterna_ritp
	ALTER COLUMN crit_propiedad SET NOT NULL;