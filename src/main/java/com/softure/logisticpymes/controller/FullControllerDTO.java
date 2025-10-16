package com.softure.logisticpymes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shared.domain.ServerException;
import com.softure.authentication.application.OrganizacionSvc;
import com.softure.authentication.application.UsuarioAutenticacionAutorizacionSvc;
import com.softure.authentication.application.UsuarioAutenticacionSvc;
import com.softure.authentication.application.UsuarioOrganizacionSvc;
import com.softure.authentication.domain.OrganizacionDTO;
import com.softure.authentication.domain.OrganizacionFilterDTO;
import com.softure.authentication.domain.UsuarioAutenticacionAutorizacionDTO;
import com.softure.authentication.domain.UsuarioAutenticacionAutorizacionFilterDTO;
import com.softure.authentication.domain.UsuarioAutenticacionDTO;
import com.softure.authentication.domain.UsuarioAutenticacionFilterDTO;
import com.softure.authentication.domain.UsuarioOrganizacionDTO;
import com.softure.authentication.domain.UsuarioOrganizacionFilterDTO;
import com.softure.authorization.application.RolAccesoSvc;
import com.softure.authorization.application.UsuarioRolProductoSvc;
import com.softure.authorization.application.UsuarioRolSvc;
import com.softure.authorization.domain.RolAccesoDTO;
import com.softure.authorization.domain.RolAccesoFilterDTO;
import com.softure.authorization.domain.UsuarioRolDTO;
import com.softure.authorization.domain.UsuarioRolFilterDTO;
import com.softure.authorization.domain.UsuarioRolProductoDTO;
import com.softure.authorization.domain.UsuarioRolProductoFilterDTO;
import com.softure.document_execution.application.CallDocumentCRUD;
import com.softure.document_execution.application.CallDocumentListWithFilters;
import com.softure.document_execution.application.DocumentoRelacionExpedienteSvc;
import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.application.PedidoVentaSvc;
import com.softure.document_execution.domain.DocumentoRelacionExpedienteDTO;
import com.softure.document_execution.domain.DocumentoRelacionExpedienteFilterDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaFilterDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaFilterDTO;
import com.softure.document_transition.application.DocumentoRelacionGestorSvc;
import com.softure.document_transition.domain.DocumentoRelacionGestorDTO;
import com.softure.document_transition.domain.DocumentoRelacionGestorFilterDTO;
import com.softure.java.dto.exception.FlexException;
import com.softure.java.services.SoftureUtil;
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
import com.softure.money.domain.CuentaDTO;
import com.softure.money.domain.CuentaFilterDTO;
import com.softure.money.domain.MovimientoDTO;
import com.softure.money.domain.MovimientoFilterDTO;
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
import com.softure.webservice.application.WebServiceEjecucionSvc;
import com.softure.webservice.application.WebServiceSvc;
import com.softure.webservice.domain.WebServiceDTO;
import com.softure.webservice.domain.WebServiceEjecucionDTO;
import com.softure.webservice.domain.WebServiceEjecucionFilterDTO;
import com.softure.webservice.domain.WebServiceFilterDTO;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/flex")
public class FullControllerDTO {
	
	@Autowired @Lazy  private UsuarioSvc usuarioService;
	
	@PostMapping(value="/listarRolUsuario")
	public List<UsuarioDTO> listarRolUsuario(@RequestBody UsuarioFilterDTO dto)throws FlexException {
		try {
			return usuarioService.listarRol(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	@PostMapping(value="/consultaXIdUsuario")
	public UsuarioDTO consultaXIdUsuario(@RequestBody String llave) throws FlexException {
		try {
			return usuarioService.consultaXId(llave);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired @Lazy  private PlantillaConsecutivoSvc plantillaConsecutivoService;
	@Autowired @Lazy  private CallDocumentCRUD crudService;
	
	@PostMapping(value="/consultaXIdPlantillaConsecutivo")
	public PlantillaConsecutivoDTO consultaXIdPlantillaConsecutivo(@RequestBody String llave) throws FlexException {
		try {
			return plantillaConsecutivoService.consultaXId(llave);
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
	
	
	@Autowired @Lazy  private ProcesoSvc procesoService;
	
	@PostMapping(value="/consultaXIdProceso")
	public ProcesoDTO consultaXIdProceso(@RequestBody String llave) throws FlexException {
		try {
			return procesoService.consultaXId(llave);
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
		
	
	@Autowired @Lazy  private ProcesoEstadoSvc procesoEstadoService;
	
	@PostMapping(value="/consultaXIdProcesoEstado")
	public ProcesoEstadoDTO consultaXIdProcesoEstado(@RequestBody String llave) throws FlexException {
		try {
			return procesoEstadoService.consultaXId(llave);
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
	
	
	@Autowired @Lazy  private ProcesoTransicionAutomaticaSvc procesoTransicionAutomaticaService;
	
	@PostMapping(value="/consultaXIdProcesoTransicionAutomatica")
	public ProcesoTransicionAutomaticaDTO consultaXIdProcesoTransicionAutomatica(@RequestBody String llave) throws FlexException {
		try {
			return procesoTransicionAutomaticaService.consultaXId(llave);
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
	
	@Autowired @Lazy  private DocumentoRelacionExpedienteSvc documentoRelacionExpedienteService;
	
	@PostMapping(value="/consultaXIdDocumentoRelacionExpediente")
	public DocumentoRelacionExpedienteDTO consultaXIdDocumentoRelacionExpediente(@RequestBody String llave) throws FlexException {
		try {
			return documentoRelacionExpedienteService.consultaXId(llave);
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
	
	
	@Autowired @Lazy  private DocumentoRelacionGestorSvc documentoRelacionGestorService;
	
	@PostMapping(value="/consultaXIdDocumentoRelacionGestor")
	public DocumentoRelacionGestorDTO consultaXIdDocumentoRelacionGestor(@RequestBody String llave) throws FlexException {
		try {
			return documentoRelacionGestorService.consultaXId(llave);
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
	
	@Autowired @Lazy  private DocumentoPlantillaCaracteristicaSvc documentoPlantillaCaracteristicaService;
	
	@PostMapping(value="/consultaXIdDocumentoPlantillaCaracteristica")
	public DocumentoPlantillaCaracteristicaDTO consultaXIdDocumentoPlantillaCaracteristica(@RequestBody String llave) throws FlexException {
		try {
			return documentoPlantillaCaracteristicaService.consultaXId(llave);
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
	

	@Autowired @Lazy  private PedidoVentaSvc pedidoVentaService;
	
	@PostMapping(value="/consultaXIdPedidoVenta")
	public PedidoVentaDTO consultaXIdPedidoVenta(@RequestBody String llave) throws FlexException {
		try {
			return pedidoVentaService.consultaXId(llave);
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
	
	@Autowired @Lazy  private CallDocumentListWithFilters documentListWithFiltersFunction;
	
	@PostMapping(value="/listarAvanzadoPedidoVenta")
	public List<PedidoVentaDTO> listarAvanzadoPedidoVenta(@RequestBody PedidoVentaFilterDTO dto)throws FlexException {
		try {
			return documentListWithFiltersFunction.listarAvanzado(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	
	@Autowired @Lazy  private PedidoVentaCaracteristicaSvc pedidoVentaCaracteristicaService;
	
	@PostMapping(value="/consultaXIdPedidoVentaCaracteristica")
	public PedidoVentaCaracteristicaDTO consultaXIdPedidoVentaCaracteristica(@RequestBody String llave) throws FlexException {
		try {
			return pedidoVentaCaracteristicaService.consultaXId(llave);
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
	
	@Autowired @Lazy  private ProcesoTransicionSvc procesoTransicionService;
	
	@PostMapping(value="/consultaXIdProcesoTransicion")
	public ProcesoTransicionDTO consultaXIdProcesoTransicion(@RequestBody String llave) throws FlexException {
		try {
			return procesoTransicionService.consultaXId(llave);
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
	
	
	
	
	@Autowired @Lazy  private DocumentoPlantillaSvc documentoPlantillaService;
	
	@PostMapping(value="/consultaXIdDocumentoPlantilla")
	public DocumentoPlantillaDTO consultaXIdDocumentoPlantilla(@RequestBody String llave) throws FlexException {
		try {
			return documentoPlantillaService.consultaXId(llave);
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
			return documentoPlantillaService.obtenerCampos(dto, token, true);
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
	
	@Autowired @Lazy  private MovimientoSvc movimientoService;
	
	@PostMapping(value="/consultaXIdMovimiento")
	public MovimientoDTO consultaXIdMovimiento(@RequestBody String llave) throws FlexException {
		try {
			return movimientoService.consultaXId(llave);
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
	
	
	@Autowired @Lazy  private CuentaSvc cuentaService;
	
	@PostMapping(value="/consultaXIdCuenta")
	public CuentaDTO consultaXIdCuenta(@RequestBody String llave) throws FlexException {
		try {
			return cuentaService.consultaXId(llave);
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
	
	
	@Autowired @Lazy  private ActividadSvc actividadService;
	
	@PostMapping(value="/consultaXIdActividad")
	public ActividadDTO consultaXIdActividad(@RequestBody String llave) throws FlexException {
		try {
			return actividadService.consultaXId(llave);
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
	
	
	
	@Autowired @Lazy  private PropiedadSvc propiedadService;
	
	@PostMapping(value="/consultaXIdPropiedad")
	public PropiedadDTO consultaXIdPropiedad(@RequestBody String llave) throws FlexException {
		try {
			return propiedadService.consultaXId(llave);
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
	
	
	@Autowired @Lazy  private RelacionInternaSvc relacionInternaService;
	
	@PostMapping(value="/consultaXIdRelacionInterna")
	public RelacionInternaDTO consultaXIdRelacionInterna(@RequestBody String llave) throws FlexException {
		try {
			return relacionInternaService.consultaXId(llave);
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
	
	
	
	@Autowired @Lazy  private CambioSvc cambioService;
	
	@PostMapping(value="/consultaXIdCambio")
	public CambioDTO consultaXIdCambio(@RequestBody String llave) throws FlexException {
		try {
			return cambioService.consultaXId(llave);
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
	
	
	
	@Autowired @Lazy  private PropiedadValorDefinidoSvc propiedadValorDefinidoService;
	
	@PostMapping(value="/consultaXIdPropiedadValorDefinido")
	public PropiedadValorDefinidoDTO consultaXIdPropiedadValorDefinido(@RequestBody String llave) throws FlexException {
		try {
			return propiedadValorDefinidoService.consultaXId(llave);
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
	
	
	@Autowired @Lazy  private UsuarioRolSvc usuarioRolService;
	
	@PostMapping(value="/consultaXIdUsuarioRol")
	public UsuarioRolDTO consultaXIdUsuarioRol(@RequestBody String llave) throws FlexException {
		try {
			return usuarioRolService.consultaXId(llave);
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
	
	
	@Autowired @Lazy  private PuestoSvc puestoService;
	
	@PostMapping(value="/consultaXIdPuesto")
	public PuestoDTO consultaXIdPuesto(@RequestBody String llave) throws FlexException {
		try {
			return puestoService.consultaXId(llave);
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
	
	
	@Autowired @Lazy  private RolAccesoSvc rolAccesoService;
	
	@PostMapping(value="/consultaXIdRolAcceso")
	public RolAccesoDTO consultaXIdRolAcceso(@RequestBody String llave) throws FlexException {
		try {
			return rolAccesoService.consultaXId(llave);
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
	


	
	@Autowired @Lazy  private WebServiceSvc webServiceService;
	
	@PostMapping(value="/consultaXIdWebService")
	public WebServiceDTO consultaXIdWebService(@RequestBody String llave) throws FlexException {
		try {
			return webServiceService.consultaXId(llave);
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
	
	
	
	
	@Autowired @Lazy  private MensajeSvc mensajeService;
	
	@PostMapping(value="/consultaXIdMensaje")
	public MensajeDTO consultaXIdMensaje(@RequestBody String llave) throws FlexException {
		try {
			return mensajeService.consultaXId(llave);
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
	
	@Autowired @Lazy  private MailUserSendMessage userSendMessage;

	@PostMapping(value="/enviarMensajeMensaje")
	public MensajeDTO enviarMensajeMensaje(@RequestBody MensajeFilterDTO dto)throws FlexException {
		try {
			return userSendMessage.call(dto);
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}
	
	
	@Autowired @Lazy  private WebServiceEjecucionSvc webServiceEjecucionService;
	
	@PostMapping(value="/consultaXIdWebServiceEjecucion")
	public WebServiceEjecucionDTO consultaXIdWebServiceEjecucion(@RequestBody String llave) throws FlexException {
		try {
			return webServiceEjecucionService.consultaXId(llave);
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
	
	
	@Autowired @Lazy  private ServidorSvc servidorService;
	
	@PostMapping(value="/consultaXIdServidor")
	public ServidorDTO consultaXIdServidor(@RequestBody String llave) throws FlexException {
		try {
			return servidorService.consultaXId(llave);
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
	
	
	@Autowired @Lazy  private MensajePlantillaCorreoSvc mensajePlantillaCorreoService;
	
	@PostMapping(value="/consultaXIdMensajePlantillaCorreo")
	public MensajePlantillaCorreoDTO consultaXIdMensajePlantillaCorreo(@RequestBody String llave) throws FlexException {
		try {
			return mensajePlantillaCorreoService.consultaXId(llave);
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
	
	
	@Autowired @Lazy  private UsuarioRolProductoSvc usuarioRolProductoService;
	
	@PostMapping(value="/consultaXIdUsuarioRolProducto")
	public UsuarioRolProductoDTO consultaXIdUsuarioRolProducto(@RequestBody String llave) throws FlexException {
		try {
			return usuarioRolProductoService.consultaXId(llave);
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
	
	
	@Autowired @Lazy  private ReporteBaseSvc reporteBaseService;
	
	@PostMapping(value="/consultaXIdReporteBase")
	public ReporteBaseDTO consultaXIdReporteBase(@RequestBody String llave) throws FlexException {
		try {
			return reporteBaseService.consultaXId(llave);
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
	
	
	@Autowired @Lazy  private UsuarioOrganizacionSvc usuarioOrganizacionService;
	
	@PostMapping(value="/consultaXIdUsuarioOrganizacion")
	public UsuarioOrganizacionDTO consultaXIdUsuarioOrganizacion(@RequestBody String llave) throws FlexException {
		try {
			return usuarioOrganizacionService.consultaXId(llave);
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
	
	
	
	@Autowired @Lazy  private UsuarioAutenticacionAutorizacionSvc usuarioAutenticacionAutorizacionService;
	
	@PostMapping(value="/consultaXIdUsuarioAutenticacionAutorizacion")
	public UsuarioAutenticacionAutorizacionDTO consultaXIdUsuarioAutenticacionAutorizacion(@RequestBody String llave) throws FlexException {
		try {
			return usuarioAutenticacionAutorizacionService.consultaXId(llave);
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
	
	
	@Autowired @Lazy  private ReporteEjecucionSvc reporteEjecucionService;
	
	@PostMapping(value="/consultaXIdReporteEjecucion")
	public ReporteEjecucionDTO consultaXIdReporteEjecucion(@RequestBody String llave) throws FlexException {
		try {
			return reporteEjecucionService.consultaXId(llave);
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
	
	
	@Autowired @Lazy  private ConsecutivoSvc consecutivoService;
	
	@PostMapping(value="/consultaXIdConsecutivo")
	public ConsecutivoDTO consultaXIdConsecutivo(@RequestBody String llave) throws FlexException {
		try {
			return consecutivoService.consultaXId(llave);
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

	
	@Autowired @Lazy  private UsuarioAutenticacionSvc usuarioAutenticacionService;
	
	@PostMapping(value="/consultaXIdUsuarioAutenticacion")
	public UsuarioAutenticacionDTO consultaXIdUsuarioAutenticacion(@RequestBody String llave) throws FlexException {
		try {
			return usuarioAutenticacionService.consultaXId(llave);
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
	public UsuarioAutenticacionDTO autenticarUsuarioAutenticacion(HttpServletRequest request, @RequestBody UsuarioAutenticacionFilterDTO dto)throws FlexException {
		try {
			return usuarioAutenticacionService.autenticar(dto, false, SoftureUtil.getRequestUrl(request));
		} catch (ServerException e) {
			throw new FlexException(e.getMessage());
		}
	}

	
	
	
	
	@Autowired @Lazy  private OrganizacionSvc organizacionService;
	
	@PostMapping(value="/consultaXIdOrganizacion")
	public OrganizacionDTO consultaXIdOrganizacion(@RequestBody String llave) throws FlexException {
		try {
			return organizacionService.consultaXId(llave);
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
	
}
