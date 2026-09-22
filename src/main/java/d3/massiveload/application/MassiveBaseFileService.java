package d3.massiveload.application;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import d3.massiveload.domain.PlantillaBaseResponse;
import d3.process.domain.DocumentoPlantillaCaracteristicaDTO;
import d3.process.domain.TemplateDTO;
import d3.shared.domain.ServerException;
import d3.upload.application.UploadSvc;
import d3.upload.domain.CargaArchivoDTO;

@Service
public class MassiveBaseFileService {

	public static final String FORMATO_XML = "xml";
	public static final String FORMATO_XLSX = "xlsx";

	private final MassiveLoadOrchestratorService orchestratorService;
	private final UploadSvc uploadService;

	public MassiveBaseFileService(@Lazy MassiveLoadOrchestratorService orchestratorService, @Lazy UploadSvc uploadService) {
		this.orchestratorService = orchestratorService;
		this.uploadService = uploadService;
	}

	public PlantillaBaseResponse generarBase(String templateId, String format) throws ServerException {
		TemplateDTO plantilla = orchestratorService.obtenerPlantilla(templateId);
		byte[] bytes;
		String extension;
		if (FORMATO_XML.equalsIgnoreCase(format)) {
			bytes = generarXml(plantilla).getBytes(StandardCharsets.UTF_8);
			extension = FORMATO_XML;
		} else if (FORMATO_XLSX.equalsIgnoreCase(format)) {
			bytes = generarXlsx(plantilla);
			extension = FORMATO_XLSX;
		} else {
			throw new ServerException("Formato no soportado: " + format + ". Use xml o xlsx");
		}
		CargaArchivoDTO registro = uploadService.uploadFileDTO(bytes, plantilla.getNombre() + "." + extension, "files",
				"public");
		if (registro.getUrl() == null)
			throw new ServerException("No se pudo generar la URL del archivo base");
		return new PlantillaBaseResponse(registro.getUrl());
	}

	private String generarXml(TemplateDTO plantilla) {
		StringBuilder xml = new StringBuilder("<root>");
		String nombre = MassiveFileParserService.formatStringXML(plantilla.getCodigo());
		if (plantilla.getCaracteristicas() != null) {
			for (int index = 0; index < 2; index++) {
				xml.append('<').append(nombre).append('>');
				for (DocumentoPlantillaCaracteristicaDTO iCampo : plantilla.getCaracteristicas()) {
					if (!DocumentoPlantillaCaracteristicaDTO.SECCION.equals(iCampo.getFormato())) {
						String campoNombre = MassiveFileParserService.formatStringXML(iCampo.getNombre());
						xml.append('<').append(campoNombre).append('>');
						xml.append(getXMLBase(iCampo.getFormato()));
						xml.append("</").append(campoNombre).append('>');
					}
				}
				xml.append("</").append(nombre).append('>');
			}
		}
		xml.append("</root>");
		return xml.toString();
	}

	private String getXMLBase(String formato) {
		switch (formato) {
		case DocumentoPlantillaCaracteristicaDTO.BINARIO:
			return "0-1";
		case DocumentoPlantillaCaracteristicaDTO.FECHA:
			return new SimpleDateFormat("yyyy/MM/dd").format(new Date());
		case DocumentoPlantillaCaracteristicaDTO.NUMERO:
			return "0";
		case DocumentoPlantillaCaracteristicaDTO.PROCESO:
			return "CODIGO";
		case DocumentoPlantillaCaracteristicaDTO.TEXTO:
			return "TEXTO ";
		case DocumentoPlantillaCaracteristicaDTO.CONFIGURACION:
			return "TEXTO EXACTO DE LA OPCION";
		default:
			return "No implementado";
		}
	}

	private byte[] generarXlsx(TemplateDTO plantilla) throws ServerException {
		try (XSSFWorkbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
			Sheet sheet = workbook.createSheet("Base");
			if (plantilla.getCaracteristicas() != null) {
				Row header = sheet.createRow(0);
				int i = 0;
				for (DocumentoPlantillaCaracteristicaDTO iCampo : plantilla.getCaracteristicas()) {
					if (!DocumentoPlantillaCaracteristicaDTO.SECCION.equals(iCampo.getFormato())) {
						header.createCell(i++).setCellValue(MassiveFileParserService.formatStringXML(iCampo.getNombre()));
					}
				}
			}
			workbook.write(out);
			return out.toByteArray();
		} catch (IOException e) {
			throw new ServerException("Error generando el archivo excel base: " + e.getMessage(), e);
		}
	}

}