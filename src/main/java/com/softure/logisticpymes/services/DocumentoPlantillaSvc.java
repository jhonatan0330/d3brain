package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import java.util.Date;
import java.util.ArrayList;

import com.softure.java.cons.ConstantesGenerales;
import com.softure.logisticpymes.dto.DocumentoPlantillaCaracteristicaDTO;
import com.softure.logisticpymes.dto.ProcesoEstadoDTO;
import com.softure.logisticpymes.dto.ProcesoTransicionDTO;
import com.softure.logisticpymes.dto.PropiedadDTO;
import com.softure.logisticpymes.dto.PropiedadValorDefinidoDTO;
import com.softure.logisticpymes.dto.ReporteBaseDTO;
import com.softure.logisticpymes.dto.RolAccesoDTO;
import com.softure.logisticpymes.dto.filter.RolAccesoFilterDTO;
import com.softure.logisticpymes.dto.filter.ProcesoEstadoFilterDTO;
import com.softure.logisticpymes.dto.filter.ProcesoTransicionFilterDTO;
import com.softure.logisticpymes.services.adapter.Propiedades;
import com.softure.java.services.SoftureUtil;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.DocumentoPlantillaDTO;
import com.softure.logisticpymes.dto.filter.DocumentoPlantillaFilterDTO;
import com.softure.logisticpymes.persistence.DocumentoPlantillaMapper;

@Service("documentoPlantillaService")
public class DocumentoPlantillaSvc extends BasicSvc<DocumentoPlantillaDTO, DocumentoPlantillaFilterDTO> {
	
	@Autowired
	private DocumentoPlantillaMapper documentoPlantillaMapper;
	
	// BEGIN region servicesDocumentoPlantilla
	@Autowired private DocumentoPlantillaCaracteristicaSvc caracteristicaService;
	@Autowired private PropiedadSvc configuracionSvc;
	@Autowired private ProcesoEstadoSvc estadoService;
	@Autowired private RolAccesoSvc rolService;
	@Autowired private ReporteBaseSvc reporteService;
	@Autowired private ProcesoTransicionSvc transicionService;
	// END region servicesDocumentoPlantilla

	@Override
	public DocumentoPlantillaDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. DocumentoPlantilla");
		DocumentoPlantillaFilterDTO dto = new DocumentoPlantillaFilterDTO();
		dto.setLlaveTabla(llave);
		return documentoPlantillaMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = documentoPlantillaMapper;
	}
	
	@Override
	public DocumentoPlantillaDTO activar(DocumentoPlantillaDTO dto, String token) throws ServerException {
		// BEGIN DocumentoPlantilla_activar
		return super.activar(dto, token);
		// END DocumentoPlantilla_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DocumentoPlantillaDTO actualizar( DocumentoPlantillaDTO dto, String token) throws ServerException {
		// BEGIN DocumentoPlantilla_actualizar
		dto.setCodigo(SoftureUtil.formatFunction(dto.getCodigo()).toUpperCase());
		dto = super.actualizar(dto, token);
		PropiedadDTO filtro = new PropiedadDTO();
		filtro.setValor(dto.getLlaveTabla());
		filtro.setTexto(dto.getNombre());
		configuracionSvc.actualizarValorPropiedad(filtro);
		return dto;
		// END DocumentoPlantilla_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DocumentoPlantillaDTO inactivar(DocumentoPlantillaDTO dto, String token) throws ServerException {
		// BEGIN DocumentoPlantilla_inactivar
		ProcesoTransicionFilterDTO validar = new ProcesoTransicionFilterDTO();
		validar.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		validar.setPlantilla(dto.getLlaveTabla());
		List<ProcesoTransicionDTO> pUsados = transicionService.listarConsulta(validar);
		if(pUsados == null || !pUsados.isEmpty()) {
			String mensaje = "La plantilla se esta usando en las siguientes transiciones : \n";
			for (ProcesoTransicionDTO iUsado : pUsados) {
				mensaje = mensaje + "Proceso: " + iUsado.getProcesoNombre() + "  -> Transicion: " + iUsado.getNombre() + "\n";
			}
			throw new ServerException(mensaje);
		}
		RolAccesoFilterDTO rolFilter = new RolAccesoFilterDTO();
		rolFilter.setPlantilla(dto.getLlaveTabla());
		rolFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		RolAccesoDTO rol = rolService.consultaUnica(rolFilter);
		if(rol!=null){
			rolService.inactivar(rol, token);
		}
		return super.inactivar(dto, token);
		// END DocumentoPlantilla_inactivar
	}
	
	@Override
	public DocumentoPlantillaDTO consultaUnica(DocumentoPlantillaFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(DocumentoPlantillaFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<DocumentoPlantillaDTO> listarConsulta(DocumentoPlantillaFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	
	public List<DocumentoPlantillaDTO> consultaUsuario(DocumentoPlantillaFilterDTO dto)throws ServerException{
		// BEGIN region consultaUsuario
		return listarPlantillasUsuario(dto, false);
		// END region consultaUsuario
	}
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DocumentoPlantillaDTO obtenerCampos(DocumentoPlantillaDTO dto, String token)throws ServerException{
		// BEGIN region obtenerCampos
		dto.setCaracteristicas(caracteristicaService.listarCamposPlantillaConComplementos(dto.getLlaveTabla(), token));
		int order = 0;
		boolean modificar = !Propiedades.obtenerValor(dto, Propiedades.PERMISO_PLANTILLA_MODIFICAR).isEmpty();
		//En caso que busca desde la interfaz
		if(dto.getPropiedades()==null) {
			modificar = true;
		}
		for (DocumentoPlantillaCaracteristicaDTO  campo: dto.getCaracteristicas()){
			order ++;
			campo.setOrden(order);
			if(campo.getImagen()==null) {
				String imagenCampo = Propiedades.obtenerValor(campo, Propiedades.PLANTILLA_AUXILIAR);
				if(!imagenCampo.isEmpty()) {
					DocumentoPlantillaDTO plantillaAuxiliar = consultaXId(imagenCampo);
					if(plantillaAuxiliar!=null) campo.setImagen(plantillaAuxiliar.getImagen());
				}
			}
			if(!modificar) Propiedades.retirarPropiedad(campo, Propiedades.PERMISO_CAMPO_MODIFICABLE);
		}
		return dto;
		// END region obtenerCampos
	}
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DocumentoPlantillaDTO duplicar(DocumentoPlantillaDTO dto, String token)throws ServerException{
		// BEGIN region duplicar
		DocumentoPlantillaDTO bd = consultaXId(dto.getLlaveTabla());
		// Copio plantilla
		DocumentoPlantillaDTO copy = new DocumentoPlantillaDTO();
		copy.setNombre("COPY_" + bd.getNombre());
		copy.setImagen(bd.getImagen());
		copy.setObjetivo(bd.getObjetivo());
		
		configurarInicioPlantilla(copy);
		copy = super.save(copy);
		// Copio campos
		bd.setCaracteristicas(caracteristicaService.listarCamposPlantillaConComplementos(bd.getLlaveTabla(), null));
		for (DocumentoPlantillaCaracteristicaDTO iCampo : bd.getCaracteristicas()) {
			DocumentoPlantillaCaracteristicaDTO newCampo = new DocumentoPlantillaCaracteristicaDTO();
			newCampo.setCodigo(iCampo.getCodigo());
			newCampo.setFormato(iCampo.getFormato());
			newCampo.setImagen(iCampo.getImagen());
			newCampo.setNombre(iCampo.getNombre());
			newCampo.setObjetivo(iCampo.getObjetivo());
			newCampo.setOrden(iCampo.getOrden());
			newCampo.setPlantilla(copy.getLlaveTabla());
			newCampo = caracteristicaService.guardar(newCampo, token);
			newCampo.setPropiedades(configuracionSvc.copiarPropiedades(iCampo.getPropiedades(), newCampo.getLlaveTabla(), token));
		}
		// Primero las propiedades de rol para evitar duplicar
		RolAccesoFilterDTO rolFiltroFilter = new RolAccesoFilterDTO();
		rolFiltroFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		rolFiltroFilter.setPlantilla(bd.getLlaveTabla());
		RolAccesoDTO rolFiltro = rolService.consultaUnica(rolFiltroFilter);
		if(rolFiltro!=null) {
			RolAccesoDTO newRol = new RolAccesoDTO();
			newRol.setMinutosSesion(rolFiltro.getMinutosSesion());
			newRol.setPermisosCompletos(rolFiltro.getPermisosCompletos());
			newRol.setPlantilla(copy.getLlaveTabla());
			newRol = rolService.guardar(newRol, token);
			rolFiltro.setPropiedades( configuracionSvc.obtenerPropiedades(PropiedadValorDefinidoDTO.ROL, rolFiltro.getLlaveTabla(),null, null));
			configuracionSvc.copiarPropiedades(rolFiltro.getPropiedades(), newRol.getLlaveTabla(), token);
		}
		// Copio propiedades plantilla
		bd.setPropiedades(obtenerPropiedadesPlantilla(bd.getLlaveTabla(), null));
		copy.setPropiedades(configuracionSvc.copiarPropiedades(bd.getPropiedades(), copy.getLlaveTabla(), token));
		// Copio reportes
		bd.setReportes(reporteService.listarDisponiblesDocumento(bd.getLlaveTabla()));
		for (ReporteBaseDTO iReporte : bd.getReportes()) {
			ReporteBaseDTO newReporte = new ReporteBaseDTO();
			newReporte.setCodigo(iReporte.getCodigo());
			newReporte.setDescripcion(iReporte.getDescripcion());
			//newReporte.setJasperText(iReporte.getJasperText());
			newReporte.setNombre(iReporte.getNombre());
			newReporte.setPlantilla(copy.getLlaveTabla());
			newReporte.setSoloExistente(iReporte.getSoloExistente());
			newReporte.setVariables(iReporte.getVariables());
			newReporte = reporteService.guardar(newReporte, token);
			configuracionSvc.copiarPropiedades(iReporte.getPropiedades(), newReporte.getLlaveTabla(), token);
		}
		
		return copy;
		// END region duplicar
	}
	public List<DocumentoPlantillaDTO> consultaAdministrador(DocumentoPlantillaFilterDTO dto)throws ServerException{
		// BEGIN region consultaAdministrador
		boolean todosPermisos = rolService.usuarioPermisosCompletos(dto.getSecurityToken());
		if(!todosPermisos) throw new ServerException("En los roles que tienes asignados no tienes un rol que tenga permisos de consultar todas las plantillas");
		return listarPlantillasUsuario(dto, true);
		// END region consultaAdministrador
	}

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public DocumentoPlantillaDTO guardar(DocumentoPlantillaDTO dto, String token) throws ServerException {
		// BEGIN DocumentoPlantilla_guardar
		configurarInicioPlantilla(dto);
		dto = super.guardar(dto, token);
		return dto;
		// END DocumentoPlantilla_guardar
	}

// BEGIN region aditionalMethods
	
	public List<DocumentoPlantillaDTO> listarPlantillaRol(DocumentoPlantillaFilterDTO dto)throws ServerException{
		if(dto==null || dto.getSecurityToken()==null) throw new ServerException("Revise la configuracion del dto filtro");
		try {
			return documentoPlantillaMapper.listarMenu(dto); 
		}catch (Exception e) {
			throw new ServerException(e.getCause().getMessage());
		}
	}
	
	public DocumentoPlantillaDTO obtenerConfiguracionSinCampos(DocumentoPlantillaFilterDTO dto, boolean fullPermisos)throws ServerException{
		if(dto.getLlaveTabla()==null) throw new ServerException("No se puede realizar la consulta sin id de la plantilla");
		DocumentoPlantillaDTO plantilla = consultaUnica(dto);
		if(plantilla==null) throw new ServerException("Consulta de la plantilla incorrecta");
		if(plantilla.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)!=0) throw new ServerException("La plantilla se encuentra inactiva");
		//plantilla.setSecurityToken(dto.getSecurityToken());
		if(fullPermisos) {
			plantilla.setPropiedades(configuracionSvc.obtenerEspecialFullPermisos(dto.getLlaveTabla()));
		}else {
			plantilla.setPropiedades(obtenerPropiedadesPlantilla(plantilla.getLlaveTabla(), dto.getSecurityToken()));			
		}
		if(plantilla.getPropiedades()==null || plantilla.getPropiedades().isEmpty()) throw new ServerException("El usuario no tiene permiso sobre el documento " + plantilla.getLlaveTabla());
		return plantilla;
	}
	

	private List<PropiedadDTO> obtenerPropiedadesPlantilla(String plantilla, String token) throws ServerException {
		//si el token es null que traiga todos principalmente para copiar
		String usuario = null;
		if(token !=null) usuario = getUserFlex(token);
		List<PropiedadDTO> propiedades = configuracionSvc.obtenerPropiedades(PropiedadValorDefinidoDTO.PLANTILLA, plantilla, null, usuario);
		if(propiedades==null) propiedades = new ArrayList<PropiedadDTO>();
		return propiedades;
	}
	
	
	
	public void configurarInicioPlantilla(DocumentoPlantillaDTO dto) throws ServerException {
		//Coloco una imagen por defecto
		if(dto.getImagen()==null) dto.setImagen(ConstantesGenerales.LOGO);
		if(dto.getCodigo()==null) {
			DocumentoPlantillaFilterDTO filtroCantidad = new DocumentoPlantillaFilterDTO();
			int cantidadCampos = contarResultados(filtroCantidad) +1;
			dto.setCodigo("F"+ cantidadCampos);
		}
		dto.setCodigo(SoftureUtil.formatFunction(dto.getCodigo()).toUpperCase());
	}
	
	public String crearCampoProcesos(String plantilla, String token) throws ServerException {
		return caracteristicaService.crearCampoProcesos(plantilla, token);
	}
	
	private List<ProcesoEstadoDTO> crearEstadosBasicos() throws ServerException {
		System.out.println ("Cargando estados basicos");
		List<ProcesoEstadoDTO> estados;
		ProcesoEstadoDTO activo = new ProcesoEstadoDTO();
		activo.setEstadoDocumento(ConstantesGenerales.ESTADO_ACTIVO);
		activo.setNombre("ACTIVO");
		ProcesoEstadoDTO inactivo = new ProcesoEstadoDTO();
		inactivo.setEstadoDocumento(ConstantesGenerales.ESTADO_INACTIVO);
		inactivo.setNombre("INACTIVO");
		estados = new ArrayList<ProcesoEstadoDTO>();
		estados.add(activo);
		estados.add(inactivo);
		return estados;
	}
	
	public List<DocumentoPlantillaDTO> listarPlantillasUsuario(DocumentoPlantillaFilterDTO dto, boolean todosPermisos)throws ServerException{
		//boolean todosPermisos = rolService.usuarioPermisosCompletos(dto.getSecurityToken());
		String usuario = null;
		if(dto.getSecurityToken() !=null) usuario = getUserFlex(dto.getSecurityToken());
		List<DocumentoPlantillaDTO> plantillasPermitidas = null;
		if(todosPermisos) {
			DocumentoPlantillaFilterDTO filtroFullFilter = new DocumentoPlantillaFilterDTO();
			filtroFullFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			plantillasPermitidas =listarConsulta(filtroFullFilter);
		}else {
			plantillasPermitidas = listarPlantillaRol(dto);
		}
		List<DocumentoPlantillaDTO> result = new ArrayList<DocumentoPlantillaDTO>();
		boolean nuevaPlantilla = true;
		if(plantillasPermitidas!=null && plantillasPermitidas.size()!=0){
			//obtengo todo y didtribuyo para evitar tantas consultas a la BD y para optimizar tiempo
			
			List<ReporteBaseDTO> reportes = reporteService.listarMenu();
			
			ProcesoEstadoFilterDTO filtroEstado = new ProcesoEstadoFilterDTO();
			filtroEstado.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			filtroEstado.setTipo(ProcesoEstadoDTO.TIPO_ESTADO);
			List<ProcesoEstadoDTO> estados = estadoService.listarConsulta(filtroEstado);
			
			ProcesoTransicionFilterDTO filtroTransicion = new ProcesoTransicionFilterDTO();
			filtroTransicion.setSecurityToken((todosPermisos)?null:dto.getSecurityToken());
			filtroTransicion.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
			
			List<ProcesoTransicionDTO> transiciones = transicionService.listarTransicionesRol(filtroTransicion);
			List<ProcesoTransicionDTO> transicionesIniciales = transicionService.listarTransaccionesIniciales(null);
			
			List<PropiedadDTO> todasPropiedadesEvitandoConsultaBD = null;
			if(todosPermisos) {
				todasPropiedadesEvitandoConsultaBD = configuracionSvc.obtenerEspecialFullPermisosSimplificandoBD(plantillasPermitidas);
			}else {
				List<PropiedadDTO> consultadas = configuracionSvc.listarPlantillasSimplificar(plantillasPermitidas, usuario);
				List<PropiedadDTO> validadas = new ArrayList<PropiedadDTO>();
				if(!consultadas.isEmpty()) {
					for (PropiedadDTO iPropiedadDTO : consultadas) {
						if(Propiedades.validarBloqueo(iPropiedadDTO))validadas.add(iPropiedadDTO);
					}
				}
				todasPropiedadesEvitandoConsultaBD =  validadas;
			}
			List<PropiedadDTO> todasPropiedadesEstados = configuracionSvc.obtenerPropiedadesSinEntidad(PropiedadValorDefinidoDTO.ESTADO, null, null, usuario);
			for(DocumentoPlantillaDTO iplantillaPermitida : plantillasPermitidas){
				nuevaPlantilla = true;
				for(DocumentoPlantillaDTO iBD : result){
					if(iplantillaPermitida.getLlaveTabla()==null || iplantillaPermitida.getLlaveTabla().compareTo(iBD.getLlaveTabla())==0) {
						nuevaPlantilla = false;
						break;
					}
				}
				if(nuevaPlantilla) {
					System.out.println (new Date().toString() + " : " + iplantillaPermitida.getNombre());
					//iplantillaPermitida.setSecurityToken(dto.getSecurityToken());
					if(iplantillaPermitida.getLlaveTabla()==null) throw new ServerException("No se puede realizar la consulta sin id de la plantilla");
					if(iplantillaPermitida.getEstado().compareTo(ConstantesGenerales.ESTADO_ACTIVO)!=0) throw new ServerException("La plantilla se encuentra inactiva");
					iplantillaPermitida.setPropiedades(new ArrayList<PropiedadDTO>());
					for (PropiedadDTO propiedadDTO : todasPropiedadesEvitandoConsultaBD) {
						if(propiedadDTO.getCampo().compareTo(iplantillaPermitida.getLlaveTabla())==0) 
							iplantillaPermitida.getPropiedades().add(propiedadDTO);
					}
					if(iplantillaPermitida.getPropiedades().isEmpty()) throw new ServerException("El usuario no tiene permiso sobre el documento " + iplantillaPermitida.getNombre());
					String procesoInicial = null;
					if(transicionesIniciales!=null && !transicionesIniciales.isEmpty()){
						for (ProcesoTransicionDTO procesoTransicionDTO : transicionesIniciales) {
							if(procesoTransicionDTO.getPlantilla().compareTo(iplantillaPermitida.getLlaveTabla())==0) {
								procesoInicial = procesoTransicionDTO.getProceso();
								break;
							}
						}
					}
					if(procesoInicial!=null) {
						statesFromProcess(estados, transiciones, todasPropiedadesEstados, iplantillaPermitida,
								procesoInicial);
					}
					if (iplantillaPermitida.getEstados()==null) iplantillaPermitida.setEstados(crearEstadosBasicos());

					//iplantillaPermitida.setReportes(reporteService.listarDisponiblesDocumento(iplantillaPermitida.getLlaveTabla(), false));
					if(reportes!=null && !reportes.isEmpty()) {
						for (ReporteBaseDTO reporteBaseDTO : reportes) {
							if(reporteBaseDTO.getPlantilla().compareTo(iplantillaPermitida.getLlaveTabla())==0) {
								if(iplantillaPermitida.getReportes()==null) iplantillaPermitida.setReportes(new ArrayList<ReporteBaseDTO>());
								iplantillaPermitida.getReportes().add(reporteBaseDTO);
							}
						}
					}
					
					result.add(iplantillaPermitida);
				}
			}
			for(DocumentoPlantillaDTO iplantillaPermitida : plantillasPermitidas){
				if(iplantillaPermitida.getLlaveTabla()==null) {
					statesFromProcess(estados, transiciones, todasPropiedadesEstados, iplantillaPermitida,
							iplantillaPermitida.getProceso());
					result.add(0, iplantillaPermitida);
				}
			}
		}
		return result;
	}

	private void statesFromProcess(List<ProcesoEstadoDTO> estados, List<ProcesoTransicionDTO> transiciones,
			List<PropiedadDTO> todasPropiedadesEstados, DocumentoPlantillaDTO iplantillaPermitida,
			String procesoInicial) {
		for (ProcesoEstadoDTO procesoEstadoDTO : estados) {
			if(procesoEstadoDTO.getProceso().compareTo(procesoInicial)==0) {
				if(iplantillaPermitida.getEstados()==null) iplantillaPermitida.setEstados(new ArrayList<ProcesoEstadoDTO>());
				if(procesoEstadoDTO.getPropiedades()==null) {
					procesoEstadoDTO.setPropiedades(new ArrayList<PropiedadDTO>());
					for (PropiedadDTO propiedadDTO : todasPropiedadesEstados) {
						if(propiedadDTO.getCampo().compareTo(procesoEstadoDTO.getLlaveTabla())==0) 
							procesoEstadoDTO.getPropiedades().add(propiedadDTO);
					}
				}
				if(procesoEstadoDTO.getTransiciones()==null) {
					procesoEstadoDTO.setTransiciones(new ArrayList<ProcesoTransicionDTO>());
					for (ProcesoTransicionDTO procesoTransicionDTO : transiciones) {
						if(procesoTransicionDTO.getEstadoPartida()!=null && procesoTransicionDTO.getEstadoPartida().compareTo(procesoEstadoDTO.getLlaveTabla())==0) {
							procesoEstadoDTO.getTransiciones().add(procesoTransicionDTO);
						}
					}
				}
				//Valida que quede empty no null, despues de validar borra
				iplantillaPermitida.getEstados().add(procesoEstadoDTO);
			}
		}
	}
	
// END region aditionalMethods

}