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
import com.softure.logisticpymes.domain.dto.PlantillaConsecutivoDTO;
import com.softure.logisticpymes.domain.filter.PlantillaConsecutivoFilterDTO;
import com.softure.logisticpymes.infrastructure.mybatis.mapper.PlantillaConsecutivoMapper;

@Service("plantillaConsecutivoService")
public class PlantillaConsecutivoSvc extends BasicSvc<PlantillaConsecutivoDTO, PlantillaConsecutivoFilterDTO> {
	
	@Autowired
	private PlantillaConsecutivoMapper plantillaConsecutivoMapper;
	
	// BEGIN region servicesPlantillaConsecutivo
	// END region servicesPlantillaConsecutivo

	@Override
	public PlantillaConsecutivoDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. PlantillaConsecutivo");
		PlantillaConsecutivoFilterDTO dto = new PlantillaConsecutivoFilterDTO();
		dto.setLlaveTabla(llave);
		return plantillaConsecutivoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = plantillaConsecutivoMapper;
	}
	
	@Override
	public PlantillaConsecutivoDTO activar(PlantillaConsecutivoDTO dto, String token) throws ServerException {
		// BEGIN PlantillaConsecutivo_activar
		return super.activar(dto, token);
		// END PlantillaConsecutivo_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PlantillaConsecutivoDTO actualizar( PlantillaConsecutivoDTO dto, String token) throws ServerException {
		// BEGIN PlantillaConsecutivo_actualizar
		return super.actualizar(dto, token);
		// END PlantillaConsecutivo_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PlantillaConsecutivoDTO inactivar(PlantillaConsecutivoDTO dto, String token) throws ServerException {
		// BEGIN PlantillaConsecutivo_inactivar
		return super.inactivar(dto, token);
		// END PlantillaConsecutivo_inactivar
	}
	
	@Override
	public PlantillaConsecutivoDTO consultaUnica(PlantillaConsecutivoFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(PlantillaConsecutivoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<PlantillaConsecutivoDTO> listarConsulta(PlantillaConsecutivoFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PlantillaConsecutivoDTO guardar(PlantillaConsecutivoDTO dto, String token) throws ServerException {
		// BEGIN PlantillaConsecutivo_guardar
		PlantillaConsecutivoFilterDTO bdFilter = new PlantillaConsecutivoFilterDTO();
		bdFilter.setCaracteristica(dto.getCaracteristica());
		bdFilter.setValorOpcion(dto.getValorOpcion());
		bdFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		PlantillaConsecutivoDTO bd = consultaUnica(bdFilter);
		if(bd!=null) throw new ServerException("Ya existe una relacion entre caracteristica y opcion");
		return super.guardar(dto, token);
		// END PlantillaConsecutivo_guardar
	}

// BEGIN region aditionalMethods
// END region aditionalMethods

}