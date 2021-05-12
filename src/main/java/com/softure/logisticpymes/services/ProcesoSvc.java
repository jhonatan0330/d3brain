package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import java.util.ArrayList;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.logisticpymes.dto.ProcesoEstadoDTO;
import com.softure.logisticpymes.dto.ProcesoTransicionDTO;
import com.softure.logisticpymes.dto.PropiedadDTO;
import com.softure.logisticpymes.dto.PropiedadValorDefinidoDTO;
import com.softure.logisticpymes.dto.filter.ProcesoEstadoFilterDTO;
import com.softure.logisticpymes.dto.filter.ProcesoTransicionFilterDTO;
import com.softure.logisticpymes.services.adapter.ProcesoHelperJson;
import com.softure.logisticpymes.services.adapter.Propiedades;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.ProcesoDTO;
import com.softure.logisticpymes.dto.filter.ProcesoFilterDTO;
import com.softure.logisticpymes.persistence.ProcesoMapper;

@Service("procesoService")
public class ProcesoSvc extends BasicSvc<ProcesoDTO, ProcesoFilterDTO> {
	
	@Autowired
	private ProcesoMapper procesoMapper;
	
	// BEGIN region servicesProceso
	@Autowired private ProcesoEstadoSvc estadoService;
	@Autowired private ProcesoTransicionSvc transicionService;
	@Autowired private PropiedadSvc propiedadService;
	@Autowired private ProcesoHelperJson helper;
	// END region servicesProceso

	@Override
	public ProcesoDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. Proceso");
		ProcesoFilterDTO dto = new ProcesoFilterDTO();
		dto.setLlaveTabla(llave);
		return procesoMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = procesoMapper;
	}
	
	@Override
	public ProcesoDTO activar(ProcesoDTO dto, String token) throws ServerException {
		// BEGIN Proceso_activar
		return super.activar(dto, token);
		// END Proceso_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProcesoDTO actualizar( ProcesoDTO dto, String token) throws ServerException {
		// BEGIN Proceso_actualizar
		validarMacroproceso(dto.getMacroproceso());
		dto = super.actualizar(dto, token);
		organizar(dto, token);
		return dto;
		// END Proceso_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProcesoDTO inactivar(ProcesoDTO dto, String token) throws ServerException {
		// BEGIN Proceso_inactivar
		dto = super.inactivar(dto, token);
		organizar(dto, token);
		return dto;
		// END Proceso_inactivar
	}
	
	@Override
	public ProcesoDTO consultaUnica(ProcesoFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(ProcesoFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<ProcesoDTO> listarConsulta(ProcesoFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	
	public List<ProcesoDTO> consultarArbol(ProcesoFilterDTO dto)throws ServerException{
		// BEGIN region consultarArbol
		boolean onlyOne2ShowClient = false;
		if(dto.getFiltroParametro()!=null && dto.getFiltroParametro().compareTo("*")==0) {
			onlyOne2ShowClient = true;
			dto.setFiltroParametro(null);
		}
		List<ProcesoDTO> result = listarConsulta(dto);
		for (ProcesoDTO procesoDTO : result) {
			if(procesoDTO.getTipo().compareTo(ProcesoDTO.EJECUTOR)==0) {
				procesoDTO = completarProceso(procesoDTO, dto.getSecurityToken());			
			}
		}
		if(dto.getFiltroParametro()==null && !onlyOne2ShowClient) {
			ProcesoDTO resulDTO = ordenar(result);
			result = new ArrayList<ProcesoDTO>();
			result.add(resulDTO);	
		}
		return result;
		// END region consultarArbol
	}
	public String exportar(ProcesoFilterDTO dto)throws ServerException{
		// BEGIN region exportar
		return helper.generarXML(dto);
		// END region exportar
	}
	public String importar(ProcesoFilterDTO dto)throws ServerException{
		// BEGIN region importar
		//return helper.convertXML(dto.getObjetivo(), dto.getSecurityToken());
		return null;
		// END region importar
	}
	public ProcesoDTO obtenerProcesoParaGraficar(ProcesoFilterDTO dto)throws ServerException{
		// BEGIN region obtenerProcesoParaGraficar
		ProcesoDTO bd = null;
		if(dto.getLlaveTabla()!=null) {
			bd = consultaXId(dto.getLlaveTabla());
		}else {
			if(dto.getEstado()!=null) {
				ProcesoEstadoDTO estado = estadoService.consultaXId(dto.getEstado());
				if(estado==null) throw new ServerException("El estado enviado no se identifica");
				bd = consultaXId(estado.getProceso());
			}
		}
		bd = completarProceso(bd, dto.getSecurityToken());
		return bd;
		// END region obtenerProcesoParaGraficar
	}

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ProcesoDTO guardar(ProcesoDTO dto, String token) throws ServerException {
		// BEGIN Proceso_guardar
		preConfigurar(dto);
		dto = super.guardar(dto, token);
		if(dto.getTipo().compareTo(ProcesoDTO.EJECUTOR)==0)crearBasico(dto, null, token);
		return dto;
		// END Proceso_guardar
	}

// BEGIN region aditionalMethods
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	private void organizar(ProcesoDTO dto, String token) throws ServerException{
		//Consulto todas las caracteristicas del documento
		ProcesoFilterDTO filtro = new ProcesoFilterDTO();
		filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		List<ProcesoDTO> campos = listarConsulta(filtro);
		if(campos!=null && !campos.isEmpty()){
			int cont = 1;
			for(ProcesoDTO campo : campos){
				if(campo.getLlaveTabla().compareTo(dto.getLlaveTabla())!=0){
					//asumo que hay dos iguales entonces debo saltar un espacio y el que modifique lo dejo quieto
					if(campo.getPrioridad().compareTo(dto.getPrioridad())==0) cont++;
					if(campo.getPrioridad()!= cont){
						campo.setPrioridad(cont);
						super.actualizar(campo, token);
					}
					cont++;
				}else{
					if(cont == dto.getPrioridad()) cont++;
				}
			}
		}
		//Debo validar que las dependencias si se puedan
	}
	
	private void crearBasico(ProcesoDTO dto, String plantillainicial, String token) throws ServerException {
		ProcesoEstadoDTO estadoActivo = new ProcesoEstadoDTO();
		estadoActivo.setEstadoDocumento(ConstantesGenerales.ESTADO_ACTIVO);
		estadoActivo.setTipo(ProcesoEstadoDTO.TIPO_ESTADO);
		estadoActivo.setProceso(dto.getLlaveTabla());
		estadoActivo.setNombre("ACTIVO");
		estadoActivo.setAvance(10);
		estadoActivo= estadoService.guardar(estadoActivo, token);
		
		PropiedadDTO propiedadModifcable = new PropiedadDTO();
		propiedadModifcable.setCampo(estadoActivo.getLlaveTabla());
		propiedadModifcable.setKey(Propiedades.MODIFICABLE);
		propiedadModifcable.setTipo(PropiedadValorDefinidoDTO.ESTADO);
		propiedadModifcable.setValor("T");
		propiedadModifcable.setMotivo("Permitir modificar los activos");
		propiedadService.guardar(propiedadModifcable, token);
		
		ProcesoEstadoDTO estadoInactivo = new ProcesoEstadoDTO();
		estadoInactivo.setEstadoDocumento(ConstantesGenerales.ESTADO_INACTIVO);
		estadoInactivo.setTipo(ProcesoEstadoDTO.TIPO_ESTADO);
		estadoInactivo.setAvance(20);
		estadoInactivo.setProceso(dto.getLlaveTabla());
		estadoInactivo.setNombre("INACTIVO");
		estadoInactivo= estadoService.guardar(estadoInactivo, token);
		
		ProcesoTransicionDTO inicial = new ProcesoTransicionDTO();
		inicial.setEstadoLLegada(estadoActivo.getLlaveTabla());
		inicial.setNombre(dto.getNombre());
		inicial.setProceso(dto.getLlaveTabla());
		transicionService.guardarConCodigo(inicial, dto.getCodigo(), plantillainicial, token);
		
		ProcesoTransicionDTO anular = new ProcesoTransicionDTO();
		anular.setEstadoPartida(estadoActivo.getLlaveTabla());
		anular.setEstadoLLegada(estadoInactivo.getLlaveTabla());
		anular.setNombre(dto.getNombre() + " - ANULAR");
		anular.setDocumentador(true);
		anular.setProceso(dto.getLlaveTabla());
		transicionService.guardarConCodigo(anular,"X"+ dto.getCodigo(), null, token);

	}
	
	private ProcesoDTO ordenar(List<ProcesoDTO> procesos) throws ServerException{
		if(procesos==null) procesos = new ArrayList<ProcesoDTO>();
		ProcesoDTO nodoPrincipal = new ProcesoDTO();
		nodoPrincipal.setLlaveTabla("NODO1476");
		nodoPrincipal.setTipo(ProcesoDTO.AGRUPADOR);
		nodoPrincipal.setNombre("MAPA DE PROCESOS");
		nodoPrincipal.setCodigo("MAPA");
		procesos.add(0, nodoPrincipal);
		while(procesos.size()>1) {
			ProcesoDTO ultimo = procesos.get(procesos.size()-1);
			if(ultimo.getMacroproceso() == null) ultimo.setMacroproceso("NODO1476");
			ProcesoDTO padre =null;
			for (int i = procesos.size() -2 ; i >= 0; i--) {
				padre = esPadre(procesos.get(i), ultimo.getMacroproceso());
				if(padre != null) break;
			}
			if(padre == null) {
				ProcesoDTO categoria = consultaXId(ultimo.getMacroproceso());
				if(categoria == null) throw new ServerException("No se encuentra la categoria principal. " + ultimo.getMacroproceso());
				procesos.add(categoria);
			}else {
				if(padre.getHijos()==null) padre.setHijos(new ArrayList<ProcesoDTO>());
				padre.getHijos().add(0, ultimo);
				procesos.remove(ultimo);
			}
		}
		return nodoPrincipal;
	}
	
	private ProcesoDTO esPadre(ProcesoDTO categoria, String llavePadre) {
		if(categoria.getLlaveTabla().compareTo(llavePadre)==0) {
			return categoria;
		}else {
			if(categoria.getHijos()==null) return null;
			for (ProcesoDTO iCategoria : categoria.getHijos()) {
				ProcesoDTO busqueda = esPadre(iCategoria, llavePadre);
				if(busqueda!=null)return busqueda; 
			}
		}
		return null;
	}
	
	public ProcesoDTO crearDesdePlantilla(String plantilla, String codigo, String nombre, String objetivo, String token) throws ServerException {
		// BEGIN Proceso_guardar
		ProcesoFilterDTO filtroCantidad = new ProcesoFilterDTO();
		int cantidad = contarResultados(filtroCantidad);
		cantidad = cantidad+1;
		ProcesoDTO dto = new ProcesoDTO();
		dto.setNombre(nombre);
		dto.setCodigo(codigo);
		dto.setTipo(ProcesoDTO.EJECUTOR);
		dto.setObjetivo(objetivo);
		dto.setPrioridad(cantidad);
		dto = super.guardar(dto, token);
		crearBasico(dto, plantilla, token);
		return dto;
		// END Proceso_guardar
	}
	
	private void validarMacroproceso(String macroproceso) throws ServerException{
		if(macroproceso== null) return;
		ProcesoDTO macro = consultaXId(macroproceso);
		if(macro==null) throw new ServerException("El macro proceso no se identifica");
		if(macro.getTipo().compareTo(ProcesoDTO.AGRUPADOR)!=0) throw new ServerException("El macroproceso no es agrupador");
	}
	
	public void preConfigurar(ProcesoDTO dto) throws ServerException {
		ProcesoFilterDTO filtroCantidad = new ProcesoFilterDTO();
		int cantidad = contarResultados(filtroCantidad);
		cantidad = cantidad+1;
		dto.setPrioridad(cantidad);
		validarMacroproceso(dto.getMacroproceso());
	}
	
	private ProcesoDTO completarProceso(ProcesoDTO proceso, String token) throws ServerException{
		ProcesoEstadoFilterDTO filtroEstadoDTO = new ProcesoEstadoFilterDTO();
		filtroEstadoDTO.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		filtroEstadoDTO.setProceso(proceso.getLlaveTabla());
		proceso.setEstados(estadoService.listarConsulta(filtroEstadoDTO));
		for (ProcesoEstadoDTO iEstado : proceso.getEstados()) {
			iEstado.setPropiedades(propiedadService.obtenerPropiedades(PropiedadValorDefinidoDTO.ESTADO, iEstado.getLlaveTabla(), null, null));
		}
		
		ProcesoTransicionFilterDTO filtroTransicionDTO = new ProcesoTransicionFilterDTO();
		filtroTransicionDTO.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		filtroTransicionDTO.setProceso(proceso.getLlaveTabla());
		proceso.setTransiciones(transicionService.listarConsulta(filtroTransicionDTO));
		for (ProcesoTransicionDTO iTransicion : proceso.getTransiciones()) {
			iTransicion.setPropiedades(propiedadService.obtenerPropiedades(PropiedadValorDefinidoDTO.TRANSICION, iTransicion.getLlaveTabla(), null, null));
			if(iTransicion.getPlantilla()!=null) {
				iTransicion.getPropiedades().addAll(propiedadService.obtenerPropiedades(PropiedadValorDefinidoDTO.PLANTILLA, iTransicion.getPlantilla(), null, null));
			}
		}
		
		return proceso;
	}

// END region aditionalMethods

}