package com.softure.document_execution.application;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softure.document_execution.application.field.AuxiliarProcesoBodega;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaFilterDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaFilterDTO;
import com.softure.java.cons.ConstantesGenerales;
import com.softure.java.dto.exception.ServerException;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.property.domain.PropiedadDTO;

@Component
public class CallDocumentListFromFieldProcess {
	
	@Autowired private CallDocumentListWithFilters listDocumentWithFiltersFunction;
	@Autowired private CallDocumentListBySQLFunction listDocumentBySQLFunction;
	@Autowired private AuxiliarProcesoBodega tipoBodega;
	@Autowired private PedidoVentaSvc pedidoService;
	@Autowired private DocumentoRelacionExpedienteSvc relacionExpedienteService;

	// Es muy importante que venga el campo con todas las propiedadses
	public PedidoVentaCaracteristicaFilterDTO execute(PedidoVentaCaracteristicaFilterDTO pCampo, DocumentoPlantillaCaracteristicaDTO pBase) throws ServerException {		
		String campoHeredado1 = Propiedades.obtenerValor(pBase, Propiedades.CAMPO_HEREDADO_1);
		String multiple = Propiedades.obtenerValor(pBase, Propiedades.MULTIPLE);
		String campoValor = Propiedades.obtenerValor(pBase, Propiedades.PROCESO_VALOR);//Principalmente para los formularios que tengan valor
		PropiedadDTO funcionConsulta = Propiedades.obtenerParametro(pBase, Propiedades.PROCESO_FUNCION_SQL);
		List<PropiedadDTO> codigoDepende = Propiedades.obtenerVariosParametro(pBase, Propiedades.DEPENDE);
		List<PedidoVentaDTO> resultados = null;
		if(multiple.isEmpty() && campoHeredado1.isEmpty()){//Consulto opciones de combo
			//Esto es de los tipo bodega
			String bodegaFija = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.BODEGA_FIJA); 
			if(!bodegaFija.isEmpty()) pCampo.setValorOpcion( tipoBodega.consultarBodegaBaseFija(bodegaFija) );
			//Movi esto porque simpre que tenga opcion va a consultar uno creo que tengo un problema con los que dependen o algo asi
			if(pCampo.getValorOpcion()!=null){//Si tiene valor opcion es porque ya esta seleccionado
				PedidoVentaDTO documentoActual = pedidoService.consultaXId(pCampo.getValorOpcion());
				if(documentoActual==null) throw new ServerException("Documento opcion no se encuentra por llave." + pBase.getNombre());
				resultados = new ArrayList<PedidoVentaDTO>();
				resultados.add(documentoActual);
				if(!campoValor.isEmpty()) {
					//Coloco valores
					listDocumentWithFiltersFunction.listadoCompleto(resultados, pCampo.getSecurityToken(), (campoValor.isEmpty())?null:campoValor);
					//Para las cuentas les lleno el valor aqui
					if(campoValor.compareTo("0")==0 && resultados.get(0)!=null &&resultados.get(0).getDinero()!=null)
						pCampo.setValorNumeroMax(resultados.get(0).getDinero().getValorTotal());
				}
					
			}else {
				PedidoVentaFilterDTO entityFilter = new PedidoVentaFilterDTO();
				
				entityFilter.setSecurityToken(pCampo.getSecurityToken());
				entityFilter.setFiltroParametro(pCampo.getFiltroParametro());//Coloco los filtros necesarios
				if(entityFilter.getFiltroParametro()!=null && entityFilter.getFiltroParametro().compareTo("*")==0)entityFilter.setFiltroParametro(null);
				entityFilter.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
				
				if(funcionConsulta == null) {
					List<PropiedadDTO> plantillasAuxiliares = Propiedades.obtenerVariosParametro(pCampo.getCampoDTO(), Propiedades.PLANTILLA_AUXILIAR);
					if(plantillasAuxiliares!=null && !plantillasAuxiliares.isEmpty()){
						if(pBase!=null){//Esto aplica para autoload de los productos con ocion de seleccion
							if(codigoDepende!=null){//Coloco las dependencias
								if(codigoDepende.get(0).getValor().compareTo(ConstantesGenerales.USUARIO)!=0 
										&&  Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.BODEGA_MOVIMIENTO).isEmpty()){
									//Valido que la cantidad de dependientes este correcta
									if(pCampo.getDependientes()==null || pCampo.getDependientes().isEmpty())throw new ServerException("En el campo " + pBase.getNombre() + " Revise los dependientes.\n " + pCampo.getCampoDTO().getNombre());
									if(pCampo.getDependientes().size()!=codigoDepende.size()) throw new ServerException("En el campo " + pBase.getNombre() + "El numero de dependientes no concuerda. Tipo Expediente" + codigoDepende.size());
									//Al parecer solo funciona para un dependiente
									entityFilter.setCaracteristicas(new ArrayList<PedidoVentaCaracteristicaDTO>());
									entityFilter.getCaracteristicas().add(colocarFiltroDocumentoAuxiliar(pCampo.getDependientes().get(0).getValorOpcion()));
									//pCampo.setValorOpcion(null);//Creo que con esto soluciono las modificaciones de documentos de dependencia
								}
							}else{
								if(pCampo.getDocumento()!=null) {//Para que coloque esto
									entityFilter.setCaracteristicas(new ArrayList<PedidoVentaCaracteristicaDTO>());
									entityFilter.getCaracteristicas().add(colocarFiltroDocumentoAuxiliar(pCampo.getDocumento()));	
								}
							}
						}
						//entityFilter.setPlantilla(documentoAuxiliar);
						entityFilter.setCampoOrigen(pBase.getLlaveTabla());
					}
					resultados = listDocumentWithFiltersFunction.listarAvanzado(entityFilter);
				}else {
					resultados = listDocumentBySQLFunction.execute(pBase, pCampo.getCampoDTO(), pCampo.getDependientes(), entityFilter, funcionConsulta, campoValor, pCampo.getSecurityToken());
				}
			}
			if(pBase!=null){
				pBase.setDocumentos(resultados);
				pCampo.setCampoDTO(pBase);
			}else{//Esto aplica para autoload de los productos con ocion de seleccion
				pCampo.getCampoDTO().setDocumentos(resultados);
			}
			if(pCampo.getCampoDTO().getDocumentos()==null)throw new ServerException("Comuniquese con el desarrollador los documentos resultado de la consulta completa no pueden ser nulos");
			return pCampo;
		}else{ 
			if(pCampo.getDocumento()==null) {//Si es multiple y es nuevo no consulte nada
				
				pCampo.setExpedientes(new ArrayList<PedidoVentaDTO>());
			}else {//Aqui solo van los documentos actuales
				if(campoValor.isEmpty() || campoValor=="1" || campoValor == "2") campoValor = null;
				resultados = listDocumentWithFiltersFunction.listarExpedientesPertenecenCampo(pCampo.getLlaveTabla(), pCampo.getSecurityToken(), campoValor);
				pCampo.setExpedientes(resultados);
				CallDocumentCommons.calcularValoresTotalesCampo(pCampo, campoValor, relacionExpedienteService);
			}
			return pCampo;
		}
	}
	
	private PedidoVentaCaracteristicaDTO colocarFiltroDocumentoAuxiliar(String documento){
		//Funcion creada para colocar los filtros
		PedidoVentaCaracteristicaDTO pvc = new PedidoVentaCaracteristicaDTO();
		pvc.setValorOpcion(documento);
		return pvc;
	}

}
