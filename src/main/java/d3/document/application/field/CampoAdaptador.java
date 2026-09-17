package d3.document.application.field;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import d3.document.domain.PedidoVentaCaracteristicaDTO;
import d3.document.domain.PedidoVentaCaracteristicaFilterDTO;
import d3.document.domain.PedidoVentaDTO;
import d3.process.application.DocumentoPlantillaCaracteristicaSvc;
import d3.process.domain.DocumentoPlantillaCaracteristicaDTO;
import d3.shared.domain.ServerException;

@Component
public class CampoAdaptador {

	private final DocumentoPlantillaCaracteristicaSvc fieldService;
	private final TipoArchivo tipoArchivo;
	private final TipoBinario tipoBinario;
	private final TipoConfiguracion tipoConfiguracion;
	private final TipoCroquis tipoCroquis;
	private final TipoDetallePedido tipoDetallePedido;
	private final TipoDisponibilidad tipoDisponibilidad;
	private final TipoFecha tipoFecha;
	private final TipoInformativo tipoInformativo;
	private final TipoGPSMap tipoGPSMap;
	private final TipoNumero tipoNumero;
	private final TipoProceso tipoProceso;
	private final TipoProductoLista tipoProductoLista;
	private final TipoSeccion tipoSeccion;
	private final TipoTexto tipoTexto;
	private final TipoVinculo tipoVinculo;

	public CampoAdaptador(@Lazy DocumentoPlantillaCaracteristicaSvc fieldService, @Lazy TipoArchivo tipoArchivo,
			@Lazy TipoBinario tipoBinario, @Lazy TipoConfiguracion tipoConfiguracion, @Lazy TipoCroquis tipoCroquis,
			@Lazy TipoDetallePedido tipoDetallePedido, @Lazy TipoDisponibilidad tipoDisponibilidad,
			@Lazy TipoFecha tipoFecha, @Lazy TipoInformativo tipoInformativo, @Lazy TipoGPSMap tipoGPSMap,
			@Lazy TipoNumero tipoNumero, @Lazy TipoProceso tipoProceso, @Lazy TipoProductoLista tipoProductoLista,
			@Lazy TipoSeccion tipoSeccion, @Lazy TipoTexto tipoTexto, @Lazy TipoVinculo tipoVinculo) {
		this.fieldService = fieldService;
		this.tipoArchivo = tipoArchivo;
		this.tipoBinario = tipoBinario;
		this.tipoConfiguracion = tipoConfiguracion;
		this.tipoCroquis = tipoCroquis;
		this.tipoDetallePedido = tipoDetallePedido;
		this.tipoDisponibilidad = tipoDisponibilidad;
		this.tipoFecha = tipoFecha;
		this.tipoInformativo = tipoInformativo;
		this.tipoGPSMap = tipoGPSMap;
		this.tipoNumero = tipoNumero;
		this.tipoProceso = tipoProceso;
		this.tipoProductoLista = tipoProductoLista;
		this.tipoSeccion = tipoSeccion;
		this.tipoTexto = tipoTexto;
		this.tipoVinculo = tipoVinculo;
	}

	public void cargarConsultaCampo(PedidoVentaCaracteristicaDTO pCampo) throws ServerException {
		if (pCampo.getCampoDTO() == null)
			throw new ServerException("Valida la informacion no se encuentra la caracteristica base");
		switch (pCampo.getCampoDTO().getFormato()) {
		case DocumentoPlantillaCaracteristicaDTO.CONFIGURACION: {
			tipoConfiguracion.cargarConsultaCampo(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.CROQUIS: {
			tipoCroquis.cargarConsultaCampo(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.PRODUCTO: {
			tipoDetallePedido.cargarConsultaCampo(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.DISPONIBILIDAD: {
			tipoDisponibilidad.cargarConsultaCampo(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.PROCESO: {
			tipoProceso.cargarConsultaCampo(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.PRODUCTO_LISTA: {
			tipoProductoLista.cargarConsultaCampo(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.VINCULO: {
			tipoVinculo.cargarConsultaCampo(pCampo);
			break;
		}
		default: {
			break;
		}
		}
	}

	public void validarPrepararCampo(PedidoVentaCaracteristicaDTO pCampo, boolean isUpdateAutomatic)
			throws ServerException {
		if (pCampo.getCampoDTO() == null)
			throw new ServerException("Valida la informacion no se encuentra la caracteristica base");
		switch (pCampo.getCampoDTO().getFormato()) {
		case DocumentoPlantillaCaracteristicaDTO.ARCHIVO: {
			tipoArchivo.validarPrepararCampo(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.BINARIO: {
			tipoBinario.validarPrepararCampo(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.CONFIGURACION: {
			tipoConfiguracion.validarPrepararCampo(pCampo, isUpdateAutomatic);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.CROQUIS: {
			tipoCroquis.validarPrepararCampo(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.PRODUCTO: {
			tipoDetallePedido.validarPrepararCampo(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.DISPONIBILIDAD: {
			tipoDisponibilidad.validarPrepararCampo(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.FECHA: {
			tipoFecha.validarPrepararCampo(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.INFORMATIVO: {
			tipoInformativo.validarPrepararCampo(pCampo, isUpdateAutomatic);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.NUMERO: {
			tipoNumero.validarPrepararCampo(pCampo, isUpdateAutomatic);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.PROCESO: {
			tipoProceso.validarPrepararCampo(pCampo, isUpdateAutomatic);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.PRODUCTO_LISTA: {
			tipoProductoLista.validarPrepararCampo(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.TEXTO: {
			tipoTexto.validarPrepararCampo(pCampo, isUpdateAutomatic);
			break;
		}
		default: {
			break;
		}
		}
	}

	public PedidoVentaCaracteristicaDTO guardarCampo(PedidoVentaCaracteristicaDTO pCampo) throws ServerException {
		if (pCampo.getCampoDTO() == null)
			throw new ServerException("Valida la informacion no se encuentra la caracteristica base");
		PedidoVentaCaracteristicaDTO vResultado = null;
		switch (pCampo.getCampoDTO().getFormato()) {
		case DocumentoPlantillaCaracteristicaDTO.ARCHIVO: {
			vResultado = tipoArchivo.guardarCampo(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.BINARIO: {
			vResultado = tipoBinario.guardarCampo(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.CONFIGURACION: {
			vResultado = tipoConfiguracion.guardarCampo(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.CROQUIS: {
			vResultado = tipoCroquis.guardarCampo(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.PRODUCTO: {
			vResultado = tipoDetallePedido.guardarCampo(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.DISPONIBILIDAD: {
			vResultado = tipoDisponibilidad.guardarCampo(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.FECHA: {
			vResultado = tipoFecha.guardarCampo(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.INFORMATIVO: {
			vResultado = tipoInformativo.guardarCampo(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.NUMERO: {
			vResultado = tipoNumero.guardarCampo(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.PROCESO: {
			vResultado = tipoProceso.guardarCampo(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.PRODUCTO_LISTA: {
			vResultado = tipoProductoLista.guardarCampo(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.SECCION: {
			vResultado = pCampo;
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.TEXTO: {
			vResultado = tipoTexto.guardarCampo(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.VINCULO: {
			vResultado = pCampo;
			break;
		}
		default: {
			break;
		}
		}
		if (vResultado == null)
			throw new ServerException(
					"El campo no se encuentra configurado para guardar " + pCampo.getCampoDTO().getNombre());
		vResultado.setCampoDTO(pCampo.getCampoDTO());
		return vResultado;
	}

	public PedidoVentaCaracteristicaFilterDTO consultarDatosBase(PedidoVentaCaracteristicaFilterDTO pCampo)
			throws ServerException {
		if (pCampo.getCampo() == null)
			throw new ServerException("Valida la informacion no se encuentra la caracteristica base");
		pCampo.setCampoDTO(fieldService.consultaXId(pCampo.getCampo()));
		pCampo.setCampoDTO(fieldService.cargarComplementos(pCampo.getCampoDTO()));
		PedidoVentaCaracteristicaFilterDTO vResultado = null;
		switch (pCampo.getCampoDTO().getFormato()) {
		case DocumentoPlantillaCaracteristicaDTO.CONFIGURACION: {
			vResultado = tipoConfiguracion.consultarDatosBase(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.CROQUIS: {
			vResultado = tipoCroquis.consultarDatosBase(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.PRODUCTO: {
			vResultado = tipoDetallePedido.consultarDatosBase(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.DISPONIBILIDAD: {
			vResultado = tipoDisponibilidad.consultarDatosBase(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.INFORMATIVO: {
			vResultado = tipoInformativo.consultarDatosBase(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.NUMERO: {
			vResultado = tipoNumero.consultarDatosBase(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.FECHA: {
			vResultado = tipoFecha.consultarDatosBase(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.PROCESO: {
			vResultado = tipoProceso.consultarDatosBase(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.PRODUCTO_LISTA: {
			vResultado = tipoProductoLista.consultarDatosBase(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.GPS_MAP: {
			vResultado = tipoGPSMap.consultarDatosBase(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.SECCION: {
			vResultado = tipoSeccion.consultarDatosBase(pCampo);
			break;
		}
		default: {
			break;
		}
		}
		return vResultado;
	}

	public PedidoVentaCaracteristicaDTO inactivar(PedidoVentaCaracteristicaDTO pCampo,
			PedidoVentaDTO documentoModificadorDTO) throws ServerException {
		if (pCampo.getCampoDTO() == null)
			throw new ServerException("Valida la informacion no se encuentra la caracteristica base");
		PedidoVentaCaracteristicaDTO vResultado = null;
		switch (pCampo.getCampoDTO().getFormato()) {
		case DocumentoPlantillaCaracteristicaDTO.PRODUCTO: {
			vResultado = tipoDetallePedido.inactivar(pCampo);
			break;
		}
		case DocumentoPlantillaCaracteristicaDTO.PROCESO: {
			vResultado = tipoProceso.inactivar(pCampo, documentoModificadorDTO);
			break;
		}
		default: {
			break;
		}
		}
		return vResultado;
	}

}
