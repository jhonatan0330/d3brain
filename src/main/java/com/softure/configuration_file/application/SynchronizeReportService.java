package com.softure.configuration_file.application;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.softure.configuration_file.domain.HierarchyExporterDTO;
import com.softure.configuration_file.domain.LogConfigurationDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.report.application.ReporteBaseSvc;
import com.softure.report.domain.ReporteBaseDTO;

@Service
public class SynchronizeReportService {

	@Autowired ReporteBaseSvc reportService;
	@Autowired SynchronizePropertiesService propertiesSynchronizeService;
	
	public void call(String token, HierarchyExporterDTO hierarchy, LogConfigurationDTO log) throws ServerException {
		List<ReporteBaseDTO> localListToErase = reportService.getFullToSynchronize(null);
		List<ReporteBaseDTO> remoteList = hierarchy.getReports();
		
		if (remoteList != null && !remoteList.isEmpty()) {
			for (ReporteBaseDTO remote : remoteList) {
				ReporteBaseDTO local = findTemplateInList(localListToErase, remote.getCodigo(), remote.getNombre());
				// Creo el nuevo proceso
				if (local!=null){
					localListToErase.remove(local);
					log.info("EXIST REPORT" + remote.getCodigo() + " - " + remote.getNombre());
				}
				else
				{
					ReporteBaseDTO newReport = new ReporteBaseDTO();
					newReport.setPlantilla(remote.getPlantilla());
					newReport.setCodigo(remote.getCodigo());
					newReport.setDescripcion(remote.getDescripcion());
					newReport.setSoloExistente(remote.getSoloExistente());
					newReport.setNombre(remote.getNombre());
					newReport.setVariables(remote.getVariables());
					try {
						newReport = reportService.save(newReport);
						log.info("NEW REPORT" + remote.getCodigo() + " - " + remote.getNombre());
					} catch (Exception e) {
						log.error(remote.getCodigo() + " - " + remote.getNombre() + " : " + e.getMessage());
					}
				}
			}
		}
		callAfterCreateAll(token, hierarchy, log);
	}
	
	private void callAfterCreateAll(String token, HierarchyExporterDTO hierarchy, LogConfigurationDTO log) throws ServerException {
		List<ReporteBaseDTO> localListToErase = reportService.getFullToSynchronize(null);
		List<ReporteBaseDTO> remoteList = hierarchy.getReports();
		if (remoteList != null && !remoteList.isEmpty()) {
			for (ReporteBaseDTO remote : remoteList) {
				ReporteBaseDTO local = findTemplateInList(localListToErase, remote.getCodigo(), remote.getNombre());
				// Creo el nuevo proceso
				if (local!=null){
					localListToErase.remove(local);
					propertiesSynchronizeService.call(hierarchy, remote.getLlaveTabla(),
							PropiedadValorDefinidoDTO.REPORTE, local.getLlaveTabla(), token, log);
				}
			}
		}
	}

	/*
	private List<ReporteBaseDTO> getReportsFromTemplate(List<ReporteBaseDTO> fullToSynchronize, String template) {
		if(fullToSynchronize ==null || fullToSynchronize.isEmpty())	return null;
		return fullToSynchronize.stream()
			      .filter(report -> (report.getPlantilla().compareTo(template)==0))
			      .collect(Collectors.toList());
	}*/

	public void callAfterRol(String token, HierarchyExporterDTO hierarchy, LogConfigurationDTO log) throws ServerException {
		List<ReporteBaseDTO> localToErase = reportService.getFullToSynchronize(null);
		List<ReporteBaseDTO> remoteList = hierarchy.getReports();
		if (remoteList != null && !remoteList.isEmpty()) {
			for (ReporteBaseDTO remote : remoteList) {
				ReporteBaseDTO local = findTemplateInList(localToErase, remote.getCodigo(), remote.getNombre());
				// Creo el nuevo proceso
				if (local!=null){
					localToErase.remove(local);
					propertiesSynchronizeService.call(hierarchy, remote.getLlaveTabla(),
							PropiedadValorDefinidoDTO.REPORTE, local.getLlaveTabla(), token, log);
				}
			}
		}
	}

	private ReporteBaseDTO findTemplateInList(List<ReporteBaseDTO> array, String code, String nombre) {
		for (ReporteBaseDTO localProcess : array) {
			if (code.compareTo(localProcess.getCodigo()) == 0 && nombre.compareTo(localProcess.getNombre()) == 0) {
				return localProcess;
			}
		}
		return null;
	}
}
