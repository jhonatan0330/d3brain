package com.softure.logisticpymes.services.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.softure.java.dto.exception.ServerException;
import com.softure.logisticpymes.dto.DocumentoPlantillaCaracteristicaDTO;
import com.softure.logisticpymes.dto.PedidoVentaCaracteristicaDTO;
import com.softure.logisticpymes.dto.PedidoVentaDTO;
import com.softure.logisticpymes.dto.filter.PedidoVentaCaracteristicaFilterDTO;

@Component
public class CampoAdaptador {

	@Autowired private TipoArchivo tipoArchivo;
	@Autowired private TipoBinario tipoBinario;
	@Autowired private TipoConfiguracion tipoConfiguracion;
	@Autowired private TipoCroquis tipoCroquis;
	@Autowired private TipoDetallePedido tipoDetallePedido;
	@Autowired private TipoDisponibilidad tipoDisponibilidad;
	@Autowired private TipoFecha tipoFecha;
	@Autowired private TipoNumero tipoNumero;
	@Autowired private TipoProceso tipoProceso;
	@Autowired private TipoProductoLista tipoProductoLista;
	@Autowired private TipoTexto tipoTexto;
	
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
			case DocumentoPlantillaCaracteristicaDTO.DISPONIBILIDAD:{ tipoDisponibilidad.cargarConsultaCampo(pCampo);break;}
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
	public void validarPrepararCampo(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException{
		if(pCampo.getCampoDTO()==null) throw new ServerException("Valida la informacion no se encuentra la caracteristica base");
		switch(pCampo.getCampoDTO().getFormato()){
			case DocumentoPlantillaCaracteristicaDTO.ARCHIVO:{ tipoArchivo.validarPrepararCampo(pCampo, token);break;}
			case DocumentoPlantillaCaracteristicaDTO.BINARIO:{tipoBinario.validarPrepararCampo(pCampo, token);break;}
			case DocumentoPlantillaCaracteristicaDTO.CONFIGURACION:{tipoConfiguracion.validarPrepararCampo(pCampo, token);break;}
			case DocumentoPlantillaCaracteristicaDTO.CROQUIS:{tipoCroquis.validarPrepararCampo(pCampo, token);break;}
			case DocumentoPlantillaCaracteristicaDTO.PRODUCTO:{ tipoDetallePedido.validarPrepararCampo(pCampo, token);break;}
			case DocumentoPlantillaCaracteristicaDTO.DISPONIBILIDAD:{ tipoDisponibilidad.validarPrepararCampo(pCampo, token);break;}
			case DocumentoPlantillaCaracteristicaDTO.FECHA:{ tipoFecha.validarPrepararCampo(pCampo, token);break;}
			case DocumentoPlantillaCaracteristicaDTO.NUMERO:{ tipoNumero.validarPrepararCampo(pCampo, token);break;}
			case DocumentoPlantillaCaracteristicaDTO.PROCESO:{ tipoProceso.validarPrepararCampo(pCampo, token);break;}
			case DocumentoPlantillaCaracteristicaDTO.PRODUCTO_LISTA:{ tipoProductoLista.validarPrepararCampo(pCampo, token);break;}
			case DocumentoPlantillaCaracteristicaDTO.TEXTO:{ tipoTexto.validarPrepararCampo(pCampo, token);break;}
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
		if(pCampo.getCampoDTO()==null) throw new ServerException("Valida la informacion no se encuentra la caracteristica base");
		PedidoVentaCaracteristicaFilterDTO vResultado =null;
		switch(pCampo.getCampoDTO().getFormato()){
			case DocumentoPlantillaCaracteristicaDTO.CONFIGURACION:{vResultado = tipoConfiguracion.consultarDatosBase(pCampo);break;}
			case DocumentoPlantillaCaracteristicaDTO.CROQUIS:{vResultado = tipoCroquis.consultarDatosBase(pCampo);break;}
			case DocumentoPlantillaCaracteristicaDTO.PRODUCTO:{vResultado = tipoDetallePedido.consultarDatosBase(pCampo);break;}	
			case DocumentoPlantillaCaracteristicaDTO.DISPONIBILIDAD:{vResultado = tipoDisponibilidad.consultarDatosBase(pCampo);break;}
			case DocumentoPlantillaCaracteristicaDTO.NUMERO:{vResultado = tipoNumero.consultarDatosBase(pCampo);break;}
			case DocumentoPlantillaCaracteristicaDTO.PROCESO:{vResultado = tipoProceso.consultarDatosBase(pCampo);break;}
			case DocumentoPlantillaCaracteristicaDTO.PRODUCTO_LISTA:{vResultado = tipoProductoLista.consultarDatosBase(pCampo);break;}
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
