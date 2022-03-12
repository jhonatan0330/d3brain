
COMMENT ON TABLE usuario_usrp IS '2018-05-16';


CREATE OR REPLACE FUNCTION copiar_plantilla(
    nombre_plantilla_actual character,
    nombre_plantilla_nueva character)
  RETURNS void LANGUAGE plpgsql AS '
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
	plantilla_inventarios documentoplantillainventario_dpip;
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

    for plantilla_inventarios in select * from documentoplantillainventario_dpip where cdpi_plantilla = id_plantilla_actual
    loop
	SELECT replace((md5(random()::text || clock_timestamp()::text)::uuid)::text, ''-'','''') INTO id_auxiliar_2;
	INSERT INTO documentoplantillainventario_dpip(cdpi_llave, cdpi_plantilla, bdpi_entrada, cdpi_constante, cdpi_estado, cdpi_parametro)
	    VALUES (id_auxiliar_2, id_plantilla_nueva, plantilla_inventarios.bdpi_entrada, plantilla_inventarios.cdpi_constante, plantilla_inventarios.cdpi_estado, plantilla_inventarios.cdpi_parametro);
    end loop;
	
    --RAISE EXCEPTION ''VAmos bien'' USING HINT = ''VAmos bien'';
END; 
';




INSERT INTO plantillapropiedad_pprp(cppr_llave, cppr_plantilla, cppr_key, cppr_valor, cppr_texto)  
select substring('ENC-'|| cdpl_llave, 0, 32), cdpl_llave, 'ENCABEZADO', 'SEDE', (select cusr_identificacion from usuario_usrp  where cusr_llave  = 'SEDE1') from documentoplantilla_dplp  where cdpl_llave  in (select crpb_plantilla from reportebase_rpbp where crpb_codigo  in ('POS001', 'POS015'));