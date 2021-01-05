package com.softure.logisticpymes.services;

import java.util.List;

// BEGIN region interImport
import java.util.ArrayList;
import java.math.BigDecimal;

import com.softure.java.cons.ConstantesGenerales;
import com.softure.logisticpymes.services.adapter.CampoAdaptador;
import com.softure.logisticpymes.services.adapter.Propiedades;
import com.softure.logisticpymes.dto.DetallePedidoVentaDTO;
import com.softure.logisticpymes.dto.DocumentoPlantillaCaracteristicaDTO;
import com.softure.logisticpymes.dto.PedidoVentaDTO;
import com.softure.logisticpymes.dto.PropiedadDTO;
import com.softure.logisticpymes.dto.RelacionInternaDTO;
import com.softure.java.services.SoftureUtil;
// END region interImport

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.dto.filter.PedidoVentaCaracteristicaFilterDTO;
import com.softure.logisticpymes.persistence.PedidoVentaCaracteristicaMapper;

@Service("pedidoVentaCaracteristicaService")
public class PedidoVentaCaracteristicaSvc extends BasicSvc<PedidoVentaCaracteristicaDTO, PedidoVentaCaracteristicaFilterDTO> {
	
	@Autowired
	private PedidoVentaCaracteristicaMapper pedidoVentaCaracteristicaMapper;
	
	// BEGIN region servicesPedidoVentaCaracteristica
	@Autowired private CampoAdaptador adaptador;
	@Autowired private DetallePedidoVentaSvc detallePedidoVentaService;
	// END region servicesPedidoVentaCaracteristica

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
	public PedidoVentaCaracteristicaDTO activar(PedidoVentaCaracteristicaDTO dto, String token) throws ServerException {
		// BEGIN PedidoVentaCaracteristica_activar
		return activate(dto);
		// END PedidoVentaCaracteristica_activar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PedidoVentaCaracteristicaDTO actualizar( PedidoVentaCaracteristicaDTO dto, String token) throws ServerException {
		// BEGIN PedidoVentaCaracteristica_actualizar
		if(dto.getValorNumero()!=null && dto.getValorNumero().compareTo(BigDecimal.ZERO)==0) dto.setValorNumero(null);
		return update(dto);
		//pues si lo uso, pero lo debo qutar
		//throw new ServerException("En teoria no se usa este metodo");
		// END PedidoVentaCaracteristica_actualizar
	}
	
	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PedidoVentaCaracteristicaDTO inactivar(PedidoVentaCaracteristicaDTO dto, String token) throws ServerException {
		// BEGIN PedidoVentaCaracteristica_inactivar
		if(dto.getTransaccionInactivo()== null) throw new ServerException("Se encesita la transaccion de inactivar");
		PedidoVentaCaracteristicaDTO bd = consultaXId(dto.getLlaveTabla());
		bd.setEstado(ConstantesGenerales.ESTADO_INACTIVO);
		bd.setTransaccionInactivo(dto.getTransaccionInactivo());
		return update(bd);
		// END PedidoVentaCaracteristica_inactivar
	}
	
	@Override
	public PedidoVentaCaracteristicaDTO consultaUnica(PedidoVentaCaracteristicaFilterDTO dto) throws ServerException {
		return super.consultaUnica(dto);
	}
	
	@Override
	public int contarResultados(PedidoVentaCaracteristicaFilterDTO dto) throws ServerException {
		return super.contarResultados(dto);
	}
	
	@Override
	public List<PedidoVentaCaracteristicaDTO> listarConsulta(PedidoVentaCaracteristicaFilterDTO dto)
			throws ServerException {
		return super.listarConsulta(dto);
	}
	
	public PedidoVentaCaracteristicaDTO completarDatosBase(PedidoVentaCaracteristicaFilterDTO dto)throws ServerException{
		// BEGIN region completarDatosBase
		return transformFilter2VO( adaptador.consultarDatosBase(dto)) ;
		// END region completarDatosBase
	}

	@Override
	@Transactional(rollbackFor=Exception.class, propagation=Propagation.REQUIRED)
	public PedidoVentaCaracteristicaDTO guardar(PedidoVentaCaracteristicaDTO dto, String token) throws ServerException {
		// BEGIN PedidoVentaCaracteristica_guardar
		DocumentoPlantillaCaracteristicaDTO base = dto.getCampoDTO();
		if(dto.getValorText()!=null && dto.getValorText().length()>4000) throw new ServerException("Se excedio la cantidad de caracteres del texto. (4000)");
		if(dto.getValorNumero()!=null && dto.getValorNumero().compareTo(BigDecimal.ZERO)==0) dto.setValorNumero(null);
		if(dto.getValorNumero()!=null && dto.getValorNumero().compareTo(new BigDecimal(9999999999999999.0))>0) throw new ServerException("Se excedio del numero maximo");
		dto = save(dto);
		dto.setCampoDTO(base);
		return dto;
		// END PedidoVentaCaracteristica_guardar
	}

// BEGIN region aditionalMethods
	public List<PedidoVentaCaracteristicaDTO> listar2Documento(String documento)
			throws ServerException {//La plantilla es para optimizar la consultas de la particion
		return pedidoVentaCaracteristicaMapper.listar2Documento(documento);
	}
	
	public List<PedidoVentaCaracteristicaDTO> readCompleteFields(String documentId, List<DocumentoPlantillaCaracteristicaDTO> templateFields)
			throws ServerException {
		List<PedidoVentaCaracteristicaDTO> result = pedidoVentaCaracteristicaMapper.listar2Documento(documentId);
		if(result==null || result.isEmpty()) return result;
		for (DocumentoPlantillaCaracteristicaDTO iFieldTemplateDTO : templateFields) {
			for (PedidoVentaCaracteristicaDTO iCurrentField : result) {
				if(iFieldTemplateDTO.getLlaveTabla().compareTo(iCurrentField.getCampo())==0) {
					iCurrentField.setCampoDTO(iFieldTemplateDTO);
					if(iFieldTemplateDTO.getFormato().compareTo(DocumentoPlantillaCaracteristicaDTO.PRODUCTO)==0) {
						iCurrentField.setDetalles(detallePedidoVentaService.listar2Documento(documentId));
						for (DetallePedidoVentaDTO detalleDocumento : iCurrentField.getDetalles()) {
							detallePedidoVentaService.createFieldsProduct(detalleDocumento);	
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
	
	public List<PedidoVentaCaracteristicaDTO> listar2DocumentoVisible(List<PedidoVentaDTO> documentos)
			throws ServerException {//La plantilla es para optimizar la consultas de la particion
		if(documentos==null || documentos.isEmpty()) return null;
		return pedidoVentaCaracteristicaMapper.listar2DocumentoVisible(documentos);
	}
	
	public List<PedidoVentaCaracteristicaDTO> listar2getMessageMailDestiny(List<PedidoVentaCaracteristicaDTO> documentIds, List<RelacionInternaDTO> fieldId)
			throws ServerException {
		if(documentIds==null || documentIds.isEmpty() || fieldId==null || fieldId.isEmpty()) return null;
		return pedidoVentaCaracteristicaMapper.listar2getMessageMailDestiny(documentIds, fieldId);
	}
	
	public PedidoVentaCaracteristicaDTO buscarActivo(PedidoVentaCaracteristicaDTO dto) throws ServerException {
		if(dto==null || dto.getDocumento()==null) throw new ServerException("Error al consultar el campo previo por falta de datos");
		PedidoVentaCaracteristicaFilterDTO bd = new PedidoVentaCaracteristicaFilterDTO();
		bd.setDocumento(dto.getDocumento());
		bd.setCampo(dto.getCampo());
		bd.setEstado(ConstantesGenerales.ESTADO_ACTIVO);
		return consultaUnica(bd);
	}
	
	public BigDecimal calcularNumeroFuncion(String sqlFuncionDecision, String documento, List<PedidoVentaCaracteristicaDTO> dependientes) throws ServerException {
		try {
			return  pedidoVentaCaracteristicaMapper.calcularNumeroFuncion(SoftureUtil.formatFunction(sqlFuncionDecision), documento, dependientes);
		} catch (Exception e) {
			throw new ServerException(e.getMessage(), "Funcion de Calculo : " + sqlFuncionDecision);
		}
	}
	
	public PedidoVentaCaracteristicaDTO consultarCampoCroquis(String nombreDocumento)
			throws ServerException {
		return pedidoVentaCaracteristicaMapper.consultarCampoCroquis(nombreDocumento);
	}
	
	public void validarDependientes(DocumentoPlantillaCaracteristicaDTO campo, List<PedidoVentaCaracteristicaDTO> dependientes) throws ServerException{
		List<PropiedadDTO> codigoDepende = Propiedades.obtenerVariosParametro(campo, Propiedades.DEPENDE);
		if(codigoDepende==null || codigoDepende.isEmpty()) return;
		//Valido que la cantidad de dependientes este correcta
		if(dependientes==null || dependientes.isEmpty())throw new ServerException("Revise los dependientes.\n " + campo.getNombre());
		if(dependientes.size()!=codigoDepende.size()) throw new ServerException("El numero de dependientes no concuerda. Tipo Expediente" + codigoDepende.size());
	}
	
	public List<PedidoVentaCaracteristicaDTO> ordenarAlfabeticaDepende(List<PedidoVentaCaracteristicaDTO> dependientes) {
		List<PedidoVentaCaracteristicaDTO> parametrosFuncionTarifario = new ArrayList<PedidoVentaCaracteristicaDTO>();
		for (int i = 0; i <dependientes.size(); i++) {
			PedidoVentaCaracteristicaDTO iDepende = dependientes.get(i);
			if(i == 0) {
				parametrosFuncionTarifario.add(iDepende);
			}else {
				int j = 0;
				while(j <parametrosFuncionTarifario.size() && parametrosFuncionTarifario.get(j).getCampoDTO().getCodigo().compareTo(iDepende.getCampoDTO().getCodigo())<0) {
					j++;
				}
				parametrosFuncionTarifario.add(j, iDepende);
			}
		}
		return parametrosFuncionTarifario;
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
			throw new ServerException(e.getMessage(), " : " + sqlFuncionDecision);
		}
	}
// END region aditionalMethods

}