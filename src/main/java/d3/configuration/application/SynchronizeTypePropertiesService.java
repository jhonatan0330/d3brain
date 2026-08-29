package d3.configuration.application;

import java.util.List;

import org.springframework.stereotype.Service;

import d3.shared.domain.ServerException;
import d3.configuration.domain.HierarchyExporterDTO;
import d3.configuration.domain.LogConfigurationDTO;
import d3.property.application.PropiedadValorDefinidoSvc;
import d3.property.domain.PropiedadDTO;
import d3.property.domain.PropiedadValorDefinidoDTO;
import org.springframework.context.annotation.Lazy;

@Service
public class SynchronizeTypePropertiesService {

	private final PropiedadValorDefinidoSvc typesService;

	public SynchronizeTypePropertiesService(@Lazy PropiedadValorDefinidoSvc typesService) {
		this.typesService = typesService;
	}

	public void call(String token, HierarchyExporterDTO hierarchy, LogConfigurationDTO log, boolean compare)
			throws ServerException {
		typesService.getUserFlex(token);
		List<PropiedadValorDefinidoDTO> localListToErase = typesService.getFullToSynchronize();
		List<PropiedadValorDefinidoDTO> remoteList = hierarchy.getPropertyTypes();
		if (remoteList != null && !remoteList.isEmpty()) {
			log.setRoot("SynchronizeTypeProperties");
			for (PropiedadValorDefinidoDTO remote : remoteList) {
				PropiedadValorDefinidoDTO local = findTemplateInList(localListToErase, remote.getCodigo());
				if (local != null) {
					localListToErase.remove(local);
					changePropertiesIdCode(hierarchy.getProperties(), remote.getLlaveTabla(), local.getLlaveTabla());
					log.info("EXIST " + remote.getCodigo());
				} else {
					if (compare) {
						log.error("COMPARE NOT EXIST " + remote.getCodigo());
					} else {
						PropiedadValorDefinidoDTO newType = new PropiedadValorDefinidoDTO();
						newType.setCodigo(remote.getCodigo());
						newType.setGrupo(remote.getGrupo());
						newType.setIncluirPreloadOrigen(remote.getIncluirPreloadOrigen());
						newType.setMultiple(remote.getMultiple());
						newType.setNombre(remote.getNombre());
						newType.setNecesitaDesarrollo(remote.getNecesitaDesarrollo());
						newType.setOrigen(remote.getOrigen());
						newType.setOrigenCategoria(remote.getOrigenCategoria());
						newType.setPideFechas(remote.getPideFechas());
						newType.setPideRol(remote.getPideRol());
						newType.setPideTiempoBloqueo(remote.getPideTiempoBloqueo());
						newType.setPideUsuario(remote.getPideUsuario());
						newType.setPropiedadBoolean(remote.getPropiedadBoolean());
						newType.setSolicitaMotivo(remote.getSolicitaMotivo());
						newType.setTextOculto(remote.getTextOculto());
						newType = typesService.save(newType);
						changePropertiesIdCode(hierarchy.getProperties(), remote.getLlaveTabla(),
								newType.getLlaveTabla());
						log.info("NEW " + remote.getCodigo());
					}

				}
			}
		}
	}

	private PropiedadValorDefinidoDTO findTemplateInList(List<PropiedadValorDefinidoDTO> array, String code) {
		for (PropiedadValorDefinidoDTO localProcess : array) {
			if (code.compareTo(localProcess.getCodigo()) == 0) {
				return localProcess;
			}
		}
		return null;
	}

	private void changePropertiesIdCode(List<PropiedadDTO> processRemote, String remote, String local) {
		for (PropiedadDTO remoteProcess : processRemote) {
			if (remoteProcess.getPropiedadValor() != null && remoteProcess.getPropiedadValor().compareTo(remote) == 0) {
				remoteProcess.setPropiedadValor(local);
			}
		}

	}
}
