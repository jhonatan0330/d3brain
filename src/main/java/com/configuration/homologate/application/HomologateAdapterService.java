package com.configuration.homologate.application;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.configuration.homologate.domain.ConfigEnum;
import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.authorization.application.RolAccesoSvc;
import com.softure.authorization.domain.RolAccesoDTO;
import com.softure.authorization.domain.RolAccesoFilterDTO;
import com.softure.document_execution.application.CallDocumentCRUD;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.inventory.application.ProductoSvc;
import com.softure.money.application.CuentaSvc;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.report.application.ReporteBaseSvc;
import com.softure.report.domain.ReporteBaseDTO;
import com.softure.report.domain.ReporteBaseFilterDTO;

@Service("HomologatePrepareService")
public class HomologateAdapterService {

	@Autowired
	@Lazy
	private PropiedadSvc propertyService;
	@Autowired
	@Lazy
	private ProductoSvc productoService;
	@Autowired
	@Lazy
	private ReporteBaseSvc reporteService;
	@Autowired
	@Lazy
	private DocumentoPlantillaSvc plantillaService;
	@Autowired
	@Lazy
	private DocumentoPlantillaCaracteristicaSvc campoService;
	@Autowired
	@Lazy
	private RolAccesoSvc rolService;
	@Autowired
	@Lazy
	private CallDocumentCRUD crudService;

	@Autowired
	@Lazy
	private CuentaSvc cuentaService;

	@Autowired
	@Lazy
	private HomologateTariff tariffHomologate;
	@Autowired
	@Lazy
	private HomologateFee feeHomologate;
	@Autowired
	@Lazy
	private HomologateFaq faqHomologate;
	@Autowired
	@Lazy
	private HomologateCatalog catalogHomologate;
	@Autowired
	@Lazy
	private HomologateAccount accountHomologate;
	@Autowired
	@Lazy
	private HomologateProduct productHomologate;
	@Autowired
	@Lazy
	private HomologateProductStock productStockHomologate;
	@Autowired
	@Lazy
	private HomologateProductStockDeduction productStockDeductionHomologate;

	public void call(PropiedadDTO dto, String token) throws ServerException {
		DocumentoPlantillaDTO plantillaPrincipal = plantillaService.consultaXId(dto.getCampo());
		switch (dto.getKey()) {
		case Propiedades.PLANTILLA_TIPO_CUENTA:
			break;
		case Propiedades.PLANTILLA_TIPO_PRODUCTO:
			productHomologate.createProductFields(plantillaPrincipal.getLlaveTabla(), token, campoService,
					propertyService);
			break;
		case Propiedades.PLANTILLA_TIPO_REPORTE:
			ReporteBaseFilterDTO reporteFilter = new ReporteBaseFilterDTO();
			reporteFilter.setPlantilla(plantillaPrincipal.getLlaveTabla());
			if (reporteService.contarResultados(reporteFilter) == 0) {
				ReporteBaseDTO reporte = new ReporteBaseDTO();
				reporte.setCodigo(plantillaPrincipal.getCodigo());
				reporte.setDescripcion("PENDIENTE");
				reporte.setNombre(plantillaPrincipal.getNombre());
				reporte.setPlantilla(plantillaPrincipal.getLlaveTabla());
				reporte = reporteService.guardar(reporte, token);
				propertyService.guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.REPORTE,
						reporte.getLlaveTabla(), Propiedades.REP_AUTOPRINT, "1", token), token);
				campoService.crearCampoTiempoReporte(plantillaPrincipal.getLlaveTabla(), token, true);
				PropiedadDTO historico = Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA,
						plantillaPrincipal.getLlaveTabla(), Propiedades.PERIODO_LIMPIEZA_HISTORICO, "15", token);
				historico.setFechaInicial(new Date());
				historico.setMotivo("Pasar a tabla historico");
				historico.setTexto("00:00:07:00:00");
				propertyService.guardar(historico, token);
				propertyService.guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA,
						plantillaPrincipal.getLlaveTabla(), Propiedades.SOLICITAR_FECHAS, "1", token), token);
				// Esto es un truco para crear un query report de una plantilla
				// Quedo pendiente
				if (dto.getUsuarioExcluyenteNombre() != null) {
					propertyService.guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.REPORTE,
							reporte.getLlaveTabla(), Propiedades.REPORT_QUERY,
							generateScriptToTemplate(dto.getUsuarioExcluyenteNombre()), token), token);
				}
			}
			break;
		case Propiedades.PLANTILLA_TIPO_ROL:
			RolAccesoFilterDTO rolFiltroFilter = new RolAccesoFilterDTO();
			rolFiltroFilter.setEstado(SharedConstants.STATE_ACTIVE);
			rolFiltroFilter.setPlantilla(plantillaPrincipal.getLlaveTabla());
			RolAccesoDTO rolFiltro = rolService.consultaUnica(rolFiltroFilter);
			if (rolFiltro == null) {// Si la propiedad ya se genero no hay que duplicar
				RolAccesoDTO nuevo = new RolAccesoDTO();
				nuevo.setPlantilla(plantillaPrincipal.getLlaveTabla());
				nuevo = rolService.guardar(nuevo, token);
				propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA,
						plantillaPrincipal.getLlaveTabla(), Propiedades.ORDEN, "N", token), token);
				propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA,
						plantillaPrincipal.getLlaveTabla(), Propiedades.DESCRIPCION, "*", token), token);
				propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA,
						plantillaPrincipal.getLlaveTabla(), Propiedades.CONSECUTIVO, "*", token), token);
				propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA,
						plantillaPrincipal.getLlaveTabla(), Propiedades.CORREO_ROL, "*", token), token);
				propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA,
						plantillaPrincipal.getLlaveTabla(), Propiedades.CELULAR_ROL, "*", token), token);
			}
			break;
		case Propiedades.PLANTILLA_TIPO_CONFIGURATION:
			adapterConfiguration(plantillaPrincipal.getLlaveTabla(), dto.getValor(), token);
			break;
		}
	}

	private String generateScriptToTemplate(String templateId) throws ServerException {
		DocumentoPlantillaDTO dp = plantillaService.consultaXId(templateId);
		String result = "select d.cpdv_nombre as \"CODIGO\"";
		List<DocumentoPlantillaCaracteristicaDTO> fields = campoService.listarCamposPlantilla(templateId, null);
		for (DocumentoPlantillaCaracteristicaDTO iField : fields) {

			switch (iField.getFormato()) {
			case DocumentoPlantillaCaracteristicaDTO.FECHA: {
				result = result
						+ "\n	,(select cp.dpvc_valorfecha from campo_documento cp where cp.cdrc_documento = d.cpdv_llave and cp.cdpf_codigo = '"
						+ iField.getCodigo() + "') as \"" + iField.getNombre() + "\"";
				break;
			}
			case DocumentoPlantillaCaracteristicaDTO.NUMERO: {
				result = result
						+ "\n	,to_char(coalesce((select cp.mpvc_valornumero from campo_documento cp where cp.cdrc_documento = d.cpdv_llave and cp.cdpf_codigo = '"
						+ iField.getCodigo() + "'), 0), 'FM9999999999999999') as \"" + iField.getNombre() + "\"";
				break;
			}
			default: {
				result = result
						+ "\n	,(select cp.cpvc_valortext from campo_documento cp where cp.cdrc_documento = d.cpdv_llave and cp.cdpf_codigo = '"
						+ iField.getCodigo() + "') as \"" + iField.getNombre() + "\"";
			}
			}
		}
		result = result
				+ "\n	,(select cpes_nombre from procesoestado_pesp where cpes_llave = d.cpdv_estadoexpediente) as \"ESTADO\"";
		result = result
				+ "\nfrom documentoplantilla_dplp \ninner join pedidoventa_pdvp d on cdpl_llave = d.cpdv_plantilla \nwhere cdpl_codigo = '"
				+ dp.getCodigo()
				+ "' and d.dpdv_fecha >= $P{P_FECHA_INICIO} and d.dpdv_fecha < $P{P_FECHA_FIN} \norder by 1";
		return result;
	}

	private void adapterConfiguration(String templateId, String propValue, String token) throws ServerException {
		if (propValue == null)
			return;
		switch (propValue) {
		case ConfigEnum.TARIFARIO: {
			tariffHomologate.createTariffFields(templateId, token, campoService, propertyService, crudService,
					campoService.getUserFlex(token));
			break;
		}
		case ConfigEnum.TARIFA: {
			feeHomologate.createFeeFields(templateId, token, campoService, propertyService, crudService);
			break;
		}
		case ConfigEnum.FAQ: {
			faqHomologate.createFaqFields(templateId, token, campoService, propertyService);
			break;
		}
		case ConfigEnum.CATALOG: {
			catalogHomologate.createCatalogFields(templateId, token, campoService, propertyService);
			break;
		}
		case ConfigEnum.ACCOUNT: {
			accountHomologate.createAccountFields(templateId, token, campoService, propertyService);
			break;
		}
		case ConfigEnum.PRODUCTO_COMPOSICION: {
			productStockDeductionHomologate.createFields(templateId, token, campoService, propertyService, crudService);
			break;
		}
		case ConfigEnum.PRODUCTO_INVENTARIO: {
			productStockHomologate.createFields(templateId, token, campoService, propertyService, crudService);
			break;
		}
		default:
			throw new ServerException("Unexpected value: " + propValue);
		}
	}

	public void createFromDocument(PedidoVentaDTO document, String propValue, String token) throws ServerException {
		switch (propValue) {
		case ConfigEnum.TARIFARIO: {
			tariffHomologate.createTariff(document);
			break;
		}
		case ConfigEnum.TARIFA: {
			feeHomologate.createFee(document, token, crudService);
			break;
		}
		case ConfigEnum.CATALOG: {
			catalogHomologate.createCatalog(document);
			break;
		}
		case ConfigEnum.ACCOUNT: {
			accountHomologate.createAccount(document);
			break;
		}
		case ConfigEnum.PRODUCTO_COMPOSICION: {
			productStockDeductionHomologate.create(document);
			break;
		}
		case ConfigEnum.PRODUCTO_INVENTARIO: {
			productStockHomologate.create(document, token);
			break;
		}
		default:
			throw new ServerException("Unexpected value: " + propValue);
		}
	}

	public void crearProducto(PedidoVentaDTO documento, String token) throws ServerException {
		productHomologate.crearDesdeDocumento(documento, token);
	}

	// Este metodo habia desaparecido pero es necesario para poder abrir los turnos
	// de una caja
	public void crearCuenta(PedidoVentaDTO dto, String token) throws ServerException {
		cuentaService.crearCuenta(dto, token);
	}
}
