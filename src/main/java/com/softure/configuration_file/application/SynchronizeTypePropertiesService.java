package com.softure.configuration_file.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.configuration_file.domain.HierarchyExporterDTO;
import com.softure.property.application.PropiedadValorDefinidoSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;

@Service
public class SynchronizeTypePropertiesService {

	@Autowired private PropiedadValorDefinidoSvc typesService;

	public void call(String token, HierarchyExporterDTO hierarchy) throws ServerException {
		List<PropiedadValorDefinidoDTO> localListToErase = typesService.getFullToSynchronize();
		List<PropiedadValorDefinidoDTO> remoteList = hierarchy.getPropertyTypes();
		if (remoteList != null && !remoteList.isEmpty()) {
			for (PropiedadValorDefinidoDTO remote : remoteList) {
				PropiedadValorDefinidoDTO local = findTemplateInList(localListToErase, remote.getCodigo());
				// Creo el nuevo proceso
				if (local!=null){
					localListToErase.remove(local);
					changePropertiesIdCode(hierarchy.getProperties(), remote.getLlaveTabla(), local.getLlaveTabla());
				}
				else
				{
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
					changePropertiesIdCode(hierarchy.getProperties(), remote.getLlaveTabla(), newType.getLlaveTabla());
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
			if(remoteProcess.getPropiedadValor()!=null && remoteProcess.getPropiedadValor().compareTo(remote)==0) {
				remoteProcess.setPropiedadValor(local);
			}
		}
		
	}
}
