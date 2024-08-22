package com.configuration.homologate.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.configuration.homologate.domain.ConfigEnum;
import com.learning.helpcenter.application.base.ArticleService;
import com.learning.helpcenter.domain.ArticleDTO;
import com.learning.helpcenter.domain.ArticleFilterDTO;
import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.authorization.application.RolAccesoSvc;
import com.softure.authorization.domain.RolAccesoDTO;
import com.softure.authorization.domain.RolAccesoFilterDTO;
import com.softure.document_execution.application.CallDocumentCRUD;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.inventory.application.BodegaSvc;
import com.softure.inventory.application.ProductoSvc;
import com.softure.money.application.CuentaSvc;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.application.DocumentoPlantillaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.process_form.domain.DocumentoPlantillaDTO;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.PropiedadFilterDTO;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.report.application.ReporteBaseSvc;
import com.softure.report.domain.ReporteBaseDTO;
import com.softure.report.domain.ReporteBaseFilterDTO;
import com.softure.tariff.application.base.TarifaSvc;
import com.softure.tariff.application.base.TarifarioService;
import com.softure.tariff.domain.TarifaDTO;
import com.softure.tariff.domain.TarifaFilterDTO;
import com.softure.tariff.domain.TarifarioDTO;
import com.softure.tariff.domain.TarifarioFilterDTO;

@Service("HomologatePrepareService")
public class HomologatePrepareService {

	@Autowired
	@Lazy
	private ReporteBaseSvc reporteService;
	@Autowired
	@Lazy
	private DocumentoPlantillaSvc plantillaService;
	@Autowired
	@Lazy
	private PropiedadSvc propertyService;
	@Autowired
	@Lazy
	private RolAccesoSvc rolService;
	@Autowired
	@Lazy
	private DocumentoPlantillaCaracteristicaSvc campoService;
	@Autowired
	@Lazy
	private TarifarioService tariffService;
	@Autowired
	@Lazy
	private TarifaSvc feeService;
	@Autowired
	@Lazy
	private CallDocumentCRUD crudService;
	@Autowired
	@Lazy
	private ArticleService articleService;
	@Autowired
	@Lazy
	private BodegaSvc bodegaService;
	@Autowired
	@Lazy
	private ProductoSvc productoService;
	@Autowired
	@Lazy
	private CuentaSvc cuentaService;

	public void call(PropiedadDTO dto, String token) throws ServerException {
		DocumentoPlantillaDTO plantillaPrincipal = plantillaService.consultaXId(dto.getCampo());
		switch (dto.getKey()) {
		case Propiedades.PLANTILLA_TIPO_BODEGA:
			break;
		case Propiedades.PLANTILLA_TIPO_CUENTA:
			break;
		case Propiedades.PLANTILLA_TIPO_PRODUCTO:
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
				/*PropiedadDTO historico = Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA,
						plantillaPrincipal.getLlaveTabla(), Propiedades.PERIODO_LIMPIEZA_HISTORICO, "15", token);
				historico.setFechaInicial(new Date());
				historico.setMotivo("Pasar a tabla historico");
				historico.setTexto("00:00:07:00:00");
				propertyService.guardar(historico, token);*/
				propertyService.guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA,
						plantillaPrincipal.getLlaveTabla(), Propiedades.SOLICITAR_FECHAS, "1", token), token);
				//Esto es un truco para crear un query report de una plantilla
				//Quedo pendiente
				if(dto.getUsuarioExcluyenteNombre()!=null) {
					propertyService.guardar(Propiedades.crearParametro(PropiedadValorDefinidoDTO.REPORTE,
							reporte.getLlaveTabla(), Propiedades.REPORT_QUERY, "1", token), token);
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
				guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA,
						plantillaPrincipal.getLlaveTabla(), Propiedades.ORDEN, "N", token), token);
				guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA,
						plantillaPrincipal.getLlaveTabla(), Propiedades.DESCRIPCION, "*", token), token);
				guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA,
						plantillaPrincipal.getLlaveTabla(), Propiedades.CONSECUTIVO, "*", token), token);
				guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA,
						plantillaPrincipal.getLlaveTabla(), Propiedades.CORREO_ROL, "*", token), token);
				guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA,
						plantillaPrincipal.getLlaveTabla(), Propiedades.CELULAR_ROL, "*", token), token);
			}
			break;
		case Propiedades.PLANTILLA_TIPO_CONFIGURATION:
			adapterConfiguration(plantillaPrincipal.getLlaveTabla(), dto.getValor(), token);
			break;
		}
	}

	private void guardarEnCasoQueNoExista(PropiedadDTO dto, String token) throws ServerException {
		// Lo copie de guardar depronto lo puedo refacorizar
		PropiedadFilterDTO existeFilter = new PropiedadFilterDTO();
		existeFilter.setCampo(dto.getCampo());
		if (dto.getPropiedadValor() == null)
			existeFilter.setPropiedadValor(
					propertyService.consultarValorDefinido(dto.getTipo(), dto.getKey()).getLlaveTabla());
		existeFilter.setEstado(SharedConstants.STATE_ACTIVE);
		existeFilter.setKey(dto.getKey());
		existeFilter.setTipo(dto.getTipo());
		PropiedadDTO existe = propertyService.consultaUnica(existeFilter);
		if (existe == null)
			propertyService.guardar(dto, token);
	}

	private void adapterConfiguration(String templateId, String propValue, String token) throws ServerException {
		if (propValue == null)
			return;
		switch (propValue) {
		case ConfigEnum.TARIFARIO: {
			createTariffFields(templateId, token);
			break;
		}
		case ConfigEnum.TARIFA: {
			createFeeFields(templateId, token);
			break;
		}
		case ConfigEnum.ARTICLE: {
			createArticleFields(templateId, token);
			break;
		}
		case ConfigEnum.FAQ: {
			createFaqFields(templateId, token);
			break;
		}
		default:
			throw new ServerException("Unexpected value: " + propValue);
		}
	}

	private void createTariffFields(String templateId, String token) throws ServerException {
		List<String> fieldsTemplate = new ArrayList<>();
		fieldsTemplate.add(
				campoService.createField(templateId, "NOMBRE", DocumentoPlantillaCaracteristicaDTO.TEXTO, 1, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(0),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA, templateId,
				Propiedades.DESCRIPCION, fieldsTemplate.get(0), token), token);
		// fecha inicial
		fieldsTemplate.add(campoService.createField(templateId, "FECHA_INICIAL",
				DocumentoPlantillaCaracteristicaDTO.FECHA, 2, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(1),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		// fecha final
		fieldsTemplate.add(campoService.createField(templateId, "FECHA_FINAL",
				DocumentoPlantillaCaracteristicaDTO.FECHA, 3, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(2),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(2),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		// Crear el campo tipo recurso
		fieldsTemplate.add(campoService.createField(templateId, "DIMENSION_1",
				DocumentoPlantillaCaracteristicaDTO.CONFIGURACION, 4, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(3),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(3),
				Propiedades.CONFIGURACION_ENTIDAD, "PLANTILLAS", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(3),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		// Crear el campo tipo recurso nombre
		fieldsTemplate.add(campoService.createField(templateId, "NOMBRE_DIM_1",
				DocumentoPlantillaCaracteristicaDTO.TEXTO, 5, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(4),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(4),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		// Crear el campo tipo dimension 2
		fieldsTemplate.add(campoService.createField(templateId, "DIMENSION_2",
				DocumentoPlantillaCaracteristicaDTO.CONFIGURACION, 6, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(5),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(5),
				Propiedades.CONFIGURACION_ENTIDAD, "PLANTILLAS", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(5),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		// Crear el campo tipo recurso nombre
		fieldsTemplate.add(campoService.createField(templateId, "NOMBRE_DIM_2",
				DocumentoPlantillaCaracteristicaDTO.TEXTO, 7, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(6),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(6),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		// Crear el campo tipo dimension 3
		fieldsTemplate.add(campoService.createField(templateId, "DIMENSION_3",
				DocumentoPlantillaCaracteristicaDTO.CONFIGURACION, 8, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(7),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(7),
				Propiedades.CONFIGURACION_ENTIDAD, "PLANTILLAS", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(7),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		// Crear el campo tipo recurso nombre
		fieldsTemplate.add(campoService.createField(templateId, "NOMBRE_DIM_3",
				DocumentoPlantillaCaracteristicaDTO.TEXTO, 9, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(8),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(8),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		// Crear el campo tipo dimension 4
		fieldsTemplate.add(campoService.createField(templateId, "DIMENSION_4",
				DocumentoPlantillaCaracteristicaDTO.CONFIGURACION, 10, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(9),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(9),
				Propiedades.CONFIGURACION_ENTIDAD, "PLANTILLAS", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(9),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		// Crear el campo tipo recurso nombre
		fieldsTemplate.add(campoService.createField(templateId, "NOMBRE_DIM_4",
				DocumentoPlantillaCaracteristicaDTO.TEXTO, 11, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(10),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(10),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		// bool producto opcional
		fieldsTemplate.add(campoService.createField(templateId, "PRODUCTO_OPCIONAL",
				DocumentoPlantillaCaracteristicaDTO.BINARIO, 12, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(11),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		// bool rango valores
		fieldsTemplate.add(campoService.createField(templateId, "RANGO_VALORES",
				DocumentoPlantillaCaracteristicaDTO.BINARIO, 13, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(12),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		// bool rango valores
		fieldsTemplate.add(campoService.createField(templateId, "RANGO_CANTIDADES",
				DocumentoPlantillaCaracteristicaDTO.BINARIO, 14, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(13),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		sincronizeTariff(templateId, fieldsTemplate, token);
	}

	private void createFeeFields(String templateId, String token) throws ServerException {

		List<String> fieldsTemplate = new ArrayList<>();

		// ctar_tarifario varchar(32) NOT NULL,
		fieldsTemplate.add(campoService.createField(templateId, "TARIFARIO",
				DocumentoPlantillaCaracteristicaDTO.PROCESO, 1, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(0),
				Propiedades.PERMISO_CAMPO_RENDER, "1", token), token);
		// ctar_producto varchar(32) NULL,
		fieldsTemplate.add(
				campoService.createField(templateId, "PRODUCTO", DocumentoPlantillaCaracteristicaDTO.PROCESO, 2, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(1),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(1),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(1),
				Propiedades.PERMISO_CAMPO_RENDER, "1", token), token);
		// ctar_recurso varchar(32) NULL,
		fieldsTemplate.add(
				campoService.createField(templateId, "DIM_1", DocumentoPlantillaCaracteristicaDTO.PROCESO, 3, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(2),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(2),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(2),
				Propiedades.PERMISO_CAMPO_RENDER, "1", token), token);
		// ctar_dimension2 varchar(32) NULL,
		fieldsTemplate.add(
				campoService.createField(templateId, "DIM_2", DocumentoPlantillaCaracteristicaDTO.PROCESO, 4, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(3),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(3),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(3),
				Propiedades.PERMISO_CAMPO_RENDER, "1", token), token);
		// ctar_dimension3 varchar(32) NULL
		fieldsTemplate.add(
				campoService.createField(templateId, "DIM_3", DocumentoPlantillaCaracteristicaDTO.PROCESO, 5, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(4),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(4),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(4),
				Propiedades.PERMISO_CAMPO_RENDER, "1", token), token);
		// ctar_dimension4 varchar(32) NULL,
		fieldsTemplate.add(
				campoService.createField(templateId, "DIM_4", DocumentoPlantillaCaracteristicaDTO.PROCESO, 6, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(5),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(5),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(5),
				Propiedades.PERMISO_CAMPO_RENDER, "1", token), token);
		// mtar_valorminimo numeric(18, 6) DEFAULT 0 NOT NULL,
		fieldsTemplate.add(campoService.createField(templateId, "VALOR_MINIMO",
				DocumentoPlantillaCaracteristicaDTO.NUMERO, 7, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(6),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(6),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		// mtar_valor numeric(18, 6) DEFAULT 0 NOT NULL,
		fieldsTemplate.add(
				campoService.createField(templateId, "VALOR", DocumentoPlantillaCaracteristicaDTO.NUMERO, 8, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(7),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(7),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		// mtar_valormaximo numeric(18, 6) DEFAULT 0 NOT NUL,
		fieldsTemplate.add(campoService.createField(templateId, "VALOR_MAXIMO",
				DocumentoPlantillaCaracteristicaDTO.NUMERO, 9, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(8),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(8),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		// ntar_cantidadmaxima int4 DEFAULT 0 NOT NULL,
		fieldsTemplate.add(campoService.createField(templateId, "CANTIDAD_MINIMA",
				DocumentoPlantillaCaracteristicaDTO.NUMERO, 10, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(9),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(9),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		// ntar_cantidadminima int4 DEFAULT 0 NOT NULL,
		fieldsTemplate.add(campoService.createField(templateId, "CANTIDAD_MAXIMA",
				DocumentoPlantillaCaracteristicaDTO.NUMERO, 11, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(10),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(10),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		// mtar_totalminimo numeric(18, 6) DEFAULT 0 NOT NULL,
		fieldsTemplate.add(campoService.createField(templateId, "TOTAL_MINIMO",
				DocumentoPlantillaCaracteristicaDTO.NUMERO, 12, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(11),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(11),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);

		sincronizeFee(templateId, fieldsTemplate, token);
	}

	private void sincronizeTariff(String templateId, List<String> fieldsTemplate, String token) throws ServerException {
		TarifarioFilterDTO filter = new TarifarioFilterDTO();
		filter.setState(SharedConstants.STATE_ACTIVE);
		List<TarifarioDTO> tariffs = tariffService.getMany(filter);
		if (tariffs != null && !tariffs.isEmpty()) {
			for (TarifarioDTO iTariff : tariffs) {
				if (iTariff.getDocumento() == null) {
					PedidoVentaDTO document = new PedidoVentaDTO();
					document.setPlantilla(templateId);
					document.setCaracteristicas(new ArrayList<>());

					PedidoVentaCaracteristicaDTO fieldName = new PedidoVentaCaracteristicaDTO();
					fieldName.setCampo(fieldsTemplate.get(0));
					fieldName.setValorText(iTariff.getNombre());
					document.getCaracteristicas().add(fieldName);

					PedidoVentaCaracteristicaDTO fieldInitialDate = new PedidoVentaCaracteristicaDTO();
					fieldInitialDate.setCampo(fieldsTemplate.get(1));
					fieldInitialDate.setValorFecha(iTariff.getFechaInicial());
					document.getCaracteristicas().add(fieldInitialDate);

					PedidoVentaCaracteristicaDTO fieldFinalDate = new PedidoVentaCaracteristicaDTO();
					fieldFinalDate.setCampo(fieldsTemplate.get(2));
					fieldFinalDate.setValorFecha(iTariff.getFechaFinal());
					document.getCaracteristicas().add(fieldFinalDate);

					PedidoVentaCaracteristicaDTO fieldDimension1 = new PedidoVentaCaracteristicaDTO();
					fieldDimension1.setCampo(fieldsTemplate.get(3));
					fieldDimension1.setValorOpcion(iTariff.getTipoRecurso());
					document.getCaracteristicas().add(fieldDimension1);

					PedidoVentaCaracteristicaDTO fieldDimension1Name = new PedidoVentaCaracteristicaDTO();
					fieldDimension1Name.setCampo(fieldsTemplate.get(4));
					fieldDimension1Name.setValorText(iTariff.getTipoRecursoNombre());
					document.getCaracteristicas().add(fieldDimension1Name);

					PedidoVentaCaracteristicaDTO fieldDimension2 = new PedidoVentaCaracteristicaDTO();
					fieldDimension2.setCampo(fieldsTemplate.get(5));
					fieldDimension2.setValorOpcion(iTariff.getTipoDimension2());
					document.getCaracteristicas().add(fieldDimension2);

					PedidoVentaCaracteristicaDTO fieldDimension2Name = new PedidoVentaCaracteristicaDTO();
					fieldDimension2Name.setCampo(fieldsTemplate.get(6));
					fieldDimension2Name.setValorText(iTariff.getTipoDimension2Nombre());
					document.getCaracteristicas().add(fieldDimension2Name);

					PedidoVentaCaracteristicaDTO fieldDimension3 = new PedidoVentaCaracteristicaDTO();
					fieldDimension3.setCampo(fieldsTemplate.get(7));
					fieldDimension3.setValorOpcion(iTariff.getTipoDimension3());
					document.getCaracteristicas().add(fieldDimension3);

					PedidoVentaCaracteristicaDTO fieldDimension3Name = new PedidoVentaCaracteristicaDTO();
					fieldDimension3Name.setCampo(fieldsTemplate.get(8));
					fieldDimension3Name.setValorText(iTariff.getTipoDimension3Nombre());
					document.getCaracteristicas().add(fieldDimension3Name);

					PedidoVentaCaracteristicaDTO fieldDimension4 = new PedidoVentaCaracteristicaDTO();
					fieldDimension4.setCampo(fieldsTemplate.get(9));
					fieldDimension4.setValorOpcion(iTariff.getTipoDimension4());
					document.getCaracteristicas().add(fieldDimension4);

					PedidoVentaCaracteristicaDTO fieldDimension4Name = new PedidoVentaCaracteristicaDTO();
					fieldDimension4Name.setCampo(fieldsTemplate.get(10));
					fieldDimension4Name.setValorText(iTariff.getTipoDimension4Nombre());
					document.getCaracteristicas().add(fieldDimension4Name);

					PedidoVentaCaracteristicaDTO fieldBoolProduct = new PedidoVentaCaracteristicaDTO();
					fieldBoolProduct.setCampo(fieldsTemplate.get(11));
					if (iTariff.getProductoOpcional())
						fieldBoolProduct.setValorNumero(BigDecimal.ONE);
					document.getCaracteristicas().add(fieldBoolProduct);

					PedidoVentaCaracteristicaDTO fieldValueRange = new PedidoVentaCaracteristicaDTO();
					fieldValueRange.setCampo(fieldsTemplate.get(12));
					if (iTariff.getRangoValores())
						fieldValueRange.setValorNumero(BigDecimal.ONE);
					document.getCaracteristicas().add(fieldValueRange);

					PedidoVentaCaracteristicaDTO fieldValueQuantity = new PedidoVentaCaracteristicaDTO();
					fieldValueQuantity.setCampo(fieldsTemplate.get(13));
					if (iTariff.getRangoCantidad())
						fieldValueQuantity.setValorNumero(BigDecimal.ONE);
					document.getCaracteristicas().add(fieldValueQuantity);

					document.setFuncionario(propertyService.getUserFlex(token));
					document = crudService.saveWithoutTransaction(document, token, true);
					iTariff.setDocumento(document.getLlaveTabla());
					tariffService.update(iTariff);
				}
			}
		}
	}

	private void sincronizeFee(String templateId, List<String> fieldsTemplate, String token) throws ServerException {
		TarifarioFilterDTO filter = new TarifarioFilterDTO();
		filter.setState(SharedConstants.STATE_ACTIVE);
		List<TarifarioDTO> tariffs = tariffService.getMany(filter);
		if (tariffs == null || tariffs.isEmpty())
			return;
		for (TarifarioDTO iTariff : tariffs) {
			TarifaFilterDTO filterFee = new TarifaFilterDTO();
			filterFee.setTarifario(iTariff.getKey());
			filterFee.setEstado(SharedConstants.STATE_ACTIVE);
			filterFee.setPaginacionRegistroFinal(20000);
			List<TarifaDTO> fees = feeService.listarConsulta(filterFee);
			if (fees != null && !fees.isEmpty()) {
				for(TarifaDTO iFee : fees) {
					if (iFee.getDocumento() == null) {
						PedidoVentaDTO document = new PedidoVentaDTO();
						document.setPlantilla(templateId);
						document.setCaracteristicas(new ArrayList<>());

						// ctar_tarifario varchar(32) NOT NULL,
						PedidoVentaCaracteristicaDTO fieldTariff = new PedidoVentaCaracteristicaDTO();
						fieldTariff.setCampo(fieldsTemplate.get(0));
						fieldTariff.setValorOpcion(iTariff.getDocumento());
						document.getCaracteristicas().add(fieldTariff);

						// ctar_producto varchar(32) NULL,
						PedidoVentaCaracteristicaDTO fieldProduct = new PedidoVentaCaracteristicaDTO();
						fieldProduct.setCampo(fieldsTemplate.get(1));
						fieldProduct.setValorOpcion(iFee.getProductoDocumento());
						document.getCaracteristicas().add(fieldProduct);
						// ctar_recurso varchar(32) NULL,
						PedidoVentaCaracteristicaDTO fieldRecurso = new PedidoVentaCaracteristicaDTO();
						fieldRecurso.setCampo(fieldsTemplate.get(2));
						fieldRecurso.setValorOpcion(iFee.getRecurso());
						document.getCaracteristicas().add(fieldRecurso);
						// ctar_dimension2 varchar(32) NULL,
						PedidoVentaCaracteristicaDTO fieldDim2 = new PedidoVentaCaracteristicaDTO();
						fieldDim2.setCampo(fieldsTemplate.get(3));
						fieldDim2.setValorOpcion(iFee.getDimension2());
						document.getCaracteristicas().add(fieldDim2);
						// ctar_dimension3 varchar(32) NULL
						PedidoVentaCaracteristicaDTO fieldDim3 = new PedidoVentaCaracteristicaDTO();
						fieldDim3.setCampo(fieldsTemplate.get(4));
						fieldDim3.setValorOpcion(iFee.getDimension3());
						document.getCaracteristicas().add(fieldDim3);
						// ctar_dimension4 varchar(32) NULL,
						PedidoVentaCaracteristicaDTO fieldDim4 = new PedidoVentaCaracteristicaDTO();
						fieldDim4.setCampo(fieldsTemplate.get(5));
						fieldDim4.setValorOpcion(iFee.getDimension4());
						document.getCaracteristicas().add(fieldDim4);
						// mtar_valorminimo numeric(18, 6) DEFAULT 0 NOT NULL,
						PedidoVentaCaracteristicaDTO fieldValueMin = new PedidoVentaCaracteristicaDTO();
						fieldValueMin.setCampo(fieldsTemplate.get(6));
						fieldValueMin.setValorNumero(iFee.getValorMinimo());
						document.getCaracteristicas().add(fieldValueMin);
						// mtar_valor numeric(18, 6) DEFAULT 0 NOT NULL,
						PedidoVentaCaracteristicaDTO fieldValue = new PedidoVentaCaracteristicaDTO();
						fieldValue.setCampo(fieldsTemplate.get(7));
						fieldValue.setValorNumero(iFee.getValor());
						document.getCaracteristicas().add(fieldValue);
						// mtar_valormaximo numeric(18, 6) DEFAULT 0 NOT NUL,
						PedidoVentaCaracteristicaDTO fieldValueMax = new PedidoVentaCaracteristicaDTO();
						fieldValueMax.setCampo(fieldsTemplate.get(8));
						fieldValueMax.setValorNumero(iFee.getValorMaximo());
						document.getCaracteristicas().add(fieldValueMax);
						
						// ntar_cantidadminima int4 DEFAULT 0 NOT NULL,
						PedidoVentaCaracteristicaDTO fieldQuantityMin = new PedidoVentaCaracteristicaDTO();
						fieldQuantityMin.setCampo(fieldsTemplate.get(9));
						fieldQuantityMin.setValorNumero(new BigDecimal(iFee.getCantidadMinima()));
						document.getCaracteristicas().add(fieldQuantityMin);
						// ntar_cantidadmaxima int4 DEFAULT 0 NOT NULL,
						PedidoVentaCaracteristicaDTO fieldQuantityMax = new PedidoVentaCaracteristicaDTO();
						fieldQuantityMax.setCampo(fieldsTemplate.get(10));
						fieldQuantityMax.setValorNumero(new BigDecimal(iFee.getCantidadMaxima()));
						document.getCaracteristicas().add(fieldQuantityMax);
						
						// mtar_totalminimo numeric(18, 6) DEFAULT 0 NOT NULL,
						PedidoVentaCaracteristicaDTO fieldTotalMin = new PedidoVentaCaracteristicaDTO();
						fieldTotalMin.setCampo(fieldsTemplate.get(11));
						fieldTotalMin.setValorNumero(iFee.getTotalMinimo());
						document.getCaracteristicas().add(fieldTotalMin);

						document.setFuncionario(propertyService.getUserFlex(token));
						document = crudService.saveWithoutTransaction(document, token, true);
						iFee.setDocumento(document.getLlaveTabla());
						feeService.update(iFee);
					}	
				}
			}
		}
	}

	public void createFromDocument(PedidoVentaDTO document, String propValue, String token) throws ServerException {
		switch (propValue) {
		case ConfigEnum.TARIFARIO: {
			createTariff(document);
			break;
		}
		case ConfigEnum.TARIFA: {
			createFee(document, token);
			break;
		}
		case ConfigEnum.ARTICLE: {
			updateArticle(document);
			break;
		}
		default:
			throw new ServerException("Unexpected value: " + propValue);
		}

	}

	private void updateArticle(PedidoVentaDTO document) throws ServerException {
		ArticleFilterDTO filter = new ArticleFilterDTO();
		filter.setDocument(document.getLlaveTabla());
		ArticleDTO updateArticle = articleService.getOne(filter);
		if (updateArticle == null)
			return;
		updateArticle.setIntroduction(getValueText(document, "INTRODUCCION"));
		updateArticle.setHelp(getValueText(document, "AYUDA_EXTRA"));
		updateArticle.setImage(getValueText(document, "IMAGEN"));
		articleService.update(updateArticle);
	}

	private void createTariff(PedidoVentaDTO document) throws ServerException {
		TarifarioFilterDTO filter = new TarifarioFilterDTO();
		filter.setDocumento(document.getLlaveTabla());
		TarifarioDTO newTariff = tariffService.getOne(filter);
		if (newTariff == null) {
			newTariff = new TarifarioDTO();
			newTariff.setDocumento(document.getLlaveTabla());
			newTariff.setFechaFinal(getValueDate(document, "FECHA_FINAL"));
			newTariff.setFechaInicial(getValueDate(document, "FECHA_INICIAL"));
			newTariff.setNombre(getValueText(document, "NOMBRE"));
			newTariff.setProductoOpcional(getValueBool(document, "PRODUCTO_OPCIONAL"));
			newTariff.setRangoCantidad(getValueBool(document, "RANGO_CANTIDADES"));
			newTariff.setRangoValores(getValueBool(document, "RANGO_VALORES"));
			newTariff.setTipoRecurso(getValueOption(document, "DIMENSION_1"));
			newTariff.setTipoDimension2(getValueOption(document, "DIMENSION_2"));
			newTariff.setTipoDimension3(getValueOption(document, "DIMENSION_3"));
			newTariff.setTipoDimension4(getValueOption(document, "DIMENSION_4"));
			newTariff.setTipoRecursoNombre(getValueText(document, "NOMBRE_DIM_1"));
			newTariff.setTipoDimension2Nombre(getValueText(document, "NOMBRE_DIM_2"));
			newTariff.setTipoDimension3Nombre(getValueText(document, "NOMBRE_DIM_3"));
			newTariff.setTipoDimension4Nombre(getValueText(document, "NOMBRE_DIM_4"));
			tariffService.save(newTariff);
		} else {
			if (document.getEstado().compareTo(SharedConstants.STATE_INACTIVE) == 0) {
				if (newTariff.getState().compareTo(SharedConstants.STATE_INACTIVE) != 0) {
					newTariff.setState(SharedConstants.STATE_INACTIVE);
					tariffService.update(newTariff);
				}
			} else {
				newTariff.setFechaFinal(getValueDate(document, "FECHA_FINAL"));
				newTariff.setFechaInicial(getValueDate(document, "FECHA_INICIAL"));
				newTariff.setNombre(getValueText(document, "NOMBRE"));
				newTariff.setProductoOpcional(getValueBool(document, "PRODUCTO_OPCIONAL"));
				newTariff.setRangoCantidad(getValueBool(document, "RANGO_CANTIDADES"));
				newTariff.setRangoValores(getValueBool(document, "RANGO_VALORES"));
				newTariff.setTipoRecurso(getValueOption(document, "DIMENSION_1"));
				newTariff.setTipoDimension2(getValueOption(document, "DIMENSION_2"));
				newTariff.setTipoDimension3(getValueOption(document, "DIMENSION_3"));
				newTariff.setTipoDimension4(getValueOption(document, "DIMENSION_4"));
				newTariff.setTipoRecursoNombre(getValueText(document, "NOMBRE_DIM_1"));
				newTariff.setTipoDimension2Nombre(getValueText(document, "NOMBRE_DIM_2"));
				newTariff.setTipoDimension3Nombre(getValueText(document, "NOMBRE_DIM_3"));
				newTariff.setTipoDimension4Nombre(getValueText(document, "NOMBRE_DIM_4"));
				newTariff.setState(SharedConstants.STATE_ACTIVE);
				tariffService.update(newTariff);
			}
		}
	}
	
	private void createFee(PedidoVentaDTO document, String token) throws ServerException {
		TarifaFilterDTO filter = new TarifaFilterDTO();
		filter.setDocumento(document.getLlaveTabla());
		TarifaDTO newFee = feeService.consultaUnica(filter);
		if (newFee == null) {
			newFee = new TarifaDTO();
			newFee.setDocumento(document.getLlaveTabla());
			newFee.setTarifarioDocumento(getValueOption(document, "TARIFARIO"));
			newFee.setProductoDocumento(getValueOption(document, "PRODUCTO"));
			newFee.setRecurso(getValueOption(document, "DIM_1"));
			newFee.setDimension2(getValueOption(document, "DIM_2"));
			newFee.setDimension3(getValueOption(document, "DIM_3"));
			newFee.setDimension4(getValueOption(document, "DIM_4"));
			newFee.setValorMinimo(getValueNumber(document, "VALOR_MINIMO"));
			newFee.setValor(getValueNumber(document, "VALOR"));
			newFee.setValorMaximo(getValueNumber(document, "VALOR_MAXIMO"));
			newFee.setCantidadMinima(getValueNumberInt(document, "CANTIDAD_MINIMA"));
			newFee.setCantidadMaxima(getValueNumberInt(document, "CANTIDAD_MAXIMA"));
			newFee.setTotalMinimo(getValueNumber(document, "TOTAL_MINIMO"));
			feeService.guardar(newFee, token);
		} else {
			if (document.getEstado().compareTo(SharedConstants.STATE_INACTIVE) == 0) {
				if (newFee.getEstado().compareTo(SharedConstants.STATE_INACTIVE) != 0) {
					newFee.setEstado(SharedConstants.STATE_INACTIVE);
					feeService.update(newFee);
				}
			} else {
				newFee.setTarifarioDocumento(getValueOption(document, "TARIFARIO"));
				newFee.setProductoDocumento(getValueOption(document, "PRODUCTO"));
				newFee.setRecurso(getValueOption(document, "DIM_1"));
				newFee.setDimension2(getValueOption(document, "DIM_2"));
				newFee.setDimension3(getValueOption(document, "DIM_3"));
				newFee.setDimension4(getValueOption(document, "DIM_4"));
				newFee.setValorMinimo(getValueNumber(document, "VALOR_MINIMO"));
				newFee.setValor(getValueNumber(document, "VALOR"));
				newFee.setValorMaximo(getValueNumber(document, "VALOR_MAXIMO"));
				newFee.setCantidadMinima(getValueNumberInt(document, "CANTIDAD_MINIMA"));
				newFee.setCantidadMaxima(getValueNumberInt(document, "CANTIDAD_MAXIMA"));
				newFee.setTotalMinimo(getValueNumber(document, "TOTAL_MINIMO"));

				newFee.setEstado(SharedConstants.STATE_ACTIVE);
				feeService.update(newFee);
			}
		}
	}

	private Date getValueDate(PedidoVentaDTO document, String code) {
		PedidoVentaCaracteristicaDTO field = getField(document, code);
		if (field == null)
			return null;
		return field.getValorFecha();
	}

	private String getValueText(PedidoVentaDTO document, String code) {
		PedidoVentaCaracteristicaDTO field = getField(document, code);
		if (field == null)
			return null;
		return field.getValorText();
	}

	private String getValueOption(PedidoVentaDTO document, String code) {
		PedidoVentaCaracteristicaDTO field = getField(document, code);
		if (field == null)
			return null;
		return field.getValorOpcion();
	}

	private boolean getValueBool(PedidoVentaDTO document, String code) {
		PedidoVentaCaracteristicaDTO field = getField(document, code);
		if (field == null)
			return false;
		if (field.getValorNumero() == null)
			return false;
		return field.getValorNumero().compareTo(BigDecimal.ONE) == 0;
	}
	
	private BigDecimal getValueNumber(PedidoVentaDTO document, String code) {
		PedidoVentaCaracteristicaDTO field = getField(document, code);
		if (field == null)
			return null;
		return field.getValorNumero();
	}
	
	private Integer getValueNumberInt(PedidoVentaDTO document, String code) {
		PedidoVentaCaracteristicaDTO field = getField(document, code);
		if (field == null)
			return null;
		return field.getValorNumero().intValue();
	}

	private PedidoVentaCaracteristicaDTO getField(PedidoVentaDTO document, String code) {
		if (document == null)
			return null;
		if (document.getCaracteristicas() == null || document.getCaracteristicas().isEmpty())
			return null;
		for (PedidoVentaCaracteristicaDTO iField : document.getCaracteristicas()) {
			if (iField.getCampoDTO() != null && iField.getCampoDTO().getCodigo().compareTo(code) == 0)
				return iField;
		}
		return null;
	}

	private void createFaqFields(String templateId, String token) throws ServerException {
		List<String> fieldsTemplate = new ArrayList<>();
		// Crear el campo Introduccion
		fieldsTemplate.add(
				campoService.createField(templateId, "PREGUNTA", DocumentoPlantillaCaracteristicaDTO.TEXTO, 1, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(0),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA, templateId,
				Propiedades.DESCRIPCION, fieldsTemplate.get(0), token), token);

		// Crear el campo Ayudas
		fieldsTemplate.add(
				campoService.createField(templateId, "IMAGEN", DocumentoPlantillaCaracteristicaDTO.ARCHIVO, 2, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(1),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(1),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
	}

	private void createArticleFields(String templateId, String token) throws ServerException {
		List<String> fieldsTemplate = new ArrayList<>();

		// Crear el campo Introduccion
		fieldsTemplate.add(campoService.createField(templateId, "INTRODUCCION",
				DocumentoPlantillaCaracteristicaDTO.TEXTO, 1, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(0),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(0),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA, templateId,
				Propiedades.DESCRIPCION, fieldsTemplate.get(0), token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(0),
				Propiedades.TEXTO_LARGO, "1", token), token);

		// Crear el campo Ayudas
		fieldsTemplate.add(campoService.createField(templateId, "AYUDA EXTRA",
				DocumentoPlantillaCaracteristicaDTO.TEXTO, 2, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(1),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(1),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(1),
				Propiedades.TEXTO_LARGO, "1", token), token);

		// Crear el campo Ayudas
		fieldsTemplate.add(
				campoService.createField(templateId, "IMAGEN", DocumentoPlantillaCaracteristicaDTO.ARCHIVO, 3, token));
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(2),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(2),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA, templateId,
				Propiedades.CAMPO_EVIDENCIA, fieldsTemplate.get(2), token), token);

	}

	public void crearProducto(PedidoVentaDTO documento, String categoria, String token) throws ServerException {
		productoService.crearDesdeDocumento(documento, categoria, token);
	}

	public void crearBodega(PedidoVentaDTO documento) throws ServerException {
		bodegaService.crearDesdeDocumento(documento);
	}

	// Este metodo habia desaparecido pero es necesario para poder abrir los turnos
	// de una caja
	public void crearCuenta(PedidoVentaDTO dto, String token) throws ServerException {
		cuentaService.crearCuenta(dto, token);
	}
}
