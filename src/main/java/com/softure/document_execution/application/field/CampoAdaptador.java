package com.softure.document_execution.application.field;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.shared.domain.ServerException;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaFilterDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;

@Component
public class CampoAdaptador {

	@Autowired @Lazy  private DocumentoPlantillaCaracteristicaSvc fieldService;
	@Autowired @Lazy  private TipoArchivo tipoArchivo;
	@Autowired @Lazy  private TipoBinario tipoBinario;
	@Autowired @Lazy  private TipoConfiguracion tipoConfiguracion;
	@Autowired @Lazy  private TipoCroquis tipoCroquis;
	@Autowired @Lazy  private TipoDetallePedido tipoDetallePedido;
	@Autowired @Lazy  private TipoDisponibilidad tipoDisponibilidad;
	@Autowired @Lazy  private TipoFecha tipoFecha;
	@Autowired @Lazy  private TipoInformativo tipoInformativo;
	@Autowired @Lazy  private TipoGPS tipoGPS;
	@Autowired @Lazy  private TipoGPSMap tipoGPSMap;
	@Autowired @Lazy  private TipoNumero tipoNumero;
	@Autowired @Lazy  private TipoProceso tipoProceso;
	@Autowired @Lazy  private TipoProductoLista tipoProductoLista;
	@Autowired @Lazy  private TipoSeccion tipoSeccion;
	@Autowired @Lazy  private TipoTexto tipoTexto;
	
	/**
	 * Este metodo consulta una caracteristica de un documento, segun las condiciones del campo
	 * @param pCampo
	 * @throws ServerException
	 */
	public void cargarConsultaCampo(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException{
		if(pCampo.getCampoDTO()==null) throw new ServerException("Valida la informacion no se encuentra la caracteristica base");
		switch(pCampo.getCampoDTO().getFormato()){
			case DocumentoPlantillaCaracteristicaDTO.CONFIGURACION:{ tipoConfiguracion.cargarConsultaCampo(pCampo);break;}
			case DocumentoPlantillaCaracteristicaDTO.CROQUIS:{ tipoCroquis.cargarConsultaCampo(pCampo);break;}
			case DocumentoPlantillaCaracteristicaDTO.PRODUCTO:{ tipoDetallePedido.cargarConsultaCampo(pCampo, token);break;}
			case DocumentoPlantillaCaracteristicaDTO.DISPONIBILIDAD:{ tipoDisponibilidad.cargarConsultaCampo(pCampo, token);break;}
			case DocumentoPlantillaCaracteristicaDTO.PROCESO:{ tipoProceso.cargarConsultaCampo(pCampo);break;}
			case DocumentoPlantillaCaracteristicaDTO.PRODUCTO_LISTA:{ tipoProductoLista.cargarConsultaCampo(pCampo);break;}
			default:{break;}
		}
	}
	/**
	 * Este metodo valida la caracteristica antes de guardar o modificar
	 * @param pCampo
	 * @throws ServerException
	 */
	public void validarPrepararCampo(PedidoVentaCaracteristicaDTO pCampo, String token, boolean isUpdateAutomatic) throws ServerException{
		if(pCampo.getCampoDTO()==null) throw new ServerException("Valida la informacion no se encuentra la caracteristica base");
		switch(pCampo.getCampoDTO().getFormato()){
			case DocumentoPlantillaCaracteristicaDTO.ARCHIVO:{ tipoArchivo.validarPrepararCampo(pCampo, token, isUpdateAutomatic);break;}
			case DocumentoPlantillaCaracteristicaDTO.BINARIO:{tipoBinario.validarPrepararCampo(pCampo, token, isUpdateAutomatic);break;}
			case DocumentoPlantillaCaracteristicaDTO.CONFIGURACION:{tipoConfiguracion.validarPrepararCampo(pCampo, token, isUpdateAutomatic);break;}
			case DocumentoPlantillaCaracteristicaDTO.CROQUIS:{tipoCroquis.validarPrepararCampo(pCampo, token, isUpdateAutomatic);break;}
			case DocumentoPlantillaCaracteristicaDTO.PRODUCTO:{ tipoDetallePedido.validarPrepararCampo(pCampo, token, isUpdateAutomatic);break;}
			case DocumentoPlantillaCaracteristicaDTO.DISPONIBILIDAD:{ tipoDisponibilidad.validarPrepararCampo(pCampo, token, isUpdateAutomatic);break;}
			case DocumentoPlantillaCaracteristicaDTO.FECHA:{ tipoFecha.validarPrepararCampo(pCampo, token, isUpdateAutomatic);break;}
			case DocumentoPlantillaCaracteristicaDTO.INFORMATIVO:{ tipoInformativo.validarPrepararCampo(pCampo, token, isUpdateAutomatic);break;}
			case DocumentoPlantillaCaracteristicaDTO.NUMERO:{ tipoNumero.validarPrepararCampo(pCampo, token, isUpdateAutomatic);break;}
			case DocumentoPlantillaCaracteristicaDTO.PROCESO:{ tipoProceso.validarPrepararCampo(pCampo, token, isUpdateAutomatic);break;}
			case DocumentoPlantillaCaracteristicaDTO.PRODUCTO_LISTA:{ tipoProductoLista.validarPrepararCampo(pCampo, token, isUpdateAutomatic);break;}
			case DocumentoPlantillaCaracteristicaDTO.TEXTO:{ tipoTexto.validarPrepararCampo(pCampo, token, isUpdateAutomatic);break;}
			default:{break;}
		}
	}
	
	/**
	 * Este metodo contiene la logica de guardar la caracteristica
	 * @param pCampo
	 * @return
	 * @throws ServerException
	 */
	public PedidoVentaCaracteristicaDTO guardarCampo(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException{
		if(pCampo.getCampoDTO()==null) throw new ServerException("Valida la informacion no se encuentra la caracteristica base");
		PedidoVentaCaracteristicaDTO vResultado =null;
		switch(pCampo.getCampoDTO().getFormato()){
			case DocumentoPlantillaCaracteristicaDTO.ARCHIVO:{vResultado = tipoArchivo.guardarCampo(pCampo, token);break;}
			case DocumentoPlantillaCaracteristicaDTO.BINARIO:{vResultado = tipoBinario.guardarCampo(pCampo, token);break;}
			case DocumentoPlantillaCaracteristicaDTO.CONFIGURACION:{vResultado = tipoConfiguracion.guardarCampo(pCampo, token);break;}
			case DocumentoPlantillaCaracteristicaDTO.CROQUIS:{vResultado = tipoCroquis.guardarCampo(pCampo, token);break;}
			case DocumentoPlantillaCaracteristicaDTO.PRODUCTO:{vResultado = tipoDetallePedido.guardarCampo(pCampo, token);break;}
			case DocumentoPlantillaCaracteristicaDTO.DISPONIBILIDAD:{vResultado = tipoDisponibilidad.guardarCampo(pCampo, token);break;}
			case DocumentoPlantillaCaracteristicaDTO.FECHA:{vResultado = tipoFecha.guardarCampo(pCampo, token);break;}
			case DocumentoPlantillaCaracteristicaDTO.GPS:{vResultado = tipoGPS.guardarCampo(pCampo, token);break;}
			//case DocumentoPlantillaCaracteristicaDTO.GPS_MAP:{vResultado = tipoGPS.guardarCampo(pCampo, token);break;}
			case DocumentoPlantillaCaracteristicaDTO.INFORMATIVO:{vResultado = tipoInformativo.guardarCampo(pCampo, token);break;}
			case DocumentoPlantillaCaracteristicaDTO.NUMERO:{vResultado = tipoNumero.guardarCampo(pCampo, token);break;}
			case DocumentoPlantillaCaracteristicaDTO.PROCESO:{vResultado = tipoProceso.guardarCampo(pCampo, token);break;}
			case DocumentoPlantillaCaracteristicaDTO.PRODUCTO_LISTA:{vResultado = tipoProductoLista.guardarCampo(pCampo, token);break;}
			case DocumentoPlantillaCaracteristicaDTO.SECCION:{vResultado = pCampo; break;}
			case DocumentoPlantillaCaracteristicaDTO.TEXTO:{vResultado = tipoTexto.guardarCampo(pCampo, token);break;}
			default:{break;}
		}
		if(vResultado==null) throw new ServerException("El campo no se encuentra configurado para guardar " + pCampo.getCampoDTO().getNombre());
		vResultado.setCampoDTO(pCampo.getCampoDTO());
		return vResultado;
	}
	
	/**
	 * Este metodo se usa para las consultas asincronas del formulario desde el cliente
	 * @param pCampo
	 * @return
	 * @throws ServerException
	 */
	public PedidoVentaCaracteristicaFilterDTO consultarDatosBase(PedidoVentaCaracteristicaFilterDTO pCampo) throws ServerException{
		if(pCampo.getCampo()==null) throw new ServerException("Valida la informacion no se encuentra la caracteristica base");
		pCampo.setCampoDTO(fieldService.consultaXId(pCampo.getCampo()));
		pCampo.setCampoDTO(fieldService.cargarComplementos(pCampo.getCampoDTO(), pCampo.getSecurityToken()));
		PedidoVentaCaracteristicaFilterDTO vResultado =null;
		switch(pCampo.getCampoDTO().getFormato()){
			case DocumentoPlantillaCaracteristicaDTO.CONFIGURACION:{vResultado = tipoConfiguracion.consultarDatosBase(pCampo);break;}
			case DocumentoPlantillaCaracteristicaDTO.CROQUIS:{vResultado = tipoCroquis.consultarDatosBase(pCampo);break;}
			case DocumentoPlantillaCaracteristicaDTO.PRODUCTO:{vResultado = tipoDetallePedido.consultarDatosBase(pCampo);break;}	
			case DocumentoPlantillaCaracteristicaDTO.DISPONIBILIDAD:{vResultado = tipoDisponibilidad.consultarDatosBase(pCampo);break;}
			case DocumentoPlantillaCaracteristicaDTO.INFORMATIVO:{vResultado = tipoInformativo.consultarDatosBase(pCampo);break;}
			case DocumentoPlantillaCaracteristicaDTO.NUMERO:{vResultado = tipoNumero.consultarDatosBase(pCampo);break;}
			case DocumentoPlantillaCaracteristicaDTO.FECHA:{vResultado = tipoFecha.consultarDatosBase(pCampo);break;}
			case DocumentoPlantillaCaracteristicaDTO.PROCESO:{vResultado = tipoProceso.consultarDatosBase(pCampo);break;}
			case DocumentoPlantillaCaracteristicaDTO.PRODUCTO_LISTA:{vResultado = tipoProductoLista.consultarDatosBase(pCampo);break;}
			case DocumentoPlantillaCaracteristicaDTO.GPS_MAP:{vResultado = tipoGPSMap.consultarDatosBase(pCampo);break;}
			case DocumentoPlantillaCaracteristicaDTO.SECCION:{vResultado = tipoSeccion.consultarDatosBase(pCampo);break;}
			default:{break;}
		}
		//if(vResultado!=null) vResultado.setCampoDTO(pCampo.getCampoDTO());
		return vResultado;
	}
	
	/**
	 * Se encargade realizar las operaciones correspondientes a la inactivacion del documento
	 * @param pCampo
	 * @throws ServerException
	 */
	public PedidoVentaCaracteristicaDTO inactivar(PedidoVentaCaracteristicaDTO pCampo, PedidoVentaDTO documentoModificadorDTO, String token) throws ServerException{
		if(pCampo.getCampoDTO()==null) throw new ServerException("Valida la informacion no se encuentra la caracteristica base");
		PedidoVentaCaracteristicaDTO vResultado =null;
		switch(pCampo.getCampoDTO().getFormato()){
			case DocumentoPlantillaCaracteristicaDTO.PRODUCTO:{vResultado = tipoDetallePedido.inactivar(pCampo, token);break;}
			case DocumentoPlantillaCaracteristicaDTO.PROCESO:{vResultado = tipoProceso.inactivar(pCampo, documentoModificadorDTO, token);break;}
			default:{break;}
		}
		return vResultado;
	}
	
}
