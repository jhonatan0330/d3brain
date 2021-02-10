package com.softure.logisticpymes.services;

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
import com.softure.logisticpymes.dto.ModuloContratadoDTO;
import com.softure.logisticpymes.dto.filter.ModuloContratadoFilterDTO;
import com.softure.logisticpymes.persistence.ModuloContratadoMapper;

@Service("moduloContratadoService")
public class ModuloContratadoSvc extends BasicSvc<ModuloContratadoDTO, ModuloContratadoFilterDTO> {
	
	@Autowired
	private ModuloContratadoMapper moduloContratadoMapper;
	
	// BEGIN region servicesModuloContratado
	// END region servicesModuloContratado

	@Override
	public ModuloContratadoDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. ModuloContratado");
		ModuloContratadoFilterDTO dto = new ModuloContratadoFilterDTO();
		dto.setLlaveTabla(llave);
		return moduloContratadoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = moduloContratadoMapper;
	}
	
	@Override
	public ModuloContratadoDTO activar(ModuloContratadoDTO dto, String token) throws ServerException {
		// BEGIN ModuloContratado_activar
		return super.activar(dto, token);
		// END ModuloContratado_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ModuloContratadoDTO actualizar( ModuloContratadoDTO dto, String token) throws ServerException {
		// BEGIN ModuloContratado_actualizar
		return super.actualizar(dto, token);
		// END ModuloContratado_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ModuloContratadoDTO inactivar(ModuloContratadoDTO dto, String token) throws ServerException {
		// BEGIN ModuloContratado_inactivar
		return super.inactivar(dto, token);
		// END ModuloContratado_inactivar
	}
	
	@Override
	public ModuloContratadoDTO consultaUnica(ModuloContratadoFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(ModuloContratadoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<ModuloContratadoDTO> listarConsulta(ModuloContratadoFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	
	public List<ModuloContratadoDTO> modulosUsuario(ModuloContratadoFilterDTO dto)throws ServerException{
		// BEGIN region modulosUsuario
		// Aqui el security token es el usuario por el momento
		List<ModuloContratadoDTO> modulos =  moduloContratadoMapper.modulosUsuario(dto);
		return modulos;
		// END region modulosUsuario
	}

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ModuloContratadoDTO guardar(ModuloContratadoDTO dto, String token) throws ServerException {
		// BEGIN ModuloContratado_guardar
		if(dto.getImagen()==null) dto.setImagen(ConstantesGenerales.LOGO);
		return super.guardar(dto, token);
		// END ModuloContratado_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}