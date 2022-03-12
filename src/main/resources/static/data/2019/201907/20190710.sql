
COMMENT ON TABLE usuario_usrp IS '2019-07-10';

update consecutivo_conp set ccon_prefijo = (select cdpl_codigo ||'-' from documentoplantilla_dplp where cdpl_consecutivo = ccon_llave)
where ccon_llave in (select cdpl_consecutivo from documentoplantilla_dplp where cdpl_tipo = 'R');

update pedidoventa_pdvp 
set cpdv_nombre = (select ccon_prefijo from consecutivo_conp 
	where ccon_llave = (select cdpl_consecutivo from documentoplantilla_dplp where cdpl_consecutivo = cpdv_plantilla))
where cpdv_plantilla in (select cdpl_consecutivo from documentoplantilla_dplp where cdpl_tipo = 'R');

ALTER TABLE bodega_bodp
	ADD COLUMN bbod_agregarmanual boolean DEFAULT false NOT NULL;

ALTER TABLE rolacceso_racp
	ADD COLUMN nrac_minutossesion integer DEFAULT 0 NOT NULL;

ALTER TABLE usuariosesion_ussp
	ADD COLUMN duss_fechacierre timestamp with time zone;
