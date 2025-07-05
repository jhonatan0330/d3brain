package com.softure.document_execution.application;

import java.util.List;

// BEGIN region interImport
import java.util.ArrayList;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import com.softure.authorization.application.RolAccesoSvc;
import com.softure.document_execution.application.field.CampoAdaptador;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.DocumentMessage;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaDineroDTO;
import com.softure.document_execution.domain.PedidoVentaFilterDTO;
import com.softure.document_execution.infrastructure.PedidoVentaMapper;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaFilterDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.process_form.domain.DocumentoPlantillaFilterDTO;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;

import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.logisticpymes.application.BasicSvc;

@Service("pedidoVentaService")
public class PedidoVentaSvc extends BasicSvc<PedidoVentaDTO, PedidoVentaFilterDTO> {

	@Autowired @Lazy 
	private PedidoVentaMapper pedidoVentaMapper;

	// BEGIN region servicesPedidoVenta
	@Autowired @Lazy 
	private CampoAdaptador adaptador;
	@Autowired @Lazy 
	private DocumentoPlantillaSvc documentoPlantillaService;
	@Autowired @Lazy 
	private DocumentoPlantillaCaracteristicaSvc documentoPlantillaCaracteristicaService;
	@Autowired @Lazy 
	private PedidoVentaDineroSvc dineroService;
	@Autowired @Lazy 
	private PedidoVentaCaracteristicaSvc pedidoVentaCaracteristicaService;
	@Autowired @Lazy 
	private PropiedadSvc propiedadService;
	@Autowired @Lazy 
	private RolAccesoSvc rolService;
	// END region servicesPedidoVenta

	@Override
	public PedidoVentaDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. PedidoVenta");
		PedidoVentaFilterDTO dto = new PedidoVentaFilterDTO();
		dto.setLlaveTabla(llave);
		return pedidoVentaMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = pedidoVentaMapper;
	}

	@Override
	public PedidoVentaDTO activar(PedidoVentaDTO dto, String token) throws ServerException {
		// BEGIN PedidoVenta_activar
		throw new ServerException("Un documento que fue inactivado no se puede volver a activar.");
		// END PedidoVenta_activar
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PedidoVentaDTO actualizar(PedidoVentaDTO dto, String token) throws ServerException {
		// BEGIN PedidoVenta_actualizar
		throw new ServerException("Usa la funcion SaveUpdateInactivateDocumentFunction update");
		// END PedidoVenta_actualizar
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PedidoVentaDTO inactivar(PedidoVentaDTO dto, String token) throws ServerException {
		// BEGIN PedidoVenta_inactivar
		throw new ServerException("Usa la funcion SaveUpdateInactivateDocumentFunction inactivate");
		// END PedidoVenta_inactivar
	}

	@Override
	public PedidoVentaDTO consultaUnica(PedidoVentaFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}

	@Override
	public int contarResultados(PedidoVentaFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}

	@Override
	public List<PedidoVentaDTO> listarConsulta(PedidoVentaFilterDTO dto) throws ServerException {
		return super.listarConsulta(dto);
	}

	public PedidoVentaDTO consultaCompleta(String documentId, String token) throws ServerException {
		// BEGIN region consultaCompleta
		if (documentId == null)
			throw new ServerException("En el desarrollo se debe crear el objeto desde la plantilla");
		String securityToken = token;
		PedidoVentaDTO bd = consultaXIdConDinero(documentId);
		if (bd == null)
			throw new ServerException("El identificador del DTO es incorrecto");
		// VAlido que el estado del pedido me permita modificaciones
		boolean modificable = true;
		if (bd.getEstadoExpediente() != null) {
			String usuarioToken = (securityToken == null) ? null : getUserFlex(securityToken);
			modificable = (propiedadService.obtenerPropiedad(PropiedadValorDefinidoDTO.ESTADO, bd.getEstadoExpediente(),
					Propiedades.MODIFICABLE, usuarioToken) == null) ? false : true;
		} else {
			if (bd.getEstado().compareTo(PedidoVentaDTO.ESTADO_ACTIVO) != 0) {
				modificable = false;
				if (bd.getEstado().compareTo(PedidoVentaDTO.ESTADO_FINALIZADO) == 0) {
					bd.setEstadoNombre("FINALIZADO");
				} else {
					bd.setEstadoNombre("INACTIVO");
				}
			} else {
				bd.setEstadoNombre("ACTIVO");
			}
		}
		DocumentoPlantillaFilterDTO plantillaFilter = new DocumentoPlantillaFilterDTO();
		plantillaFilter.setLlaveTabla(bd.getPlantilla());
		plantillaFilter.setSecurityToken(securityToken);
		DocumentoPlantillaDTO plantilla = documentoPlantillaService.obtenerConfiguracionSinCampos(plantillaFilter,
				rolService.usuarioPermisosCompletos(securityToken));
		plantilla = documentoPlantillaService.obtenerCampos(plantilla, securityToken, false);
		if (plantilla.getCaracteristicas() != null & plantilla.getCaracteristicas().size() != 0) {
			List<PedidoVentaCaracteristicaDTO> caracteristicasActuales = pedidoVentaCaracteristicaService
					.listar2Documento(bd.getLlaveTabla(), bd.getHistorico());
			bd.setCaracteristicas(new ArrayList<PedidoVentaCaracteristicaDTO>());
			PedidoVentaCaracteristicaDTO uc = null;
			for (DocumentoPlantillaCaracteristicaDTO documentoCaracteristicaDTO : plantilla.getCaracteristicas()) {
				uc = null;
				for (PedidoVentaCaracteristicaDTO pedidoCaracteristica : caracteristicasActuales) {
					if (pedidoCaracteristica.getCampo().compareTo(documentoCaracteristicaDTO.getLlaveTabla()) == 0) {
						uc = pedidoCaracteristica;
						break;
					}
				}
				if (uc == null)
					uc = new PedidoVentaCaracteristicaDTO();
				uc.setCampo(documentoCaracteristicaDTO.getLlaveTabla());
				uc.setCampoDTO(documentoCaracteristicaDTO);
				uc.setDocumento(bd.getLlaveTabla());
				adaptador.cargarConsultaCampo(uc, securityToken);
				if (!modificable) {
					Propiedades.retirarPropiedad(uc.getCampoDTO(), Propiedades.PERMISO_CAMPO_MODIFICABLE);
				}
				bd.getCaracteristicas().add(uc);
			}
		}
		return bd;
		// END region consultaCompleta
	}
	
	public PedidoVentaDTO validateBeforeNew(PedidoVentaFilterDTO filter) throws ServerException {
		PedidoVentaDTO result = new PedidoVentaDTO();
		List<PropiedadDTO> prop = propiedadService.obtenerPropiedades(PropiedadValorDefinidoDTO.PLANTILLA, filter.getPlantilla(),
                Propiedades.FUNCION_SQL_NEW_ANTES, filter.getSecurityToken());
		if(prop.isEmpty() || prop.size() != 1) return result;
		for (PropiedadDTO propiedadDTO : prop) {
			String resultString = propiedadService.validarFuncionSQL2(propiedadDTO, filter.getPlantilla(), filter.getSecurityToken());
			if (resultString != null && resultString.compareTo(SharedConstants.OK) != 0) {
				if(result.getMessages() == null) result.setMessages(new ArrayList<>());
				DocumentMessage message = new DocumentMessage();
				message.setType(SharedConstants.ERROR);
				message.setMessage(resultString);
				result.getMessages().add(message);
			}
		}
		return result;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PedidoVentaDTO guardar(PedidoVentaDTO dto, String token) throws ServerException {
		throw new ServerException("Usa la funcion SaveUpdateInactivateDocumentFunction save");
	}

	public PedidoVentaDTO consultaXIdConDinero(String llave) throws ServerException {
		PedidoVentaDTO result = consultaXId(llave);
		if (result == null)
			return result;
		// En softure en una iteracion al consultar por segunda vez no encontraba el
		// valor del dinero entonces para que se mantenga voy a decir que si es nulo
		// entonces use el que tiene
		PedidoVentaDineroDTO moneyDB = dineroService.consultaPorDocumento(llave, result.getHistorico(),
				result.getNombre());
		if (moneyDB != null)
			result.setDinero(moneyDB);

		return result;
	}

	public PedidoVentaDTO obtenerCamposCompletos(PedidoVentaDTO pedido, String token) throws ServerException {
		// Caracteristicas
		if (pedido == null || pedido.getPlantilla() == null)
			throw new ServerException("Desarrollador el pedido y su plantilla no deben venir nulos");
		DocumentoPlantillaCaracteristicaFilterDTO rcDTOFilter = new DocumentoPlantillaCaracteristicaFilterDTO();
		rcDTOFilter.setEstado(SharedConstants.STATE_ACTIVE);
		rcDTOFilter.setPlantilla(pedido.getPlantilla());
		List<DocumentoPlantillaCaracteristicaDTO> camposBase = documentoPlantillaCaracteristicaService
				.listarConsulta(rcDTOFilter);
		if (camposBase != null & camposBase.size() != 0) {
			List<PedidoVentaCaracteristicaDTO> caracteristicasActuales = pedidoVentaCaracteristicaService
					.listar2Documento(pedido.getLlaveTabla(), pedido.getHistorico());
			pedido.setCaracteristicas(new ArrayList<PedidoVentaCaracteristicaDTO>());
			if(caracteristicasActuales==null) caracteristicasActuales = new ArrayList<>();
			PedidoVentaCaracteristicaDTO uc = null;
			for (DocumentoPlantillaCaracteristicaDTO documentoCaracteristicaDTO : camposBase) {
				uc = null;
				for (PedidoVentaCaracteristicaDTO pedidoCaracteristica : caracteristicasActuales) {
					if (pedidoCaracteristica.getCampo().compareTo(documentoCaracteristicaDTO.getLlaveTabla()) == 0) {
						uc = pedidoCaracteristica;
						break;
					}
				}
				if (uc == null)
					uc = new PedidoVentaCaracteristicaDTO();
				uc.setCampo(documentoCaracteristicaDTO.getLlaveTabla());
				// documentoCaracteristicaDTO.setRol(pedido.getRol());
				uc.setCampoDTO(documentoCaracteristicaDTO);
				uc.setDocumento(pedido.getLlaveTabla());
				adaptador.cargarConsultaCampo(uc, token);
				pedido.getCaracteristicas().add(uc);
			}
		}
		return pedido;
	}

	public void actualizarEstadosNuevoProceso(PedidoVentaDTO dto) throws ServerException {
		if (dto == null)
			return;
		try {
			pedidoVentaMapper.actualizarEstados(dto);
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public int listarEstadosNuevoProceso(PedidoVentaDTO dto) throws ServerException {
		if (dto == null)
			return 1000;
		try {
			return pedidoVentaMapper.contarEstados(dto);
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

	public List<PedidoVentaDTO> iteracionesProceso(String sqlFuncionDecision, String llaveTablaDocumento,
			String llaveTablaModificador) throws ServerException {
		List<PedidoVentaDTO> result = null;
		try {
			//ramdom por problemas del framework se repetia la respuesta cuando iteraba
			result = pedidoVentaMapper.iteracion(sqlFuncionDecision, llaveTablaDocumento, llaveTablaModificador, generarLlave());
		} catch (Exception e) {
			throw new ServerException(e.getMessage(), "Funcion de Iteracion con errores: ");
		}
		return result;
	}

	public List<PedidoVentaDTO> listarTareasOtroUsuario(String usuario) throws ServerException {
		PedidoVentaFilterDTO filter = new PedidoVentaFilterDTO();
		filter.setFuncionario(usuario);
		paginar(filter);
		try {
			return pedidoVentaMapper.listarUsuario(filter);
		} catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}

}