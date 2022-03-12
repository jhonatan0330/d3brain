
COMMENT ON TABLE usuario_usrp IS '2020-06-20';
COMMENT ON TABLE usuariosesion_ussp IS '2020.06.20.00';

CREATE TABLE servidor_serp (
	cser_llave character varying(32) NOT NULL,
	cser_nombre character varying(100) NOT NULL,
	cser_url character varying(4000) NOT NULL,
	cser_puerto character varying(10),
	cser_usuario character varying(4000),
	cser_clave character varying(4000),
	cser_base character varying(4000),
	cser_urlconexion character varying(4000),
	cser_tipo character varying(1) NOT NULL,
	nser_orden integer DEFAULT 0 NOT NULL,
	cser_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

insert into servidor_serp (cser_llave, cser_nombre, cser_url, cser_usuario, cser_clave, nser_orden, cser_tipo) 
	select cmpl_host, cmpl_host, cmpl_host, cmpl_usuario, cmpl_clave, 1, 'E' from mensajeplantillacorreo_mplp
	group by cmpl_host, cmpl_usuario, cmpl_clave;

ALTER TABLE mensajeplantillacorreo_mplp
	ADD COLUMN cmpl_servidor character varying(32);

update mensajeplantillacorreo_mplp set cmpl_servidor = cmpl_host;
   
ALTER TABLE mensajeplantillacorreo_mplp
	DROP COLUMN cmpl_host,
	DROP COLUMN cmpl_usuario,
	DROP COLUMN cmpl_clave,
	ALTER COLUMN cmpl_servidor SET NOT NULL;;

ALTER TABLE organizacion_orgp
	DROP COLUMN corg_conexion,
	ADD COLUMN corg_servidor character varying(32);

ALTER TABLE reportebase_rpbp
	ADD COLUMN crpb_servidor character varying(32);

ALTER TABLE servidor_serp
	ADD CONSTRAINT pk_servidor_serp PRIMARY KEY (cser_llave);

ALTER TABLE mensajeplantillacorreo_mplp
	ADD CONSTRAINT fk_mensajeplantillacorreoservidor FOREIGN KEY (cmpl_servidor) REFERENCES servidor_serp(cser_llave);

ALTER TABLE organizacion_orgp
	ADD CONSTRAINT fk_organizacionservidor FOREIGN KEY (corg_servidor) REFERENCES servidor_serp(cser_llave);

ALTER TABLE reportebase_rpbp
	ADD CONSTRAINT fk_reportebaseservidor FOREIGN KEY (crpb_servidor) REFERENCES servidor_serp(cser_llave);

insert into servidor_serp (cser_llave, cser_nombre, cser_url, cser_puerto, cser_usuario, cser_clave, cser_base, nser_orden, cser_urlconexion, cser_tipo)
select 
	p64.cppd_valor
	,p64.cppd_valor
	,p64.cppd_valor
	,(select p65.cppd_valor from propiedad_ppdp p65 where p65.cppd_propiedadvalor = 'PROP_65' and p65.cppd_estado = 'A')
	,(select p65.cppd_valor from propiedad_ppdp p65 where p65.cppd_propiedadvalor = 'PROP_66' and p65.cppd_estado = 'A')
	,(select p65.cppd_valor from propiedad_ppdp p65 where p65.cppd_propiedadvalor = 'PROP_67' and p65.cppd_estado = 'A')
	,(select p65.cppd_valor from propiedad_ppdp p65 where p65.cppd_propiedadvalor = 'PROP_62' and p65.cppd_estado = 'A')
	,1
	,(select p65.cppd_valor from propiedad_ppdp p65 where p65.cppd_propiedadvalor = 'PROP_68' and p65.cppd_estado = 'A')
	,'F'
from propiedad_ppdp p64
where p64.cppd_propiedadvalor = 'PROP_64' 
and p64.cppd_estado = 'A';

insert into servidor_serp (cser_llave, cser_nombre, cser_url, cser_puerto, cser_usuario, cser_clave, cser_base, nser_orden, cser_urlconexion, cser_tipo)
select 
	p62.cppd_valor
	,p62.cppd_valor
	,p62.cppd_valor
	,(select p65.cppd_valor from propiedad_ppdp p65 where p65.cppd_propiedadvalor = 'PROP_65' and p65.cppd_estado = 'A')
	,(select p65.cppd_valor from propiedad_ppdp p65 where p65.cppd_propiedadvalor = 'PROP_66' and p65.cppd_estado = 'A')
	,(select p65.cppd_valor from propiedad_ppdp p65 where p65.cppd_propiedadvalor = 'PROP_67' and p65.cppd_estado = 'A')
	,(select p65.cppd_valor from propiedad_ppdp p65 where p65.cppd_propiedadvalor = 'PROP_62' and p65.cppd_estado = 'A')
	,1
	,(select p65.cppd_valor from propiedad_ppdp p65 where p65.cppd_propiedadvalor = 'PROP_68' and p65.cppd_estado = 'A')
	,'L'
from propiedad_ppdp p62
where p62.cppd_propiedadvalor = 'PROP_62' 
and p62.cppd_estado = 'A';

delete from propiedad_ppdp where cppd_propiedadvalor  in ('PROP_61','PROP_62','PROP_63','PROP_64','PROP_65','PROP_66','PROP_67','PROP_68', 'PROP_86');
delete from propiedadvalordefinido_pvdp where cpvd_llave in ('PROP_61','PROP_62','PROP_63','PROP_64','PROP_65','PROP_66','PROP_67','PROP_68', 'PROP_86');

