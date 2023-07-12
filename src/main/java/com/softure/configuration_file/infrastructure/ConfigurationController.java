package com.softure.configuration_file.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softure.configuration_file.application.ExportConfigurationFileService;
import com.softure.configuration_file.application.ImportConfigurationFileService;
import com.softure.configuration_file.domain.FileVO;
import com.softure.java.dto.exception.ServerException;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/configuration")
public class ConfigurationController {

	@Autowired private ExportConfigurationFileService exportService;
	@Autowired private ImportConfigurationFileService importService;
	
	@PostMapping("export")
	private FileVO generateFileWithConfiguration(@RequestHeader("Authorization") String token)throws ServerException  {
		return exportService.call(token);
	}
	
	@PostMapping("import")
	private FileVO loadConfiguration(@RequestHeader("Authorization") String token,@RequestBody FileVO file)throws ServerException  {
		return importService.call(token, file);
	}
}
