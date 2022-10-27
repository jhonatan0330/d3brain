package com.softure.logisticpymes.services.refactor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.domain.dto.DocumentoPlantillaCaracteristicaDTO;
import com.softure.logisticpymes.domain.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.domain.dto.PedidoVentaDTO;
import com.softure.logisticpymes.domain.dto.PropiedadDTO;
import com.softure.logisticpymes.domain.filter.PedidoVentaFilterDTO;
import com.softure.logisticpymes.services.PedidoVentaCaracteristicaSvc;
import com.softure.logisticpymes.services.adapter.Propiedades;

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
		List<PropiedadDTO> codigoDepende = Propiedades.obtenerVariosParametro(pBase, Propiedades.DEPENDE);
		if(entityFilter==null) entityFilter = new PedidoVentaFilterDTO(); // en tipo proceos autoload no sabia que filtrar
		if(codigoDepende!=null){//Coloco las dependencias
			campoService.validarDependientes(campo, dependientes);
			dependientes = campoService.ordenarAlfabeticaDepende(dependientes);
			if(dependientes.get(0).getValorOpcion()!=null)//Se me perdia la referencia y no se porque
				entityFilter.setLlaveTabla(new String(dependientes.get(0).getValorOpcion()));
			List<PedidoVentaCaracteristicaDTO> expedientesMultiples = new ArrayList<PedidoVentaCaracteristicaDTO>();
			for (PedidoVentaCaracteristicaDTO iDependiente : dependientes) {
				if(iDependiente.getValorOpcion()==null && iDependiente.getExpedientes()!=null) {
					//Esto aplica para los campos multiples
					for (PedidoVentaDTO iExpediente : iDependiente.getExpedientes()) {
						PedidoVentaCaracteristicaDTO pd = new PedidoVentaCaracteristicaDTO();
						pd.setValorOpcion(iExpediente.getLlaveTabla());
						expedientesMultiples.add(pd);
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
