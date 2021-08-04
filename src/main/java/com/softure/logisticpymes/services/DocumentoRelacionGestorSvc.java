package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import java.util.Date;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.DocumentoRelacionGestorDTO;
import com.softure.logisticpymes.dto.filter.DocumentoRelacionGestorFilterDTO;
import com.softure.logisticpymes.persistence.DocumentoRelacionGestorMapper;

@Service("documentoRelacionGestorService")
public class DocumentoRelacionGestorSvc extends BasicSvc<DocumentoRelacionGestorDTO, DocumentoRelacionGestorFilterDTO> {
	
	@Autowired
	private DocumentoRelacionGestorMapper documentoRelacionGestorMapper;
	
	// BEGIN region servicesDocumentoRelacionGestor
	// END region servicesDocumentoRelacionGestor

	@Override
	public DocumentoRelacionGestorDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. DocumentoRelacionGestor");
		DocumentoRelacionGestorFilterDTO dto = new DocumentoRelacionGestorFilterDTO();
		dto.setLlaveTabla(llave);
		return documentoRelacionGestorMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = documentoRelacionGestorMapper;
	}
	
	@Override
	public DocumentoRelacionGestorDTO activar(DocumentoRelacionGestorDTO dto, String token) throws ServerException {
		// BEGIN DocumentoRelacionGestor_activar
		return super.activar(dto, token);
		// END DocumentoRelacionGestor_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DocumentoRelacionGestorDTO actualizar( DocumentoRelacionGestorDTO dto, String token) throws ServerException {
		// BEGIN DocumentoRelacionGestor_actualizar
		return super.actualizar(dto, token);
		// END DocumentoRelacionGestor_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DocumentoRelacionGestorDTO inactivar(DocumentoRelacionGestorDTO dto, String token) throws ServerException {
		// BEGIN DocumentoRelacionGestor_inactivar
		return super.inactivar(dto, token);
		// END DocumentoRelacionGestor_inactivar
	}
	
	@Override
	public DocumentoRelacionGestorDTO consultaUnica(DocumentoRelacionGestorFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(DocumentoRelacionGestorFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<DocumentoRelacionGestorDTO> listarConsulta(DocumentoRelacionGestorFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	
	public List<DocumentoRelacionGestorDTO> listarExpedientesGestionadores(DocumentoRelacionGestorFilterDTO dto)throws ServerException{
		// BEGIN region listarExpedientesGestionadores
		if(dto==null || dto.getDocumentoPrincipal()==null) throw new ServerException("Revisa porque no encontramos el documento principal");
		paginar(dto);
		String verMensaje = null;
		String verAsignacion = null;
		String verInventarios = null;
		String verReportes = null;
		String usuarioAutomatico = null;
		String verApi = null;
		if(dto.getEstado()!=null) {
			if(dto.getEstado().length() > 1 && dto.getEstado().charAt(1) == '1') verAsignacion = "1";
			if(dto.getEstado().length() > 2 && dto.getEstado().charAt(2) == '1') verMensaje = "1";
			if(dto.getEstado().length() > 3 && dto.getEstado().charAt(3) == '1') verInventarios = "1";
			if(dto.getEstado().length() > 4 && dto.getEstado().charAt(4) == '1') verReportes = "1";
			if(dto.getEstado().length() > 5 && dto.getEstado().charAt(5) != '1') usuarioAutomatico = documentoRelacionGestorMapper.getSystemUser();
			if(dto.getEstado().length() > 6 && dto.getEstado().charAt(6) == '1') verApi = "1";
		}
		return documentoRelacionGestorMapper.listarExpedientesGestionadores(
				dto,
				documentoRelacionGestorMapper.isActual(dto.getDocumentoPrincipal()),
				verAsignacion, verMensaje, verInventarios, verReportes, usuarioAutomatico, verApi);
		// END region listarExpedientesGestionadores
	}

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DocumentoRelacionGestorDTO guardar(DocumentoRelacionGestorDTO dto, String token) throws ServerException {
		// BEGIN DocumentoRelacionGestor_guardar
		if(documentoRelacionGestorMapper.isActual(dto.getDocumentoPrincipal())!=null) {
			return super.guardar(dto, token);	
		}else {
			getUserFlex(token);
			return documentoRelacionGestorMapper.insertHistoricTable(dto);
		}
		// END DocumentoRelacionGestor_guardar
	}

// BEGIN region aditionalMethods
	public DocumentoRelacionGestorDTO trazar(
			String principal, 
			String modificador, 
			String nombre, 
			String estadoInicial, 
			String estadoFinal, 
			String valores, 
			String ubicacion, 
			String token, 
			DocumentoRelacionGestorDTO anterior,
			Integer historico) throws ServerException {
		DocumentoRelacionGestorDTO actual;
		if(anterior==null) {
			actual = documentoRelacionGestorMapper.ultimoRegistro(principal, (historico==null)?null:"historico");
			/*if(actual==null) {
				if(documentoRelacionGestorMapper.isActual(principal)==null) {
					throw new ServerException("Revisa con el desarrollador porque este documento se encuentra en el historico");
				}
			}*/
			
		}else {
			actual = anterior;
		}
		DocumentoRelacionGestorDTO gestor = new DocumentoRelacionGestorDTO();
		gestor.setDocumentoPrincipal(principal);
		gestor.setDocumentoModificador(modificador);
		gestor.setFecha(new Date());
		gestor.setEstadoInicial(estadoInicial);
		gestor.setEstadoFinal(estadoFinal);
		gestor.setValores(valores);
		gestor.setUbicacion(ubicacion);
		gestor.setNombre(nombre);
		if(actual!=null) {
			actual.setCierre(new Date());
			if ( historico ==null) {
				update(actual);
			}else {
				documentoRelacionGestorMapper.actualizarHistoricTable(actual);
			}
			if(gestor.getUbicacion()==null) gestor.setUbicacion( actual.getUbicacion());
		}
		gestor.setUsuario(getUserFlex(token));
		if(historico ==null) {
			gestor = save(gestor);
		}else {
			gestor.setLlaveTabla(generarLlave());
			documentoRelacionGestorMapper.insertHistoricTable(gestor);
		}
		
		System.out.format("\n[] TRACE por transicion %s, con estado inicial ( %s ) y estado final ( %s )", gestor.getNombre(), gestor.getEstadoInicial(), gestor.getEstadoFinal());
		return gestor;
	}
// END region aditionalMethods

}