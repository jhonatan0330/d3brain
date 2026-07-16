package com.softure.configuration_file.infrastructure;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shared.domain.ServerException;
import com.softure.configuration_file.application.ExportConfigurationFileService;
import com.softure.configuration_file.application.ImportConfigurationFileService;
import com.softure.configuration_file.domain.ExportListRequest;
import com.softure.configuration_file.domain.FileVO;
import org.springframework.context.annotation.Lazy;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/configuration")
public class ConfigurationController {

	private final ExportConfigurationFileService exportService;
	private final ImportConfigurationFileService importService;

	public ConfigurationController(@Lazy ExportConfigurationFileService exportService,
			@Lazy ImportConfigurationFileService importService) {
		this.exportService = exportService;
		this.importService = importService;
	}

	@GetMapping("export")
	private FileVO generateFileWithConfiguration(@RequestHeader("Authorization") String token) throws ServerException {
		return exportService.call(token);
	}

	@PostMapping("module")
	private FileVO generateFileWithConfiguration(@RequestHeader("Authorization") String token,
			@RequestBody ExportListRequest modules) throws ServerException {
		return exportService.call(token, modules);
	}

	@PostMapping("import")
	private FileVO loadConfiguration(@RequestHeader("Authorization") String token, @RequestBody FileVO file)
			throws ServerException {
		return importService.call(token, file);
	}

	@PostMapping("compare")
	private FileVO compare(@RequestHeader("Authorization") String token, @RequestBody FileVO file)
			throws ServerException {
		return importService.compare(token, file);
	}
}
