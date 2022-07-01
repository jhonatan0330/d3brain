
COMMENT ON TABLE usuario_usrp IS '2018-02-13';

CREATE OR REPLACE FUNCTION casos_prueba()
RETURNS TABLE (rol character, plantilla character, escenario character, resultado character, parametros character) AS $$
DECLARE
    roles rolacceso_racp;
	plantillas RECORD;
	campos documentoplantillacaracteristica_dpcp;
	producto RECORD;
	inventario documentoplantillainventario_dpip;
	tipo_movimiento_inventario varchar(100);
	reportes reportebase_rpbp;

begin 
	for roles in select * from rolacceso_racp where crac_estado = 'A' order by crac_codigo
	loop
		for plantillas in
			select * from documentoplantillarol_dprp, documentoplantilla_dplp
			where roles.crac_llave = cdpr_rol and cdpr_estado = 'A' and cdpl_llave = cdpr_plantilla and cdpl_estado = 'A'  
				and (bdpr_listable and (bdpr_crear or bdpr_modificar))-- or cdpl_tipo = 'R'
			order by cdpl_codigo
		loop
		for campos in 
			select * from documentoplantillacaracteristica_dpcp where cdpc_plantilla = plantillas.cdpl_llave and cdpc_estado= 'A' order by ndpc_orden
			loop
				rol := roles.crac_nombre;
				plantilla := plantillas.cdpl_nombre;
				parametros := null;
				if campos.bdpc_obligatorio then
					-- Pruebas de campos obligatorios
					escenario := 'Guardar un documento ' || plantillas.cdpl_nombre || ' sin valor en ' || campos.cdpc_nombre;
					resultado := 'Sale una validacion de que el campo ' || campos.cdpc_nombre || ' es obligatorio';
					return next;
				end if;
				case campos.cdpc_formato when 'J' then
						--Prueba de caracteristicas de seleccion
						select * into producto from producto_prop,  productocaracteristica_pcrp
						where cpcr_base = cpro_llave and cpro_categoria in (select cdpc_categoria from documentoplantillacategoria_dpcp where cdpc_plantilla = 'VENTA' and cdpc_estado = 'A') and cpro_estado = 'A'
							and cpcr_documentoauxiliar is not null 	ORDER BY random() limit 1;
						if producto.cpro_llave is not null then
						else
						end if;
						select * into inventario from documentoplantillainventario_dpip where cdpi_estado = 'A' and cdpi_plantilla = plantillas.cdpl_llave;
						if inventario is not null then
							if inventario.bdpi_entrada then tipo_movimiento_inventario = 'ENTRADA';
							else tipo_movimiento_inventario = 'SALIDA';
							end if;
							--Pruebas de campos sin manejo de inventarios
							select * into producto from producto_prop  where cpro_categoria in (select cdpc_categoria from documentoplantillacategoria_dpcp where cdpc_plantilla = plantillas.cdpl_llave and cdpc_estado = 'A')
								and cpro_llave not in (select cpin_producto from productoinventario_pinp where cpin_estado = 'A') and cpro_estado = 'A' ORDER BY random() limit 1;
							if producto.cpro_llave is not null then
								escenario := 'Guardar un documento ' || plantillas.cdpl_nombre || ' con ' || campos.cdpc_nombre || ' sin manejo de inventarios';
								resultado := 'Genera consecutivo de documento, no afecta inventarios';
								parametros := campos.cdpc_nombre || ' = ' || producto.cpro_nombre;
								return next;
							end if;
							
							select * into producto from producto_prop, productoinventario_pinp  where cpro_categoria in (select cdpc_categoria from documentoplantillacategoria_dpcp where cdpc_plantilla = plantillas.cdpl_llave and cdpc_estado = 'A')
								and cpro_llave = cpin_producto and cpin_estado = 'A' and cpro_estado = 'A' ORDER BY random() limit 1;
							--Pruebas de productos con inventarios insuficientes
							if producto.cpro_llave is not null then
								escenario := 'Guardar un documento ' || plantillas.cdpl_nombre || ' de un ' || campos.cdpc_nombre || ' con inventarios insuficientes';
								resultado := 'Sale una validaci�n de Producto ' || producto.cpro_nombre || ' insuficiente';
								parametros := campos.cdpc_nombre || ' = ' || producto.cpro_nombre || 'x(>=' || producto.mpin_cantidadactual || ')';
								return next;
							end if;
							select * into producto from producto_prop, productoinventario_pinp  where cpro_categoria in (select cdpc_categoria from documentoplantillacategoria_dpcp where cdpc_plantilla = plantillas.cdpl_llave and cdpc_estado = 'A')
								and cpro_llave = cpin_producto and cpin_estado = 'A' and mpin_cantidadactual !=0 and cpro_estado = 'A' ORDER BY random() limit 1;
							--Pruebas de productos con inventarios disponibles
							if producto.cpro_llave is not null then
								escenario := 'Guardar un documento ' || plantillas.cdpl_nombre || ' de un ' || campos.cdpc_nombre || ' con inventarios disponibles';
								resultado := 'Genera consecutivo de documento, afecta el inventario del producto ' || producto.cpro_nombre || ' de forma ' || tipo_movimiento_inventario;
								parametros := campos.cdpc_nombre || ' = ' || producto.cpro_nombre || 'x(<=' || producto.mpin_cantidadactual || ')';
								return next;
							end if;
							--Prueba de inventario de composicion
							select * into producto from producto_prop,  productocaracteristica_pcrp
							where cpcr_base = cpro_llave and cpro_categoria in (select cdpc_categoria from documentoplantillacategoria_dpcp where cdpc_plantilla = 'VENTA' and cdpc_estado = 'A') and cpro_estado = 'A'
								and cpcr_documentoauxiliar is not null 	ORDER BY random() limit 1;
							if producto.cpro_llave is not null then
							else
							end if;
						else
							select * into producto from producto_prop where cpro_categoria in (select cdpc_categoria from documentoplantillacategoria_dpcp where cdpc_plantilla = plantillas.cdpl_llave and cdpc_estado = 'A') and cpro_estado = 'A' ORDER BY random() limit 1;
							if producto.cpro_llave is not null then
								escenario := 'Guardar un documento ' || plantillas.cdpl_nombre || ' de un ' || campos.cdpc_nombre ;
								resultado := 'Genera consecutivo de documento';
								parametros := campos.cdpc_nombre || ' = ' || producto.cpro_nombre;
								return next;
							else
								escenario := 'Guardar un documento ' || plantillas.cdpl_nombre || ' de un ' || campos.cdpc_nombre ;
								resultado := 'Genera consecutivo de documento, no afecta inventarios';
								parametros := campos.cdpc_nombre || ' = <No hay>';
								return next;
							end if;
						end if;
						
						
					else
							
				end case;
			end loop;
				escenario := 'Guardar un documento ' || plantillas.cdpl_nombre || ' con parametros validos';
				select * into reportes from reportebase_rpbp where crpb_plantilla = plantillas.cdpl_llave and crpb_estado = 'A';
				if reportes.crpb_llave is not null then
					escenario := escenario || ' e imprime el reporte ' || reportes.crpb_nombre;
				end if;
				parametros := 'Validos';
				return next;
		end loop;
	end loop;
end; 
$$ LANGUAGE plpgsql;

update reportebase_rpbp set crpb_variables  = 'JASPERTIPO=XLS' where crpb_llave = 'CST003';
update reportebase_rpbp set crpb_variables  = 'JASPERTIPO=XLS' where crpb_llave = 'CST005';
ALTER TABLE movimiento_movp DROP CONSTRAINT fk_movimientoturno;

UPDATE movimiento_movp set cmov_turno  =null;
DELETE FROM turno_turp where ctur_estado  != 'E';

ALTER TABLE turno_turp
	ADD COLUMN ctur_documento character varying(32);

ALTER TABLE turno_turp
	ADD CONSTRAINT fk_turnodocumento FOREIGN KEY (ctur_documento) REFERENCES pedidoventa_pdvp(cpdv_llave);

ALTER TABLE movimiento_movp
  ADD CONSTRAINT fk_movimientoturno FOREIGN KEY (cmov_turno)
      REFERENCES turno_turp (ctur_llave) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
	  
--ALTER TABLE turno_turp
--	ALTER COLUMN ctur_documento SET NOT NULL;

INSERT INTO catalogo_catp (ccat_llave, ccat_tipo, ccat_codigo, ccat_nombre) VALUES ('INGxTRANSF', 'I', 'IxT','INGRESOS POR TRANSFERENCIA');
INSERT INTO cuentapermisousuario_cpup (ccpu_llave, ccpu_usuario, ccpu_cuenta, ccpu_tipo, ccpu_catalogo)
	select substring(substring(ccpu_usuario,0,16) || substring(ccpu_cuenta,0,16),0,32),ccpu_usuario, ccpu_cuenta, 'I', 'INGxTRANSF' from cuentapermisousuario_cpup where ccpu_estado = 'A' and ccpu_tipo = 'T' group by ccpu_usuario, ccpu_cuenta;

INSERT INTO plantillacampoparametro_pcpp (cpcp_llave, cpcp_campo, cpcp_key, cpcp_valor) VALUES ('CIERRE_CAJA_FILTRO', 'CAJA_CIERRE_CUENTA_T', 'CUENTA_CATALOGO_FILTRO', 'INGxTRANSF');

update documentoplantillacaracteristica_dpcp set cdpc_codigodepende  = 'CAJA_FUERTE' where cdpc_llave = 'CAJA_CIERRE_VALOR_T';
INSERT INTO plantillacampoparametro_pcpp (cpcp_llave, cpcp_campo, cpcp_key, cpcp_valor) VALUES ('CIERRE_CAJA_FORMULA', 'CAJA_CIERRE_VALOR_T', 'NUMERO_FORMULA', 'CAJA_FUERTE');


CREATE OR REPLACE FUNCTION upsert_reporte(
    llave character,
    nombre character,
    codigo character,
    formato character,
    variables character,
    texto character)
  RETURNS void AS
$BODY$ 
DECLARE 
BEGIN 
    UPDATE reportebase_rpbp SET crpb_nombre = nombre, crpb_jaspertext = texto WHERE crpb_llave = llave; 
    /*IF NOT FOUND THEN 
    INSERT INTO consecutivo_conp(ccon_llave, ccon_nombre, ccon_prefijo, mcon_numeroinicial, mcon_numerofinal, mcon_numeroactual)
    	VALUES (llave, llave, llave || '-', 100, 99999999, 100);
	INSERT INTO documentoplantilla_dplp(cdpl_llave, cdpl_nombre, cdpl_consecutivo, cdpl_imagen, cdpl_codigo, cdpl_tipo)
    	VALUES (llave, nombre,  llave, 'http://golyat.cloud/imagenes/modulo.png', llave, 'R');
	INSERT INTO reportebase_rpbp (crpb_llave, crpb_nombre, crpb_jaspertext, crpb_plantilla) values (llave, nombre, texto, llave);
	INSERT INTO documentoplantillarol_dprp(cdpr_llave, cdpr_plantilla, cdpr_rol, bdpr_iniciorapido, bdpr_rangofiltro, bdpr_crear)
    	select substring(llave ||crac_llave,1,32), llave, crac_llave, true, true, true from rolacceso_racp where brac_permisoscompletos = true; 
    END IF;*/
END; 
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION upsert_reporte(character, character, character, character, character, character)
  OWNER TO postgres;