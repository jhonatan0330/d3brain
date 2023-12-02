package com.softure.inventory.application;

import java.util.List;

// BEGIN region interImport
import java.util.ArrayList;
import java.util.Date;
import java.math.BigDecimal;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import com.softure.document_execution.application.PedidoVentaSvc;
import com.softure.document_execution.application.field.AuxiliarProcesoBodega;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.inventory.domain.DeduccionProductoDTO;
import com.softure.inventory.domain.DeduccionProductoFilterDTO;
import com.softure.inventory.domain.TrazabilidadProductoInventarioDTO;
import com.softure.inventory.domain.TrazabilidadProductoInventarioFilterDTO;
import com.softure.inventory.infrastructure.DeduccionProductoMapper;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.logisticpymes.application.BasicSvc;

@Service("deduccionProductoService")
public class DeduccionProductoSvc extends BasicSvc<DeduccionProductoDTO, DeduccionProductoFilterDTO> {
	
	@Autowired
	private DeduccionProductoMapper deduccionProductoMapper;
	
	// BEGIN region servicesDeduccionProducto
	@Autowired private TrazabilidadProductoInventarioSvc trazabilidadProductoInventarioService;
	@Autowired private PedidoVentaSvc pedidoService;
	@Autowired private AuxiliarProcesoBodega tipoBodega;
	@Autowired private PropiedadSvc propiedadService;
	// END region servicesDeduccionProducto

	@Override
	public DeduccionProductoDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. DeduccionProducto");
		DeduccionProductoFilterDTO dto = new DeduccionProductoFilterDTO();
		dto.setLlaveTabla(llave);
		return deduccionProductoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = deduccionProductoMapper;
	}
	
	@Override
	public DeduccionProductoDTO activar(DeduccionProductoDTO dto, String token) throws ServerException {
		// BEGIN DeduccionProducto_activar
		return super.activar(dto, token);
		// END DeduccionProducto_activar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DeduccionProductoDTO actualizar( DeduccionProductoDTO dto, String token) throws ServerException {
		// BEGIN DeduccionProducto_actualizar
		return super.actualizar(dto, token);
		// END DeduccionProducto_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DeduccionProductoDTO inactivar(DeduccionProductoDTO dto, String token) throws ServerException {
		// BEGIN DeduccionProducto_inactivar
		dto = super.inactivar(dto, token);
		TrazabilidadProductoInventarioFilterDTO trazabilidadFilter = new TrazabilidadProductoInventarioFilterDTO();
		trazabilidadFilter.setDeduccionProducto(dto.getLlaveTabla());
		List<TrazabilidadProductoInventarioDTO>trazas = trazabilidadProductoInventarioService.listarConsulta(trazabilidadFilter);
		if(trazas!=null && trazas.size()!=0){
			TrazabilidadProductoInventarioDTO trazabilidad = new TrazabilidadProductoInventarioDTO();
			trazabilidad.setProducto(dto.getProducto());
			trazabilidad.setCantidad(BigDecimal.ZERO);
			trazabilidad.setBodega(dto.getBodega());
			for (TrazabilidadProductoInventarioDTO trazabilidadProductoInventarioDTO : trazas) {
				trazabilidad.setCantidad(trazabilidad.getCantidad().add(trazabilidadProductoInventarioDTO.getCantidad().negate()));
			}
			trazabilidad.setDeduccionProducto(dto.getLlaveTabla());
			trazabilidadProductoInventarioService.guardar(trazabilidad, token);
		}
		return dto;
		// END DeduccionProducto_inactivar
	}
	
	@Override
	public DeduccionProductoDTO consultaUnica(DeduccionProductoFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(DeduccionProductoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<DeduccionProductoDTO> listarConsulta(DeduccionProductoFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	public DeduccionProductoDTO guardar(DeduccionProductoDTO dto, String token) throws ServerException {
		// BEGIN DeduccionProducto_guardar
		if(dto.getFecha()==null) dto.setFecha(new Date());
		if(dto.getCantidad() == null || dto.getCantidad().compareTo(BigDecimal.ZERO)==0) throw new ServerException("No se puede realizar una deduccion sin cantidad");
		dto = super.guardar(dto, token);
		TrazabilidadProductoInventarioDTO trazabilidad = new TrazabilidadProductoInventarioDTO();
		trazabilidad.setProducto(dto.getProducto());
		trazabilidad.setCantidad(dto.getCantidad());
		trazabilidad.setBodega(dto.getBodega());
		trazabilidad.setDeduccionProducto(dto.getLlaveTabla());
		trazabilidad = trazabilidadProductoInventarioService.guardar(trazabilidad, token);
		return dto;
		// END DeduccionProducto_guardar
	}

// BEGIN region aditionalMethods
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public void recalcularInventarioDocumento(String documento, String token) throws ServerException {
		PedidoVentaDTO expediente = pedidoService.obtenerCamposCompletos(pedidoService.consultaXId(documento), token);
		//2. Coloco los dependientes//Actualizar dependencias despues de los camps para que queden completas asi el campo este despues en orden
		for (PedidoVentaCaracteristicaDTO campoDocumento : expediente.getCaracteristicas()) {
			campoDocumento.getCampoDTO().setPropiedades(propiedadService.obtenerPropiedades(PropiedadValorDefinidoDTO.CAMPO, campoDocumento.getCampo(), null,null));
			List<PropiedadDTO> codigoDepende = Propiedades.obtenerVariosParametro(campoDocumento.getCampoDTO(), Propiedades.DEPENDE);
			if(codigoDepende!=null ){
				for (PropiedadDTO codigo: codigoDepende){
					for (PedidoVentaCaracteristicaDTO fieldExpediente: expediente.getCaracteristicas()) {
						if(codigo.getValor().compareTo(fieldExpediente.getCampo()) == 0){
							if(fieldExpediente.getCampoDTO().getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.PROCESO)==0
									&& fieldExpediente.getValorOpcion()!=null && fieldExpediente.getExpedientes()==null) {
								fieldExpediente.setExpedientes(new ArrayList<PedidoVentaDTO>());
								fieldExpediente.getExpedientes().add(pedidoService.consultaXId(fieldExpediente.getValorOpcion()));
							}
							if(campoDocumento.getDependientes()==null)campoDocumento.setDependientes(new ArrayList<PedidoVentaCaracteristicaDTO>());
							campoDocumento.getDependientes().add(fieldExpediente);
							
							break;
						}
					}
				}
			}
		}
		DeduccionProductoFilterDTO filter = new DeduccionProductoFilterDTO();
		filter.setDocumento(documento);
		filter.setEstado(SharedConstants.STATE_ACTIVE);
		List<DeduccionProductoDTO> deduccionesActuales = listarConsulta(filter);
		if(deduccionesActuales==null) deduccionesActuales = new ArrayList<DeduccionProductoDTO>();
		for(DeduccionProductoDTO iDeduccion : deduccionesActuales) {
			iDeduccion.setCantidad(iDeduccion.getCantidad().negate());
		}
		List<DeduccionProductoDTO> deduccionesFinales = new ArrayList<>();
		for (PedidoVentaCaracteristicaDTO iCampo : expediente.getCaracteristicas()) {
			// Identificar los campos bodega
			if(Propiedades.obtenerParametro(iCampo.getCampoDTO(), Propiedades.BODEGA_MOVIMIENTO)!=null) {
				// Validar
				String bodega = Propiedades.obtenerValor(iCampo.getCampoDTO(), Propiedades.BODEGA_FIJA);
				if(bodega.isEmpty()) {
					tipoBodega.consultarBodegaDesdeDocumento(iCampo);
					bodega = iCampo.getValorAuxiliar();
				}
				tipoBodega.validarPrepararCampo(iCampo, bodega);
				// Guardar
				deduccionesFinales = tipoBodega.validarInventario(iCampo, token) ;
				for(DeduccionProductoDTO iDeduccion : deduccionesFinales){
					deduccionesActuales = tipoBodega.adicionarDeduccion(deduccionesActuales, iDeduccion);
				}
			}
		}
		
		if(deduccionesActuales!=null && !deduccionesActuales.isEmpty()) {
			for(DeduccionProductoDTO iDeduccion : deduccionesActuales) {
				if(iDeduccion.getCantidad().compareTo(BigDecimal.ZERO)!=0){
					iDeduccion = guardar(iDeduccion, token);
				}
			}
		}
	}
// END region aditionalMethods

}