package com.configuration.homologate.application;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.document_execution.application.CallDocumentCRUD;
import com.softure.document_execution.application.CallDocumentCommons;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.property.application.PropiedadSvc;
import com.softure.property.domain.PropiedadValorDefinidoDTO;
import com.softure.tariff.application.base.TarifaSvc;
import com.softure.tariff.application.base.TarifarioService;
import com.softure.tariff.domain.TarifaDTO;
import com.softure.tariff.domain.TarifaFilterDTO;
import com.softure.tariff.domain.TarifarioDTO;
import com.softure.tariff.domain.TarifarioFilterDTO;

@Component
public class HomologateFee {

	@Autowired @Lazy private TarifarioService tariffService;
	@Autowired @Lazy private TarifaSvc feeService;
	
	public void createFeeFields(String templateId, String token, DocumentoPlantillaCaracteristicaSvc campoService, PropiedadSvc propertyService, CallDocumentCRUD crudService) throws ServerException {

		List<String> fieldsTemplate = new ArrayList<>();

		// ctar_tarifario varchar(32) NOT NULL,
		fieldsTemplate.add(campoService.createField(templateId, "TARIFARIO",
				DocumentoPlantillaCaracteristicaDTO.PROCESO, 1, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(0),
				Propiedades.PERMISO_CAMPO_RENDER, "1", token), token);
		// ctar_producto varchar(32) NULL,
		fieldsTemplate.add(
				campoService.createField(templateId, "PRODUCTO", DocumentoPlantillaCaracteristicaDTO.PROCESO, 2, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(1),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(1),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(1),
				Propiedades.PERMISO_CAMPO_RENDER, "1", token), token);
		// ctar_recurso varchar(32) NULL,
		/*fieldsTemplate.add(
				campoService.createField(templateId, "DIM_1", DocumentoPlantillaCaracteristicaDTO.PROCESO, 3, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(2),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(2),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(2),
				Propiedades.PERMISO_CAMPO_RENDER, "1", token), token);*/
		// ctar_dimension2 varchar(32) NULL,
		/*fieldsTemplate.add(
				campoService.createField(templateId, "DIM_2", DocumentoPlantillaCaracteristicaDTO.PROCESO, 4, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(3),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(3),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(3),
				Propiedades.PERMISO_CAMPO_RENDER, "1", token), token);
		// ctar_dimension3 varchar(32) NULL
		fieldsTemplate.add(
				campoService.createField(templateId, "DIM_3", DocumentoPlantillaCaracteristicaDTO.PROCESO, 5, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(4),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(4),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(4),
				Propiedades.PERMISO_CAMPO_RENDER, "1", token), token);
		// ctar_dimension4 varchar(32) NULL,
		fieldsTemplate.add(
				campoService.createField(templateId, "DIM_4", DocumentoPlantillaCaracteristicaDTO.PROCESO, 6, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(5),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(5),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(5),
				Propiedades.PERMISO_CAMPO_RENDER, "1", token), token);*/
		// mtar_valorminimo numeric(18, 6) DEFAULT 0 NOT NULL,
		fieldsTemplate.add(campoService.createField(templateId, "VALOR_MINIMO",
				DocumentoPlantillaCaracteristicaDTO.NUMERO, 3, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(2),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(2),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		// mtar_valor numeric(18, 6) DEFAULT 0 NOT NULL,
		fieldsTemplate.add(
				campoService.createField(templateId, "VALOR", DocumentoPlantillaCaracteristicaDTO.NUMERO, 4, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(3),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(3),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		// mtar_valormaximo numeric(18, 6) DEFAULT 0 NOT NUL,
		fieldsTemplate.add(campoService.createField(templateId, "VALOR_MAXIMO",
				DocumentoPlantillaCaracteristicaDTO.NUMERO, 5, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(4),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(4),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		// ntar_cantidadmaxima int4 DEFAULT 0 NOT NULL,
		fieldsTemplate.add(campoService.createField(templateId, "CANTIDAD_MINIMA",
				DocumentoPlantillaCaracteristicaDTO.NUMERO, 6, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(5),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(5),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		// ntar_cantidadminima int4 DEFAULT 0 NOT NULL,
		fieldsTemplate.add(campoService.createField(templateId, "CANTIDAD_MAXIMA",
				DocumentoPlantillaCaracteristicaDTO.NUMERO, 7, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(6),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(6),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		// mtar_totalminimo numeric(18, 6) DEFAULT 0 NOT NULL,
		fieldsTemplate.add(campoService.createField(templateId, "TOTAL_MINIMO",
				DocumentoPlantillaCaracteristicaDTO.NUMERO, 8, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(7),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(7),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);

		sincronizeFee(templateId, fieldsTemplate, token, crudService);
	}
	
	private void sincronizeFee(String templateId, List<String> fieldsTemplate, String token, CallDocumentCRUD crudService) throws ServerException {
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
						/*PedidoVentaCaracteristicaDTO fieldDim2 = new PedidoVentaCaracteristicaDTO();
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
						document.getCaracteristicas().add(fieldDim4);*/
						// mtar_valorminimo numeric(18, 6) DEFAULT 0 NOT NULL,
						PedidoVentaCaracteristicaDTO fieldValueMin = new PedidoVentaCaracteristicaDTO();
						fieldValueMin.setCampo(fieldsTemplate.get(3));
						fieldValueMin.setValorNumero(iFee.getValorMinimo());
						document.getCaracteristicas().add(fieldValueMin);
						// mtar_valor numeric(18, 6) DEFAULT 0 NOT NULL,
						PedidoVentaCaracteristicaDTO fieldValue = new PedidoVentaCaracteristicaDTO();
						fieldValue.setCampo(fieldsTemplate.get(4));
						fieldValue.setValorNumero(iFee.getValor());
						document.getCaracteristicas().add(fieldValue);
						// mtar_valormaximo numeric(18, 6) DEFAULT 0 NOT NUL,
						PedidoVentaCaracteristicaDTO fieldValueMax = new PedidoVentaCaracteristicaDTO();
						fieldValueMax.setCampo(fieldsTemplate.get(5));
						fieldValueMax.setValorNumero(iFee.getValorMaximo());
						document.getCaracteristicas().add(fieldValueMax);
						
						// ntar_cantidadminima int4 DEFAULT 0 NOT NULL,
						PedidoVentaCaracteristicaDTO fieldQuantityMin = new PedidoVentaCaracteristicaDTO();
						fieldQuantityMin.setCampo(fieldsTemplate.get(6));
						fieldQuantityMin.setValorNumero(new BigDecimal(iFee.getCantidadMinima()));
						document.getCaracteristicas().add(fieldQuantityMin);
						// ntar_cantidadmaxima int4 DEFAULT 0 NOT NULL,
						PedidoVentaCaracteristicaDTO fieldQuantityMax = new PedidoVentaCaracteristicaDTO();
						fieldQuantityMax.setCampo(fieldsTemplate.get(7));
						fieldQuantityMax.setValorNumero(new BigDecimal(iFee.getCantidadMaxima()));
						document.getCaracteristicas().add(fieldQuantityMax);
						
						// mtar_totalminimo numeric(18, 6) DEFAULT 0 NOT NULL,
						PedidoVentaCaracteristicaDTO fieldTotalMin = new PedidoVentaCaracteristicaDTO();
						fieldTotalMin.setCampo(fieldsTemplate.get(8));
						fieldTotalMin.setValorNumero(iFee.getTotalMinimo());
						document.getCaracteristicas().add(fieldTotalMin);

						document.setFuncionario(feeService.getUserFlex(token));
						document = crudService.saveWithoutTransaction(document, token, true);
						iFee.setDocumento(document.getLlaveTabla());
						feeService.update(iFee);
					}	
				}
			}
		}
	}
	
	public void createFee(PedidoVentaDTO document, String token, CallDocumentCRUD crudService) throws ServerException {
		TarifaFilterDTO filter = new TarifaFilterDTO();
		filter.setDocumento(document.getLlaveTabla());
		//Sucede que el actualizar crea una nueva tarifa asi que toca filtrar por activo
		filter.setEstado(SharedConstants.STATE_ACTIVE);
		TarifaDTO newFee = feeService.consultaUnica(filter);
		if (newFee == null) {
			newFee = new TarifaDTO();
			newFee.setDocumento(document.getLlaveTabla());
			newFee.setTarifarioDocumento(CallDocumentCommons.getValueOption(document, "TARIFARIO"));
			newFee.setProductoDocumento(CallDocumentCommons.getValueOption(document, "PRODUCTO"));
			newFee.setRecurso(CallDocumentCommons.getValueOption(document, "DIM_1"));
			newFee.setDimension2(CallDocumentCommons.getValueOption(document, "DIM_2"));
			newFee.setDimension3(CallDocumentCommons.getValueOption(document, "DIM_3"));
			newFee.setDimension4(CallDocumentCommons.getValueOption(document, "DIM_4"));
			newFee.setValorMinimo(CallDocumentCommons.getValueNumber(document, "VALOR_MINIMO"));
			newFee.setValor(CallDocumentCommons.getValueNumber(document, "VALOR"));
			newFee.setValorMaximo(CallDocumentCommons.getValueNumber(document, "VALOR_MAXIMO"));
			newFee.setCantidadMinima(CallDocumentCommons.getValueNumberInt(document, "CANTIDAD_MINIMA"));
			newFee.setCantidadMaxima(CallDocumentCommons.getValueNumberInt(document, "CANTIDAD_MAXIMA"));
			newFee.setTotalMinimo(CallDocumentCommons.getValueNumber(document, "TOTAL_MINIMO"));
			feeService.guardar(newFee, token);
		} else {
			if (document.getEstado().compareTo(SharedConstants.STATE_INACTIVE) == 0) {
				if (newFee.getEstado().compareTo(SharedConstants.STATE_INACTIVE) != 0) {
					newFee.setEstado(SharedConstants.STATE_INACTIVE);
					feeService.update(newFee);
				}
			} else {
				newFee.setTarifarioDocumento(CallDocumentCommons.getValueOption(document, "TARIFARIO"));
				newFee.setProductoDocumento(CallDocumentCommons.getValueOption(document, "PRODUCTO"));
				newFee.setRecurso(CallDocumentCommons.getValueOption(document, "DIM_1"));
				newFee.setDimension2(CallDocumentCommons.getValueOption(document, "DIM_2"));
				newFee.setDimension3(CallDocumentCommons.getValueOption(document, "DIM_3"));
				newFee.setDimension4(CallDocumentCommons.getValueOption(document, "DIM_4"));
				newFee.setValorMinimo(CallDocumentCommons.getValueNumber(document, "VALOR_MINIMO"));
				newFee.setValor(CallDocumentCommons.getValueNumber(document, "VALOR"));
				newFee.setValorMaximo(CallDocumentCommons.getValueNumber(document, "VALOR_MAXIMO"));
				newFee.setCantidadMinima(CallDocumentCommons.getValueNumberInt(document, "CANTIDAD_MINIMA"));
				newFee.setCantidadMaxima(CallDocumentCommons.getValueNumberInt(document, "CANTIDAD_MAXIMA"));
				newFee.setTotalMinimo(CallDocumentCommons.getValueNumber(document, "TOTAL_MINIMO"));
				
				//if(newFee.getValorMinimo()==null) newFee.setValorMinimo(newFee.getValor());
				//if(newFee.getValor()==null) newFee.setValor(BigDecimal.ZERO);

				newFee.setEstado(SharedConstants.STATE_ACTIVE);
				feeService.actualizar(newFee, token);
			}
		}
	}
	
	
}
