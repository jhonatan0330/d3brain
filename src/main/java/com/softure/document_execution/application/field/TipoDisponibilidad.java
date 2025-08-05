package com.softure.document_execution.application.field;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired; import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.shared.domain.SharedConstants;
import com.shared.domain.ServerException;
import com.softure.document_execution.application.DetallePedidoVentaSvc;
import com.softure.document_execution.application.PedidoVentaCaracteristicaSvc;
import com.softure.document_execution.domain.DetallePedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaDTO;
import com.softure.document_execution.domain.PedidoVentaCaracteristicaFilterDTO;
import com.softure.document_execution.domain.PedidoVentaDTO;
import com.softure.document_execution.domain.PedidoVentaDineroDTO;
import com.softure.logisticpymes.application.PuestoSvc;
import com.softure.logisticpymes.domain.PuestoDTO;
import com.softure.logisticpymes.domain.PuestoFilterDTO;
import com.softure.process_form.application.DocumentoPlantillaCaracteristicaSvc;
import com.softure.process_form.domain.DocumentoPlantillaCaracteristicaDTO;
import com.softure.property.application.PropertyNavigateIntoRelationsToFindFieldsService;
import com.softure.property.domain.PropiedadDTO;

@Component
public class TipoDisponibilidad {

	@Autowired @Lazy 
	private DocumentoPlantillaCaracteristicaSvc baseService;
	@Autowired @Lazy 
	private PedidoVentaCaracteristicaSvc campoService;
	@Autowired @Lazy 
	private DetallePedidoVentaSvc inventoryService;
	@Autowired @Lazy 
	private PuestoSvc puestoService;
	@Autowired @Lazy 
	private CallProductValidateAndSave validateAndSave;
	@Autowired @Lazy 
	private DetallePedidoVentaSvc detallePedidoVentaService;
	@Autowired @Lazy 
	private PropertyNavigateIntoRelationsToFindFieldsService findFieldService;

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
			pCampo = campoService.guardar(pCampo, token);
			if (pCampo.getDetalles() != null && !pCampo.getDetalles().isEmpty()) {
				pCampo.setDetalles(validateAndSave.save(pCampo.getDetalles(), token, pCampo.getDocumento(),
						pCampo.getCampoDTO().getPlantilla(), pCampo.getTransaccionRegistro(), pCampo.getLlaveTabla()));
			}
			return pCampo;
		}
	}

	public void cargarConsultaCampo(PedidoVentaCaracteristicaDTO pCampo, String token) throws ServerException {
		// Retire algo que tenia que ver con el valor opcion ver hisotiral
		String producto = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.PRODUCTO_PUESTO);
		if (!producto.isEmpty()) {
			if (!pCampo.getModificado())
				pCampo.setDetalles(
						detallePedidoVentaService.listarCompleto(pCampo.getDocumento(), null, null, null, token, null));
		}
	}

	public PedidoVentaCaracteristicaFilterDTO consultarDatosBase(PedidoVentaCaracteristicaFilterDTO pCampo)
			throws ServerException {
		DocumentoPlantillaCaracteristicaDTO pBase = baseService.consultaUnicaConComplementos(pCampo.getCampo(),
				pCampo.getSecurityToken());
		List<PuestoDTO> componentesActuales = getOptionsToSelect(pCampo.getDependientes(), pBase);
		if (componentesActuales != null && !componentesActuales.isEmpty()) {
			pBase.setDocumentos(new ArrayList<PedidoVentaDTO>());
			for (PuestoDTO actual : componentesActuales) {
				pBase.getDocumentos().add(convertirPuestoEnDocumento(actual));
			}
			PropiedadDTO funcion = Propiedades.obtenerParametro(pBase, Propiedades.DISPONIBILIDAD_FUNCION_SQL);
			if (funcion != null) {
				campoService.validarDependientes(pBase, pCampo.getDependientes());
				List<PedidoVentaCaracteristicaDTO> ocupados = campoService.camposOcupadosCroquis(
						funcion.getLlaveTabla(), pCampo.getDocumento(), pCampo.getSecurityToken(),
						campoService.ordenarAlfabeticaDepende(pCampo.getDependientes()));
				if (ocupados != null && !ocupados.isEmpty()) {
					for (PedidoVentaCaracteristicaDTO iOcupado : ocupados) {
						String[] pOcupados = iOcupado.getValorText().split("-");
						for (String iPuesto : pOcupados) {
							if (!iPuesto.isEmpty()) {
								for (PedidoVentaDTO iEspacio : pBase.getDocumentos()) {
									if (iEspacio.getNombre().compareTo(iPuesto) == 0) {
										iEspacio.setLlaveTabla(iOcupado.getDocumento());
										iEspacio.setPlantilla(iOcupado.getValorAuxiliar());
										iEspacio.setEstadoExpediente(iOcupado.getEstado());
										iEspacio.setEstadoNombre(iOcupado.getTransaccionRegistro());
										break;
									}
								}
							}
						}
					}
				}
			}
		}
		String producto = Propiedades.obtenerValor(pBase, Propiedades.PRODUCTO_PUESTO);
		if (!producto.isEmpty()) {
			pBase.setProductos(inventoryService.getCompleteDetailFromProductId(producto, pCampo.getSecurityToken()));
		}
		pCampo.setCampoDTO(pBase);
		return pCampo;
	}

	private List<PuestoDTO> getOptionsToSelect(List<PedidoVentaCaracteristicaDTO> dependents,
			DocumentoPlantillaCaracteristicaDTO pBase) throws ServerException {
		PropiedadDTO estructura = Propiedades.obtenerParametro(pBase, Propiedades.DISPONIBILIDAD_CROQUIS);
		if (estructura == null)
			throw new ServerException(
					"Es necesario colocar la caracteristica del Documento base que tiene el croquis. Tipo Disponibilidad");
		 if (dependents == null || dependents.isEmpty())
					 throw new ServerException("Revise los dependientes. Tipo Disponibilidad");
		PedidoVentaCaracteristicaDTO vCroquis = null;
		
		List<PedidoVentaCaracteristicaDTO> fieldsInRelations = findFieldService.call(estructura.getLlaveTabla(), dependents);
		if(fieldsInRelations!=null && !fieldsInRelations.isEmpty()) {
			vCroquis = fieldsInRelations.get(0);
		} else {
			// Proximamente vamos a retirar esta funcionalidad se maneja solopor el camino
			PedidoVentaCaracteristicaDTO dependienteCroquis = null;
			for (PedidoVentaCaracteristicaDTO iDependiente : dependents) {
				if (iDependiente.getCampo().compareTo(estructura.getValor()) == 0) {
					dependienteCroquis = iDependiente;
					break;
				}
			}

			if (dependienteCroquis == null)
				throw new ServerException("No se encontro en los dependientes la estructura del croquis");
			vCroquis = campoService.consultarCampoCroquis(dependienteCroquis.getValorOpcion());
		}
		
		if (vCroquis == null)
			throw new ServerException("La estructura no tiene un campo croquis que se encuentre activo");

		pBase.setImagen(vCroquis.getValorText());
		PuestoFilterDTO filtro = new PuestoFilterDTO();
		filtro.setCampo(vCroquis.getLlaveTabla());
		filtro.setEstado(SharedConstants.STATE_ACTIVE);
		return puestoService.listarConsulta(filtro);
	}

	private PedidoVentaDTO convertirPuestoEnDocumento(PuestoDTO actual) {
		PedidoVentaDTO componente = new PedidoVentaDTO();
		// componente.setLlaveTabla(actual.getLlaveTabla());
		componente.setNombre(actual.getNombre());
		componente.setDinero(new PedidoVentaDineroDTO());
		componente.getDinero().setValorTotal(new BigDecimal(actual.getFila()));
		componente.getDinero().setSaldo(new BigDecimal(actual.getColumna()));
		componente.setImagen(actual.getImagen());
		return componente;
	}

	public void validarPrepararCampo(PedidoVentaCaracteristicaDTO pCampo, String token, boolean isUpdateAutomatic) throws ServerException {
		String[] locations = null;
		if (pCampo.getValorText() != null && pCampo.getValorText().isEmpty())
			pCampo.setValorText(null);

		// Valido obligatoriedad
		if (Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL) == null
				&& pCampo.getValorText() == null)
			throw new ServerException("Es necesario registrar el campo " + pCampo.getCampoDTO().getNombre());

		if (pCampo.getValorText() != null)
			locations = pCampo.getValorText().split("-");
		if (Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL) == null
				&& (locations == null || locations.length == 0))
			throw new ServerException("Es necesario registrar el campo " + pCampo.getCampoDTO().getNombre());
		if (locations != null) {

			List<PuestoDTO> currentItems = getOptionsToSelect(pCampo.getDependientes(), pCampo.getCampoDTO());
			if (currentItems == null || currentItems.isEmpty())
				throw new ServerException("No hay opciones para seleccionar una posicion del croquis");
			int positionCount = 0;
			for (String actual : locations) {
				if (actual != null && !actual.isEmpty()) {
					positionCount++;
					PuestoDTO findItem = null;
					for (PuestoDTO iPuesto : currentItems) {
						if (iPuesto.getNombre().compareTo(actual) == 0) {
							findItem = iPuesto;
							break;
						}

					}
					if (findItem == null)
						throw new ServerException("En el campo " + pCampo.getCampoDTO().getNombre() + " la posicion "
								+ actual + " no pertence al croquis");
				}
			}
			pCampo.setValorNumero(new BigDecimal(positionCount));
			PropiedadDTO funcion = Propiedades.obtenerParametro(pCampo.getCampoDTO(),
					Propiedades.DISPONIBILIDAD_FUNCION_SQL);
			if (funcion != null) {
				campoService.validarDependientes(pCampo.getCampoDTO(), pCampo.getDependientes());
				List<PedidoVentaCaracteristicaDTO> ocupados = campoService.camposOcupadosCroquis(
						funcion.getLlaveTabla(), pCampo.getLlaveTabla(), token,
						campoService.ordenarAlfabeticaDepende(pCampo.getDependientes()));
				if (ocupados != null && !ocupados.isEmpty()) {
					for (PedidoVentaCaracteristicaDTO iOcupado : ocupados) {
						String[] pOcupados = iOcupado.getValorText().split("-");
						for (String iPuesto : pOcupados) {
							if (!iPuesto.isEmpty()) {
								for (String actual : locations) {
									if (actual.compareTo(iPuesto) == 0) {
										throw new ServerException("El " + pCampo.getCampoDTO().getNombre() + " "
												+ iPuesto + " ya se encuentra ocupado");
									}
								}
							}
						}
					}
				}
			}
			pCampo.setValorText("");
			for (String componente : locations) {
				if (!componente.isEmpty())
					pCampo.setValorText(pCampo.getValorText() + "-" + componente);
			}

			List<DetallePedidoVentaDTO> agrupados = new ArrayList<DetallePedidoVentaDTO>();
			if (pCampo.getDetalles() != null && !pCampo.getDetalles().isEmpty()) {
				agrupados = validateAndSave.orderToValidate(pCampo.getDetalles());
				pCampo.setDetalles(agrupados);
			}
			if (pCampo.getDocumento() != null) {
				pCampo.setDetalles(
						validateAndSave.validateWithExistProducts(agrupados, pCampo.getDocumento(), null, token, null));
			} else {
				pCampo.setDetalles(agrupados);
			}
		} else {
			pCampo.setValorText(null);
		}
	}

}
