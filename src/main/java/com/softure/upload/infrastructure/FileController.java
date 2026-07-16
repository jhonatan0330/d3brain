package com.softure.upload.infrastructure;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shared.domain.ServerException;
import com.softure.logisticpymes.application.ServidorSvc;
import com.softure.logisticpymes.domain.ServidorDTO;
import org.springframework.context.annotation.Lazy;

@RestController
@RequestMapping("/files")
public class FileController {

	private final ServidorSvc servidorService;

	public FileController(@Lazy ServidorSvc servidorService) {
		this.servidorService = servidorService;
	}

	@GetMapping("/{visibility}/{type}/{year}/{month}/{day}/{filename:.+}")
	public ResponseEntity<?> getFile(@PathVariable("visibility") String pVisibility, @PathVariable("type") String pType,
			@PathVariable("year") String pYear, @PathVariable("month") String pMonth, @PathVariable("day") String pDay,
			@PathVariable("filename") String pFilename) throws ServerException {

		ServidorDTO _server = servidorService.resolveLocalServer();

		// Construye la ruta absoluta
		File file = new File(_server.getBase() + File.separator + pVisibility + File.separator + pType + File.separator
				+ pYear + File.separator + pMonth + File.separator + pDay + File.separator + pFilename);

		if (!file.exists() || !file.isFile()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Archivo no encontrado: " + pFilename);
		}

		try {
			String mimeType = Files.probeContentType(file.toPath());
			InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(mimeType != null ? mimeType : "application/octet-stream"))
					.contentLength(file.length()).body(resource);

		} catch (IOException e) {
			throw new ServerException("Error al leer el archivo: " + e.getMessage(), e);
		}
	}
}
