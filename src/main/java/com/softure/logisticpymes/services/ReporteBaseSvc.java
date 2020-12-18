package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.softure.logisticpymes.dto.DocumentoPlantillaCaracteristicaDTO;
import com.softure.logisticpymes.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.dto.PropiedadDTO;
import com.softure.logisticpymes.dto.PropiedadValorDefinidoDTO;
import com.softure.logisticpymes.services.adapter.Propiedades;
import com.softure.java.cons.ConstantesGenerales;
import javax.sql.DataSource;
import com.softure.java.services.GeneradorReportes;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.ReporteBaseDTO;
import com.softure.logisticpymes.dto.ReporteEjecucionDTO;
import com.softure.logisticpymes.dto.filter.ReporteBaseFilterDTO;
import com.softure.logisticpymes.persistence.ReporteBaseMapper;

@Service("reporteBaseService")
public class ReporteBaseSvc extends BasicSvc<ReporteBaseDTO, ReporteBaseFilterDTO> {
	
	@Autowired
	private ReporteBaseMapper reporteBaseMapper;
	
	// BEGIN region servicesReporteBase
	@Autowired DataSource dataSource;
	@Autowired private PedidoVentaCaracteristicaSvc pedidoVentaCaracteristicaService;
	@Autowired private PropiedadSvc propiedadService;
	@Autowired private UsuarioAutenticacionSvc autenticacionService;
	public static final String P_KEY = "P_KEY";
	@Autowired private ReporteEjecucionSvc ejecucionService;
	// END region servicesReporteBase

	@Override
	public ReporteBaseDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. ReporteBase");
		ReporteBaseFilterDTO dto = new ReporteBaseFilterDTO();
		dto.setLlaveTabla(llave);
		return reporteBaseMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = reporteBaseMapper;
	}
	
	@Override
	public ReporteBaseDTO activar(ReporteBaseDTO dto, String token) throws ServerException {
		// BEGIN ReporteBase_activar
		return super.activar(dto, token);
		// END ReporteBase_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ReporteBaseDTO actualizar( ReporteBaseDTO dto, String token) throws ServerException {
		// BEGIN ReporteBase_actualizar
		return super.update(dto);
		// END ReporteBase_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ReporteBaseDTO inactivar(ReporteBaseDTO dto, String token) throws ServerException {
		// BEGIN ReporteBase_inactivar
		return super.inactivar(dto, token);
		// END ReporteBase_inactivar
	}
	
	@Override
	public ReporteBaseDTO consultaUnica(ReporteBaseFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(ReporteBaseFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<ReporteBaseDTO> listarConsulta(ReporteBaseFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public ReporteBaseDTO guardar(ReporteBaseDTO dto, String token) throws ServerException {
		// BEGIN ReporteBase_guardar
		return super.save(dto);
		// END ReporteBase_guardar
	}

// BEGIN region aditionalMethods
	public List<ReporteBaseDTO> listarDisponiblesDocumento(String documento) throws ServerException {
		ReporteBaseFilterDTO filtro = new ReporteBaseFilterDTO();
		filtro.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		filtro.setPlantilla(documento);
		List<ReporteBaseDTO> result = listarConsulta(filtro);
		for (ReporteBaseDTO reporteBaseDTO : result) {
			reporteBaseDTO.setPropiedades(propiedadService.obtenerPropiedades(PropiedadValorDefinidoDTO.REPORTE, reporteBaseDTO.getLlaveTabla(), null, null));
		}
		return result;
	}
	
	public List<ReporteBaseDTO> listarMenu() throws ServerException {
		return reporteBaseMapper.listarMenu();
	}
	
	public Map<String, Object> llenarParametros(String keyDocumento)throws ServerException {
		Map<String, Object> parametrosJasper = new HashMap<String, Object>();
		parametrosJasper.put(P_KEY, keyDocumento);
		List<PedidoVentaCaracteristicaDTO> caracteristicasActuales = pedidoVentaCaracteristicaService.listarParaReporte(keyDocumento);
		if(caracteristicasActuales!=null && !caracteristicasActuales.isEmpty()){
			for(PedidoVentaCaracteristicaDTO dato : caracteristicasActuales){
				switch (dato.getEstado()){
					case DocumentoPlantillaCaracteristicaDTO.PROCESO:{ parametrosJasper.put("P_" + dato.getCampo(), dato.getValorOpcion());break;}
					case DocumentoPlantillaCaracteristicaDTO.TEXTO:{ parametrosJasper.put("P_" + dato.getCampo(), dato.getValorText());break;}
					case DocumentoPlantillaCaracteristicaDTO.NUMERO:{ parametrosJasper.put("P_" + dato.getCampo(), dato.getValorText());break;}
					case DocumentoPlantillaCaracteristicaDTO.BINARIO:{ parametrosJasper.put("P_" + dato.getCampo(), dato.getValorText());break;}
					case DocumentoPlantillaCaracteristicaDTO.FECHA:{
						if(dato.getValorNumero()==null || dato.getValorNumero().compareTo(BigDecimal.ZERO)==0){
							parametrosJasper.put("P_" + dato.getCampo(), new Timestamp( dato.getValorFecha().getTime()) );
						}else{
							parametrosJasper.put("P_" + dato.getCampo() + "_INICIO", new Timestamp(dato.getValorFecha().getTime()));
							parametrosJasper.put("P_" + dato.getCampo() + "_FIN", new Timestamp(dato.getValorFecha().getTime() + dato.getValorNumero().longValue()) );
						}
						break;
					}
					case DocumentoPlantillaCaracteristicaDTO.CONFIGURACION:{ parametrosJasper.put("P_" + dato.getCampo(), dato.getValorOpcion());break;}
				}
			}
		}
		return parametrosJasper;
	}
	
	public Map<String, Object> parametrosPropiedades(String reporte, String usuario)throws ServerException {
		Map<String, Object> parametrosJasper = new HashMap<String, Object>();
		List<PropiedadDTO> propiedades = propiedadService.obtenerPropiedades(PropiedadValorDefinidoDTO.REPORTE, reporte, null, usuario);//getUserFlex(token)
		if(propiedades != null  && !propiedades.isEmpty()) {
			for (PropiedadDTO propiedadDTO : propiedades) {
				if(propiedadDTO.getKey().compareTo(Propiedades.P_SUBREPORT_)==0
						|| propiedadDTO.getKey().compareTo(Propiedades.REPORTE_EXCEL)==0
						|| propiedadDTO.getKey().compareTo(Propiedades.REPORTE_ENCABEZADO)==0
						|| propiedadDTO.getKey().compareTo(Propiedades.REPORTE_PIE_PAGINA)==0
						|| propiedadDTO.getKey().compareTo(Propiedades.REPORTE_ENCABEZADO_EXCEL)==0) {
					
					PropiedadDTO subreporteJRXML = propiedadService.obtenerPropiedad(PropiedadValorDefinidoDTO.REPORTE, propiedadDTO.getValor(), Propiedades.REPORTE_JRXML, usuario);//getUserFlex(token)
					if(subreporteJRXML==null) throw new ServerException("El reporte "+ reporte + "no encuentra el subreporte " + propiedadDTO.getValor());
					if(propiedadDTO.getKey().compareTo(Propiedades.P_SUBREPORT_)==0) {//Esto es porque son multiples parametros
						ReporteBaseDTO subreporte = consultaXId(propiedadDTO.getValor());
						parametrosJasper.put(propiedadDTO.getKey() + subreporte.getCodigo(), subreporteJRXML.getValor());
					}else {
						parametrosJasper.put(propiedadDTO.getKey(), subreporteJRXML.getValor());	
					}
				}else {
					parametrosJasper.put(propiedadDTO.getKey(), propiedadDTO.getValor());
				}
			}
		}
		return parametrosJasper;
	}
	
	
	public byte[] generarReporte(String nombreReporte, String key,Map<String, Object> parametrosJasper, String usuario) throws Exception {
		ReporteEjecucionDTO ejecucion = new ReporteEjecucionDTO();
		ejecucion.setFechaInicio(new Date());
		try {
			ReporteBaseDTO base = consultaXId(nombreReporte);
			if(base == null) throw new ServerException("Reporte base no encontrado");
			ejecucion.setReporte(nombreReporte);
			ejecucion.setDocumento(key);
			if(parametrosJasper == null)parametrosJasper = new HashMap<String, Object>();
			String token = (String) parametrosJasper.get("P_TOKEN");
			if(usuario ==null) {
				if(token==null) {
					if(base.getPublico()) {
						token = autenticacionService.generateAdministratorToken().getLlaveTabla();
					}else {
						throw new ServerException("Este reporte no es publico y no puede generar el token con el usuario");
					}
				}else {
					usuario = getUserFlex(token);
				}
			}
			ejecucion.setUsuario(usuario);
			if(key!=null)parametrosJasper.putAll(llenarParametros(key));
			parametrosJasper.putAll(parametrosPropiedades(base.getLlaveTabla(), usuario));
			String tipoReporte = (String) parametrosJasper.get("P_JASPERTIPO");
			String jrxmlReporte = (String) parametrosJasper.get(Propiedades.REPORTE_JRXML);
			if(jrxmlReporte==null) throw new ServerException("No se a definido el cuerpo del reporte JRXML");
			Object propiedadExcel = null;
			//Seccion del reporte
			GeneradorReportes generadorReporte = new GeneradorReportes(dataSource.getConnection());
			byte[] resultado=null;
			if (tipoReporte!=null && tipoReporte.toUpperCase().equals("XLS")) {
				propiedadExcel = parametrosJasper.get(Propiedades.REPORTE_EXCEL);
				if(propiedadExcel!=null && !propiedadExcel.toString().isEmpty()) {
					resultado = generadorReporte.generarReporteExcel(propiedadExcel.toString(), parametrosJasper);					
				}else {
					resultado = generadorReporte.generarReporteExcel(jrxmlReporte, parametrosJasper);
				}
			}else{
				resultado = generadorReporte.generarReportePDF(jrxmlReporte, parametrosJasper);
			}
			ejecucion.setFechaFin(new Date());
			try {
				if(ejecucion.getReporte()!=null)ejecucionService.save(ejecucion);
			}catch (Exception e) {	}
			return resultado;	
		}catch (Exception e) {
			ejecucion.setError(e.getMessage());
			ejecucion.setFechaFin(new Date());
			try {
				if(ejecucion.getReporte()!=null)ejecucionService.save(ejecucion);
			}catch (Exception ex) {	}
			throw new Exception(e.getMessage());
		}
	}
	
// END region aditionalMethods

}