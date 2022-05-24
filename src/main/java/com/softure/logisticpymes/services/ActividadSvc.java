package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import java.util.Date;
import java.util.ArrayList;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.logisticpymes.dto.UsuarioDTO;
import com.softure.logisticpymes.dto.PedidoVentaDTO;
import com.softure.logisticpymes.dto.filter.PedidoVentaFilterDTO;
import com.softure.logisticpymes.services.refactor.CallListDocumentWithFilters;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.ActividadDTO;
import com.softure.logisticpymes.dto.filter.ActividadFilterDTO;
import com.softure.logisticpymes.persistence.ActividadMapper;

@Service("actividadService")
public class ActividadSvc extends BasicSvc<ActividadDTO, ActividadFilterDTO> {
	
	@Autowired
	private ActividadMapper actividadMapper;
	
	// BEGIN region servicesActividad
	@Autowired private MensajeSvc mensajeSvc;
	@Autowired private PedidoVentaSvc pedidoService;
	@Autowired private UsuarioSvc usuarioService;
	@Autowired private CallListDocumentWithFilters listDocumentWithFiltersFunction;
	// END region servicesActividad

	@Override
	public ActividadDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. Actividad");
		ActividadFilterDTO dto = new ActividadFilterDTO();
		dto.setLlaveTabla(llave);
		return actividadMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = actividadMapper;
	}
	
	@Override
	public ActividadDTO activar(ActividadDTO dto, String token) throws ServerException {
		// BEGIN Actividad_activar
		return super.activar(dto, token);
		// END Actividad_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ActividadDTO actualizar( ActividadDTO dto, String token) throws ServerException {
		// BEGIN Actividad_actualizar
		return super.update(dto);
		// END Actividad_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ActividadDTO inactivar(ActividadDTO dto, String token) throws ServerException {
		// BEGIN Actividad_inactivar
		dto.setEstado(ConstantesGenerales.ESTADO_INACTIVO);
        dto.setFechaInactivo(new Date());
        dto.setUsuarioInactivo(getUserFlex(token));
        return super.update(dto);
		// END Actividad_inactivar
	}
	
	@Override
	public ActividadDTO consultaUnica(ActividadFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(ActividadFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<ActividadDTO> listarConsulta(ActividadFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ActividadDTO guardar(ActividadDTO dto, String token) throws ServerException {
		// BEGIN Actividad_guardar
		//Esto solo se usa para cuando cambio de responsable un documento, puede que si no se usa bien se duplique el mensaje
		crearActividad(dto, token);
		PedidoVentaFilterDTO pedidoFilter = new PedidoVentaFilterDTO();
		pedidoFilter.setLlaveTabla(dto.getDocumento());
		pedidoFilter.setSecurityToken(token);
		PedidoVentaDTO pedido = pedidoService.consultaCompleta(pedidoFilter);
		//Esto es para que se vean losparametros del mensaje
		PedidoVentaDTO pedidoModificador = new PedidoVentaDTO();
		pedidoModificador.setNombre(pedido.getNombre());
		pedidoModificador.setDescripcion(dto.getComentario());
		pedidoModificador.setPlantilla(pedido.getPlantilla());
		mensajeSvc.gestionarMensajes(pedido, null, usuarioService.consultaXId(dto.getResponsable()), pedidoModificador, token);
		return dto;
		// END Actividad_guardar
	}

// BEGIN region aditionalMethods
	public UsuarioDTO crearActividad(ActividadDTO dto, String token) throws ServerException {
		if(dto.getDocumento()==null) throw new ServerException("Al guardar el responsable no viene el documento");
        ActividadFilterDTO anteriorFilter = new ActividadFilterDTO();
        anteriorFilter.setDocumento(dto.getDocumento());
        anteriorFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
        ActividadDTO anterior = consultaUnica(anteriorFilter);
        if(anterior!=null){
            if (dto.getResponsable()!=null){
                if(anterior.getResponsable().compareTo(dto.getResponsable())==0) return validarUsuario(anterior.getResponsable());
            }
            //anterior.setSecurityToken(dto.getSecurityToken());
            inactivar(anterior, token);
        }else{
            if(dto.getResponsable()==null) return null; //throw new ServerException("Al guardar el responsable no viene el usuario");
        }
        if(dto.getResponsable()!=null) {
            dto.setFechaRegistro(new Date());
            dto.setUsuarioRegistro(getUserFlex(token));
            dto = super.save(dto);
            return validarUsuario(dto.getResponsable());
        }
        return null;
	}
	
	private UsuarioDTO validarUsuario(String id) throws ServerException {
		UsuarioDTO usuario = usuarioService.consultaXId(id);
        if(usuario==null) throw new ServerException("Error al consultar el usuario responsable por llave");
        if(usuario.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)!=0) throw new ServerException("El responsable que desea asignar no esta activo.\n" + usuario.getNombre());
        return usuario;
	}
	
	public List<ActividadDTO> listUserActivities(String token) throws ServerException {
		ActividadFilterDTO pd = new ActividadFilterDTO();
		pd.setResponsable(getUserFlex(token));
		pd.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		List<ActividadDTO> result = listarConsulta(pd);
		if(!result.isEmpty()) {
			List<String> ids = new ArrayList<>();
			for (ActividadDTO iActivity : result) {
				ids.add(iActivity.getDocumento());
			}
			List<PedidoVentaDTO> documentos = listDocumentWithFiltersFunction.listar2Activity(ids, token);
			for (ActividadDTO iActivity : result) {
				for (PedidoVentaDTO pedidoVentaDTO : documentos) {
					if(iActivity.getDocumento().compareTo(pedidoVentaDTO.getLlaveTabla())==0) {
						iActivity.setDocumentoDTO(pedidoVentaDTO);
						break;
					}
				}
				
			}
		}
		return result;
	}

	public ActividadDTO readActivity(String id, String token) throws ServerException {
		ActividadDTO bd = consultaXId(id);
		if(bd.getFechaLeido()!=null) return bd;
		bd.setFechaLeido(new Date());
		bd = update(bd);
		bd.setDocumentoDTO(pedidoService.consultaXId(bd.getDocumento()));
		return bd;
	}
// END region aditionalMethods

}