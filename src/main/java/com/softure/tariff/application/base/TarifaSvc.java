package com.softure.tariff.application.base;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.document_execution.application.CallDocumentListWithFilters;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaFilterDTO;
import com.softure.inventory.application.ProductoSvc;
import com.softure.inventory.domain.ProductoDTO;
import com.softure.inventory.domain.ProductoFilterDTO;
import com.softure.java.services.SoftureUtil;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.process_form.application.CallSearchProcessFromText;
import com.softure.tariff.domain.TarifaDTO;
import com.softure.tariff.domain.TarifaFilterDTO;
import com.softure.tariff.domain.TarifarioDTO;
import com.softure.tariff.infrastructure.TarifaMapper;

import jakarta.annotation.PostConstruct;

@Service("tarifaService")
public class TarifaSvc extends BasicSvc<TarifaDTO, TarifaFilterDTO> {

	@Autowired @Lazy 
	private TarifaMapper tarifaMapper;

	@Autowired @Lazy 
	private TarifarioService tarifarioService;
	@Autowired @Lazy 
	private ProductoSvc productoService;
	@Autowired @Lazy 
	private CallDocumentListWithFilters listDocumentWithFiltersFunction;
	@Autowired @Lazy 
	private CallSearchProcessFromText searchDocumentService;

	@Override
	public TarifaDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. Tarifa");
		TarifaFilterDTO dto = new TarifaFilterDTO();
		dto.setLlaveTabla(llave);
		return tarifaMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = tarifaMapper;
	}

	@Override
	public TarifaDTO activar(TarifaDTO dto, String token) throws ServerException {
		// BEGIN Tarifa_activar
		return super.activar(dto, token);
		// END Tarifa_activar
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public TarifaDTO actualizar(TarifaDTO dto, String token) throws ServerException {
		clean(dto);
		if(dto.getLlaveTabla()==null) throw new ServerException("No podemos actualizar una tarifa sin su id");
		TarifaDTO existe = validarTarifa(dto);
		if (existe != null && existe.getLlaveTabla().compareTo(dto.getLlaveTabla()) != 0)
			throw new ServerException(
					"Existe una tarifa con las mimsas condiciones de tarifario, origen y destino activa, por favor revise su configuracion");
		TarifarioDTO tarifario = tarifarioService.getById(dto.getTarifario());
		if (!tarifario.getRangoValores())
			dto.setRangoPrecios(false);
		if (!dto.getRangoPrecios()) {
			dto.setValorMinimo(dto.getValor());
			dto.setValorMaximo(dto.getValor());
		}
		if (!tarifario.getProductoOpcional() && dto.getProducto() == null)
			throw new ServerException("Seleccione el producto porfavor");
		if (!tarifario.getRangoCantidad()) {
			dto.setCantidadMinima(0);
			dto.setCantidadMaxima(0);
		} /*
			 * else{ if(dto.getCantidadMaxima().compareTo(1)==0 &&
			 * dto.getCantidadMaxima().compareTo(dto.getCantidadMinima())==0){
			 * dto.setCantidadMinima(0); dto.setCantidadMaxima(0); } }
			 */
		TarifaDTO tariff = consultaXId(dto.getLlaveTabla());
		tariff.setEstado(SharedConstants.STATE_INACTIVE);
		tariff.setUpdatedAt(new Date());
		tariff.setUpdatedUser(getUserFlex(token));
		super.actualizar(tariff, token);
		// Para el historial
		dto.setLlaveTabla(null);
		dto.setCreatedUser(getUserFlex(token));
		return super.guardar(dto, token);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public TarifaDTO inactivar(TarifaDTO dto, String token) throws ServerException {
		return super.inactivar(dto, token);
	}

	@Override
	public TarifaDTO consultaUnica(TarifaFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}

	@Override
	public int contarResultados(TarifaFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}

	@Override
	public List<TarifaDTO> listarConsulta(TarifaFilterDTO dto) throws ServerException {
		return super.listarConsulta(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public TarifaDTO guardar(TarifaDTO dto, String token) throws ServerException {
		clean(dto);
		TarifarioDTO tarifario = tarifarioService.getById(dto.getTarifario());
		// Para las cargas valido los codigos y el recurso
		if (dto.getProducto() == null && dto.getProductoNombre() != null) {
			ProductoDTO filtroProducto = productoService.filtrarPorCodigo(dto.getProductoNombre());
			if (filtroProducto == null)
				throw new ServerException("No se identifica producto con el codigo : " + dto.getProductoNombre());
			dto.setProducto(filtroProducto.getLlaveTabla());
		} else {
			if(dto.getProducto()!=null) {
				ProductoDTO productoById = productoService.consultaXId(dto.getProducto());
				if(productoById==null) {
					ProductoFilterDTO filterOne = new ProductoFilterDTO();
					filterOne.setDocumento(dto.getProducto());
					productoById = productoService.consultaUnica(filterOne);
					if(productoById!=null) dto.setProducto(productoById.getLlaveTabla());
				}
			}
		}
		if (tarifario.getTipoRecurso() != null && dto.getRecurso() == null && dto.getRecursoNombre() != null)
			dto.setRecurso(getDocumentToDimension(dto.getRecursoNombre(), tarifario.getTipoRecurso(),
					tarifario.getTipoRecursoNombre(), token));

		if (tarifario.getTipoDimension2() != null && dto.getDimension2() == null && dto.getDimension2Nombre() != null)
			dto.setDimension2(getDocumentToDimension(dto.getDimension2Nombre(), tarifario.getTipoDimension2(),
					tarifario.getTipoDimension2Nombre(), token));

		if (tarifario.getTipoDimension3() != null && dto.getDimension3() == null && dto.getDimension3Nombre() != null)
			dto.setDimension3(getDocumentToDimension(dto.getDimension3Nombre(), tarifario.getTipoDimension3(),
					tarifario.getTipoDimension3Nombre(), token));

		if (tarifario.getTipoDimension4() != null && dto.getDimension4() == null && dto.getDimension4Nombre() != null)
			dto.setDimension4(getDocumentToDimension(dto.getDimension4Nombre(), tarifario.getTipoDimension4(),
					tarifario.getTipoDimension4Nombre(), token));

		if (!tarifario.getProductoOpcional() && dto.getProducto() == null)
			throw new ServerException("Seleccione el producto porfavor");
		if (validarTarifa(dto) != null)
			throw new ServerException(
					"Existe una tarifa con las mismas condiciones de tarifario, origen y destino activa, por favor revise su configuracion");
		// if(tarifario.getDeducciones() && dto.getDeduccion()!=null) throw new
		// ServerException("El tarifario no recibe deducciones");
		if (!tarifario.getRangoValores())
			dto.setRangoPrecios(false);
		if (!dto.getRangoPrecios()) {
			dto.setValorMinimo(dto.getValor());
			dto.setValorMaximo(dto.getValor());
		}

		if (!tarifario.getRangoCantidad()) {
			dto.setCantidadMinima(0);
			dto.setCantidadMaxima(0);
		} /*
			 * else{ //if(dto.getCantidadMaxima().compareTo(1)==0 &&
			 * dto.getCantidadMaxima().compareTo(dto.getCantidadMinima())==0){
			 * dto.setCantidadMinima(0); dto.setCantidadMaxima(0); //} }
			 */
		dto.setCreatedUser(getUserFlex(token));
		return super.guardar(dto, token);

	}

	private void clean(TarifaDTO dto) {
		if(dto.getCantidadMaxima()==null) dto.setCantidadMaxima(0);
		if(dto.getCantidadMinima()==null) dto.setCantidadMinima(0);
		if(dto.getValorMaximo()==null) dto.setValorMaximo(BigDecimal.ZERO);
		if(dto.getValorMinimo()==null) dto.setValorMinimo(BigDecimal.ZERO);
		if(dto.getValor()==null) dto.setValor(BigDecimal.ZERO);
		if(dto.getTotalMinimo()==null) dto.setTotalMinimo(BigDecimal.ZERO);
		if(dto.getProducto()!=null && dto.getProducto().isEmpty()) dto.setProducto(null);
		if(dto.getRecurso()!=null && dto.getRecurso().isEmpty()) dto.setRecurso(null);
		if(dto.getDimension2()!=null && dto.getDimension2().isEmpty()) dto.setDimension2(null);
		if(dto.getDimension3()!=null && dto.getDimension3().isEmpty()) dto.setDimension3(null);
		if(dto.getDimension4()!=null && dto.getDimension4().isEmpty()) dto.setDimension4(null);
		if(dto.getProductoNombre()!=null && dto.getProductoNombre().isEmpty()) dto.setProductoNombre(null);
		if(dto.getRecursoNombre()!=null && dto.getRecursoNombre().isEmpty()) dto.setRecursoNombre(null);
		if(dto.getDimension2Nombre()!=null && dto.getDimension2Nombre().isEmpty()) dto.setDimension2Nombre(null);
		if(dto.getDimension3Nombre()!=null && dto.getDimension3Nombre().isEmpty()) dto.setDimension3Nombre(null);
		if(dto.getDimension4Nombre()!=null && dto.getDimension4Nombre().isEmpty()) dto.setDimension4Nombre(null);
	}

	private String getDocumentToDimension(String filter, String dimensionTemplate, String dimensionName, String token)
			throws ServerException {
		PedidoVentaFilterDTO filtroDocumentoFilter = new PedidoVentaFilterDTO();
		filtroDocumentoFilter.setFiltroParametro(filter);
		filtroDocumentoFilter.setPlantilla(dimensionTemplate);
		filtroDocumentoFilter.setSecurityToken(token);
		filtroDocumentoFilter.setEstado(SharedConstants.STATE_ACTIVE);
		List<PedidoVentaDTO> filtroDocumento = listDocumentWithFiltersFunction.listarAvanzado(filtroDocumentoFilter);
		if (filtroDocumento == null || filtroDocumento.isEmpty())
			throw new ServerException("No se identifica " + dimensionName + " con el codigo : " + filter);
		String keyDocument = searchDocumentService.getDocumentFromManyResults(filter, filtroDocumento);
		if (keyDocument == null)
			throw new ServerException("Existen muchos " + dimensionName + " con el codigo : " + filter);
		return keyDocument;
	}

// BEGIN region aditionalMethods
	public List<TarifaDTO> obtenerTarifa(TarifaFilterDTO dto) throws ServerException {
		List<TarifaDTO> resultado = null;
		if (dto.getRecurso() != null) {
			resultado = tarifaMapper.obtenerTarifa(dto);
			dto.setRecurso(null);
		}
		if (resultado == null) {
			resultado = tarifaMapper.obtenerTarifa(dto);
		} else {
			resultado.addAll(tarifaMapper.obtenerTarifa(dto));
		}
		if (resultado != null && !resultado.isEmpty()) {
			for (TarifaDTO iTarifa : resultado) {
				if (iTarifa.getRangoPrecios()) {
					if (iTarifa.getValorMinimo().compareTo(BigDecimal.ZERO) == 0
							&& iTarifa.getValorMaximo().compareTo(BigDecimal.ZERO) == 0)
						iTarifa.setValorMaximo(new BigDecimal(Integer.MAX_VALUE));
				} else {
					iTarifa.setValorMaximo(iTarifa.getValor());
					iTarifa.setValorMinimo(iTarifa.getValor());
				}
			}
		}
		return resultado;
	}

	public TarifaDTO validarTarifa(TarifaDTO dto) throws ServerException {
		// Por el momento solo valido que existaq en crear y actualizar
		TarifaFilterDTO existe = new TarifaFilterDTO();
		existe.setTarifario(dto.getTarifario());
		existe.setProducto(dto.getProducto());
		TarifarioDTO tarifario = tarifarioService.getById(dto.getTarifario());
		if (tarifario.getRangoCantidad()) {
			existe.setCantidadMinima(dto.getCantidadMinima());
			existe.setCantidadMaxima(dto.getCantidadMaxima());
			if (existe.getCantidadMaxima().compareTo(1) == 0
					&& existe.getCantidadMaxima().compareTo(existe.getCantidadMinima()) == 0) {
				existe.setCantidadMinima(0);
				existe.setCantidadMaxima(0);
			}
			if(existe.getCantidadMaxima().compareTo(0)==0) existe.setCantidadMaxima(Integer.MAX_VALUE);
		}
		existe.setRecurso(dto.getRecurso());
		existe.setDimension2(dto.getDimension2());
		existe.setDimension3(dto.getDimension3());
		existe.setDimension4(dto.getDimension4());
		existe.setEstado(SharedConstants.STATE_ACTIVE);
		List<TarifaDTO> tarifa = tarifaMapper.obtenerTarifa(existe);
		if (tarifa == null || tarifa.isEmpty())
			return null;
		if (tarifa.size() > 1)
			throw new ServerException("Revisa porque al consultar la tarifa, se consultan varias respuestas");
		return tarifa.get(0);
	}

	public List<TarifaDTO> obtenerTarifaFuncion(String propiedad, ProductoDTO producto,
			List<PedidoVentaCaracteristicaDTO> parametros) throws ServerException {
		try {
			return tarifaMapper.obtenerTarifaFuncion(SoftureUtil.formatFunction(propiedad), producto.getLlaveTabla(),
					producto.getProductoBase(), parametros);
		} catch (Exception e) {
			throw new ServerException(e.getMessage(), "Funcion de tarifas : " + producto.getNombre());
		}
	}

	public List<TarifaDTO> getTarifas2Product(String productId) throws ServerException {
		TarifaFilterDTO t = new TarifaFilterDTO();
		t.setEstado(SharedConstants.STATE_ACTIVE);
		t.setProducto(productId);
		return listarConsulta(t);
	}

// END region aditionalMethods

}