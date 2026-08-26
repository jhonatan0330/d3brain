package d3.document_execution.infrastructure;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.document_execution.domain.PedidoVentaCaracteristicaDTO;
import d3.document_execution.domain.PedidoVentaCaracteristicaFilterDTO;
import d3.document_execution.domain.PedidoVentaDTO;
import d3.java.domain.IBasicMapper;
import d3.property.domain.RelacionInternaDTO;

@D3SqlConnMapper(value = "PedidoVentaCaracteristicaMapper")
public interface PedidoVentaCaracteristicaMapper
		extends IBasicMapper<PedidoVentaCaracteristicaDTO, PedidoVentaCaracteristicaFilterDTO> {

	List<PedidoVentaCaracteristicaDTO> listar2Documento(@Param("documento") String documento,
			@Param("campo") String campo);

	List<PedidoVentaCaracteristicaDTO> listar2DocumentoHistorico(@Param("documento") String documento,
			@Param("campo") String campo);

	List<PedidoVentaCaracteristicaDTO> listarGestionables(@Param("documento") String documento);

	List<PedidoVentaCaracteristicaDTO> listar2DocumentoVisible(@Param("documentos") List<PedidoVentaDTO> documentos,
			@Param("historicos") List<PedidoVentaDTO> historicos);

	List<PedidoVentaCaracteristicaDTO> listar2getMessageMailDestiny(
			@Param("documentos") List<PedidoVentaCaracteristicaDTO> documentos,
			@Param("campoIds") List<RelacionInternaDTO> campoId);

	List<PedidoVentaCaracteristicaDTO> listarParaReporte(@Param("documento") String documento);

	List<PedidoVentaCaracteristicaDTO> listarParaMensaje(@Param("documento") String documento,
			@Param("plantilla") String plantilla, @Param("propiedad") String propiedad,
			@Param("modificador") String modificador);

	List<PedidoVentaCaracteristicaDTO> listarParaGestor(@Param("documento") String documento,
			@Param("transaccion") String transaccion);

	BigDecimal calcularNumeroFuncion(@Param("sqlFuncionCalculo") String sqlFuncionDecision,
			@Param("documento") String documento, @Param("token") String token,
			@Param("parametros") List<PedidoVentaCaracteristicaDTO> parametros);

	Date calcularFechaFuncion(@Param("sqlFuncionCalculo") String sqlFuncionDecision,
			@Param("documento") String documento, @Param("token") String token,
			@Param("parametros") List<PedidoVentaCaracteristicaDTO> parametros);

	PedidoVentaCaracteristicaDTO consultarCampoCroquis(String nombreDocumento);

	List<PedidoVentaCaracteristicaDTO> consultarCamposOcupados(@Param("sqlFuncionCalculo") String sqlFuncionDecision,
			@Param("campoId") String campoId, @Param("token") String token,
			@Param("parametros") List<PedidoVentaCaracteristicaDTO> parametros);

	String getTemplate(String documento);

	// String valueFieldProcessMultipleToPartialDivideDocument(String field);
	String getUnique(PedidoVentaCaracteristicaDTO dto);

	PedidoVentaCaracteristicaDTO consultarSQLCampoGenerarDocumento(
			@Param("sqlFuncionCalculo") String sqlFuncionDecision,
			@Param("llaveTablaDocumento") String llaveTablaDocumento,
			@Param("llaveTablaModificador") String llaveTablaModificador,
			@Param("llaveTablaIterador") String llaveTablaIterador);

	List<PedidoVentaCaracteristicaDTO> camposEspecialesPlantilla(@Param("sqlFuncion") String sqlFuncion,
			@Param("llaveTablaDocumento") String llaveTablaDocumento);

	PedidoVentaCaracteristicaDTO inactivarCampoHistorico(@Param("idCampo") String idCampo,
			@Param("transaccion") String transaccion, @Param("historico") String historico);

	PedidoVentaCaracteristicaDTO insertarHistorico(PedidoVentaCaracteristicaDTO dto);

	List<PedidoVentaCaracteristicaDTO> listar2getApiCode(
			@Param("documentos") List<PedidoVentaCaracteristicaDTO> documentos,
			@Param("campoIds") List<RelacionInternaDTO> campoId);

	String getCodeKeyOfTemplate(@Param("pTemplate") String pTemplate, @Param("pCode") String pCode);

	String getKeyOfDocumentBase(@Param("pCodeKey") String pCodeKey, @Param("pValue") String pValue);

	PedidoVentaCaracteristicaDTO getKeyToReplace(@Param("pKeyBase") String pKeyBase,
			@Param("pTemplate") String pTemplate, @Param("pCodeTemplate") String pCodeTemplate);

}