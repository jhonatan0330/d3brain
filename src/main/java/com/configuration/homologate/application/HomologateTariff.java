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
import com.softure.tariff.application.base.TarifarioService;
import com.softure.tariff.domain.TarifarioDTO;
import com.softure.tariff.domain.TarifarioFilterDTO;

@Component
public class HomologateTariff {

	@Autowired @Lazy TarifarioService tariffService;
	
	public void createTariffFields(String templateId, String token, DocumentoPlantillaCaracteristicaSvc campoService, PropiedadSvc propertyService, CallDocumentCRUD crudService, String funcionario) throws ServerException {
		List<String> fieldsTemplate = new ArrayList<>();
		fieldsTemplate.add(
				campoService.createField(templateId, "NOMBRE", DocumentoPlantillaCaracteristicaDTO.TEXTO, 1, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(0),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.PLANTILLA, templateId,
				Propiedades.DESCRIPCION, fieldsTemplate.get(0), token), token);
		// fecha inicial
		fieldsTemplate.add(campoService.createField(templateId, "FECHA_INICIAL",
				DocumentoPlantillaCaracteristicaDTO.FECHA, 2, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(1),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		// fecha final
		fieldsTemplate.add(campoService.createField(templateId, "FECHA_FINAL",
				DocumentoPlantillaCaracteristicaDTO.FECHA, 3, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(2),
				Propiedades.PERMISO_CAMPO_OPCIONAL, "1", token), token);
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(2),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
	
		// bool producto opcional
		fieldsTemplate.add(campoService.createField(templateId, "PRODUCTO_OPCIONAL",
				DocumentoPlantillaCaracteristicaDTO.BINARIO, 4, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(3),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		// bool rango valores
		fieldsTemplate.add(campoService.createField(templateId, "RANGO_VALORES",
				DocumentoPlantillaCaracteristicaDTO.BINARIO, 5, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(4),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		// bool rango valores
		fieldsTemplate.add(campoService.createField(templateId, "RANGO_CANTIDADES",
				DocumentoPlantillaCaracteristicaDTO.BINARIO, 6, token));
		propertyService.guardarEnCasoQueNoExista(Propiedades.crearParametro(PropiedadValorDefinidoDTO.CAMPO, fieldsTemplate.get(5),
				Propiedades.PERMISO_CAMPO_MODIFICABLE, "1", token), token);
		sincronizeTariff(templateId, fieldsTemplate, token, crudService, funcionario);
	}
	

	private void sincronizeTariff(String templateId, List<String> fieldsTemplate, String token, CallDocumentCRUD crudService, String funcionario) throws ServerException {
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

					PedidoVentaCaracteristicaDTO fieldBoolProduct = new PedidoVentaCaracteristicaDTO();
					fieldBoolProduct.setCampo(fieldsTemplate.get(3));
					if (iTariff.getProductoOpcional())
						fieldBoolProduct.setValorNumero(BigDecimal.ONE);
					document.getCaracteristicas().add(fieldBoolProduct);

					PedidoVentaCaracteristicaDTO fieldValueRange = new PedidoVentaCaracteristicaDTO();
					fieldValueRange.setCampo(fieldsTemplate.get(4));
					if (iTariff.getRangoValores())
						fieldValueRange.setValorNumero(BigDecimal.ONE);
					document.getCaracteristicas().add(fieldValueRange);

					PedidoVentaCaracteristicaDTO fieldValueQuantity = new PedidoVentaCaracteristicaDTO();
					fieldValueQuantity.setCampo(fieldsTemplate.get(5));
					if (iTariff.getRangoCantidad())
						fieldValueQuantity.setValorNumero(BigDecimal.ONE);
					document.getCaracteristicas().add(fieldValueQuantity);

					document.setFuncionario(funcionario);
					document = crudService.saveWithoutTransaction(document, token, true);
					iTariff.setDocumento(document.getLlaveTabla());
					tariffService.update(iTariff);
				}
			}
		}
	}
	

	public void createTariff(PedidoVentaDTO document) throws ServerException {
		TarifarioFilterDTO filter = new TarifarioFilterDTO();
		filter.setDocumento(document.getLlaveTabla());
		TarifarioDTO newTariff = tariffService.getOne(filter);
		if (newTariff == null) {
			newTariff = new TarifarioDTO();
			newTariff.setDocumento(document.getLlaveTabla());
			newTariff.setFechaFinal(CallDocumentCommons.getValueDate(document, "FECHA_FINAL"));
			newTariff.setFechaInicial(CallDocumentCommons.getValueDate(document, "FECHA_INICIAL"));
			newTariff.setNombre(CallDocumentCommons.getValueText(document, "NOMBRE"));
			newTariff.setProductoOpcional(CallDocumentCommons.getValueBool(document, "PRODUCTO_OPCIONAL"));
			newTariff.setRangoCantidad(CallDocumentCommons.getValueBool(document, "RANGO_CANTIDADES"));
			newTariff.setRangoValores(CallDocumentCommons.getValueBool(document, "RANGO_VALORES"));
			tariffService.save(newTariff);
		} else {
			if (document.getEstado().compareTo(SharedConstants.STATE_INACTIVE) == 0) {
				if (newTariff.getState().compareTo(SharedConstants.STATE_INACTIVE) != 0) {
					newTariff.setState(SharedConstants.STATE_INACTIVE);
					tariffService.update(newTariff);
				}
			} else {
				newTariff.setFechaFinal(CallDocumentCommons.getValueDate(document, "FECHA_FINAL"));
				newTariff.setFechaInicial(CallDocumentCommons.getValueDate(document, "FECHA_INICIAL"));
				newTariff.setNombre(CallDocumentCommons.getValueText(document, "NOMBRE"));
				newTariff.setProductoOpcional(CallDocumentCommons.getValueBool(document, "PRODUCTO_OPCIONAL"));
				newTariff.setRangoCantidad(CallDocumentCommons.getValueBool(document, "RANGO_CANTIDADES"));
				newTariff.setRangoValores(CallDocumentCommons.getValueBool(document, "RANGO_VALORES"));
				newTariff.setState(SharedConstants.STATE_ACTIVE);
				tariffService.update(newTariff);
			}
		}
	}
}
