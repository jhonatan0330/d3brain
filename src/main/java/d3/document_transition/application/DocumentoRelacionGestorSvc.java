package d3.document_transition.application;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.document_transition.domain.DocumentoRelacionGestorDTO;
import d3.document_transition.domain.DocumentoRelacionGestorFilterDTO;
import d3.document_transition.infrastructure.DocumentoRelacionGestorMapper;
import d3.logisticpymes.application.BasicSvc;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import d3.authentication.application.UsuarioSesionSvc;

@Service("documentoRelacionGestorService")
public class DocumentoRelacionGestorSvc extends BasicSvc<DocumentoRelacionGestorDTO, DocumentoRelacionGestorFilterDTO> {

	private final DocumentoRelacionGestorMapper documentoRelacionGestorMapper;

	public DocumentoRelacionGestorSvc(@Lazy UsuarioSesionSvc usuarioSesionService,
			@Lazy DocumentoRelacionGestorMapper documentoRelacionGestorMapper) {
		super(usuarioSesionService);
		this.documentoRelacionGestorMapper = documentoRelacionGestorMapper;
	}

	@Override
	public DocumentoRelacionGestorDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. DocumentoRelacionGestor");
		DocumentoRelacionGestorFilterDTO dto = new DocumentoRelacionGestorFilterDTO();
		dto.setLlaveTabla(llave);
		return documentoRelacionGestorMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = documentoRelacionGestorMapper;
	}

	@Override
	public List<DocumentoRelacionGestorDTO> listarConsulta(DocumentoRelacionGestorFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}

	public List<DocumentoRelacionGestorDTO> listarExpedientesGestionadores(DocumentoRelacionGestorFilterDTO dto)
			throws ServerException {
		if (dto == null || dto.getDocumentoPrincipal() == null)
			throw new ServerException("Revisa porque no encontramos el documento principal");
		paginar(dto);
		String verMensaje = null;
		String verAsignacion = null;
		String verInventarios = null;
		String verReportes = null;
		// String usuarioAutomatico = null;
		String verApi = null;
		String verValores = null;
		String verUbicacion = null;
		String verComprobantes = null;
		if (dto.getEstado() != null) {
			if (dto.getEstado().length() > 1 && dto.getEstado().charAt(1) == '1')
				verAsignacion = "1";
			if (dto.getEstado().length() > 2 && dto.getEstado().charAt(2) == '1')
				verMensaje = "1";
			if (dto.getEstado().length() > 3 && dto.getEstado().charAt(3) == '1')
				verInventarios = "1";
			if (dto.getEstado().length() > 4 && dto.getEstado().charAt(4) == '1')
				verValores = "1";
			if (dto.getEstado().length() > 5 && dto.getEstado().charAt(5) == '1')
				verReportes = "1";
			if (dto.getEstado().length() > 6 && dto.getEstado().charAt(6) == '1')
				verApi = "1";
			if (dto.getEstado().length() > 7 && dto.getEstado().charAt(7) == '1')
				verUbicacion = "1";
			if (dto.getEstado().length() > 8 && dto.getEstado().charAt(8) == '1')
				verComprobantes = "1";
			// if(dto.getEstado().length() > 9 && dto.getEstado().charAt(5) != '1')
			// usuarioAutomatico = documentoRelacionGestorMapper.getSystemUser();
		}
		return documentoRelacionGestorMapper.listarExpedientesGestionadores(dto,
				documentoRelacionGestorMapper.isActual(dto.getDocumentoPrincipal()), verAsignacion, verMensaje,
				verInventarios, verReportes, verApi, verValores, verUbicacion, verComprobantes);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public DocumentoRelacionGestorDTO guardar(DocumentoRelacionGestorDTO dto, String token) throws ServerException {
		if (documentoRelacionGestorMapper.isActual(dto.getDocumentoPrincipal()) != null) {
			return super.guardar(dto, token);
		} else {
			getUserFlex(token);
			return documentoRelacionGestorMapper.insertHistoricTable(dto);
		}
	}

	public DocumentoRelacionGestorDTO trazar(String principal, String modificador, String nombre, String estadoInicial,
			String estadoFinal, String valores,

			String token, DocumentoRelacionGestorDTO anterior, Integer historico, String transaccion,
			boolean isUpdateDocument) throws ServerException {
		DocumentoRelacionGestorDTO actual;
		if (anterior == null) {
			actual = documentoRelacionGestorMapper.ultimoRegistro(principal, (historico == null) ? null : "historico");
			/*
			 * if(actual==null) {
			 * if(documentoRelacionGestorMapper.isActual(principal)==null) { throw new
			 * ServerException("Revisa con el desarrollador porque este documento se encuentra en el historico"
			 * ); } }
			 */

		} else {
			actual = anterior;
		}

		// Para ayudar a obtener la ultima gestion de cada estado vamos avanzando
		if (actual != null) {
			DocumentoRelacionGestorFilterDTO filter = new DocumentoRelacionGestorFilterDTO();
			filter.setEstadorepetidoFilter(false);
			filter.setEstado(SharedConstants.STATE_ACTIVE);
			filter.setDocumentoPrincipal(principal);
			filter.setEstadoFinal(estadoFinal);
			List<DocumentoRelacionGestorDTO> actuales = listarConsulta(filter);
			if (actuales != null && !actuales.isEmpty()) {
				for (DocumentoRelacionGestorDTO documentoRelacionGestorDTO : actuales) {
					documentoRelacionGestorDTO.setEstadorepetido(true);
					update(documentoRelacionGestorDTO);
				}
			}
		}

		DocumentoRelacionGestorDTO gestor = new DocumentoRelacionGestorDTO();
		gestor.setDocumentoPrincipal(principal);
		gestor.setDocumentoModificador(modificador);
		gestor.setFecha(new Date());
		gestor.setEstadoInicial(estadoInicial);
		gestor.setEstadoFinal(estadoFinal);
		gestor.setValores(valores);

		if (nombre != null && nombre.length() > 100)
			nombre = nombre.substring(nombre.length() - 100, nombre.length());
		gestor.setNombre(nombre);
		gestor.setTransaccion(transaccion);
		if (actual != null) {
			actual.setCierre(new Date());
			if (historico == null) {
				update(actual);
			} else {
				documentoRelacionGestorMapper.actualizarHistoricTable(actual);
			}
			// Esto no es necesario en universal generaba un error que las guias no
			// cambiaban de ubicacion
			// toca en cada estado colocar la ubicacion
			// lo volvi a activar para las transacciones que no tienen modificador asi no me
			// borra al modificar el documento
			// if((modificador == null || isUpdateDocument) && gestor.getUbicacion()==null)
			// gestor.setUbicacion( actual.getUbicacion());
		}
		gestor.setUsuario(getUserFlex(token));
		if (historico == null) {
			gestor = save(gestor);
		} else {
			gestor.setLlaveTabla(generarLlave());
			documentoRelacionGestorMapper.insertHistoricTable(gestor);
		}

		System.out.format("\n[] TRACE por transicion %s, con estado inicial ( %s ) y estado final ( %s )",
				gestor.getNombre(), gestor.getEstadoInicial(), gestor.getEstadoFinal());
		return gestor;
	}

}