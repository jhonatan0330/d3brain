package com.softure.logisticpymes.application;

import java.util.List;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.logisticpymes.domain.ServidorDTO;
import com.softure.logisticpymes.domain.ServidorFilterDTO;
import com.softure.logisticpymes.infrastructure.ServidorMapper;

import jakarta.annotation.PostConstruct;

@Service("servidorService")
public class ServidorSvc extends BasicSvc<ServidorDTO, ServidorFilterDTO> {
	
	@Autowired @Lazy 
	private ServidorMapper servidorMapper;
	
	// BEGIN region servicesServidor
	// END region servicesServidor

	@Override
	public ServidorDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. Servidor");
		ServidorFilterDTO dto = new ServidorFilterDTO();
		dto.setLlaveTabla(llave);
		return servidorMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = servidorMapper;
	}
	
	@Override
	public ServidorDTO activar(ServidorDTO dto, String token) throws ServerException {
		// BEGIN Servidor_activar
		return super.activar(dto, token);
		// END Servidor_activar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ServidorDTO actualizar( ServidorDTO dto, String token) throws ServerException {
		// BEGIN Servidor_actualizar
		validateDTO(dto);
		return super.actualizar(dto, token);
		// END Servidor_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ServidorDTO inactivar(ServidorDTO dto, String token) throws ServerException {
		// BEGIN Servidor_inactivar
		return super.inactivar(dto, token);
		// END Servidor_inactivar
	}
	
	@Override
	public ServidorDTO consultaUnica(ServidorFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(ServidorFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<ServidorDTO> listarConsulta(ServidorFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ServidorDTO guardar(ServidorDTO dto, String token) throws ServerException {
		// BEGIN Servidor_guardar
		validateDTO(dto);
		return super.guardar(dto, token);
		// END Servidor_guardar
	}

// BEGIN region aditionalMethods
	public ServidorDTO obtenerServidorPrincipal(String tipo) throws ServerException {
		ServidorFilterDTO filtroFilter = new ServidorFilterDTO();
		filtroFilter.setEstado(SharedConstants.STATE_ACTIVE);
		filtroFilter.setTipo(tipo);
		List<ServidorDTO> servidores = listarConsulta(filtroFilter);
		if(servidores==null || servidores.isEmpty()) return null;
		return servidores.get(0);
	}
	
	private void validateDTO(ServidorDTO dto) throws ServerException {
		if(dto.getPuerto()!=null) {
			try {
				Integer.parseInt(dto.getPuerto());
			}catch (Exception e) {
				throw new ServerException("REvisa el valor del puerto que debe ser numerico y sin espacios");
			}
		}
	}
	
	public String getFromMail(ServidorDTO server) {
		if(server.getBase() != null && !server.getBase().isEmpty()) {
			return server.getBase();
		}
		return server.getUsuario();
	}
// END region aditionalMethods

}