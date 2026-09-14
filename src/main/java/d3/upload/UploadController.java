package d3.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import d3.shared.domain.ServerException;
import d3.upload.application.UploadSvc;
import d3.upload.domain.CargaArchivoDTO;
import d3.users.application.ServidorSvc;
import d3.users.domain.ServidorDTO;

@RestController
@RequestMapping("/files")
public class UploadController {

	private final ServidorSvc servidorService;
	private final UploadSvc uploadService;

	public UploadController(@Lazy ServidorSvc servidorService, @Lazy UploadSvc uploadService) {
		this.servidorService = servidorService;
		this.uploadService = uploadService;
	}

	@PostMapping("/upload")
	public CargaArchivoDTO subirArchivo(@RequestParam("file") MultipartFile pFile,
			@RequestHeader(name = "Authorization", required = false) String token) throws ServerException {
		if (pFile.isEmpty())
			throw new ServerException("You failed to upload because the file was empty.");
		try {
			return uploadService.uploadFileDTO(pFile.getBytes(), pFile.getOriginalFilename(), token, null, "public"); 
		} catch (IOException e) {
			throw new ServerException(e.getMessage());
		}
	}

	@GetMapping("/{visibility}/{type}/{year}/{month}/{day}/{filename:.+}")
	public ResponseEntity<?> getFile(@PathVariable("visibility") String pVisibility, @PathVariable("type") String pType,
			@PathVariable("year") String pYear, @PathVariable("month") String pMonth, @PathVariable("day") String pDay,
			@PathVariable("filename") String pFilename) throws ServerException {

		ServidorDTO _server = servidorService.resolveLocalServer();

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