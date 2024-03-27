package com.softure.notification;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shared.domain.ServerException;
import com.softure.logisticpymes.application.UsuarioSvc;
import com.softure.logisticpymes.domain.UsuarioDTO;
import com.softure.notification.application.ActividadSvc;
import com.softure.notification.domain.ActividadDTO;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/notification")
public class NotificationController {

	@Autowired private ActividadSvc actividadService;
	@Autowired private UsuarioSvc usuarioService;

	@GetMapping(value="/getNotifications")
	public List<ActividadDTO> listUserActivities(@RequestHeader("Authorization") String token) throws ServerException {
		return actividadService.listUserActivities(token);
	}
	
	@PostMapping(value="/readActivity")
	public ActividadDTO readActivity(@RequestBody ActividadDTO activity, @RequestHeader("Authorization") String token) throws ServerException {
		return actividadService.readActivity(activity.getLlaveTabla(), token);
	}
	
	@PostMapping(value="/transfer")
	public ActividadDTO transfer(@RequestBody ActividadDTO asignacion, @RequestHeader("Authorization") String token)  throws ServerException  {
		return actividadService.guardar(asignacion, token);	
	}
	
	@PostMapping(value="/userToTransfer")
	public List<UsuarioDTO> usuariosXRol(@RequestBody ActividadDTO activity, @RequestHeader("Authorization") String token)  throws ServerException  {
		if(activity==null) throw new ServerException("Porfavor envie el objeto documento");
		if(activity.getDocumento()==null) throw new ServerException("Porfavor envie la llave del documento");
		return usuarioService.getUsersState(activity.getDocumento());
	}
	
}
