package com.softure.document_execution.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.shared.domain.ServerException;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaFilterDTO;
import com.softure.java.services.SoftureUtil;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.property.domain.PropiedadDTO;

@Component
public class CallDocumentListBySQLFunction {
	
	@Autowired private PedidoVentaCaracteristicaSvc campoService;
	@Autowired private CallDocumentListWithFilters listDocumentWithFiltersFunction;
	
	public List<PedidoVentaDTO> execute(
			DocumentoPlantillaCaracteristicaDTO pBase, 
			DocumentoPlantillaCaracteristicaDTO campo, 
			List<PedidoVentaCaracteristicaDTO> dependientes,
			PedidoVentaFilterDTO entityFilter,
			PropiedadDTO funcionConsulta, 
			String campoValor,
			String token) throws ServerException{
		//En caso que sea funcion y tenga una dependencia va a aenviar ese valor como llave tabla
		String[] cars = { Propiedades.DEPENDE, Propiedades.MODIFICAR_CAMPO, Propiedades.INFORMATIVE_DATA,
				Propiedades.RELACIONAR_DOCUMENTOS, Propiedades.RETIRAR_DOCUMENTOS,
				Propiedades.UPDATE_INFORMATIVE_FIELD, Propiedades.FECHA_MAXIMA_CAMPO, Propiedades.FECHA_MINIMA_CAMPO };
		List<PropiedadDTO> codigoDepende = Propiedades.obtenerVariosParametro(campo, cars);
		if(entityFilter==null) entityFilter = new PedidoVentaFilterDTO(); // en tipo proceos autoload no sabia que filtrar
		if(codigoDepende!=null){//Coloco las dependencias
			campoService.validarDependientes(campo, dependientes);
			dependientes = campoService.ordenarAlfabeticaDepende(dependientes);
			dependientes = campoService.removeDuplicateDepends(dependientes);
			if(dependientes.get(0).getValorOpcion()!=null)//Se me perdia la referencia y no se porque
				entityFilter.setLlaveTabla(new String(dependientes.get(0).getValorOpcion()));
			List<PedidoVentaCaracteristicaDTO> expedientesMultiples = new ArrayList<PedidoVentaCaracteristicaDTO>();
			for (PedidoVentaCaracteristicaDTO iDependiente : dependientes) {
				if(iDependiente.getValorOpcion()==null) {
					if(iDependiente.getExpedientes()!=null ) {
						//Esto aplica para los campos multiples
						for (PedidoVentaDTO iExpediente : iDependiente.getExpedientes()) {
							PedidoVentaCaracteristicaDTO pd = new PedidoVentaCaracteristicaDTO();
							pd.setValorOpcion(iExpediente.getLlaveTabla());
							expedientesMultiples.add(pd);
						}	
					}
					if(iDependiente.getValorText()==null) {
						if(iDependiente.getValorFecha()!=null) {
							iDependiente.setValorText(SoftureUtil.formatDateTime(iDependiente.getValorFecha()));
						}
					}
				}
			}
			if(expedientesMultiples.size()!=0) dependientes.addAll(expedientesMultiples);
		}
		//entityFilter.setDescripcion(funcionConsulta.getLlaveTabla());
		List<PedidoVentaDTO> result = listDocumentWithFiltersFunction.listarExpedientesDisponiblesDocumentoFuncion(entityFilter, 
				funcionConsulta.getLlaveTabla(), dependientes);
		return listDocumentWithFiltersFunction.listadoCompleto( result, token, campoValor);
	}

}
