package d3.document.application.field;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import d3.document.application.PedidoVentaCaracteristicaSvc;
import d3.document.domain.PedidoVentaCaracteristicaDTO;
import d3.document.domain.PedidoVentaCaracteristicaFilterDTO;
import d3.document.domain.PedidoVentaDTO;
import d3.document.domain.PedidoVentaDineroDTO;
import d3.process.application.DocumentoPlantillaCaracteristicaSvc;
import d3.process.domain.DocumentoPlantillaCaracteristicaDTO;
import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.users.application.PuestoSvc;
import d3.users.domain.PuestoDTO;
import d3.users.domain.PuestoFilterDTO;

@Component
public class TipoCroquis {

	private final DocumentoPlantillaCaracteristicaSvc caracteristicaService;
	private final PedidoVentaCaracteristicaSvc campoService;
	private final PuestoSvc puestoService;

	public TipoCroquis(@Lazy DocumentoPlantillaCaracteristicaSvc caracteristicaService,
			@Lazy PedidoVentaCaracteristicaSvc campoService, @Lazy PuestoSvc puestoService) {
		this.caracteristicaService = caracteristicaService;
		this.campoService = campoService;
		this.puestoService = puestoService;
	}

	public void validarPrepararCampo(PedidoVentaCaracteristicaDTO pCampo, String token, boolean isUpdateAutomatic)
			throws ServerException {
		if (Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL) == null
				&& (pCampo.getValorText() == null || pCampo.getValorText().isEmpty()))
			throw new ServerException("En la plantilla " + pCampo.getCampoDTO().getPlantillaNombre()
					+ "Es obligatorio registrar el campo " + pCampo.getCampoDTO().getNombre() + "(codigo : "
					+ pCampo.getCampoDTO().getCodigo() + ")");
		if (pCampo.getExpedientes() != null && !pCampo.getExpedientes().isEmpty()) {
			List<PuestoDTO> componentesActuales = null;
			if (pCampo.getLlaveTabla() != null) {
				PuestoFilterDTO filtro = new PuestoFilterDTO();
				filtro.setCampo(pCampo.getLlaveTabla());
				filtro.setEstado(SharedConstants.STATE_ACTIVE);
				componentesActuales = puestoService.listarConsulta(filtro);
			}
			for (PedidoVentaDTO componente : pCampo.getExpedientes()) {
				if (componente.getDinero() == null)
					throw new ServerException("Revisa porque dinero viene vacio");
				if (componente.getDinero().getSaldo().compareTo(BigDecimal.ZERO) == 0)
					throw new ServerException("Los campos no pueden tener coordenada 0 en x");
				if (componente.getDinero().getValorTotal().compareTo(BigDecimal.ZERO) == 0)
					throw new ServerException("Los campos no pueden tener coordenada 0 en Y");
				if (componente.getNombre() == null)
					throw new ServerException("Los campos deben tener nombre");

				componente.setEstado(null);

				if (componente.getLlaveTabla() != null) {
					if (componentesActuales != null && !componentesActuales.isEmpty()) {
						for (PuestoDTO actual : componentesActuales) {
							if (actual.getLlaveTabla().compareTo(componente.getLlaveTabla()) == 0) {
								if (actual.getNombre().compareTo(componente.getNombre()) == 0
										&& actual.getColumna()
												.compareTo(componente.getDinero().getSaldo().intValue()) == 0
										&& actual.getFila()
												.compareTo(componente.getDinero().getValorTotal().intValue()) == 0)
									componente.setEstado(SharedConstants.STATE_ACTIVE);
								if (componente.getEstado() != null)
									componentesActuales.remove(actual);
								break;
							}
						}
					}
				}
			}
			if (componentesActuales != null && !componentesActuales.isEmpty()) {
				for (PuestoDTO actual : componentesActuales) {
					PedidoVentaDTO adicionarI = new PedidoVentaDTO();
					adicionarI.setLlaveTabla(actual.getLlaveTabla());
					adicionarI.setEstado(SharedConstants.STATE_INACTIVE);
					pCampo.getExpedientes().add(adicionarI);
				}
			}
		}
	}

	public PedidoVentaCaracteristicaDTO guardarCampo(PedidoVentaCaracteristicaDTO pCampo, String token)
			throws ServerException {
		PedidoVentaCaracteristicaDTO bd = campoService.buscarActivo(pCampo, pCampo.getPrincipal().getHistorico());
		if (bd != null) {
			if (pCampo.getValorText() == null) {
				bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
				bd.setPrincipal(pCampo.getPrincipal());
				campoService.inactivar(bd, token);
				return pCampo;
			} else {
				if (pCampo.getValorText().compareTo(bd.getValorText()) == 0) {
					actualizarExpedientes(pCampo, token);
					return pCampo;
				} else {
					bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
					bd.setPrincipal(pCampo.getPrincipal());
					campoService.inactivar(bd, token);
				}
			}
		}
		if (pCampo.getValorText() == null) {
			return pCampo;
		} else {
			PedidoVentaCaracteristicaDTO result = campoService.guardar(pCampo, token);
			pCampo.setLlaveTabla(result.getLlaveTabla());
			actualizarExpedientes(pCampo, token);
			return result;
		}
	}

	private void actualizarExpedientes(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException {
		if (pCampo.getExpedientes() != null) {
			if (pCampo.getLlaveTabla() == null)
				throw new ServerException("Revise porque la llave del campo es nula. Tipo Croquis");
			// PAra poder validar que no existan puestos con el mismo nombre se deben anular
			// primero
			for (PedidoVentaDTO componente : pCampo.getExpedientes()) {
				if (componente.getEstado() != null
						&& componente.getEstado().compareTo(SharedConstants.STATE_INACTIVE) == 0) {
					PuestoDTO inactivar = new PuestoDTO();
					inactivar.setLlaveTabla(componente.getLlaveTabla());
					puestoService.inactivar(inactivar, token);
				}
			}
			for (PedidoVentaDTO componente : pCampo.getExpedientes()) {
				if (componente.getEstado() == null) {
					PuestoDTO nuevo = new PuestoDTO();
					nuevo.setColumna(componente.getDinero().getSaldo().intValue());
					nuevo.setFila(componente.getDinero().getValorTotal().intValue());
					nuevo.setCampo(pCampo.getLlaveTabla());
					nuevo.setNombre(componente.getNombre());
					nuevo.setImagen(componente.getImagen());
					puestoService.guardar(nuevo, token);
				}
			}
		}
	}

	public void cargarConsultaCampo(PedidoVentaCaracteristicaDTO pCampo) throws ServerException {
		if (pCampo.getLlaveTabla() != null) {
			PuestoFilterDTO filtro = new PuestoFilterDTO();
			filtro.setCampo(pCampo.getLlaveTabla());
			filtro.setEstado(SharedConstants.STATE_ACTIVE);
			List<PuestoDTO> componentesActuales = puestoService.listarConsulta(filtro);
			if (componentesActuales != null && !componentesActuales.isEmpty()) {
				pCampo.setExpedientes(new ArrayList<PedidoVentaDTO>());
				for (PuestoDTO actual : componentesActuales) {
					pCampo.getExpedientes().add(convertirPuestoEnDocumento(actual));
				}
			}
		}
	}

	public PedidoVentaCaracteristicaFilterDTO consultarDatosBase(PedidoVentaCaracteristicaFilterDTO pCampo)
			throws ServerException {
		if (pCampo == null || pCampo.getCampo() == null)
			throw new ServerException("Revise la parametro del metodo");
		DocumentoPlantillaCaracteristicaDTO pBase = caracteristicaService.consultaXId(pCampo.getCampo());
		if (pBase == null)
			throw new ServerException("Error en el identificador de la caracteristica");

		if (pCampo.getLlaveTabla() != null) {
			PuestoFilterDTO filtro = new PuestoFilterDTO();
			filtro.setCampo(pCampo.getLlaveTabla());
			filtro.setEstado(SharedConstants.STATE_ACTIVE);
			List<PuestoDTO> componentesActuales = puestoService.listarConsulta(filtro);
			if (componentesActuales != null && !componentesActuales.isEmpty()) {
				pCampo.setExpedientes(new ArrayList<PedidoVentaDTO>());
				for (PuestoDTO actual : componentesActuales) {
					pCampo.getExpedientes().add(convertirPuestoEnDocumento(actual));
				}
			}
		}

		pCampo.setCampoDTO(pBase);

		return pCampo;
	}

	private PedidoVentaDTO convertirPuestoEnDocumento(PuestoDTO actual) {
		PedidoVentaDTO componente = new PedidoVentaDTO();
		componente.setLlaveTabla(actual.getLlaveTabla());
		componente.setNombre(actual.getNombre());
		componente.setDinero(new PedidoVentaDineroDTO());
		componente.getDinero().setValorTotal(new BigDecimal(actual.getFila()));
		componente.getDinero().setSaldo(new BigDecimal(actual.getColumna()));
		componente.setImagen(actual.getImagen());
		return componente;
	}

}
