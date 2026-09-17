package d3.notification;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import d3.notification.application.ActividadSvc;
import d3.notification.domain.ActividadDTO;
import d3.shared.domain.ServerException;
import d3.users.application.UsuarioSvc;
import d3.users.domain.UsuarioDTO;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/notification")
public class NotificationController {

	private final ActividadSvc actividadService;
	private final UsuarioSvc usuarioService;

	public NotificationController(@Lazy ActividadSvc actividadService, @Lazy UsuarioSvc usuarioService) {
		this.actividadService = actividadService;
		this.usuarioService = usuarioService;
	}

	@GetMapping(value = "/getNotifications")
	public List<ActividadDTO> listUserActivities() throws ServerException {
		return actividadService.listUserActivities();
	}

	@PostMapping(value = "/readActivity")
	public ActividadDTO readActivity(@RequestBody ActividadDTO activity) throws ServerException {
		return actividadService.readActivity(activity.getLlaveTabla());
	}

	@PostMapping(value = "/transfer")
	public ActividadDTO transfer(@RequestBody ActividadDTO asignacion) throws ServerException {
		return actividadService.guardar(asignacion);
	}

	@PostMapping(value = "/userToTransfer")
	public List<UsuarioDTO> usuariosXRol(@RequestBody ActividadDTO activity) throws ServerException {
		if (activity == null)
			throw new ServerException("Porfavor envie el objeto documento");
		if (activity.getDocumento() == null)
			throw new ServerException("Porfavor envie la llave del documento");
		return usuarioService.getUsersState(activity.getDocumento());
	}

}
