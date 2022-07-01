COMMENT ON TABLE usuario_usrp IS '2020-07-30';

INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_141' , 'L', 'TIPO ROL', 'PLANTILLA_TIPO_ROL', 'www.softwareparati.com', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_142' , 'L', 'TIPO REPORTE', 'PLANTILLA_TIPO_REPORTE', 'www.softwareparati.com', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_143' , 'L', 'TIPO CUENTA', 'PLANTILLA_TIPO_CUENTA', 'www.softwareparati.com', 'REQUISITO', true);
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo) 
	VALUES('PROP_144' , 'L', 'TIPO PRODUCTO', 'PLANTILLA_TIPO_PRODUCTO', 'www.softwareparati.com', 'REQUISITO');
INSERT INTO propiedadvalordefinido_pvdp (cpvd_llave, cpvd_origen, cpvd_nombre, cpvd_codigo, cpvd_ayuda, cpvd_grupo, bpvd_propiedadboolean) 
	VALUES('PROP_145' , 'L', 'TIPO BODEGA', 'PLANTILLA_TIPO_BODEGA', 'www.softwareparati.com', 'REQUISITO', true);

ALTER TABLE documentoplantilla_dplp
	DROP CONSTRAINT fk_documentoplantillaproceso;

ALTER TABLE documentoplantilla_dplp
	DROP COLUMN cdpl_proceso;

ALTER TABLE pedidoventa_pdvp
	ALTER COLUMN cpdv_funcionario DROP NOT NULL;

ALTER TABLE bodega_bodp
	DROP COLUMN bbod_agregarmanual,
	ADD COLUMN cbod_documento character varying(32);

INSERT INTO cambio_cmbp(ccmb_llave,  ccmb_nombre,  ccmb_motivo,  dcmb_fecha)VALUES('SC_20200730',  'SC_20200730',  'Ingresar las bodegas y productos',  now());

INSERT INTO propiedad_ppdp (cppd_llave, cppd_campo, cppd_valor, cppd_propiedadvalor, dppd_fechadefinicion,  cppd_cambiocreacion, cppd_tipo) 
	select substring('TP-L-' || cdpl_llave , 0 , 32), cdpl_llave, '1', 'PROP_141', now(), 'SC_20200730', 'L' 
	from documentoplantilla_dplp where cdpl_tipo = 'L' and cdpl_estado = 'A';--Rol

INSERT INTO propiedad_ppdp (cppd_llave, cppd_campo, cppd_valor, cppd_propiedadvalor, dppd_fechadefinicion,  cppd_cambiocreacion, cppd_tipo) 
	select substring('TP-R-' || cdpl_llave , 0 , 32), cdpl_llave, '1', 'PROP_142', now(), 'SC_20200730', 'L' 
	from documentoplantilla_dplp where cdpl_tipo = 'R' and cdpl_estado = 'A';--Reporte

INSERT INTO propiedad_ppdp (cppd_llave, cppd_campo, cppd_valor, cppd_propiedadvalor, dppd_fechadefinicion,  cppd_cambiocreacion, cppd_tipo) 
	select substring('TP-C-' || cdpl_llave , 0 , 32), cdpl_llave, '1', 'PROP_143', now(), 'SC_20200730', 'L' 
	from documentoplantilla_dplp where cdpl_tipo = 'C' and cdpl_estado = 'A';--Cuenta

ALTER TABLE documentoplantilla_dplp
	DROP COLUMN cdpl_tipo;

CREATE OR REPLACE FUNCTION crear_bodegas()
 RETURNS void
 LANGUAGE plpgsql AS '
declare 
	_bodegas bodega_bodp;
begin
	if(select count(*) from bodega_bodp) !=0 then
		 INSERT INTO documentotransaccion_trap (ctra_llave, dtra_fecha, ctra_usuario) 
			values (''TRA-BODEGAS_TIPO'', now(), ''SYSTEM'');
		
		INSERT INTO consecutivo_conp(ccon_llave,  ccon_nombre,  ccon_prefijo, mcon_numeroinicial, mcon_numeroactual) 
			VALUES(''CON_BODEGAS'',''BODEGAS'',''BDG-'',100.00, 100.00);
		INSERT INTO documentoplantilla_dplp(cdpl_llave,  cdpl_nombre,  cdpl_consecutivo, cdpl_imagen,  cdpl_codigo, cdpl_objetivo)
			VALUES(''DPL_BODEGAS'',  ''BODEGAS'',  ''CON_BODEGAS'', ''http://golyat.cloud/imagenes/modulo.png'', ''BDG'', ''Registrar las bodegas de inventarios'');
		INSERT INTO documentoplantillacaracteristica_dpcp(cdpc_llave,  cdpc_plantilla,  ndpc_orden,  cdpc_nombre,  cdpc_codigo,  cdpc_formato,  cdpc_objetivo)
			VALUES(''DPL_BDG_1'',  ''DPL_BODEGAS'', 1, ''NOMBRE'',  ''NOMBRE'',  ''T'',  ''Almacenar el nombre'');
		INSERT INTO propiedad_ppdp(cppd_llave,  cppd_campo,  cppd_valor,  cppd_texto, cppd_propiedadvalor,  dppd_fechadefinicion,  cppd_cambiocreacion, cppd_tipo)
			VALUES(''DPL_BDG_1'',  ''DPL_BODEGAS'',  ''DPL_BDG_1'',  ''NOMBRE'', ''PROP_44'',  now(),  ''SC_20200730'', ''L'');
		INSERT INTO propiedad_ppdp(cppd_llave,  cppd_campo,  cppd_valor,  cppd_propiedadvalor,  dppd_fechadefinicion,  cppd_cambiocreacion, cppd_tipo)
			VALUES(''DPL_BDG_TIPO'',  ''DPL_BODEGAS'',  ''1'',  ''PROP_145'',  now(),  ''SC_20200730'', ''L'');
		
		FOR _bodegas in (select * from bodega_bodp)
	   	LOOP
		    INSERT INTO pedidoventa_pdvp (cpdv_llave, cpdv_funcionario, dpdv_fecha, dpdv_fecharegistro, cpdv_plantilla, cpdv_nombre, cpdv_transaccion, cpdv_estado)
		    values (substring(''BGS-'' || _bodegas.cbod_llave, 0 , 32),''SYSTEM'', now(), now(), ''DPL_BODEGAS'', _bodegas.cbod_codigo, ''TRA-BODEGAS_TIPO'', _bodegas.cbod_estado);

			INSERT INTO pedidoventacaracteristica_pvcp (cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, cpvc_transaccionregistro) 
			values(substring(''BGS-'' || _bodegas.cbod_llave, 0 , 32), substring(''BGS-'' || _bodegas.cbod_llave, 0 , 32), ''DPL_BDG_1'', _bodegas.cbod_nombre, ''TRA-BODEGAS_TIPO'');

			update bodega_bodp set cbod_documento = substring(''BGS-'' || _bodegas.cbod_llave, 0 , 32) where cbod_llave = _bodegas.cbod_llave;
		END LOOP;
	end if;
END;
';

SELECT crear_bodegas();

ALTER TABLE bodega_bodp
	ALTER COLUMN cbod_documento SET NOT NULL;

DROP FUNCTION crear_bodegas();

ALTER TABLE producto_prop
	ADD COLUMN cpro_documento character varying(32);

CREATE OR REPLACE FUNCTION crear_productos()
 RETURNS void
 LANGUAGE plpgsql AS '
declare 
	_productos producto_prop;
	_categorias categoriaproducto_cprp;
begin
	if(select count(*) from producto_prop where cpro_estado = ''A'') !=0 then

		 INSERT INTO documentotransaccion_trap (ctra_llave, dtra_fecha, ctra_usuario) 
			values (''TRA-PRODUCTOS_TIPO'', now(), ''SYSTEM'');

		FOR _categorias in (select * from categoriaproducto_cprp where ccpr_llave in (select cpro_categoria from producto_prop ))
	   	loop
	   		
		   	INSERT INTO consecutivo_conp(ccon_llave,  ccon_nombre, mcon_numeroinicial, mcon_numeroactual) 
		   		VALUES(substring(''T-'' || _categorias.ccpr_llave, 0 , 32), ''CATEGORIA '' || _categorias.ccpr_nombre ,100.00, 100.00);
			INSERT INTO documentoplantilla_dplp(cdpl_llave,  cdpl_nombre,  cdpl_consecutivo, cdpl_imagen,  cdpl_codigo, cdpl_objetivo)
				VALUES(substring(''T-'' || _categorias.ccpr_llave, 0 , 32),  _categorias.ccpr_nombre ,  substring(''T-'' || _categorias.ccpr_llave, 0 , 32), ''http://golyat.cloud/imagenes/modulo.png'', substring(_categorias.ccpr_nombre, 0, 16) , _categorias.ccpr_nombre);
			INSERT INTO documentoplantillacaracteristica_dpcp(cdpc_llave,  cdpc_plantilla,  ndpc_orden,  cdpc_nombre,  cdpc_codigo,  cdpc_formato,  cdpc_objetivo)
				VALUES(substring(''T-'' || _categorias.ccpr_llave, 0 , 32),  substring(''T-'' || _categorias.ccpr_llave, 0 , 32), 1, ''NOMBRE'',  ''NOMBRE'',  ''T'',  ''Almacenar el nombre'');
			INSERT INTO propiedad_ppdp(cppd_llave,  cppd_campo,  cppd_valor,  cppd_texto, cppd_propiedadvalor,  dppd_fechadefinicion,  cppd_cambiocreacion, cppd_tipo)
				VALUES(substring(''T-'' || _categorias.ccpr_llave, 0 , 32),  substring(''T-'' || _categorias.ccpr_llave, 0 , 32),  substring(''T-'' || _categorias.ccpr_llave, 0 , 32),  ''NOMBRE'', ''PROP_44'',  now(),  ''SC_20200730'', ''L'');
			INSERT INTO propiedad_ppdp(cppd_llave,  cppd_campo,  cppd_valor,  cppd_texto, cppd_propiedadvalor,  dppd_fechadefinicion,  cppd_cambiocreacion, cppd_tipo)
				VALUES(substring(''T-T_'' || _categorias.ccpr_llave, 0 , 32),  substring(''T-'' || _categorias.ccpr_llave, 0 , 32), _categorias.ccpr_llave, _categorias.ccpr_nombre, ''PROP_144'',  now(),  ''SC_20200730'', ''L'');
		
			FOR _productos in (select * from producto_prop where cpro_categoria = _categorias.ccpr_llave)
		   	LOOP
			    INSERT INTO pedidoventa_pdvp (cpdv_llave, cpdv_funcionario, dpdv_fecha, dpdv_fecharegistro, cpdv_plantilla, cpdv_nombre, cpdv_transaccion , cpdv_estado)
			    values (substring(''PRO-'' || _productos.cpro_llave, 0 , 32),''SYSTEM'',now(), now(), substring(''T-'' || _categorias.ccpr_llave, 0 , 32), _productos.cpro_codigo, ''TRA-PRODUCTOS_TIPO'', _productos.cpro_estado);
		   
				INSERT INTO pedidoventacaracteristica_pvcp (cpvc_llave, cpvc_documento, cpvc_campo, cpvc_valortext, cpvc_transaccionregistro) 
				values(substring(''PRO-'' || _productos.cpro_llave, 0 , 32), substring(''PRO-'' || _productos.cpro_llave, 0 , 32), substring(''T-'' || _categorias.ccpr_llave, 0 , 32), _productos.cpro_nombre, ''TRA-PRODUCTOS_TIPO'');
			
				update producto_prop set cpro_documento = substring(''PRO-'' || _productos.cpro_llave, 0 , 32) where cpro_llave = _productos.cpro_llave;
	
			END LOOP;
	
	   	end loop;

	end if;
END;
';
SELECT crear_productos();

DROP FUNCTION crear_productos();

ALTER TABLE producto_prop
	ALTER COLUMN cpro_documento SET NOT NULL;

ALTER TABLE documentoplantilla_dplp
	ALTER COLUMN cdpl_objetivo DROP NOT NULL;

ALTER TABLE bodega_bodp
	ADD CONSTRAINT fk_bodegadocumento FOREIGN KEY (cbod_documento) REFERENCES pedidoventa_pdvp(cpdv_llave);

ALTER TABLE producto_prop
	ADD CONSTRAINT fk_productodocumento FOREIGN KEY (cpro_documento) REFERENCES pedidoventa_pdvp(cpdv_llave);



