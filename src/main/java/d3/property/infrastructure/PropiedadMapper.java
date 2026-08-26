package d3.property.infrastructure;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import d3.D3SqlConnMapper;
import d3.document_execution.domain.PedidoVentaCaracteristicaDTO;
import d3.inventory.domain.ProductoDTO;
import d3.java.domain.IBasicMapper;
import d3.property.domain.PropiedadDTO;
import d3.property.domain.PropiedadFilterDTO;

@D3SqlConnMapper(value = "PropiedadMapper")
public interface PropiedadMapper extends IBasicMapper<PropiedadDTO, PropiedadFilterDTO> {

	String funcionAsignacion(@Param("estado") String estado, @Param("documento") String documento,
			@Param("modificador") String modificador, @Param("token") String token);

	void funcionPrevalidacionPlantilla(@Param("funcion") String funcion, @Param("documento") String documento,
			@Param("token") String token, @Param("campos") List<PedidoVentaCaracteristicaDTO> campos);

	// La idea es cambiar las funciones a que respondan string y no se muestre en bd
	String funcionPrevalidacionPlantillaReturnString(@Param("funcion") String funcion,
			@Param("documento") String documento, @Param("token") String token,
			@Param("campos") List<PedidoVentaCaracteristicaDTO> campos);

	void funcionPrevalidateAPI(@Param("funcion") String funcion, @Param("documento") String documento,
			@Param("modificador") String modificador, @Param("extracciones") String extracciones);

	void crearFuncion(PropiedadDTO dto);

	void crearFuncionMail(PropiedadDTO dto);

	void crearFuncionFiltros(PropiedadDTO dto);

	void crearFuncionProductos(PropiedadDTO dto);

	void crearFuncionDecision(PropiedadDTO dto);

	void crearFuncionIteracion(PropiedadDTO dto);

	void crearFuncionTarifas(PropiedadDTO dto);

	void crearFuncionNumerica(PropiedadDTO dto);

	void crearFuncionFecha(PropiedadDTO dto);

	void crearFuncionParametros(PropiedadDTO dto);

	void crearFuncionCampoGenerar(PropiedadDTO dto);

	void crearFuncionCamposEspecialesPlantilla(PropiedadDTO dto);

	void crearFuncionAutorizacion(PropiedadDTO dto);

	void crearFuncionPrevalidacion(PropiedadDTO dto);

	// Para mejorar los errores en BD
	void crearFuncionPrevalidacionReturnString(PropiedadDTO dto);

	void crearFuncionPrevalidateAPI(PropiedadDTO dto);

	// void eliminarFuncionPrevalidateAPI(PropiedadDTO dto);
	void eliminarFuncionPrevalidacion(PropiedadDTO dto);

	void eliminarFuncionCamposEspecialesPlantilla(PropiedadDTO dto);

	void eliminarFuncionCampoGenerar(PropiedadDTO dto);

	void eliminarFuncionNumerica(PropiedadDTO dto);

	void eliminarFuncionTarifas(PropiedadDTO dto);

	void eliminarFuncionDecision(PropiedadDTO dto);

	void eliminarFuncionIteracion(PropiedadDTO dto);

	void eliminarFuncion(PropiedadDTO dto);

	void eliminarFuncionFiltros(PropiedadDTO dto);

	void eliminarFuncionProductos(PropiedadDTO dto);

	void actualizarValorPropiedad(PropiedadDTO dto);

	List<PropiedadDTO> consultarTemporizadoresPendientes();

	List<PropiedadDTO> listarProductoSimplificado(@Param("productos") List<ProductoDTO> productos);

	List<PropiedadDTO> getFullPropertiesToConfiguration();

	List<PropiedadDTO> getTemplateWithoutUpdate();

	PropiedadDTO getByIdWithType(@Param("key") String key);

}