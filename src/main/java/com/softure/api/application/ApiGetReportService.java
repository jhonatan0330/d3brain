package com.softure.api.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shared.domain.SharedIdResponse;
import com.softure.api.domain.ReportRequest;
import com.softure.document_execution.application.PedidoVentaSvc;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.java.dto.exception.ServerException;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.report.application.ReporteBaseSvc;
import com.softure.report.domain.ReportDTO;
import com.softure.report.domain.ReporteBaseDTO;

@Service
public class ApiGetReportService {

	@Autowired private DocumentoPlantillaSvc templateService;
	@Autowired private ReporteBaseSvc reportService;
	@Autowired private PedidoVentaSvc documentService;
	
	public SharedIdResponse call(String token, ReportRequest filter) throws ServerException {
		
		if(token==null || token.isEmpty()) throw new ServerException("Es obligatorio enviar un token valido");
		DocumentoPlantillaDTO templateBD = templateService.consultarPorCodigo(filter.getTemplate());
		if(templateBD==null) throw new ServerException("No se encontro una plantilla con el codigo " + filter.getTemplate());
	
		ReporteBaseDTO reportBD = reportService.getByCode(filter.getCode(), templateBD.getLlaveTabla());
		if(reportBD==null) throw new ServerException("No se encontro un reporte con el codigo " + filter.getCode() + " en la plantilla " + templateBD.getNombre());
		
		PedidoVentaDTO document = documentService.consultaXId(filter.getDocumentId());
		if(document==null) throw new ServerException("No se encontro un documento con el id " + filter.getDocumentId());
		if(document.getPlantilla().compareTo(templateBD.getLlaveTabla())!=0) throw new ServerException("El documento seleccionado no concuerda con la plantilla seleccionada");
		
		
		reportBD = reportService.validateReport(reportBD.getLlaveTabla(), token);
		
		ReportDTO resultado;
		try {
			resultado = reportService.generarReporte(reportBD, filter.getDocumentId(), null, token);
		} catch (Exception e) {
			throw new ServerException(e.getMessage());
		}
		if(resultado.getData()==null ) throw new ServerException("Se ha presentado un error en la generacion del reporte ");
		return new SharedIdResponse(resultado.getData().getError(), resultado.getData().getUrl());
		
	}

	
	


}
