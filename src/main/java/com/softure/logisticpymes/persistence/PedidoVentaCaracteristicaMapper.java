package com.softure.logisticpymes.persistence;


// BEGIN region interImport  
import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.softure.logisticpymes.dto.PedidoVentaDTO;
import com.softure.logisticpymes.dto.RelacionInternaDTO;
// END region interImport
import com.softure.logisticpymes.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.dto.filter.PedidoVentaCaracteristicaFilterDTO;

public interface PedidoVentaCaracteristicaMapper extends IBasicMapper<PedidoVentaCaracteristicaDTO, PedidoVentaCaracteristicaFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<PedidoVentaCaracteristicaDTO> listar2Documento(@Param("documento")String documento);
	List<PedidoVentaCaracteristicaDTO> listarGestionables(@Param("documento")String documento);
	List<PedidoVentaCaracteristicaDTO> listar2DocumentoVisible(@Param("documentos") List<PedidoVentaDTO> documentos);
	List<PedidoVentaCaracteristicaDTO> listar2getMessageMailDestiny(@Param("documentos") List<PedidoVentaCaracteristicaDTO> documentos, @Param("campoIds") List<RelacionInternaDTO> campoId);
	List<PedidoVentaCaracteristicaDTO> listarParaReporte(@Param("documento")String documento);
	List<PedidoVentaCaracteristicaDTO> listarParaMensaje(@Param("documento") String documento, @Param("plantilla") String plantilla, @Param("propiedad") String propiedad, @Param("modificador") String modificador);
	BigDecimal calcularNumeroFuncion(@Param("sqlFuncionCalculo") String sqlFuncionDecision, @Param("documento") String documento, @Param("parametros") List<PedidoVentaCaracteristicaDTO> parametros);
	PedidoVentaCaracteristicaDTO consultarCampoCroquis(String nombreDocumento);
	String getTemplate(String documento);
	String getUnique(PedidoVentaCaracteristicaDTO dto);
// END region aditionalMethods
}