package com.softure.logisticpymes.application;

import java.util.List;

// BEGIN region interImport
import com.softure.java.cons.ConstantesGenerales;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.domain.ServidorDTO;
import com.softure.logisticpymes.domain.ServidorFilterDTO;
import com.softure.logisticpymes.infrastructure.ServidorMapper;

@Service("servidorService")
public class ServidorSvc extends BasicSvc<ServidorDTO, ServidorFilterDTO> {
	
	@Autowired
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
		filtroFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
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
// END region aditionalMethods

}