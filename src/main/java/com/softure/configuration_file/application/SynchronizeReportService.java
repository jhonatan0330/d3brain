package com.softure.configuration_file.application;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softure.configuration_file.domain.HierarchyExporterDTO;
import com.softure.java.dto.exception.ServerException;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.report.application.ReporteBaseSvc;
import com.softure.report.domain.ReporteBaseDTO;

@Service
public class SynchronizeReportService {

	@Autowired ReporteBaseSvc reportService;
	@Autowired SynchronizePropertiesService propertiesSynchronizeService;
	
	public void call(String token, HierarchyExporterDTO hierarchy, String remoteTemplate, String localTemplate) throws ServerException {
		List<ReporteBaseDTO> localListToErase = getReportsFromTemplate (reportService.getFullToSynchronize(), localTemplate);
		List<ReporteBaseDTO> remoteList = getReportsFromTemplate( hierarchy.getReports(), remoteTemplate);
		
		if (remoteList != null && !remoteList.isEmpty()) {
			for (ReporteBaseDTO remote : remoteList) {
				ReporteBaseDTO local = findTemplateInList(localListToErase, remote.getCodigo());
				// Creo el nuevo proceso
				if (local!=null){
					localListToErase.remove(local);
				}
				else
				{
					ReporteBaseDTO newReport = new ReporteBaseDTO();
					newReport.setPlantilla(localTemplate);
					newReport.setCodigo(remote.getCodigo());
					newReport.setDescripcion(remote.getDescripcion());
					newReport.setSoloExistente(remote.getSoloExistente());
					newReport.setNombre(remote.getNombre());
					newReport.setVariables(remote.getVariables());
					newReport = reportService.save(newReport);
				}
			}
		}
		callAfterCreateAll(token, hierarchy, remoteTemplate, localTemplate);
	}
	private void callAfterCreateAll(String token, HierarchyExporterDTO hierarchy, String remoteTemplate, String localTemplate) throws ServerException {
		List<ReporteBaseDTO> localListToErase = getReportsFromTemplate (reportService.getFullToSynchronize(), localTemplate);
		List<ReporteBaseDTO> remoteList = getReportsFromTemplate( hierarchy.getReports(), remoteTemplate);
		if (remoteList != null && !remoteList.isEmpty()) {
			for (ReporteBaseDTO remote : remoteList) {
				ReporteBaseDTO local = findTemplateInList(localListToErase, remote.getCodigo());
				// Creo el nuevo proceso
				if (local!=null){
					System.out.println(local.getNombre());
					localListToErase.remove(local);
					propertiesSynchronizeService.call(hierarchy.getProperties(), remote.getLlaveTabla(),
							PropiedadValorDefinidoDTO.REPORTE, local.getLlaveTabla(), token);
				}
			}
		}
	}

	private List<ReporteBaseDTO> getReportsFromTemplate(List<ReporteBaseDTO> fullToSynchronize, String template) {
		if(fullToSynchronize ==null || fullToSynchronize.isEmpty())	return null;
		return fullToSynchronize.stream()
			      .filter(report -> (report.getPlantilla().compareTo(template)==0))
			      .collect(Collectors.toList());
	}

	public void callAfterRol(String token, HierarchyExporterDTO hierarchy) throws ServerException {
		List<ReporteBaseDTO> localToErase = reportService.getFullToSynchronize();
		List<ReporteBaseDTO> remoteList = hierarchy.getReports();
		if (remoteList != null && !remoteList.isEmpty()) {
			for (ReporteBaseDTO remote : remoteList) {
				ReporteBaseDTO local = findTemplateInList(localToErase, remote.getCodigo());
				// Creo el nuevo proceso
				if (local!=null){
					localToErase.remove(local);
					propertiesSynchronizeService.call(hierarchy.getProperties(), remote.getLlaveTabla(),
							PropiedadValorDefinidoDTO.REPORTE, local.getLlaveTabla(), token);
				}
			}
		}
	}

	private ReporteBaseDTO findTemplateInList(List<ReporteBaseDTO> array, String code) {
		for (ReporteBaseDTO localProcess : array) {
			if (code.compareTo(localProcess.getCodigo()) == 0) {
				return localProcess;
			}
		}
		return null;
	}
}
