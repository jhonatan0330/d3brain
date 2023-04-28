package com.softure.tariff.application;

import java.util.List;

// BEGIN region interImport
import java.math.BigDecimal;

import com.softure.tariff.domain.TarifaDTO;
import com.softure.tariff.domain.TarifaFilterDTO;
import com.softure.tariff.domain.TarifarioDTO;
import com.softure.tariff.infrastructure.TarifaMapper;
import com.softure.document_execution.application.PedidoVentaSvc;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaFilterDTO;
import com.softure.inventory.application.ProductoSvc;
import com.softure.inventory.domain.ProductoDTO;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.services.SoftureUtil;
// END region interImport
import com.softure.logisticpymes.application.BasicSvc;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;

@Service("tarifaService")
public class TarifaSvc extends BasicSvc<TarifaDTO, TarifaFilterDTO> {
	
	@Autowired
	private TarifaMapper tarifaMapper;
	
	// BEGIN region servicesTarifa
	@Autowired private TarifarioSvc tarifarioService;
	@Autowired private ProductoSvc productoService;
	@Autowired private PedidoVentaSvc documentoService;	
	// END region servicesTarifa

	@Override
	public TarifaDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. Tarifa");
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
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public TarifaDTO actualizar( TarifaDTO dto, String token) throws ServerException {
		// BEGIN Tarifa_actualizar
		TarifaDTO existe  = validarTarifa(dto);
		if (existe!=null && existe.getLlaveTabla().compareTo(dto.getLlaveTabla())!=0) throw new ServerException("Existe una tarifa con las mimsas condiciones de tarifario, origen y destino activa, por favor revise su configuracion");
		TarifarioDTO tarifario = tarifarioService.consultaXId(dto.getTarifario());
		if(!tarifario.getRangoValores()) dto.setRangoPrecios(false);
		if(!dto.getRangoPrecios()){
			dto.setValorMinimo(dto.getValor());
			dto.setValorMaximo(dto.getValor());
		}
		if(!tarifario.getProductoOpcional() && dto.getProducto()==null) throw new ServerException("Seleccione el producto porfavor");
		if(!tarifario.getRangoCantidad()){
			dto.setCantidadMinima(0);
			dto.setCantidadMaxima(0);
		}/*else{
			if(dto.getCantidadMaxima().compareTo(1)==0 && dto.getCantidadMaxima().compareTo(dto.getCantidadMinima())==0){
				dto.setCantidadMinima(0);
				dto.setCantidadMaxima(0);
			}
		}*/
		return super.actualizar(dto, token);
		// END Tarifa_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public TarifaDTO inactivar(TarifaDTO dto, String token) throws ServerException {
		// BEGIN Tarifa_inactivar
		return super.inactivar(dto, token);
		// END Tarifa_inactivar
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
	public List<TarifaDTO> listarConsulta(TarifaFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public TarifaDTO guardar(TarifaDTO dto, String token) throws ServerException {
		// BEGIN Tarifa_guardar
		TarifarioDTO tarifario = tarifarioService.consultaXId(dto.getTarifario());
		//Para las cargas valido los codigos y el recurso
		if(dto.getProducto()==null &&dto.getProductoNombre()!=null) {
			ProductoDTO filtroProducto = productoService.filtrarPorCodigo(dto.getProductoNombre());
			if(filtroProducto==null) throw new ServerException("No se identifica producto con el codigo : " + dto.getProductoNombre());
			dto.setProducto(filtroProducto.getLlaveTabla());
		}
		if(tarifario.getTipoRecurso()!=null && dto.getRecurso()==null &&dto.getRecursoNombre()!=null) {
			PedidoVentaFilterDTO filtroDocumentoFilter = new PedidoVentaFilterDTO();
			filtroDocumentoFilter.setNombre(dto.getRecursoNombre());
			filtroDocumentoFilter.setPlantilla(tarifario.getTipoRecurso());
			PedidoVentaDTO filtroDocumento = documentoService.consultaUnica(filtroDocumentoFilter);
			if(filtroDocumento==null) throw new ServerException("No se identifica "+ tarifario.getTipoRecursoNombre() +" con el codigo : " + dto.getRecursoNombre());
			dto.setRecurso(filtroDocumento.getLlaveTabla());
		}
		if(tarifario.getTipoDimension2()!=null && dto.getDimension2()==null &&dto.getDimension2Nombre()!=null) {
			PedidoVentaFilterDTO filtroDocumentoFilter = new PedidoVentaFilterDTO();
			filtroDocumentoFilter.setNombre(dto.getDimension2Nombre());
			filtroDocumentoFilter.setPlantilla(tarifario.getTipoDimension2());
			PedidoVentaDTO filtroDocumento = documentoService.consultaUnica(filtroDocumentoFilter);
			if(filtroDocumento==null) throw new ServerException("No se identifica "+ tarifario.getTipoDimension2Nombre() +" con el codigo : " + dto.getDimension2Nombre());
			dto.setDimension2(filtroDocumento.getLlaveTabla());
		}
		
		if(tarifario.getTipoDimension3()!=null && dto.getDimension3()==null &&dto.getDimension3Nombre()!=null) {
			PedidoVentaFilterDTO filtroDocumentoFilter = new PedidoVentaFilterDTO();
			filtroDocumentoFilter.setNombre(dto.getDimension3Nombre());
			filtroDocumentoFilter.setPlantilla(tarifario.getTipoDimension3());
			PedidoVentaDTO filtroDocumento = documentoService.consultaUnica(filtroDocumentoFilter);
			if(filtroDocumento==null) throw new ServerException("No se identifica "+ tarifario.getTipoDimension3Nombre() +" con el codigo : " + dto.getDimension3Nombre());
			dto.setDimension3(filtroDocumento.getLlaveTabla());
		}
		
		if(tarifario.getTipoDimension4()!=null && dto.getDimension4()==null &&dto.getDimension4Nombre()!=null) {
			PedidoVentaFilterDTO filtroDocumentoFilter = new PedidoVentaFilterDTO();
			filtroDocumentoFilter.setNombre(dto.getDimension4Nombre());
			filtroDocumentoFilter.setPlantilla(tarifario.getTipoDimension4());
			PedidoVentaDTO filtroDocumento = documentoService.consultaUnica(filtroDocumentoFilter);
			if(filtroDocumento==null) throw new ServerException("No se identifica "+ tarifario.getTipoDimension4Nombre() +" con el codigo : " + dto.getDimension4Nombre());
			dto.setDimension4(filtroDocumento.getLlaveTabla());
		}
		
		if(!tarifario.getProductoOpcional() && dto.getProducto()==null) throw new ServerException("Seleccione el producto porfavor");
		if (validarTarifa(dto)!=null) throw new ServerException("Existe una tarifa con las mismas condiciones de tarifario, origen y destino activa, por favor revise su configuracion");
		//if(tarifario.getDeducciones() && dto.getDeduccion()!=null) throw new ServerException("El tarifario no recibe deducciones");
		if(!tarifario.getRangoValores()) dto.setRangoPrecios(false);
		if(!dto.getRangoPrecios()){
			dto.setValorMinimo(dto.getValor());
			dto.setValorMaximo(dto.getValor());
		}
		
		if(!tarifario.getRangoCantidad()){
			dto.setCantidadMinima(0);
			dto.setCantidadMaxima(0);
		}/*else{
			//if(dto.getCantidadMaxima().compareTo(1)==0 && dto.getCantidadMaxima().compareTo(dto.getCantidadMinima())==0){
				dto.setCantidadMinima(0);
				dto.setCantidadMaxima(0);
			//}
		}*/
		return super.guardar(dto, token);
		// END Tarifa_guardar
	}

// BEGIN region aditionalMethods
	public List<TarifaDTO> obtenerTarifa(TarifaFilterDTO dto)throws ServerException{
		List<TarifaDTO> resultado = null;
		if(dto.getRecurso()!=null){
			resultado = tarifaMapper.obtenerTarifa(dto);
			dto.setRecurso(null);
		}
		if(resultado==null){
			resultado = tarifaMapper.obtenerTarifa(dto);
		}else{
			resultado.addAll(tarifaMapper.obtenerTarifa(dto));
		}
		if(resultado!=null && !resultado.isEmpty()) {
			for(TarifaDTO iTarifa : resultado) {
				if(iTarifa.getRangoPrecios()) {
					if(iTarifa.getValorMinimo().compareTo(BigDecimal.ZERO)==0 && iTarifa.getValorMaximo().compareTo(BigDecimal.ZERO)==0) 
						iTarifa.setValorMaximo(new BigDecimal(Integer.MAX_VALUE));
				}else {
					iTarifa.setValorMaximo(iTarifa.getValor());
					iTarifa.setValorMinimo(iTarifa.getValor());	
				}
			}
		}
		return resultado;
	}
	
	public TarifaDTO validarTarifa(TarifaDTO dto)throws ServerException{
		//Por el momento solo valido que existaq en crear y actualizar
		TarifaFilterDTO existe = new TarifaFilterDTO();
		existe.setTarifario(dto.getTarifario());
		existe.setProducto(dto.getProducto());
		TarifarioDTO tarifario = tarifarioService.consultaXId(dto.getTarifario());
		if(tarifario.getRangoCantidad()){
			existe.setCantidadMinima(dto.getCantidadMinima());
			existe.setCantidadMaxima(dto.getCantidadMaxima());
			if(existe.getCantidadMaxima().compareTo(1)==0 && existe.getCantidadMaxima().compareTo(existe.getCantidadMinima())==0){
				existe.setCantidadMinima(0);
				existe.setCantidadMaxima(0);
			}
		}
		existe.setRecurso(dto.getRecurso());
		existe.setDimension2(dto.getDimension2());
		existe.setDimension3(dto.getDimension3());
		existe.setDimension4(dto.getDimension4());
		List<TarifaDTO> tarifa = tarifaMapper.obtenerTarifa(existe);
		if(tarifa==null || tarifa.isEmpty()) return null;
		if(tarifa.size()>1) throw new ServerException("Revisa porque al consultar la tarifa, se consultan varias respuestas");
		return tarifa.get(0);
	}

	
	public List<TarifaDTO> obtenerTarifaFuncion(String propiedad, ProductoDTO producto, List<PedidoVentaCaracteristicaDTO> parametros)throws ServerException{
		try {
			return tarifaMapper.obtenerTarifaFuncion(SoftureUtil.formatFunction(propiedad), producto.getLlaveTabla(), producto.getProductoBase(), parametros);
		} catch (Exception e) {
			throw new ServerException(e.getMessage(), "Funcion de tarifas : " + producto.getNombre());
		}
	}
	
	public List<TarifaDTO> getTarifas2Product(String productId)throws ServerException{
		TarifaFilterDTO t = new TarifaFilterDTO();
		t.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		t.setProducto(productId);
		return listarConsulta(t);
	}
	
// END region aditionalMethods

}