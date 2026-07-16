package com.softure.api.application;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.shared.domain.ServerException;
import com.shared.domain.SharedIdResponse;
import com.softure.api.domain.ReportParameterRequest;
import com.softure.api.domain.ReportRequest;
import com.softure.document_execution.application.PedidoVentaSvc;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.report.application.ReporteBaseSvc;
import com.softure.report.domain.ReportDTO;
import com.softure.report.domain.ReporteBaseDTO;
import org.springframework.context.annotation.Lazy;

@Service
public class ApiGetReportService {

	private final DocumentoPlantillaSvc templateService;
	private final ReporteBaseSvc reportService;
	private final PedidoVentaSvc documentService;

	public ApiGetReportService(@Lazy DocumentoPlantillaSvc templateService, @Lazy ReporteBaseSvc reportService,
			@Lazy PedidoVentaSvc documentService) {
		this.templateService = templateService;
		this.reportService = reportService;
		this.documentService = documentService;
	}

	public SharedIdResponse call(String token, ReportRequest filter) throws ServerException {

		if (token == null || token.isEmpty())
			throw new ServerException("Es obligatorio enviar un token valido");
		DocumentoPlantillaDTO templateBD = templateService.consultarPorCodigo(filter.getTemplate());
		if (templateBD == null)
			throw new ServerException("No se encontro una plantilla con el codigo " + filter.getTemplate());

		ReporteBaseDTO reportBD = reportService.getByCode(filter.getCode(), templateBD.getLlaveTabla());
		if (reportBD == null)
			throw new ServerException("No se encontro un reporte con el codigo " + filter.getCode()
					+ " en la plantilla " + templateBD.getNombre());

		PedidoVentaDTO document = documentService.consultaXId(filter.getDocumentId());
		if (document == null)
			throw new ServerException("No se encontro un documento con el id " + filter.getDocumentId());
		// Para la factura electronica de ROA se imprime el reporte de otro
		// if(document.getPlantilla().compareTo(templateBD.getLlaveTabla())!=0) throw
		// new ServerException("El documento seleccionado no concuerda con la plantilla
		// seleccionada");

		reportBD = reportService.validateReport(reportBD.getLlaveTabla(), token);

		ReportDTO resultado;
		try {
			Map<String, Object> jasperParameters = null;
			if (filter.getParameters() != null && !filter.getParameters().isEmpty()) {
				jasperParameters = new HashMap<String, Object>();
				for (ReportParameterRequest element : filter.getParameters()) {
					jasperParameters.put(element.getParameter(), element.getValue());
				}
			}
			resultado = reportService.generarReporte(reportBD, filter.getDocumentId(), jasperParameters, token);
		} catch (Exception e) {
			throw new ServerException(e.getMessage());
		}
		if (resultado.getData() == null)
			throw new ServerException("Se ha presentado un error en la generacion del reporte ");
		return new SharedIdResponse(resultado.getData().getError(), resultado.getData().getUrl());

	}

}
