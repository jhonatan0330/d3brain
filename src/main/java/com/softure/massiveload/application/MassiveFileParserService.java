package com.softure.massiveload.application;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shared.domain.ServerException;
import com.softure.process_form.domain.DocumentoPlantillaDTO;

@Service
public class MassiveFileParserService {

	public List<Map<String, String>> parse(MultipartFile file, DocumentoPlantillaDTO template) throws ServerException {
		String name = (file.getOriginalFilename() == null) ? "" : file.getOriginalFilename().toLowerCase();
		try {
			if (name.endsWith(".json"))
				return parseJson(file.getInputStream());
			if (name.endsWith(".xlsx"))
				return parseExcel(new XSSFWorkbook(file.getInputStream()), template);
			if (name.endsWith(".xls"))
				return parseExcel(new HSSFWorkbook(file.getInputStream()), template);
			if (name.endsWith(".csv"))
				return parseCsv(file, template);
			throw new ServerException(
					"Formato de archivo no soportado. Use .xlsx, .xls, .csv o .json para la carga masiva");
		} catch (ServerException e) {
			throw e;
		} catch (Exception e) {
			throw new ServerException("Error leyendo el archivo de carga masiva: " + e.getMessage());
		}
	}

	private List<Map<String, String>> parseJson(InputStream is) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(is);
		List<Map<String, String>> result = new ArrayList<>();
		if (root.isArray()) {
			Iterator<JsonNode> it = root.elements();
			while (it.hasNext()) {
				JsonNode node = it.next();
				Map<String, String> row = new LinkedHashMap<>();
				Iterator<String> names = node.fieldNames();
				while (names.hasNext()) {
					String name = names.next();
					row.put(name, node.get(name).asText());
				}
				result.add(row);
			}
		} else if (root.isObject()) {
			Map<String, String> row = new LinkedHashMap<>();
			Iterator<String> names = root.fieldNames();
			while (names.hasNext()) {
				String name = names.next();
				row.put(name, root.get(name).asText());
			}
			result.add(row);
		}
		return result;
	}

	private List<Map<String, String>> parseExcel(Workbook wb, DocumentoPlantillaDTO template) {
		Sheet sheet = wb.getSheetAt(0);
		List<Map<String, String>> result = new ArrayList<>();
		Row header = sheet.getRow(0);
		List<String> headers = new ArrayList<>();
		if (header != null) {
			for (Cell c : header) {
				headers.add(formatStringXML(getCellValue(c)));
			}
		}
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {
			Row r = sheet.getRow(i);
			if (r == null)
				continue;
			Map<String, String> row = new LinkedHashMap<>();
			boolean empty = true;
			for (int j = 0; j < headers.size(); j++) {
				Cell c = r.getCell(j);
				String val = getCellValue(c);
				if (val != null && !val.isEmpty())
					empty = false;
				row.put(headers.get(j), val);
			}
			if (!empty)
				result.add(row);
		}
		return result;
	}

	private List<Map<String, String>> parseCsv(MultipartFile file, DocumentoPlantillaDTO template) throws IOException {
		CSVFormat fmt = CSVFormat.Builder.create(CSVFormat.DEFAULT).setHeader().setSkipHeaderRecord(true)
				.setIgnoreHeaderCase(true).build();
		try (CSVParser parser = CSVParser.parse(file.getInputStream(), java.nio.charset.StandardCharsets.UTF_8, fmt)) {
			List<Map<String, String>> result = new ArrayList<>();
			for (CSVRecord rec : parser) {
				Map<String, String> row = new LinkedHashMap<>();
				boolean empty = true;
				for (String h : parser.getHeaderNames()) {
					String val = rec.get(h);
					if (val != null && !val.isEmpty())
						empty = false;
					row.put(formatStringXML(h), val);
				}
				if (!empty)
					result.add(row);
			}
			return result;
		}
	}

	private String getCellValue(Cell c) {
		if (c == null)
			return null;
		switch (c.getCellType()) {
		case STRING:
			return c.getStringCellValue();
		case NUMERIC:
			if (DateUtil.isCellDateFormatted(c))
				return new SimpleDateFormat("yyyy-MM-dd").format(c.getDateCellValue());
			double d = c.getNumericCellValue();
			if (d == Math.floor(d) && !Double.isInfinite(d))
				return String.valueOf((long) d);
			return String.valueOf(d);
		case BOOLEAN:
			return String.valueOf(c.getBooleanCellValue());
		default:
			return null;
		}
	}

	public static String formatStringXML(String texto) {
		if (texto == null)
			return "EMPTY";
		texto = texto.replace(" ", "_").replace("Ñ", "N").replace("ñ", "n").replace("(", "").replace(")", "")
				.replace(":", "").trim();
		String de = "ÁÃÀÄÂÉËÈÊÍÏÌÎÓÖÒÔÚÜÙÛÑÇáãàäâéëèêíïìîóöòôúüùûñç";
		String a = "AAAAAEEEEIIIIOOOOUUUUNCaaaaaeeeeiiiioooouuuunc";
		StringBuilder sb = new StringBuilder();
		for (char ch : texto.toCharArray()) {
			int idx = de.indexOf(ch);
			sb.append(idx >= 0 ? a.charAt(idx) : ch);
		}
		return sb.toString();
	}
}
