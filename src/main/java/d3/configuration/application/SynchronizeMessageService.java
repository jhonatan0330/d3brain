package d3.configuration.application;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.shared.domain.ServerException;
import d3.configuration.domain.HierarchyExporterDTO;
import d3.configuration.domain.LogConfigurationDTO;
import d3.mail.application.MensajePlantillaCorreoSvc;
import d3.mail.domain.MensajePlantillaCorreoDTO;

@Service
public class SynchronizeMessageService {

	private final MensajePlantillaCorreoSvc messagesService;

	public SynchronizeMessageService(@Lazy MensajePlantillaCorreoSvc messagesService) {
		this.messagesService = messagesService;
	}

	public void call(String token, HierarchyExporterDTO hierarchy, LogConfigurationDTO log, boolean compare)
			throws ServerException {
		List<MensajePlantillaCorreoDTO> localListToErase = messagesService.getFullToSynchronize(null);
		List<MensajePlantillaCorreoDTO> remoteList = hierarchy.getMessages();
		if (remoteList != null && !remoteList.isEmpty()) {
			log.setRoot("SynchronizeMessage");
			for (MensajePlantillaCorreoDTO remote : remoteList) {
				MensajePlantillaCorreoDTO local = findTemplateInList(localListToErase, remote.getNombre());
				// Creo el nuevo proceso
				if (local != null) {
					localListToErase.remove(local);
					// changePropertiesIdCode(hierarchy.getProperties(), remote.getLlaveTabla(),
					// local.getLlaveTabla());
					log.info("EXIST MESSAGE " + remote.getNombre());
				} else {
					if (compare) {
						log.error("COMPARE NOT EXIST MESSAGE " + remote.getNombre());
					} else {
						MensajePlantillaCorreoDTO newMessage = new MensajePlantillaCorreoDTO();
						newMessage.setTitulo(remote.getTitulo());
						newMessage.setNombre(remote.getNombre());
						newMessage.setTexto(remote.getTexto());
						newMessage = messagesService.save(newMessage);
						// changePropertiesIdCode(hierarchy.getProperties(), remote.getLlaveTabla(),
						// newType.getLlaveTabla());
						log.info("NEW MESSAGE " + remote.getNombre());
					}

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
	 * private void changePropertiesIdCode(List<PropiedadDTO> processRemote, String
	 * remote, String local) { for (PropiedadDTO remoteProcess : processRemote) {
	 * if(remoteProcess.getPropiedadValor()!=null &&
	 * remoteProcess.getPropiedadValor().compareTo(remote)==0) {
	 * remoteProcess.setPropiedadValor(local); } } }
	 */
}
