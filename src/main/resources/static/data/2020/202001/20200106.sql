
COMMENT ON TABLE usuario_usrp IS '2020-01-06';
COMMENT ON TABLE usuariosesion_ussp IS '2020.01.06.00';

ALTER TABLE puesto_puep
	DROP CONSTRAINT IF EXISTS fk_puestocampo;

CREATE TABLE usuarioorganizacion_uorp (
	cuor_llave character varying(32) NOT NULL,
	cuor_usuario character varying(32) NOT NULL,
	cuor_organizacion character varying(32) NOT NULL,
	buor_funciones boolean DEFAULT false NOT NULL,
	cuor_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

ALTER TABLE organizacion_orgp
	ADD COLUMN corg_principal character varying(32),
	ADD COLUMN corg_conexion character varying(4000),
	ADD COLUMN corg_invitado character varying(32),
	ADD COLUMN corg_imagen character varying(2000);

ALTER TABLE usuarioorganizacion_uorp
	ADD CONSTRAINT pk_usuarioorganizacion_uorp PRIMARY KEY (cuor_llave);

ALTER TABLE usuarioorganizacion_uorp
	ADD CONSTRAINT fk_usuarioorganizacionorganizacion FOREIGN KEY (cuor_organizacion) REFERENCES organizacion_orgp(corg_llave);

ALTER TABLE usuarioorganizacion_uorp
	ADD CONSTRAINT fk_usuarioorganizacionusuario FOREIGN KEY (cuor_usuario) REFERENCES usuario_usrp(cusr_llave);

ALTER TABLE organizacion_orgp
	ADD COLUMN corg_slogan character varying(4000);

update organizacion_orgp set corg_nombre = 'SOFTWARE PARA TI';
update organizacion_orgp set corg_slogan = 'Unificar, Simplificar y optimizar';
update organizacion_orgp set corg_conexion = 'jdbc:postgresql://192.168.0.10:5432/desarrollo_cs';
update organizacion_orgp set corg_imagen = 'http://golyat.cloud/imagenes/fondo.png';

ALTER TABLE organizacion_orgp
	ALTER COLUMN corg_slogan SET NOT NULL,
	ALTER COLUMN corg_conexion SET NOT NULL,
	ALTER COLUMN corg_imagen SET NOT NULL;
	

ALTER TABLE organizacion_orgp
	ADD COLUMN borg_sincronizacion boolean DEFAULT false NOT NULL;
	
ALTER TABLE organizacion_orgp
	ADD COLUMN corg_mensajeingreso character varying(4000);
	
update propiedadvalordefinido_pvdp set bpvd_propiedadboolean = true where cpvd_llave = 'PROP_93';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, cpvd_motivo) 
	VALUES('PROP_100' , 'E', 'REPORTE_PIE_PAGINA', 'PIE DE PAGINA', 'www.softwareparati.com', 'REQUISITO', 'El reporte tendra el pie de pagina general del sistema');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, bpvd_textoculto, cpvd_motivo) 
	VALUES('PROP_101' , 'L', 'IMAGEN_DOCUMENTACION', 'IMAGEN', 'www.softwareparati.com', 'REQUISITO', true, 'Imagen de ayuda al formulario');