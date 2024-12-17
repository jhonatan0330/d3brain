COMMENT ON TABLE usuario_usrp IS '2024-12-17';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo)
    SELECT 'PROP_275' , 'O', 'PLANTILLA REGISTRO NUEVO USUARIO', 'PLANTILLA_NUEVO_USUARIO', 'PERMISOS'
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_275');

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_piderol, bpvd_pideusuario, bpvd_propiedadboolean)
    SELECT 'PROP_276' , 'L', 'PERMISO GUARDAR  A USUARIO PUBLICO', 'PLANTILLA_PERMISO_PUBLICO', 'PERMISOS', true, true, true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_276');

ALTER TABLE usuariosesion_ussp
	ADD COLUMN buss_privada boolean DEFAULT false NOT NULL;
	
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_grupo, bpvd_piderol, bpvd_pideusuario, bpvd_textoculto)
    SELECT 'PROP_277' , 'L', 'INFORMACION AL USUARIO AL GUARDAR PLANTILLA', 'PLANTILLA_SUCCESS_INFORMATION', 'PERMISOS', true, true, true
    where not exists (select 1 from propiedadvalordefinido_pvdp where cpvd_llave  = 'PROP_277');