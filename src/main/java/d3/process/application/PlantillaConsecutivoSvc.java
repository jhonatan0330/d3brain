package d3.process.application;

import java.util.List;

import d3.shared.domain.SharedConstants;
import d3.shared.domain.ServerException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.shared.application.BasicSvc;
import d3.process.domain.PlantillaConsecutivoDTO;
import d3.process.domain.PlantillaConsecutivoFilterDTO;
import d3.process.infrastructure.PlantillaConsecutivoMapper;

import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;

@Service("plantillaConsecutivoService")
public class PlantillaConsecutivoSvc extends BasicSvc<PlantillaConsecutivoDTO, PlantillaConsecutivoFilterDTO> {

	private final PlantillaConsecutivoMapper plantillaConsecutivoMapper;

	public PlantillaConsecutivoSvc(
			@Lazy PlantillaConsecutivoMapper plantillaConsecutivoMapper) {
		this.plantillaConsecutivoMapper = plantillaConsecutivoMapper;
	}

	@Override
	public PlantillaConsecutivoDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. PlantillaConsecutivo");
		PlantillaConsecutivoFilterDTO dto = new PlantillaConsecutivoFilterDTO();
		dto.setLlaveTabla(llave);
		return plantillaConsecutivoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = plantillaConsecutivoMapper;
	}

	@Override
	public PlantillaConsecutivoDTO activar(PlantillaConsecutivoDTO dto) throws ServerException {
		return super.activar(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PlantillaConsecutivoDTO actualizar(PlantillaConsecutivoDTO dto) throws ServerException {
		return super.actualizar(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PlantillaConsecutivoDTO inactivar(PlantillaConsecutivoDTO dto) throws ServerException {
		return super.inactivar(dto);
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
	public List<PlantillaConsecutivoDTO> listarConsulta(PlantillaConsecutivoFilterDTO dto) throws ServerException {
		return super.listarConsulta(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PlantillaConsecutivoDTO guardar(PlantillaConsecutivoDTO dto) throws ServerException {
		PlantillaConsecutivoFilterDTO bdFilter = new PlantillaConsecutivoFilterDTO();
		bdFilter.setCaracteristica(dto.getCaracteristica());
		bdFilter.setValorOpcion(dto.getValorOpcion());
		bdFilter.setEstado(SharedConstants.STATE_ACTIVE);
		PlantillaConsecutivoDTO bd = consultaUnica(bdFilter);
		if (bd != null)
			throw new ServerException("Ya existe una relacion entre caracteristica y opcion");
		return super.guardar(dto);
	}


}