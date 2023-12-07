package com.softure.configuration_file.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.configuration_file.domain.HierarchyExporterDTO;
import com.softure.configuration_file.domain.LogConfigurationDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.webservice.application.WebServiceSvc;
import com.softure.webservice.domain.WebServiceDTO;

@Service
public class SynchronizeApiService {

	@Autowired private WebServiceSvc apisService;
	@Autowired private SynchronizePropertiesService propertiesSynchronizeService;

	public void call(String token, HierarchyExporterDTO hierarchy, LogConfigurationDTO log) throws ServerException {
		List<WebServiceDTO> localListToErase = apisService.getFullToSynchronize(null);
		List<WebServiceDTO> remoteList = hierarchy.getApis();
		if (remoteList != null && !remoteList.isEmpty()) {
			log.setRoot("SynchronizeApi");
			for (WebServiceDTO remote : remoteList) {
				WebServiceDTO local = findTemplateInList(localListToErase, remote.getCodigo());
				// Creo el nuevo proceso
				if (local!=null){
					localListToErase.remove(local);
					log.info("EXIST " + remote.getCodigo() + " - " + remote.getNombre());
				}
				else
				{
					WebServiceDTO newType = new WebServiceDTO();
					newType.setCodigo(remote.getCodigo());
					newType.setNombre(remote.getNombre());
					newType.setTemplate(remote.getTemplate());
					newType.setUrl(remote.getUrl());
					newType = apisService.save(newType);
					log.info("NEW " +remote.getCodigo() + " - " + remote.getNombre());
				}
			}
		}
		callAfterCreateAllTemplate(token, hierarchy, log);
	}
	
	private void callAfterCreateAllTemplate(String token, HierarchyExporterDTO hierarchy, LogConfigurationDTO log) throws ServerException {
		List<WebServiceDTO> localListToErase = apisService.getFullToSynchronize(null);
		List<WebServiceDTO> remoteList = hierarchy.getApis();
		if (remoteList != null && !remoteList.isEmpty()) {
			for (WebServiceDTO remote : remoteList) {
				WebServiceDTO local = findTemplateInList(localListToErase, remote.getCodigo());
				// Creo el nuevo proceso
				if (local!=null){
					log.setRoot("SynchronizeApi " + local.getNombre());
					localListToErase.remove(local);
					propertiesSynchronizeService.call(hierarchy, remote.getLlaveTabla(),
							PropiedadValorDefinidoDTO.API_SERVICE, local.getLlaveTabla(), token, log);
				}
			}
		}
	}


	private WebServiceDTO findTemplateInList(List<WebServiceDTO> array, String code) {
		if (array == null) return null;
		for (WebServiceDTO localProcess : array) {
			if (code.compareTo(localProcess.getCodigo()) == 0) {
				return localProcess;
			}
		}
		return null;
	}


	/*
	private void changePropertiesIdCode(List<PropiedadDTO> processRemote, String remote, String local) {
		for (PropiedadDTO remoteProcess : processRemote) {
			if(remoteProcess.getPropiedadValor()!=null && remoteProcess.getPropiedadValor().compareTo(remote)==0) {
				remoteProcess.setPropiedadValor(local);
			}
		}
		
	}*/
}
