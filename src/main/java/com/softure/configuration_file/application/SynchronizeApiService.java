package com.softure.configuration_file.application;

import java.util.List;

import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.configuration_file.domain.HierarchyExporterDTO;
import com.softure.configuration_file.domain.LogConfigurationDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.webservice.application.WebServiceSvc;
import com.softure.webservice.domain.WebServiceDTO;
import org.springframework.context.annotation.Lazy;

@Service
public class SynchronizeApiService {

	private final WebServiceSvc apisService;
	private final SynchronizePropertiesService propertiesSynchronizeService;

	public SynchronizeApiService(@Lazy WebServiceSvc apisService,
			@Lazy SynchronizePropertiesService propertiesSynchronizeService) {
		this.apisService = apisService;
		this.propertiesSynchronizeService = propertiesSynchronizeService;
	}

	public void call(String token, HierarchyExporterDTO hierarchy, LogConfigurationDTO log, boolean compare)
			throws ServerException {
		List<WebServiceDTO> localListToErase = apisService.getFullToSynchronize(null);
		List<WebServiceDTO> remoteList = hierarchy.getApis();
		if (remoteList != null && !remoteList.isEmpty()) {
			log.setRoot("SynchronizeApi");
			for (WebServiceDTO remote : remoteList) {
				if (remote.getCodigo() == null || remote.getCodigo().isEmpty()) {
					log.error("API CODE IS NULL " + remote.getNombre());
					continue;
				}
				WebServiceDTO local = findTemplateInList(localListToErase, remote.getCodigo());
				// Creo el nuevo proceso
				if (local != null) {
					localListToErase.remove(local);
					log.info("EXIST API " + remote.getCodigo() + " - " + remote.getNombre());
				} else {
					if (compare) {
						log.error("COMPARE NOT EXIST API " + remote.getCodigo() + " - " + remote.getNombre());
					} else {
						WebServiceDTO newType = new WebServiceDTO();
						newType.setCodigo(remote.getCodigo());
						newType.setNombre(remote.getNombre());
						newType.setProceso(remote.getProceso());
						newType = apisService.save(newType);
						log.info("NEW API " + remote.getCodigo() + " - " + remote.getNombre());
					}

				}
			}
		}
		callAfterCreateAllTemplate(token, hierarchy, log, compare);
	}

	private void callAfterCreateAllTemplate(String token, HierarchyExporterDTO hierarchy, LogConfigurationDTO log,
			boolean compare) throws ServerException {
		List<WebServiceDTO> localListToErase = apisService.getFullToSynchronize(null);
		List<WebServiceDTO> remoteList = hierarchy.getApis();
		if (remoteList != null && !remoteList.isEmpty()) {
			for (WebServiceDTO remote : remoteList) {
				WebServiceDTO local = findTemplateInList(localListToErase, remote.getCodigo());
				// Creo el nuevo proceso
				if (local != null) {
					log.setRoot("SynchronizeApi " + local.getNombre());
					localListToErase.remove(local);
					propertiesSynchronizeService.call(hierarchy, remote.getLlaveTabla(),
							PropiedadValorDefinidoDTO.API_SERVICE, local.getLlaveTabla(), token, log, compare);
				}
			}
		}
	}

	private WebServiceDTO findTemplateInList(List<WebServiceDTO> array, String code) {
		if (array == null || array.isEmpty() || code == null)
			return null;
		for (WebServiceDTO localProcess : array) {
			if (localProcess != null && code.compareTo(localProcess.getCodigo()) == 0) {
				return localProcess;
			}
		}
		return null;
	}

	/*
	 * private void changePropertiesIdCode(List<PropiedadDTO> processRemote, String
	 * remote, String local) { for (PropiedadDTO remoteProcess : processRemote) {
	 * if(remoteProcess.getPropiedadValor()!=null &&
	 * remoteProcess.getPropiedadValor().compareTo(remote)==0) {
	 * remoteProcess.setPropiedadValor(local); } }
	 * 
	 * }
	 */
}
