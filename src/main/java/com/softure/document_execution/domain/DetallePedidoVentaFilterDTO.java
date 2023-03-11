package com.softure.document_execution.domain;

// BEGIN region interImport
// END region interImport

import org.apache.ibatis.type.Alias;

import com.softure.java.domain.BasicFilterDTO;

import lombok.Getter;
import lombok.Setter;
@Alias("DetallePedidoVentaFilterDTO")
@Getter
@Setter
public class DetallePedidoVentaFilterDTO extends BasicFilterDTO
{

	private String documento;
	private String producto;
	private String productoTercero;
	private String productoCodigo;
	private String productoImagen;
	private String productoDocumento;
	private String nombre;
	private Integer cantidadPromocion;
	private Integer cantidadPromocionBase;
	private String plantilla;
	private String transaccionRegistro;
	private String transaccionInactivo;
	private String campo;


}