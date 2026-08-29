package d3.document.application.field;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import d3.shared.domain.ServerException;
import d3.shared.domain.SharedConstants;
import d3.document.application.CallDocumentCommons;
import d3.document.application.PedidoVentaCaracteristicaSvc;
import d3.document.domain.PedidoVentaCaracteristicaDTO;
import d3.shared.application.ProcessTemplate;
import d3.shared.application.D3Utils;
import org.springframework.context.annotation.Lazy;

@Component
public class TipoTexto {

	private final PedidoVentaCaracteristicaSvc campoService;
	private final ProcessTemplate processTemplate;

	public TipoTexto(@Lazy PedidoVentaCaracteristicaSvc campoService, @Lazy ProcessTemplate processTemplate) {
		this.campoService = campoService;
		this.processTemplate = processTemplate;
	}

	public void validarPrepararCampo(PedidoVentaCaracteristicaDTO pCampo, String token, boolean isUpdateAutomatic)
			throws ServerException {
		// System.out.format("\n[%s - %s] Validando.....",
		// pCampo.getCampoDTO().getPlantillaNombre(), pCampo.getCampoDTO().getNombre());
		if (pCampo.getValorText() != null) {
			pCampo.setValorText(D3Utils.cleanStartEndSpaces(pCampo.getValorText()));
			if (pCampo.getValorText().isEmpty())
				pCampo.setValorText(null);
		}

		if (// pCampo.getLlaveTabla() == null &&
		pCampo.getModificado()
				&& Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_BLOQUEAR) != null
				&& Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.TEXTO_FORMULA) != null) {
			// En consecutvos me fallo porque no calcula
			calcularValorFormula(pCampo);
			// Esto lo coloque hace proco y estoy revisando
			if (pCampo.getValorText() == null)
				return;
		}
		if (Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_OPCIONAL) == null
				&& pCampo.getValorText() == null) {
			if (isUpdateAutomatic) {
				CallDocumentCommons.addMessageError(pCampo.getPrincipal(),
						"En la plantilla " + pCampo.getCampoDTO().getPlantillaNombre()
								+ " Es obligatorio registrar el campo " + pCampo.getCampoDTO().getNombre()
								+ "(codigo : " + pCampo.getCampoDTO().getCodigo() + ")");
			} else {
				throw new ServerException("En la plantilla " + pCampo.getCampoDTO().getPlantillaNombre()
						+ " Es obligatorio registrar el campo " + pCampo.getCampoDTO().getNombre() + "(codigo : "
						+ pCampo.getCampoDTO().getCodigo() + ")");
			}
		}
		if (pCampo.getValorText() == null)
			return;
		if (Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.TEXTO_LONGITUD) != null) {
			try {
				int maxSize = Integer
						.valueOf(Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.TEXTO_LONGITUD));
				if (pCampo.getValorText().length() > maxSize)
					throw new ServerException("En la plantilla " + pCampo.getCampoDTO().getPlantillaNombre()
							+ " el campo " + pCampo.getCampoDTO().getNombre()
							+ " supera la cantidad limite de caracteres que son (" + maxSize
							+ ") el texto que sobra es el siguiente: " + pCampo.getValorText().substring(maxSize));
			} catch (NumberFormatException e) {
				throw new ServerException("En la plantilla " + pCampo.getCampoDTO().getPlantillaNombre() + " el campo "
						+ pCampo.getCampoDTO().getNombre()
						+ " tiene mal configurado el valor de la propiedad TEXTO LONGITUD, debe ser un numero");
			}
		}
		if (Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.TEXTO_LONGITUD_MINIMA) != null) {
			try {
				int minSize = Integer
						.valueOf(Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.TEXTO_LONGITUD_MINIMA));
				if (pCampo.getValorText().length() < minSize)
					throw new ServerException("En la plantilla " + pCampo.getCampoDTO().getPlantillaNombre()
							+ " el campo " + pCampo.getCampoDTO().getNombre() + " debe contener mas de " + minSize
							+ " caracteres");
			} catch (NumberFormatException e) {
				throw new ServerException("En la plantilla " + pCampo.getCampoDTO().getPlantillaNombre() + " el campo "
						+ pCampo.getCampoDTO().getNombre()
						+ " tiene mal configurado el valor de la propiedad TEXTO LONGITUD, debe ser un numero");
			}
		}
		String formato = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.FORMATO);
		if (!formato.isEmpty()) {
			switch (formato) {
			case "E": {
				pCampo.setValorText(pCampo.getValorText().replaceAll("\\s", ""));
				validateFormatProperty(pCampo.getValorText().split(";"),
						"^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", pCampo);
				pCampo.setValorText(pCampo.getValorText().toLowerCase());
				break;
			}
			case "T": {
				pCampo.setValorText(pCampo.getValorText().replaceAll("[^\\d+]", ""));
				validateFormatProperty(pCampo.getValorText().split(";"), "^(\\+?57)?(3\\d{9}|60[1-9]\\d{7})$", pCampo);
				pCampo.setValorText(pCampo.getValorText());
				break;
			}
			case "S": {
				break;
			}
			case "D": {
				break;
			}
			case "N": {
				validateFormatProperty(pCampo.getValorText().split(";"), "^[0-9]*$", pCampo);
				pCampo.setValorText(pCampo.getValorText().toLowerCase());
				break;
			}
			default:
				throw new ServerException("En la plantilla " + pCampo.getCampoDTO().getPlantillaNombre() + " el campo "
						+ pCampo.getCampoDTO().getNombre() + " tiene FORMATO no valido :" + formato);
			}

		} else {
			String parametro = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.TEXTO_LARGO);
			if (parametro.isEmpty())
				pCampo.setValorText(pCampo.getValorText().toUpperCase());
		}
	}

	private void validateFormatProperty(String[] registros, String emailRegex, PedidoVentaCaracteristicaDTO pCampo)
			throws ServerException {
		Pattern pat = Pattern.compile(emailRegex);
		for (String iRegistro : registros) {
			if (iRegistro != null && !iRegistro.isEmpty()) {
				if (!pat.matcher(iRegistro).matches())
					throw new ServerException("En la plantilla " + pCampo.getCampoDTO().getPlantillaNombre()
							+ " Revisa el campo " + pCampo.getCampoDTO().getNombre()
							+ "  ya que no tiene un formato valido, " + iRegistro);
			}
		}
	}

	public PedidoVentaCaracteristicaDTO guardarCampo(PedidoVentaCaracteristicaDTO pCampo, String token)
			throws ServerException {
		// Esto lo pase de validar aqui porque necesito a veces el ID y asumo que si es
		// bloqueado se va a calcular bien
		if (pCampo.getLlaveTabla() == null
				&& Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.PERMISO_CAMPO_BLOQUEAR) != null
				&& Propiedades.obtenerParametro(pCampo.getCampoDTO(), Propiedades.TEXTO_FORMULA) != null) {
			calcularValorFormula(pCampo);
		}
		PedidoVentaCaracteristicaDTO bd = campoService.buscarActivo(pCampo, pCampo.getPrincipal().getHistorico());
		if (bd != null) {
			if (pCampo.getValorText() == null) {
				bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
				bd.setPrincipal(pCampo.getPrincipal());
				campoService.inactivar(bd, token);
				pCampo.setDifference(new PedidoVentaCaracteristicaDTO());
				pCampo.getDifference().setValorText(bd.getValorText());
				return pCampo;
			} else {
				if (pCampo.getValorText().compareTo(bd.getValorText()) == 0) {
					return pCampo;
				} else {
					bd.setTransaccionInactivo(pCampo.getTransaccionRegistro());
					bd.setPrincipal(pCampo.getPrincipal());
					campoService.inactivar(bd, token);
					pCampo.setDifference(new PedidoVentaCaracteristicaDTO());
					pCampo.getDifference().setValorText(bd.getValorText());
				}
			}
		}
		if (pCampo.getValorText() == null) {
			return pCampo;
		} else {
			return campoService.guardar(pCampo, token);
		}
	}

	private void calcularValorFormula(PedidoVentaCaracteristicaDTO pCampo) {
		String textoCalculado = Propiedades.obtenerValor(pCampo.getCampoDTO(), Propiedades.TEXTO_FORMULA);
		if (!(textoCalculado.contains("$") || textoCalculado.contains("<#"))) {
			if (pCampo.getDependientes() != null && !pCampo.getDependientes().isEmpty()) {
				for (PedidoVentaCaracteristicaDTO iDep : pCampo.getDependientes()) {
					textoCalculado = StringUtils.replace(textoCalculado, iDep.getCampoDTO().getCodigo(),
							(iDep.getValorText() == null) ? "" : iDep.getValorText());
				}
			}
		} else {
			String params = processTemplate.transformDependsToParams(pCampo.getDependientes());
			// En trustmetrans se necesita armar un texto con el id del documento
			if (pCampo.getPrincipal() != null) {
				if (pCampo.getPrincipal().getNombre() != null)
					params = params + SharedConstants.PUNTO_COMA_DOBLE + "E_CODE" + SharedConstants.IGUAL
							+ pCampo.getPrincipal().getNombre();
				// if(pCampo.getPrincipal().getDescripcion()!=null) textoCalculado =
				// StringUtils.replace(textoCalculado,
				// "E_CODE",pCampo.getPrincipal().getNombre());
			}
			textoCalculado = processTemplate.generateOutputFile(textoCalculado, params);
		}
		pCampo.setValorText(textoCalculado);
	}

}
