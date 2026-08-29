package d3.configuration.application;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.shared.application.BasicSvc;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Lazy;
import d3.authentication.application.UsuarioSesionSvc;
import d3.configuration.domain.RelacionInternaDTO;
import d3.configuration.domain.RelacionInternaFilterDTO;
import d3.configuration.infrastructure.RelacionInternaMapper;

@Service("relacionInternaService")
public class RelacionInternaSvc extends BasicSvc<RelacionInternaDTO, RelacionInternaFilterDTO> {

	private final RelacionInternaMapper relacionInternaMapper;
	private final PropertyGetWithCacheService cacheService;

	public RelacionInternaSvc(@Lazy UsuarioSesionSvc usuarioSesionService,
			@Lazy RelacionInternaMapper relacionInternaMapper, @Lazy PropertyGetWithCacheService cacheService) {
		super(usuarioSesionService);
		this.relacionInternaMapper = relacionInternaMapper;
		this.cacheService = cacheService;
	}

	@Override
	public RelacionInternaDTO consultaXId(String llave) throws ServerException {
		if (llave == null)
			throw new ServerException("La llave del DTO se encuentra vacia. RelacionInterna");
		RelacionInternaFilterDTO dto = new RelacionInternaFilterDTO();
		dto.setLlaveTabla(llave);
		return relacionInternaMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
		this.mapper = relacionInternaMapper;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public RelacionInternaDTO actualizar(RelacionInternaDTO pDTO, String pToken) throws ServerException {
		String llaveTabla = pDTO.getLlaveTabla();
		RelacionInternaDTO _newRelation = guardar(pDTO, pToken);
		if (_newRelation.getLlaveTabla().equals(llaveTabla))
			return _newRelation;
		RelacionInternaDTO _deleteRelation = new RelacionInternaDTO();
		_deleteRelation.setLlaveTabla(llaveTabla);
		inactivar(_deleteRelation, pToken);
		relacionInternaMapper.updatePropertyRelations(pDTO.getPropiedad());
		return pDTO;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public RelacionInternaDTO inactivar(RelacionInternaDTO dto, String token) throws ServerException {
		RelacionInternaDTO bd = consultaXId(dto.getLlaveTabla());
		bd.setUsuarioEliminacion(getUserFlex(token));
		bd.setFechaEliminacion(new Date());
		bd.setEstado(SharedConstants.STATE_INACTIVE);
		bd = super.update(bd);
		relacionInternaMapper.updatePropertyRelations(bd.getPropiedad());
		cacheService.clearProperties();
		return bd;
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public RelacionInternaDTO guardar(RelacionInternaDTO dto, String token) throws ServerException {
		if (dto.getPlantilla() == null)
			throw new ServerException("Es obligatorio registrar la plantilla de la relacion");
		if (dto.getCampo() == null)
			throw new ServerException("Es obligatorio registrar el campo de la relacion");
		if (dto.getAuxiliar() != null && dto.getAuxiliar().length() == 0)
			dto.setAuxiliar(null);
		RelacionInternaFilterDTO existeFilter = new RelacionInternaFilterDTO();
		existeFilter.setCampo(dto.getCampo());
		existeFilter.setPlantilla(dto.getPlantilla());
		existeFilter.setPropiedad(dto.getPropiedad());
		existeFilter.setAuxiliar(dto.getAuxiliar());
		existeFilter.setEstado(SharedConstants.STATE_ACTIVE);
		List<RelacionInternaDTO> existe = listarConsulta(existeFilter);
		if (existe != null && !existe.isEmpty()) {
			for (RelacionInternaDTO iRelation : existe) {
				if (dto.getAuxiliar() == null) {
					if (iRelation.getAuxiliar() == null)
						return iRelation;
				} else {
					if (dto.getAuxiliar().compareTo(iRelation.getAuxiliar()) == 0)
						return iRelation;
				}
			}
		}
		if (dto.getUsuarioCreacion() == null)
			dto.setUsuarioCreacion(getUserFlex(token));
		if (dto.getFechaInicio() == null)
			dto.setFechaInicio(new Date());

		String _templateOfField = relacionInternaMapper.getTemplateOfField(dto.getCampo());
		if (_templateOfField == null)
			return null;// En caso que sea un producto
		if (dto.getPlantilla().compareTo(_templateOfField) != 0)
			throw new ServerException("La plantilla no corresponde al campo escogido");

		if (dto.getFechaInicio() == null)
			dto.setFechaInicio(new Date());
		dto = super.saveSimple(dto);
		relacionInternaMapper.updatePropertyRelations(dto.getPropiedad());
		cacheService.clearProperties();
		return dto;
	}

	public List<RelacionInternaDTO> relacionesPropiedad(String propiedad) throws ServerException {
		RelacionInternaFilterDTO filtro = new RelacionInternaFilterDTO();
		filtro.setPropiedad(propiedad);
		filtro.setEstado(SharedConstants.STATE_ACTIVE);
		return super.listarConsulta(filtro);
	}

	public RelacionInternaDTO getFirstRelation(String pProperty, String pOptionalTemplateId) throws ServerException {
		List<RelacionInternaDTO> _relations = relacionesPropiedad(pProperty);
		if (_relations == null || _relations.isEmpty()) {
			return null;
		}

		if (pOptionalTemplateId == null || pOptionalTemplateId.isEmpty()) {
			return _relations.get(0);
		} else {
			for (RelacionInternaDTO iRelation : _relations) {
				if (iRelation.getPlantilla().compareTo(pOptionalTemplateId) == 0) {
					return iRelation;
				}
			}
		}
		return null;
	}

	public List<RelacionInternaDTO> getRelationsFullToSynchronize() throws ServerException {
		return relacionInternaMapper.getRelationsFullToSynchronize();
	}

	public void copyFromProperty(String propertyIdOld, String propertyIdNew, String token, String pUserCreation,
			boolean mantainDateInitial) throws ServerException {
		List<RelacionInternaDTO> relations = relacionesPropiedad(propertyIdOld);
		if (relations != null && !relations.isEmpty()) {
			for (RelacionInternaDTO iRelation : relations) {
				RelacionInternaDTO newRelation = new RelacionInternaDTO();
				newRelation.setAuxiliar(iRelation.getAuxiliar());
				newRelation.setCampo(iRelation.getCampo());
				newRelation.setPlantilla(iRelation.getPlantilla());
				newRelation.setPropiedad(propertyIdNew);
				if (mantainDateInitial) {
					newRelation.setFechaInicio(iRelation.getFechaInicio());
					newRelation.setUsuarioCreacion(pUserCreation);
				}
				guardar(newRelation, token);
			}
		} else {
			relacionInternaMapper.updatePropertyRelations(propertyIdNew);
		}
	}

}