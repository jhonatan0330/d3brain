package com.softure.upload.application;

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
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.upload.domain.CargaArchivoDTO;
import com.softure.upload.domain.CargaArchivoFilterDTO;
import com.softure.upload.infrastructure.CargaArchivoMapper;

@Service("cargaArchivoService")
public class CargaArchivoSvc extends BasicSvc<CargaArchivoDTO, CargaArchivoFilterDTO> {
	
	@Autowired
	private CargaArchivoMapper cargaArchivoMapper;
	
	// BEGIN region servicesCargaArchivo
	// END region servicesCargaArchivo

	@Override
	public CargaArchivoDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. CargaArchivo");
		CargaArchivoFilterDTO dto = new CargaArchivoFilterDTO();
		dto.setLlaveTabla(llave);
		return cargaArchivoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = cargaArchivoMapper;
	}
	
	@Override
	public CargaArchivoDTO activar(CargaArchivoDTO dto, String token) throws ServerException {
		// BEGIN CargaArchivo_activar
		return super.activar(dto, token);
		// END CargaArchivo_activar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CargaArchivoDTO actualizar( CargaArchivoDTO dto, String token) throws ServerException {
		// BEGIN CargaArchivo_actualizar
		return super.actualizar(dto, token);
		// END CargaArchivo_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CargaArchivoDTO inactivar(CargaArchivoDTO dto, String token) throws ServerException {
		// BEGIN CargaArchivo_inactivar
		return super.inactivar(dto, token);
		// END CargaArchivo_inactivar
	}
	
	@Override
	public CargaArchivoDTO consultaUnica(CargaArchivoFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(CargaArchivoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<CargaArchivoDTO> listarConsulta(CargaArchivoFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public CargaArchivoDTO guardar(CargaArchivoDTO dto, String token) throws ServerException {
		// BEGIN CargaArchivo_guardar
		dto.setFechaFin(new Date());
		return super.save(dto);
		// END CargaArchivo_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}