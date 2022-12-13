package com.softure.document_execution.infrastructure;


// BEGIN region interImport  
import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaFilterDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.java.domain.IBasicMapper;
import com.softure.property.domain.RelacionInternaDTO;

public interface PedidoVentaCaracteristicaMapper extends IBasicMapper<PedidoVentaCaracteristicaDTO, PedidoVentaCaracteristicaFilterDTO>{
	

// BEGIN region aditionalMethods  
	List<PedidoVentaCaracteristicaDTO> listar2Documento(@Param("documento")String documento, @Param("campo")String campo);
	List<PedidoVentaCaracteristicaDTO> listar2DocumentoHistorico(@Param("documento")String documento, @Param("campo")String campo);
	List<PedidoVentaCaracteristicaDTO> listarGestionables(@Param("documento")String documento);
	List<PedidoVentaCaracteristicaDTO> listar2DocumentoVisible(@Param("documentos") List<PedidoVentaDTO> documentos, @Param("historicos") List<PedidoVentaDTO> historicos);
	List<PedidoVentaCaracteristicaDTO> listar2getMessageMailDestiny(@Param("documentos") List<PedidoVentaCaracteristicaDTO> documentos, @Param("campoIds") List<RelacionInternaDTO> campoId);
	List<PedidoVentaCaracteristicaDTO> listar2getApiCode(@Param("documentos") List<PedidoVentaCaracteristicaDTO> documentos, @Param("campoIds") List<RelacionInternaDTO> campoId);
	List<PedidoVentaCaracteristicaDTO> listarParaReporte(@Param("documento")String documento);
	List<PedidoVentaCaracteristicaDTO> listarParaMensaje(@Param("documento") String documento, @Param("plantilla") String plantilla, @Param("propiedad") String propiedad, @Param("modificador") String modificador);
	List<PedidoVentaCaracteristicaDTO> listarParaGestor(@Param("documento") String documento, @Param("transaccion") String transaccion);
	BigDecimal calcularNumeroFuncion(@Param("sqlFuncionCalculo") String sqlFuncionDecision, @Param("documento") String documento, @Param("parametros") List<PedidoVentaCaracteristicaDTO> parametros);
	PedidoVentaCaracteristicaDTO consultarCampoCroquis(String nombreDocumento);
	List<PedidoVentaCaracteristicaDTO> consultarCamposOcupados(@Param("sqlFuncionCalculo") String sqlFuncionDecision, @Param("campoId")String campoId, @Param("parametros") List<PedidoVentaCaracteristicaDTO> parametros);
	String getTemplate(String documento);
	String valueFieldProcessMultipleToPartialDivideDocument(String field);
	String getUnique(PedidoVentaCaracteristicaDTO dto);
	PedidoVentaCaracteristicaDTO consultarSQLCampoGenerarDocumento(@Param("sqlFuncionCalculo") String sqlFuncionDecision, @Param("llaveTablaDocumento") String llaveTablaDocumento, @Param("llaveTablaModificador") String llaveTablaModificador);
	List<PedidoVentaCaracteristicaDTO> camposEspecialesPlantilla(@Param("sqlFuncion") String sqlFuncion, @Param("llaveTablaDocumento") String llaveTablaDocumento);
	
	PedidoVentaCaracteristicaDTO inactivarCampoHistorico(@Param("idCampo") String idCampo, @Param("transaccion") String transaccion, @Param("historico") String historico);
	PedidoVentaCaracteristicaDTO insertarHistorico(PedidoVentaCaracteristicaDTO dto);
// END region aditionalMethods
}