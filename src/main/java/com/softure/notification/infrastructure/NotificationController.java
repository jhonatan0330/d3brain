package com.softure.notification.infrastructure;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softure.java.dto.exception.ServerException;
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

	@RequestMapping(value="/getNotifications", method=RequestMethod.GET)
	public List<ActividadDTO> listUserActivities(@RequestHeader("Authorization") String token) throws ServerException {
		return actividadService.listUserActivities(token);
	}
	
	@RequestMapping(value="/readActivity", method=RequestMethod.POST)
	public ActividadDTO readActivity(@RequestBody ActividadDTO activity, @RequestHeader("Authorization") String token) throws ServerException {
		return actividadService.readActivity(activity.getLlaveTabla(), token);
	}
	
	@RequestMapping(value="/transfer", method=RequestMethod.POST)
	public ActividadDTO transfer(@RequestBody ActividadDTO asignacion, @RequestHeader("Authorization") String token)  throws ServerException  {
		return actividadService.guardar(asignacion, token);	
	}
	
	@RequestMapping(value="/userToTransfer", method=RequestMethod.POST)
	public List<UsuarioDTO> usuariosXRol(@RequestBody ActividadDTO activity, @RequestHeader("Authorization") String token)  throws ServerException  {
		if(activity==null) throw new ServerException("Porfavor envie el objeto documento");
		if(activity.getDocumento()==null) throw new ServerException("Porfavor envie la llave del documento");
		return usuarioService.getUsersState(activity.getDocumento());
	}
	
}
