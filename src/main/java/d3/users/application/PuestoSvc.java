package d3.users.application;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.shared.application.BasicSvc;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.users.domain.PuestoDTO;
import d3.users.domain.PuestoFilterDTO;
import d3.users.infrastructure.PuestoMapper;
import jakarta.annotation.PostConstruct;

@Service("puestoService")
public class PuestoSvc extends BasicSvc<PuestoDTO, PuestoFilterDTO> {

	private final PuestoMapper puestoMapper;

	public PuestoSvc(@Lazy PuestoMapper puestoMapper) {
		this.puestoMapper = puestoMapper;
	}

	@Override
	public PuestoDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. Puesto");
		PuestoFilterDTO dto = new PuestoFilterDTO();
		dto.setLlaveTabla(llave);
		return puestoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = puestoMapper;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PuestoDTO actualizar(PuestoDTO dto) throws ServerException {
		// Validar
		if (dto.getFila().compareTo(0) < 0)
			throw new ServerException("Revisa la posicion de no puede estar por encima del espacio visible");
		return super.actualizar(dto);
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public PuestoDTO guardar(PuestoDTO dto) throws ServerException {
		if (dto.getFila().compareTo(0) < 0)
			throw new ServerException("Revisa la posicion de no puede estar por encima del espacio visible");
		if (dto.getCampo() == null)
			throw new ServerException("Campo de puesto sin enviar");
		if (dto.getNombre() == null || dto.getNombre().isEmpty())
			throw new ServerException("Nombre de puesto no puede ser vacio");
		PuestoFilterDTO _filter = new PuestoFilterDTO();
		_filter.setCampo(dto.getCampo());
		_filter.setNombre(dto.getNombre());
		_filter.setEstado(SharedConstants.STATE_ACTIVE);
		if (contarResultados(_filter) != 0)
			throw new ServerException("Ya existe un puesto con este mismo nombre " + _filter.getNombre());
		return super.guardar(dto);
	}

}