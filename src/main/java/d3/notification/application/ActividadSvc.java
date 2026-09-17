package d3.notification.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.document.application.CallDocumentListWithFilters;
import d3.document.application.PedidoVentaSvc;
import d3.document.domain.PedidoVentaDTO;
import d3.mail.application.MailGenerateMessageService;
import d3.notification.domain.ActividadDTO;
import d3.notification.domain.ActividadFilterDTO;
import d3.notification.infrastructure.ActividadMapper;
import d3.shared.application.BasicSvc;
import d3.shared.application.SessionContext;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.users.application.UsuarioSvc;
import d3.users.domain.UsuarioDTO;
import jakarta.annotation.PostConstruct;

@Service("actividadService")
public class ActividadSvc extends BasicSvc<ActividadDTO, ActividadFilterDTO> {

	private final ActividadMapper actividadMapper;

	public ActividadSvc(@Lazy ActividadMapper actividadMapper,
			@Lazy MailGenerateMessageService generateMessageService, @Lazy PedidoVentaSvc pedidoService,
			@Lazy UsuarioSvc usuarioService, @Lazy CallDocumentListWithFilters listDocumentWithFiltersFunction) {
		this.actividadMapper = actividadMapper;
		this.generateMessageService = generateMessageService;
		this.pedidoService = pedidoService;
		this.usuarioService = usuarioService;
		this.listDocumentWithFiltersFunction = listDocumentWithFiltersFunction;
	}

	private final MailGenerateMessageService generateMessageService;
	private final PedidoVentaSvc pedidoService;
	private final UsuarioSvc usuarioService;
	private final CallDocumentListWithFilters listDocumentWithFiltersFunction;

	@Override
	public ActividadDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. Actividad");
		ActividadFilterDTO dto = new ActividadFilterDTO();
		dto.setLlaveTabla(llave);
		return actividadMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = actividadMapper;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ActividadDTO inactivar(ActividadDTO dto) throws ServerException {
		dto.setEstado(SharedConstants.STATE_INACTIVE);
		dto.setFechaInactivo(new Date());
		dto.setUsuarioInactivo(SessionContext.getCurrentUser());
		return super.update(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public ActividadDTO guardar(ActividadDTO dto) throws ServerException {
		// Esto solo se usa para cuando cambio de responsable un documento, puede que si
		// no se usa bien se duplique el mensaje
		crearActividad(dto);
		PedidoVentaDTO pedido = pedidoService.consultaCompleta(dto.getDocumento());
		// Esto es para que se vean losparametros del mensaje
		PedidoVentaDTO pedidoModificador = new PedidoVentaDTO();
		pedidoModificador.setNombre(pedido.getNombre());
		pedidoModificador.setDescripcion(dto.getComentario());
		pedidoModificador.setPlantilla(pedido.getPlantilla());
		generateMessageService.call(pedido, null, usuarioService.consultaXId(dto.getResponsable()), pedidoModificador);
		return dto;
	}

	public UsuarioDTO crearActividad(ActividadDTO dto) throws ServerException {
		if (dto.getDocumento() == null)
			throw new ServerException("Al guardar el responsable no viene el documento");
		ActividadFilterDTO anteriorFilter = new ActividadFilterDTO();
		anteriorFilter.setDocumento(dto.getDocumento());
		anteriorFilter.setEstado(SharedConstants.STATE_ACTIVE);
		ActividadDTO anterior = consultaUnica(anteriorFilter);
		if (anterior != null) {
			if (dto.getResponsable() != null) {
				if (anterior.getResponsable().compareTo(dto.getResponsable()) == 0)
					return validarUsuario(anterior.getResponsable());
			}
			inactivar(anterior);
		} else {
			if (dto.getResponsable() == null)
				return null; // throw new ServerException("Al guardar el responsable no viene el usuario");
		}
		if (dto.getResponsable() != null) {
			dto.setFechaRegistro(new Date());
			dto.setUsuarioRegistro(SessionContext.getCurrentUser());
			dto = super.save(dto);
			return validarUsuario(dto.getResponsable());
		}
		return null;
	}

	private UsuarioDTO validarUsuario(String id) throws ServerException {
		UsuarioDTO usuario = usuarioService.consultaXId(id);
		if (usuario == null)
			throw new ServerException("Error al consultar el usuario responsable por llave");
		if (usuario.getEstado().compareTo(SharedConstants.STATE_ACTIVE) != 0)
			throw new ServerException("El responsable que desea asignar no esta activo.\n" + usuario.getNombre());
		return usuario;
	}

	public List<ActividadDTO> listUserActivities() throws ServerException {
		ActividadFilterDTO pd = new ActividadFilterDTO();
		pd.setResponsable(SessionContext.getCurrentUser());
		pd.setEstado(SharedConstants.STATE_ACTIVE);
		List<ActividadDTO> result = listarConsulta(pd);
		if (!result.isEmpty()) {
			List<String> ids = new ArrayList<>();
			for (ActividadDTO iActivity : result) {
				ids.add(iActivity.getDocumento());
			}
			List<PedidoVentaDTO> documentos = listDocumentWithFiltersFunction.listar2Activity(ids);
			for (ActividadDTO iActivity : result) {
				for (PedidoVentaDTO pedidoVentaDTO : documentos) {
					if (iActivity.getDocumento().compareTo(pedidoVentaDTO.getLlaveTabla()) == 0) {
						iActivity.setDocumentoDTO(pedidoVentaDTO);
						break;
					}
				}

			}
		}
		return result;
	}

	public ActividadDTO readActivity(String id) throws ServerException {
		ActividadDTO bd = consultaXId(id);
		if (bd.getFechaLeido() != null)
			return bd;
		bd.setFechaLeido(new Date());
		bd = update(bd);
		bd.setDocumentoDTO(pedidoService.consultaXId(bd.getDocumento()));
		return bd;
	}

	public void validateActivitiesToInactivateUser(String userId) throws ServerException {
		ActividadFilterDTO filtro = new ActividadFilterDTO();
		filtro.setEstado(SharedConstants.STATE_ACTIVE);
		filtro.setResponsable(userId);
		int cont = contarResultados(filtro);
		if (cont != 0) {
			List<PedidoVentaDTO> tareasActuales = pedidoService.listarTareasOtroUsuario(userId);
			String mensaje = "No se puede inactivar debido a que tiene asignaciones. " + cont;
			for (PedidoVentaDTO iTarea : tareasActuales) {
				if (iTarea.getDescripcion() != null) {
					mensaje = mensaje + "\n(" + iTarea.getNombre() + ") " + iTarea.getDescripcion();
				} else {
					mensaje = mensaje + "\n(" + iTarea.getNombre() + ") ";
				}
			}
			throw new ServerException(mensaje);
		}
	}

}