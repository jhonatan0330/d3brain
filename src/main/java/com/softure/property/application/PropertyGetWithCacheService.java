package com.softure.property.application;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadFilterDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.property.domain.PropiedadValorDefinidoFilterDTO;
import com.softure.property.infrastructure.PropertyCacheMapper;
import com.softure.property.infrastructure.PropiedadValorDefinidoMapper;

@Service
public class PropertyGetWithCacheService {
	
	@Autowired @Lazy private PropertyCacheMapper propiedadMapper;
	@Autowired @Lazy private PropiedadValorDefinidoMapper propiedadValorDefinidoMapper;
	
	private List<PropiedadValorDefinidoDTO> types;
	
	private Map<String, List<PropiedadDTO>> propByTypeMap = new HashMap<String, List<PropiedadDTO>>();
	private Map<String, List<String>> userRoleMap = new HashMap<String, List<String>>();
	private Map<String, List<PropiedadDTO>> propByKey = new HashMap<String, List<PropiedadDTO>>();

	private List<PropiedadDTO> getCacheRolProperties( String pUser, Boolean pPrivada, PropiedadFilterDTO pFilter) {
		List<PropiedadDTO> _propertiesType = null;
		if(pFilter.getCampo()==null) {
			if(pFilter.getPropiedadValor()!=null) {
				_propertiesType = propByKey.get(pFilter.getPropiedadValor());
				if(_propertiesType==null) {
					PropiedadFilterDTO _filterAll = new PropiedadFilterDTO();
					_filterAll.setPropiedadValor(pFilter.getPropiedadValor());
					_filterAll.setTipo(pFilter.getTipo());
					List<PropiedadDTO> _fromDB = propiedadMapper.consultarRol(_filterAll, null, null, null);
					propByKey.put(pFilter.getPropiedadValor(), _fromDB);
					_propertiesType = _fromDB;
				}
			} else {
				return propiedadMapper.consultarRol(pFilter, pUser, new Date(), pPrivada);
			}
		} else {
			String _key = pFilter.getTipo()+ "_" + pFilter.getCampo();
			_propertiesType = propByTypeMap.get(_key);
			if(_propertiesType==null) {
				PropiedadFilterDTO _filterAll = new PropiedadFilterDTO();
				_filterAll.setTipo(pFilter.getTipo());
				_filterAll.setCampo(pFilter.getCampo());
				List<PropiedadDTO> _fromDB = propiedadMapper.consultarRol(_filterAll, null, null, null);
				propByTypeMap.put(_key, _fromDB);
				_propertiesType = _fromDB;
			}
		}
		if(_propertiesType ==null) {
			_propertiesType = new ArrayList<>();
		}else {
			// Esto es para que no se afecte el cache
			_propertiesType = new ArrayList<>(_propertiesType);			
		}
		if(_propertiesType.isEmpty()) return _propertiesType;
		if(pFilter.getPropiedadValor()!=null) {
			 _propertiesType.removeIf(p -> !p.getPropiedadValor().equals(pFilter.getPropiedadValor()));
		}
		if(_propertiesType.isEmpty()) return _propertiesType;
		if(pUser!=null) {
			// REviso propiedades por usuario
			_propertiesType.removeIf(p -> p.getUsuario()!=null && !p.getUsuario().equals(pUser));
			_propertiesType.removeIf(p -> p.getUsuarioExcluyente()!=null && !p.getUsuarioExcluyente().equals(pUser));
			if(_propertiesType.isEmpty()) return _propertiesType;
			// REviso propiedades por rol
			List<String> _role = userRoleMap.get(pUser);
			if(_role==null) {
				_role = propiedadMapper.getUserRole(pUser);
				userRoleMap.put(pUser, _role);	
			}
			final List<String> _roleFinalToManageError = _role;
			_propertiesType.removeIf(p -> p.getRol() != null && !_roleFinalToManageError.contains(p.getRol()));
			_propertiesType.removeIf(p -> p.getRolExcluyente() != null && !_roleFinalToManageError.contains(p.getRolExcluyente()));
		
		} else {
			_propertiesType.removeIf(p -> p.getUsuario()!=null);
			_propertiesType.removeIf(p -> p.getUsuarioExcluyente()!=null);
			_propertiesType.removeIf(p -> p.getRol()!=null);
			_propertiesType.removeIf(p -> p.getRolExcluyente()!=null);
		}
		if(_propertiesType.isEmpty()) return _propertiesType;
		_propertiesType.removeIf(p -> p.getFechaInicial()!=null && p.getFechaInicial().after(new Date()));
		_propertiesType.removeIf(p -> p.getFechaFinal()!=null && p.getFechaFinal().before(new Date()));
		return _propertiesType;
	}
	
	public void clearProperties() throws ServerException {
		propByTypeMap.clear();
		propByKey.clear();
	}
	
	public void clearRole() throws ServerException {
		userRoleMap.clear();
	}

	public List<PropiedadDTO> obtenerPropiedadesSinEntidad( String tipo, String entidad, String key, String usuario, Boolean privada)
			throws ServerException {
		PropiedadFilterDTO filtroOrden = new PropiedadFilterDTO();
		filtroOrden.setTipo(tipo);
		filtroOrden.setCampo(entidad);
		if (key != null) {
			PropiedadValorDefinidoDTO valorDefinido = consultarValorDefinido(tipo, key);
			filtroOrden.setPropiedadValor(valorDefinido.getLlaveTabla());
		}
		List<PropiedadDTO> consultadas = getCacheRolProperties(usuario, privada, filtroOrden);
		if (usuario != null) {
			return cleanPropertiesFromTimeAndExclusion(consultadas);
		}
		return consultadas;
	}

	public List<PropiedadDTO> obtenerPropiedadesSinEntidad( String tipo, String entidad, String key, String usuario)
			throws ServerException {
		return obtenerPropiedadesSinEntidad( tipo, entidad, key, usuario, null);
	}

	public List<PropiedadDTO> obtenerPropiedades( String tipo, String entidad, String key, String usuario, Boolean privada)
			throws ServerException {
		if (entidad == null)
			throw new ServerException("El campo esta nulo");
		return obtenerPropiedadesSinEntidad( tipo, entidad, key, usuario, privada);
	}

	private List<PropiedadDTO> cleanPropertiesFromTimeAndExclusion(List<PropiedadDTO> consultadas) {
	
		List<PropiedadDTO> validadas = new ArrayList<PropiedadDTO>();
		List<PropiedadDTO> excluidas = new ArrayList<PropiedadDTO>();
	
		if (!consultadas.isEmpty()) {
			// Valido bloqueo por exclusion
			for (PropiedadDTO iPropiedadDTO : consultadas) {
				if (iPropiedadDTO.getUsuarioExcluyente() != null || iPropiedadDTO.getRolExcluyente() != null)
					excluidas.add(iPropiedadDTO);
			}
			if (!excluidas.isEmpty()) {
				for (PropiedadDTO iPropiedadDTO : excluidas) {
					// Aqui me di cuenta que estaba borrando todas las propiedades de la plantilla
					// Filtro el valor: en las opciones de una lista (multiple) se puede dejar de un
					// tipo y mostrar el otro
					consultadas.removeIf(x -> (x.getPropiedadValor().compareTo(iPropiedadDTO.getPropiedadValor()) == 0
							&& x.getCampo().compareTo(iPropiedadDTO.getCampo()) == 0
							&& x.getValor().compareTo(iPropiedadDTO.getValor()) == 0));
				}
			}
			// Valido bloqueo por tiempo
			for (PropiedadDTO iPropiedadDTO : consultadas) {
				if (Propiedades.validarBloqueo(iPropiedadDTO))
					validadas.add(iPropiedadDTO);
			}
		}
	
		return validadas;
	
	}

	public List<PropiedadDTO> obtenerEspecialFullPermisosSimplificandoBD( List<DocumentoPlantillaDTO> plantillas, String pProfile)
			throws ServerException {
		return propiedadMapper.obtenerEspecialFullPermisosSimplificandoBD(plantillas, pProfile);
	}

	public List<PropiedadDTO> obtenerEspecialFullPermisos( String plantilla) throws ServerException {
		PropiedadDTO filtroOrden = new PropiedadDTO();
		filtroOrden.setTipo(PropiedadValorDefinidoDTO.PLANTILLA);
		filtroOrden.setCampo(plantilla);
		return propiedadMapper.consultarPermisosFullPlantilla(filtroOrden);
	}

	public List<PropiedadDTO> getToUser( String usuario)
			throws ServerException {
		return propiedadMapper.consultarPermisosUsuario(usuario);
	}

	public List<PropiedadDTO> obtenerPropiedades( String tipo, String entidad, String key, String usuario)
			throws ServerException {
		return obtenerPropiedades( tipo, entidad, key, usuario, null);
	}

	public PropiedadDTO obtenerPropiedad( String tipo, String plantilla, String key, String usuario)
			throws ServerException {
		if (plantilla == null)
			throw new ServerException("El campo esta nulo");
		List<PropiedadDTO> propiedades = obtenerPropiedades( tipo, plantilla, key, usuario);
		if (propiedades == null || propiedades.isEmpty())
			return null;
		return propiedades.get(0);
	}

	public String obtenerUnica( String tipo, String plantilla, String key, String usuario) throws ServerException {
		PropiedadDTO filtroOrden = obtenerPropiedad( tipo, plantilla, key, usuario);
		if (filtroOrden == null)
			return null;
		return filtroOrden.getValor();
	}

	public List<PropiedadDTO> listarPlantillasSimplificar( List<DocumentoPlantillaDTO> plantillas, String usuario)
			throws ServerException {
	
		List<PropiedadDTO> consultadas = propiedadMapper.listarPlantillasSimplificar(plantillas, usuario, new Date());
		return cleanPropertiesFromTimeAndExclusion(consultadas);
	}
	
	private PropiedadValorDefinidoDTO consultarValorDefinido(String tipo, String key) throws ServerException {
		if(types==null) {
			PropiedadValorDefinidoFilterDTO valorDefinidoFilter = new PropiedadValorDefinidoFilterDTO();
			valorDefinidoFilter.setEstado(SharedConstants.STATE_ACTIVE);
			types = propiedadValorDefinidoMapper.listar(valorDefinidoFilter);	
		}
		
		PropiedadValorDefinidoDTO _value = types.stream()
				.filter(p -> p.getCodigo().equals(key) && p.getOrigen().equals(tipo))
				.findFirst()
				.orElse(null);
		if (_value == null)
			throw new ServerException("No se encontro la propiedad " + key + " del tipo " + tipo);
		return _value;
	}

	public List<PropiedadDTO> getByValueWithoutField(String pType, String pKey, String pValue, String pUser)
			throws ServerException {
		if (pKey == null) throw new ServerException("Es encesaario colcoar el pKey");
		PropiedadValorDefinidoDTO valorDefinido = consultarValorDefinido(pType, pKey);
		PropiedadFilterDTO _filter = new PropiedadFilterDTO();
		_filter.setPropiedadValor(valorDefinido.getLlaveTabla());
		_filter.setTipo(pType);
		
		List<PropiedadDTO> _props = getCacheRolProperties( pUser, false, _filter);
		_props.removeIf(p -> p.getValor()==null || !p.getValor().equals(pValue));
		return _props;
	}

}
