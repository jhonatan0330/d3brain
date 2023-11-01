package com.softure.logisticpymes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.softure.authentication.application.OrganizacionSvc;
import com.softure.authentication.application.UsuarioAutenticacionAutorizacionSvc;
import com.softure.authentication.application.UsuarioAutenticacionSvc;
import com.softure.authentication.application.UsuarioOrganizacionSvc;
import com.softure.authentication.application.UsuarioSesionErrorSvc;
import com.softure.authentication.application.UsuarioSesionSvc;
import com.softure.authentication.domain.OrganizacionDTO;
import com.softure.authentication.domain.OrganizacionFilterDTO;
import com.softure.authentication.domain.UsuarioAutenticacionAutorizacionDTO;
import com.softure.authentication.domain.UsuarioAutenticacionAutorizacionFilterDTO;
import com.softure.authentication.domain.UsuarioAutenticacionDTO;
import com.softure.authentication.domain.UsuarioAutenticacionFilterDTO;
import com.softure.authentication.domain.UsuarioOrganizacionDTO;
import com.softure.authentication.domain.UsuarioOrganizacionFilterDTO;
import com.softure.authentication.domain.UsuarioSesionDTO;
import com.softure.authentication.domain.UsuarioSesionErrorDTO;
import com.softure.authentication.domain.UsuarioSesionErrorFilterDTO;
import com.softure.authentication.domain.UsuarioSesionFilterDTO;
import com.softure.authorization.application.ModuloSvc;
import com.softure.authorization.application.PermisoSvc;
import com.softure.authorization.application.RolAccesoSvc;
import com.softure.authorization.application.UsuarioRolProductoSvc;
import com.softure.authorization.application.UsuarioRolSvc;
import com.softure.authorization.domain.ModuloDTO;
import com.softure.authorization.domain.ModuloFilterDTO;
import com.softure.authorization.domain.PermisoDTO;
import com.softure.authorization.domain.PermisoFilterDTO;
import com.softure.authorization.domain.RolAccesoDTO;
import com.softure.authorization.domain.RolAccesoFilterDTO;
import com.softure.authorization.domain.UsuarioRolDTO;
import com.softure.authorization.domain.UsuarioRolFilterDTO;
import com.softure.authorization.domain.UsuarioRolProductoDTO;
import com.softure.authorization.domain.UsuarioRolProductoFilterDTO;
import com.softure.document_execution.application.CallDocumentCRUD;
import com.softure.document_execution.application.CallDocumentListWithFilters;
import com.softure.document_execution.application.DetallePedidoVentaSvc;
import com.softure.document_execution.application.DocumentoRelacionExpedienteSvc;
import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.application.PedidoVentaDineroSvc;
import com.softure.document_execution.application.PedidoVentaSvc;
import com.softure.document_execution.application.PedidoVentaTiempoSvc;
import com.softure.document_execution.domain.DetallePedidoVentaDTO;
import com.softure.document_execution.domain.DetallePedidoVentaFilterDTO;
import com.softure.document_execution.domain.DocumentoRelacionExpedienteDTO;
import com.softure.document_execution.domain.DocumentoRelacionExpedienteFilterDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaFilterDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaDineroDTO;
import com.softure.document_execution.domain.PedidoVentaDineroFilterDTO;
import com.softure.document_execution.domain.PedidoVentaFilterDTO;
import com.softure.document_execution.domain.PedidoVentaTiempoDTO;
import com.softure.document_execution.domain.PedidoVentaTiempoFilterDTO;
import com.softure.document_transaction.application.DocumentoTransaccionSvc;
import com.softure.document_transaction.application.TransaccionErrorSvc;
import com.softure.document_transaction.application.TransaccionLogSvc;
import com.softure.document_transaction.domain.DocumentoTransaccionDTO;
import com.softure.document_transaction.domain.DocumentoTransaccionFilterDTO;
import com.softure.document_transaction.domain.TransaccionErrorDTO;
import com.softure.document_transaction.domain.TransaccionErrorFilterDTO;
import com.softure.document_transaction.domain.TransaccionLogDTO;
import com.softure.document_transaction.domain.TransaccionLogFilterDTO;
import com.softure.document_transition.application.DocumentoRelacionGestorSvc;
import com.softure.document_transition.application.PedidoVentaAjusteSvc;
import com.softure.document_transition.domain.DocumentoRelacionGestorDTO;
import com.softure.document_transition.domain.DocumentoRelacionGestorFilterDTO;
import com.softure.document_transition.domain.PedidoVentaAjusteDTO;
import com.softure.document_transition.domain.PedidoVentaAjusteFilterDTO;
import com.softure.gps.application.GPSDispositivoSvc;
import com.softure.gps.application.GPSLocalizacionSvc;
import com.softure.gps.domain.GPSDispositivoDTO;
import com.softure.gps.domain.GPSDispositivoFilterDTO;
import com.softure.gps.domain.GPSLocalizacionDTO;
import com.softure.gps.domain.GPSLocalizacionFilterDTO;
import com.softure.inventory.application.BodegaSvc;
import com.softure.inventory.application.CategoriaProductoSvc;
import com.softure.inventory.application.DeduccionProductoSvc;
import com.softure.inventory.application.DetalleCaracteristicaProductoSvc;
import com.softure.inventory.application.ProductoCaracteristicaSvc;
import com.softure.inventory.application.ProductoInventarioDescuentoSvc;
import com.softure.inventory.application.ProductoInventarioSvc;
import com.softure.inventory.application.ProductoSvc;
import com.softure.inventory.application.TrazabilidadProductoInventarioSvc;
import com.softure.inventory.domain.BodegaDTO;
import com.softure.inventory.domain.BodegaFilterDTO;
import com.softure.inventory.domain.CategoriaProductoDTO;
import com.softure.inventory.domain.CategoriaProductoFilterDTO;
import com.softure.inventory.domain.DeduccionProductoDTO;
import com.softure.inventory.domain.DeduccionProductoFilterDTO;
import com.softure.inventory.domain.DetalleCaracteristicaProductoDTO;
import com.softure.inventory.domain.DetalleCaracteristicaProductoFilterDTO;
import com.softure.inventory.domain.ProductoCaracteristicaDTO;
import com.softure.inventory.domain.ProductoCaracteristicaFilterDTO;
import com.softure.inventory.domain.ProductoDTO;
import com.softure.inventory.domain.ProductoFilterDTO;
import com.softure.inventory.domain.ProductoInventarioDTO;
import com.softure.inventory.domain.ProductoInventarioDescuentoDTO;
import com.softure.inventory.domain.ProductoInventarioDescuentoFilterDTO;
import com.softure.inventory.domain.ProductoInventarioFilterDTO;
import com.softure.inventory.domain.TrazabilidadProductoInventarioDTO;
import com.softure.inventory.domain.TrazabilidadProductoInventarioFilterDTO;
import com.softure.java.dto.exception.FlexException;
import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.application.CambioSvc;
import com.softure.logisticpymes.application.PuestoSvc;
import com.softure.logisticpymes.application.ServidorSvc;
import com.softure.logisticpymes.application.UsuarioSvc;
import com.softure.logisticpymes.domain.CambioDTO;
import com.softure.logisticpymes.domain.CambioFilterDTO;
import com.softure.logisticpymes.domain.PuestoDTO;
import com.softure.logisticpymes.domain.PuestoFilterDTO;
import com.softure.logisticpymes.domain.ServidorDTO;
import com.softure.logisticpymes.domain.ServidorFilterDTO;
import com.softure.logisticpymes.domain.UsuarioDTO;
import com.softure.logisticpymes.domain.UsuarioFilterDTO;
import com.softure.mail.application.MailUserSendMessage;
import com.softure.mail.application.MensajePlantillaCorreoSvc;
import com.softure.mail.application.MensajeSvc;
import com.softure.mail.domain.MensajeDTO;
import com.softure.mail.domain.MensajeFilterDTO;
import com.softure.mail.domain.MensajePlantillaCorreoDTO;
import com.softure.mail.domain.MensajePlantillaCorreoFilterDTO;
import com.softure.money.application.CuentaSvc;
import com.softure.money.application.MovimientoSvc;
import com.softure.money.application.TurnoSvc;
import com.softure.money.domain.CuentaDTO;
import com.softure.money.domain.CuentaFilterDTO;
import com.softure.money.domain.MovimientoDTO;
import com.softure.money.domain.MovimientoFilterDTO;
import com.softure.money.domain.TurnoDTO;
import com.softure.money.domain.TurnoFilterDTO;
import com.softure.notification.application.ActividadSvc;
import com.softure.notification.domain.ActividadDTO;
import com.softure.notification.domain.ActividadFilterDTO;
import com.softure.process_designer.application.ProcesoEstadoSvc;
import com.softure.process_designer.application.ProcesoSvc;
import com.softure.process_designer.application.ProcesoTransicionAutomaticaSvc;
import com.softure.process_designer.application.ProcesoTransicionSvc;
import com.softure.process_designer.domain.ProcesoDTO;
import com.softure.process_designer.domain.ProcesoEstadoDTO;
import com.softure.process_designer.domain.ProcesoEstadoFilterDTO;
import com.softure.process_designer.domain.ProcesoFilterDTO;
import com.softure.process_designer.domain.ProcesoTransicionAutomaticaDTO;
import com.softure.process_designer.domain.ProcesoTransicionAutomaticaFilterDTO;
import com.softure.process_designer.domain.ProcesoTransicionDTO;
import com.softure.process_designer.domain.ProcesoTransicionFilterDTO;
import com.softure.process_form.application.ConsecutivoSvc;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.application.PlantillaConsecutivoSvc;
import com.softure.process_form.domain.ConsecutivoDTO;
import com.softure.process_form.domain.ConsecutivoFilterDTO;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaFilterDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.process_form.domain.DocumentoPlantillaFilterDTO;
import com.softure.process_form.domain.PlantillaConsecutivoDTO;
import com.softure.process_form.domain.PlantillaConsecutivoFilterDTO;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.application.PropiedadValorDefinidoSvc;
import com.softure.property.application.RelacionInternaSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadFilterDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.property.domain.PropiedadValorDefinidoFilterDTO;
import com.softure.property.domain.RelacionInternaDTO;
import com.softure.property.domain.RelacionInternaFilterDTO;
import com.softure.report.application.ReporteBaseSvc;
import com.softure.report.application.ReporteEjecucionSvc;
import com.softure.report.domain.ReporteBaseDTO;
import com.softure.report.domain.ReporteBaseFilterDTO;
import com.softure.report.domain.ReporteEjecucionDTO;
import com.softure.report.domain.ReporteEjecucionFilterDTO;
import com.softure.survey.application.EncuestaGrupoSvc;
import com.softure.survey.application.EncuestaOpcionRespuestaSvc;
import com.softure.survey.application.EncuestaPreguntaSvc;
import com.softure.survey.application.EncuestaRespuestaSvc;
import com.softure.survey.application.EncuestaSvc;
import com.softure.survey.application.PostCalificacionSvc;
import com.softure.survey.application.PostPreguntaSvc;
import com.softure.survey.application.PostRespuestaSvc;
import com.softure.survey.domain.EncuestaDTO;
import com.softure.survey.domain.EncuestaFilterDTO;
import com.softure.survey.domain.EncuestaGrupoDTO;
import com.softure.survey.domain.EncuestaGrupoFilterDTO;
import com.softure.survey.domain.EncuestaOpcionRespuestaDTO;
import com.softure.survey.domain.EncuestaOpcionRespuestaFilterDTO;
import com.softure.survey.domain.EncuestaPreguntaDTO;
import com.softure.survey.domain.EncuestaPreguntaFilterDTO;
import com.softure.survey.domain.EncuestaRespuestaDTO;
import com.softure.survey.domain.EncuestaRespuestaFilterDTO;
import com.softure.survey.domain.PostCalificacionDTO;
import com.softure.survey.domain.PostCalificacionFilterDTO;
import com.softure.survey.domain.PostPreguntaDTO;
import com.softure.survey.domain.PostPreguntaFilterDTO;
import com.softure.survey.domain.PostRespuestaDTO;
import com.softure.survey.domain.PostRespuestaFilterDTO;
import com.softure.tariff.application.TarifaSvc;
import com.softure.tariff.application.TarifarioSvc;
import com.softure.tariff.domain.TarifaDTO;
import com.softure.tariff.domain.TarifaFilterDTO;
import com.softure.tariff.domain.TarifarioDTO;
import com.softure.tariff.domain.TarifarioFilterDTO;
import com.softure.upload.application.CargaArchivoSvc;
import com.softure.upload.domain.CargaArchivoDTO;
import com.softure.upload.domain.CargaArchivoFilterDTO;
import com.softure.webservice.application.WebServiceEjecucionSvc;
import com.softure.webservice.application.WebServiceSvc;
import com.softure.webservice.domain.WebServiceDTO;
import com.softure.webservice.domain.WebServiceEjecucionDTO;
import com.softure.webservice.domain.WebServiceEjecucionFilterDTO;
import com.softure.webservice.domain.WebServiceFilterDTO;

@RestController
@RequestMapping("/flex")
public class FullControllerDTO {
	
	
	@Autowired private PlantillaConsecutivoSvc plantillaConsecutivoService;
	@Autowired private CallDocumentCRUD crudService;
	
	@PostMapping(value="/consultaXIdPlantillaConsecutivo")
	public PlantillaConsecutivoDTO consultaXIdPlantillaConsecutivo(@RequestBody String llave) throws FlexException {
		try {
			return plantillaConsecutivoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosPlantillaConsecutivo")
	public int contarResultadosPlantillaConsecutivo(@RequestBody PlantillaConsecutivoFilterDTO dto) throws FlexException  {
		try {
			return plantillaConsecutivoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaPlantillaConsecutivo")
	public PlantillaConsecutivoDTO consultaUnicaPlantillaConsecutivo(@RequestBody PlantillaConsecutivoFilterDTO dto) throws FlexException  {
		try {
			return plantillaConsecutivoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaPlantillaConsecutivo")
	public List<PlantillaConsecutivoDTO> listarConsultaPlantillaConsecutivo(@RequestBody PlantillaConsecutivoFilterDTO dto) throws FlexException  {
		try {
			return plantillaConsecutivoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarPlantillaConsecutivo")
	public PlantillaConsecutivoDTO activarPlantillaConsecutivo(@RequestBody PlantillaConsecutivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return plantillaConsecutivoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarPlantillaConsecutivo")
	public PlantillaConsecutivoDTO inactivarPlantillaConsecutivo(@RequestBody PlantillaConsecutivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return plantillaConsecutivoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarPlantillaConsecutivo")
	public PlantillaConsecutivoDTO actualizarPlantillaConsecutivo(@RequestBody PlantillaConsecutivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return plantillaConsecutivoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarPlantillaConsecutivo")
	public PlantillaConsecutivoDTO guardarPlantillaConsecutivo(@RequestBody PlantillaConsecutivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return plantillaConsecutivoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private ProcesoSvc procesoService;
	
	@PostMapping(value="/consultaXIdProceso")
	public ProcesoDTO consultaXIdProceso(@RequestBody String llave) throws FlexException {
		try {
			return procesoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosProceso")
	public int contarResultadosProceso(@RequestBody ProcesoFilterDTO dto) throws FlexException  {
		try {
			return procesoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaProceso")
	public ProcesoDTO consultaUnicaProceso(@RequestBody ProcesoFilterDTO dto) throws FlexException  {
		try {
			return procesoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaProceso")
	public List<ProcesoDTO> listarConsultaProceso(@RequestBody ProcesoFilterDTO dto) throws FlexException  {
		try {
			return procesoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarProceso")
	public ProcesoDTO activarProceso(@RequestBody ProcesoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarProceso")
	public ProcesoDTO inactivarProceso(@RequestBody ProcesoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarProceso")
	public ProcesoDTO actualizarProceso(@RequestBody ProcesoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarProceso")
	public ProcesoDTO guardarProceso(@RequestBody ProcesoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@PostMapping(value="/consultarArbolProceso")
	public List<ProcesoDTO> consultarArbolProceso(@RequestBody ProcesoFilterDTO dto)throws FlexException {
		try {
			return procesoService.consultarArbol(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}

	@PostMapping(value="/obtenerProcesoParaGraficarProceso")
	public ProcesoDTO obtenerProcesoParaGraficarProceso(@RequestBody ProcesoFilterDTO dto)throws FlexException {
		try {
			return procesoService.obtenerProcesoParaGraficar(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private DocumentoTransaccionSvc documentoTransaccionService;
	
	@PostMapping(value="/consultaXIdDocumentoTransaccion")
	public DocumentoTransaccionDTO consultaXIdDocumentoTransaccion(@RequestBody String llave) throws FlexException {
		try {
			return documentoTransaccionService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosDocumentoTransaccion")
	public int contarResultadosDocumentoTransaccion(@RequestBody DocumentoTransaccionFilterDTO dto) throws FlexException  {
		try {
			return documentoTransaccionService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaDocumentoTransaccion")
	public DocumentoTransaccionDTO consultaUnicaDocumentoTransaccion(@RequestBody DocumentoTransaccionFilterDTO dto) throws FlexException  {
		try {
			return documentoTransaccionService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaDocumentoTransaccion")
	public List<DocumentoTransaccionDTO> listarConsultaDocumentoTransaccion(@RequestBody DocumentoTransaccionFilterDTO dto) throws FlexException  {
		try {
			return documentoTransaccionService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarDocumentoTransaccion")
	public DocumentoTransaccionDTO activarDocumentoTransaccion(@RequestBody DocumentoTransaccionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoTransaccionService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarDocumentoTransaccion")
	public DocumentoTransaccionDTO inactivarDocumentoTransaccion(@RequestBody DocumentoTransaccionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoTransaccionService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarDocumentoTransaccion")
	public DocumentoTransaccionDTO actualizarDocumentoTransaccion(@RequestBody DocumentoTransaccionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoTransaccionService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarDocumentoTransaccion")
	public DocumentoTransaccionDTO guardarDocumentoTransaccion(@RequestBody DocumentoTransaccionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoTransaccionService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private ProcesoEstadoSvc procesoEstadoService;
	
	@PostMapping(value="/consultaXIdProcesoEstado")
	public ProcesoEstadoDTO consultaXIdProcesoEstado(@RequestBody String llave) throws FlexException {
		try {
			return procesoEstadoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosProcesoEstado")
	public int contarResultadosProcesoEstado(@RequestBody ProcesoEstadoFilterDTO dto) throws FlexException  {
		try {
			return procesoEstadoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaProcesoEstado")
	public ProcesoEstadoDTO consultaUnicaProcesoEstado(@RequestBody ProcesoEstadoFilterDTO dto) throws FlexException  {
		try {
			return procesoEstadoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaProcesoEstado")
	public List<ProcesoEstadoDTO> listarConsultaProcesoEstado(@RequestBody ProcesoEstadoFilterDTO dto) throws FlexException  {
		try {
			return procesoEstadoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarProcesoEstado")
	public ProcesoEstadoDTO activarProcesoEstado(@RequestBody ProcesoEstadoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoEstadoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarProcesoEstado")
	public ProcesoEstadoDTO inactivarProcesoEstado(@RequestBody ProcesoEstadoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoEstadoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarProcesoEstado")
	public ProcesoEstadoDTO actualizarProcesoEstado(@RequestBody ProcesoEstadoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoEstadoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarProcesoEstado")
	public ProcesoEstadoDTO guardarProcesoEstado(@RequestBody ProcesoEstadoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoEstadoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private ProcesoTransicionAutomaticaSvc procesoTransicionAutomaticaService;
	
	@PostMapping(value="/consultaXIdProcesoTransicionAutomatica")
	public ProcesoTransicionAutomaticaDTO consultaXIdProcesoTransicionAutomatica(@RequestBody String llave) throws FlexException {
		try {
			return procesoTransicionAutomaticaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosProcesoTransicionAutomatica")
	public int contarResultadosProcesoTransicionAutomatica(@RequestBody ProcesoTransicionAutomaticaFilterDTO dto) throws FlexException  {
		try {
			return procesoTransicionAutomaticaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaProcesoTransicionAutomatica")
	public ProcesoTransicionAutomaticaDTO consultaUnicaProcesoTransicionAutomatica(@RequestBody ProcesoTransicionAutomaticaFilterDTO dto) throws FlexException  {
		try {
			return procesoTransicionAutomaticaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaProcesoTransicionAutomatica")
	public List<ProcesoTransicionAutomaticaDTO> listarConsultaProcesoTransicionAutomatica(@RequestBody ProcesoTransicionAutomaticaFilterDTO dto) throws FlexException  {
		try {
			return procesoTransicionAutomaticaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarProcesoTransicionAutomatica")
	public ProcesoTransicionAutomaticaDTO activarProcesoTransicionAutomatica(@RequestBody ProcesoTransicionAutomaticaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoTransicionAutomaticaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarProcesoTransicionAutomatica")
	public ProcesoTransicionAutomaticaDTO inactivarProcesoTransicionAutomatica(@RequestBody ProcesoTransicionAutomaticaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoTransicionAutomaticaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarProcesoTransicionAutomatica")
	public ProcesoTransicionAutomaticaDTO actualizarProcesoTransicionAutomatica(@RequestBody ProcesoTransicionAutomaticaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoTransicionAutomaticaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarProcesoTransicionAutomatica")
	public ProcesoTransicionAutomaticaDTO guardarProcesoTransicionAutomatica(@RequestBody ProcesoTransicionAutomaticaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoTransicionAutomaticaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@PostMapping(value="/ejecutarProcesoTransicionAutomatica")
	public ProcesoTransicionAutomaticaDTO ejecutarProcesoTransicionAutomatica(@RequestBody ProcesoTransicionAutomaticaDTO dto, @RequestHeader("Authorization") String token)throws FlexException {
		try {
			return procesoTransicionAutomaticaService.ejecutar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}

	@PostMapping(value="/programarProcesoTransicionAutomatica")
	public ProcesoTransicionAutomaticaDTO programarProcesoTransicionAutomatica(@RequestBody ProcesoTransicionAutomaticaDTO dto, @RequestHeader("Authorization") String token)throws FlexException {
		try {
			return procesoTransicionAutomaticaService.programar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private DocumentoRelacionExpedienteSvc documentoRelacionExpedienteService;
	
	@PostMapping(value="/consultaXIdDocumentoRelacionExpediente")
	public DocumentoRelacionExpedienteDTO consultaXIdDocumentoRelacionExpediente(@RequestBody String llave) throws FlexException {
		try {
			return documentoRelacionExpedienteService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosDocumentoRelacionExpediente")
	public int contarResultadosDocumentoRelacionExpediente(@RequestBody DocumentoRelacionExpedienteFilterDTO dto) throws FlexException  {
		try {
			return documentoRelacionExpedienteService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaDocumentoRelacionExpediente")
	public DocumentoRelacionExpedienteDTO consultaUnicaDocumentoRelacionExpediente(@RequestBody DocumentoRelacionExpedienteFilterDTO dto) throws FlexException  {
		try {
			return documentoRelacionExpedienteService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaDocumentoRelacionExpediente")
	public List<DocumentoRelacionExpedienteDTO> listarConsultaDocumentoRelacionExpediente(@RequestBody DocumentoRelacionExpedienteFilterDTO dto) throws FlexException  {
		try {
			return documentoRelacionExpedienteService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarDocumentoRelacionExpediente")
	public DocumentoRelacionExpedienteDTO activarDocumentoRelacionExpediente(@RequestBody DocumentoRelacionExpedienteDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoRelacionExpedienteService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarDocumentoRelacionExpediente")
	public DocumentoRelacionExpedienteDTO inactivarDocumentoRelacionExpediente(@RequestBody DocumentoRelacionExpedienteDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoRelacionExpedienteService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarDocumentoRelacionExpediente")
	public DocumentoRelacionExpedienteDTO actualizarDocumentoRelacionExpediente(@RequestBody DocumentoRelacionExpedienteDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoRelacionExpedienteService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarDocumentoRelacionExpediente")
	public DocumentoRelacionExpedienteDTO guardarDocumentoRelacionExpediente(@RequestBody DocumentoRelacionExpedienteDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoRelacionExpedienteService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private DocumentoRelacionGestorSvc documentoRelacionGestorService;
	
	@PostMapping(value="/consultaXIdDocumentoRelacionGestor")
	public DocumentoRelacionGestorDTO consultaXIdDocumentoRelacionGestor(@RequestBody String llave) throws FlexException {
		try {
			return documentoRelacionGestorService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosDocumentoRelacionGestor")
	public int contarResultadosDocumentoRelacionGestor(@RequestBody DocumentoRelacionGestorFilterDTO dto) throws FlexException  {
		try {
			return documentoRelacionGestorService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaDocumentoRelacionGestor")
	public DocumentoRelacionGestorDTO consultaUnicaDocumentoRelacionGestor(@RequestBody DocumentoRelacionGestorFilterDTO dto) throws FlexException  {
		try {
			return documentoRelacionGestorService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaDocumentoRelacionGestor")
	public List<DocumentoRelacionGestorDTO> listarConsultaDocumentoRelacionGestor(@RequestBody DocumentoRelacionGestorFilterDTO dto) throws FlexException  {
		try {
			return documentoRelacionGestorService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarDocumentoRelacionGestor")
	public DocumentoRelacionGestorDTO activarDocumentoRelacionGestor(@RequestBody DocumentoRelacionGestorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoRelacionGestorService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarDocumentoRelacionGestor")
	public DocumentoRelacionGestorDTO inactivarDocumentoRelacionGestor(@RequestBody DocumentoRelacionGestorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoRelacionGestorService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarDocumentoRelacionGestor")
	public DocumentoRelacionGestorDTO actualizarDocumentoRelacionGestor(@RequestBody DocumentoRelacionGestorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoRelacionGestorService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarDocumentoRelacionGestor")
	public DocumentoRelacionGestorDTO guardarDocumentoRelacionGestor(@RequestBody DocumentoRelacionGestorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoRelacionGestorService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@PostMapping(value="/listarExpedientesGestionadoresDocumentoRelacionGestor")
	public List<DocumentoRelacionGestorDTO> listarExpedientesGestionadoresDocumentoRelacionGestor(@RequestBody DocumentoRelacionGestorFilterDTO dto)throws FlexException {
		try {
			return documentoRelacionGestorService.listarExpedientesGestionadores(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private DocumentoPlantillaCaracteristicaSvc documentoPlantillaCaracteristicaService;
	
	@PostMapping(value="/consultaXIdDocumentoPlantillaCaracteristica")
	public DocumentoPlantillaCaracteristicaDTO consultaXIdDocumentoPlantillaCaracteristica(@RequestBody String llave) throws FlexException {
		try {
			return documentoPlantillaCaracteristicaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosDocumentoPlantillaCaracteristica")
	public int contarResultadosDocumentoPlantillaCaracteristica(@RequestBody DocumentoPlantillaCaracteristicaFilterDTO dto) throws FlexException  {
		try {
			return documentoPlantillaCaracteristicaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaDocumentoPlantillaCaracteristica")
	public DocumentoPlantillaCaracteristicaDTO consultaUnicaDocumentoPlantillaCaracteristica(@RequestBody DocumentoPlantillaCaracteristicaFilterDTO dto) throws FlexException  {
		try {
			return documentoPlantillaCaracteristicaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaDocumentoPlantillaCaracteristica")
	public List<DocumentoPlantillaCaracteristicaDTO> listarConsultaDocumentoPlantillaCaracteristica(@RequestBody DocumentoPlantillaCaracteristicaFilterDTO dto) throws FlexException  {
		try {
			dto.setPaginacionRegistroFinal(500);
			return documentoPlantillaCaracteristicaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarDocumentoPlantillaCaracteristica")
	public DocumentoPlantillaCaracteristicaDTO activarDocumentoPlantillaCaracteristica(@RequestBody DocumentoPlantillaCaracteristicaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoPlantillaCaracteristicaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarDocumentoPlantillaCaracteristica")
	public DocumentoPlantillaCaracteristicaDTO inactivarDocumentoPlantillaCaracteristica(@RequestBody DocumentoPlantillaCaracteristicaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoPlantillaCaracteristicaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarDocumentoPlantillaCaracteristica")
	public DocumentoPlantillaCaracteristicaDTO actualizarDocumentoPlantillaCaracteristica(@RequestBody DocumentoPlantillaCaracteristicaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoPlantillaCaracteristicaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarDocumentoPlantillaCaracteristica")
	public DocumentoPlantillaCaracteristicaDTO guardarDocumentoPlantillaCaracteristica(@RequestBody DocumentoPlantillaCaracteristicaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoPlantillaCaracteristicaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@PostMapping(value="/listarCargaDocumentoPlantillaCaracteristica")
	public DocumentoPlantillaCaracteristicaDTO listarCargaDocumentoPlantillaCaracteristica(@RequestBody DocumentoPlantillaCaracteristicaFilterDTO dto)throws FlexException {
		try {
			return documentoPlantillaCaracteristicaService.listarCarga(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private PedidoVentaSvc pedidoVentaService;
	
	@PostMapping(value="/consultaXIdPedidoVenta")
	public PedidoVentaDTO consultaXIdPedidoVenta(@RequestBody String llave) throws FlexException {
		try {
			return pedidoVentaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosPedidoVenta")
	public int contarResultadosPedidoVenta(@RequestBody PedidoVentaFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaPedidoVenta")
	public PedidoVentaDTO consultaUnicaPedidoVenta(@RequestBody PedidoVentaFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaPedidoVenta")
	public List<PedidoVentaDTO> listarConsultaPedidoVenta(@RequestBody PedidoVentaFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarPedidoVenta")
	public PedidoVentaDTO activarPedidoVenta(@RequestBody PedidoVentaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarPedidoVenta")
	public PedidoVentaDTO inactivarPedidoVenta(@RequestBody PedidoVentaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarPedidoVenta")
	public PedidoVentaDTO actualizarPedidoVenta(@RequestBody PedidoVentaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return crudService.update(dto,dto.getLlaveTabla(), token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarPedidoVenta")
	public PedidoVentaDTO guardarPedidoVenta(@RequestBody PedidoVentaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return crudService.save(dto, token, null);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@PostMapping(value="/consultaCompletaPedidoVenta")
	public PedidoVentaDTO consultaCompletaPedidoVenta(@RequestBody PedidoVentaFilterDTO dto)throws FlexException {
		try {
			return pedidoVentaService.consultaCompleta(dto.getLlaveTabla(), dto.getSecurityToken());
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private CallDocumentListWithFilters documentListWithFiltersFunction;
	
	@PostMapping(value="/listarAvanzadoPedidoVenta")
	public List<PedidoVentaDTO> listarAvanzadoPedidoVenta(@RequestBody PedidoVentaFilterDTO dto)throws FlexException {
		try {
			return documentListWithFiltersFunction.listarAvanzado(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	
	@Autowired private PedidoVentaCaracteristicaSvc pedidoVentaCaracteristicaService;
	
	@PostMapping(value="/consultaXIdPedidoVentaCaracteristica")
	public PedidoVentaCaracteristicaDTO consultaXIdPedidoVentaCaracteristica(@RequestBody String llave) throws FlexException {
		try {
			return pedidoVentaCaracteristicaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosPedidoVentaCaracteristica")
	public int contarResultadosPedidoVentaCaracteristica(@RequestBody PedidoVentaCaracteristicaFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaCaracteristicaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaPedidoVentaCaracteristica")
	public PedidoVentaCaracteristicaDTO consultaUnicaPedidoVentaCaracteristica(@RequestBody PedidoVentaCaracteristicaFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaCaracteristicaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaPedidoVentaCaracteristica")
	public List<PedidoVentaCaracteristicaDTO> listarConsultaPedidoVentaCaracteristica(@RequestBody PedidoVentaCaracteristicaFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaCaracteristicaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarPedidoVentaCaracteristica")
	public PedidoVentaCaracteristicaDTO activarPedidoVentaCaracteristica(@RequestBody PedidoVentaCaracteristicaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaCaracteristicaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarPedidoVentaCaracteristica")
	public PedidoVentaCaracteristicaDTO inactivarPedidoVentaCaracteristica(@RequestBody PedidoVentaCaracteristicaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaCaracteristicaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarPedidoVentaCaracteristica")
	public PedidoVentaCaracteristicaDTO actualizarPedidoVentaCaracteristica(@RequestBody PedidoVentaCaracteristicaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaCaracteristicaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarPedidoVentaCaracteristica")
	public PedidoVentaCaracteristicaDTO guardarPedidoVentaCaracteristica(@RequestBody PedidoVentaCaracteristicaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaCaracteristicaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@PostMapping(value="/completarDatosBasePedidoVentaCaracteristica")
	public PedidoVentaCaracteristicaDTO completarDatosBasePedidoVentaCaracteristica(@RequestBody PedidoVentaCaracteristicaFilterDTO dto)throws FlexException {
		try {
			return pedidoVentaCaracteristicaService.completarDatosBase(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private ProcesoTransicionSvc procesoTransicionService;
	
	@PostMapping(value="/consultaXIdProcesoTransicion")
	public ProcesoTransicionDTO consultaXIdProcesoTransicion(@RequestBody String llave) throws FlexException {
		try {
			return procesoTransicionService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosProcesoTransicion")
	public int contarResultadosProcesoTransicion(@RequestBody ProcesoTransicionFilterDTO dto) throws FlexException  {
		try {
			return procesoTransicionService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaProcesoTransicion")
	public ProcesoTransicionDTO consultaUnicaProcesoTransicion(@RequestBody ProcesoTransicionFilterDTO dto) throws FlexException  {
		try {
			return procesoTransicionService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaProcesoTransicion")
	public List<ProcesoTransicionDTO> listarConsultaProcesoTransicion(@RequestBody ProcesoTransicionFilterDTO dto) throws FlexException  {
		try {
			return procesoTransicionService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarProcesoTransicion")
	public ProcesoTransicionDTO activarProcesoTransicion(@RequestBody ProcesoTransicionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoTransicionService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarProcesoTransicion")
	public ProcesoTransicionDTO inactivarProcesoTransicion(@RequestBody ProcesoTransicionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoTransicionService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarProcesoTransicion")
	public ProcesoTransicionDTO actualizarProcesoTransicion(@RequestBody ProcesoTransicionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoTransicionService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarProcesoTransicion")
	public ProcesoTransicionDTO guardarProcesoTransicion(@RequestBody ProcesoTransicionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return procesoTransicionService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private PedidoVentaAjusteSvc pedidoVentaAjusteService;
	
	@PostMapping(value="/consultaXIdPedidoVentaAjuste")
	public PedidoVentaAjusteDTO consultaXIdPedidoVentaAjuste(@RequestBody String llave) throws FlexException {
		try {
			return pedidoVentaAjusteService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosPedidoVentaAjuste")
	public int contarResultadosPedidoVentaAjuste(@RequestBody PedidoVentaAjusteFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaAjusteService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaPedidoVentaAjuste")
	public PedidoVentaAjusteDTO consultaUnicaPedidoVentaAjuste(@RequestBody PedidoVentaAjusteFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaAjusteService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaPedidoVentaAjuste")
	public List<PedidoVentaAjusteDTO> listarConsultaPedidoVentaAjuste(@RequestBody PedidoVentaAjusteFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaAjusteService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarPedidoVentaAjuste")
	public PedidoVentaAjusteDTO activarPedidoVentaAjuste(@RequestBody PedidoVentaAjusteDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaAjusteService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarPedidoVentaAjuste")
	public PedidoVentaAjusteDTO inactivarPedidoVentaAjuste(@RequestBody PedidoVentaAjusteDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaAjusteService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarPedidoVentaAjuste")
	public PedidoVentaAjusteDTO actualizarPedidoVentaAjuste(@RequestBody PedidoVentaAjusteDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaAjusteService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarPedidoVentaAjuste")
	public PedidoVentaAjusteDTO guardarPedidoVentaAjuste(@RequestBody PedidoVentaAjusteDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaAjusteService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private DocumentoPlantillaSvc documentoPlantillaService;
	
	@PostMapping(value="/consultaXIdDocumentoPlantilla")
	public DocumentoPlantillaDTO consultaXIdDocumentoPlantilla(@RequestBody String llave) throws FlexException {
		try {
			return documentoPlantillaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosDocumentoPlantilla")
	public int contarResultadosDocumentoPlantilla(@RequestBody DocumentoPlantillaFilterDTO dto) throws FlexException  {
		try {
			return documentoPlantillaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaDocumentoPlantilla")
	public DocumentoPlantillaDTO consultaUnicaDocumentoPlantilla(@RequestBody DocumentoPlantillaFilterDTO dto) throws FlexException  {
		try {
			return documentoPlantillaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaDocumentoPlantilla")
	public List<DocumentoPlantillaDTO> listarConsultaDocumentoPlantilla(@RequestBody DocumentoPlantillaFilterDTO dto) throws FlexException  {
		try {
			return documentoPlantillaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarDocumentoPlantilla")
	public DocumentoPlantillaDTO activarDocumentoPlantilla(@RequestBody DocumentoPlantillaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoPlantillaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarDocumentoPlantilla")
	public DocumentoPlantillaDTO inactivarDocumentoPlantilla(@RequestBody DocumentoPlantillaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoPlantillaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarDocumentoPlantilla")
	public DocumentoPlantillaDTO actualizarDocumentoPlantilla(@RequestBody DocumentoPlantillaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoPlantillaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarDocumentoPlantilla")
	public DocumentoPlantillaDTO guardarDocumentoPlantilla(@RequestBody DocumentoPlantillaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return documentoPlantillaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@PostMapping(value="/consultaUsuarioDocumentoPlantilla")
	public List<DocumentoPlantillaDTO> consultaUsuarioDocumentoPlantilla(@RequestBody DocumentoPlantillaFilterDTO dto)throws FlexException {
		try {
			return documentoPlantillaService.consultaUsuario(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}

	@PostMapping(value="/obtenerCamposDocumentoPlantilla")
	public DocumentoPlantillaDTO obtenerCamposDocumentoPlantilla(@RequestBody DocumentoPlantillaDTO dto, @RequestHeader("Authorization") String token)throws FlexException {
		try {
			return documentoPlantillaService.obtenerCampos(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}

	@PostMapping(value="/duplicarDocumentoPlantilla")
	public DocumentoPlantillaDTO duplicarDocumentoPlantilla(@RequestBody DocumentoPlantillaDTO dto, @RequestHeader("Authorization") String token)throws FlexException {
		try {
			return documentoPlantillaService.duplicar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}

	@PostMapping(value="/consultaAdministradorDocumentoPlantilla")
	public List<DocumentoPlantillaDTO> consultaAdministradorDocumentoPlantilla(@RequestBody DocumentoPlantillaFilterDTO dto)throws FlexException {
		try {
			return documentoPlantillaService.consultaAdministrador(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private MovimientoSvc movimientoService;
	
	@PostMapping(value="/consultaXIdMovimiento")
	public MovimientoDTO consultaXIdMovimiento(@RequestBody String llave) throws FlexException {
		try {
			return movimientoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosMovimiento")
	public int contarResultadosMovimiento(@RequestBody MovimientoFilterDTO dto) throws FlexException  {
		try {
			return movimientoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaMovimiento")
	public MovimientoDTO consultaUnicaMovimiento(@RequestBody MovimientoFilterDTO dto) throws FlexException  {
		try {
			return movimientoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaMovimiento")
	public List<MovimientoDTO> listarConsultaMovimiento(@RequestBody MovimientoFilterDTO dto) throws FlexException  {
		try {
			return movimientoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarMovimiento")
	public MovimientoDTO activarMovimiento(@RequestBody MovimientoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return movimientoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarMovimiento")
	public MovimientoDTO inactivarMovimiento(@RequestBody MovimientoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return movimientoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarMovimiento")
	public MovimientoDTO actualizarMovimiento(@RequestBody MovimientoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return movimientoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarMovimiento")
	public MovimientoDTO guardarMovimiento(@RequestBody MovimientoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return movimientoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@PostMapping(value="/obtenerMovimientoAnteriorFechaMovimiento")
	public List<MovimientoDTO> obtenerMovimientoAnteriorFechaMovimiento(@RequestBody MovimientoFilterDTO dto)throws FlexException {
		try {
			return movimientoService.obtenerMovimientoAnteriorFecha(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}

	@PostMapping(value="/obtenerMovimientoSiguienteFechaMovimiento")
	public List<MovimientoDTO> obtenerMovimientoSiguienteFechaMovimiento(@RequestBody MovimientoFilterDTO dto)throws FlexException {
		try {
			return movimientoService.obtenerMovimientoSiguienteFecha(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private TurnoSvc turnoService;
	
	@PostMapping(value="/consultaXIdTurno")
	public TurnoDTO consultaXIdTurno(@RequestBody String llave) throws FlexException {
		try {
			return turnoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosTurno")
	public int contarResultadosTurno(@RequestBody TurnoFilterDTO dto) throws FlexException  {
		try {
			return turnoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaTurno")
	public TurnoDTO consultaUnicaTurno(@RequestBody TurnoFilterDTO dto) throws FlexException  {
		try {
			return turnoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaTurno")
	public List<TurnoDTO> listarConsultaTurno(@RequestBody TurnoFilterDTO dto) throws FlexException  {
		try {
			return turnoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarTurno")
	public TurnoDTO activarTurno(@RequestBody TurnoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return turnoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarTurno")
	public TurnoDTO inactivarTurno(@RequestBody TurnoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return turnoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarTurno")
	public TurnoDTO actualizarTurno(@RequestBody TurnoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return turnoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarTurno")
	public TurnoDTO guardarTurno(@RequestBody TurnoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return turnoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private TarifaSvc tarifaService;
	
	@PostMapping(value="/consultaXIdTarifa")
	public TarifaDTO consultaXIdTarifa(@RequestBody String llave) throws FlexException {
		try {
			return tarifaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosTarifa")
	public int contarResultadosTarifa(@RequestBody TarifaFilterDTO dto) throws FlexException  {
		try {
			return tarifaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaTarifa")
	public TarifaDTO consultaUnicaTarifa(@RequestBody TarifaFilterDTO dto) throws FlexException  {
		try {
			return tarifaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaTarifa")
	public List<TarifaDTO> listarConsultaTarifa(@RequestBody TarifaFilterDTO dto) throws FlexException  {
		try {
			return tarifaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarTarifa")
	public TarifaDTO activarTarifa(@RequestBody TarifaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return tarifaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarTarifa")
	public TarifaDTO inactivarTarifa(@RequestBody TarifaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return tarifaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarTarifa")
	public TarifaDTO actualizarTarifa(@RequestBody TarifaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return tarifaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarTarifa")
	public TarifaDTO guardarTarifa(@RequestBody TarifaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return tarifaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private PedidoVentaDineroSvc pedidoVentaDineroService;
	
	@PostMapping(value="/consultaXIdPedidoVentaDinero")
	public PedidoVentaDineroDTO consultaXIdPedidoVentaDinero(@RequestBody String llave) throws FlexException {
		try {
			return pedidoVentaDineroService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosPedidoVentaDinero")
	public int contarResultadosPedidoVentaDinero(@RequestBody PedidoVentaDineroFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaDineroService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaPedidoVentaDinero")
	public PedidoVentaDineroDTO consultaUnicaPedidoVentaDinero(@RequestBody PedidoVentaDineroFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaDineroService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaPedidoVentaDinero")
	public List<PedidoVentaDineroDTO> listarConsultaPedidoVentaDinero(@RequestBody PedidoVentaDineroFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaDineroService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarPedidoVentaDinero")
	public PedidoVentaDineroDTO activarPedidoVentaDinero(@RequestBody PedidoVentaDineroDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaDineroService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarPedidoVentaDinero")
	public PedidoVentaDineroDTO inactivarPedidoVentaDinero(@RequestBody PedidoVentaDineroDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaDineroService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarPedidoVentaDinero")
	public PedidoVentaDineroDTO actualizarPedidoVentaDinero(@RequestBody PedidoVentaDineroDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaDineroService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarPedidoVentaDinero")
	public PedidoVentaDineroDTO guardarPedidoVentaDinero(@RequestBody PedidoVentaDineroDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaDineroService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private CuentaSvc cuentaService;
	
	@PostMapping(value="/consultaXIdCuenta")
	public CuentaDTO consultaXIdCuenta(@RequestBody String llave) throws FlexException {
		try {
			return cuentaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosCuenta")
	public int contarResultadosCuenta(@RequestBody CuentaFilterDTO dto) throws FlexException  {
		try {
			return cuentaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaCuenta")
	public CuentaDTO consultaUnicaCuenta(@RequestBody CuentaFilterDTO dto) throws FlexException  {
		try {
			return cuentaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaCuenta")
	public List<CuentaDTO> listarConsultaCuenta(@RequestBody CuentaFilterDTO dto) throws FlexException  {
		try {
			return cuentaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarCuenta")
	public CuentaDTO activarCuenta(@RequestBody CuentaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cuentaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarCuenta")
	public CuentaDTO inactivarCuenta(@RequestBody CuentaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cuentaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarCuenta")
	public CuentaDTO actualizarCuenta(@RequestBody CuentaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cuentaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarCuenta")
	public CuentaDTO guardarCuenta(@RequestBody CuentaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cuentaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private TarifarioSvc tarifarioService;
	
	@PostMapping(value="/consultaXIdTarifario")
	public TarifarioDTO consultaXIdTarifario(@RequestBody String llave) throws FlexException {
		try {
			return tarifarioService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosTarifario")
	public int contarResultadosTarifario(@RequestBody TarifarioFilterDTO dto) throws FlexException  {
		try {
			return tarifarioService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaTarifario")
	public TarifarioDTO consultaUnicaTarifario(@RequestBody TarifarioFilterDTO dto) throws FlexException  {
		try {
			return tarifarioService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaTarifario")
	public List<TarifarioDTO> listarConsultaTarifario(@RequestBody TarifarioFilterDTO dto) throws FlexException  {
		try {
			return tarifarioService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarTarifario")
	public TarifarioDTO activarTarifario(@RequestBody TarifarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return tarifarioService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarTarifario")
	public TarifarioDTO inactivarTarifario(@RequestBody TarifarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return tarifarioService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarTarifario")
	public TarifarioDTO actualizarTarifario(@RequestBody TarifarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return tarifarioService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarTarifario")
	public TarifarioDTO guardarTarifario(@RequestBody TarifarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return tarifarioService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private ActividadSvc actividadService;
	
	@PostMapping(value="/consultaXIdActividad")
	public ActividadDTO consultaXIdActividad(@RequestBody String llave) throws FlexException {
		try {
			return actividadService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosActividad")
	public int contarResultadosActividad(@RequestBody ActividadFilterDTO dto) throws FlexException  {
		try {
			return actividadService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaActividad")
	public ActividadDTO consultaUnicaActividad(@RequestBody ActividadFilterDTO dto) throws FlexException  {
		try {
			return actividadService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaActividad")
	public List<ActividadDTO> listarConsultaActividad(@RequestBody ActividadFilterDTO dto) throws FlexException  {
		try {
			return actividadService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarActividad")
	public ActividadDTO activarActividad(@RequestBody ActividadDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return actividadService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarActividad")
	public ActividadDTO inactivarActividad(@RequestBody ActividadDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return actividadService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarActividad")
	public ActividadDTO actualizarActividad(@RequestBody ActividadDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return actividadService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarActividad")
	public ActividadDTO guardarActividad(@RequestBody ActividadDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return actividadService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private PedidoVentaTiempoSvc pedidoVentaTiempoService;
	
	@PostMapping(value="/consultaXIdPedidoVentaTiempo")
	public PedidoVentaTiempoDTO consultaXIdPedidoVentaTiempo(@RequestBody String llave) throws FlexException {
		try {
			return pedidoVentaTiempoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosPedidoVentaTiempo")
	public int contarResultadosPedidoVentaTiempo(@RequestBody PedidoVentaTiempoFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaTiempoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaPedidoVentaTiempo")
	public PedidoVentaTiempoDTO consultaUnicaPedidoVentaTiempo(@RequestBody PedidoVentaTiempoFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaTiempoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaPedidoVentaTiempo")
	public List<PedidoVentaTiempoDTO> listarConsultaPedidoVentaTiempo(@RequestBody PedidoVentaTiempoFilterDTO dto) throws FlexException  {
		try {
			return pedidoVentaTiempoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarPedidoVentaTiempo")
	public PedidoVentaTiempoDTO activarPedidoVentaTiempo(@RequestBody PedidoVentaTiempoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaTiempoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarPedidoVentaTiempo")
	public PedidoVentaTiempoDTO inactivarPedidoVentaTiempo(@RequestBody PedidoVentaTiempoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaTiempoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarPedidoVentaTiempo")
	public PedidoVentaTiempoDTO actualizarPedidoVentaTiempo(@RequestBody PedidoVentaTiempoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaTiempoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarPedidoVentaTiempo")
	public PedidoVentaTiempoDTO guardarPedidoVentaTiempo(@RequestBody PedidoVentaTiempoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return pedidoVentaTiempoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private PropiedadSvc propiedadService;
	
	@PostMapping(value="/consultaXIdPropiedad")
	public PropiedadDTO consultaXIdPropiedad(@RequestBody String llave) throws FlexException {
		try {
			return propiedadService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosPropiedad")
	public int contarResultadosPropiedad(@RequestBody PropiedadFilterDTO dto) throws FlexException  {
		try {
			return propiedadService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaPropiedad")
	public PropiedadDTO consultaUnicaPropiedad(@RequestBody PropiedadFilterDTO dto) throws FlexException  {
		try {
			return propiedadService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaPropiedad")
	public List<PropiedadDTO> listarConsultaPropiedad(@RequestBody PropiedadFilterDTO dto) throws FlexException  {
		try {
			return propiedadService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarPropiedad")
	public PropiedadDTO activarPropiedad(@RequestBody PropiedadDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return propiedadService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarPropiedad")
	public PropiedadDTO inactivarPropiedad(@RequestBody PropiedadDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return propiedadService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarPropiedad")
	public PropiedadDTO actualizarPropiedad(@RequestBody PropiedadDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return propiedadService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarPropiedad")
	public PropiedadDTO guardarPropiedad(@RequestBody PropiedadDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return propiedadService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private RelacionInternaSvc relacionInternaService;
	
	@PostMapping(value="/consultaXIdRelacionInterna")
	public RelacionInternaDTO consultaXIdRelacionInterna(@RequestBody String llave) throws FlexException {
		try {
			return relacionInternaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosRelacionInterna")
	public int contarResultadosRelacionInterna(@RequestBody RelacionInternaFilterDTO dto) throws FlexException  {
		try {
			return relacionInternaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaRelacionInterna")
	public RelacionInternaDTO consultaUnicaRelacionInterna(@RequestBody RelacionInternaFilterDTO dto) throws FlexException  {
		try {
			return relacionInternaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaRelacionInterna")
	public List<RelacionInternaDTO> listarConsultaRelacionInterna(@RequestBody RelacionInternaFilterDTO dto) throws FlexException  {
		try {
			return relacionInternaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarRelacionInterna")
	public RelacionInternaDTO activarRelacionInterna(@RequestBody RelacionInternaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return relacionInternaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarRelacionInterna")
	public RelacionInternaDTO inactivarRelacionInterna(@RequestBody RelacionInternaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return relacionInternaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarRelacionInterna")
	public RelacionInternaDTO actualizarRelacionInterna(@RequestBody RelacionInternaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return relacionInternaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarRelacionInterna")
	public RelacionInternaDTO guardarRelacionInterna(@RequestBody RelacionInternaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return relacionInternaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@PostMapping(value="/listarRelacionRelacionInterna")
	public List<RelacionInternaDTO> listarRelacionRelacionInterna(@RequestBody RelacionInternaFilterDTO dto)throws FlexException {
		try {
			return relacionInternaService.listarRelacion(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private EncuestaRespuestaSvc encuestaRespuestaService;
	
	@PostMapping(value="/consultaXIdEncuestaRespuesta")
	public EncuestaRespuestaDTO consultaXIdEncuestaRespuesta(@RequestBody String llave) throws FlexException {
		try {
			return encuestaRespuestaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosEncuestaRespuesta")
	public int contarResultadosEncuestaRespuesta(@RequestBody EncuestaRespuestaFilterDTO dto) throws FlexException  {
		try {
			return encuestaRespuestaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaEncuestaRespuesta")
	public EncuestaRespuestaDTO consultaUnicaEncuestaRespuesta(@RequestBody EncuestaRespuestaFilterDTO dto) throws FlexException  {
		try {
			return encuestaRespuestaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaEncuestaRespuesta")
	public List<EncuestaRespuestaDTO> listarConsultaEncuestaRespuesta(@RequestBody EncuestaRespuestaFilterDTO dto) throws FlexException  {
		try {
			return encuestaRespuestaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarEncuestaRespuesta")
	public EncuestaRespuestaDTO activarEncuestaRespuesta(@RequestBody EncuestaRespuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaRespuestaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarEncuestaRespuesta")
	public EncuestaRespuestaDTO inactivarEncuestaRespuesta(@RequestBody EncuestaRespuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaRespuestaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarEncuestaRespuesta")
	public EncuestaRespuestaDTO actualizarEncuestaRespuesta(@RequestBody EncuestaRespuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaRespuestaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarEncuestaRespuesta")
	public EncuestaRespuestaDTO guardarEncuestaRespuesta(@RequestBody EncuestaRespuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaRespuestaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private CambioSvc cambioService;
	
	@PostMapping(value="/consultaXIdCambio")
	public CambioDTO consultaXIdCambio(@RequestBody String llave) throws FlexException {
		try {
			return cambioService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosCambio")
	public int contarResultadosCambio(@RequestBody CambioFilterDTO dto) throws FlexException  {
		try {
			return cambioService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaCambio")
	public CambioDTO consultaUnicaCambio(@RequestBody CambioFilterDTO dto) throws FlexException  {
		try {
			return cambioService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaCambio")
	public List<CambioDTO> listarConsultaCambio(@RequestBody CambioFilterDTO dto) throws FlexException  {
		try {
			return cambioService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarCambio")
	public CambioDTO activarCambio(@RequestBody CambioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cambioService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarCambio")
	public CambioDTO inactivarCambio(@RequestBody CambioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cambioService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarCambio")
	public CambioDTO actualizarCambio(@RequestBody CambioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cambioService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarCambio")
	public CambioDTO guardarCambio(@RequestBody CambioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cambioService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private EncuestaOpcionRespuestaSvc encuestaOpcionRespuestaService;
	
	@PostMapping(value="/consultaXIdEncuestaOpcionRespuesta")
	public EncuestaOpcionRespuestaDTO consultaXIdEncuestaOpcionRespuesta(@RequestBody String llave) throws FlexException {
		try {
			return encuestaOpcionRespuestaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosEncuestaOpcionRespuesta")
	public int contarResultadosEncuestaOpcionRespuesta(@RequestBody EncuestaOpcionRespuestaFilterDTO dto) throws FlexException  {
		try {
			return encuestaOpcionRespuestaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaEncuestaOpcionRespuesta")
	public EncuestaOpcionRespuestaDTO consultaUnicaEncuestaOpcionRespuesta(@RequestBody EncuestaOpcionRespuestaFilterDTO dto) throws FlexException  {
		try {
			return encuestaOpcionRespuestaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaEncuestaOpcionRespuesta")
	public List<EncuestaOpcionRespuestaDTO> listarConsultaEncuestaOpcionRespuesta(@RequestBody EncuestaOpcionRespuestaFilterDTO dto) throws FlexException  {
		try {
			return encuestaOpcionRespuestaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarEncuestaOpcionRespuesta")
	public EncuestaOpcionRespuestaDTO activarEncuestaOpcionRespuesta(@RequestBody EncuestaOpcionRespuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaOpcionRespuestaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarEncuestaOpcionRespuesta")
	public EncuestaOpcionRespuestaDTO inactivarEncuestaOpcionRespuesta(@RequestBody EncuestaOpcionRespuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaOpcionRespuestaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarEncuestaOpcionRespuesta")
	public EncuestaOpcionRespuestaDTO actualizarEncuestaOpcionRespuesta(@RequestBody EncuestaOpcionRespuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaOpcionRespuestaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarEncuestaOpcionRespuesta")
	public EncuestaOpcionRespuestaDTO guardarEncuestaOpcionRespuesta(@RequestBody EncuestaOpcionRespuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaOpcionRespuestaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private PropiedadValorDefinidoSvc propiedadValorDefinidoService;
	
	@PostMapping(value="/consultaXIdPropiedadValorDefinido")
	public PropiedadValorDefinidoDTO consultaXIdPropiedadValorDefinido(@RequestBody String llave) throws FlexException {
		try {
			return propiedadValorDefinidoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosPropiedadValorDefinido")
	public int contarResultadosPropiedadValorDefinido(@RequestBody PropiedadValorDefinidoFilterDTO dto) throws FlexException  {
		try {
			return propiedadValorDefinidoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaPropiedadValorDefinido")
	public PropiedadValorDefinidoDTO consultaUnicaPropiedadValorDefinido(@RequestBody PropiedadValorDefinidoFilterDTO dto) throws FlexException  {
		try {
			return propiedadValorDefinidoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaPropiedadValorDefinido")
	public List<PropiedadValorDefinidoDTO> listarConsultaPropiedadValorDefinido(@RequestBody PropiedadValorDefinidoFilterDTO dto) throws FlexException  {
		try {
			return propiedadValorDefinidoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarPropiedadValorDefinido")
	public PropiedadValorDefinidoDTO activarPropiedadValorDefinido(@RequestBody PropiedadValorDefinidoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return propiedadValorDefinidoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarPropiedadValorDefinido")
	public PropiedadValorDefinidoDTO inactivarPropiedadValorDefinido(@RequestBody PropiedadValorDefinidoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return propiedadValorDefinidoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarPropiedadValorDefinido")
	public PropiedadValorDefinidoDTO actualizarPropiedadValorDefinido(@RequestBody PropiedadValorDefinidoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return propiedadValorDefinidoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarPropiedadValorDefinido")
	public PropiedadValorDefinidoDTO guardarPropiedadValorDefinido(@RequestBody PropiedadValorDefinidoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return propiedadValorDefinidoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@PostMapping(value="/listarPorOrigenPropiedadValorDefinido")
	public List<PropiedadValorDefinidoDTO> listarPorOrigenPropiedadValorDefinido(@RequestBody PropiedadValorDefinidoFilterDTO dto)throws FlexException {
		try {
			return propiedadValorDefinidoService.listarPorOrigen(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private EncuestaGrupoSvc encuestaGrupoService;
	
	@PostMapping(value="/consultaXIdEncuestaGrupo")
	public EncuestaGrupoDTO consultaXIdEncuestaGrupo(@RequestBody String llave) throws FlexException {
		try {
			return encuestaGrupoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosEncuestaGrupo")
	public int contarResultadosEncuestaGrupo(@RequestBody EncuestaGrupoFilterDTO dto) throws FlexException  {
		try {
			return encuestaGrupoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaEncuestaGrupo")
	public EncuestaGrupoDTO consultaUnicaEncuestaGrupo(@RequestBody EncuestaGrupoFilterDTO dto) throws FlexException  {
		try {
			return encuestaGrupoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaEncuestaGrupo")
	public List<EncuestaGrupoDTO> listarConsultaEncuestaGrupo(@RequestBody EncuestaGrupoFilterDTO dto) throws FlexException  {
		try {
			return encuestaGrupoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarEncuestaGrupo")
	public EncuestaGrupoDTO activarEncuestaGrupo(@RequestBody EncuestaGrupoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaGrupoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarEncuestaGrupo")
	public EncuestaGrupoDTO inactivarEncuestaGrupo(@RequestBody EncuestaGrupoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaGrupoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarEncuestaGrupo")
	public EncuestaGrupoDTO actualizarEncuestaGrupo(@RequestBody EncuestaGrupoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaGrupoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarEncuestaGrupo")
	public EncuestaGrupoDTO guardarEncuestaGrupo(@RequestBody EncuestaGrupoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaGrupoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@PostMapping(value="/responderEncuestaEncuestaGrupo")
	public EncuestaGrupoDTO responderEncuestaEncuestaGrupo(@RequestBody EncuestaGrupoDTO dto, @RequestHeader("Authorization") String token)throws FlexException {
		try {
			return encuestaGrupoService.responderEncuesta(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}

	@PostMapping(value="/copiarEncuestaGrupo")
	public EncuestaGrupoDTO copiarEncuestaGrupo(@RequestBody EncuestaGrupoDTO dto, @RequestHeader("Authorization") String token)throws FlexException {
		try {
			return encuestaGrupoService.copiar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private EncuestaPreguntaSvc encuestaPreguntaService;
	
	@PostMapping(value="/consultaXIdEncuestaPregunta")
	public EncuestaPreguntaDTO consultaXIdEncuestaPregunta(@RequestBody String llave) throws FlexException {
		try {
			return encuestaPreguntaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosEncuestaPregunta")
	public int contarResultadosEncuestaPregunta(@RequestBody EncuestaPreguntaFilterDTO dto) throws FlexException  {
		try {
			return encuestaPreguntaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaEncuestaPregunta")
	public EncuestaPreguntaDTO consultaUnicaEncuestaPregunta(@RequestBody EncuestaPreguntaFilterDTO dto) throws FlexException  {
		try {
			return encuestaPreguntaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaEncuestaPregunta")
	public List<EncuestaPreguntaDTO> listarConsultaEncuestaPregunta(@RequestBody EncuestaPreguntaFilterDTO dto) throws FlexException  {
		try {
			return encuestaPreguntaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarEncuestaPregunta")
	public EncuestaPreguntaDTO activarEncuestaPregunta(@RequestBody EncuestaPreguntaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaPreguntaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarEncuestaPregunta")
	public EncuestaPreguntaDTO inactivarEncuestaPregunta(@RequestBody EncuestaPreguntaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaPreguntaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarEncuestaPregunta")
	public EncuestaPreguntaDTO actualizarEncuestaPregunta(@RequestBody EncuestaPreguntaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaPreguntaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarEncuestaPregunta")
	public EncuestaPreguntaDTO guardarEncuestaPregunta(@RequestBody EncuestaPreguntaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaPreguntaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@PostMapping(value="/listarPermitidasEncuestaPregunta")
	public List<EncuestaPreguntaDTO> listarPermitidasEncuestaPregunta(@RequestBody EncuestaPreguntaFilterDTO dto)throws FlexException {
		try {
			return encuestaPreguntaService.listarPermitidas(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private EncuestaSvc encuestaService;
	
	@PostMapping(value="/consultaXIdEncuesta")
	public EncuestaDTO consultaXIdEncuesta(@RequestBody String llave) throws FlexException {
		try {
			return encuestaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosEncuesta")
	public int contarResultadosEncuesta(@RequestBody EncuestaFilterDTO dto) throws FlexException  {
		try {
			return encuestaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaEncuesta")
	public EncuestaDTO consultaUnicaEncuesta(@RequestBody EncuestaFilterDTO dto) throws FlexException  {
		try {
			return encuestaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaEncuesta")
	public List<EncuestaDTO> listarConsultaEncuesta(@RequestBody EncuestaFilterDTO dto) throws FlexException  {
		try {
			return encuestaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarEncuesta")
	public EncuestaDTO activarEncuesta(@RequestBody EncuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarEncuesta")
	public EncuestaDTO inactivarEncuesta(@RequestBody EncuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarEncuesta")
	public EncuestaDTO actualizarEncuesta(@RequestBody EncuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarEncuesta")
	public EncuestaDTO guardarEncuesta(@RequestBody EncuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return encuestaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@PostMapping(value="/copiarEncuesta")
	public EncuestaDTO copiarEncuesta(@RequestBody EncuestaDTO dto, @RequestHeader("Authorization") String token)throws FlexException {
		try {
			return encuestaService.copiar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}

	@PostMapping(value="/listarDisponiblesEncuesta")
	public List<EncuestaDTO> listarDisponiblesEncuesta(@RequestBody EncuestaFilterDTO dto)throws FlexException {
		try {
			return encuestaService.listarDisponibles(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private UsuarioRolSvc usuarioRolService;
	
	@PostMapping(value="/consultaXIdUsuarioRol")
	public UsuarioRolDTO consultaXIdUsuarioRol(@RequestBody String llave) throws FlexException {
		try {
			return usuarioRolService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosUsuarioRol")
	public int contarResultadosUsuarioRol(@RequestBody UsuarioRolFilterDTO dto) throws FlexException  {
		try {
			return usuarioRolService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaUsuarioRol")
	public UsuarioRolDTO consultaUnicaUsuarioRol(@RequestBody UsuarioRolFilterDTO dto) throws FlexException  {
		try {
			return usuarioRolService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaUsuarioRol")
	public List<UsuarioRolDTO> listarConsultaUsuarioRol(@RequestBody UsuarioRolFilterDTO dto) throws FlexException  {
		try {
			return usuarioRolService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarUsuarioRol")
	public UsuarioRolDTO activarUsuarioRol(@RequestBody UsuarioRolDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioRolService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarUsuarioRol")
	public UsuarioRolDTO inactivarUsuarioRol(@RequestBody UsuarioRolDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioRolService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarUsuarioRol")
	public UsuarioRolDTO actualizarUsuarioRol(@RequestBody UsuarioRolDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioRolService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarUsuarioRol")
	public UsuarioRolDTO guardarUsuarioRol(@RequestBody UsuarioRolDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioRolService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private PuestoSvc puestoService;
	
	@PostMapping(value="/consultaXIdPuesto")
	public PuestoDTO consultaXIdPuesto(@RequestBody String llave) throws FlexException {
		try {
			return puestoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosPuesto")
	public int contarResultadosPuesto(@RequestBody PuestoFilterDTO dto) throws FlexException  {
		try {
			return puestoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaPuesto")
	public PuestoDTO consultaUnicaPuesto(@RequestBody PuestoFilterDTO dto) throws FlexException  {
		try {
			return puestoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaPuesto")
	public List<PuestoDTO> listarConsultaPuesto(@RequestBody PuestoFilterDTO dto) throws FlexException  {
		try {
			return puestoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarPuesto")
	public PuestoDTO activarPuesto(@RequestBody PuestoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return puestoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarPuesto")
	public PuestoDTO inactivarPuesto(@RequestBody PuestoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return puestoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarPuesto")
	public PuestoDTO actualizarPuesto(@RequestBody PuestoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return puestoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarPuesto")
	public PuestoDTO guardarPuesto(@RequestBody PuestoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return puestoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private RolAccesoSvc rolAccesoService;
	
	@PostMapping(value="/consultaXIdRolAcceso")
	public RolAccesoDTO consultaXIdRolAcceso(@RequestBody String llave) throws FlexException {
		try {
			return rolAccesoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosRolAcceso")
	public int contarResultadosRolAcceso(@RequestBody RolAccesoFilterDTO dto) throws FlexException  {
		try {
			return rolAccesoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaRolAcceso")
	public RolAccesoDTO consultaUnicaRolAcceso(@RequestBody RolAccesoFilterDTO dto) throws FlexException  {
		try {
			return rolAccesoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaRolAcceso")
	public List<RolAccesoDTO> listarConsultaRolAcceso(@RequestBody RolAccesoFilterDTO dto) throws FlexException  {
		try {
			return rolAccesoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarRolAcceso")
	public RolAccesoDTO activarRolAcceso(@RequestBody RolAccesoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return rolAccesoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarRolAcceso")
	public RolAccesoDTO inactivarRolAcceso(@RequestBody RolAccesoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return rolAccesoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarRolAcceso")
	public RolAccesoDTO actualizarRolAcceso(@RequestBody RolAccesoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return rolAccesoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarRolAcceso")
	public RolAccesoDTO guardarRolAcceso(@RequestBody RolAccesoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return rolAccesoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@PostMapping(value="/consultaUsuarioDocumentoRolAcceso")
	public List<RolAccesoDTO> consultaUsuarioDocumentoRolAcceso(@RequestBody RolAccesoFilterDTO dto)throws FlexException {
		try {
			return rolAccesoService.consultaUsuarioDocumento(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private UsuarioSvc usuarioService;
	
	@PostMapping(value="/consultaXIdUsuario")
	public UsuarioDTO consultaXIdUsuario(@RequestBody String llave) throws FlexException {
		try {
			return usuarioService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosUsuario")
	public int contarResultadosUsuario(@RequestBody UsuarioFilterDTO dto) throws FlexException  {
		try {
			return usuarioService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaUsuario")
	public UsuarioDTO consultaUnicaUsuario(@RequestBody UsuarioFilterDTO dto) throws FlexException  {
		try {
			return usuarioService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaUsuario")
	public List<UsuarioDTO> listarConsultaUsuario(@RequestBody UsuarioFilterDTO dto) throws FlexException  {
		try {
			return usuarioService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarUsuario")
	public UsuarioDTO activarUsuario(@RequestBody UsuarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarUsuario")
	public UsuarioDTO inactivarUsuario(@RequestBody UsuarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarUsuario")
	public UsuarioDTO actualizarUsuario(@RequestBody UsuarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarUsuario")
	public UsuarioDTO guardarUsuario(@RequestBody UsuarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@PostMapping(value="/listarRolUsuario")
	public List<UsuarioDTO> listarRolUsuario(@RequestBody UsuarioFilterDTO dto)throws FlexException {
		try {
			return usuarioService.listarRol(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private WebServiceSvc webServiceService;
	
	@PostMapping(value="/consultaXIdWebService")
	public WebServiceDTO consultaXIdWebService(@RequestBody String llave) throws FlexException {
		try {
			return webServiceService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosWebService")
	public int contarResultadosWebService(@RequestBody WebServiceFilterDTO dto) throws FlexException  {
		try {
			return webServiceService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaWebService")
	public WebServiceDTO consultaUnicaWebService(@RequestBody WebServiceFilterDTO dto) throws FlexException  {
		try {
			return webServiceService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaWebService")
	public List<WebServiceDTO> listarConsultaWebService(@RequestBody WebServiceFilterDTO dto) throws FlexException  {
		try {
			return webServiceService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarWebService")
	public WebServiceDTO activarWebService(@RequestBody WebServiceDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return webServiceService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarWebService")
	public WebServiceDTO inactivarWebService(@RequestBody WebServiceDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return webServiceService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarWebService")
	public WebServiceDTO actualizarWebService(@RequestBody WebServiceDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return webServiceService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarWebService")
	public WebServiceDTO guardarWebService(@RequestBody WebServiceDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return webServiceService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private PostRespuestaSvc postRespuestaService;
	
	@PostMapping(value="/consultaXIdPostRespuesta")
	public PostRespuestaDTO consultaXIdPostRespuesta(@RequestBody String llave) throws FlexException {
		try {
			return postRespuestaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosPostRespuesta")
	public int contarResultadosPostRespuesta(@RequestBody PostRespuestaFilterDTO dto) throws FlexException  {
		try {
			return postRespuestaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaPostRespuesta")
	public PostRespuestaDTO consultaUnicaPostRespuesta(@RequestBody PostRespuestaFilterDTO dto) throws FlexException  {
		try {
			return postRespuestaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaPostRespuesta")
	public List<PostRespuestaDTO> listarConsultaPostRespuesta(@RequestBody PostRespuestaFilterDTO dto) throws FlexException  {
		try {
			return postRespuestaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarPostRespuesta")
	public PostRespuestaDTO activarPostRespuesta(@RequestBody PostRespuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return postRespuestaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarPostRespuesta")
	public PostRespuestaDTO inactivarPostRespuesta(@RequestBody PostRespuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return postRespuestaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarPostRespuesta")
	public PostRespuestaDTO actualizarPostRespuesta(@RequestBody PostRespuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return postRespuestaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarPostRespuesta")
	public PostRespuestaDTO guardarPostRespuesta(@RequestBody PostRespuestaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return postRespuestaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@PostMapping(value="/listarEnOrdenPostRespuesta")
	public List<PostRespuestaDTO> listarEnOrdenPostRespuesta(@RequestBody PostRespuestaFilterDTO dto)throws FlexException {
		try {
			return postRespuestaService.listarEnOrden(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private GPSLocalizacionSvc gPSLocalizacionService;
	
	@PostMapping(value="/consultaXIdGPSLocalizacion")
	public GPSLocalizacionDTO consultaXIdGPSLocalizacion(@RequestBody String llave) throws FlexException {
		try {
			return gPSLocalizacionService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosGPSLocalizacion")
	public int contarResultadosGPSLocalizacion(@RequestBody GPSLocalizacionFilterDTO dto) throws FlexException  {
		try {
			return gPSLocalizacionService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaGPSLocalizacion")
	public GPSLocalizacionDTO consultaUnicaGPSLocalizacion(@RequestBody GPSLocalizacionFilterDTO dto) throws FlexException  {
		try {
			return gPSLocalizacionService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaGPSLocalizacion")
	public List<GPSLocalizacionDTO> listarConsultaGPSLocalizacion(@RequestBody GPSLocalizacionFilterDTO dto) throws FlexException  {
		try {
			return gPSLocalizacionService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarGPSLocalizacion")
	public GPSLocalizacionDTO activarGPSLocalizacion(@RequestBody GPSLocalizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return gPSLocalizacionService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarGPSLocalizacion")
	public GPSLocalizacionDTO inactivarGPSLocalizacion(@RequestBody GPSLocalizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return gPSLocalizacionService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarGPSLocalizacion")
	public GPSLocalizacionDTO actualizarGPSLocalizacion(@RequestBody GPSLocalizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return gPSLocalizacionService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarGPSLocalizacion")
	public GPSLocalizacionDTO guardarGPSLocalizacion(@RequestBody GPSLocalizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return gPSLocalizacionService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private MensajeSvc mensajeService;
	
	@PostMapping(value="/consultaXIdMensaje")
	public MensajeDTO consultaXIdMensaje(@RequestBody String llave) throws FlexException {
		try {
			return mensajeService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosMensaje")
	public int contarResultadosMensaje(@RequestBody MensajeFilterDTO dto) throws FlexException  {
		try {
			return mensajeService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaMensaje")
	public MensajeDTO consultaUnicaMensaje(@RequestBody MensajeFilterDTO dto) throws FlexException  {
		try {
			return mensajeService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaMensaje")
	public List<MensajeDTO> listarConsultaMensaje(@RequestBody MensajeFilterDTO dto) throws FlexException  {
		try {
			return mensajeService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarMensaje")
	public MensajeDTO activarMensaje(@RequestBody MensajeDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return mensajeService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarMensaje")
	public MensajeDTO inactivarMensaje(@RequestBody MensajeDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return mensajeService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarMensaje")
	public MensajeDTO actualizarMensaje(@RequestBody MensajeDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return mensajeService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarMensaje")
	public MensajeDTO guardarMensaje(@RequestBody MensajeDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return mensajeService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@PostMapping(value="/mensajesUsuarioMensaje")
	public List<MensajeDTO> mensajesUsuarioMensaje(@RequestBody MensajeFilterDTO dto)throws FlexException {
		try {
			return mensajeService.mensajesUsuario(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private MailUserSendMessage userSendMessage;

	@PostMapping(value="/enviarMensajeMensaje")
	public MensajeDTO enviarMensajeMensaje(@RequestBody MensajeFilterDTO dto)throws FlexException {
		try {
			return userSendMessage.call(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private PostCalificacionSvc postCalificacionService;
	
	@PostMapping(value="/consultaXIdPostCalificacion")
	public PostCalificacionDTO consultaXIdPostCalificacion(@RequestBody String llave) throws FlexException {
		try {
			return postCalificacionService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosPostCalificacion")
	public int contarResultadosPostCalificacion(@RequestBody PostCalificacionFilterDTO dto) throws FlexException  {
		try {
			return postCalificacionService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaPostCalificacion")
	public PostCalificacionDTO consultaUnicaPostCalificacion(@RequestBody PostCalificacionFilterDTO dto) throws FlexException  {
		try {
			return postCalificacionService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaPostCalificacion")
	public List<PostCalificacionDTO> listarConsultaPostCalificacion(@RequestBody PostCalificacionFilterDTO dto) throws FlexException  {
		try {
			return postCalificacionService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarPostCalificacion")
	public PostCalificacionDTO activarPostCalificacion(@RequestBody PostCalificacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return postCalificacionService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarPostCalificacion")
	public PostCalificacionDTO inactivarPostCalificacion(@RequestBody PostCalificacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return postCalificacionService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarPostCalificacion")
	public PostCalificacionDTO actualizarPostCalificacion(@RequestBody PostCalificacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return postCalificacionService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarPostCalificacion")
	public PostCalificacionDTO guardarPostCalificacion(@RequestBody PostCalificacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return postCalificacionService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private WebServiceEjecucionSvc webServiceEjecucionService;
	
	@PostMapping(value="/consultaXIdWebServiceEjecucion")
	public WebServiceEjecucionDTO consultaXIdWebServiceEjecucion(@RequestBody String llave) throws FlexException {
		try {
			return webServiceEjecucionService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosWebServiceEjecucion")
	public int contarResultadosWebServiceEjecucion(@RequestBody WebServiceEjecucionFilterDTO dto) throws FlexException  {
		try {
			return webServiceEjecucionService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaWebServiceEjecucion")
	public WebServiceEjecucionDTO consultaUnicaWebServiceEjecucion(@RequestBody WebServiceEjecucionFilterDTO dto) throws FlexException  {
		try {
			return webServiceEjecucionService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaWebServiceEjecucion")
	public List<WebServiceEjecucionDTO> listarConsultaWebServiceEjecucion(@RequestBody WebServiceEjecucionFilterDTO dto) throws FlexException  {
		try {
			return webServiceEjecucionService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarWebServiceEjecucion")
	public WebServiceEjecucionDTO activarWebServiceEjecucion(@RequestBody WebServiceEjecucionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return webServiceEjecucionService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarWebServiceEjecucion")
	public WebServiceEjecucionDTO inactivarWebServiceEjecucion(@RequestBody WebServiceEjecucionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return webServiceEjecucionService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarWebServiceEjecucion")
	public WebServiceEjecucionDTO actualizarWebServiceEjecucion(@RequestBody WebServiceEjecucionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return webServiceEjecucionService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarWebServiceEjecucion")
	public WebServiceEjecucionDTO guardarWebServiceEjecucion(@RequestBody WebServiceEjecucionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return webServiceEjecucionService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@PostMapping(value="/ejecutarAPIWebServiceEjecucion")
	public WebServiceEjecucionDTO ejecutarAPIWebServiceEjecucion(@RequestBody WebServiceEjecucionFilterDTO dto)throws FlexException {
		try {
			return webServiceEjecucionService.ejecutarAPI(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private PostPreguntaSvc postPreguntaService;
	
	@PostMapping(value="/consultaXIdPostPregunta")
	public PostPreguntaDTO consultaXIdPostPregunta(@RequestBody String llave) throws FlexException {
		try {
			return postPreguntaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosPostPregunta")
	public int contarResultadosPostPregunta(@RequestBody PostPreguntaFilterDTO dto) throws FlexException  {
		try {
			return postPreguntaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaPostPregunta")
	public PostPreguntaDTO consultaUnicaPostPregunta(@RequestBody PostPreguntaFilterDTO dto) throws FlexException  {
		try {
			return postPreguntaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaPostPregunta")
	public List<PostPreguntaDTO> listarConsultaPostPregunta(@RequestBody PostPreguntaFilterDTO dto) throws FlexException  {
		try {
			return postPreguntaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarPostPregunta")
	public PostPreguntaDTO activarPostPregunta(@RequestBody PostPreguntaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return postPreguntaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarPostPregunta")
	public PostPreguntaDTO inactivarPostPregunta(@RequestBody PostPreguntaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return postPreguntaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarPostPregunta")
	public PostPreguntaDTO actualizarPostPregunta(@RequestBody PostPreguntaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return postPreguntaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarPostPregunta")
	public PostPreguntaDTO guardarPostPregunta(@RequestBody PostPreguntaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return postPreguntaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@PostMapping(value="/listarEnOrdenPostPregunta")
	public List<PostPreguntaDTO> listarEnOrdenPostPregunta(@RequestBody PostPreguntaFilterDTO dto)throws FlexException {
		try {
			return postPreguntaService.listarEnOrden(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}

	@PostMapping(value="/listarPreguntasSinRespuestaPostPregunta")
	public List<PostPreguntaDTO> listarPreguntasSinRespuestaPostPregunta(@RequestBody PostPreguntaFilterDTO dto)throws FlexException {
		try {
			return postPreguntaService.listarPreguntasSinRespuesta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private ServidorSvc servidorService;
	
	@PostMapping(value="/consultaXIdServidor")
	public ServidorDTO consultaXIdServidor(@RequestBody String llave) throws FlexException {
		try {
			return servidorService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosServidor")
	public int contarResultadosServidor(@RequestBody ServidorFilterDTO dto) throws FlexException  {
		try {
			return servidorService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaServidor")
	public ServidorDTO consultaUnicaServidor(@RequestBody ServidorFilterDTO dto) throws FlexException  {
		try {
			return servidorService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaServidor")
	public List<ServidorDTO> listarConsultaServidor(@RequestBody ServidorFilterDTO dto) throws FlexException  {
		try {
			return servidorService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarServidor")
	public ServidorDTO activarServidor(@RequestBody ServidorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return servidorService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarServidor")
	public ServidorDTO inactivarServidor(@RequestBody ServidorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return servidorService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarServidor")
	public ServidorDTO actualizarServidor(@RequestBody ServidorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return servidorService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarServidor")
	public ServidorDTO guardarServidor(@RequestBody ServidorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return servidorService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private MensajePlantillaCorreoSvc mensajePlantillaCorreoService;
	
	@PostMapping(value="/consultaXIdMensajePlantillaCorreo")
	public MensajePlantillaCorreoDTO consultaXIdMensajePlantillaCorreo(@RequestBody String llave) throws FlexException {
		try {
			return mensajePlantillaCorreoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosMensajePlantillaCorreo")
	public int contarResultadosMensajePlantillaCorreo(@RequestBody MensajePlantillaCorreoFilterDTO dto) throws FlexException  {
		try {
			return mensajePlantillaCorreoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaMensajePlantillaCorreo")
	public MensajePlantillaCorreoDTO consultaUnicaMensajePlantillaCorreo(@RequestBody MensajePlantillaCorreoFilterDTO dto) throws FlexException  {
		try {
			return mensajePlantillaCorreoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaMensajePlantillaCorreo")
	public List<MensajePlantillaCorreoDTO> listarConsultaMensajePlantillaCorreo(@RequestBody MensajePlantillaCorreoFilterDTO dto) throws FlexException  {
		try {
			return mensajePlantillaCorreoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarMensajePlantillaCorreo")
	public MensajePlantillaCorreoDTO activarMensajePlantillaCorreo(@RequestBody MensajePlantillaCorreoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return mensajePlantillaCorreoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarMensajePlantillaCorreo")
	public MensajePlantillaCorreoDTO inactivarMensajePlantillaCorreo(@RequestBody MensajePlantillaCorreoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return mensajePlantillaCorreoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarMensajePlantillaCorreo")
	public MensajePlantillaCorreoDTO actualizarMensajePlantillaCorreo(@RequestBody MensajePlantillaCorreoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return mensajePlantillaCorreoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarMensajePlantillaCorreo")
	public MensajePlantillaCorreoDTO guardarMensajePlantillaCorreo(@RequestBody MensajePlantillaCorreoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return mensajePlantillaCorreoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private GPSDispositivoSvc gPSDispositivoService;
	
	@PostMapping(value="/consultaXIdGPSDispositivo")
	public GPSDispositivoDTO consultaXIdGPSDispositivo(@RequestBody String llave) throws FlexException {
		try {
			return gPSDispositivoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosGPSDispositivo")
	public int contarResultadosGPSDispositivo(@RequestBody GPSDispositivoFilterDTO dto) throws FlexException  {
		try {
			return gPSDispositivoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaGPSDispositivo")
	public GPSDispositivoDTO consultaUnicaGPSDispositivo(@RequestBody GPSDispositivoFilterDTO dto) throws FlexException  {
		try {
			return gPSDispositivoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaGPSDispositivo")
	public List<GPSDispositivoDTO> listarConsultaGPSDispositivo(@RequestBody GPSDispositivoFilterDTO dto) throws FlexException  {
		try {
			return gPSDispositivoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarGPSDispositivo")
	public GPSDispositivoDTO activarGPSDispositivo(@RequestBody GPSDispositivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return gPSDispositivoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarGPSDispositivo")
	public GPSDispositivoDTO inactivarGPSDispositivo(@RequestBody GPSDispositivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return gPSDispositivoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarGPSDispositivo")
	public GPSDispositivoDTO actualizarGPSDispositivo(@RequestBody GPSDispositivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return gPSDispositivoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarGPSDispositivo")
	public GPSDispositivoDTO guardarGPSDispositivo(@RequestBody GPSDispositivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return gPSDispositivoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private TrazabilidadProductoInventarioSvc trazabilidadProductoInventarioService;
	
	@PostMapping(value="/consultaXIdTrazabilidadProductoInventario")
	public TrazabilidadProductoInventarioDTO consultaXIdTrazabilidadProductoInventario(@RequestBody String llave) throws FlexException {
		try {
			return trazabilidadProductoInventarioService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosTrazabilidadProductoInventario")
	public int contarResultadosTrazabilidadProductoInventario(@RequestBody TrazabilidadProductoInventarioFilterDTO dto) throws FlexException  {
		try {
			return trazabilidadProductoInventarioService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaTrazabilidadProductoInventario")
	public TrazabilidadProductoInventarioDTO consultaUnicaTrazabilidadProductoInventario(@RequestBody TrazabilidadProductoInventarioFilterDTO dto) throws FlexException  {
		try {
			return trazabilidadProductoInventarioService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaTrazabilidadProductoInventario")
	public List<TrazabilidadProductoInventarioDTO> listarConsultaTrazabilidadProductoInventario(@RequestBody TrazabilidadProductoInventarioFilterDTO dto) throws FlexException  {
		try {
			return trazabilidadProductoInventarioService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarTrazabilidadProductoInventario")
	public TrazabilidadProductoInventarioDTO activarTrazabilidadProductoInventario(@RequestBody TrazabilidadProductoInventarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return trazabilidadProductoInventarioService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarTrazabilidadProductoInventario")
	public TrazabilidadProductoInventarioDTO inactivarTrazabilidadProductoInventario(@RequestBody TrazabilidadProductoInventarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return trazabilidadProductoInventarioService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarTrazabilidadProductoInventario")
	public TrazabilidadProductoInventarioDTO actualizarTrazabilidadProductoInventario(@RequestBody TrazabilidadProductoInventarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return trazabilidadProductoInventarioService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarTrazabilidadProductoInventario")
	public TrazabilidadProductoInventarioDTO guardarTrazabilidadProductoInventario(@RequestBody TrazabilidadProductoInventarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return trazabilidadProductoInventarioService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private DetallePedidoVentaSvc detallePedidoVentaService;
	
	@PostMapping(value="/consultaXIdDetallePedidoVenta")
	public DetallePedidoVentaDTO consultaXIdDetallePedidoVenta(@RequestBody String llave) throws FlexException {
		try {
			return detallePedidoVentaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosDetallePedidoVenta")
	public int contarResultadosDetallePedidoVenta(@RequestBody DetallePedidoVentaFilterDTO dto) throws FlexException  {
		try {
			return detallePedidoVentaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaDetallePedidoVenta")
	public DetallePedidoVentaDTO consultaUnicaDetallePedidoVenta(@RequestBody DetallePedidoVentaFilterDTO dto) throws FlexException  {
		try {
			return detallePedidoVentaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaDetallePedidoVenta")
	public List<DetallePedidoVentaDTO> listarConsultaDetallePedidoVenta(@RequestBody DetallePedidoVentaFilterDTO dto) throws FlexException  {
		try {
			return detallePedidoVentaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarDetallePedidoVenta")
	public DetallePedidoVentaDTO activarDetallePedidoVenta(@RequestBody DetallePedidoVentaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return detallePedidoVentaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarDetallePedidoVenta")
	public DetallePedidoVentaDTO inactivarDetallePedidoVenta(@RequestBody DetallePedidoVentaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return detallePedidoVentaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarDetallePedidoVenta")
	public DetallePedidoVentaDTO actualizarDetallePedidoVenta(@RequestBody DetallePedidoVentaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return detallePedidoVentaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarDetallePedidoVenta")
	public DetallePedidoVentaDTO guardarDetallePedidoVenta(@RequestBody DetallePedidoVentaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return detallePedidoVentaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private CategoriaProductoSvc categoriaProductoService;
	
	@PostMapping(value="/consultaXIdCategoriaProducto")
	public CategoriaProductoDTO consultaXIdCategoriaProducto(@RequestBody String llave) throws FlexException {
		try {
			return categoriaProductoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosCategoriaProducto")
	public int contarResultadosCategoriaProducto(@RequestBody CategoriaProductoFilterDTO dto) throws FlexException  {
		try {
			return categoriaProductoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaCategoriaProducto")
	public CategoriaProductoDTO consultaUnicaCategoriaProducto(@RequestBody CategoriaProductoFilterDTO dto) throws FlexException  {
		try {
			return categoriaProductoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaCategoriaProducto")
	public List<CategoriaProductoDTO> listarConsultaCategoriaProducto(@RequestBody CategoriaProductoFilterDTO dto) throws FlexException  {
		try {
			return categoriaProductoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarCategoriaProducto")
	public CategoriaProductoDTO activarCategoriaProducto(@RequestBody CategoriaProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return categoriaProductoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarCategoriaProducto")
	public CategoriaProductoDTO inactivarCategoriaProducto(@RequestBody CategoriaProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return categoriaProductoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarCategoriaProducto")
	public CategoriaProductoDTO actualizarCategoriaProducto(@RequestBody CategoriaProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return categoriaProductoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarCategoriaProducto")
	public CategoriaProductoDTO guardarCategoriaProducto(@RequestBody CategoriaProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return categoriaProductoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private ProductoInventarioSvc productoInventarioService;
	
	@PostMapping(value="/consultaXIdProductoInventario")
	public ProductoInventarioDTO consultaXIdProductoInventario(@RequestBody String llave) throws FlexException {
		try {
			return productoInventarioService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosProductoInventario")
	public int contarResultadosProductoInventario(@RequestBody ProductoInventarioFilterDTO dto) throws FlexException  {
		try {
			return productoInventarioService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaProductoInventario")
	public ProductoInventarioDTO consultaUnicaProductoInventario(@RequestBody ProductoInventarioFilterDTO dto) throws FlexException  {
		try {
			return productoInventarioService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaProductoInventario")
	public List<ProductoInventarioDTO> listarConsultaProductoInventario(@RequestBody ProductoInventarioFilterDTO dto) throws FlexException  {
		try {
			return productoInventarioService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarProductoInventario")
	public ProductoInventarioDTO activarProductoInventario(@RequestBody ProductoInventarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoInventarioService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarProductoInventario")
	public ProductoInventarioDTO inactivarProductoInventario(@RequestBody ProductoInventarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoInventarioService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarProductoInventario")
	public ProductoInventarioDTO actualizarProductoInventario(@RequestBody ProductoInventarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoInventarioService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarProductoInventario")
	public ProductoInventarioDTO guardarProductoInventario(@RequestBody ProductoInventarioDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoInventarioService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private ProductoCaracteristicaSvc productoCaracteristicaService;
	
	@PostMapping(value="/consultaXIdProductoCaracteristica")
	public ProductoCaracteristicaDTO consultaXIdProductoCaracteristica(@RequestBody String llave) throws FlexException {
		try {
			return productoCaracteristicaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosProductoCaracteristica")
	public int contarResultadosProductoCaracteristica(@RequestBody ProductoCaracteristicaFilterDTO dto) throws FlexException  {
		try {
			return productoCaracteristicaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaProductoCaracteristica")
	public ProductoCaracteristicaDTO consultaUnicaProductoCaracteristica(@RequestBody ProductoCaracteristicaFilterDTO dto) throws FlexException  {
		try {
			return productoCaracteristicaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaProductoCaracteristica")
	public List<ProductoCaracteristicaDTO> listarConsultaProductoCaracteristica(@RequestBody ProductoCaracteristicaFilterDTO dto) throws FlexException  {
		try {
			return productoCaracteristicaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarProductoCaracteristica")
	public ProductoCaracteristicaDTO activarProductoCaracteristica(@RequestBody ProductoCaracteristicaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoCaracteristicaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarProductoCaracteristica")
	public ProductoCaracteristicaDTO inactivarProductoCaracteristica(@RequestBody ProductoCaracteristicaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoCaracteristicaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarProductoCaracteristica")
	public ProductoCaracteristicaDTO actualizarProductoCaracteristica(@RequestBody ProductoCaracteristicaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoCaracteristicaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarProductoCaracteristica")
	public ProductoCaracteristicaDTO guardarProductoCaracteristica(@RequestBody ProductoCaracteristicaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoCaracteristicaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private UsuarioRolProductoSvc usuarioRolProductoService;
	
	@PostMapping(value="/consultaXIdUsuarioRolProducto")
	public UsuarioRolProductoDTO consultaXIdUsuarioRolProducto(@RequestBody String llave) throws FlexException {
		try {
			return usuarioRolProductoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosUsuarioRolProducto")
	public int contarResultadosUsuarioRolProducto(@RequestBody UsuarioRolProductoFilterDTO dto) throws FlexException  {
		try {
			return usuarioRolProductoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaUsuarioRolProducto")
	public UsuarioRolProductoDTO consultaUnicaUsuarioRolProducto(@RequestBody UsuarioRolProductoFilterDTO dto) throws FlexException  {
		try {
			return usuarioRolProductoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaUsuarioRolProducto")
	public List<UsuarioRolProductoDTO> listarConsultaUsuarioRolProducto(@RequestBody UsuarioRolProductoFilterDTO dto) throws FlexException  {
		try {
			return usuarioRolProductoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarUsuarioRolProducto")
	public UsuarioRolProductoDTO activarUsuarioRolProducto(@RequestBody UsuarioRolProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioRolProductoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarUsuarioRolProducto")
	public UsuarioRolProductoDTO inactivarUsuarioRolProducto(@RequestBody UsuarioRolProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioRolProductoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarUsuarioRolProducto")
	public UsuarioRolProductoDTO actualizarUsuarioRolProducto(@RequestBody UsuarioRolProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioRolProductoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarUsuarioRolProducto")
	public UsuarioRolProductoDTO guardarUsuarioRolProducto(@RequestBody UsuarioRolProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioRolProductoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private DetalleCaracteristicaProductoSvc detalleCaracteristicaProductoService;
	
	@PostMapping(value="/consultaXIdDetalleCaracteristicaProducto")
	public DetalleCaracteristicaProductoDTO consultaXIdDetalleCaracteristicaProducto(@RequestBody String llave) throws FlexException {
		try {
			return detalleCaracteristicaProductoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosDetalleCaracteristicaProducto")
	public int contarResultadosDetalleCaracteristicaProducto(@RequestBody DetalleCaracteristicaProductoFilterDTO dto) throws FlexException  {
		try {
			return detalleCaracteristicaProductoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaDetalleCaracteristicaProducto")
	public DetalleCaracteristicaProductoDTO consultaUnicaDetalleCaracteristicaProducto(@RequestBody DetalleCaracteristicaProductoFilterDTO dto) throws FlexException  {
		try {
			return detalleCaracteristicaProductoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaDetalleCaracteristicaProducto")
	public List<DetalleCaracteristicaProductoDTO> listarConsultaDetalleCaracteristicaProducto(@RequestBody DetalleCaracteristicaProductoFilterDTO dto) throws FlexException  {
		try {
			return detalleCaracteristicaProductoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarDetalleCaracteristicaProducto")
	public DetalleCaracteristicaProductoDTO activarDetalleCaracteristicaProducto(@RequestBody DetalleCaracteristicaProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return detalleCaracteristicaProductoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarDetalleCaracteristicaProducto")
	public DetalleCaracteristicaProductoDTO inactivarDetalleCaracteristicaProducto(@RequestBody DetalleCaracteristicaProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return detalleCaracteristicaProductoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarDetalleCaracteristicaProducto")
	public DetalleCaracteristicaProductoDTO actualizarDetalleCaracteristicaProducto(@RequestBody DetalleCaracteristicaProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return detalleCaracteristicaProductoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarDetalleCaracteristicaProducto")
	public DetalleCaracteristicaProductoDTO guardarDetalleCaracteristicaProducto(@RequestBody DetalleCaracteristicaProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return detalleCaracteristicaProductoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private BodegaSvc bodegaService;
	
	@PostMapping(value="/consultaXIdBodega")
	public BodegaDTO consultaXIdBodega(@RequestBody String llave) throws FlexException {
		try {
			return bodegaService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosBodega")
	public int contarResultadosBodega(@RequestBody BodegaFilterDTO dto) throws FlexException  {
		try {
			return bodegaService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaBodega")
	public BodegaDTO consultaUnicaBodega(@RequestBody BodegaFilterDTO dto) throws FlexException  {
		try {
			return bodegaService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaBodega")
	public List<BodegaDTO> listarConsultaBodega(@RequestBody BodegaFilterDTO dto) throws FlexException  {
		try {
			return bodegaService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarBodega")
	public BodegaDTO activarBodega(@RequestBody BodegaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return bodegaService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarBodega")
	public BodegaDTO inactivarBodega(@RequestBody BodegaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return bodegaService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarBodega")
	public BodegaDTO actualizarBodega(@RequestBody BodegaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return bodegaService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarBodega")
	public BodegaDTO guardarBodega(@RequestBody BodegaDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return bodegaService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private ProductoSvc productoService;
	
	@PostMapping(value="/consultaXIdProducto")
	public ProductoDTO consultaXIdProducto(@RequestBody String llave) throws FlexException {
		try {
			return productoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosProducto")
	public int contarResultadosProducto(@RequestBody ProductoFilterDTO dto) throws FlexException  {
		try {
			return productoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaProducto")
	public ProductoDTO consultaUnicaProducto(@RequestBody ProductoFilterDTO dto) throws FlexException  {
		try {
			return productoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaProducto")
	public List<ProductoDTO> listarConsultaProducto(@RequestBody ProductoFilterDTO dto) throws FlexException  {
		try {
			return productoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarProducto")
	public ProductoDTO activarProducto(@RequestBody ProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarProducto")
	public ProductoDTO inactivarProducto(@RequestBody ProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarProducto")
	public ProductoDTO actualizarProducto(@RequestBody ProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarProducto")
	public ProductoDTO guardarProducto(@RequestBody ProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private ProductoInventarioDescuentoSvc productoInventarioDescuentoService;
	
	@PostMapping(value="/consultaXIdProductoInventarioDescuento")
	public ProductoInventarioDescuentoDTO consultaXIdProductoInventarioDescuento(@RequestBody String llave) throws FlexException {
		try {
			return productoInventarioDescuentoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosProductoInventarioDescuento")
	public int contarResultadosProductoInventarioDescuento(@RequestBody ProductoInventarioDescuentoFilterDTO dto) throws FlexException  {
		try {
			return productoInventarioDescuentoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaProductoInventarioDescuento")
	public ProductoInventarioDescuentoDTO consultaUnicaProductoInventarioDescuento(@RequestBody ProductoInventarioDescuentoFilterDTO dto) throws FlexException  {
		try {
			return productoInventarioDescuentoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaProductoInventarioDescuento")
	public List<ProductoInventarioDescuentoDTO> listarConsultaProductoInventarioDescuento(@RequestBody ProductoInventarioDescuentoFilterDTO dto) throws FlexException  {
		try {
			return productoInventarioDescuentoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarProductoInventarioDescuento")
	public ProductoInventarioDescuentoDTO activarProductoInventarioDescuento(@RequestBody ProductoInventarioDescuentoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoInventarioDescuentoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarProductoInventarioDescuento")
	public ProductoInventarioDescuentoDTO inactivarProductoInventarioDescuento(@RequestBody ProductoInventarioDescuentoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoInventarioDescuentoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarProductoInventarioDescuento")
	public ProductoInventarioDescuentoDTO actualizarProductoInventarioDescuento(@RequestBody ProductoInventarioDescuentoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoInventarioDescuentoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarProductoInventarioDescuento")
	public ProductoInventarioDescuentoDTO guardarProductoInventarioDescuento(@RequestBody ProductoInventarioDescuentoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return productoInventarioDescuentoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private DeduccionProductoSvc deduccionProductoService;
	
	@PostMapping(value="/consultaXIdDeduccionProducto")
	public DeduccionProductoDTO consultaXIdDeduccionProducto(@RequestBody String llave) throws FlexException {
		try {
			return deduccionProductoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosDeduccionProducto")
	public int contarResultadosDeduccionProducto(@RequestBody DeduccionProductoFilterDTO dto) throws FlexException  {
		try {
			return deduccionProductoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaDeduccionProducto")
	public DeduccionProductoDTO consultaUnicaDeduccionProducto(@RequestBody DeduccionProductoFilterDTO dto) throws FlexException  {
		try {
			return deduccionProductoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaDeduccionProducto")
	public List<DeduccionProductoDTO> listarConsultaDeduccionProducto(@RequestBody DeduccionProductoFilterDTO dto) throws FlexException  {
		try {
			return deduccionProductoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarDeduccionProducto")
	public DeduccionProductoDTO activarDeduccionProducto(@RequestBody DeduccionProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return deduccionProductoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarDeduccionProducto")
	public DeduccionProductoDTO inactivarDeduccionProducto(@RequestBody DeduccionProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return deduccionProductoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarDeduccionProducto")
	public DeduccionProductoDTO actualizarDeduccionProducto(@RequestBody DeduccionProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return deduccionProductoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarDeduccionProducto")
	public DeduccionProductoDTO guardarDeduccionProducto(@RequestBody DeduccionProductoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return deduccionProductoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private ModuloSvc moduloService;
	
	@PostMapping(value="/consultaXIdModulo")
	public ModuloDTO consultaXIdModulo(@RequestBody String llave) throws FlexException {
		try {
			return moduloService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosModulo")
	public int contarResultadosModulo(@RequestBody ModuloFilterDTO dto) throws FlexException  {
		try {
			return moduloService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaModulo")
	public ModuloDTO consultaUnicaModulo(@RequestBody ModuloFilterDTO dto) throws FlexException  {
		try {
			return moduloService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaModulo")
	public List<ModuloDTO> listarConsultaModulo(@RequestBody ModuloFilterDTO dto) throws FlexException  {
		try {
			return moduloService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarModulo")
	public ModuloDTO activarModulo(@RequestBody ModuloDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return moduloService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarModulo")
	public ModuloDTO inactivarModulo(@RequestBody ModuloDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return moduloService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarModulo")
	public ModuloDTO actualizarModulo(@RequestBody ModuloDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return moduloService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarModulo")
	public ModuloDTO guardarModulo(@RequestBody ModuloDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return moduloService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private ReporteBaseSvc reporteBaseService;
	
	@PostMapping(value="/consultaXIdReporteBase")
	public ReporteBaseDTO consultaXIdReporteBase(@RequestBody String llave) throws FlexException {
		try {
			return reporteBaseService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosReporteBase")
	public int contarResultadosReporteBase(@RequestBody ReporteBaseFilterDTO dto) throws FlexException  {
		try {
			return reporteBaseService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaReporteBase")
	public ReporteBaseDTO consultaUnicaReporteBase(@RequestBody ReporteBaseFilterDTO dto) throws FlexException  {
		try {
			return reporteBaseService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaReporteBase")
	public List<ReporteBaseDTO> listarConsultaReporteBase(@RequestBody ReporteBaseFilterDTO dto) throws FlexException  {
		try {
			return reporteBaseService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarReporteBase")
	public ReporteBaseDTO activarReporteBase(@RequestBody ReporteBaseDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return reporteBaseService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarReporteBase")
	public ReporteBaseDTO inactivarReporteBase(@RequestBody ReporteBaseDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return reporteBaseService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarReporteBase")
	public ReporteBaseDTO actualizarReporteBase(@RequestBody ReporteBaseDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return reporteBaseService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarReporteBase")
	public ReporteBaseDTO guardarReporteBase(@RequestBody ReporteBaseDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return reporteBaseService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private UsuarioOrganizacionSvc usuarioOrganizacionService;
	
	@PostMapping(value="/consultaXIdUsuarioOrganizacion")
	public UsuarioOrganizacionDTO consultaXIdUsuarioOrganizacion(@RequestBody String llave) throws FlexException {
		try {
			return usuarioOrganizacionService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosUsuarioOrganizacion")
	public int contarResultadosUsuarioOrganizacion(@RequestBody UsuarioOrganizacionFilterDTO dto) throws FlexException  {
		try {
			return usuarioOrganizacionService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaUsuarioOrganizacion")
	public UsuarioOrganizacionDTO consultaUnicaUsuarioOrganizacion(@RequestBody UsuarioOrganizacionFilterDTO dto) throws FlexException  {
		try {
			return usuarioOrganizacionService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaUsuarioOrganizacion")
	public List<UsuarioOrganizacionDTO> listarConsultaUsuarioOrganizacion(@RequestBody UsuarioOrganizacionFilterDTO dto) throws FlexException  {
		try {
			return usuarioOrganizacionService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarUsuarioOrganizacion")
	public UsuarioOrganizacionDTO activarUsuarioOrganizacion(@RequestBody UsuarioOrganizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioOrganizacionService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarUsuarioOrganizacion")
	public UsuarioOrganizacionDTO inactivarUsuarioOrganizacion(@RequestBody UsuarioOrganizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioOrganizacionService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarUsuarioOrganizacion")
	public UsuarioOrganizacionDTO actualizarUsuarioOrganizacion(@RequestBody UsuarioOrganizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioOrganizacionService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarUsuarioOrganizacion")
	public UsuarioOrganizacionDTO guardarUsuarioOrganizacion(@RequestBody UsuarioOrganizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioOrganizacionService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@PostMapping(value="/sincronizarUsuariosUsuarioOrganizacion")
	public List<UsuarioOrganizacionDTO> sincronizarUsuariosUsuarioOrganizacion(@RequestBody UsuarioOrganizacionDTO dto, @RequestHeader("Authorization") String token)throws FlexException {
		try {
			return usuarioOrganizacionService.sincronizarUsuarios(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private PermisoSvc permisoService;
	
	@PostMapping(value="/consultaXIdPermiso")
	public PermisoDTO consultaXIdPermiso(@RequestBody String llave) throws FlexException {
		try {
			return permisoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosPermiso")
	public int contarResultadosPermiso(@RequestBody PermisoFilterDTO dto) throws FlexException  {
		try {
			return permisoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaPermiso")
	public PermisoDTO consultaUnicaPermiso(@RequestBody PermisoFilterDTO dto) throws FlexException  {
		try {
			return permisoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaPermiso")
	public List<PermisoDTO> listarConsultaPermiso(@RequestBody PermisoFilterDTO dto) throws FlexException  {
		try {
			return permisoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarPermiso")
	public PermisoDTO activarPermiso(@RequestBody PermisoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return permisoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarPermiso")
	public PermisoDTO inactivarPermiso(@RequestBody PermisoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return permisoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarPermiso")
	public PermisoDTO actualizarPermiso(@RequestBody PermisoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return permisoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarPermiso")
	public PermisoDTO guardarPermiso(@RequestBody PermisoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return permisoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private UsuarioAutenticacionAutorizacionSvc usuarioAutenticacionAutorizacionService;
	
	@PostMapping(value="/consultaXIdUsuarioAutenticacionAutorizacion")
	public UsuarioAutenticacionAutorizacionDTO consultaXIdUsuarioAutenticacionAutorizacion(@RequestBody String llave) throws FlexException {
		try {
			return usuarioAutenticacionAutorizacionService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosUsuarioAutenticacionAutorizacion")
	public int contarResultadosUsuarioAutenticacionAutorizacion(@RequestBody UsuarioAutenticacionAutorizacionFilterDTO dto) throws FlexException  {
		try {
			return usuarioAutenticacionAutorizacionService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaUsuarioAutenticacionAutorizacion")
	public UsuarioAutenticacionAutorizacionDTO consultaUnicaUsuarioAutenticacionAutorizacion(@RequestBody UsuarioAutenticacionAutorizacionFilterDTO dto) throws FlexException  {
		try {
			return usuarioAutenticacionAutorizacionService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaUsuarioAutenticacionAutorizacion")
	public List<UsuarioAutenticacionAutorizacionDTO> listarConsultaUsuarioAutenticacionAutorizacion(@RequestBody UsuarioAutenticacionAutorizacionFilterDTO dto) throws FlexException  {
		try {
			return usuarioAutenticacionAutorizacionService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarUsuarioAutenticacionAutorizacion")
	public UsuarioAutenticacionAutorizacionDTO activarUsuarioAutenticacionAutorizacion(@RequestBody UsuarioAutenticacionAutorizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioAutenticacionAutorizacionService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarUsuarioAutenticacionAutorizacion")
	public UsuarioAutenticacionAutorizacionDTO inactivarUsuarioAutenticacionAutorizacion(@RequestBody UsuarioAutenticacionAutorizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioAutenticacionAutorizacionService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarUsuarioAutenticacionAutorizacion")
	public UsuarioAutenticacionAutorizacionDTO actualizarUsuarioAutenticacionAutorizacion(@RequestBody UsuarioAutenticacionAutorizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioAutenticacionAutorizacionService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarUsuarioAutenticacionAutorizacion")
	public UsuarioAutenticacionAutorizacionDTO guardarUsuarioAutenticacionAutorizacion(@RequestBody UsuarioAutenticacionAutorizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioAutenticacionAutorizacionService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private UsuarioSesionSvc usuarioSesionService;
	
	@PostMapping(value="/consultaXIdUsuarioSesion")
	public UsuarioSesionDTO consultaXIdUsuarioSesion(@RequestBody String llave) throws FlexException {
		try {
			return usuarioSesionService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosUsuarioSesion")
	public int contarResultadosUsuarioSesion(@RequestBody UsuarioSesionFilterDTO dto) throws FlexException  {
		try {
			return usuarioSesionService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaUsuarioSesion")
	public UsuarioSesionDTO consultaUnicaUsuarioSesion(@RequestBody UsuarioSesionFilterDTO dto) throws FlexException  {
		try {
			return usuarioSesionService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaUsuarioSesion")
	public List<UsuarioSesionDTO> listarConsultaUsuarioSesion(@RequestBody UsuarioSesionFilterDTO dto) throws FlexException  {
		try {
			return usuarioSesionService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarUsuarioSesion")
	public UsuarioSesionDTO activarUsuarioSesion(@RequestBody UsuarioSesionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioSesionService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarUsuarioSesion")
	public UsuarioSesionDTO inactivarUsuarioSesion(@RequestBody UsuarioSesionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioSesionService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarUsuarioSesion")
	public UsuarioSesionDTO actualizarUsuarioSesion(@RequestBody UsuarioSesionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioSesionService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarUsuarioSesion")
	public UsuarioSesionDTO guardarUsuarioSesion(@RequestBody UsuarioSesionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioSesionService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private CargaArchivoSvc cargaArchivoService;
	
	@PostMapping(value="/consultaXIdCargaArchivo")
	public CargaArchivoDTO consultaXIdCargaArchivo(@RequestBody String llave) throws FlexException {
		try {
			return cargaArchivoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosCargaArchivo")
	public int contarResultadosCargaArchivo(@RequestBody CargaArchivoFilterDTO dto) throws FlexException  {
		try {
			return cargaArchivoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaCargaArchivo")
	public CargaArchivoDTO consultaUnicaCargaArchivo(@RequestBody CargaArchivoFilterDTO dto) throws FlexException  {
		try {
			return cargaArchivoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaCargaArchivo")
	public List<CargaArchivoDTO> listarConsultaCargaArchivo(@RequestBody CargaArchivoFilterDTO dto) throws FlexException  {
		try {
			return cargaArchivoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarCargaArchivo")
	public CargaArchivoDTO activarCargaArchivo(@RequestBody CargaArchivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cargaArchivoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarCargaArchivo")
	public CargaArchivoDTO inactivarCargaArchivo(@RequestBody CargaArchivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cargaArchivoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarCargaArchivo")
	public CargaArchivoDTO actualizarCargaArchivo(@RequestBody CargaArchivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cargaArchivoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarCargaArchivo")
	public CargaArchivoDTO guardarCargaArchivo(@RequestBody CargaArchivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return cargaArchivoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private ReporteEjecucionSvc reporteEjecucionService;
	
	@PostMapping(value="/consultaXIdReporteEjecucion")
	public ReporteEjecucionDTO consultaXIdReporteEjecucion(@RequestBody String llave) throws FlexException {
		try {
			return reporteEjecucionService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosReporteEjecucion")
	public int contarResultadosReporteEjecucion(@RequestBody ReporteEjecucionFilterDTO dto) throws FlexException  {
		try {
			return reporteEjecucionService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaReporteEjecucion")
	public ReporteEjecucionDTO consultaUnicaReporteEjecucion(@RequestBody ReporteEjecucionFilterDTO dto) throws FlexException  {
		try {
			return reporteEjecucionService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaReporteEjecucion")
	public List<ReporteEjecucionDTO> listarConsultaReporteEjecucion(@RequestBody ReporteEjecucionFilterDTO dto) throws FlexException  {
		try {
			return reporteEjecucionService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarReporteEjecucion")
	public ReporteEjecucionDTO activarReporteEjecucion(@RequestBody ReporteEjecucionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return reporteEjecucionService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarReporteEjecucion")
	public ReporteEjecucionDTO inactivarReporteEjecucion(@RequestBody ReporteEjecucionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return reporteEjecucionService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarReporteEjecucion")
	public ReporteEjecucionDTO actualizarReporteEjecucion(@RequestBody ReporteEjecucionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return reporteEjecucionService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarReporteEjecucion")
	public ReporteEjecucionDTO guardarReporteEjecucion(@RequestBody ReporteEjecucionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return reporteEjecucionService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private ConsecutivoSvc consecutivoService;
	
	@PostMapping(value="/consultaXIdConsecutivo")
	public ConsecutivoDTO consultaXIdConsecutivo(@RequestBody String llave) throws FlexException {
		try {
			return consecutivoService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosConsecutivo")
	public int contarResultadosConsecutivo(@RequestBody ConsecutivoFilterDTO dto) throws FlexException  {
		try {
			return consecutivoService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaConsecutivo")
	public ConsecutivoDTO consultaUnicaConsecutivo(@RequestBody ConsecutivoFilterDTO dto) throws FlexException  {
		try {
			return consecutivoService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaConsecutivo")
	public List<ConsecutivoDTO> listarConsultaConsecutivo(@RequestBody ConsecutivoFilterDTO dto) throws FlexException  {
		try {
			return consecutivoService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarConsecutivo")
	public ConsecutivoDTO activarConsecutivo(@RequestBody ConsecutivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return consecutivoService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarConsecutivo")
	public ConsecutivoDTO inactivarConsecutivo(@RequestBody ConsecutivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return consecutivoService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarConsecutivo")
	public ConsecutivoDTO actualizarConsecutivo(@RequestBody ConsecutivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return consecutivoService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarConsecutivo")
	public ConsecutivoDTO guardarConsecutivo(@RequestBody ConsecutivoDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return consecutivoService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@PostMapping(value="/asignarConsecutivoConsecutivo")
	public ConsecutivoDTO asignarConsecutivoConsecutivo(@RequestBody ConsecutivoDTO dto, @RequestHeader("Authorization") String token)throws FlexException {
		try {
			return consecutivoService.asignarConsecutivo(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@Autowired private TransaccionLogSvc transaccionLogService;
	
	@PostMapping(value="/consultaXIdTransaccionLog")
	public TransaccionLogDTO consultaXIdTransaccionLog(@RequestBody String llave) throws FlexException {
		try {
			return transaccionLogService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosTransaccionLog")
	public int contarResultadosTransaccionLog(@RequestBody TransaccionLogFilterDTO dto) throws FlexException  {
		try {
			return transaccionLogService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaTransaccionLog")
	public TransaccionLogDTO consultaUnicaTransaccionLog(@RequestBody TransaccionLogFilterDTO dto) throws FlexException  {
		try {
			return transaccionLogService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaTransaccionLog")
	public List<TransaccionLogDTO> listarConsultaTransaccionLog(@RequestBody TransaccionLogFilterDTO dto) throws FlexException  {
		try {
			return transaccionLogService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarTransaccionLog")
	public TransaccionLogDTO activarTransaccionLog(@RequestBody TransaccionLogDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return transaccionLogService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarTransaccionLog")
	public TransaccionLogDTO inactivarTransaccionLog(@RequestBody TransaccionLogDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return transaccionLogService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarTransaccionLog")
	public TransaccionLogDTO actualizarTransaccionLog(@RequestBody TransaccionLogDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return transaccionLogService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarTransaccionLog")
	public TransaccionLogDTO guardarTransaccionLog(@RequestBody TransaccionLogDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return transaccionLogService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private UsuarioSesionErrorSvc usuarioSesionErrorService;
	
	@PostMapping(value="/consultaXIdUsuarioSesionError")
	public UsuarioSesionErrorDTO consultaXIdUsuarioSesionError(@RequestBody String llave) throws FlexException {
		try {
			return usuarioSesionErrorService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosUsuarioSesionError")
	public int contarResultadosUsuarioSesionError(@RequestBody UsuarioSesionErrorFilterDTO dto) throws FlexException  {
		try {
			return usuarioSesionErrorService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaUsuarioSesionError")
	public UsuarioSesionErrorDTO consultaUnicaUsuarioSesionError(@RequestBody UsuarioSesionErrorFilterDTO dto) throws FlexException  {
		try {
			return usuarioSesionErrorService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaUsuarioSesionError")
	public List<UsuarioSesionErrorDTO> listarConsultaUsuarioSesionError(@RequestBody UsuarioSesionErrorFilterDTO dto) throws FlexException  {
		try {
			return usuarioSesionErrorService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarUsuarioSesionError")
	public UsuarioSesionErrorDTO activarUsuarioSesionError(@RequestBody UsuarioSesionErrorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioSesionErrorService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarUsuarioSesionError")
	public UsuarioSesionErrorDTO inactivarUsuarioSesionError(@RequestBody UsuarioSesionErrorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioSesionErrorService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarUsuarioSesionError")
	public UsuarioSesionErrorDTO actualizarUsuarioSesionError(@RequestBody UsuarioSesionErrorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioSesionErrorService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarUsuarioSesionError")
	public UsuarioSesionErrorDTO guardarUsuarioSesionError(@RequestBody UsuarioSesionErrorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioSesionErrorService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private UsuarioAutenticacionSvc usuarioAutenticacionService;
	
	@PostMapping(value="/consultaXIdUsuarioAutenticacion")
	public UsuarioAutenticacionDTO consultaXIdUsuarioAutenticacion(@RequestBody String llave) throws FlexException {
		try {
			return usuarioAutenticacionService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosUsuarioAutenticacion")
	public int contarResultadosUsuarioAutenticacion(@RequestBody UsuarioAutenticacionFilterDTO dto) throws FlexException  {
		try {
			return usuarioAutenticacionService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaUsuarioAutenticacion")
	public UsuarioAutenticacionDTO consultaUnicaUsuarioAutenticacion(@RequestBody UsuarioAutenticacionFilterDTO dto) throws FlexException  {
		try {
			return usuarioAutenticacionService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaUsuarioAutenticacion")
	public List<UsuarioAutenticacionDTO> listarConsultaUsuarioAutenticacion(@RequestBody UsuarioAutenticacionFilterDTO dto) throws FlexException  {
		try {
			return usuarioAutenticacionService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarUsuarioAutenticacion")
	public UsuarioAutenticacionDTO activarUsuarioAutenticacion(@RequestBody UsuarioAutenticacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioAutenticacionService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarUsuarioAutenticacion")
	public UsuarioAutenticacionDTO inactivarUsuarioAutenticacion(@RequestBody UsuarioAutenticacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioAutenticacionService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarUsuarioAutenticacion")
	public UsuarioAutenticacionDTO actualizarUsuarioAutenticacion(@RequestBody UsuarioAutenticacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioAutenticacionService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarUsuarioAutenticacion")
	public UsuarioAutenticacionDTO guardarUsuarioAutenticacion(@RequestBody UsuarioAutenticacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return usuarioAutenticacionService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@PostMapping(value="/autenticarUsuarioAutenticacion")
	public UsuarioAutenticacionDTO autenticarUsuarioAutenticacion(@RequestBody UsuarioAutenticacionFilterDTO dto)throws FlexException {
		try {
			return usuarioAutenticacionService.autenticar(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}

	@PostMapping(value="/cambiarClaveUsuarioAutenticacion")
	public UsuarioAutenticacionDTO cambiarClaveUsuarioAutenticacion(@RequestBody UsuarioAutenticacionDTO dto, @RequestHeader("Authorization") String token)throws FlexException {
		try {
			return usuarioAutenticacionService.cambiarClave(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private TransaccionErrorSvc transaccionErrorService;
	
	@PostMapping(value="/consultaXIdTransaccionError")
	public TransaccionErrorDTO consultaXIdTransaccionError(@RequestBody String llave) throws FlexException {
		try {
			return transaccionErrorService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosTransaccionError")
	public int contarResultadosTransaccionError(@RequestBody TransaccionErrorFilterDTO dto) throws FlexException  {
		try {
			return transaccionErrorService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaTransaccionError")
	public TransaccionErrorDTO consultaUnicaTransaccionError(@RequestBody TransaccionErrorFilterDTO dto) throws FlexException  {
		try {
			return transaccionErrorService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaTransaccionError")
	public List<TransaccionErrorDTO> listarConsultaTransaccionError(@RequestBody TransaccionErrorFilterDTO dto) throws FlexException  {
		try {
			return transaccionErrorService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarTransaccionError")
	public TransaccionErrorDTO activarTransaccionError(@RequestBody TransaccionErrorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return transaccionErrorService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarTransaccionError")
	public TransaccionErrorDTO inactivarTransaccionError(@RequestBody TransaccionErrorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return transaccionErrorService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarTransaccionError")
	public TransaccionErrorDTO actualizarTransaccionError(@RequestBody TransaccionErrorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return transaccionErrorService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarTransaccionError")
	public TransaccionErrorDTO guardarTransaccionError(@RequestBody TransaccionErrorDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return transaccionErrorService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired private OrganizacionSvc organizacionService;
	
	@PostMapping(value="/consultaXIdOrganizacion")
	public OrganizacionDTO consultaXIdOrganizacion(@RequestBody String llave) throws FlexException {
		try {
			return organizacionService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/contarResultadosOrganizacion")
	public int contarResultadosOrganizacion(@RequestBody OrganizacionFilterDTO dto) throws FlexException  {
		try {
			return organizacionService.contarResultados(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/consultaUnicaOrganizacion")
	public OrganizacionDTO consultaUnicaOrganizacion(@RequestBody OrganizacionFilterDTO dto) throws FlexException  {
		try {
			return organizacionService.consultaUnica(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/listarConsultaOrganizacion")
	public List<OrganizacionDTO> listarConsultaOrganizacion(@RequestBody OrganizacionFilterDTO dto) throws FlexException  {
		try {
			return organizacionService.listarConsulta(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/activarOrganizacion")
	public OrganizacionDTO activarOrganizacion(@RequestBody OrganizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return organizacionService.activar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/inactivarOrganizacion")
	public OrganizacionDTO inactivarOrganizacion(@RequestBody OrganizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return organizacionService.inactivar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/actualizarOrganizacion")
	public OrganizacionDTO actualizarOrganizacion(@RequestBody OrganizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return organizacionService.actualizar(dto, token);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/guardarOrganizacion")
	public OrganizacionDTO guardarOrganizacion(@RequestBody OrganizacionDTO dto, @RequestHeader("Authorization") String token) throws FlexException  {
		try {
			return organizacionService.guardar(dto, token);		
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	

	@PostMapping(value="/obtenerPrincipalOrganizacion")
	public OrganizacionDTO obtenerPrincipalOrganizacion(@RequestBody OrganizacionFilterDTO dto)throws FlexException {
		try {
			return organizacionService.obtenerPrincipal();
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	@PostMapping(value="/modulosUsuarioModuloContratado")
	public List<ModuloDTO> modulosUsuarioModuloContratado(@RequestBody ModuloFilterDTO dto)throws FlexException {
		try {
			return moduloService.modulosUsuario(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
}