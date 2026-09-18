package d3.users;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import d3.shared.domain.ServerException;
import d3.upload.domain.CargaArchivoDTO;
import d3.users.application.UsuarioSvc;
import d3.users.domain.UsuarioDTO;
import d3.users.domain.UsuarioFilterDTO;

@RestController
@RequestMapping("/users")
public class UsersController {

	private final UsuarioSvc userService;

	public UsersController(@Lazy UsuarioSvc userService) {
		this.userService = userService;
	}

	@PostMapping(value = "/getUsers")
	public List<UsuarioDTO> getUsers(@RequestBody UsuarioFilterDTO pFilter) throws ServerException {
		return userService.listarRol(pFilter);
	}

	@GetMapping("/{userId}")
	public UsuarioDTO getUserById(@PathVariable(name = "userId") String pUserId) throws ServerException {
		return userService.consultaXId(pUserId);
	}
	
	@GetMapping("/document/{documentId}")
	public UsuarioDTO getUserByDocument(@PathVariable(name = "documentId") String pDocumentId) {
		return userService.getUserByDocument(pDocumentId);
	}
	
	@PostMapping(value = "/userToTransfer/{documentId}")
	public List<UsuarioDTO> usuariosXRol(@PathVariable String documentId) throws ServerException {
		if (documentId == null)
			throw new ServerException("Porfavor envie la llave del documento");
		return userService.getUsersState(documentId);
	}
	
	@PostMapping(value = "/changePicture")
	public UsuarioDTO cambiarImagen(@RequestBody CargaArchivoDTO request) throws ServerException {
		if (request == null || request.getUrl() == null || request.getUrl().isEmpty())
			throw new ServerException("La url de la imagen se encuentra vacia");
		return userService.changePicture(request.getUrl());
	}

}
