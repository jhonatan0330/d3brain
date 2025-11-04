package com.softure.document_transaction.application;

// BEGIN region interImport
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.authentication.application.UsuarioSesionSvc;
import com.softure.document_transaction.domain.DocumentoTransaccionDTO;
import com.softure.document_transaction.domain.DocumentoTransaccionFilterDTO;
import com.softure.document_transaction.infrastructure.DocumentoTransaccionMapper;
import com.softure.logisticpymes.application.BasicSvc;

import jakarta.annotation.PostConstruct;

@Service("documentoTransaccionService")
public class DocumentoTransaccionSvc extends BasicSvc<DocumentoTransaccionDTO, DocumentoTransaccionFilterDTO> {
	
	@Autowired @Lazy 
	private DocumentoTransaccionMapper documentoTransaccionMapper;
	
	@Autowired @Lazy  private UsuarioSesionSvc sesionSvc;

	public static final String API_ASYNC = "A";
	public static final String API_PREPARE_ASYNC = "P";
	public static final String MAIL_ASYNC = "M";


	@Override
	public DocumentoTransaccionDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. DocumentoTransaccion");
		DocumentoTransaccionFilterDTO dto = new DocumentoTransaccionFilterDTO();
		dto.setLlaveTabla(llave);
		return documentoTransaccionMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = documentoTransaccionMapper;
	}
	
	public DocumentoTransaccionDTO crear(String token) throws ServerException {
		DocumentoTransaccionDTO nuevo = new DocumentoTransaccionDTO();
		nuevo.setUsuario(sesionSvc.actualizarSesion(token));
		nuevo.setFecha(new Date());
		return saveSimple(nuevo);
	}

}