
COMMENT ON TABLE usuario_usrp IS '2018-07-27';

CREATE TABLE bodega_bodp (
	cbod_llave character varying(32) NOT NULL,
	cbod_nombre character varying(100) NOT NULL,
	cbod_codigo character varying(20) NOT NULL,
	cbod_estado character varying(1) DEFAULT 'A'::character varying NOT NULL
);

ALTER TABLE bodega_bodp
	ADD CONSTRAINT pk_bodega_bodp PRIMARY KEY (cbod_llave);

INSERT INTO bodega_bodp (cbod_llave, cbod_nombre, cbod_codigo)
select cusr_llave, cusr_nombre, cusr_identificacion from usuario_usrp where cusr_llave in (select distinct(cpin_bodega) from productoinventario_pinp);

ALTER TABLE deduccionproducto_dprp
	DROP CONSTRAINT fk_deduccionproductobodega;
	
INSERT INTO bodega_bodp (cbod_llave, cbod_nombre, cbod_codigo)
select cdpr_bodega,cdpr_bodega,cdpr_bodega from deduccionproducto_dprp where cdpr_bodega not in (select cbod_llave from bodega_bodp)
	group by cdpr_bodega;
	
ALTER TABLE deduccionproducto_dprp
	ADD CONSTRAINT fk_deduccionproductobodega FOREIGN KEY (cdpr_bodega) REFERENCES bodega_bodp(cbod_llave);

ALTER TABLE productoinventario_pinp
	DROP CONSTRAINT fk_productoinventariobodega;

ALTER TABLE productoinventario_pinp
	ADD CONSTRAINT fk_productoinventariobodega FOREIGN KEY (cpin_bodega) REFERENCES bodega_bodp(cbod_llave);

ALTER TABLE trazabilidadproductoinventario_tpip
	DROP CONSTRAINT fk_trazabilidadproductoinventariobodega;

ALTER TABLE trazabilidadproductoinventario_tpip
	ADD CONSTRAINT fk_trazabilidadproductoinventariobodega FOREIGN KEY (ctpi_bodega) REFERENCES public.bodega_bodp(cbod_llave);

CREATE OR REPLACE FUNCTION copiar_plantilla(nombre_plantilla_actual character, nombre_plantilla_nueva character) RETURNS void
    LANGUAGE plpgsql AS '
DECLARE 
	id_plantilla_actual character varying;
	id_plantilla_nueva character varying;
	id_auxiliar character varying;
	id_auxiliar_2 character varying;
	plantilla_parametros plantillaparametro_pprp;
	plantilla_propiedades plantillapropiedad_pprp;
	plantilla_reportes reportebase_rpbp;
	plantilla_roles documentoplantillarol_dprp;
	plantilla_categorias documentoplantillacategoria_dpcp;
	campos documentoplantillacaracteristica_dpcp;
	campo_parametros plantillacampoparametro_pcpp;
	campo_consecutivos plantillaconsecutivo_pcnp;
	campo_permisos documentoplantillapermiso_dppp;
	
BEGIN 
    SELECT cdpl_llave INTO id_plantilla_actual FROM documentoplantilla_dplp where cdpl_nombre = nombre_plantilla_actual and cdpl_estado = ''A'';
    IF id_plantilla_actual IS NULL THEN 
	RAISE EXCEPTION ''No se encuentra una plantilla con ese nombre'' USING HINT = ''Verificque el nombre de la plantilla que desea copiar'';
    END IF;
    SELECT replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, ''-'','''') INTO id_plantilla_nueva;
    INSERT INTO consecutivo_conp (ccon_llave, ccon_nombre, ccon_prefijo, ccon_sufijo, bcon_manual, mcon_numeroinicial, mcon_numerofinal, mcon_numeroactual)
	select id_plantilla_nueva, nombre_plantilla_nueva, null, null, bcon_manual, 100, mcon_numerofinal, 100 from consecutivo_conp 
		where ccon_llave = (select cdpl_consecutivo from documentoplantilla_dplp where cdpl_llave =  id_plantilla_actual);
    INSERT INTO documentoplantilla_dplp (cdpl_llave, cdpl_nombre, cdpl_consecutivo, cdpl_imagen, cdpl_proceso, cdpl_color, cdpl_codigo, cdpl_tipo)
	select id_plantilla_nueva, nombre_plantilla_nueva, id_plantilla_nueva, cdpl_imagen, cdpl_proceso, cdpl_color, cdpl_codigo, cdpl_tipo from documentoplantilla_dplp where cdpl_llave =  id_plantilla_actual;

    for campos in select * from documentoplantillacaracteristica_dpcp where cdpc_plantilla = id_plantilla_actual
    loop
	 SELECT replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, ''-'','''') INTO id_auxiliar;
	 INSERT INTO documentoplantillacaracteristica_dpcp (cdpc_llave, cdpc_plantilla, bdpc_obligatorio, cdpc_estado, 
		ndpc_orden, bdpc_visiblerender, cdpc_valordefecto, bdpc_filtro, cdpc_codigodepende, bdpc_editable, cdpc_imagen, 
		bdpc_modificable, cdpc_nombre, cdpc_codigo, cdpc_formato)
		VALUES(id_auxiliar, id_plantilla_nueva, campos.bdpc_obligatorio, campos.cdpc_estado, 
		campos.ndpc_orden, campos.bdpc_visiblerender, campos.cdpc_valordefecto, campos.bdpc_filtro, campos.cdpc_codigodepende, campos.bdpc_editable, campos.cdpc_imagen, 
		campos.bdpc_modificable, campos.cdpc_nombre, campos.cdpc_codigo, campos.cdpc_formato);
	for campo_parametros in select * from plantillacampoparametro_pcpp where cpcp_campo = campos.cdpc_llave
		loop
			SELECT replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, ''-'','''') INTO id_auxiliar_2;
			INSERT INTO plantillacampoparametro_pcpp (cpcp_llave, cpcp_campo, cpcp_key, cpcp_valor, cpcp_estado, cpcp_texto)
				VALUES(id_auxiliar_2, id_auxiliar, campo_parametros.cpcp_key, campo_parametros.cpcp_valor, campo_parametros.cpcp_estado, campo_parametros.cpcp_texto);
		end loop;
	for campo_consecutivos in select * from plantillaconsecutivo_pcnp where cpcn_caracteristica = campos.cdpc_llave
		loop
			SELECT replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, ''-'','''') INTO id_auxiliar_2;
			INSERT INTO plantillaconsecutivo_pcnp (cpcn_llave, cpcn_caracteristica, cpcn_valoropcion, cpcn_consecutivo, cpcn_estado)
				VALUES(id_auxiliar_2, id_auxiliar, campo_consecutivos.cpcn_valoropcion, campo_consecutivos.cpcn_consecutivo, campo_consecutivos.cpcn_estado);
		end loop;
	for campo_permisos in select * from documentoplantillapermiso_dppp where cdpp_caracteristica = campos.cdpc_llave
		loop
			SELECT replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, ''-'','''') INTO id_auxiliar_2;
			INSERT INTO documentoplantillapermiso_dppp (cdpp_llave, cdpp_caracteristica, cdpp_rol, cdpp_valordefecto, cdpp_estado, 
				bdpp_modificable, bdpp_editable, cdpp_codigodepende,  bdpp_visiblerender)
				VALUES(id_auxiliar_2, id_auxiliar, campo_permisos.cdpp_rol, campo_permisos.cdpp_valordefecto, campo_permisos.cdpp_estado, 
				campo_permisos.bdpp_modificable, campo_permisos.bdpp_editable, campo_permisos.cdpp_codigodepende,  campo_permisos.bdpp_visiblerender);
		end loop;
    end loop;
    for plantilla_parametros in select * from plantillaparametro_pprp where cppr_plantilla = id_plantilla_actual
    loop
	SELECT replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, ''-'','''') INTO id_auxiliar_2;
	INSERT INTO plantillaparametro_pprp(cppr_llave, cppr_plantilla, cppr_valorsubtotal, cppr_valortotal, 
            cppr_descripcion, cppr_consecutivoescrito, cppr_fecha, bppr_ordenadoxnombre, cppr_estado)
		VALUES (id_auxiliar_2, id_plantilla_nueva, plantilla_parametros.cppr_valorsubtotal, plantilla_parametros.cppr_valortotal, 
		plantilla_parametros.cppr_descripcion, plantilla_parametros.cppr_consecutivoescrito, plantilla_parametros.cppr_fecha, plantilla_parametros.bppr_ordenadoxnombre, plantilla_parametros.cppr_estado);

    end loop;
    for plantilla_propiedades in select * from plantillapropiedad_pprp where cppr_plantilla = id_plantilla_actual
    loop
	SELECT replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, ''-'','''') INTO id_auxiliar_2;
	INSERT INTO plantillapropiedad_pprp(cppr_llave, cppr_plantilla, cppr_key, cppr_valor, cppr_texto, 
            cppr_estado)
		VALUES (id_auxiliar_2, id_plantilla_nueva, plantilla_propiedades.cppr_key, plantilla_propiedades.cppr_valor, plantilla_propiedades.cppr_texto, plantilla_propiedades.cppr_estado);

    end loop;
    for plantilla_reportes in select * from reportebase_rpbp where crpb_plantilla = id_plantilla_actual
    loop
	SELECT replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, ''-'','''') INTO id_auxiliar_2;
	INSERT INTO reportebase_rpbp(crpb_llave, crpb_nombre, crpb_jaspertext, crpb_estado, crpb_variables, crpb_plantilla, brpb_soloexistente, crpb_codigo)
		VALUES (id_auxiliar_2, plantilla_reportes.crpb_nombre, plantilla_reportes.crpb_jaspertext, plantilla_reportes.crpb_estado, plantilla_reportes.crpb_variables, 
            id_plantilla_nueva, plantilla_reportes.brpb_soloexistente, plantilla_reportes.crpb_codigo);
    end loop;
    for plantilla_roles in select * from documentoplantillarol_dprp where cdpr_plantilla = id_plantilla_actual
    loop
	SELECT replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, ''-'','''') INTO id_auxiliar_2;
	INSERT INTO documentoplantillarol_dprp(cdpr_llave, cdpr_plantilla, cdpr_rol, cdpr_estado, bdpr_iniciorapido, 
            bdpr_crear, bdpr_modificar, bdpr_listable, bdpr_rangofiltro, 
            bdpr_vertodos, ndpr_orden, bdpr_totalvisiblerender)
	    VALUES (id_auxiliar_2, id_plantilla_nueva, plantilla_roles.cdpr_rol, plantilla_roles.cdpr_estado, plantilla_roles.bdpr_iniciorapido, 
            plantilla_roles.bdpr_crear, plantilla_roles.bdpr_modificar, plantilla_roles.bdpr_listable, plantilla_roles.bdpr_rangofiltro, 
            plantilla_roles.bdpr_vertodos, plantilla_roles.ndpr_orden, plantilla_roles.bdpr_totalvisiblerender);
    end loop;
    for plantilla_categorias in select * from documentoplantillacategoria_dpcp where cdpc_plantilla = id_plantilla_actual
    loop
	SELECT replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, ''-'','''') INTO id_auxiliar_2;
	INSERT INTO documentoplantillacategoria_dpcp(cdpc_llave, cdpc_plantilla, cdpc_categoria, cdpc_estado)
	    VALUES (id_auxiliar_2, id_plantilla_nueva, plantilla_categorias.cdpc_categoria, plantilla_categorias.cdpc_estado);
    end loop;
	
    --RAISE EXCEPTION ''VAmos bien'' USING HINT = ''VAmos bien'';
END; 
';

--select * from documentoplantillainventario_dpip where cdpi_estado = 'A';
--DROP TABLE documentoplantillainventario_dpip;
insert into documentoplantillacaracteristica_dpcp  (cdpc_llave, cdpc_plantilla, bdpc_obligatorio, ndpc_orden, cdpc_nombre, cdpc_codigo, cdpc_formato, cdpc_codigodepende)
select substring('BOD-' || cdpi_llave, 0, 32), cdpi_plantilla, true, 1, 'BODEGA', 'BOD', 'D', 
(SELECT CASE WHEN (select cdpc_codigo from documentoplantillacaracteristica_dpcp  where cdpc_plantilla  = cdpi_plantilla and cdpc_formato = 'J') IS NULL 
THEN (select cdpc_codigo from documentoplantillacaracteristica_dpcp  where cdpc_plantilla  = cdpi_plantilla and cdpc_formato = 'Z' and (SELECT cpcp_valor FROM plantillacampoparametro_pcpp  where cpcp_campo  = cdpc_llave  and cpcp_key = 'PROCESO_GESTIONAR_ESTADOS' and cpcp_estado = 'A') = 'TRUE')
ELSE (select cdpc_codigo from documentoplantillacaracteristica_dpcp  where cdpc_plantilla  = cdpi_plantilla and cdpc_formato = 'J') END)
from documentoplantillainventario_dpip where cdpi_estado = 'A';

insert into plantillacampoparametro_pcpp (cpcp_llave, cpcp_campo, cpcp_key, cpcp_valor, cpcp_texto)
select substring('BOD-' || cdpi_llave, 0, 32), substring('BOD-' || cdpi_llave, 0, 32), 'BODEGA_FIJA', cdpi_constante 
,(select cbod_nombre from bodega_bodp where cbod_llave = cdpi_constante)
from documentoplantillainventario_dpip where cdpi_estado = 'A' and cdpi_constante is not null;

insert into plantillacampoparametro_pcpp (cpcp_llave, cpcp_campo, cpcp_key, cpcp_valor, cpcp_texto)
select substring('BOD-1-' || cdpi_llave, 0, 32), substring('BOD-' || cdpi_llave, 0, 32), 'BODEGA_MOVIMIENTO', 'E', 'ENTRADA' from documentoplantillainventario_dpip where cdpi_estado = 'A' and bdpi_entrada = true;