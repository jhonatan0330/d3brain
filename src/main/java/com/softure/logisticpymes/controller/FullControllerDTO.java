package com.softure.logisticpymes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.softure.java.dto.exception.FlexException;
import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.PlantillaConsecutivoDTO;
import com.softure.logisticpymes.dto.filter.PlantillaConsecutivoFilterDTO;
import com.softure.logisticpymes.services.PlantillaConsecutivoSvc;
import com.softure.logisticpymes.dto.ProcesoDTO;
import com.softure.logisticpymes.dto.filter.ProcesoFilterDTO;
import com.softure.logisticpymes.services.ProcesoSvc;
import com.softure.logisticpymes.dto.DocumentoTransaccionDTO;
import com.softure.logisticpymes.dto.filter.DocumentoTransaccionFilterDTO;
import com.softure.logisticpymes.services.DocumentoTransaccionSvc;
import com.softure.logisticpymes.dto.ProcesoEstadoDTO;
import com.softure.logisticpymes.dto.filter.ProcesoEstadoFilterDTO;
import com.softure.logisticpymes.services.ProcesoEstadoSvc;
import com.softure.logisticpymes.dto.ProcesoTransicionAutomaticaDTO;
import com.softure.logisticpymes.dto.filter.ProcesoTransicionAutomaticaFilterDTO;
import com.softure.logisticpymes.services.ProcesoTransicionAutomaticaSvc;
import com.softure.logisticpymes.dto.DocumentoRelacionExpedienteDTO;
import com.softure.logisticpymes.dto.filter.DocumentoRelacionExpedienteFilterDTO;
import com.softure.logisticpymes.services.DocumentoRelacionExpedienteSvc;
import com.softure.logisticpymes.dto.DocumentoRelacionGestorDTO;
import com.softure.logisticpymes.dto.filter.DocumentoRelacionGestorFilterDTO;
import com.softure.logisticpymes.services.DocumentoRelacionGestorSvc;
import com.softure.logisticpymes.dto.DocumentoPlantillaCaracteristicaDTO;
import com.softure.logisticpymes.dto.filter.DocumentoPlantillaCaracteristicaFilterDTO;
import com.softure.logisticpymes.services.DocumentoPlantillaCaracteristicaSvc;
import com.softure.logisticpymes.dto.PedidoVentaDTO;
import com.softure.logisticpymes.dto.filter.PedidoVentaFilterDTO;
import com.softure.logisticpymes.services.PedidoVentaSvc;
import com.softure.logisticpymes.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.dto.filter.PedidoVentaCaracteristicaFilterDTO;
import com.softure.logisticpymes.services.PedidoVentaCaracteristicaSvc;
import com.softure.logisticpymes.dto.ProcesoTransicionDTO;
import com.softure.logisticpymes.dto.filter.ProcesoTransicionFilterDTO;
import com.softure.logisticpymes.services.ProcesoTransicionSvc;
import com.softure.logisticpymes.dto.PedidoVentaAjusteDTO;
import com.softure.logisticpymes.dto.filter.PedidoVentaAjusteFilterDTO;
import com.softure.logisticpymes.services.PedidoVentaAjusteSvc;
import com.softure.logisticpymes.dto.DocumentoPlantillaDTO;
import com.softure.logisticpymes.dto.filter.DocumentoPlantillaFilterDTO;
import com.softure.logisticpymes.services.DocumentoPlantillaSvc;
import com.softure.logisticpymes.dto.MovimientoDTO;
import com.softure.logisticpymes.dto.filter.MovimientoFilterDTO;
import com.softure.logisticpymes.services.MovimientoSvc;
import com.softure.logisticpymes.dto.TurnoDTO;
import com.softure.logisticpymes.dto.filter.TurnoFilterDTO;
import com.softure.logisticpymes.services.TurnoSvc;
import com.softure.logisticpymes.dto.TarifaDTO;
import com.softure.logisticpymes.dto.filter.TarifaFilterDTO;
import com.softure.logisticpymes.services.TarifaSvc;
import com.softure.logisticpymes.dto.PedidoVentaDineroDTO;
import com.softure.logisticpymes.dto.filter.PedidoVentaDineroFilterDTO;
import com.softure.logisticpymes.services.PedidoVentaDineroSvc;
import com.softure.logisticpymes.dto.CuentaDTO;
import com.softure.logisticpymes.dto.filter.CuentaFilterDTO;
import com.softure.logisticpymes.services.CuentaSvc;
import com.softure.logisticpymes.dto.TarifarioDTO;
import com.softure.logisticpymes.dto.filter.TarifarioFilterDTO;
import com.softure.logisticpymes.services.TarifarioSvc;
import com.softure.logisticpymes.dto.CuentaAuxiliarDocumentoDTO;
import com.softure.logisticpymes.dto.filter.CuentaAuxiliarDocumentoFilterDTO;
import com.softure.logisticpymes.services.CuentaAuxiliarDocumentoSvc;
import com.softure.logisticpymes.dto.ComprobanteConfiguracionDetalleDTO;
import com.softure.logisticpymes.dto.filter.ComprobanteConfiguracionDetalleFilterDTO;
import com.softure.logisticpymes.services.ComprobanteConfiguracionDetalleSvc;
import com.softure.logisticpymes.dto.CuentaContableMovimientoDTO;
import com.softure.logisticpymes.dto.filter.CuentaContableMovimientoFilterDTO;
import com.softure.logisticpymes.services.CuentaContableMovimientoSvc;
import com.softure.logisticpymes.dto.CuentaAuxiliarPlantillaDTO;
import com.softure.logisticpymes.dto.filter.CuentaAuxiliarPlantillaFilterDTO;
import com.softure.logisticpymes.services.CuentaAuxiliarPlantillaSvc;
import com.softure.logisticpymes.dto.ComprobanteConfiguracionDTO;
import com.softure.logisticpymes.dto.filter.ComprobanteConfiguracionFilterDTO;
import com.softure.logisticpymes.services.ComprobanteConfiguracionSvc;
import com.softure.logisticpymes.dto.ComprobanteCuentaDetalleDTO;
import com.softure.logisticpymes.dto.filter.ComprobanteCuentaDetalleFilterDTO;
import com.softure.logisticpymes.services.ComprobanteCuentaDetalleSvc;
import com.softure.logisticpymes.dto.ComprobanteContableDTO;
import com.softure.logisticpymes.dto.filter.ComprobanteContableFilterDTO;
import com.softure.logisticpymes.services.ComprobanteContableSvc;
import com.softure.logisticpymes.dto.CuentaContableDTO;
import com.softure.logisticpymes.dto.filter.CuentaContableFilterDTO;
import com.softure.logisticpymes.services.CuentaContableSvc;
import com.softure.logisticpymes.dto.CatalogoContableDTO;
import com.softure.logisticpymes.dto.filter.CatalogoContableFilterDTO;
import com.softure.logisticpymes.services.CatalogoContableSvc;
import com.softure.logisticpymes.dto.ActividadDTO;
import com.softure.logisticpymes.dto.filter.ActividadFilterDTO;
import com.softure.logisticpymes.services.ActividadSvc;
import com.softure.logisticpymes.dto.PedidoVentaTiempoDTO;
import com.softure.logisticpymes.dto.filter.PedidoVentaTiempoFilterDTO;
import com.softure.logisticpymes.services.PedidoVentaTiempoSvc;
import com.softure.logisticpymes.dto.PropiedadDTO;
import com.softure.logisticpymes.dto.filter.PropiedadFilterDTO;
import com.softure.logisticpymes.services.PropiedadSvc;
import com.softure.logisticpymes.dto.RelacionInternaDTO;
import com.softure.logisticpymes.dto.filter.RelacionInternaFilterDTO;
import com.softure.logisticpymes.services.RelacionInternaSvc;
import com.softure.logisticpymes.dto.EncuestaRespuestaDTO;
import com.softure.logisticpymes.dto.filter.EncuestaRespuestaFilterDTO;
import com.softure.logisticpymes.services.EncuestaRespuestaSvc;
import com.softure.logisticpymes.dto.CambioDTO;
import com.softure.logisticpymes.dto.filter.CambioFilterDTO;
import com.softure.logisticpymes.services.CambioSvc;
import com.softure.logisticpymes.dto.EncuestaOpcionRespuestaDTO;
import com.softure.logisticpymes.dto.filter.EncuestaOpcionRespuestaFilterDTO;
import com.softure.logisticpymes.services.EncuestaOpcionRespuestaSvc;
import com.softure.logisticpymes.dto.PropiedadValorDefinidoDTO;
import com.softure.logisticpymes.dto.filter.PropiedadValorDefinidoFilterDTO;
import com.softure.logisticpymes.services.PropiedadValorDefinidoSvc;
import com.softure.logisticpymes.dto.EncuestaGrupoDTO;
import com.softure.logisticpymes.dto.filter.EncuestaGrupoFilterDTO;
import com.softure.logisticpymes.services.EncuestaGrupoSvc;
import com.softure.logisticpymes.dto.EncuestaPreguntaDTO;
import com.softure.logisticpymes.dto.filter.EncuestaPreguntaFilterDTO;
import com.softure.logisticpymes.services.EncuestaPreguntaSvc;
import com.softure.logisticpymes.dto.EncuestaDTO;
import com.softure.logisticpymes.dto.filter.EncuestaFilterDTO;
import com.softure.logisticpymes.services.EncuestaSvc;
import com.softure.logisticpymes.dto.UsuarioRolDTO;
import com.softure.logisticpymes.dto.filter.UsuarioRolFilterDTO;
import com.softure.logisticpymes.services.UsuarioRolSvc;
import com.softure.logisticpymes.dto.PuestoDTO;
import com.softure.logisticpymes.dto.filter.PuestoFilterDTO;
import com.softure.logisticpymes.services.PuestoSvc;
import com.softure.logisticpymes.dto.RolAccesoDTO;
import com.softure.logisticpymes.dto.filter.RolAccesoFilterDTO;
import com.softure.logisticpymes.services.RolAccesoSvc;
import com.softure.logisticpymes.dto.UsuarioDTO;
import com.softure.logisticpymes.dto.filter.UsuarioFilterDTO;
import com.softure.logisticpymes.services.UsuarioSvc;
import com.softure.logisticpymes.dto.WebServiceDTO;
import com.softure.logisticpymes.dto.filter.WebServiceFilterDTO;
import com.softure.logisticpymes.services.WebServiceSvc;
import com.softure.logisticpymes.services.refactor.CallCRUDDocument;
import com.softure.logisticpymes.services.refactor.CallListDocumentWithFilters;
import com.softure.logisticpymes.dto.PostRespuestaDTO;
import com.softure.logisticpymes.dto.filter.PostRespuestaFilterDTO;
import com.softure.logisticpymes.services.PostRespuestaSvc;
import com.softure.logisticpymes.dto.GPSLocalizacionDTO;
import com.softure.logisticpymes.dto.filter.GPSLocalizacionFilterDTO;
import com.softure.logisticpymes.services.GPSLocalizacionSvc;
import com.softure.logisticpymes.dto.MensajeDTO;
import com.softure.logisticpymes.dto.filter.MensajeFilterDTO;
import com.softure.logisticpymes.services.MensajeSvc;
import com.softure.logisticpymes.dto.PostCalificacionDTO;
import com.softure.logisticpymes.dto.filter.PostCalificacionFilterDTO;
import com.softure.logisticpymes.services.PostCalificacionSvc;
import com.softure.logisticpymes.dto.WebServiceEjecucionDTO;
import com.softure.logisticpymes.dto.filter.WebServiceEjecucionFilterDTO;
import com.softure.logisticpymes.services.WebServiceEjecucionSvc;
import com.softure.logisticpymes.dto.PostPreguntaDTO;
import com.softure.logisticpymes.dto.filter.PostPreguntaFilterDTO;
import com.softure.logisticpymes.services.PostPreguntaSvc;
import com.softure.logisticpymes.dto.ServidorDTO;
import com.softure.logisticpymes.dto.filter.ServidorFilterDTO;
import com.softure.logisticpymes.services.ServidorSvc;
import com.softure.logisticpymes.dto.MensajePlantillaCorreoDTO;
import com.softure.logisticpymes.dto.filter.MensajePlantillaCorreoFilterDTO;
import com.softure.logisticpymes.services.MensajePlantillaCorreoSvc;
import com.softure.logisticpymes.dto.GPSDispositivoDTO;
import com.softure.logisticpymes.dto.filter.GPSDispositivoFilterDTO;
import com.softure.logisticpymes.services.GPSDispositivoSvc;
import com.softure.logisticpymes.dto.TrazabilidadProductoInventarioDTO;
import com.softure.logisticpymes.dto.filter.TrazabilidadProductoInventarioFilterDTO;
import com.softure.logisticpymes.services.TrazabilidadProductoInventarioSvc;
import com.softure.logisticpymes.dto.DetallePedidoVentaDTO;
import com.softure.logisticpymes.dto.filter.DetallePedidoVentaFilterDTO;
import com.softure.logisticpymes.services.DetallePedidoVentaSvc;
import com.softure.logisticpymes.dto.CategoriaProductoDTO;
import com.softure.logisticpymes.dto.filter.CategoriaProductoFilterDTO;
import com.softure.logisticpymes.services.CategoriaProductoSvc;
import com.softure.logisticpymes.dto.ProductoInventarioDTO;
import com.softure.logisticpymes.dto.filter.ProductoInventarioFilterDTO;
import com.softure.logisticpymes.services.ProductoInventarioSvc;
import com.softure.logisticpymes.dto.ProductoCaracteristicaDTO;
import com.softure.logisticpymes.dto.filter.ProductoCaracteristicaFilterDTO;
import com.softure.logisticpymes.services.ProductoCaracteristicaSvc;
import com.softure.logisticpymes.dto.UsuarioRolProductoDTO;
import com.softure.logisticpymes.dto.filter.UsuarioRolProductoFilterDTO;
import com.softure.logisticpymes.services.UsuarioRolProductoSvc;
import com.softure.logisticpymes.dto.DetalleCaracteristicaProductoDTO;
import com.softure.logisticpymes.dto.filter.DetalleCaracteristicaProductoFilterDTO;
import com.softure.logisticpymes.services.DetalleCaracteristicaProductoSvc;
import com.softure.logisticpymes.dto.BodegaDTO;
import com.softure.logisticpymes.dto.filter.BodegaFilterDTO;
import com.softure.logisticpymes.services.BodegaSvc;
import com.softure.logisticpymes.dto.ProductoDTO;
import com.softure.logisticpymes.dto.filter.ProductoFilterDTO;
import com.softure.logisticpymes.services.ProductoSvc;
import com.softure.logisticpymes.dto.ProductoInventarioDescuentoDTO;
import com.softure.logisticpymes.dto.filter.ProductoInventarioDescuentoFilterDTO;
import com.softure.logisticpymes.services.ProductoInventarioDescuentoSvc;
import com.softure.logisticpymes.dto.DeduccionProductoDTO;
import com.softure.logisticpymes.dto.filter.DeduccionProductoFilterDTO;
import com.softure.logisticpymes.services.DeduccionProductoSvc;
import com.softure.logisticpymes.dto.ModuloContratadoDTO;
import com.softure.logisticpymes.dto.filter.ModuloContratadoFilterDTO;
import com.softure.logisticpymes.services.ModuloContratadoSvc;
import com.softure.logisticpymes.dto.ModuloDTO;
import com.softure.logisticpymes.dto.filter.ModuloFilterDTO;
import com.softure.logisticpymes.services.ModuloSvc;
import com.softure.logisticpymes.dto.ReporteBaseDTO;
import com.softure.logisticpymes.dto.filter.ReporteBaseFilterDTO;
import com.softure.logisticpymes.services.ReporteBaseSvc;
import com.softure.logisticpymes.dto.UsuarioOrganizacionDTO;
import com.softure.logisticpymes.dto.filter.UsuarioOrganizacionFilterDTO;
import com.softure.logisticpymes.services.UsuarioOrganizacionSvc;
import com.softure.logisticpymes.dto.PermisoDTO;
import com.softure.logisticpymes.dto.filter.PermisoFilterDTO;
import com.softure.logisticpymes.services.PermisoSvc;
import com.softure.logisticpymes.dto.UsuarioAutenticacionAutorizacionDTO;
import com.softure.logisticpymes.dto.filter.UsuarioAutenticacionAutorizacionFilterDTO;
import com.softure.logisticpymes.services.UsuarioAutenticacionAutorizacionSvc;
import com.softure.logisticpymes.dto.UsuarioSesionDTO;
import com.softure.logisticpymes.dto.filter.UsuarioSesionFilterDTO;
import com.softure.logisticpymes.services.UsuarioSesionSvc;
import com.softure.logisticpymes.dto.CargaArchivoDTO;
import com.softure.logisticpymes.dto.filter.CargaArchivoFilterDTO;
import com.softure.logisticpymes.services.CargaArchivoSvc;
import com.softure.logisticpymes.dto.ReporteEjecucionDTO;
import com.softure.logisticpymes.dto.filter.ReporteEjecucionFilterDTO;
import com.softure.logisticpymes.services.ReporteEjecucionSvc;
import com.softure.logisticpymes.dto.ConsecutivoDTO;
import com.softure.logisticpymes.dto.filter.ConsecutivoFilterDTO;
import com.softure.logisticpymes.services.ConsecutivoSvc;
import com.softure.logisticpymes.dto.TransaccionLogDTO;
import com.softure.logisticpymes.dto.filter.TransaccionLogFilterDTO;
import com.softure.logisticpymes.services.TransaccionLogSvc;
import com.softure.logisticpymes.dto.UsuarioSesionErrorDTO;
import com.softure.logisticpymes.dto.filter.UsuarioSesionErrorFilterDTO;
import com.softure.logisticpymes.services.UsuarioSesionErrorSvc;
import com.softure.logisticpymes.dto.UsuarioAutenticacionDTO;
import com.softure.logisticpymes.dto.filter.UsuarioAutenticacionFilterDTO;
import com.softure.logisticpymes.services.UsuarioAutenticacionSvc;
import com.softure.logisticpymes.dto.AuditoriaDTO;
import com.softure.logisticpymes.dto.filter.AuditoriaFilterDTO;
import com.softure.logisticpymes.services.AuditoriaSvc;
import com.softure.logisticpymes.dto.TransaccionErrorDTO;
import com.softure.logisticpymes.dto.filter.TransaccionErrorFilterDTO;
import com.softure.logisticpymes.services.TransaccionErrorSvc;
import com.softure.logisticpymes.dto.OrganizacionDTO;
import com.softure.logisticpymes.dto.filter.OrganizacionFilterDTO;
import com.softure.logisticpymes.services.OrganizacionSvc;

@RestController
@RequestMapping("/flex")
public class FullControllerDTO {
	
	
	@Autowired private PlantillaConsecutivoSvc plantillaConsecutivoService;
	@Autowired private CallCRUDDocument crudService;
	
	@RequestMapping(value="/consultaXIdPlantillaConsecutivo", method=RequestMethod.POST)
	public PlantillaConsecutivoDTO consultaXIdPlantillaConsecutivo(@RequestBody String llave) throws FlexException {
		try {
			return plantillaConsecutivoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosPlantillaConsecutivo", method=RequestMethod.POST)
	public int contarResultadosPlantillaConsecutivo(@RequestBody PlantillaConsecutivoFilterDTO dto) throws FlexException  {
		try {
			return plantillaConsecutivoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaPlantillaConsecutivo", method=RequestMethod.POST)
	public PlantillaConsecutivoDTO consultaUnicaPlantillaConsecutivo(@RequestBody PlantillaConsecutivoFilterDTO dto) throws FlexException  {
		try {
			return plantillaConsecutivoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaPlantillaConsecutivo", method=RequestMethod.POST)
	public List<PlantillaConsecutivoDTO> listarConsultaPlantillaConsecutivo(@RequestBody PlantillaConsecutivoFilterDTO dto) throws FlexException  {
		try {
			return plantillaConsecutivoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarPlantillaConsecutivo", method=RequestMethod.POST)
	public PlantillaConsecutivoDTO activarPlantillaConsecutivo(@RequestBody PlantillaConsecutivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return plantillaConsecutivoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarPlantillaConsecutivo", method=RequestMethod.POST)
	public PlantillaConsecutivoDTO inactivarPlantillaConsecutivo(@RequestBody PlantillaConsecutivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return plantillaConsecutivoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarPlantillaConsecutivo", method=RequestMethod.POST)
	public PlantillaConsecutivoDTO actualizarPlantillaConsecutivo(@RequestBody PlantillaConsecutivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return plantillaConsecutivoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarPlantillaConsecutivo", method=RequestMethod.POST)
	public PlantillaConsecutivoDTO guardarPlantillaConsecutivo(@RequestBody PlantillaConsecutivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return plantillaConsecutivoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private ProcesoSvc procesoService;
	
	@RequestMapping(value="/consultaXIdProceso", method=RequestMethod.POST)
	public ProcesoDTO consultaXIdProceso(@RequestBody String llave) throws FlexException {
		try {
			return procesoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosProceso", method=RequestMethod.POST)
	public int contarResultadosProceso(@RequestBody ProcesoFilterDTO dto) throws FlexException  {
		try {
			return procesoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaProceso", method=RequestMethod.POST)
	public ProcesoDTO consultaUnicaProceso(@RequestBody ProcesoFilterDTO dto) throws FlexException  {
		try {
			return procesoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaProceso", method=RequestMethod.POST)
	public List<ProcesoDTO> listarConsultaProceso(@RequestBody ProcesoFilterDTO dto) throws FlexException  {
		try {
			return procesoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarProceso", method=RequestMethod.POST)
	public ProcesoDTO activarProceso(@RequestBody ProcesoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarProceso", method=RequestMethod.POST)
	public ProcesoDTO inactivarProceso(@RequestBody ProcesoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarProceso", method=RequestMethod.POST)
	public ProcesoDTO actualizarProceso(@RequestBody ProcesoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarProceso", method=RequestMethod.POST)
	public ProcesoDTO guardarProceso(@RequestBody ProcesoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@RequestMapping(value="/consultarArbolProceso", method=RequestMethod.POST)
	public List<ProcesoDTO> consultarArbolProceso(@RequestBody ProcesoFilterDTO dto)throws FlexException {
		try {
			return procesoService.consultarArbol(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}

	@RequestMapping(value="/exportarProceso", method=RequestMethod.POST)
	public String exportarProceso(@RequestBody ProcesoFilterDTO dto)throws FlexException {
		try {
			return procesoService.exportar(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}

	@RequestMapping(value="/importarProceso", method=RequestMethod.POST)
	public String importarProceso(@RequestBody ProcesoFilterDTO dto)throws FlexException {
		try {
			return procesoService.importar(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}

	@RequestMapping(value="/obtenerProcesoParaGraficarProceso", method=RequestMethod.POST)
	public ProcesoDTO obtenerProcesoParaGraficarProceso(@RequestBody ProcesoFilterDTO dto)throws FlexException {
		try {
			return procesoService.obtenerProcesoParaGraficar(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private DocumentoTransaccionSvc documentoTransaccionService;
	
	@RequestMapping(value="/consultaXIdDocumentoTransaccion", method=RequestMethod.POST)
	public DocumentoTransaccionDTO consultaXIdDocumentoTransaccion(@RequestBody String llave) throws FlexException {
		try {
			return documentoTransaccionService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosDocumentoTransaccion", method=RequestMethod.POST)
	public int contarResultadosDocumentoTransaccion(@RequestBody DocumentoTransaccionFilterDTO dto) throws FlexException  {
		try {
			return documentoTransaccionService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaDocumentoTransaccion", method=RequestMethod.POST)
	public DocumentoTransaccionDTO consultaUnicaDocumentoTransaccion(@RequestBody DocumentoTransaccionFilterDTO dto) throws FlexException  {
		try {
			return documentoTransaccionService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaDocumentoTransaccion", method=RequestMethod.POST)
	public List<DocumentoTransaccionDTO> listarConsultaDocumentoTransaccion(@RequestBody DocumentoTransaccionFilterDTO dto) throws FlexException  {
		try {
			return documentoTransaccionService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarDocumentoTransaccion", method=RequestMethod.POST)
	public DocumentoTransaccionDTO activarDocumentoTransaccion(@RequestBody DocumentoTransaccionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoTransaccionService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarDocumentoTransaccion", method=RequestMethod.POST)
	public DocumentoTransaccionDTO inactivarDocumentoTransaccion(@RequestBody DocumentoTransaccionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoTransaccionService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarDocumentoTransaccion", method=RequestMethod.POST)
	public DocumentoTransaccionDTO actualizarDocumentoTransaccion(@RequestBody DocumentoTransaccionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoTransaccionService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarDocumentoTransaccion", method=RequestMethod.POST)
	public DocumentoTransaccionDTO guardarDocumentoTransaccion(@RequestBody DocumentoTransaccionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoTransaccionService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private ProcesoEstadoSvc procesoEstadoService;
	
	@RequestMapping(value="/consultaXIdProcesoEstado", method=RequestMethod.POST)
	public ProcesoEstadoDTO consultaXIdProcesoEstado(@RequestBody String llave) throws FlexException {
		try {
			return procesoEstadoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosProcesoEstado", method=RequestMethod.POST)
	public int contarResultadosProcesoEstado(@RequestBody ProcesoEstadoFilterDTO dto) throws FlexException  {
		try {
			return procesoEstadoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaProcesoEstado", method=RequestMethod.POST)
	public ProcesoEstadoDTO consultaUnicaProcesoEstado(@RequestBody ProcesoEstadoFilterDTO dto) throws FlexException  {
		try {
			return procesoEstadoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaProcesoEstado", method=RequestMethod.POST)
	public List<ProcesoEstadoDTO> listarConsultaProcesoEstado(@RequestBody ProcesoEstadoFilterDTO dto) throws FlexException  {
		try {
			return procesoEstadoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarProcesoEstado", method=RequestMethod.POST)
	public ProcesoEstadoDTO activarProcesoEstado(@RequestBody ProcesoEstadoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoEstadoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarProcesoEstado", method=RequestMethod.POST)
	public ProcesoEstadoDTO inactivarProcesoEstado(@RequestBody ProcesoEstadoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoEstadoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarProcesoEstado", method=RequestMethod.POST)
	public ProcesoEstadoDTO actualizarProcesoEstado(@RequestBody ProcesoEstadoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoEstadoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarProcesoEstado", method=RequestMethod.POST)
	public ProcesoEstadoDTO guardarProcesoEstado(@RequestBody ProcesoEstadoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoEstadoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private ProcesoTransicionAutomaticaSvc procesoTransicionAutomaticaService;
	
	@RequestMapping(value="/consultaXIdProcesoTransicionAutomatica", method=RequestMethod.POST)
	public ProcesoTransicionAutomaticaDTO consultaXIdProcesoTransicionAutomatica(@RequestBody String llave) throws FlexException {
		try {
			return procesoTransicionAutomaticaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosProcesoTransicionAutomatica", method=RequestMethod.POST)
	public int contarResultadosProcesoTransicionAutomatica(@RequestBody ProcesoTransicionAutomaticaFilterDTO dto) throws FlexException  {
		try {
			return procesoTransicionAutomaticaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaProcesoTransicionAutomatica", method=RequestMethod.POST)
	public ProcesoTransicionAutomaticaDTO consultaUnicaProcesoTransicionAutomatica(@RequestBody ProcesoTransicionAutomaticaFilterDTO dto) throws FlexException  {
		try {
			return procesoTransicionAutomaticaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaProcesoTransicionAutomatica", method=RequestMethod.POST)
	public List<ProcesoTransicionAutomaticaDTO> listarConsultaProcesoTransicionAutomatica(@RequestBody ProcesoTransicionAutomaticaFilterDTO dto) throws FlexException  {
		try {
			return procesoTransicionAutomaticaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarProcesoTransicionAutomatica", method=RequestMethod.POST)
	public ProcesoTransicionAutomaticaDTO activarProcesoTransicionAutomatica(@RequestBody ProcesoTransicionAutomaticaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoTransicionAutomaticaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarProcesoTransicionAutomatica", method=RequestMethod.POST)
	public ProcesoTransicionAutomaticaDTO inactivarProcesoTransicionAutomatica(@RequestBody ProcesoTransicionAutomaticaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoTransicionAutomaticaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarProcesoTransicionAutomatica", method=RequestMethod.POST)
	public ProcesoTransicionAutomaticaDTO actualizarProcesoTransicionAutomatica(@RequestBody ProcesoTransicionAutomaticaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoTransicionAutomaticaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarProcesoTransicionAutomatica", method=RequestMethod.POST)
	public ProcesoTransicionAutomaticaDTO guardarProcesoTransicionAutomatica(@RequestBody ProcesoTransicionAutomaticaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoTransicionAutomaticaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@RequestMapping(value="/ejecutarProcesoTransicionAutomatica", method=RequestMethod.POST)
	public ProcesoTransicionAutomaticaDTO ejecutarProcesoTransicionAutomatica(@RequestBody ProcesoTransicionAutomaticaDTO dto, @RequestHeader("Authorization") String token)throws FlexException {
		try {
			return procesoTransicionAutomaticaService.ejecutar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}

	@RequestMapping(value="/programarProcesoTransicionAutomatica", method=RequestMethod.POST)
	public ProcesoTransicionAutomaticaDTO programarProcesoTransicionAutomatica(@RequestBody ProcesoTransicionAutomaticaDTO dto, @RequestHeader("Authorization") String token)throws FlexException {
		try {
			return procesoTransicionAutomaticaService.programar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private DocumentoRelacionExpedienteSvc documentoRelacionExpedienteService;
	
	@RequestMapping(value="/consultaXIdDocumentoRelacionExpediente", method=RequestMethod.POST)
	public DocumentoRelacionExpedienteDTO consultaXIdDocumentoRelacionExpediente(@RequestBody String llave) throws FlexException {
		try {
			return documentoRelacionExpedienteService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosDocumentoRelacionExpediente", method=RequestMethod.POST)
	public int contarResultadosDocumentoRelacionExpediente(@RequestBody DocumentoRelacionExpedienteFilterDTO dto) throws FlexException  {
		try {
			return documentoRelacionExpedienteService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaDocumentoRelacionExpediente", method=RequestMethod.POST)
	public DocumentoRelacionExpedienteDTO consultaUnicaDocumentoRelacionExpediente(@RequestBody DocumentoRelacionExpedienteFilterDTO dto) throws FlexException  {
		try {
			return documentoRelacionExpedienteService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaDocumentoRelacionExpediente", method=RequestMethod.POST)
	public List<DocumentoRelacionExpedienteDTO> listarConsultaDocumentoRelacionExpediente(@RequestBody DocumentoRelacionExpedienteFilterDTO dto) throws FlexException  {
		try {
			return documentoRelacionExpedienteService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarDocumentoRelacionExpediente", method=RequestMethod.POST)
	public DocumentoRelacionExpedienteDTO activarDocumentoRelacionExpediente(@RequestBody DocumentoRelacionExpedienteDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoRelacionExpedienteService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarDocumentoRelacionExpediente", method=RequestMethod.POST)
	public DocumentoRelacionExpedienteDTO inactivarDocumentoRelacionExpediente(@RequestBody DocumentoRelacionExpedienteDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoRelacionExpedienteService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarDocumentoRelacionExpediente", method=RequestMethod.POST)
	public DocumentoRelacionExpedienteDTO actualizarDocumentoRelacionExpediente(@RequestBody DocumentoRelacionExpedienteDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoRelacionExpedienteService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarDocumentoRelacionExpediente", method=RequestMethod.POST)
	public DocumentoRelacionExpedienteDTO guardarDocumentoRelacionExpediente(@RequestBody DocumentoRelacionExpedienteDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoRelacionExpedienteService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private DocumentoRelacionGestorSvc documentoRelacionGestorService;
	
	@RequestMapping(value="/consultaXIdDocumentoRelacionGestor", method=RequestMethod.POST)
	public DocumentoRelacionGestorDTO consultaXIdDocumentoRelacionGestor(@RequestBody String llave) throws FlexException {
		try {
			return documentoRelacionGestorService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosDocumentoRelacionGestor", method=RequestMethod.POST)
	public int contarResultadosDocumentoRelacionGestor(@RequestBody DocumentoRelacionGestorFilterDTO dto) throws FlexException  {
		try {
			return documentoRelacionGestorService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaDocumentoRelacionGestor", method=RequestMethod.POST)
	public DocumentoRelacionGestorDTO consultaUnicaDocumentoRelacionGestor(@RequestBody DocumentoRelacionGestorFilterDTO dto) throws FlexException  {
		try {
			return documentoRelacionGestorService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaDocumentoRelacionGestor", method=RequestMethod.POST)
	public List<DocumentoRelacionGestorDTO> listarConsultaDocumentoRelacionGestor(@RequestBody DocumentoRelacionGestorFilterDTO dto) throws FlexException  {
		try {
			return documentoRelacionGestorService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarDocumentoRelacionGestor", method=RequestMethod.POST)
	public DocumentoRelacionGestorDTO activarDocumentoRelacionGestor(@RequestBody DocumentoRelacionGestorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoRelacionGestorService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarDocumentoRelacionGestor", method=RequestMethod.POST)
	public DocumentoRelacionGestorDTO inactivarDocumentoRelacionGestor(@RequestBody DocumentoRelacionGestorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoRelacionGestorService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarDocumentoRelacionGestor", method=RequestMethod.POST)
	public DocumentoRelacionGestorDTO actualizarDocumentoRelacionGestor(@RequestBody DocumentoRelacionGestorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoRelacionGestorService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarDocumentoRelacionGestor", method=RequestMethod.POST)
	public DocumentoRelacionGestorDTO guardarDocumentoRelacionGestor(@RequestBody DocumentoRelacionGestorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoRelacionGestorService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@RequestMapping(value="/listarExpedientesGestionadoresDocumentoRelacionGestor", method=RequestMethod.POST)
	public List<DocumentoRelacionGestorDTO> listarExpedientesGestionadoresDocumentoRelacionGestor(@RequestBody DocumentoRelacionGestorFilterDTO dto)throws FlexException {
		try {
			return documentoRelacionGestorService.listarExpedientesGestionadores(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private DocumentoPlantillaCaracteristicaSvc documentoPlantillaCaracteristicaService;
	
	@RequestMapping(value="/consultaXIdDocumentoPlantillaCaracteristica", method=RequestMethod.POST)
	public DocumentoPlantillaCaracteristicaDTO consultaXIdDocumentoPlantillaCaracteristica(@RequestBody String llave) throws FlexException {
		try {
			return documentoPlantillaCaracteristicaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosDocumentoPlantillaCaracteristica", method=RequestMethod.POST)
	public int contarResultadosDocumentoPlantillaCaracteristica(@RequestBody DocumentoPlantillaCaracteristicaFilterDTO dto) throws FlexException  {
		try {
			return documentoPlantillaCaracteristicaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaDocumentoPlantillaCaracteristica", method=RequestMethod.POST)
	public DocumentoPlantillaCaracteristicaDTO consultaUnicaDocumentoPlantillaCaracteristica(@RequestBody DocumentoPlantillaCaracteristicaFilterDTO dto) throws FlexException  {
		try {
			return documentoPlantillaCaracteristicaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaDocumentoPlantillaCaracteristica", method=RequestMethod.POST)
	public List<DocumentoPlantillaCaracteristicaDTO> listarConsultaDocumentoPlantillaCaracteristica(@RequestBody DocumentoPlantillaCaracteristicaFilterDTO dto) throws FlexException  {
		try {
			return documentoPlantillaCaracteristicaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarDocumentoPlantillaCaracteristica", method=RequestMethod.POST)
	public DocumentoPlantillaCaracteristicaDTO activarDocumentoPlantillaCaracteristica(@RequestBody DocumentoPlantillaCaracteristicaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoPlantillaCaracteristicaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarDocumentoPlantillaCaracteristica", method=RequestMethod.POST)
	public DocumentoPlantillaCaracteristicaDTO inactivarDocumentoPlantillaCaracteristica(@RequestBody DocumentoPlantillaCaracteristicaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoPlantillaCaracteristicaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarDocumentoPlantillaCaracteristica", method=RequestMethod.POST)
	public DocumentoPlantillaCaracteristicaDTO actualizarDocumentoPlantillaCaracteristica(@RequestBody DocumentoPlantillaCaracteristicaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoPlantillaCaracteristicaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarDocumentoPlantillaCaracteristica", method=RequestMethod.POST)
	public DocumentoPlantillaCaracteristicaDTO guardarDocumentoPlantillaCaracteristica(@RequestBody DocumentoPlantillaCaracteristicaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoPlantillaCaracteristicaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@RequestMapping(value="/listarCargaDocumentoPlantillaCaracteristica", method=RequestMethod.POST)
	public DocumentoPlantillaCaracteristicaDTO listarCargaDocumentoPlantillaCaracteristica(@RequestBody DocumentoPlantillaCaracteristicaFilterDTO dto)throws FlexException {
		try {
			return documentoPlantillaCaracteristicaService.listarCarga(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private PedidoVentaSvc pedidoVentaService;
	
	@RequestMapping(value="/consultaXIdPedidoVenta", method=RequestMethod.POST)
	public PedidoVentaDTO consultaXIdPedidoVenta(@RequestBody String llave) throws FlexException {
		try {
			return pedidoVentaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosPedidoVenta", method=RequestMethod.POST)
	public int contarResultadosPedidoVenta(@RequestBody PedidoVentaFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaPedidoVenta", method=RequestMethod.POST)
	public PedidoVentaDTO consultaUnicaPedidoVenta(@RequestBody PedidoVentaFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaPedidoVenta", method=RequestMethod.POST)
	public List<PedidoVentaDTO> listarConsultaPedidoVenta(@RequestBody PedidoVentaFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarPedidoVenta", method=RequestMethod.POST)
	public PedidoVentaDTO activarPedidoVenta(@RequestBody PedidoVentaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarPedidoVenta", method=RequestMethod.POST)
	public PedidoVentaDTO inactivarPedidoVenta(@RequestBody PedidoVentaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarPedidoVenta", method=RequestMethod.POST)
	public PedidoVentaDTO actualizarPedidoVenta(@RequestBody PedidoVentaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarPedidoVenta", method=RequestMethod.POST)
	public PedidoVentaDTO guardarPedidoVenta(@RequestBody PedidoVentaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return crudService.save(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@RequestMapping(value="/consultaCompletaPedidoVenta", method=RequestMethod.POST)
	public PedidoVentaDTO consultaCompletaPedidoVenta(@RequestBody PedidoVentaFilterDTO dto)throws FlexException {
		try {
			return pedidoVentaService.consultaCompleta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private CallListDocumentWithFilters documentListWithFiltersFunction;
	
	@RequestMapping(value="/listarAvanzadoPedidoVenta", method=RequestMethod.POST)
	public List<PedidoVentaDTO> listarAvanzadoPedidoVenta(@RequestBody PedidoVentaFilterDTO dto)throws FlexException {
		try {
			return documentListWithFiltersFunction.listarAvanzado(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	
	@Autowired private PedidoVentaCaracteristicaSvc pedidoVentaCaracteristicaService;
	
	@RequestMapping(value="/consultaXIdPedidoVentaCaracteristica", method=RequestMethod.POST)
	public PedidoVentaCaracteristicaDTO consultaXIdPedidoVentaCaracteristica(@RequestBody String llave) throws FlexException {
		try {
			return pedidoVentaCaracteristicaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosPedidoVentaCaracteristica", method=RequestMethod.POST)
	public int contarResultadosPedidoVentaCaracteristica(@RequestBody PedidoVentaCaracteristicaFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaCaracteristicaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaPedidoVentaCaracteristica", method=RequestMethod.POST)
	public PedidoVentaCaracteristicaDTO consultaUnicaPedidoVentaCaracteristica(@RequestBody PedidoVentaCaracteristicaFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaCaracteristicaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaPedidoVentaCaracteristica", method=RequestMethod.POST)
	public List<PedidoVentaCaracteristicaDTO> listarConsultaPedidoVentaCaracteristica(@RequestBody PedidoVentaCaracteristicaFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaCaracteristicaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarPedidoVentaCaracteristica", method=RequestMethod.POST)
	public PedidoVentaCaracteristicaDTO activarPedidoVentaCaracteristica(@RequestBody PedidoVentaCaracteristicaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaCaracteristicaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarPedidoVentaCaracteristica", method=RequestMethod.POST)
	public PedidoVentaCaracteristicaDTO inactivarPedidoVentaCaracteristica(@RequestBody PedidoVentaCaracteristicaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaCaracteristicaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarPedidoVentaCaracteristica", method=RequestMethod.POST)
	public PedidoVentaCaracteristicaDTO actualizarPedidoVentaCaracteristica(@RequestBody PedidoVentaCaracteristicaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaCaracteristicaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarPedidoVentaCaracteristica", method=RequestMethod.POST)
	public PedidoVentaCaracteristicaDTO guardarPedidoVentaCaracteristica(@RequestBody PedidoVentaCaracteristicaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaCaracteristicaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@RequestMapping(value="/completarDatosBasePedidoVentaCaracteristica", method=RequestMethod.POST)
	public PedidoVentaCaracteristicaDTO completarDatosBasePedidoVentaCaracteristica(@RequestBody PedidoVentaCaracteristicaFilterDTO dto)throws FlexException {
		try {
			return pedidoVentaCaracteristicaService.completarDatosBase(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private ProcesoTransicionSvc procesoTransicionService;
	
	@RequestMapping(value="/consultaXIdProcesoTransicion", method=RequestMethod.POST)
	public ProcesoTransicionDTO consultaXIdProcesoTransicion(@RequestBody String llave) throws FlexException {
		try {
			return procesoTransicionService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosProcesoTransicion", method=RequestMethod.POST)
	public int contarResultadosProcesoTransicion(@RequestBody ProcesoTransicionFilterDTO dto) throws FlexException  {
		try {
			return procesoTransicionService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaProcesoTransicion", method=RequestMethod.POST)
	public ProcesoTransicionDTO consultaUnicaProcesoTransicion(@RequestBody ProcesoTransicionFilterDTO dto) throws FlexException  {
		try {
			return procesoTransicionService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaProcesoTransicion", method=RequestMethod.POST)
	public List<ProcesoTransicionDTO> listarConsultaProcesoTransicion(@RequestBody ProcesoTransicionFilterDTO dto) throws FlexException  {
		try {
			return procesoTransicionService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarProcesoTransicion", method=RequestMethod.POST)
	public ProcesoTransicionDTO activarProcesoTransicion(@RequestBody ProcesoTransicionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoTransicionService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarProcesoTransicion", method=RequestMethod.POST)
	public ProcesoTransicionDTO inactivarProcesoTransicion(@RequestBody ProcesoTransicionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoTransicionService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarProcesoTransicion", method=RequestMethod.POST)
	public ProcesoTransicionDTO actualizarProcesoTransicion(@RequestBody ProcesoTransicionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoTransicionService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarProcesoTransicion", method=RequestMethod.POST)
	public ProcesoTransicionDTO guardarProcesoTransicion(@RequestBody ProcesoTransicionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoTransicionService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private PedidoVentaAjusteSvc pedidoVentaAjusteService;
	
	@RequestMapping(value="/consultaXIdPedidoVentaAjuste", method=RequestMethod.POST)
	public PedidoVentaAjusteDTO consultaXIdPedidoVentaAjuste(@RequestBody String llave) throws FlexException {
		try {
			return pedidoVentaAjusteService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosPedidoVentaAjuste", method=RequestMethod.POST)
	public int contarResultadosPedidoVentaAjuste(@RequestBody PedidoVentaAjusteFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaAjusteService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaPedidoVentaAjuste", method=RequestMethod.POST)
	public PedidoVentaAjusteDTO consultaUnicaPedidoVentaAjuste(@RequestBody PedidoVentaAjusteFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaAjusteService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaPedidoVentaAjuste", method=RequestMethod.POST)
	public List<PedidoVentaAjusteDTO> listarConsultaPedidoVentaAjuste(@RequestBody PedidoVentaAjusteFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaAjusteService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarPedidoVentaAjuste", method=RequestMethod.POST)
	public PedidoVentaAjusteDTO activarPedidoVentaAjuste(@RequestBody PedidoVentaAjusteDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaAjusteService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarPedidoVentaAjuste", method=RequestMethod.POST)
	public PedidoVentaAjusteDTO inactivarPedidoVentaAjuste(@RequestBody PedidoVentaAjusteDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaAjusteService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarPedidoVentaAjuste", method=RequestMethod.POST)
	public PedidoVentaAjusteDTO actualizarPedidoVentaAjuste(@RequestBody PedidoVentaAjusteDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaAjusteService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarPedidoVentaAjuste", method=RequestMethod.POST)
	public PedidoVentaAjusteDTO guardarPedidoVentaAjuste(@RequestBody PedidoVentaAjusteDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaAjusteService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private DocumentoPlantillaSvc documentoPlantillaService;
	
	@RequestMapping(value="/consultaXIdDocumentoPlantilla", method=RequestMethod.POST)
	public DocumentoPlantillaDTO consultaXIdDocumentoPlantilla(@RequestBody String llave) throws FlexException {
		try {
			return documentoPlantillaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosDocumentoPlantilla", method=RequestMethod.POST)
	public int contarResultadosDocumentoPlantilla(@RequestBody DocumentoPlantillaFilterDTO dto) throws FlexException  {
		try {
			return documentoPlantillaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaDocumentoPlantilla", method=RequestMethod.POST)
	public DocumentoPlantillaDTO consultaUnicaDocumentoPlantilla(@RequestBody DocumentoPlantillaFilterDTO dto) throws FlexException  {
		try {
			return documentoPlantillaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaDocumentoPlantilla", method=RequestMethod.POST)
	public List<DocumentoPlantillaDTO> listarConsultaDocumentoPlantilla(@RequestBody DocumentoPlantillaFilterDTO dto) throws FlexException  {
		try {
			return documentoPlantillaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarDocumentoPlantilla", method=RequestMethod.POST)
	public DocumentoPlantillaDTO activarDocumentoPlantilla(@RequestBody DocumentoPlantillaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoPlantillaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarDocumentoPlantilla", method=RequestMethod.POST)
	public DocumentoPlantillaDTO inactivarDocumentoPlantilla(@RequestBody DocumentoPlantillaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoPlantillaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarDocumentoPlantilla", method=RequestMethod.POST)
	public DocumentoPlantillaDTO actualizarDocumentoPlantilla(@RequestBody DocumentoPlantillaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoPlantillaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarDocumentoPlantilla", method=RequestMethod.POST)
	public DocumentoPlantillaDTO guardarDocumentoPlantilla(@RequestBody DocumentoPlantillaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoPlantillaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@RequestMapping(value="/consultaUsuarioDocumentoPlantilla", method=RequestMethod.POST)
	public List<DocumentoPlantillaDTO> consultaUsuarioDocumentoPlantilla(@RequestBody DocumentoPlantillaFilterDTO dto)throws FlexException {
		try {
			return documentoPlantillaService.consultaUsuario(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}

	@RequestMapping(value="/obtenerCamposDocumentoPlantilla", method=RequestMethod.POST)
	public DocumentoPlantillaDTO obtenerCamposDocumentoPlantilla(@RequestBody DocumentoPlantillaDTO dto, @RequestHeader("Authorization") String token)throws FlexException {
		try {
			return documentoPlantillaService.obtenerCampos(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}

	@RequestMapping(value="/duplicarDocumentoPlantilla", method=RequestMethod.POST)
	public DocumentoPlantillaDTO duplicarDocumentoPlantilla(@RequestBody DocumentoPlantillaDTO dto, @RequestHeader("Authorization") String token)throws FlexException {
		try {
			return documentoPlantillaService.duplicar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}

	@RequestMapping(value="/consultaAdministradorDocumentoPlantilla", method=RequestMethod.POST)
	public List<DocumentoPlantillaDTO> consultaAdministradorDocumentoPlantilla(@RequestBody DocumentoPlantillaFilterDTO dto)throws FlexException {
		try {
			return documentoPlantillaService.consultaAdministrador(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private MovimientoSvc movimientoService;
	
	@RequestMapping(value="/consultaXIdMovimiento", method=RequestMethod.POST)
	public MovimientoDTO consultaXIdMovimiento(@RequestBody String llave) throws FlexException {
		try {
			return movimientoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosMovimiento", method=RequestMethod.POST)
	public int contarResultadosMovimiento(@RequestBody MovimientoFilterDTO dto) throws FlexException  {
		try {
			return movimientoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaMovimiento", method=RequestMethod.POST)
	public MovimientoDTO consultaUnicaMovimiento(@RequestBody MovimientoFilterDTO dto) throws FlexException  {
		try {
			return movimientoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaMovimiento", method=RequestMethod.POST)
	public List<MovimientoDTO> listarConsultaMovimiento(@RequestBody MovimientoFilterDTO dto) throws FlexException  {
		try {
			return movimientoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarMovimiento", method=RequestMethod.POST)
	public MovimientoDTO activarMovimiento(@RequestBody MovimientoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return movimientoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarMovimiento", method=RequestMethod.POST)
	public MovimientoDTO inactivarMovimiento(@RequestBody MovimientoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return movimientoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarMovimiento", method=RequestMethod.POST)
	public MovimientoDTO actualizarMovimiento(@RequestBody MovimientoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return movimientoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarMovimiento", method=RequestMethod.POST)
	public MovimientoDTO guardarMovimiento(@RequestBody MovimientoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return movimientoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@RequestMapping(value="/obtenerMovimientoAnteriorFechaMovimiento", method=RequestMethod.POST)
	public List<MovimientoDTO> obtenerMovimientoAnteriorFechaMovimiento(@RequestBody MovimientoFilterDTO dto)throws FlexException {
		try {
			return movimientoService.obtenerMovimientoAnteriorFecha(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}

	@RequestMapping(value="/obtenerMovimientoSiguienteFechaMovimiento", method=RequestMethod.POST)
	public List<MovimientoDTO> obtenerMovimientoSiguienteFechaMovimiento(@RequestBody MovimientoFilterDTO dto)throws FlexException {
		try {
			return movimientoService.obtenerMovimientoSiguienteFecha(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private TurnoSvc turnoService;
	
	@RequestMapping(value="/consultaXIdTurno", method=RequestMethod.POST)
	public TurnoDTO consultaXIdTurno(@RequestBody String llave) throws FlexException {
		try {
			return turnoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosTurno", method=RequestMethod.POST)
	public int contarResultadosTurno(@RequestBody TurnoFilterDTO dto) throws FlexException  {
		try {
			return turnoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaTurno", method=RequestMethod.POST)
	public TurnoDTO consultaUnicaTurno(@RequestBody TurnoFilterDTO dto) throws FlexException  {
		try {
			return turnoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaTurno", method=RequestMethod.POST)
	public List<TurnoDTO> listarConsultaTurno(@RequestBody TurnoFilterDTO dto) throws FlexException  {
		try {
			return turnoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarTurno", method=RequestMethod.POST)
	public TurnoDTO activarTurno(@RequestBody TurnoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return turnoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarTurno", method=RequestMethod.POST)
	public TurnoDTO inactivarTurno(@RequestBody TurnoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return turnoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarTurno", method=RequestMethod.POST)
	public TurnoDTO actualizarTurno(@RequestBody TurnoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return turnoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarTurno", method=RequestMethod.POST)
	public TurnoDTO guardarTurno(@RequestBody TurnoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return turnoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private TarifaSvc tarifaService;
	
	@RequestMapping(value="/consultaXIdTarifa", method=RequestMethod.POST)
	public TarifaDTO consultaXIdTarifa(@RequestBody String llave) throws FlexException {
		try {
			return tarifaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosTarifa", method=RequestMethod.POST)
	public int contarResultadosTarifa(@RequestBody TarifaFilterDTO dto) throws FlexException  {
		try {
			return tarifaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaTarifa", method=RequestMethod.POST)
	public TarifaDTO consultaUnicaTarifa(@RequestBody TarifaFilterDTO dto) throws FlexException  {
		try {
			return tarifaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaTarifa", method=RequestMethod.POST)
	public List<TarifaDTO> listarConsultaTarifa(@RequestBody TarifaFilterDTO dto) throws FlexException  {
		try {
			return tarifaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarTarifa", method=RequestMethod.POST)
	public TarifaDTO activarTarifa(@RequestBody TarifaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return tarifaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarTarifa", method=RequestMethod.POST)
	public TarifaDTO inactivarTarifa(@RequestBody TarifaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return tarifaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarTarifa", method=RequestMethod.POST)
	public TarifaDTO actualizarTarifa(@RequestBody TarifaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return tarifaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarTarifa", method=RequestMethod.POST)
	public TarifaDTO guardarTarifa(@RequestBody TarifaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return tarifaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private PedidoVentaDineroSvc pedidoVentaDineroService;
	
	@RequestMapping(value="/consultaXIdPedidoVentaDinero", method=RequestMethod.POST)
	public PedidoVentaDineroDTO consultaXIdPedidoVentaDinero(@RequestBody String llave) throws FlexException {
		try {
			return pedidoVentaDineroService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosPedidoVentaDinero", method=RequestMethod.POST)
	public int contarResultadosPedidoVentaDinero(@RequestBody PedidoVentaDineroFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaDineroService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaPedidoVentaDinero", method=RequestMethod.POST)
	public PedidoVentaDineroDTO consultaUnicaPedidoVentaDinero(@RequestBody PedidoVentaDineroFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaDineroService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaPedidoVentaDinero", method=RequestMethod.POST)
	public List<PedidoVentaDineroDTO> listarConsultaPedidoVentaDinero(@RequestBody PedidoVentaDineroFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaDineroService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarPedidoVentaDinero", method=RequestMethod.POST)
	public PedidoVentaDineroDTO activarPedidoVentaDinero(@RequestBody PedidoVentaDineroDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaDineroService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarPedidoVentaDinero", method=RequestMethod.POST)
	public PedidoVentaDineroDTO inactivarPedidoVentaDinero(@RequestBody PedidoVentaDineroDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaDineroService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarPedidoVentaDinero", method=RequestMethod.POST)
	public PedidoVentaDineroDTO actualizarPedidoVentaDinero(@RequestBody PedidoVentaDineroDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaDineroService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarPedidoVentaDinero", method=RequestMethod.POST)
	public PedidoVentaDineroDTO guardarPedidoVentaDinero(@RequestBody PedidoVentaDineroDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaDineroService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private CuentaSvc cuentaService;
	
	@RequestMapping(value="/consultaXIdCuenta", method=RequestMethod.POST)
	public CuentaDTO consultaXIdCuenta(@RequestBody String llave) throws FlexException {
		try {
			return cuentaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosCuenta", method=RequestMethod.POST)
	public int contarResultadosCuenta(@RequestBody CuentaFilterDTO dto) throws FlexException  {
		try {
			return cuentaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaCuenta", method=RequestMethod.POST)
	public CuentaDTO consultaUnicaCuenta(@RequestBody CuentaFilterDTO dto) throws FlexException  {
		try {
			return cuentaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaCuenta", method=RequestMethod.POST)
	public List<CuentaDTO> listarConsultaCuenta(@RequestBody CuentaFilterDTO dto) throws FlexException  {
		try {
			return cuentaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarCuenta", method=RequestMethod.POST)
	public CuentaDTO activarCuenta(@RequestBody CuentaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cuentaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarCuenta", method=RequestMethod.POST)
	public CuentaDTO inactivarCuenta(@RequestBody CuentaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cuentaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarCuenta", method=RequestMethod.POST)
	public CuentaDTO actualizarCuenta(@RequestBody CuentaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cuentaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarCuenta", method=RequestMethod.POST)
	public CuentaDTO guardarCuenta(@RequestBody CuentaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cuentaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private TarifarioSvc tarifarioService;
	
	@RequestMapping(value="/consultaXIdTarifario", method=RequestMethod.POST)
	public TarifarioDTO consultaXIdTarifario(@RequestBody String llave) throws FlexException {
		try {
			return tarifarioService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosTarifario", method=RequestMethod.POST)
	public int contarResultadosTarifario(@RequestBody TarifarioFilterDTO dto) throws FlexException  {
		try {
			return tarifarioService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaTarifario", method=RequestMethod.POST)
	public TarifarioDTO consultaUnicaTarifario(@RequestBody TarifarioFilterDTO dto) throws FlexException  {
		try {
			return tarifarioService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaTarifario", method=RequestMethod.POST)
	public List<TarifarioDTO> listarConsultaTarifario(@RequestBody TarifarioFilterDTO dto) throws FlexException  {
		try {
			return tarifarioService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarTarifario", method=RequestMethod.POST)
	public TarifarioDTO activarTarifario(@RequestBody TarifarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return tarifarioService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarTarifario", method=RequestMethod.POST)
	public TarifarioDTO inactivarTarifario(@RequestBody TarifarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return tarifarioService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarTarifario", method=RequestMethod.POST)
	public TarifarioDTO actualizarTarifario(@RequestBody TarifarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return tarifarioService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarTarifario", method=RequestMethod.POST)
	public TarifarioDTO guardarTarifario(@RequestBody TarifarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return tarifarioService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private CuentaAuxiliarDocumentoSvc cuentaAuxiliarDocumentoService;
	
	@RequestMapping(value="/consultaXIdCuentaAuxiliarDocumento", method=RequestMethod.POST)
	public CuentaAuxiliarDocumentoDTO consultaXIdCuentaAuxiliarDocumento(@RequestBody String llave) throws FlexException {
		try {
			return cuentaAuxiliarDocumentoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosCuentaAuxiliarDocumento", method=RequestMethod.POST)
	public int contarResultadosCuentaAuxiliarDocumento(@RequestBody CuentaAuxiliarDocumentoFilterDTO dto) throws FlexException  {
		try {
			return cuentaAuxiliarDocumentoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaCuentaAuxiliarDocumento", method=RequestMethod.POST)
	public CuentaAuxiliarDocumentoDTO consultaUnicaCuentaAuxiliarDocumento(@RequestBody CuentaAuxiliarDocumentoFilterDTO dto) throws FlexException  {
		try {
			return cuentaAuxiliarDocumentoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaCuentaAuxiliarDocumento", method=RequestMethod.POST)
	public List<CuentaAuxiliarDocumentoDTO> listarConsultaCuentaAuxiliarDocumento(@RequestBody CuentaAuxiliarDocumentoFilterDTO dto) throws FlexException  {
		try {
			return cuentaAuxiliarDocumentoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarCuentaAuxiliarDocumento", method=RequestMethod.POST)
	public CuentaAuxiliarDocumentoDTO activarCuentaAuxiliarDocumento(@RequestBody CuentaAuxiliarDocumentoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cuentaAuxiliarDocumentoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarCuentaAuxiliarDocumento", method=RequestMethod.POST)
	public CuentaAuxiliarDocumentoDTO inactivarCuentaAuxiliarDocumento(@RequestBody CuentaAuxiliarDocumentoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cuentaAuxiliarDocumentoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarCuentaAuxiliarDocumento", method=RequestMethod.POST)
	public CuentaAuxiliarDocumentoDTO actualizarCuentaAuxiliarDocumento(@RequestBody CuentaAuxiliarDocumentoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cuentaAuxiliarDocumentoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarCuentaAuxiliarDocumento", method=RequestMethod.POST)
	public CuentaAuxiliarDocumentoDTO guardarCuentaAuxiliarDocumento(@RequestBody CuentaAuxiliarDocumentoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cuentaAuxiliarDocumentoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private ComprobanteConfiguracionDetalleSvc comprobanteConfiguracionDetalleService;
	
	@RequestMapping(value="/consultaXIdComprobanteConfiguracionDetalle", method=RequestMethod.POST)
	public ComprobanteConfiguracionDetalleDTO consultaXIdComprobanteConfiguracionDetalle(@RequestBody String llave) throws FlexException {
		try {
			return comprobanteConfiguracionDetalleService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosComprobanteConfiguracionDetalle", method=RequestMethod.POST)
	public int contarResultadosComprobanteConfiguracionDetalle(@RequestBody ComprobanteConfiguracionDetalleFilterDTO dto) throws FlexException  {
		try {
			return comprobanteConfiguracionDetalleService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaComprobanteConfiguracionDetalle", method=RequestMethod.POST)
	public ComprobanteConfiguracionDetalleDTO consultaUnicaComprobanteConfiguracionDetalle(@RequestBody ComprobanteConfiguracionDetalleFilterDTO dto) throws FlexException  {
		try {
			return comprobanteConfiguracionDetalleService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaComprobanteConfiguracionDetalle", method=RequestMethod.POST)
	public List<ComprobanteConfiguracionDetalleDTO> listarConsultaComprobanteConfiguracionDetalle(@RequestBody ComprobanteConfiguracionDetalleFilterDTO dto) throws FlexException  {
		try {
			return comprobanteConfiguracionDetalleService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarComprobanteConfiguracionDetalle", method=RequestMethod.POST)
	public ComprobanteConfiguracionDetalleDTO activarComprobanteConfiguracionDetalle(@RequestBody ComprobanteConfiguracionDetalleDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return comprobanteConfiguracionDetalleService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarComprobanteConfiguracionDetalle", method=RequestMethod.POST)
	public ComprobanteConfiguracionDetalleDTO inactivarComprobanteConfiguracionDetalle(@RequestBody ComprobanteConfiguracionDetalleDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return comprobanteConfiguracionDetalleService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarComprobanteConfiguracionDetalle", method=RequestMethod.POST)
	public ComprobanteConfiguracionDetalleDTO actualizarComprobanteConfiguracionDetalle(@RequestBody ComprobanteConfiguracionDetalleDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return comprobanteConfiguracionDetalleService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarComprobanteConfiguracionDetalle", method=RequestMethod.POST)
	public ComprobanteConfiguracionDetalleDTO guardarComprobanteConfiguracionDetalle(@RequestBody ComprobanteConfiguracionDetalleDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return comprobanteConfiguracionDetalleService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private CuentaContableMovimientoSvc cuentaContableMovimientoService;
	
	@RequestMapping(value="/consultaXIdCuentaContableMovimiento", method=RequestMethod.POST)
	public CuentaContableMovimientoDTO consultaXIdCuentaContableMovimiento(@RequestBody String llave) throws FlexException {
		try {
			return cuentaContableMovimientoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosCuentaContableMovimiento", method=RequestMethod.POST)
	public int contarResultadosCuentaContableMovimiento(@RequestBody CuentaContableMovimientoFilterDTO dto) throws FlexException  {
		try {
			return cuentaContableMovimientoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaCuentaContableMovimiento", method=RequestMethod.POST)
	public CuentaContableMovimientoDTO consultaUnicaCuentaContableMovimiento(@RequestBody CuentaContableMovimientoFilterDTO dto) throws FlexException  {
		try {
			return cuentaContableMovimientoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaCuentaContableMovimiento", method=RequestMethod.POST)
	public List<CuentaContableMovimientoDTO> listarConsultaCuentaContableMovimiento(@RequestBody CuentaContableMovimientoFilterDTO dto) throws FlexException  {
		try {
			return cuentaContableMovimientoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarCuentaContableMovimiento", method=RequestMethod.POST)
	public CuentaContableMovimientoDTO activarCuentaContableMovimiento(@RequestBody CuentaContableMovimientoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cuentaContableMovimientoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarCuentaContableMovimiento", method=RequestMethod.POST)
	public CuentaContableMovimientoDTO inactivarCuentaContableMovimiento(@RequestBody CuentaContableMovimientoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cuentaContableMovimientoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarCuentaContableMovimiento", method=RequestMethod.POST)
	public CuentaContableMovimientoDTO actualizarCuentaContableMovimiento(@RequestBody CuentaContableMovimientoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cuentaContableMovimientoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarCuentaContableMovimiento", method=RequestMethod.POST)
	public CuentaContableMovimientoDTO guardarCuentaContableMovimiento(@RequestBody CuentaContableMovimientoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cuentaContableMovimientoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private CuentaAuxiliarPlantillaSvc cuentaAuxiliarPlantillaService;
	
	@RequestMapping(value="/consultaXIdCuentaAuxiliarPlantilla", method=RequestMethod.POST)
	public CuentaAuxiliarPlantillaDTO consultaXIdCuentaAuxiliarPlantilla(@RequestBody String llave) throws FlexException {
		try {
			return cuentaAuxiliarPlantillaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosCuentaAuxiliarPlantilla", method=RequestMethod.POST)
	public int contarResultadosCuentaAuxiliarPlantilla(@RequestBody CuentaAuxiliarPlantillaFilterDTO dto) throws FlexException  {
		try {
			return cuentaAuxiliarPlantillaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaCuentaAuxiliarPlantilla", method=RequestMethod.POST)
	public CuentaAuxiliarPlantillaDTO consultaUnicaCuentaAuxiliarPlantilla(@RequestBody CuentaAuxiliarPlantillaFilterDTO dto) throws FlexException  {
		try {
			return cuentaAuxiliarPlantillaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaCuentaAuxiliarPlantilla", method=RequestMethod.POST)
	public List<CuentaAuxiliarPlantillaDTO> listarConsultaCuentaAuxiliarPlantilla(@RequestBody CuentaAuxiliarPlantillaFilterDTO dto) throws FlexException  {
		try {
			return cuentaAuxiliarPlantillaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarCuentaAuxiliarPlantilla", method=RequestMethod.POST)
	public CuentaAuxiliarPlantillaDTO activarCuentaAuxiliarPlantilla(@RequestBody CuentaAuxiliarPlantillaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cuentaAuxiliarPlantillaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarCuentaAuxiliarPlantilla", method=RequestMethod.POST)
	public CuentaAuxiliarPlantillaDTO inactivarCuentaAuxiliarPlantilla(@RequestBody CuentaAuxiliarPlantillaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cuentaAuxiliarPlantillaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarCuentaAuxiliarPlantilla", method=RequestMethod.POST)
	public CuentaAuxiliarPlantillaDTO actualizarCuentaAuxiliarPlantilla(@RequestBody CuentaAuxiliarPlantillaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cuentaAuxiliarPlantillaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarCuentaAuxiliarPlantilla", method=RequestMethod.POST)
	public CuentaAuxiliarPlantillaDTO guardarCuentaAuxiliarPlantilla(@RequestBody CuentaAuxiliarPlantillaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cuentaAuxiliarPlantillaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private ComprobanteConfiguracionSvc comprobanteConfiguracionService;
	
	@RequestMapping(value="/consultaXIdComprobanteConfiguracion", method=RequestMethod.POST)
	public ComprobanteConfiguracionDTO consultaXIdComprobanteConfiguracion(@RequestBody String llave) throws FlexException {
		try {
			return comprobanteConfiguracionService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosComprobanteConfiguracion", method=RequestMethod.POST)
	public int contarResultadosComprobanteConfiguracion(@RequestBody ComprobanteConfiguracionFilterDTO dto) throws FlexException  {
		try {
			return comprobanteConfiguracionService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaComprobanteConfiguracion", method=RequestMethod.POST)
	public ComprobanteConfiguracionDTO consultaUnicaComprobanteConfiguracion(@RequestBody ComprobanteConfiguracionFilterDTO dto) throws FlexException  {
		try {
			return comprobanteConfiguracionService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaComprobanteConfiguracion", method=RequestMethod.POST)
	public List<ComprobanteConfiguracionDTO> listarConsultaComprobanteConfiguracion(@RequestBody ComprobanteConfiguracionFilterDTO dto) throws FlexException  {
		try {
			return comprobanteConfiguracionService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarComprobanteConfiguracion", method=RequestMethod.POST)
	public ComprobanteConfiguracionDTO activarComprobanteConfiguracion(@RequestBody ComprobanteConfiguracionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return comprobanteConfiguracionService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarComprobanteConfiguracion", method=RequestMethod.POST)
	public ComprobanteConfiguracionDTO inactivarComprobanteConfiguracion(@RequestBody ComprobanteConfiguracionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return comprobanteConfiguracionService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarComprobanteConfiguracion", method=RequestMethod.POST)
	public ComprobanteConfiguracionDTO actualizarComprobanteConfiguracion(@RequestBody ComprobanteConfiguracionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return comprobanteConfiguracionService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarComprobanteConfiguracion", method=RequestMethod.POST)
	public ComprobanteConfiguracionDTO guardarComprobanteConfiguracion(@RequestBody ComprobanteConfiguracionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return comprobanteConfiguracionService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private ComprobanteCuentaDetalleSvc comprobanteCuentaDetalleService;
	
	@RequestMapping(value="/consultaXIdComprobanteCuentaDetalle", method=RequestMethod.POST)
	public ComprobanteCuentaDetalleDTO consultaXIdComprobanteCuentaDetalle(@RequestBody String llave) throws FlexException {
		try {
			return comprobanteCuentaDetalleService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosComprobanteCuentaDetalle", method=RequestMethod.POST)
	public int contarResultadosComprobanteCuentaDetalle(@RequestBody ComprobanteCuentaDetalleFilterDTO dto) throws FlexException  {
		try {
			return comprobanteCuentaDetalleService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaComprobanteCuentaDetalle", method=RequestMethod.POST)
	public ComprobanteCuentaDetalleDTO consultaUnicaComprobanteCuentaDetalle(@RequestBody ComprobanteCuentaDetalleFilterDTO dto) throws FlexException  {
		try {
			return comprobanteCuentaDetalleService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaComprobanteCuentaDetalle", method=RequestMethod.POST)
	public List<ComprobanteCuentaDetalleDTO> listarConsultaComprobanteCuentaDetalle(@RequestBody ComprobanteCuentaDetalleFilterDTO dto) throws FlexException  {
		try {
			return comprobanteCuentaDetalleService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarComprobanteCuentaDetalle", method=RequestMethod.POST)
	public ComprobanteCuentaDetalleDTO activarComprobanteCuentaDetalle(@RequestBody ComprobanteCuentaDetalleDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return comprobanteCuentaDetalleService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarComprobanteCuentaDetalle", method=RequestMethod.POST)
	public ComprobanteCuentaDetalleDTO inactivarComprobanteCuentaDetalle(@RequestBody ComprobanteCuentaDetalleDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return comprobanteCuentaDetalleService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarComprobanteCuentaDetalle", method=RequestMethod.POST)
	public ComprobanteCuentaDetalleDTO actualizarComprobanteCuentaDetalle(@RequestBody ComprobanteCuentaDetalleDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return comprobanteCuentaDetalleService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarComprobanteCuentaDetalle", method=RequestMethod.POST)
	public ComprobanteCuentaDetalleDTO guardarComprobanteCuentaDetalle(@RequestBody ComprobanteCuentaDetalleDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return comprobanteCuentaDetalleService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private ComprobanteContableSvc comprobanteContableService;
	
	@RequestMapping(value="/consultaXIdComprobanteContable", method=RequestMethod.POST)
	public ComprobanteContableDTO consultaXIdComprobanteContable(@RequestBody String llave) throws FlexException {
		try {
			return comprobanteContableService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosComprobanteContable", method=RequestMethod.POST)
	public int contarResultadosComprobanteContable(@RequestBody ComprobanteContableFilterDTO dto) throws FlexException  {
		try {
			return comprobanteContableService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaComprobanteContable", method=RequestMethod.POST)
	public ComprobanteContableDTO consultaUnicaComprobanteContable(@RequestBody ComprobanteContableFilterDTO dto) throws FlexException  {
		try {
			return comprobanteContableService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaComprobanteContable", method=RequestMethod.POST)
	public List<ComprobanteContableDTO> listarConsultaComprobanteContable(@RequestBody ComprobanteContableFilterDTO dto) throws FlexException  {
		try {
			return comprobanteContableService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarComprobanteContable", method=RequestMethod.POST)
	public ComprobanteContableDTO activarComprobanteContable(@RequestBody ComprobanteContableDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return comprobanteContableService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarComprobanteContable", method=RequestMethod.POST)
	public ComprobanteContableDTO inactivarComprobanteContable(@RequestBody ComprobanteContableDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return comprobanteContableService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarComprobanteContable", method=RequestMethod.POST)
	public ComprobanteContableDTO actualizarComprobanteContable(@RequestBody ComprobanteContableDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return comprobanteContableService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarComprobanteContable", method=RequestMethod.POST)
	public ComprobanteContableDTO guardarComprobanteContable(@RequestBody ComprobanteContableDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return comprobanteContableService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private CuentaContableSvc cuentaContableService;
	
	@RequestMapping(value="/consultaXIdCuentaContable", method=RequestMethod.POST)
	public CuentaContableDTO consultaXIdCuentaContable(@RequestBody String llave) throws FlexException {
		try {
			return cuentaContableService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosCuentaContable", method=RequestMethod.POST)
	public int contarResultadosCuentaContable(@RequestBody CuentaContableFilterDTO dto) throws FlexException  {
		try {
			return cuentaContableService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaCuentaContable", method=RequestMethod.POST)
	public CuentaContableDTO consultaUnicaCuentaContable(@RequestBody CuentaContableFilterDTO dto) throws FlexException  {
		try {
			return cuentaContableService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaCuentaContable", method=RequestMethod.POST)
	public List<CuentaContableDTO> listarConsultaCuentaContable(@RequestBody CuentaContableFilterDTO dto) throws FlexException  {
		try {
			return cuentaContableService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarCuentaContable", method=RequestMethod.POST)
	public CuentaContableDTO activarCuentaContable(@RequestBody CuentaContableDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cuentaContableService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarCuentaContable", method=RequestMethod.POST)
	public CuentaContableDTO inactivarCuentaContable(@RequestBody CuentaContableDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cuentaContableService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarCuentaContable", method=RequestMethod.POST)
	public CuentaContableDTO actualizarCuentaContable(@RequestBody CuentaContableDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cuentaContableService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarCuentaContable", method=RequestMethod.POST)
	public CuentaContableDTO guardarCuentaContable(@RequestBody CuentaContableDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cuentaContableService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private CatalogoContableSvc catalogoContableService;
	
	@RequestMapping(value="/consultaXIdCatalogoContable", method=RequestMethod.POST)
	public CatalogoContableDTO consultaXIdCatalogoContable(@RequestBody String llave) throws FlexException {
		try {
			return catalogoContableService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosCatalogoContable", method=RequestMethod.POST)
	public int contarResultadosCatalogoContable(@RequestBody CatalogoContableFilterDTO dto) throws FlexException  {
		try {
			return catalogoContableService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaCatalogoContable", method=RequestMethod.POST)
	public CatalogoContableDTO consultaUnicaCatalogoContable(@RequestBody CatalogoContableFilterDTO dto) throws FlexException  {
		try {
			return catalogoContableService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaCatalogoContable", method=RequestMethod.POST)
	public List<CatalogoContableDTO> listarConsultaCatalogoContable(@RequestBody CatalogoContableFilterDTO dto) throws FlexException  {
		try {
			return catalogoContableService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarCatalogoContable", method=RequestMethod.POST)
	public CatalogoContableDTO activarCatalogoContable(@RequestBody CatalogoContableDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return catalogoContableService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarCatalogoContable", method=RequestMethod.POST)
	public CatalogoContableDTO inactivarCatalogoContable(@RequestBody CatalogoContableDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return catalogoContableService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarCatalogoContable", method=RequestMethod.POST)
	public CatalogoContableDTO actualizarCatalogoContable(@RequestBody CatalogoContableDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return catalogoContableService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarCatalogoContable", method=RequestMethod.POST)
	public CatalogoContableDTO guardarCatalogoContable(@RequestBody CatalogoContableDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return catalogoContableService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private ActividadSvc actividadService;
	
	@RequestMapping(value="/consultaXIdActividad", method=RequestMethod.POST)
	public ActividadDTO consultaXIdActividad(@RequestBody String llave) throws FlexException {
		try {
			return actividadService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosActividad", method=RequestMethod.POST)
	public int contarResultadosActividad(@RequestBody ActividadFilterDTO dto) throws FlexException  {
		try {
			return actividadService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaActividad", method=RequestMethod.POST)
	public ActividadDTO consultaUnicaActividad(@RequestBody ActividadFilterDTO dto) throws FlexException  {
		try {
			return actividadService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaActividad", method=RequestMethod.POST)
	public List<ActividadDTO> listarConsultaActividad(@RequestBody ActividadFilterDTO dto) throws FlexException  {
		try {
			return actividadService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarActividad", method=RequestMethod.POST)
	public ActividadDTO activarActividad(@RequestBody ActividadDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return actividadService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarActividad", method=RequestMethod.POST)
	public ActividadDTO inactivarActividad(@RequestBody ActividadDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return actividadService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarActividad", method=RequestMethod.POST)
	public ActividadDTO actualizarActividad(@RequestBody ActividadDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return actividadService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarActividad", method=RequestMethod.POST)
	public ActividadDTO guardarActividad(@RequestBody ActividadDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return actividadService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private PedidoVentaTiempoSvc pedidoVentaTiempoService;
	
	@RequestMapping(value="/consultaXIdPedidoVentaTiempo", method=RequestMethod.POST)
	public PedidoVentaTiempoDTO consultaXIdPedidoVentaTiempo(@RequestBody String llave) throws FlexException {
		try {
			return pedidoVentaTiempoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosPedidoVentaTiempo", method=RequestMethod.POST)
	public int contarResultadosPedidoVentaTiempo(@RequestBody PedidoVentaTiempoFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaTiempoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaPedidoVentaTiempo", method=RequestMethod.POST)
	public PedidoVentaTiempoDTO consultaUnicaPedidoVentaTiempo(@RequestBody PedidoVentaTiempoFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaTiempoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaPedidoVentaTiempo", method=RequestMethod.POST)
	public List<PedidoVentaTiempoDTO> listarConsultaPedidoVentaTiempo(@RequestBody PedidoVentaTiempoFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaTiempoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarPedidoVentaTiempo", method=RequestMethod.POST)
	public PedidoVentaTiempoDTO activarPedidoVentaTiempo(@RequestBody PedidoVentaTiempoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaTiempoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarPedidoVentaTiempo", method=RequestMethod.POST)
	public PedidoVentaTiempoDTO inactivarPedidoVentaTiempo(@RequestBody PedidoVentaTiempoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaTiempoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarPedidoVentaTiempo", method=RequestMethod.POST)
	public PedidoVentaTiempoDTO actualizarPedidoVentaTiempo(@RequestBody PedidoVentaTiempoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaTiempoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarPedidoVentaTiempo", method=RequestMethod.POST)
	public PedidoVentaTiempoDTO guardarPedidoVentaTiempo(@RequestBody PedidoVentaTiempoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaTiempoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private PropiedadSvc propiedadService;
	
	@RequestMapping(value="/consultaXIdPropiedad", method=RequestMethod.POST)
	public PropiedadDTO consultaXIdPropiedad(@RequestBody String llave) throws FlexException {
		try {
			return propiedadService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosPropiedad", method=RequestMethod.POST)
	public int contarResultadosPropiedad(@RequestBody PropiedadFilterDTO dto) throws FlexException  {
		try {
			return propiedadService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaPropiedad", method=RequestMethod.POST)
	public PropiedadDTO consultaUnicaPropiedad(@RequestBody PropiedadFilterDTO dto) throws FlexException  {
		try {
			return propiedadService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaPropiedad", method=RequestMethod.POST)
	public List<PropiedadDTO> listarConsultaPropiedad(@RequestBody PropiedadFilterDTO dto) throws FlexException  {
		try {
			return propiedadService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarPropiedad", method=RequestMethod.POST)
	public PropiedadDTO activarPropiedad(@RequestBody PropiedadDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return propiedadService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarPropiedad", method=RequestMethod.POST)
	public PropiedadDTO inactivarPropiedad(@RequestBody PropiedadDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return propiedadService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarPropiedad", method=RequestMethod.POST)
	public PropiedadDTO actualizarPropiedad(@RequestBody PropiedadDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return propiedadService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarPropiedad", method=RequestMethod.POST)
	public PropiedadDTO guardarPropiedad(@RequestBody PropiedadDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return propiedadService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private RelacionInternaSvc relacionInternaService;
	
	@RequestMapping(value="/consultaXIdRelacionInterna", method=RequestMethod.POST)
	public RelacionInternaDTO consultaXIdRelacionInterna(@RequestBody String llave) throws FlexException {
		try {
			return relacionInternaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosRelacionInterna", method=RequestMethod.POST)
	public int contarResultadosRelacionInterna(@RequestBody RelacionInternaFilterDTO dto) throws FlexException  {
		try {
			return relacionInternaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaRelacionInterna", method=RequestMethod.POST)
	public RelacionInternaDTO consultaUnicaRelacionInterna(@RequestBody RelacionInternaFilterDTO dto) throws FlexException  {
		try {
			return relacionInternaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaRelacionInterna", method=RequestMethod.POST)
	public List<RelacionInternaDTO> listarConsultaRelacionInterna(@RequestBody RelacionInternaFilterDTO dto) throws FlexException  {
		try {
			return relacionInternaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarRelacionInterna", method=RequestMethod.POST)
	public RelacionInternaDTO activarRelacionInterna(@RequestBody RelacionInternaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return relacionInternaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarRelacionInterna", method=RequestMethod.POST)
	public RelacionInternaDTO inactivarRelacionInterna(@RequestBody RelacionInternaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return relacionInternaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarRelacionInterna", method=RequestMethod.POST)
	public RelacionInternaDTO actualizarRelacionInterna(@RequestBody RelacionInternaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return relacionInternaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarRelacionInterna", method=RequestMethod.POST)
	public RelacionInternaDTO guardarRelacionInterna(@RequestBody RelacionInternaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return relacionInternaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@RequestMapping(value="/listarRelacionRelacionInterna", method=RequestMethod.POST)
	public List<RelacionInternaDTO> listarRelacionRelacionInterna(@RequestBody RelacionInternaFilterDTO dto)throws FlexException {
		try {
			return relacionInternaService.listarRelacion(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private EncuestaRespuestaSvc encuestaRespuestaService;
	
	@RequestMapping(value="/consultaXIdEncuestaRespuesta", method=RequestMethod.POST)
	public EncuestaRespuestaDTO consultaXIdEncuestaRespuesta(@RequestBody String llave) throws FlexException {
		try {
			return encuestaRespuestaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosEncuestaRespuesta", method=RequestMethod.POST)
	public int contarResultadosEncuestaRespuesta(@RequestBody EncuestaRespuestaFilterDTO dto) throws FlexException  {
		try {
			return encuestaRespuestaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaEncuestaRespuesta", method=RequestMethod.POST)
	public EncuestaRespuestaDTO consultaUnicaEncuestaRespuesta(@RequestBody EncuestaRespuestaFilterDTO dto) throws FlexException  {
		try {
			return encuestaRespuestaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaEncuestaRespuesta", method=RequestMethod.POST)
	public List<EncuestaRespuestaDTO> listarConsultaEncuestaRespuesta(@RequestBody EncuestaRespuestaFilterDTO dto) throws FlexException  {
		try {
			return encuestaRespuestaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarEncuestaRespuesta", method=RequestMethod.POST)
	public EncuestaRespuestaDTO activarEncuestaRespuesta(@RequestBody EncuestaRespuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaRespuestaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarEncuestaRespuesta", method=RequestMethod.POST)
	public EncuestaRespuestaDTO inactivarEncuestaRespuesta(@RequestBody EncuestaRespuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaRespuestaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarEncuestaRespuesta", method=RequestMethod.POST)
	public EncuestaRespuestaDTO actualizarEncuestaRespuesta(@RequestBody EncuestaRespuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaRespuestaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarEncuestaRespuesta", method=RequestMethod.POST)
	public EncuestaRespuestaDTO guardarEncuestaRespuesta(@RequestBody EncuestaRespuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaRespuestaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private CambioSvc cambioService;
	
	@RequestMapping(value="/consultaXIdCambio", method=RequestMethod.POST)
	public CambioDTO consultaXIdCambio(@RequestBody String llave) throws FlexException {
		try {
			return cambioService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosCambio", method=RequestMethod.POST)
	public int contarResultadosCambio(@RequestBody CambioFilterDTO dto) throws FlexException  {
		try {
			return cambioService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaCambio", method=RequestMethod.POST)
	public CambioDTO consultaUnicaCambio(@RequestBody CambioFilterDTO dto) throws FlexException  {
		try {
			return cambioService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaCambio", method=RequestMethod.POST)
	public List<CambioDTO> listarConsultaCambio(@RequestBody CambioFilterDTO dto) throws FlexException  {
		try {
			return cambioService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarCambio", method=RequestMethod.POST)
	public CambioDTO activarCambio(@RequestBody CambioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cambioService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarCambio", method=RequestMethod.POST)
	public CambioDTO inactivarCambio(@RequestBody CambioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cambioService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarCambio", method=RequestMethod.POST)
	public CambioDTO actualizarCambio(@RequestBody CambioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cambioService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarCambio", method=RequestMethod.POST)
	public CambioDTO guardarCambio(@RequestBody CambioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cambioService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private EncuestaOpcionRespuestaSvc encuestaOpcionRespuestaService;
	
	@RequestMapping(value="/consultaXIdEncuestaOpcionRespuesta", method=RequestMethod.POST)
	public EncuestaOpcionRespuestaDTO consultaXIdEncuestaOpcionRespuesta(@RequestBody String llave) throws FlexException {
		try {
			return encuestaOpcionRespuestaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosEncuestaOpcionRespuesta", method=RequestMethod.POST)
	public int contarResultadosEncuestaOpcionRespuesta(@RequestBody EncuestaOpcionRespuestaFilterDTO dto) throws FlexException  {
		try {
			return encuestaOpcionRespuestaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaEncuestaOpcionRespuesta", method=RequestMethod.POST)
	public EncuestaOpcionRespuestaDTO consultaUnicaEncuestaOpcionRespuesta(@RequestBody EncuestaOpcionRespuestaFilterDTO dto) throws FlexException  {
		try {
			return encuestaOpcionRespuestaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaEncuestaOpcionRespuesta", method=RequestMethod.POST)
	public List<EncuestaOpcionRespuestaDTO> listarConsultaEncuestaOpcionRespuesta(@RequestBody EncuestaOpcionRespuestaFilterDTO dto) throws FlexException  {
		try {
			return encuestaOpcionRespuestaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarEncuestaOpcionRespuesta", method=RequestMethod.POST)
	public EncuestaOpcionRespuestaDTO activarEncuestaOpcionRespuesta(@RequestBody EncuestaOpcionRespuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaOpcionRespuestaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarEncuestaOpcionRespuesta", method=RequestMethod.POST)
	public EncuestaOpcionRespuestaDTO inactivarEncuestaOpcionRespuesta(@RequestBody EncuestaOpcionRespuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaOpcionRespuestaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarEncuestaOpcionRespuesta", method=RequestMethod.POST)
	public EncuestaOpcionRespuestaDTO actualizarEncuestaOpcionRespuesta(@RequestBody EncuestaOpcionRespuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaOpcionRespuestaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarEncuestaOpcionRespuesta", method=RequestMethod.POST)
	public EncuestaOpcionRespuestaDTO guardarEncuestaOpcionRespuesta(@RequestBody EncuestaOpcionRespuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaOpcionRespuestaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private PropiedadValorDefinidoSvc propiedadValorDefinidoService;
	
	@RequestMapping(value="/consultaXIdPropiedadValorDefinido", method=RequestMethod.POST)
	public PropiedadValorDefinidoDTO consultaXIdPropiedadValorDefinido(@RequestBody String llave) throws FlexException {
		try {
			return propiedadValorDefinidoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosPropiedadValorDefinido", method=RequestMethod.POST)
	public int contarResultadosPropiedadValorDefinido(@RequestBody PropiedadValorDefinidoFilterDTO dto) throws FlexException  {
		try {
			return propiedadValorDefinidoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaPropiedadValorDefinido", method=RequestMethod.POST)
	public PropiedadValorDefinidoDTO consultaUnicaPropiedadValorDefinido(@RequestBody PropiedadValorDefinidoFilterDTO dto) throws FlexException  {
		try {
			return propiedadValorDefinidoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaPropiedadValorDefinido", method=RequestMethod.POST)
	public List<PropiedadValorDefinidoDTO> listarConsultaPropiedadValorDefinido(@RequestBody PropiedadValorDefinidoFilterDTO dto) throws FlexException  {
		try {
			return propiedadValorDefinidoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarPropiedadValorDefinido", method=RequestMethod.POST)
	public PropiedadValorDefinidoDTO activarPropiedadValorDefinido(@RequestBody PropiedadValorDefinidoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return propiedadValorDefinidoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarPropiedadValorDefinido", method=RequestMethod.POST)
	public PropiedadValorDefinidoDTO inactivarPropiedadValorDefinido(@RequestBody PropiedadValorDefinidoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return propiedadValorDefinidoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarPropiedadValorDefinido", method=RequestMethod.POST)
	public PropiedadValorDefinidoDTO actualizarPropiedadValorDefinido(@RequestBody PropiedadValorDefinidoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return propiedadValorDefinidoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarPropiedadValorDefinido", method=RequestMethod.POST)
	public PropiedadValorDefinidoDTO guardarPropiedadValorDefinido(@RequestBody PropiedadValorDefinidoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return propiedadValorDefinidoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@RequestMapping(value="/listarPorOrigenPropiedadValorDefinido", method=RequestMethod.POST)
	public List<PropiedadValorDefinidoDTO> listarPorOrigenPropiedadValorDefinido(@RequestBody PropiedadValorDefinidoFilterDTO dto)throws FlexException {
		try {
			return propiedadValorDefinidoService.listarPorOrigen(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private EncuestaGrupoSvc encuestaGrupoService;
	
	@RequestMapping(value="/consultaXIdEncuestaGrupo", method=RequestMethod.POST)
	public EncuestaGrupoDTO consultaXIdEncuestaGrupo(@RequestBody String llave) throws FlexException {
		try {
			return encuestaGrupoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosEncuestaGrupo", method=RequestMethod.POST)
	public int contarResultadosEncuestaGrupo(@RequestBody EncuestaGrupoFilterDTO dto) throws FlexException  {
		try {
			return encuestaGrupoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaEncuestaGrupo", method=RequestMethod.POST)
	public EncuestaGrupoDTO consultaUnicaEncuestaGrupo(@RequestBody EncuestaGrupoFilterDTO dto) throws FlexException  {
		try {
			return encuestaGrupoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaEncuestaGrupo", method=RequestMethod.POST)
	public List<EncuestaGrupoDTO> listarConsultaEncuestaGrupo(@RequestBody EncuestaGrupoFilterDTO dto) throws FlexException  {
		try {
			return encuestaGrupoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarEncuestaGrupo", method=RequestMethod.POST)
	public EncuestaGrupoDTO activarEncuestaGrupo(@RequestBody EncuestaGrupoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaGrupoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarEncuestaGrupo", method=RequestMethod.POST)
	public EncuestaGrupoDTO inactivarEncuestaGrupo(@RequestBody EncuestaGrupoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaGrupoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarEncuestaGrupo", method=RequestMethod.POST)
	public EncuestaGrupoDTO actualizarEncuestaGrupo(@RequestBody EncuestaGrupoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaGrupoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarEncuestaGrupo", method=RequestMethod.POST)
	public EncuestaGrupoDTO guardarEncuestaGrupo(@RequestBody EncuestaGrupoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaGrupoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@RequestMapping(value="/responderEncuestaEncuestaGrupo", method=RequestMethod.POST)
	public EncuestaGrupoDTO responderEncuestaEncuestaGrupo(@RequestBody EncuestaGrupoDTO dto, @RequestHeader("Authorization") String token)throws FlexException {
		try {
			return encuestaGrupoService.responderEncuesta(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}

	@RequestMapping(value="/copiarEncuestaGrupo", method=RequestMethod.POST)
	public EncuestaGrupoDTO copiarEncuestaGrupo(@RequestBody EncuestaGrupoDTO dto, @RequestHeader("Authorization") String token)throws FlexException {
		try {
			return encuestaGrupoService.copiar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private EncuestaPreguntaSvc encuestaPreguntaService;
	
	@RequestMapping(value="/consultaXIdEncuestaPregunta", method=RequestMethod.POST)
	public EncuestaPreguntaDTO consultaXIdEncuestaPregunta(@RequestBody String llave) throws FlexException {
		try {
			return encuestaPreguntaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosEncuestaPregunta", method=RequestMethod.POST)
	public int contarResultadosEncuestaPregunta(@RequestBody EncuestaPreguntaFilterDTO dto) throws FlexException  {
		try {
			return encuestaPreguntaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaEncuestaPregunta", method=RequestMethod.POST)
	public EncuestaPreguntaDTO consultaUnicaEncuestaPregunta(@RequestBody EncuestaPreguntaFilterDTO dto) throws FlexException  {
		try {
			return encuestaPreguntaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaEncuestaPregunta", method=RequestMethod.POST)
	public List<EncuestaPreguntaDTO> listarConsultaEncuestaPregunta(@RequestBody EncuestaPreguntaFilterDTO dto) throws FlexException  {
		try {
			return encuestaPreguntaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarEncuestaPregunta", method=RequestMethod.POST)
	public EncuestaPreguntaDTO activarEncuestaPregunta(@RequestBody EncuestaPreguntaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaPreguntaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarEncuestaPregunta", method=RequestMethod.POST)
	public EncuestaPreguntaDTO inactivarEncuestaPregunta(@RequestBody EncuestaPreguntaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaPreguntaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarEncuestaPregunta", method=RequestMethod.POST)
	public EncuestaPreguntaDTO actualizarEncuestaPregunta(@RequestBody EncuestaPreguntaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaPreguntaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarEncuestaPregunta", method=RequestMethod.POST)
	public EncuestaPreguntaDTO guardarEncuestaPregunta(@RequestBody EncuestaPreguntaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaPreguntaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@RequestMapping(value="/listarPermitidasEncuestaPregunta", method=RequestMethod.POST)
	public List<EncuestaPreguntaDTO> listarPermitidasEncuestaPregunta(@RequestBody EncuestaPreguntaFilterDTO dto)throws FlexException {
		try {
			return encuestaPreguntaService.listarPermitidas(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private EncuestaSvc encuestaService;
	
	@RequestMapping(value="/consultaXIdEncuesta", method=RequestMethod.POST)
	public EncuestaDTO consultaXIdEncuesta(@RequestBody String llave) throws FlexException {
		try {
			return encuestaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosEncuesta", method=RequestMethod.POST)
	public int contarResultadosEncuesta(@RequestBody EncuestaFilterDTO dto) throws FlexException  {
		try {
			return encuestaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaEncuesta", method=RequestMethod.POST)
	public EncuestaDTO consultaUnicaEncuesta(@RequestBody EncuestaFilterDTO dto) throws FlexException  {
		try {
			return encuestaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaEncuesta", method=RequestMethod.POST)
	public List<EncuestaDTO> listarConsultaEncuesta(@RequestBody EncuestaFilterDTO dto) throws FlexException  {
		try {
			return encuestaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarEncuesta", method=RequestMethod.POST)
	public EncuestaDTO activarEncuesta(@RequestBody EncuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarEncuesta", method=RequestMethod.POST)
	public EncuestaDTO inactivarEncuesta(@RequestBody EncuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarEncuesta", method=RequestMethod.POST)
	public EncuestaDTO actualizarEncuesta(@RequestBody EncuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarEncuesta", method=RequestMethod.POST)
	public EncuestaDTO guardarEncuesta(@RequestBody EncuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@RequestMapping(value="/copiarEncuesta", method=RequestMethod.POST)
	public EncuestaDTO copiarEncuesta(@RequestBody EncuestaDTO dto, @RequestHeader("Authorization") String token)throws FlexException {
		try {
			return encuestaService.copiar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}

	@RequestMapping(value="/listarDisponiblesEncuesta", method=RequestMethod.POST)
	public List<EncuestaDTO> listarDisponiblesEncuesta(@RequestBody EncuestaFilterDTO dto)throws FlexException {
		try {
			return encuestaService.listarDisponibles(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private UsuarioRolSvc usuarioRolService;
	
	@RequestMapping(value="/consultaXIdUsuarioRol", method=RequestMethod.POST)
	public UsuarioRolDTO consultaXIdUsuarioRol(@RequestBody String llave) throws FlexException {
		try {
			return usuarioRolService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosUsuarioRol", method=RequestMethod.POST)
	public int contarResultadosUsuarioRol(@RequestBody UsuarioRolFilterDTO dto) throws FlexException  {
		try {
			return usuarioRolService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaUsuarioRol", method=RequestMethod.POST)
	public UsuarioRolDTO consultaUnicaUsuarioRol(@RequestBody UsuarioRolFilterDTO dto) throws FlexException  {
		try {
			return usuarioRolService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaUsuarioRol", method=RequestMethod.POST)
	public List<UsuarioRolDTO> listarConsultaUsuarioRol(@RequestBody UsuarioRolFilterDTO dto) throws FlexException  {
		try {
			return usuarioRolService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarUsuarioRol", method=RequestMethod.POST)
	public UsuarioRolDTO activarUsuarioRol(@RequestBody UsuarioRolDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioRolService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarUsuarioRol", method=RequestMethod.POST)
	public UsuarioRolDTO inactivarUsuarioRol(@RequestBody UsuarioRolDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioRolService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarUsuarioRol", method=RequestMethod.POST)
	public UsuarioRolDTO actualizarUsuarioRol(@RequestBody UsuarioRolDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioRolService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarUsuarioRol", method=RequestMethod.POST)
	public UsuarioRolDTO guardarUsuarioRol(@RequestBody UsuarioRolDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioRolService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private PuestoSvc puestoService;
	
	@RequestMapping(value="/consultaXIdPuesto", method=RequestMethod.POST)
	public PuestoDTO consultaXIdPuesto(@RequestBody String llave) throws FlexException {
		try {
			return puestoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosPuesto", method=RequestMethod.POST)
	public int contarResultadosPuesto(@RequestBody PuestoFilterDTO dto) throws FlexException  {
		try {
			return puestoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaPuesto", method=RequestMethod.POST)
	public PuestoDTO consultaUnicaPuesto(@RequestBody PuestoFilterDTO dto) throws FlexException  {
		try {
			return puestoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaPuesto", method=RequestMethod.POST)
	public List<PuestoDTO> listarConsultaPuesto(@RequestBody PuestoFilterDTO dto) throws FlexException  {
		try {
			return puestoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarPuesto", method=RequestMethod.POST)
	public PuestoDTO activarPuesto(@RequestBody PuestoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return puestoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarPuesto", method=RequestMethod.POST)
	public PuestoDTO inactivarPuesto(@RequestBody PuestoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return puestoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarPuesto", method=RequestMethod.POST)
	public PuestoDTO actualizarPuesto(@RequestBody PuestoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return puestoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarPuesto", method=RequestMethod.POST)
	public PuestoDTO guardarPuesto(@RequestBody PuestoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return puestoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private RolAccesoSvc rolAccesoService;
	
	@RequestMapping(value="/consultaXIdRolAcceso", method=RequestMethod.POST)
	public RolAccesoDTO consultaXIdRolAcceso(@RequestBody String llave) throws FlexException {
		try {
			return rolAccesoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosRolAcceso", method=RequestMethod.POST)
	public int contarResultadosRolAcceso(@RequestBody RolAccesoFilterDTO dto) throws FlexException  {
		try {
			return rolAccesoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaRolAcceso", method=RequestMethod.POST)
	public RolAccesoDTO consultaUnicaRolAcceso(@RequestBody RolAccesoFilterDTO dto) throws FlexException  {
		try {
			return rolAccesoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaRolAcceso", method=RequestMethod.POST)
	public List<RolAccesoDTO> listarConsultaRolAcceso(@RequestBody RolAccesoFilterDTO dto) throws FlexException  {
		try {
			return rolAccesoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarRolAcceso", method=RequestMethod.POST)
	public RolAccesoDTO activarRolAcceso(@RequestBody RolAccesoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return rolAccesoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarRolAcceso", method=RequestMethod.POST)
	public RolAccesoDTO inactivarRolAcceso(@RequestBody RolAccesoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return rolAccesoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarRolAcceso", method=RequestMethod.POST)
	public RolAccesoDTO actualizarRolAcceso(@RequestBody RolAccesoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return rolAccesoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarRolAcceso", method=RequestMethod.POST)
	public RolAccesoDTO guardarRolAcceso(@RequestBody RolAccesoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return rolAccesoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@RequestMapping(value="/consultaUsuarioDocumentoRolAcceso", method=RequestMethod.POST)
	public List<RolAccesoDTO> consultaUsuarioDocumentoRolAcceso(@RequestBody RolAccesoFilterDTO dto)throws FlexException {
		try {
			return rolAccesoService.consultaUsuarioDocumento(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private UsuarioSvc usuarioService;
	
	@RequestMapping(value="/consultaXIdUsuario", method=RequestMethod.POST)
	public UsuarioDTO consultaXIdUsuario(@RequestBody String llave) throws FlexException {
		try {
			return usuarioService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosUsuario", method=RequestMethod.POST)
	public int contarResultadosUsuario(@RequestBody UsuarioFilterDTO dto) throws FlexException  {
		try {
			return usuarioService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaUsuario", method=RequestMethod.POST)
	public UsuarioDTO consultaUnicaUsuario(@RequestBody UsuarioFilterDTO dto) throws FlexException  {
		try {
			return usuarioService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaUsuario", method=RequestMethod.POST)
	public List<UsuarioDTO> listarConsultaUsuario(@RequestBody UsuarioFilterDTO dto) throws FlexException  {
		try {
			return usuarioService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarUsuario", method=RequestMethod.POST)
	public UsuarioDTO activarUsuario(@RequestBody UsuarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarUsuario", method=RequestMethod.POST)
	public UsuarioDTO inactivarUsuario(@RequestBody UsuarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarUsuario", method=RequestMethod.POST)
	public UsuarioDTO actualizarUsuario(@RequestBody UsuarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarUsuario", method=RequestMethod.POST)
	public UsuarioDTO guardarUsuario(@RequestBody UsuarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@RequestMapping(value="/listarRolUsuario", method=RequestMethod.POST)
	public List<UsuarioDTO> listarRolUsuario(@RequestBody UsuarioFilterDTO dto)throws FlexException {
		try {
			return usuarioService.listarRol(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private WebServiceSvc webServiceService;
	
	@RequestMapping(value="/consultaXIdWebService", method=RequestMethod.POST)
	public WebServiceDTO consultaXIdWebService(@RequestBody String llave) throws FlexException {
		try {
			return webServiceService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosWebService", method=RequestMethod.POST)
	public int contarResultadosWebService(@RequestBody WebServiceFilterDTO dto) throws FlexException  {
		try {
			return webServiceService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaWebService", method=RequestMethod.POST)
	public WebServiceDTO consultaUnicaWebService(@RequestBody WebServiceFilterDTO dto) throws FlexException  {
		try {
			return webServiceService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaWebService", method=RequestMethod.POST)
	public List<WebServiceDTO> listarConsultaWebService(@RequestBody WebServiceFilterDTO dto) throws FlexException  {
		try {
			return webServiceService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarWebService", method=RequestMethod.POST)
	public WebServiceDTO activarWebService(@RequestBody WebServiceDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return webServiceService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarWebService", method=RequestMethod.POST)
	public WebServiceDTO inactivarWebService(@RequestBody WebServiceDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return webServiceService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarWebService", method=RequestMethod.POST)
	public WebServiceDTO actualizarWebService(@RequestBody WebServiceDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return webServiceService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarWebService", method=RequestMethod.POST)
	public WebServiceDTO guardarWebService(@RequestBody WebServiceDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return webServiceService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private PostRespuestaSvc postRespuestaService;
	
	@RequestMapping(value="/consultaXIdPostRespuesta", method=RequestMethod.POST)
	public PostRespuestaDTO consultaXIdPostRespuesta(@RequestBody String llave) throws FlexException {
		try {
			return postRespuestaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosPostRespuesta", method=RequestMethod.POST)
	public int contarResultadosPostRespuesta(@RequestBody PostRespuestaFilterDTO dto) throws FlexException  {
		try {
			return postRespuestaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaPostRespuesta", method=RequestMethod.POST)
	public PostRespuestaDTO consultaUnicaPostRespuesta(@RequestBody PostRespuestaFilterDTO dto) throws FlexException  {
		try {
			return postRespuestaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaPostRespuesta", method=RequestMethod.POST)
	public List<PostRespuestaDTO> listarConsultaPostRespuesta(@RequestBody PostRespuestaFilterDTO dto) throws FlexException  {
		try {
			return postRespuestaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarPostRespuesta", method=RequestMethod.POST)
	public PostRespuestaDTO activarPostRespuesta(@RequestBody PostRespuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return postRespuestaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarPostRespuesta", method=RequestMethod.POST)
	public PostRespuestaDTO inactivarPostRespuesta(@RequestBody PostRespuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return postRespuestaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarPostRespuesta", method=RequestMethod.POST)
	public PostRespuestaDTO actualizarPostRespuesta(@RequestBody PostRespuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return postRespuestaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarPostRespuesta", method=RequestMethod.POST)
	public PostRespuestaDTO guardarPostRespuesta(@RequestBody PostRespuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return postRespuestaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@RequestMapping(value="/listarEnOrdenPostRespuesta", method=RequestMethod.POST)
	public List<PostRespuestaDTO> listarEnOrdenPostRespuesta(@RequestBody PostRespuestaFilterDTO dto)throws FlexException {
		try {
			return postRespuestaService.listarEnOrden(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private GPSLocalizacionSvc gPSLocalizacionService;
	
	@RequestMapping(value="/consultaXIdGPSLocalizacion", method=RequestMethod.POST)
	public GPSLocalizacionDTO consultaXIdGPSLocalizacion(@RequestBody String llave) throws FlexException {
		try {
			return gPSLocalizacionService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosGPSLocalizacion", method=RequestMethod.POST)
	public int contarResultadosGPSLocalizacion(@RequestBody GPSLocalizacionFilterDTO dto) throws FlexException  {
		try {
			return gPSLocalizacionService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaGPSLocalizacion", method=RequestMethod.POST)
	public GPSLocalizacionDTO consultaUnicaGPSLocalizacion(@RequestBody GPSLocalizacionFilterDTO dto) throws FlexException  {
		try {
			return gPSLocalizacionService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaGPSLocalizacion", method=RequestMethod.POST)
	public List<GPSLocalizacionDTO> listarConsultaGPSLocalizacion(@RequestBody GPSLocalizacionFilterDTO dto) throws FlexException  {
		try {
			return gPSLocalizacionService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarGPSLocalizacion", method=RequestMethod.POST)
	public GPSLocalizacionDTO activarGPSLocalizacion(@RequestBody GPSLocalizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return gPSLocalizacionService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarGPSLocalizacion", method=RequestMethod.POST)
	public GPSLocalizacionDTO inactivarGPSLocalizacion(@RequestBody GPSLocalizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return gPSLocalizacionService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarGPSLocalizacion", method=RequestMethod.POST)
	public GPSLocalizacionDTO actualizarGPSLocalizacion(@RequestBody GPSLocalizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return gPSLocalizacionService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarGPSLocalizacion", method=RequestMethod.POST)
	public GPSLocalizacionDTO guardarGPSLocalizacion(@RequestBody GPSLocalizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return gPSLocalizacionService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private MensajeSvc mensajeService;
	
	@RequestMapping(value="/consultaXIdMensaje", method=RequestMethod.POST)
	public MensajeDTO consultaXIdMensaje(@RequestBody String llave) throws FlexException {
		try {
			return mensajeService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosMensaje", method=RequestMethod.POST)
	public int contarResultadosMensaje(@RequestBody MensajeFilterDTO dto) throws FlexException  {
		try {
			return mensajeService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaMensaje", method=RequestMethod.POST)
	public MensajeDTO consultaUnicaMensaje(@RequestBody MensajeFilterDTO dto) throws FlexException  {
		try {
			return mensajeService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaMensaje", method=RequestMethod.POST)
	public List<MensajeDTO> listarConsultaMensaje(@RequestBody MensajeFilterDTO dto) throws FlexException  {
		try {
			return mensajeService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarMensaje", method=RequestMethod.POST)
	public MensajeDTO activarMensaje(@RequestBody MensajeDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return mensajeService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarMensaje", method=RequestMethod.POST)
	public MensajeDTO inactivarMensaje(@RequestBody MensajeDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return mensajeService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarMensaje", method=RequestMethod.POST)
	public MensajeDTO actualizarMensaje(@RequestBody MensajeDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return mensajeService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarMensaje", method=RequestMethod.POST)
	public MensajeDTO guardarMensaje(@RequestBody MensajeDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return mensajeService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@RequestMapping(value="/mensajesUsuarioMensaje", method=RequestMethod.POST)
	public List<MensajeDTO> mensajesUsuarioMensaje(@RequestBody MensajeFilterDTO dto)throws FlexException {
		try {
			return mensajeService.mensajesUsuario(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}

	@RequestMapping(value="/enviarMensajeMensaje", method=RequestMethod.POST)
	public MensajeDTO enviarMensajeMensaje(@RequestBody MensajeFilterDTO dto)throws FlexException {
		try {
			return mensajeService.enviarMensaje(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private PostCalificacionSvc postCalificacionService;
	
	@RequestMapping(value="/consultaXIdPostCalificacion", method=RequestMethod.POST)
	public PostCalificacionDTO consultaXIdPostCalificacion(@RequestBody String llave) throws FlexException {
		try {
			return postCalificacionService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosPostCalificacion", method=RequestMethod.POST)
	public int contarResultadosPostCalificacion(@RequestBody PostCalificacionFilterDTO dto) throws FlexException  {
		try {
			return postCalificacionService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaPostCalificacion", method=RequestMethod.POST)
	public PostCalificacionDTO consultaUnicaPostCalificacion(@RequestBody PostCalificacionFilterDTO dto) throws FlexException  {
		try {
			return postCalificacionService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaPostCalificacion", method=RequestMethod.POST)
	public List<PostCalificacionDTO> listarConsultaPostCalificacion(@RequestBody PostCalificacionFilterDTO dto) throws FlexException  {
		try {
			return postCalificacionService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarPostCalificacion", method=RequestMethod.POST)
	public PostCalificacionDTO activarPostCalificacion(@RequestBody PostCalificacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return postCalificacionService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarPostCalificacion", method=RequestMethod.POST)
	public PostCalificacionDTO inactivarPostCalificacion(@RequestBody PostCalificacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return postCalificacionService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarPostCalificacion", method=RequestMethod.POST)
	public PostCalificacionDTO actualizarPostCalificacion(@RequestBody PostCalificacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return postCalificacionService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarPostCalificacion", method=RequestMethod.POST)
	public PostCalificacionDTO guardarPostCalificacion(@RequestBody PostCalificacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return postCalificacionService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private WebServiceEjecucionSvc webServiceEjecucionService;
	
	@RequestMapping(value="/consultaXIdWebServiceEjecucion", method=RequestMethod.POST)
	public WebServiceEjecucionDTO consultaXIdWebServiceEjecucion(@RequestBody String llave) throws FlexException {
		try {
			return webServiceEjecucionService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosWebServiceEjecucion", method=RequestMethod.POST)
	public int contarResultadosWebServiceEjecucion(@RequestBody WebServiceEjecucionFilterDTO dto) throws FlexException  {
		try {
			return webServiceEjecucionService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaWebServiceEjecucion", method=RequestMethod.POST)
	public WebServiceEjecucionDTO consultaUnicaWebServiceEjecucion(@RequestBody WebServiceEjecucionFilterDTO dto) throws FlexException  {
		try {
			return webServiceEjecucionService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaWebServiceEjecucion", method=RequestMethod.POST)
	public List<WebServiceEjecucionDTO> listarConsultaWebServiceEjecucion(@RequestBody WebServiceEjecucionFilterDTO dto) throws FlexException  {
		try {
			return webServiceEjecucionService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarWebServiceEjecucion", method=RequestMethod.POST)
	public WebServiceEjecucionDTO activarWebServiceEjecucion(@RequestBody WebServiceEjecucionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return webServiceEjecucionService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarWebServiceEjecucion", method=RequestMethod.POST)
	public WebServiceEjecucionDTO inactivarWebServiceEjecucion(@RequestBody WebServiceEjecucionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return webServiceEjecucionService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarWebServiceEjecucion", method=RequestMethod.POST)
	public WebServiceEjecucionDTO actualizarWebServiceEjecucion(@RequestBody WebServiceEjecucionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return webServiceEjecucionService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarWebServiceEjecucion", method=RequestMethod.POST)
	public WebServiceEjecucionDTO guardarWebServiceEjecucion(@RequestBody WebServiceEjecucionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return webServiceEjecucionService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@RequestMapping(value="/ejecutarAPIWebServiceEjecucion", method=RequestMethod.POST)
	public WebServiceEjecucionDTO ejecutarAPIWebServiceEjecucion(@RequestBody WebServiceEjecucionFilterDTO dto)throws FlexException {
		try {
			return webServiceEjecucionService.ejecutarAPI(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private PostPreguntaSvc postPreguntaService;
	
	@RequestMapping(value="/consultaXIdPostPregunta", method=RequestMethod.POST)
	public PostPreguntaDTO consultaXIdPostPregunta(@RequestBody String llave) throws FlexException {
		try {
			return postPreguntaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosPostPregunta", method=RequestMethod.POST)
	public int contarResultadosPostPregunta(@RequestBody PostPreguntaFilterDTO dto) throws FlexException  {
		try {
			return postPreguntaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaPostPregunta", method=RequestMethod.POST)
	public PostPreguntaDTO consultaUnicaPostPregunta(@RequestBody PostPreguntaFilterDTO dto) throws FlexException  {
		try {
			return postPreguntaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaPostPregunta", method=RequestMethod.POST)
	public List<PostPreguntaDTO> listarConsultaPostPregunta(@RequestBody PostPreguntaFilterDTO dto) throws FlexException  {
		try {
			return postPreguntaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarPostPregunta", method=RequestMethod.POST)
	public PostPreguntaDTO activarPostPregunta(@RequestBody PostPreguntaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return postPreguntaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarPostPregunta", method=RequestMethod.POST)
	public PostPreguntaDTO inactivarPostPregunta(@RequestBody PostPreguntaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return postPreguntaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarPostPregunta", method=RequestMethod.POST)
	public PostPreguntaDTO actualizarPostPregunta(@RequestBody PostPreguntaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return postPreguntaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarPostPregunta", method=RequestMethod.POST)
	public PostPreguntaDTO guardarPostPregunta(@RequestBody PostPreguntaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return postPreguntaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@RequestMapping(value="/listarEnOrdenPostPregunta", method=RequestMethod.POST)
	public List<PostPreguntaDTO> listarEnOrdenPostPregunta(@RequestBody PostPreguntaFilterDTO dto)throws FlexException {
		try {
			return postPreguntaService.listarEnOrden(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}

	@RequestMapping(value="/listarPreguntasSinRespuestaPostPregunta", method=RequestMethod.POST)
	public List<PostPreguntaDTO> listarPreguntasSinRespuestaPostPregunta(@RequestBody PostPreguntaFilterDTO dto)throws FlexException {
		try {
			return postPreguntaService.listarPreguntasSinRespuesta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private ServidorSvc servidorService;
	
	@RequestMapping(value="/consultaXIdServidor", method=RequestMethod.POST)
	public ServidorDTO consultaXIdServidor(@RequestBody String llave) throws FlexException {
		try {
			return servidorService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosServidor", method=RequestMethod.POST)
	public int contarResultadosServidor(@RequestBody ServidorFilterDTO dto) throws FlexException  {
		try {
			return servidorService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaServidor", method=RequestMethod.POST)
	public ServidorDTO consultaUnicaServidor(@RequestBody ServidorFilterDTO dto) throws FlexException  {
		try {
			return servidorService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaServidor", method=RequestMethod.POST)
	public List<ServidorDTO> listarConsultaServidor(@RequestBody ServidorFilterDTO dto) throws FlexException  {
		try {
			return servidorService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarServidor", method=RequestMethod.POST)
	public ServidorDTO activarServidor(@RequestBody ServidorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return servidorService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarServidor", method=RequestMethod.POST)
	public ServidorDTO inactivarServidor(@RequestBody ServidorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return servidorService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarServidor", method=RequestMethod.POST)
	public ServidorDTO actualizarServidor(@RequestBody ServidorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return servidorService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarServidor", method=RequestMethod.POST)
	public ServidorDTO guardarServidor(@RequestBody ServidorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return servidorService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private MensajePlantillaCorreoSvc mensajePlantillaCorreoService;
	
	@RequestMapping(value="/consultaXIdMensajePlantillaCorreo", method=RequestMethod.POST)
	public MensajePlantillaCorreoDTO consultaXIdMensajePlantillaCorreo(@RequestBody String llave) throws FlexException {
		try {
			return mensajePlantillaCorreoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosMensajePlantillaCorreo", method=RequestMethod.POST)
	public int contarResultadosMensajePlantillaCorreo(@RequestBody MensajePlantillaCorreoFilterDTO dto) throws FlexException  {
		try {
			return mensajePlantillaCorreoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaMensajePlantillaCorreo", method=RequestMethod.POST)
	public MensajePlantillaCorreoDTO consultaUnicaMensajePlantillaCorreo(@RequestBody MensajePlantillaCorreoFilterDTO dto) throws FlexException  {
		try {
			return mensajePlantillaCorreoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaMensajePlantillaCorreo", method=RequestMethod.POST)
	public List<MensajePlantillaCorreoDTO> listarConsultaMensajePlantillaCorreo(@RequestBody MensajePlantillaCorreoFilterDTO dto) throws FlexException  {
		try {
			return mensajePlantillaCorreoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarMensajePlantillaCorreo", method=RequestMethod.POST)
	public MensajePlantillaCorreoDTO activarMensajePlantillaCorreo(@RequestBody MensajePlantillaCorreoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return mensajePlantillaCorreoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarMensajePlantillaCorreo", method=RequestMethod.POST)
	public MensajePlantillaCorreoDTO inactivarMensajePlantillaCorreo(@RequestBody MensajePlantillaCorreoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return mensajePlantillaCorreoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarMensajePlantillaCorreo", method=RequestMethod.POST)
	public MensajePlantillaCorreoDTO actualizarMensajePlantillaCorreo(@RequestBody MensajePlantillaCorreoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return mensajePlantillaCorreoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarMensajePlantillaCorreo", method=RequestMethod.POST)
	public MensajePlantillaCorreoDTO guardarMensajePlantillaCorreo(@RequestBody MensajePlantillaCorreoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return mensajePlantillaCorreoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private GPSDispositivoSvc gPSDispositivoService;
	
	@RequestMapping(value="/consultaXIdGPSDispositivo", method=RequestMethod.POST)
	public GPSDispositivoDTO consultaXIdGPSDispositivo(@RequestBody String llave) throws FlexException {
		try {
			return gPSDispositivoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosGPSDispositivo", method=RequestMethod.POST)
	public int contarResultadosGPSDispositivo(@RequestBody GPSDispositivoFilterDTO dto) throws FlexException  {
		try {
			return gPSDispositivoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaGPSDispositivo", method=RequestMethod.POST)
	public GPSDispositivoDTO consultaUnicaGPSDispositivo(@RequestBody GPSDispositivoFilterDTO dto) throws FlexException  {
		try {
			return gPSDispositivoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaGPSDispositivo", method=RequestMethod.POST)
	public List<GPSDispositivoDTO> listarConsultaGPSDispositivo(@RequestBody GPSDispositivoFilterDTO dto) throws FlexException  {
		try {
			return gPSDispositivoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarGPSDispositivo", method=RequestMethod.POST)
	public GPSDispositivoDTO activarGPSDispositivo(@RequestBody GPSDispositivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return gPSDispositivoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarGPSDispositivo", method=RequestMethod.POST)
	public GPSDispositivoDTO inactivarGPSDispositivo(@RequestBody GPSDispositivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return gPSDispositivoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarGPSDispositivo", method=RequestMethod.POST)
	public GPSDispositivoDTO actualizarGPSDispositivo(@RequestBody GPSDispositivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return gPSDispositivoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarGPSDispositivo", method=RequestMethod.POST)
	public GPSDispositivoDTO guardarGPSDispositivo(@RequestBody GPSDispositivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return gPSDispositivoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private TrazabilidadProductoInventarioSvc trazabilidadProductoInventarioService;
	
	@RequestMapping(value="/consultaXIdTrazabilidadProductoInventario", method=RequestMethod.POST)
	public TrazabilidadProductoInventarioDTO consultaXIdTrazabilidadProductoInventario(@RequestBody String llave) throws FlexException {
		try {
			return trazabilidadProductoInventarioService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosTrazabilidadProductoInventario", method=RequestMethod.POST)
	public int contarResultadosTrazabilidadProductoInventario(@RequestBody TrazabilidadProductoInventarioFilterDTO dto) throws FlexException  {
		try {
			return trazabilidadProductoInventarioService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaTrazabilidadProductoInventario", method=RequestMethod.POST)
	public TrazabilidadProductoInventarioDTO consultaUnicaTrazabilidadProductoInventario(@RequestBody TrazabilidadProductoInventarioFilterDTO dto) throws FlexException  {
		try {
			return trazabilidadProductoInventarioService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaTrazabilidadProductoInventario", method=RequestMethod.POST)
	public List<TrazabilidadProductoInventarioDTO> listarConsultaTrazabilidadProductoInventario(@RequestBody TrazabilidadProductoInventarioFilterDTO dto) throws FlexException  {
		try {
			return trazabilidadProductoInventarioService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarTrazabilidadProductoInventario", method=RequestMethod.POST)
	public TrazabilidadProductoInventarioDTO activarTrazabilidadProductoInventario(@RequestBody TrazabilidadProductoInventarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return trazabilidadProductoInventarioService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarTrazabilidadProductoInventario", method=RequestMethod.POST)
	public TrazabilidadProductoInventarioDTO inactivarTrazabilidadProductoInventario(@RequestBody TrazabilidadProductoInventarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return trazabilidadProductoInventarioService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarTrazabilidadProductoInventario", method=RequestMethod.POST)
	public TrazabilidadProductoInventarioDTO actualizarTrazabilidadProductoInventario(@RequestBody TrazabilidadProductoInventarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return trazabilidadProductoInventarioService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarTrazabilidadProductoInventario", method=RequestMethod.POST)
	public TrazabilidadProductoInventarioDTO guardarTrazabilidadProductoInventario(@RequestBody TrazabilidadProductoInventarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return trazabilidadProductoInventarioService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private DetallePedidoVentaSvc detallePedidoVentaService;
	
	@RequestMapping(value="/consultaXIdDetallePedidoVenta", method=RequestMethod.POST)
	public DetallePedidoVentaDTO consultaXIdDetallePedidoVenta(@RequestBody String llave) throws FlexException {
		try {
			return detallePedidoVentaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosDetallePedidoVenta", method=RequestMethod.POST)
	public int contarResultadosDetallePedidoVenta(@RequestBody DetallePedidoVentaFilterDTO dto) throws FlexException  {
		try {
			return detallePedidoVentaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaDetallePedidoVenta", method=RequestMethod.POST)
	public DetallePedidoVentaDTO consultaUnicaDetallePedidoVenta(@RequestBody DetallePedidoVentaFilterDTO dto) throws FlexException  {
		try {
			return detallePedidoVentaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaDetallePedidoVenta", method=RequestMethod.POST)
	public List<DetallePedidoVentaDTO> listarConsultaDetallePedidoVenta(@RequestBody DetallePedidoVentaFilterDTO dto) throws FlexException  {
		try {
			return detallePedidoVentaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarDetallePedidoVenta", method=RequestMethod.POST)
	public DetallePedidoVentaDTO activarDetallePedidoVenta(@RequestBody DetallePedidoVentaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return detallePedidoVentaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarDetallePedidoVenta", method=RequestMethod.POST)
	public DetallePedidoVentaDTO inactivarDetallePedidoVenta(@RequestBody DetallePedidoVentaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return detallePedidoVentaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarDetallePedidoVenta", method=RequestMethod.POST)
	public DetallePedidoVentaDTO actualizarDetallePedidoVenta(@RequestBody DetallePedidoVentaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return detallePedidoVentaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarDetallePedidoVenta", method=RequestMethod.POST)
	public DetallePedidoVentaDTO guardarDetallePedidoVenta(@RequestBody DetallePedidoVentaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return detallePedidoVentaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private CategoriaProductoSvc categoriaProductoService;
	
	@RequestMapping(value="/consultaXIdCategoriaProducto", method=RequestMethod.POST)
	public CategoriaProductoDTO consultaXIdCategoriaProducto(@RequestBody String llave) throws FlexException {
		try {
			return categoriaProductoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosCategoriaProducto", method=RequestMethod.POST)
	public int contarResultadosCategoriaProducto(@RequestBody CategoriaProductoFilterDTO dto) throws FlexException  {
		try {
			return categoriaProductoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaCategoriaProducto", method=RequestMethod.POST)
	public CategoriaProductoDTO consultaUnicaCategoriaProducto(@RequestBody CategoriaProductoFilterDTO dto) throws FlexException  {
		try {
			return categoriaProductoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaCategoriaProducto", method=RequestMethod.POST)
	public List<CategoriaProductoDTO> listarConsultaCategoriaProducto(@RequestBody CategoriaProductoFilterDTO dto) throws FlexException  {
		try {
			return categoriaProductoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarCategoriaProducto", method=RequestMethod.POST)
	public CategoriaProductoDTO activarCategoriaProducto(@RequestBody CategoriaProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return categoriaProductoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarCategoriaProducto", method=RequestMethod.POST)
	public CategoriaProductoDTO inactivarCategoriaProducto(@RequestBody CategoriaProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return categoriaProductoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarCategoriaProducto", method=RequestMethod.POST)
	public CategoriaProductoDTO actualizarCategoriaProducto(@RequestBody CategoriaProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return categoriaProductoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarCategoriaProducto", method=RequestMethod.POST)
	public CategoriaProductoDTO guardarCategoriaProducto(@RequestBody CategoriaProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return categoriaProductoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private ProductoInventarioSvc productoInventarioService;
	
	@RequestMapping(value="/consultaXIdProductoInventario", method=RequestMethod.POST)
	public ProductoInventarioDTO consultaXIdProductoInventario(@RequestBody String llave) throws FlexException {
		try {
			return productoInventarioService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosProductoInventario", method=RequestMethod.POST)
	public int contarResultadosProductoInventario(@RequestBody ProductoInventarioFilterDTO dto) throws FlexException  {
		try {
			return productoInventarioService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaProductoInventario", method=RequestMethod.POST)
	public ProductoInventarioDTO consultaUnicaProductoInventario(@RequestBody ProductoInventarioFilterDTO dto) throws FlexException  {
		try {
			return productoInventarioService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaProductoInventario", method=RequestMethod.POST)
	public List<ProductoInventarioDTO> listarConsultaProductoInventario(@RequestBody ProductoInventarioFilterDTO dto) throws FlexException  {
		try {
			return productoInventarioService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarProductoInventario", method=RequestMethod.POST)
	public ProductoInventarioDTO activarProductoInventario(@RequestBody ProductoInventarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoInventarioService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarProductoInventario", method=RequestMethod.POST)
	public ProductoInventarioDTO inactivarProductoInventario(@RequestBody ProductoInventarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoInventarioService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarProductoInventario", method=RequestMethod.POST)
	public ProductoInventarioDTO actualizarProductoInventario(@RequestBody ProductoInventarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoInventarioService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarProductoInventario", method=RequestMethod.POST)
	public ProductoInventarioDTO guardarProductoInventario(@RequestBody ProductoInventarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoInventarioService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private ProductoCaracteristicaSvc productoCaracteristicaService;
	
	@RequestMapping(value="/consultaXIdProductoCaracteristica", method=RequestMethod.POST)
	public ProductoCaracteristicaDTO consultaXIdProductoCaracteristica(@RequestBody String llave) throws FlexException {
		try {
			return productoCaracteristicaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosProductoCaracteristica", method=RequestMethod.POST)
	public int contarResultadosProductoCaracteristica(@RequestBody ProductoCaracteristicaFilterDTO dto) throws FlexException  {
		try {
			return productoCaracteristicaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaProductoCaracteristica", method=RequestMethod.POST)
	public ProductoCaracteristicaDTO consultaUnicaProductoCaracteristica(@RequestBody ProductoCaracteristicaFilterDTO dto) throws FlexException  {
		try {
			return productoCaracteristicaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaProductoCaracteristica", method=RequestMethod.POST)
	public List<ProductoCaracteristicaDTO> listarConsultaProductoCaracteristica(@RequestBody ProductoCaracteristicaFilterDTO dto) throws FlexException  {
		try {
			return productoCaracteristicaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarProductoCaracteristica", method=RequestMethod.POST)
	public ProductoCaracteristicaDTO activarProductoCaracteristica(@RequestBody ProductoCaracteristicaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoCaracteristicaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarProductoCaracteristica", method=RequestMethod.POST)
	public ProductoCaracteristicaDTO inactivarProductoCaracteristica(@RequestBody ProductoCaracteristicaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoCaracteristicaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarProductoCaracteristica", method=RequestMethod.POST)
	public ProductoCaracteristicaDTO actualizarProductoCaracteristica(@RequestBody ProductoCaracteristicaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoCaracteristicaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarProductoCaracteristica", method=RequestMethod.POST)
	public ProductoCaracteristicaDTO guardarProductoCaracteristica(@RequestBody ProductoCaracteristicaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoCaracteristicaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private UsuarioRolProductoSvc usuarioRolProductoService;
	
	@RequestMapping(value="/consultaXIdUsuarioRolProducto", method=RequestMethod.POST)
	public UsuarioRolProductoDTO consultaXIdUsuarioRolProducto(@RequestBody String llave) throws FlexException {
		try {
			return usuarioRolProductoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosUsuarioRolProducto", method=RequestMethod.POST)
	public int contarResultadosUsuarioRolProducto(@RequestBody UsuarioRolProductoFilterDTO dto) throws FlexException  {
		try {
			return usuarioRolProductoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaUsuarioRolProducto", method=RequestMethod.POST)
	public UsuarioRolProductoDTO consultaUnicaUsuarioRolProducto(@RequestBody UsuarioRolProductoFilterDTO dto) throws FlexException  {
		try {
			return usuarioRolProductoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaUsuarioRolProducto", method=RequestMethod.POST)
	public List<UsuarioRolProductoDTO> listarConsultaUsuarioRolProducto(@RequestBody UsuarioRolProductoFilterDTO dto) throws FlexException  {
		try {
			return usuarioRolProductoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarUsuarioRolProducto", method=RequestMethod.POST)
	public UsuarioRolProductoDTO activarUsuarioRolProducto(@RequestBody UsuarioRolProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioRolProductoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarUsuarioRolProducto", method=RequestMethod.POST)
	public UsuarioRolProductoDTO inactivarUsuarioRolProducto(@RequestBody UsuarioRolProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioRolProductoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarUsuarioRolProducto", method=RequestMethod.POST)
	public UsuarioRolProductoDTO actualizarUsuarioRolProducto(@RequestBody UsuarioRolProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioRolProductoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarUsuarioRolProducto", method=RequestMethod.POST)
	public UsuarioRolProductoDTO guardarUsuarioRolProducto(@RequestBody UsuarioRolProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioRolProductoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private DetalleCaracteristicaProductoSvc detalleCaracteristicaProductoService;
	
	@RequestMapping(value="/consultaXIdDetalleCaracteristicaProducto", method=RequestMethod.POST)
	public DetalleCaracteristicaProductoDTO consultaXIdDetalleCaracteristicaProducto(@RequestBody String llave) throws FlexException {
		try {
			return detalleCaracteristicaProductoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosDetalleCaracteristicaProducto", method=RequestMethod.POST)
	public int contarResultadosDetalleCaracteristicaProducto(@RequestBody DetalleCaracteristicaProductoFilterDTO dto) throws FlexException  {
		try {
			return detalleCaracteristicaProductoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaDetalleCaracteristicaProducto", method=RequestMethod.POST)
	public DetalleCaracteristicaProductoDTO consultaUnicaDetalleCaracteristicaProducto(@RequestBody DetalleCaracteristicaProductoFilterDTO dto) throws FlexException  {
		try {
			return detalleCaracteristicaProductoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaDetalleCaracteristicaProducto", method=RequestMethod.POST)
	public List<DetalleCaracteristicaProductoDTO> listarConsultaDetalleCaracteristicaProducto(@RequestBody DetalleCaracteristicaProductoFilterDTO dto) throws FlexException  {
		try {
			return detalleCaracteristicaProductoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarDetalleCaracteristicaProducto", method=RequestMethod.POST)
	public DetalleCaracteristicaProductoDTO activarDetalleCaracteristicaProducto(@RequestBody DetalleCaracteristicaProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return detalleCaracteristicaProductoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarDetalleCaracteristicaProducto", method=RequestMethod.POST)
	public DetalleCaracteristicaProductoDTO inactivarDetalleCaracteristicaProducto(@RequestBody DetalleCaracteristicaProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return detalleCaracteristicaProductoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarDetalleCaracteristicaProducto", method=RequestMethod.POST)
	public DetalleCaracteristicaProductoDTO actualizarDetalleCaracteristicaProducto(@RequestBody DetalleCaracteristicaProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return detalleCaracteristicaProductoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarDetalleCaracteristicaProducto", method=RequestMethod.POST)
	public DetalleCaracteristicaProductoDTO guardarDetalleCaracteristicaProducto(@RequestBody DetalleCaracteristicaProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return detalleCaracteristicaProductoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private BodegaSvc bodegaService;
	
	@RequestMapping(value="/consultaXIdBodega", method=RequestMethod.POST)
	public BodegaDTO consultaXIdBodega(@RequestBody String llave) throws FlexException {
		try {
			return bodegaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosBodega", method=RequestMethod.POST)
	public int contarResultadosBodega(@RequestBody BodegaFilterDTO dto) throws FlexException  {
		try {
			return bodegaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaBodega", method=RequestMethod.POST)
	public BodegaDTO consultaUnicaBodega(@RequestBody BodegaFilterDTO dto) throws FlexException  {
		try {
			return bodegaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaBodega", method=RequestMethod.POST)
	public List<BodegaDTO> listarConsultaBodega(@RequestBody BodegaFilterDTO dto) throws FlexException  {
		try {
			return bodegaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarBodega", method=RequestMethod.POST)
	public BodegaDTO activarBodega(@RequestBody BodegaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return bodegaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarBodega", method=RequestMethod.POST)
	public BodegaDTO inactivarBodega(@RequestBody BodegaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return bodegaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarBodega", method=RequestMethod.POST)
	public BodegaDTO actualizarBodega(@RequestBody BodegaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return bodegaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarBodega", method=RequestMethod.POST)
	public BodegaDTO guardarBodega(@RequestBody BodegaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return bodegaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private ProductoSvc productoService;
	
	@RequestMapping(value="/consultaXIdProducto", method=RequestMethod.POST)
	public ProductoDTO consultaXIdProducto(@RequestBody String llave) throws FlexException {
		try {
			return productoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosProducto", method=RequestMethod.POST)
	public int contarResultadosProducto(@RequestBody ProductoFilterDTO dto) throws FlexException  {
		try {
			return productoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaProducto", method=RequestMethod.POST)
	public ProductoDTO consultaUnicaProducto(@RequestBody ProductoFilterDTO dto) throws FlexException  {
		try {
			return productoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaProducto", method=RequestMethod.POST)
	public List<ProductoDTO> listarConsultaProducto(@RequestBody ProductoFilterDTO dto) throws FlexException  {
		try {
			return productoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarProducto", method=RequestMethod.POST)
	public ProductoDTO activarProducto(@RequestBody ProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarProducto", method=RequestMethod.POST)
	public ProductoDTO inactivarProducto(@RequestBody ProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarProducto", method=RequestMethod.POST)
	public ProductoDTO actualizarProducto(@RequestBody ProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarProducto", method=RequestMethod.POST)
	public ProductoDTO guardarProducto(@RequestBody ProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private ProductoInventarioDescuentoSvc productoInventarioDescuentoService;
	
	@RequestMapping(value="/consultaXIdProductoInventarioDescuento", method=RequestMethod.POST)
	public ProductoInventarioDescuentoDTO consultaXIdProductoInventarioDescuento(@RequestBody String llave) throws FlexException {
		try {
			return productoInventarioDescuentoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosProductoInventarioDescuento", method=RequestMethod.POST)
	public int contarResultadosProductoInventarioDescuento(@RequestBody ProductoInventarioDescuentoFilterDTO dto) throws FlexException  {
		try {
			return productoInventarioDescuentoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaProductoInventarioDescuento", method=RequestMethod.POST)
	public ProductoInventarioDescuentoDTO consultaUnicaProductoInventarioDescuento(@RequestBody ProductoInventarioDescuentoFilterDTO dto) throws FlexException  {
		try {
			return productoInventarioDescuentoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaProductoInventarioDescuento", method=RequestMethod.POST)
	public List<ProductoInventarioDescuentoDTO> listarConsultaProductoInventarioDescuento(@RequestBody ProductoInventarioDescuentoFilterDTO dto) throws FlexException  {
		try {
			return productoInventarioDescuentoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarProductoInventarioDescuento", method=RequestMethod.POST)
	public ProductoInventarioDescuentoDTO activarProductoInventarioDescuento(@RequestBody ProductoInventarioDescuentoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoInventarioDescuentoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarProductoInventarioDescuento", method=RequestMethod.POST)
	public ProductoInventarioDescuentoDTO inactivarProductoInventarioDescuento(@RequestBody ProductoInventarioDescuentoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoInventarioDescuentoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarProductoInventarioDescuento", method=RequestMethod.POST)
	public ProductoInventarioDescuentoDTO actualizarProductoInventarioDescuento(@RequestBody ProductoInventarioDescuentoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoInventarioDescuentoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarProductoInventarioDescuento", method=RequestMethod.POST)
	public ProductoInventarioDescuentoDTO guardarProductoInventarioDescuento(@RequestBody ProductoInventarioDescuentoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoInventarioDescuentoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private DeduccionProductoSvc deduccionProductoService;
	
	@RequestMapping(value="/consultaXIdDeduccionProducto", method=RequestMethod.POST)
	public DeduccionProductoDTO consultaXIdDeduccionProducto(@RequestBody String llave) throws FlexException {
		try {
			return deduccionProductoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosDeduccionProducto", method=RequestMethod.POST)
	public int contarResultadosDeduccionProducto(@RequestBody DeduccionProductoFilterDTO dto) throws FlexException  {
		try {
			return deduccionProductoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaDeduccionProducto", method=RequestMethod.POST)
	public DeduccionProductoDTO consultaUnicaDeduccionProducto(@RequestBody DeduccionProductoFilterDTO dto) throws FlexException  {
		try {
			return deduccionProductoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaDeduccionProducto", method=RequestMethod.POST)
	public List<DeduccionProductoDTO> listarConsultaDeduccionProducto(@RequestBody DeduccionProductoFilterDTO dto) throws FlexException  {
		try {
			return deduccionProductoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarDeduccionProducto", method=RequestMethod.POST)
	public DeduccionProductoDTO activarDeduccionProducto(@RequestBody DeduccionProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return deduccionProductoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarDeduccionProducto", method=RequestMethod.POST)
	public DeduccionProductoDTO inactivarDeduccionProducto(@RequestBody DeduccionProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return deduccionProductoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarDeduccionProducto", method=RequestMethod.POST)
	public DeduccionProductoDTO actualizarDeduccionProducto(@RequestBody DeduccionProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return deduccionProductoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarDeduccionProducto", method=RequestMethod.POST)
	public DeduccionProductoDTO guardarDeduccionProducto(@RequestBody DeduccionProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return deduccionProductoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private ModuloContratadoSvc moduloContratadoService;
	
	@RequestMapping(value="/consultaXIdModuloContratado", method=RequestMethod.POST)
	public ModuloContratadoDTO consultaXIdModuloContratado(@RequestBody String llave) throws FlexException {
		try {
			return moduloContratadoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosModuloContratado", method=RequestMethod.POST)
	public int contarResultadosModuloContratado(@RequestBody ModuloContratadoFilterDTO dto) throws FlexException  {
		try {
			return moduloContratadoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaModuloContratado", method=RequestMethod.POST)
	public ModuloContratadoDTO consultaUnicaModuloContratado(@RequestBody ModuloContratadoFilterDTO dto) throws FlexException  {
		try {
			return moduloContratadoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaModuloContratado", method=RequestMethod.POST)
	public List<ModuloContratadoDTO> listarConsultaModuloContratado(@RequestBody ModuloContratadoFilterDTO dto) throws FlexException  {
		try {
			return moduloContratadoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarModuloContratado", method=RequestMethod.POST)
	public ModuloContratadoDTO activarModuloContratado(@RequestBody ModuloContratadoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return moduloContratadoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarModuloContratado", method=RequestMethod.POST)
	public ModuloContratadoDTO inactivarModuloContratado(@RequestBody ModuloContratadoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return moduloContratadoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarModuloContratado", method=RequestMethod.POST)
	public ModuloContratadoDTO actualizarModuloContratado(@RequestBody ModuloContratadoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return moduloContratadoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarModuloContratado", method=RequestMethod.POST)
	public ModuloContratadoDTO guardarModuloContratado(@RequestBody ModuloContratadoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return moduloContratadoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@RequestMapping(value="/modulosUsuarioModuloContratado", method=RequestMethod.POST)
	public List<ModuloContratadoDTO> modulosUsuarioModuloContratado(@RequestBody ModuloContratadoFilterDTO dto)throws FlexException {
		try {
			return moduloContratadoService.modulosUsuario(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private ModuloSvc moduloService;
	
	@RequestMapping(value="/consultaXIdModulo", method=RequestMethod.POST)
	public ModuloDTO consultaXIdModulo(@RequestBody String llave) throws FlexException {
		try {
			return moduloService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosModulo", method=RequestMethod.POST)
	public int contarResultadosModulo(@RequestBody ModuloFilterDTO dto) throws FlexException  {
		try {
			return moduloService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaModulo", method=RequestMethod.POST)
	public ModuloDTO consultaUnicaModulo(@RequestBody ModuloFilterDTO dto) throws FlexException  {
		try {
			return moduloService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaModulo", method=RequestMethod.POST)
	public List<ModuloDTO> listarConsultaModulo(@RequestBody ModuloFilterDTO dto) throws FlexException  {
		try {
			return moduloService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarModulo", method=RequestMethod.POST)
	public ModuloDTO activarModulo(@RequestBody ModuloDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return moduloService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarModulo", method=RequestMethod.POST)
	public ModuloDTO inactivarModulo(@RequestBody ModuloDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return moduloService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarModulo", method=RequestMethod.POST)
	public ModuloDTO actualizarModulo(@RequestBody ModuloDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return moduloService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarModulo", method=RequestMethod.POST)
	public ModuloDTO guardarModulo(@RequestBody ModuloDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return moduloService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private ReporteBaseSvc reporteBaseService;
	
	@RequestMapping(value="/consultaXIdReporteBase", method=RequestMethod.POST)
	public ReporteBaseDTO consultaXIdReporteBase(@RequestBody String llave) throws FlexException {
		try {
			return reporteBaseService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosReporteBase", method=RequestMethod.POST)
	public int contarResultadosReporteBase(@RequestBody ReporteBaseFilterDTO dto) throws FlexException  {
		try {
			return reporteBaseService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaReporteBase", method=RequestMethod.POST)
	public ReporteBaseDTO consultaUnicaReporteBase(@RequestBody ReporteBaseFilterDTO dto) throws FlexException  {
		try {
			return reporteBaseService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaReporteBase", method=RequestMethod.POST)
	public List<ReporteBaseDTO> listarConsultaReporteBase(@RequestBody ReporteBaseFilterDTO dto) throws FlexException  {
		try {
			return reporteBaseService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarReporteBase", method=RequestMethod.POST)
	public ReporteBaseDTO activarReporteBase(@RequestBody ReporteBaseDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return reporteBaseService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarReporteBase", method=RequestMethod.POST)
	public ReporteBaseDTO inactivarReporteBase(@RequestBody ReporteBaseDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return reporteBaseService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarReporteBase", method=RequestMethod.POST)
	public ReporteBaseDTO actualizarReporteBase(@RequestBody ReporteBaseDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return reporteBaseService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarReporteBase", method=RequestMethod.POST)
	public ReporteBaseDTO guardarReporteBase(@RequestBody ReporteBaseDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return reporteBaseService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private UsuarioOrganizacionSvc usuarioOrganizacionService;
	
	@RequestMapping(value="/consultaXIdUsuarioOrganizacion", method=RequestMethod.POST)
	public UsuarioOrganizacionDTO consultaXIdUsuarioOrganizacion(@RequestBody String llave) throws FlexException {
		try {
			return usuarioOrganizacionService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosUsuarioOrganizacion", method=RequestMethod.POST)
	public int contarResultadosUsuarioOrganizacion(@RequestBody UsuarioOrganizacionFilterDTO dto) throws FlexException  {
		try {
			return usuarioOrganizacionService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaUsuarioOrganizacion", method=RequestMethod.POST)
	public UsuarioOrganizacionDTO consultaUnicaUsuarioOrganizacion(@RequestBody UsuarioOrganizacionFilterDTO dto) throws FlexException  {
		try {
			return usuarioOrganizacionService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaUsuarioOrganizacion", method=RequestMethod.POST)
	public List<UsuarioOrganizacionDTO> listarConsultaUsuarioOrganizacion(@RequestBody UsuarioOrganizacionFilterDTO dto) throws FlexException  {
		try {
			return usuarioOrganizacionService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarUsuarioOrganizacion", method=RequestMethod.POST)
	public UsuarioOrganizacionDTO activarUsuarioOrganizacion(@RequestBody UsuarioOrganizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioOrganizacionService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarUsuarioOrganizacion", method=RequestMethod.POST)
	public UsuarioOrganizacionDTO inactivarUsuarioOrganizacion(@RequestBody UsuarioOrganizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioOrganizacionService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarUsuarioOrganizacion", method=RequestMethod.POST)
	public UsuarioOrganizacionDTO actualizarUsuarioOrganizacion(@RequestBody UsuarioOrganizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioOrganizacionService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarUsuarioOrganizacion", method=RequestMethod.POST)
	public UsuarioOrganizacionDTO guardarUsuarioOrganizacion(@RequestBody UsuarioOrganizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioOrganizacionService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@RequestMapping(value="/sincronizarUsuariosUsuarioOrganizacion", method=RequestMethod.POST)
	public List<UsuarioOrganizacionDTO> sincronizarUsuariosUsuarioOrganizacion(@RequestBody UsuarioOrganizacionDTO dto, @RequestHeader("Authorization") String token)throws FlexException {
		try {
			return usuarioOrganizacionService.sincronizarUsuarios(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private PermisoSvc permisoService;
	
	@RequestMapping(value="/consultaXIdPermiso", method=RequestMethod.POST)
	public PermisoDTO consultaXIdPermiso(@RequestBody String llave) throws FlexException {
		try {
			return permisoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosPermiso", method=RequestMethod.POST)
	public int contarResultadosPermiso(@RequestBody PermisoFilterDTO dto) throws FlexException  {
		try {
			return permisoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaPermiso", method=RequestMethod.POST)
	public PermisoDTO consultaUnicaPermiso(@RequestBody PermisoFilterDTO dto) throws FlexException  {
		try {
			return permisoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaPermiso", method=RequestMethod.POST)
	public List<PermisoDTO> listarConsultaPermiso(@RequestBody PermisoFilterDTO dto) throws FlexException  {
		try {
			return permisoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarPermiso", method=RequestMethod.POST)
	public PermisoDTO activarPermiso(@RequestBody PermisoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return permisoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarPermiso", method=RequestMethod.POST)
	public PermisoDTO inactivarPermiso(@RequestBody PermisoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return permisoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarPermiso", method=RequestMethod.POST)
	public PermisoDTO actualizarPermiso(@RequestBody PermisoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return permisoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarPermiso", method=RequestMethod.POST)
	public PermisoDTO guardarPermiso(@RequestBody PermisoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return permisoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private UsuarioAutenticacionAutorizacionSvc usuarioAutenticacionAutorizacionService;
	
	@RequestMapping(value="/consultaXIdUsuarioAutenticacionAutorizacion", method=RequestMethod.POST)
	public UsuarioAutenticacionAutorizacionDTO consultaXIdUsuarioAutenticacionAutorizacion(@RequestBody String llave) throws FlexException {
		try {
			return usuarioAutenticacionAutorizacionService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosUsuarioAutenticacionAutorizacion", method=RequestMethod.POST)
	public int contarResultadosUsuarioAutenticacionAutorizacion(@RequestBody UsuarioAutenticacionAutorizacionFilterDTO dto) throws FlexException  {
		try {
			return usuarioAutenticacionAutorizacionService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaUsuarioAutenticacionAutorizacion", method=RequestMethod.POST)
	public UsuarioAutenticacionAutorizacionDTO consultaUnicaUsuarioAutenticacionAutorizacion(@RequestBody UsuarioAutenticacionAutorizacionFilterDTO dto) throws FlexException  {
		try {
			return usuarioAutenticacionAutorizacionService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaUsuarioAutenticacionAutorizacion", method=RequestMethod.POST)
	public List<UsuarioAutenticacionAutorizacionDTO> listarConsultaUsuarioAutenticacionAutorizacion(@RequestBody UsuarioAutenticacionAutorizacionFilterDTO dto) throws FlexException  {
		try {
			return usuarioAutenticacionAutorizacionService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarUsuarioAutenticacionAutorizacion", method=RequestMethod.POST)
	public UsuarioAutenticacionAutorizacionDTO activarUsuarioAutenticacionAutorizacion(@RequestBody UsuarioAutenticacionAutorizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioAutenticacionAutorizacionService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarUsuarioAutenticacionAutorizacion", method=RequestMethod.POST)
	public UsuarioAutenticacionAutorizacionDTO inactivarUsuarioAutenticacionAutorizacion(@RequestBody UsuarioAutenticacionAutorizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioAutenticacionAutorizacionService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarUsuarioAutenticacionAutorizacion", method=RequestMethod.POST)
	public UsuarioAutenticacionAutorizacionDTO actualizarUsuarioAutenticacionAutorizacion(@RequestBody UsuarioAutenticacionAutorizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioAutenticacionAutorizacionService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarUsuarioAutenticacionAutorizacion", method=RequestMethod.POST)
	public UsuarioAutenticacionAutorizacionDTO guardarUsuarioAutenticacionAutorizacion(@RequestBody UsuarioAutenticacionAutorizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioAutenticacionAutorizacionService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private UsuarioSesionSvc usuarioSesionService;
	
	@RequestMapping(value="/consultaXIdUsuarioSesion", method=RequestMethod.POST)
	public UsuarioSesionDTO consultaXIdUsuarioSesion(@RequestBody String llave) throws FlexException {
		try {
			return usuarioSesionService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosUsuarioSesion", method=RequestMethod.POST)
	public int contarResultadosUsuarioSesion(@RequestBody UsuarioSesionFilterDTO dto) throws FlexException  {
		try {
			return usuarioSesionService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaUsuarioSesion", method=RequestMethod.POST)
	public UsuarioSesionDTO consultaUnicaUsuarioSesion(@RequestBody UsuarioSesionFilterDTO dto) throws FlexException  {
		try {
			return usuarioSesionService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaUsuarioSesion", method=RequestMethod.POST)
	public List<UsuarioSesionDTO> listarConsultaUsuarioSesion(@RequestBody UsuarioSesionFilterDTO dto) throws FlexException  {
		try {
			return usuarioSesionService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarUsuarioSesion", method=RequestMethod.POST)
	public UsuarioSesionDTO activarUsuarioSesion(@RequestBody UsuarioSesionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioSesionService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarUsuarioSesion", method=RequestMethod.POST)
	public UsuarioSesionDTO inactivarUsuarioSesion(@RequestBody UsuarioSesionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioSesionService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarUsuarioSesion", method=RequestMethod.POST)
	public UsuarioSesionDTO actualizarUsuarioSesion(@RequestBody UsuarioSesionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioSesionService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarUsuarioSesion", method=RequestMethod.POST)
	public UsuarioSesionDTO guardarUsuarioSesion(@RequestBody UsuarioSesionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioSesionService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private CargaArchivoSvc cargaArchivoService;
	
	@RequestMapping(value="/consultaXIdCargaArchivo", method=RequestMethod.POST)
	public CargaArchivoDTO consultaXIdCargaArchivo(@RequestBody String llave) throws FlexException {
		try {
			return cargaArchivoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosCargaArchivo", method=RequestMethod.POST)
	public int contarResultadosCargaArchivo(@RequestBody CargaArchivoFilterDTO dto) throws FlexException  {
		try {
			return cargaArchivoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaCargaArchivo", method=RequestMethod.POST)
	public CargaArchivoDTO consultaUnicaCargaArchivo(@RequestBody CargaArchivoFilterDTO dto) throws FlexException  {
		try {
			return cargaArchivoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaCargaArchivo", method=RequestMethod.POST)
	public List<CargaArchivoDTO> listarConsultaCargaArchivo(@RequestBody CargaArchivoFilterDTO dto) throws FlexException  {
		try {
			return cargaArchivoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarCargaArchivo", method=RequestMethod.POST)
	public CargaArchivoDTO activarCargaArchivo(@RequestBody CargaArchivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cargaArchivoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarCargaArchivo", method=RequestMethod.POST)
	public CargaArchivoDTO inactivarCargaArchivo(@RequestBody CargaArchivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cargaArchivoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarCargaArchivo", method=RequestMethod.POST)
	public CargaArchivoDTO actualizarCargaArchivo(@RequestBody CargaArchivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cargaArchivoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarCargaArchivo", method=RequestMethod.POST)
	public CargaArchivoDTO guardarCargaArchivo(@RequestBody CargaArchivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cargaArchivoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private ReporteEjecucionSvc reporteEjecucionService;
	
	@RequestMapping(value="/consultaXIdReporteEjecucion", method=RequestMethod.POST)
	public ReporteEjecucionDTO consultaXIdReporteEjecucion(@RequestBody String llave) throws FlexException {
		try {
			return reporteEjecucionService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosReporteEjecucion", method=RequestMethod.POST)
	public int contarResultadosReporteEjecucion(@RequestBody ReporteEjecucionFilterDTO dto) throws FlexException  {
		try {
			return reporteEjecucionService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaReporteEjecucion", method=RequestMethod.POST)
	public ReporteEjecucionDTO consultaUnicaReporteEjecucion(@RequestBody ReporteEjecucionFilterDTO dto) throws FlexException  {
		try {
			return reporteEjecucionService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaReporteEjecucion", method=RequestMethod.POST)
	public List<ReporteEjecucionDTO> listarConsultaReporteEjecucion(@RequestBody ReporteEjecucionFilterDTO dto) throws FlexException  {
		try {
			return reporteEjecucionService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarReporteEjecucion", method=RequestMethod.POST)
	public ReporteEjecucionDTO activarReporteEjecucion(@RequestBody ReporteEjecucionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return reporteEjecucionService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarReporteEjecucion", method=RequestMethod.POST)
	public ReporteEjecucionDTO inactivarReporteEjecucion(@RequestBody ReporteEjecucionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return reporteEjecucionService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarReporteEjecucion", method=RequestMethod.POST)
	public ReporteEjecucionDTO actualizarReporteEjecucion(@RequestBody ReporteEjecucionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return reporteEjecucionService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarReporteEjecucion", method=RequestMethod.POST)
	public ReporteEjecucionDTO guardarReporteEjecucion(@RequestBody ReporteEjecucionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return reporteEjecucionService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private ConsecutivoSvc consecutivoService;
	
	@RequestMapping(value="/consultaXIdConsecutivo", method=RequestMethod.POST)
	public ConsecutivoDTO consultaXIdConsecutivo(@RequestBody String llave) throws FlexException {
		try {
			return consecutivoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosConsecutivo", method=RequestMethod.POST)
	public int contarResultadosConsecutivo(@RequestBody ConsecutivoFilterDTO dto) throws FlexException  {
		try {
			return consecutivoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaConsecutivo", method=RequestMethod.POST)
	public ConsecutivoDTO consultaUnicaConsecutivo(@RequestBody ConsecutivoFilterDTO dto) throws FlexException  {
		try {
			return consecutivoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaConsecutivo", method=RequestMethod.POST)
	public List<ConsecutivoDTO> listarConsultaConsecutivo(@RequestBody ConsecutivoFilterDTO dto) throws FlexException  {
		try {
			return consecutivoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarConsecutivo", method=RequestMethod.POST)
	public ConsecutivoDTO activarConsecutivo(@RequestBody ConsecutivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return consecutivoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarConsecutivo", method=RequestMethod.POST)
	public ConsecutivoDTO inactivarConsecutivo(@RequestBody ConsecutivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return consecutivoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarConsecutivo", method=RequestMethod.POST)
	public ConsecutivoDTO actualizarConsecutivo(@RequestBody ConsecutivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return consecutivoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarConsecutivo", method=RequestMethod.POST)
	public ConsecutivoDTO guardarConsecutivo(@RequestBody ConsecutivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return consecutivoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@RequestMapping(value="/asignarConsecutivoConsecutivo", method=RequestMethod.POST)
	public ConsecutivoDTO asignarConsecutivoConsecutivo(@RequestBody ConsecutivoDTO dto, @RequestHeader("Authorization") String token)throws FlexException {
		try {
			return consecutivoService.asignarConsecutivo(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private TransaccionLogSvc transaccionLogService;
	
	@RequestMapping(value="/consultaXIdTransaccionLog", method=RequestMethod.POST)
	public TransaccionLogDTO consultaXIdTransaccionLog(@RequestBody String llave) throws FlexException {
		try {
			return transaccionLogService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosTransaccionLog", method=RequestMethod.POST)
	public int contarResultadosTransaccionLog(@RequestBody TransaccionLogFilterDTO dto) throws FlexException  {
		try {
			return transaccionLogService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaTransaccionLog", method=RequestMethod.POST)
	public TransaccionLogDTO consultaUnicaTransaccionLog(@RequestBody TransaccionLogFilterDTO dto) throws FlexException  {
		try {
			return transaccionLogService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaTransaccionLog", method=RequestMethod.POST)
	public List<TransaccionLogDTO> listarConsultaTransaccionLog(@RequestBody TransaccionLogFilterDTO dto) throws FlexException  {
		try {
			return transaccionLogService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarTransaccionLog", method=RequestMethod.POST)
	public TransaccionLogDTO activarTransaccionLog(@RequestBody TransaccionLogDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return transaccionLogService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarTransaccionLog", method=RequestMethod.POST)
	public TransaccionLogDTO inactivarTransaccionLog(@RequestBody TransaccionLogDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return transaccionLogService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarTransaccionLog", method=RequestMethod.POST)
	public TransaccionLogDTO actualizarTransaccionLog(@RequestBody TransaccionLogDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return transaccionLogService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarTransaccionLog", method=RequestMethod.POST)
	public TransaccionLogDTO guardarTransaccionLog(@RequestBody TransaccionLogDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return transaccionLogService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private UsuarioSesionErrorSvc usuarioSesionErrorService;
	
	@RequestMapping(value="/consultaXIdUsuarioSesionError", method=RequestMethod.POST)
	public UsuarioSesionErrorDTO consultaXIdUsuarioSesionError(@RequestBody String llave) throws FlexException {
		try {
			return usuarioSesionErrorService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosUsuarioSesionError", method=RequestMethod.POST)
	public int contarResultadosUsuarioSesionError(@RequestBody UsuarioSesionErrorFilterDTO dto) throws FlexException  {
		try {
			return usuarioSesionErrorService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaUsuarioSesionError", method=RequestMethod.POST)
	public UsuarioSesionErrorDTO consultaUnicaUsuarioSesionError(@RequestBody UsuarioSesionErrorFilterDTO dto) throws FlexException  {
		try {
			return usuarioSesionErrorService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaUsuarioSesionError", method=RequestMethod.POST)
	public List<UsuarioSesionErrorDTO> listarConsultaUsuarioSesionError(@RequestBody UsuarioSesionErrorFilterDTO dto) throws FlexException  {
		try {
			return usuarioSesionErrorService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarUsuarioSesionError", method=RequestMethod.POST)
	public UsuarioSesionErrorDTO activarUsuarioSesionError(@RequestBody UsuarioSesionErrorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioSesionErrorService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarUsuarioSesionError", method=RequestMethod.POST)
	public UsuarioSesionErrorDTO inactivarUsuarioSesionError(@RequestBody UsuarioSesionErrorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioSesionErrorService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarUsuarioSesionError", method=RequestMethod.POST)
	public UsuarioSesionErrorDTO actualizarUsuarioSesionError(@RequestBody UsuarioSesionErrorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioSesionErrorService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarUsuarioSesionError", method=RequestMethod.POST)
	public UsuarioSesionErrorDTO guardarUsuarioSesionError(@RequestBody UsuarioSesionErrorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioSesionErrorService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private UsuarioAutenticacionSvc usuarioAutenticacionService;
	
	@RequestMapping(value="/consultaXIdUsuarioAutenticacion", method=RequestMethod.POST)
	public UsuarioAutenticacionDTO consultaXIdUsuarioAutenticacion(@RequestBody String llave) throws FlexException {
		try {
			return usuarioAutenticacionService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosUsuarioAutenticacion", method=RequestMethod.POST)
	public int contarResultadosUsuarioAutenticacion(@RequestBody UsuarioAutenticacionFilterDTO dto) throws FlexException  {
		try {
			return usuarioAutenticacionService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaUsuarioAutenticacion", method=RequestMethod.POST)
	public UsuarioAutenticacionDTO consultaUnicaUsuarioAutenticacion(@RequestBody UsuarioAutenticacionFilterDTO dto) throws FlexException  {
		try {
			return usuarioAutenticacionService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaUsuarioAutenticacion", method=RequestMethod.POST)
	public List<UsuarioAutenticacionDTO> listarConsultaUsuarioAutenticacion(@RequestBody UsuarioAutenticacionFilterDTO dto) throws FlexException  {
		try {
			return usuarioAutenticacionService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarUsuarioAutenticacion", method=RequestMethod.POST)
	public UsuarioAutenticacionDTO activarUsuarioAutenticacion(@RequestBody UsuarioAutenticacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioAutenticacionService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarUsuarioAutenticacion", method=RequestMethod.POST)
	public UsuarioAutenticacionDTO inactivarUsuarioAutenticacion(@RequestBody UsuarioAutenticacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioAutenticacionService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarUsuarioAutenticacion", method=RequestMethod.POST)
	public UsuarioAutenticacionDTO actualizarUsuarioAutenticacion(@RequestBody UsuarioAutenticacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioAutenticacionService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarUsuarioAutenticacion", method=RequestMethod.POST)
	public UsuarioAutenticacionDTO guardarUsuarioAutenticacion(@RequestBody UsuarioAutenticacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioAutenticacionService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@RequestMapping(value="/autenticarUsuarioAutenticacion", method=RequestMethod.POST)
	public UsuarioAutenticacionDTO autenticarUsuarioAutenticacion(@RequestBody UsuarioAutenticacionFilterDTO dto)throws FlexException {
		try {
			return usuarioAutenticacionService.autenticar(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}

	@RequestMapping(value="/cambiarClaveUsuarioAutenticacion", method=RequestMethod.POST)
	public UsuarioAutenticacionDTO cambiarClaveUsuarioAutenticacion(@RequestBody UsuarioAutenticacionDTO dto, @RequestHeader("Authorization") String token)throws FlexException {
		try {
			return usuarioAutenticacionService.cambiarClave(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private AuditoriaSvc auditoriaService;
	
	@RequestMapping(value="/consultaXIdAuditoria", method=RequestMethod.POST)
	public AuditoriaDTO consultaXIdAuditoria(@RequestBody String llave) throws FlexException {
		try {
			return auditoriaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosAuditoria", method=RequestMethod.POST)
	public int contarResultadosAuditoria(@RequestBody AuditoriaFilterDTO dto) throws FlexException  {
		try {
			return auditoriaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaAuditoria", method=RequestMethod.POST)
	public AuditoriaDTO consultaUnicaAuditoria(@RequestBody AuditoriaFilterDTO dto) throws FlexException  {
		try {
			return auditoriaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaAuditoria", method=RequestMethod.POST)
	public List<AuditoriaDTO> listarConsultaAuditoria(@RequestBody AuditoriaFilterDTO dto) throws FlexException  {
		try {
			return auditoriaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarAuditoria", method=RequestMethod.POST)
	public AuditoriaDTO activarAuditoria(@RequestBody AuditoriaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return auditoriaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarAuditoria", method=RequestMethod.POST)
	public AuditoriaDTO inactivarAuditoria(@RequestBody AuditoriaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return auditoriaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarAuditoria", method=RequestMethod.POST)
	public AuditoriaDTO actualizarAuditoria(@RequestBody AuditoriaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return auditoriaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarAuditoria", method=RequestMethod.POST)
	public AuditoriaDTO guardarAuditoria(@RequestBody AuditoriaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return auditoriaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private TransaccionErrorSvc transaccionErrorService;
	
	@RequestMapping(value="/consultaXIdTransaccionError", method=RequestMethod.POST)
	public TransaccionErrorDTO consultaXIdTransaccionError(@RequestBody String llave) throws FlexException {
		try {
			return transaccionErrorService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosTransaccionError", method=RequestMethod.POST)
	public int contarResultadosTransaccionError(@RequestBody TransaccionErrorFilterDTO dto) throws FlexException  {
		try {
			return transaccionErrorService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaTransaccionError", method=RequestMethod.POST)
	public TransaccionErrorDTO consultaUnicaTransaccionError(@RequestBody TransaccionErrorFilterDTO dto) throws FlexException  {
		try {
			return transaccionErrorService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaTransaccionError", method=RequestMethod.POST)
	public List<TransaccionErrorDTO> listarConsultaTransaccionError(@RequestBody TransaccionErrorFilterDTO dto) throws FlexException  {
		try {
			return transaccionErrorService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarTransaccionError", method=RequestMethod.POST)
	public TransaccionErrorDTO activarTransaccionError(@RequestBody TransaccionErrorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return transaccionErrorService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarTransaccionError", method=RequestMethod.POST)
	public TransaccionErrorDTO inactivarTransaccionError(@RequestBody TransaccionErrorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return transaccionErrorService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarTransaccionError", method=RequestMethod.POST)
	public TransaccionErrorDTO actualizarTransaccionError(@RequestBody TransaccionErrorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return transaccionErrorService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarTransaccionError", method=RequestMethod.POST)
	public TransaccionErrorDTO guardarTransaccionError(@RequestBody TransaccionErrorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return transaccionErrorService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private OrganizacionSvc organizacionService;
	
	@RequestMapping(value="/consultaXIdOrganizacion", method=RequestMethod.POST)
	public OrganizacionDTO consultaXIdOrganizacion(@RequestBody String llave) throws FlexException {
		try {
			return organizacionService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/contarResultadosOrganizacion", method=RequestMethod.POST)
	public int contarResultadosOrganizacion(@RequestBody OrganizacionFilterDTO dto) throws FlexException  {
		try {
			return organizacionService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/consultaUnicaOrganizacion", method=RequestMethod.POST)
	public OrganizacionDTO consultaUnicaOrganizacion(@RequestBody OrganizacionFilterDTO dto) throws FlexException  {
		try {
			return organizacionService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/listarConsultaOrganizacion", method=RequestMethod.POST)
	public List<OrganizacionDTO> listarConsultaOrganizacion(@RequestBody OrganizacionFilterDTO dto) throws FlexException  {
		try {
			return organizacionService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/activarOrganizacion", method=RequestMethod.POST)
	public OrganizacionDTO activarOrganizacion(@RequestBody OrganizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return organizacionService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/inactivarOrganizacion", method=RequestMethod.POST)
	public OrganizacionDTO inactivarOrganizacion(@RequestBody OrganizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return organizacionService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/actualizarOrganizacion", method=RequestMethod.POST)
	public OrganizacionDTO actualizarOrganizacion(@RequestBody OrganizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return organizacionService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@RequestMapping(value="/guardarOrganizacion", method=RequestMethod.POST)
	public OrganizacionDTO guardarOrganizacion(@RequestBody OrganizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return organizacionService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@RequestMapping(value="/obtenerPrincipalOrganizacion", method=RequestMethod.POST)
	public OrganizacionDTO obtenerPrincipalOrganizacion(@RequestBody OrganizacionFilterDTO dto)throws FlexException {
		try {
			return organizacionService.obtenerPrincipal(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
}