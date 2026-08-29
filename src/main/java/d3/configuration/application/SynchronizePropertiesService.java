package d3.configuration.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import d3.shared.domain.ServerException;
import d3.configuration.domain.HierarchyExporterDTO;
import d3.configuration.domain.LogConfigurationDTO;
import d3.shared.application.D3Utils;
import d3.property.application.PropertyGetWithCacheService;
import d3.property.application.PropiedadSvc;
import d3.property.application.PropiedadValorDefinidoSvc;
import d3.property.domain.PropiedadDTO;
import d3.property.domain.PropiedadValorDefinidoDTO;
import org.springframework.context.annotation.Lazy;

@Service
public class SynchronizePropertiesService {

	private final PropiedadSvc propertiesService;
	private final PropertyGetWithCacheService cacheService;
	private final PropiedadValorDefinidoSvc typeService;

	public SynchronizePropertiesService(@Lazy PropiedadSvc propertiesService,
			@Lazy PropertyGetWithCacheService cacheService, @Lazy PropiedadValorDefinidoSvc typeService) {
		this.propertiesService = propertiesService;
		this.cacheService = cacheService;
		this.typeService = typeService;
	}

	public void call(HierarchyExporterDTO hierarchy, String entityRemote, String type, String entityLocal, String token,
			LogConfigurationDTO log, boolean compare) throws ServerException {
		List<PropiedadDTO> localPropertiesToErase = cacheService.obtenerPropiedades(type, entityLocal, null, null);
		List<PropiedadDTO> propertiesRemote = filterPropertiesToTypeAndEntity(hierarchy.getProperties(), type,
				entityRemote);
		// Saco un listado de las propiedades nuevas
		// Saco un listado de las propiedades a borrar
		if (propertiesRemote != null && !propertiesRemote.isEmpty()) {
			for (PropiedadDTO remoteProperty : propertiesRemote) {
				PropiedadDTO findProperty = findPropertyInList(localPropertiesToErase,
						remoteProperty.getPropiedadValor(), remoteProperty.getValor(), remoteProperty.getTexto(),
						remoteProperty.getRol(), remoteProperty.getMotivo());
				// Creo la nueva propiedad
				if (findProperty != null) {
					localPropertiesToErase.remove(findProperty);
					log.info("EXIST PROPERTY " + remoteProperty.getPropiedadValor());
					remoteProperty.setUsuarioEliminacion("YA");
					remoteProperty.setUsuarioCreacion(findProperty.getLlaveTabla());
				} else {
					if (compare) {
						PropiedadValorDefinidoDTO typeVD = typeService.consultaXId(remoteProperty.getPropiedadValor());
						if (typeVD == null) {
							log.error("COMPARE NOT EXIST PROPERTY TYPE VALUE " + remoteProperty.getPropiedadValor());
						} else {
							log.error("COMPARE NOT EXIST PROPERTY " + remoteProperty.getPropiedadValor() + " - "
									+ typeVD.getNombre() + " val: "
									+ D3Utils.recortar(remoteProperty.getValor(), 20) + " mot: "
									+ remoteProperty.getMotivo());
						}

					} else {
						PropiedadDTO newProperty = new PropiedadDTO();
						newProperty.setCampo(entityLocal);
						newProperty.setMotivo(remoteProperty.getMotivo());
						newProperty.setPropiedadValor(remoteProperty.getPropiedadValor());
						newProperty.setRol(remoteProperty.getRol());
						newProperty.setRolExcluyente(remoteProperty.getRolExcluyente());
						if (remoteProperty.getTexto() == null) {
							newProperty.setValor(remoteProperty.getValor());
						} else {
							switch (remoteProperty.getPropiedadValor()) {
							case "PROP_06":
							case "PROP_29":
							case "PROP_37":
							case "PROP_41":
							case "PROP_51":
							case "PROP_54":
							case "PROP_58":
							case "PROP_59":
							case "PROP_69":
							case "PROP_74":
							case "PROP_90":
							case "PROP_118":
							case "PROP_120":
							case "PROP_122":
							case "PROP_125":
							case "PROP_139":
							case "PROP_140":
							case "PROP_146":
							case "PROP_147":
							case "PROP_156":
							case "PROP_159":
							case "PROP_160":
							case "PROP_164":
							case "PROP_175":
							case "PROP_182":
							case "PROP_187":
							case "PROP_189":
							case "PROP_185":
							case "PROP_192":
							case "PROP_212":
							case "PROP_235":
							case "PROP_237":
							case "PROP_224":
							case "PROP_249":
							case "PROP_273":
							case "PROP_297": {
								newProperty.setValor(remoteProperty.getValor());
								newProperty.setTexto(remoteProperty.getTexto());
								break;
							}
							default:
								newProperty.setValor(remoteProperty.getTexto());
							}
						}
						try {
							findProperty = propertiesService.guardar(newProperty, token);
							log.info("NEW PROPERTY " + remoteProperty.getPropiedadValor());
							remoteProperty.setUsuarioEliminacion("YA");
							remoteProperty.setUsuarioCreacion(findProperty.getLlaveTabla());
						} catch (Exception e) {
							String _msgError = "La propiedad " + remoteProperty.getNombre() + "("
									+ remoteProperty.getPropiedadValor() + ") ";
							if (remoteProperty.getTexto() != null) {
								_msgError = _msgError + " Con texto " + remoteProperty.getTexto();
							} else {
								_msgError = _msgError + " Con valor "
										+ D3Utils.recortar(remoteProperty.getValor(), 20);
							}
							_msgError = _msgError + " Genera el siguiente error : " + e.getMessage();
							log.error(_msgError);

							if (remoteProperty.getPropiedadValor().compareTo("PROP_242") == 0) {
								log.error("NUL");
							}
						}
					}
				}

			}
		}
		// elimino las propiedades que no estaban en la sincronizacion y no tenian
		// usuario especifico
		// for (PropiedadDTO propiedadDTO : localPropertiesToErase) {
		// propertiesService.inactivar(propiedadDTO, token);
		// }

	}

	private List<PropiedadDTO> filterPropertiesToTypeAndEntity(List<PropiedadDTO> propertiesFull, String type,
			String entity) {
		if (propertiesFull == null || propertiesFull.isEmpty())
			return null;
		return propertiesFull.stream().filter(
				property -> (property.getCampo().compareTo(entity) == 0 && property.getTipo().compareTo(type) == 0))
				.collect(Collectors.toList());
	}

	private PropiedadDTO findPropertyInList(List<PropiedadDTO> array, String code, String value, String text,
			String role, String motive) {
		for (PropiedadDTO property : array) {
			if ((role == null && property.getRol() == null)
					|| (property.getRol() != null && role != null && role.compareTo(property.getRol()) == 0)) {
				// Lo del motivo sucedio porque en los apis tengo mucahs poropiedades
				// referenciadas y se confundian
				if ((motive == null && property.getMotivo() == null) || (property.getMotivo() != null && motive != null
						&& motive.compareTo(property.getMotivo()) == 0)) {
					if (code.compareTo(property.getPropiedadValor()) == 0) {
						if (property.getTexto() != null && text != null && property.getTexto().compareTo(text) == 0)
							return property;
						if (property.getValor() != null && property.getValor().compareTo(value) == 0)
							return property;
					}
				}

			}
		}
		return null;
	}

}
