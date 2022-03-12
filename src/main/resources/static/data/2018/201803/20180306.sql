
COMMENT ON TABLE usuario_usrp IS '2018-03-06';

COMMENT ON TABLE usuariosesion_ussp IS '2018.03.06.10';

ALTER TABLE documentoplantillacosto_dpcp
	ADD COLUMN cdpc_afectasaldo character varying(1);

UPDATE documentoplantillacosto_dpcp SET cdpc_afectasaldo = 'S' where bdpc_saldo;

UPDATE documentoplantillacosto_dpcp SET cdpc_afectasaldo = 'R' where cdpc_campoafectasaldo is not null;

ALTER TABLE documentoplantillacosto_dpcp
	DROP COLUMN bdpc_saldo;

ALTER TABLE pedidoventadinero_pvdp
	ADD COLUMN dpvd_fecha timestamp with time zone;

update pedidoventadinero_pvdp set dpvd_fecha = (select dpdv_fecharegistro from pedidoventa_pdvp where cpdv_llave =cpvd_documento);

ALTER TABLE pedidoventadinero_pvdp
	ALTER COLUMN dpvd_fecha SET NOT NULL;


CREATE OR REPLACE FUNCTION casos_prueba() RETURNS TABLE(rol character, plantilla character, escenario character, resultado character, parametros character)
    LANGUAGE plpgsql
    AS $$
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
					-- TEST: Pruebas de campos obligatorios
					escenario := 'Guardar un documento ' || plantillas.cdpl_nombre || ' sin valor en ' || campos.cdpc_nombre;
					resultado := 'Sale un mensaje indicando: el campo ' || campos.cdpc_nombre || ' es obligatorio';
					return next;
				end if;
				case campos.cdpc_formato when 'J' then
						--TEST:Prueba de caracteristicas de seleccion
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
							--TEST: Pruebas de campos sin manejo de inventarios
							select * into producto from producto_prop  where cpro_categoria in (select cdpc_categoria from documentoplantillacategoria_dpcp where cdpc_plantilla = plantillas.cdpl_llave and cdpc_estado = 'A')
								and cpro_llave not in (select cpid_producto from productoinventariodescuento_pidp where cpid_estado  = 'A') 
								and cpro_llave not in (select cpin_producto from productoinventario_pinp where cpin_estado = 'A') and cpro_estado = 'A' ORDER BY random() limit 1;
							if producto.cpro_llave is not null then
								escenario := 'Guardar un documento ' || plantillas.cdpl_nombre || ' con ' || campos.cdpc_nombre || ' sin manejo de inventarios';
								resultado := 'Genera consecutivo de documento, no afecta inventarios';
								parametros := campos.cdpc_nombre || ' = ' || producto.cpro_nombre;
								return next;
							end if;
							
							select * into producto from producto_prop, productoinventario_pinp  where cpro_categoria in (select cdpc_categoria from documentoplantillacategoria_dpcp where cdpc_plantilla = plantillas.cdpl_llave and cdpc_estado = 'A')
								and cpro_llave = cpin_producto and cpin_estado = 'A' and cpro_estado = 'A' ORDER BY random() limit 1;
							--TEST: Pruebas de productos con inventarios insuficientes
							if producto.cpro_llave is not null then
								escenario := 'Guardar un documento ' || plantillas.cdpl_nombre || ' de un ' || campos.cdpc_nombre || ' con inventarios insuficientes';
								resultado := 'Sale una validacion de Producto ' || producto.cpro_nombre || ' insuficiente';
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
				resultado := 'Genera consecutivo de documento';
				parametros := 'Validos';
				return next;
		end loop;
	end loop;
end; 
$$;
