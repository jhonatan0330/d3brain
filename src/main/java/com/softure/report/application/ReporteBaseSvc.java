package com.softure.report.application;

import java.util.List;

// BEGIN region interImport
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.report.domain.ReporteBaseDTO;
import com.softure.report.domain.ReporteBaseFilterDTO;
import com.softure.report.domain.ReporteEjecucionDTO;
import com.softure.report.domain.ReporteEjecucionFilterDTO;
import com.softure.report.infrastructure.ReporteBaseMapper;
import com.softure.upload.application.UploadSvc;
import com.softure.authentication.application.UsuarioAutenticacionSvc;
import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.java.cons.ConstantesGenerales;
import javax.sql.DataSource;
import com.softure.java.services.GeneradorReportes;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.application.BasicSvc;
import com.softure.logisticpymes.application.UsuarioSvc;
import com.softure.logisticpymes.domain.UsuarioDTO;

@Service("reporteBaseService")
public class ReporteBaseSvc extends BasicSvc<ReporteBaseDTO, ReporteBaseFilterDTO> {
	
	@Autowired
	private ReporteBaseMapper reporteBaseMapper;
	
	// BEGIN region servicesReporteBase
	@Autowired DataSource dataSource;
	@Autowired private PedidoVentaCaracteristicaSvc pedidoVentaCaracteristicaService;
	@Autowired private PropiedadSvc propiedadService;
	@Autowired private UsuarioAutenticacionSvc autenticacionService;
	@Autowired private UsuarioSvc usuarioService;
	public static final String P_KEY = "P_KEY";
	@Autowired private ReporteEjecucionSvc ejecucionService;
	@Autowired private UploadSvc uploadService;
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
	
	public Map<String, Object> parametrosPropiedades(ReporteBaseDTO reporte, String usuario)throws ServerException {
		Map<String, Object> parametrosJasper = new HashMap<String, Object>();
		List<PropiedadDTO> propiedades = reporte.getPropiedades();
		if(propiedades != null  && !propiedades.isEmpty()) {
			for (PropiedadDTO propiedadDTO : propiedades) {
				switch (propiedadDTO.getKey()) {
				case Propiedades.P_SUBREPORT_: 
				case Propiedades.REPORTE_EXCEL:
				case Propiedades.REPORTE_ENCABEZADO:
				case Propiedades.REPORTE_PIE_PAGINA:
				case Propiedades.REPORTE_ENCABEZADO_EXCEL:
				{
					PropiedadDTO subreporteJRXML = propiedadService.obtenerPropiedad(PropiedadValorDefinidoDTO.REPORTE, propiedadDTO.getValor(), Propiedades.REPORTE_JRXML, usuario);//getUserFlex(token)
					if(subreporteJRXML==null) throw new ServerException("El reporte "+ reporte + "no encuentra el subreporte " + propiedadDTO.getValor());
					if(propiedadDTO.getKey().compareTo(Propiedades.P_SUBREPORT_)==0) {//Esto es porque son multiples parametros
						ReporteBaseDTO subreporte = consultaXId(propiedadDTO.getValor());
						parametrosJasper.put(propiedadDTO.getKey() + subreporte.getCodigo(), subreporteJRXML.getValor());
					}else {
						parametrosJasper.put(propiedadDTO.getKey(), subreporteJRXML.getValor());	
					}
					break;
				}
				case Propiedades.REPORTE_IMAGEN:
				{
					parametrosJasper.put(propiedadDTO.getTexto(), propiedadDTO.getValor().substring(propiedadDTO.getValor().indexOf(",")+1));
					break;
				}
				default:
					parametrosJasper.put(propiedadDTO.getKey(), propiedadDTO.getValor());
				}
			}
		}
		return parametrosJasper;
	}
	
	public ReporteBaseDTO validateReport(String reportId, String token) throws ServerException {
		ReporteBaseDTO base = consultaXId(reportId);
		if(base == null) throw new ServerException("Reporte base no encontrado");
		base.setPropiedades( propiedadService.obtenerPropiedades(PropiedadValorDefinidoDTO.REPORTE, reportId, null, getUserFromParameters(token)) );//getUserFlex(token)
		return base;
	}
	
	public String getUserFromParameters (String token) throws ServerException {
		if(token == null) return null;
		return getUserFlex(token);
	}
	
	public byte[] generarReporte(ReporteBaseDTO reporte, String key, Map<String, Object> parametrosJasper, String token) throws Exception {
		ReporteEjecucionDTO ejecucion = new ReporteEjecucionDTO();
		ejecucion.setFechaInicio(new Date());
		ejecucion.setReporte(reporte.getLlaveTabla());
		if(Propiedades.obtenerParametro(reporte, Propiedades.REP_PRINT_ONE)!=null) {
			//Valido que solo tenga una ejecucion
			ReporteEjecucionFilterDTO uniqueFilter = new ReporteEjecucionFilterDTO();
			uniqueFilter.setDocumento(key);
			uniqueFilter.setReporte(reporte.getLlaveTabla());
			List<ReporteEjecucionDTO> ejecuciones = ejecucionService.listarConsulta(uniqueFilter);
			if(ejecuciones!=null & ejecuciones.size()!=0) {
				UsuarioDTO usuarioImpresion = usuarioService.consultaXId(ejecuciones.get(0).getUsuario());
				throw new ServerException("Este reportes esta configurado para ejecutarse una unica vez, fue impreso el " + ejecuciones.get(0).getFechaInicio().toString() + "por el usuario "+ usuarioImpresion.getNombre() );
			}
		}
		String usuario = getUserFromParameters(token);
		ejecucion.setDocumento(key);
		ejecucion.setUsuario(usuario);
		try {
			if(usuario == null ) {
				if(reporte.getPublico()) {
					usuario = autenticacionService.getUserSystem().getLlaveTabla();
				}else {
					throw new ServerException("Este reporte no es publico y no puede generar el token con el usuario");
				}
			}
			propiedadService.validarFuncionConsultandoPropiedad(reporte, key, null, usuario, token);
			if(parametrosJasper == null)parametrosJasper = new HashMap<String, Object>();
			ejecucion.setUsuario(usuario);
			if(key!=null)parametrosJasper.putAll(llenarParametros(key));
			parametrosJasper.putAll(parametrosPropiedades(reporte, usuario));
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
				if(tipoReporte==null) tipoReporte = "pdf";//Corrige que los reportes se guarden como .null
				if(Propiedades.obtenerParametro(reporte, Propiedades.REP_EXCLUDE_STORAGE_FILE)==null) ejecucion.setUrl( uploadService.uploadFile(resultado, reporte.getNombre() +"_(" + DateFormat.getInstance().format(new Date()) + ")." + tipoReporte, token, "reports"));
				ejecucionService.save(ejecucion);
			}catch (Exception e) {	}
			return resultado;	
		}catch (Exception e) {
			ejecucion.setError(e.getMessage());
			ejecucion.setFechaFin(new Date());
			try {
				ejecucionService.save(ejecucion);
			}catch (Exception ex) {	}
			throw new Exception(e.getMessage());
		}
	}
	
// END region aditionalMethods

}