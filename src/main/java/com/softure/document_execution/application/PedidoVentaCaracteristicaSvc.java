package com.softure.document_execution.application;

import java.util.List;

import java.util.ArrayList;
import java.util.Date;
import java.math.BigDecimal;

import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.property.domain.PropiedadDTO;
import com.softure.property.domain.RelacionInternaDTO;
import com.softure.java.services.ProcessTemplate;
import com.softure.java.services.SoftureUtil;

import jakarta.annotation.PostConstruct;
import com.softure.logisticpymes.application.BasicSvc;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.shared.domain.ServerException;
import com.shared.domain.SharedConstants;
import com.softure.document_execution.application.field.CampoAdaptador;
import com.softure.document_execution.application.field.Propiedades;
import com.softure.document_execution.domain.DetallePedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaFilterDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_execution.infrastructure.PedidoVentaCaracteristicaMapper;

@Service("pedidoVentaCaracteristicaService")
public class PedidoVentaCaracteristicaSvc extends BasicSvc<PedidoVentaCaracteristicaDTO, PedidoVentaCaracteristicaFilterDTO> {
	
	@Autowired @Lazy 
	private PedidoVentaCaracteristicaMapper pedidoVentaCaracteristicaMapper;
	@Autowired @Lazy 
	private ProcessTemplate templatesService;
	
	@Autowired @Lazy  private CampoAdaptador adaptador;
	@Autowired @Lazy  private DetallePedidoVentaSvc detallePedidoVentaService;
	@Autowired @Lazy  private DocumentoPlantillaCaracteristicaSvc campoDocumentoService;

	@Override
	public PedidoVentaCaracteristicaDTO consultaXId(String llave) throws ServerException {
		if(llave==null) throw new ServerException("La llave del DTO se encuentra vacia. PedidoVentaCaracteristica");
		PedidoVentaCaracteristicaFilterDTO dto = new PedidoVentaCaracteristicaFilterDTO();
		dto.setLlaveTabla(llave);
		return pedidoVentaCaracteristicaMapper.consultar(dto);
	}

	@PostConstruct
	public void initIt() throws Exception {
	  this.mapper = pedidoVentaCaracteristicaMapper;
	}
	

	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PedidoVentaCaracteristicaDTO actualizar( PedidoVentaCaracteristicaDTO dto, String token) throws ServerException {
		// BEGIN PedidoVentaCaracteristica_actualizar
		if(dto.getValorNumero()!=null && dto.getValorNumero().compareTo(BigDecimal.ZERO)==0) dto.setValorNumero(null);
		return update(dto);
		//pues si lo uso, pero lo debo qutar
		//throw new ServerException("En teoria no se usa este metodo");
		// END PedidoVentaCaracteristica_actualizar
	}
	
	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PedidoVentaCaracteristicaDTO inactivar(PedidoVentaCaracteristicaDTO dto, String token) throws ServerException {
		// BEGIN PedidoVentaCaracteristica_inactivar
		if(dto.getTransaccionInactivo()== null) throw new ServerException("Se encesita la transaccion de inactivar");
		if(dto.getPrincipal()==null) throw new ServerException("Se necesita adjuntar el principal para identificar si es historico");
		return pedidoVentaCaracteristicaMapper.inactivarCampoHistorico(dto.getLlaveTabla(), dto.getTransaccionInactivo(), (dto.getPrincipal().getHistorico()==null)?null:"Historico");
		// END PedidoVentaCaracteristica_inactivar
	}
	
	
	public PedidoVentaCaracteristicaDTO completarDatosBase(PedidoVentaCaracteristicaFilterDTO dto)throws ServerException{
		// BEGIN region completarDatosBase
		return transformFilter2VO( adaptador.consultarDatosBase(dto)) ;
		// END region completarDatosBase
	}

	@Override
	@Transactional(value = "transactionManager", rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PedidoVentaCaracteristicaDTO guardar(PedidoVentaCaracteristicaDTO dto, String token) throws ServerException {
		// BEGIN PedidoVentaCaracteristica_guardar
		if(dto.getPrincipal()==null) throw new ServerException("Se necesita adjuntar el principal para identificar si es historico");
		if(dto.getValorText()!=null && dto.getValorText().length()>4000) throw new ServerException("Se excedio la cantidad de caracteres del texto. (4000)");
		if(dto.getValorNumero()!=null && dto.getValorNumero().compareTo(BigDecimal.ZERO)==0) dto.setValorNumero(null);
		if(dto.getValorNumero()!=null && dto.getValorNumero().compareTo(new BigDecimal(9999999999999999.0))>0) throw new ServerException("Se excedio del numero maximo");
		if(dto.getPrincipal().getHistorico() == null) {
			saveSimple(dto);
		} else {
			dto.setLlaveTabla(generarLlave());
			pedidoVentaCaracteristicaMapper.insertarHistorico(dto);
		}
		DocumentoPlantillaCaracteristicaDTO base = dto.getCampoDTO();
		dto.setCampoDTO(base);
		return dto;
		// END PedidoVentaCaracteristica_guardar
	}

	public List<PedidoVentaCaracteristicaDTO> listar2Documento(String documento, Integer historico)
			throws ServerException {//La plantilla es para optimizar la consultas de la particion
		return listar2Documento(documento, historico, null);
	}
	
	public List<PedidoVentaCaracteristicaDTO> listar2Documento(String documento, Integer historico, String campo)
			throws ServerException {//La plantilla es para optimizar la consultas de la particion
		if(documento ==null) return null;
		if( historico == null || historico == 0 ) {
			return pedidoVentaCaracteristicaMapper.listar2Documento(documento, campo);
		}else {
			return pedidoVentaCaracteristicaMapper.listar2DocumentoHistorico(documento, campo);
		}
	}
	
	public List<PedidoVentaCaracteristicaDTO> readCompleteFields(String documentId, List<DocumentoPlantillaCaracteristicaDTO> templateFields, Integer historico, String token)
			throws ServerException {
		List<PedidoVentaCaracteristicaDTO> result = listar2Documento(documentId, historico, null);
		if(result==null || result.isEmpty()) return result;
		for (DocumentoPlantillaCaracteristicaDTO iFieldTemplateDTO : templateFields) {
			for (PedidoVentaCaracteristicaDTO iCurrentField : result) {
				if(iFieldTemplateDTO.getLlaveTabla().compareTo(iCurrentField.getCampo())==0) {
					iCurrentField.setCampoDTO(iFieldTemplateDTO);
					if(iFieldTemplateDTO.getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.PRODUCTO)==0) {
						iCurrentField.setDetalles(detallePedidoVentaService.listar2Documento(documentId));
						for (DetallePedidoVentaDTO detalleDocumento : iCurrentField.getDetalles()) {
							detallePedidoVentaService.createFieldsProduct(detalleDocumento, token, Propiedades.obtenerValor(iFieldTemplateDTO, Propiedades.ITEM_DETAIL_FORM_VISIBLE));	
						}
					}
						
				}
			}
		}
		return result;
	}
	
	public List<PedidoVentaCaracteristicaDTO> listarGestionables(String documento)
			throws ServerException {//La plantilla es para optimizar la consultas de la particion
		return pedidoVentaCaracteristicaMapper.listarGestionables(documento);
	}
	
	public List<PedidoVentaCaracteristicaDTO> listarParaReporte(String documento)throws ServerException {
		return pedidoVentaCaracteristicaMapper.listarParaReporte(documento);
	}
	
	public List<PedidoVentaCaracteristicaDTO> listarParaMensaje(String documento, String plantilla, String propiedad, String modificador)throws ServerException {
		return pedidoVentaCaracteristicaMapper.listarParaMensaje(documento, plantilla, propiedad, modificador);
	}
	
	public List<PedidoVentaCaracteristicaDTO> listar2Gestor(String documento, String transaccion)throws ServerException {
		return pedidoVentaCaracteristicaMapper.listarParaGestor(documento, transaccion);
	}
	
	public List<PedidoVentaCaracteristicaDTO> listar2DocumentoVisible(List<PedidoVentaDTO> documentos)
			throws ServerException {//La plantilla es para optimizar la consultas de la particion
		if(documentos==null || documentos.isEmpty()) return null;
		List<PedidoVentaDTO> produccion = null;
		List<PedidoVentaDTO> historicos = null;
		for (PedidoVentaDTO iDocumento : documentos) {
			if(iDocumento.getHistorico()==null) {
				if(produccion==null) produccion = new ArrayList<PedidoVentaDTO>();
				produccion.add(iDocumento);
			}else {
				if(historicos==null) historicos = new ArrayList<PedidoVentaDTO>();
				historicos.add(iDocumento);
			}
		}
		return pedidoVentaCaracteristicaMapper.listar2DocumentoVisible(produccion, historicos);
	}
	
	// En los documentos lo importante es el valor opcion  que es el id que va a buscar 
	public List<PedidoVentaCaracteristicaDTO> listar2getMessageMailDestiny(List<PedidoVentaCaracteristicaDTO> documentIds, List<RelacionInternaDTO> fieldId)
			throws ServerException {
		if(documentIds==null || documentIds.isEmpty() || fieldId==null || fieldId.isEmpty()) return null;
		List<PedidoVentaCaracteristicaDTO> fieldsInternal = pedidoVentaCaracteristicaMapper.listar2getMessageMailDestiny(documentIds, fieldId);
		//Existia un error al consultar una lista larga de relaciones para un mensaje
		//Esa lista en el segundo nivel de un campo multiple se bloqueaba porque validaba que el campoDTO no fuera nulo
		//asiq ue toco empezar a colocarles a todos el campoDTO
		if(fieldsInternal==null) return null;
		for (PedidoVentaCaracteristicaDTO iField : fieldsInternal) {
			if(iField.getCampoDTO()==null) {
				iField.setCampoDTO(campoDocumentoService.consultaXId(iField.getCampo()));
			}
		}
		return fieldsInternal;
	}
	

	public PedidoVentaCaracteristicaDTO buscarActivo(PedidoVentaCaracteristicaDTO dto, Integer historico) throws ServerException {
		if(dto==null || dto.getDocumento()==null || dto.getCampo()==null) throw new ServerException("Error al consultar el campo previo por falta de datos");
		List<PedidoVentaCaracteristicaDTO> filter = listar2Documento(dto.getDocumento(), historico, dto.getCampo());
		if (filter != null && filter.size()==1) {
			return filter.get(0);
		}
		return null;
	}
	
	public BigDecimal calcularNumeroFuncion(PropiedadDTO propFunction, String documento, String token, List<PedidoVentaCaracteristicaDTO> dependientes) throws ServerException {
		if(propFunction ==null)	return BigDecimal.ZERO;
		if(Propiedades.isFunctionNotFreeMarker(propFunction.getValor())) {
			try {
				
				List<PedidoVentaCaracteristicaDTO> dependientesOrdenados = null;
				if(dependientes!=null) {
					// En formularios de pedidos de bbx se envia a calclualr el flete sin dependientes
					// al final se carga pero en ibatis falla por el arrray vacio
					dependientesOrdenados = ordenarAlfabeticaDepende(dependientes);
					dependientesOrdenados = removeDuplicateDepends(dependientesOrdenados);
					includeDocumentArray(dependientesOrdenados);
					if(dependientesOrdenados !=null && dependientesOrdenados.isEmpty()) dependientesOrdenados = null;					
				}
				return  pedidoVentaCaracteristicaMapper.calcularNumeroFuncion(SoftureUtil.formatFunction(propFunction.getLlaveTabla()), documento, token, dependientesOrdenados);
			} catch (Exception e) {
				throw new ServerException(e.getMessage(), "");
			}	
		}else {
			String parameters = "";
			for (PedidoVentaCaracteristicaDTO iProp : dependientes) {
				parameters = parameters + SharedConstants.PUNTO_COMA_DOBLE + "R_" + iProp.getCampoDTO().getCodigo() + SharedConstants.IGUAL;
				
				if(iProp.getCampoDTO().getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.NUMERO)==0) {
					parameters = parameters + ((iProp.getValorNumero()==null)?"0":iProp.getValorNumero().toString());
				}else {
					parameters = parameters + ((iProp.getValorText()==null)?"0":iProp.getValorText());
				}
			}
			try {
				return new BigDecimal(templatesService.generateOutputFile(propFunction.getValor(), parameters));	
			}catch(NumberFormatException nf) {
				return BigDecimal.ZERO;	
			}
			
		}
	}

	private void includeDocumentArray(List<PedidoVentaCaracteristicaDTO> dependientesOrdenados) {
		List<PedidoVentaCaracteristicaDTO> expedientesMultiples = new ArrayList<PedidoVentaCaracteristicaDTO>();
		for (PedidoVentaCaracteristicaDTO iDependiente : dependientesOrdenados) {
			if(iDependiente.getValorOpcion()==null) {
				if(iDependiente.getExpedientes()!=null ) {
					//Esto aplica para los campos multiples
					for (PedidoVentaDTO iExpediente : iDependiente.getExpedientes()) {
						//Esto porque algunos expedientes los estamos descargando
						if(iExpediente.getEstado()==null || iExpediente.getEstado().compareTo(SharedConstants.STATE_INACTIVE)!=0) {
							PedidoVentaCaracteristicaDTO pd = new PedidoVentaCaracteristicaDTO();
							pd.setValorOpcion(iExpediente.getLlaveTabla());
							expedientesMultiples.add(pd);	
						}
					}	
				}
				if(iDependiente.getValorText()==null) {
					if(iDependiente.getValorFecha()!=null) {
						iDependiente.setValorText(SoftureUtil.formatDateTime(iDependiente.getValorFecha()));
					}
				}
			}
		}
		if(expedientesMultiples.size()!=0) dependientesOrdenados.addAll(expedientesMultiples);
	}
	
	public Date calcularFechaFuncion(PropiedadDTO sqlFuncionDecision, String documento, String token, List<PedidoVentaCaracteristicaDTO> dependientes) throws ServerException {
		if(sqlFuncionDecision ==null)	return null;
		if(Propiedades.isFunctionNotFreeMarker(sqlFuncionDecision.getValor())) {
			try {
				List<PedidoVentaCaracteristicaDTO> dependientesOrdenados = ordenarAlfabeticaDepende(dependientes);
				if(dependientesOrdenados !=null && dependientesOrdenados.isEmpty()) dependientesOrdenados = null;
				return  pedidoVentaCaracteristicaMapper.calcularFechaFuncion(SoftureUtil.formatFunction(sqlFuncionDecision.getLlaveTabla()), documento, token, dependientesOrdenados);
			} catch (Exception e) {
				throw new ServerException(e.getMessage(), "");
			}
		}else {
			String parameters = "";
			for (PedidoVentaCaracteristicaDTO iProp : dependientes) {
				parameters = parameters + SharedConstants.PUNTO_COMA_DOBLE + "R_" + iProp.getCampoDTO().getCodigo() + SharedConstants.IGUAL;
				
				if(iProp.getCampoDTO().getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.NUMERO)==0) {
					parameters = parameters + ((iProp.getValorNumero()==null)?"0":iProp.getValorNumero().toString());
				}else {
					parameters = parameters + ((iProp.getValorText()==null)?"0":iProp.getValorText());
				}
			}
			return SoftureUtil.toDate(templatesService.generateOutputFile(sqlFuncionDecision.getValor(), parameters));
		}
		
	}
	
	
	public PedidoVentaCaracteristicaDTO consultarCampoCroquis(String estructuraId)
			throws ServerException {
		return pedidoVentaCaracteristicaMapper.consultarCampoCroquis(estructuraId);
	}
	
	// Aqui traigo en el valor auxiliar traigo la plantila y en el estado el estado del documento 
	// para dibujar los colores y la plantilla
	public List<PedidoVentaCaracteristicaDTO> camposOcupadosCroquis(String sqlFuncionDecision, String campoId, String token , List<PedidoVentaCaracteristicaDTO> dependientes) throws ServerException {
		try {
			return  pedidoVentaCaracteristicaMapper.consultarCamposOcupados(SoftureUtil.formatFunction(sqlFuncionDecision), campoId, token, dependientes);
		} catch (Exception e) {
			throw new ServerException(e.getMessage(), "Funcion de campos ocupados : " + sqlFuncionDecision);
		}
	}
	
	public PedidoVentaCaracteristicaDTO getDependent(String field, List<PedidoVentaCaracteristicaDTO> dependents) throws ServerException{
		if(field==null) throw new ServerException("No se identifica el campo dependiente");
		if(dependents==null) return null;
		for (PedidoVentaCaracteristicaDTO iField : dependents) {
			if(iField.getCampo().compareTo(field)==0)
				return iField;
		}
		return null;
	}
	
	public void validarDependientes(DocumentoPlantillaCaracteristicaDTO campo, List<PedidoVentaCaracteristicaDTO> dependientes) throws ServerException{
		List<PropiedadDTO> codigoDepende = Propiedades.obtenerVariosParametro(campo, Propiedades.DEPENDENT_PROPS);
		if(codigoDepende==null || codigoDepende.isEmpty()) return;
		// En algunso casos de relacionar me apsaba que llaamba el mismo dependiente y no venia
		if(codigoDepende.size()==1 && codigoDepende.get(0).getCampo().compareTo(campo.getLlaveTabla())==0) return;
		//Valido que la cantidad de dependientes este correcta
		if(dependientes==null || dependientes.isEmpty())throw new ServerException("Revise los dependientes.\n " + campo.getNombre());
		//if(dependientes.size()!=codigoDepende.size()) throw new ServerException("El numero de dependientes no concuerda. Tipo Expediente" + codigoDepende.size());
		for (int i = 0; i <dependientes.size(); i++) {
			PedidoVentaCaracteristicaDTO iDepende = dependientes.get(i);
			if(iDepende.getCampoDTO()==null) 
				iDepende.setCampoDTO(campoDocumentoService.consultaXId(iDepende.getCampo()));
			
		}
	}
	
	public List<PedidoVentaCaracteristicaDTO> ordenarAlfabeticaDepende(List<PedidoVentaCaracteristicaDTO> dependientes) throws ServerException{
		List<PedidoVentaCaracteristicaDTO> dependentOrderList = new ArrayList<PedidoVentaCaracteristicaDTO>();
		if(dependientes==null) throw new ServerException("Por favor cierra y vuelve a cargar la pagina"); 
		for (int i = 0; i <dependientes.size(); i++) {
			PedidoVentaCaracteristicaDTO iDepende = dependientes.get(i);
			// Esto fue por un error en ccarga al consultar el producto pero no se si pasa lo mismo en otros sistemas
			if (iDepende.getCampoDTO() == null)
				iDepende.setCampoDTO(campoDocumentoService.consultaXId(iDepende.getCampo()));
			if(i == 0) {
				dependentOrderList.add(iDepende);
			}else {
				int j = 0;
				while(j <dependentOrderList.size() && dependentOrderList.get(j).getCampoDTO().getCodigo().compareTo(iDepende.getCampoDTO().getCodigo())<=0) {
					j++;
				}
				dependentOrderList.add(j, iDepende);
			}
		}
		return dependentOrderList;
	}
	
	public List<PedidoVentaCaracteristicaDTO> removeDuplicateDepends(List<PedidoVentaCaracteristicaDTO> dependents) throws ServerException{
		for (int i = dependents.size()-1; i >= 0; i--) {
			for (int j = i-1; j >= 0; j--) {
				if(dependents.get(i).getCampoDTO().getCodigo().compareTo(dependents.get(j).getCampoDTO().getCodigo())==0) {
					dependents.remove(i);
					break;
				}
			}
		}
		return dependents;
	}
	
	public PedidoVentaCaracteristicaDTO transformFilter2VO(PedidoVentaCaracteristicaFilterDTO filter) {
		PedidoVentaCaracteristicaDTO vo = new PedidoVentaCaracteristicaDTO();
		vo.setCampo(filter.getCampo());
		vo.setCampoDTO(filter.getCampoDTO());
		vo.setDependientes(filter.getDependientes());
		//vo.setDetalles(filter.getDependientes());
		vo.setDocumento(filter.getDocumento());
		vo.setEstado(filter.getEstado());
		vo.setExpedientes(filter.getExpedientes());
		vo.setLlaveTabla(filter.getLlaveTabla());
		//vo.setModificado(filter.getCampo());
		//vo.setPrincipal(filter.getp);
		//vo.setProductosExclusivos(filter.get);
		vo.setValorAuxiliar(filter.getValorAuxiliar());
		//vo.setValorFecha(filter.getval);
		vo.setValorNumero(filter.getValorNumeroMax());
		vo.setValorOpcion(filter.getValorOpcion());
		vo.setValorText(filter.getValorText());
		return vo;
	}
	
	//Consulto un campo que tenga ese mismo texto, 
	//La idea es que el resultado sea null
	//Si existe una conincidencia retorno el documento
	public String validarUnique(PedidoVentaCaracteristicaDTO value) throws ServerException {
		if(value ==null) throw new ServerException("Trae los datos del campo");
		return pedidoVentaCaracteristicaMapper.getUnique(value);
	}
	
	public PedidoVentaCaracteristicaDTO consultarSQLCampoGenerarDocumento(String sqlFuncionDecision, String documentoId, String modificadorId)
			throws ServerException {
		try {
			return pedidoVentaCaracteristicaMapper.consultarSQLCampoGenerarDocumento(SoftureUtil.formatFunction(sqlFuncionDecision), documentoId, modificadorId);
		} catch (Exception e) {
			throw new ServerException(e.getMessage());
		}
	}
	
	/*
	 * Creado porq un cliente desea ver en al lista unos campos que no hacen parte de la plantilla
	 * asi que con una funcion los traemos
	 */
	public List<PedidoVentaCaracteristicaDTO> camposEspecialesPlantilla(String sqlFuncion, String documentoId)
			throws ServerException {
		try {
			return pedidoVentaCaracteristicaMapper.camposEspecialesPlantilla(SoftureUtil.formatFunction(sqlFuncion), documentoId);
		} catch (Exception e) {
			throw new ServerException(e.getMessage(), " : " + sqlFuncion);
		}
	}
	
	/**
	 * PARA MOVER A OTRO MAPPER
	 */
	public List<PedidoVentaCaracteristicaDTO> listar2getApiCode(List<PedidoVentaCaracteristicaDTO> documentIds,
			List<RelacionInternaDTO> fieldId) throws ServerException {
		if (documentIds == null || documentIds.isEmpty() || fieldId == null || fieldId.isEmpty())
			return null;
		return pedidoVentaCaracteristicaMapper.listar2getApiCode(documentIds, fieldId);
	}
	public String getCodeKeyOfTemplate(String pTemplate, String pCode) throws ServerException {
		if (pTemplate == null || pTemplate.isEmpty() || pCode == null || pCode.isEmpty())
			return null;
		return pedidoVentaCaracteristicaMapper.getCodeKeyOfTemplate(pTemplate, pCode);
	}
	
	public String getKeyOfDocumentBase(String pCodeKey, String pValue) throws ServerException {
		if (pCodeKey == null || pCodeKey.isEmpty() || pValue == null || pValue.isEmpty())
			return null;
		return pedidoVentaCaracteristicaMapper.getKeyOfDocumentBase(pCodeKey, pValue);
	}
	
	public PedidoVentaCaracteristicaDTO getKeyToReplace(String pKeyBase, String pTemplate, String pCodeTemplate) throws ServerException {
		if (pKeyBase == null || pKeyBase.isEmpty() || pCodeTemplate == null || pCodeTemplate.isEmpty())
			return null;
		return pedidoVentaCaracteristicaMapper.getKeyToReplace(pKeyBase, pTemplate, pCodeTemplate);
	}
	

}