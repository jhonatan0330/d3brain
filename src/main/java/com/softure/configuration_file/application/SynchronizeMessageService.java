package com.softure.configuration_file.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.configuration_file.domain.HierarchyExporterDTO;
import com.softure.mail.application.MensajePlantillaCorreoSvc;
import com.softure.mail.domain.MensajePlantillaCorreoDTO;

@Service
public class SynchronizeMessageService {

	@Autowired private MensajePlantillaCorreoSvc messagesService;
	@Autowired SynchronizePropertiesService propertiesSynchronizeService;

	public void call(String token, HierarchyExporterDTO hierarchy) throws ServerException {
		List<MensajePlantillaCorreoDTO> localListToErase = messagesService.getFullToSynchronize();
		List<MensajePlantillaCorreoDTO> remoteList = hierarchy.getMessages();
		if (remoteList != null && !remoteList.isEmpty()) {
			for (MensajePlantillaCorreoDTO remote : remoteList) {
				MensajePlantillaCorreoDTO local = findTemplateInList(localListToErase, remote.getNombre());
				// Creo el nuevo proceso
				if (local!=null){
					localListToErase.remove(local);
					//changePropertiesIdCode(hierarchy.getProperties(), remote.getLlaveTabla(), local.getLlaveTabla());
				}
				else
				{
					MensajePlantillaCorreoDTO newMessage = new MensajePlantillaCorreoDTO();
					newMessage.setTitulo(remote.getTitulo());
					newMessage.setNombre(remote.getNombre());
					newMessage.setTexto(remote.getTexto());
					newMessage = messagesService.save(newMessage);
					//changePropertiesIdCode(hierarchy.getProperties(), remote.getLlaveTabla(), newType.getLlaveTabla());
				}
			}
		}
	}

	private MensajePlantillaCorreoDTO findTemplateInList(List<MensajePlantillaCorreoDTO> array, String code) {
		for (MensajePlantillaCorreoDTO localProcess : array) {
			if (code.compareTo(localProcess.getNombre()) == 0) {
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
