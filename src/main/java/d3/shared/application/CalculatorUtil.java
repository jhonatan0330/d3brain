package d3.shared.application;

import java.math.BigDecimal;
import java.math.RoundingMode;

import d3.shared.domain.ServerException;

public class CalculatorUtil {

	public static BigDecimal calcular(String formula) throws ServerException {
		BigDecimal result;
		int parentesisCierra = formula.indexOf(")");
		if (parentesisCierra != -1) {
			int parentesisAbre = formula.substring(0, parentesisCierra).lastIndexOf("(");
			String formulaInterna = formula.substring(parentesisAbre + 1, parentesisCierra);
			formula = formula.replace("(" + formulaInterna + ")", calcular(formulaInterna).toPlainString());
			// System.out.println("Parentesis Formula = " + formula );
			result = calcular(formula);
			// System.out.println("Parentesis = " + formula + " = " + result);
		} else {
			int signo = formula.indexOf("?");
			if (signo != -1) {
				int dospunto = formula.indexOf(":");
				String carac = formula.substring(signo + 1, dospunto);
				int newPregunta = carac.indexOf("?");
				while (dospunto > 0 && newPregunta != -1) {
					dospunto = formula.indexOf(":", dospunto + 1);
					carac = formula.substring(signo + newPregunta + 2, dospunto);
					newPregunta = carac.indexOf("?");
				}
				result = calcular(formula.substring(0, signo));
				// System.out.println("Pregunta Formula = " + formula + " = " + result);
				if (result.compareTo(BigDecimal.ZERO) > 0) {
					result = calcular(formula.substring(signo + 1, dospunto));
					// System.out.println("Mayor = " + formula.substring(signo+1,dospunto) + " = " +
					// result);
				} else {
					result = calcular(formula.substring(dospunto + 1, formula.length()));
					// System.out.println("Menor = " + formula.substring(dospunto+1,
					// formula.length()) + " = " + result);
				}
			} else {
				result = calculateText(formula);
				// System.out.println("Simple = " + formula + " = " + result);
			}
		}
		return result;
	}

	private static BigDecimal calculateText(String text) throws ServerException {

		int posOperator;
		BigDecimal leftOperator;
		BigDecimal righOperator;
		BigDecimal result = null;

		text = text.replace("++", "+");
		text = text.replace("+-", "-");
		text = text.replace("--", "+");
		text = text.replace("-+", "-");
		posOperator = text.indexOf("-", 1);
		if (posOperator != -1) {
			leftOperator = crearBigDecimalMensaje(text.substring(0, posOperator));
			righOperator = crearBigDecimalMensaje(text.substring(posOperator + 1, text.length()));
			result = leftOperator.add(righOperator.negate());
		} else {
			posOperator = text.indexOf("+");
			if (posOperator != -1) {
				leftOperator = crearBigDecimalMensaje(text.substring(0, posOperator));
				righOperator = crearBigDecimalMensaje(text.substring(posOperator + 1, text.length()));
				result = leftOperator.add(righOperator);
			} else {
				posOperator = text.indexOf("*");
				if (posOperator != -1) {
					leftOperator = crearBigDecimalMensaje(text.substring(0, posOperator));
					righOperator = crearBigDecimalMensaje(text.substring(posOperator + 1, text.length()));
					result = leftOperator.multiply(righOperator);
				} else {
					posOperator = text.indexOf("/");
					if (posOperator != -1) {
						leftOperator = crearBigDecimalMensaje(text.substring(0, posOperator));
						righOperator = crearBigDecimalMensaje(text.substring(posOperator + 1, text.length()));
						if (righOperator.compareTo(BigDecimal.ZERO) == 0) {
							result = BigDecimal.ZERO;
						} else {
							result = leftOperator.divide(righOperator, 8, RoundingMode.CEILING);
						}
					} else {
						posOperator = text.indexOf("%");
						if (posOperator != -1) {
							leftOperator = crearBigDecimalMensaje(text.substring(0, posOperator));
							righOperator = crearBigDecimalMensaje(text.substring(posOperator + 1, text.length()));
							result = leftOperator.remainder(righOperator);
						} else {
							posOperator = text.indexOf("<");
							if (posOperator != -1) {
								leftOperator = crearBigDecimalMensaje(text.substring(0, posOperator));
								righOperator = crearBigDecimalMensaje(text.substring(posOperator + 1, text.length()));
								result = new BigDecimal(righOperator.compareTo(leftOperator));
							} else {
								posOperator = text.indexOf(">");
								if (posOperator != -1) {
									leftOperator = crearBigDecimalMensaje(text.substring(0, posOperator));
									righOperator = crearBigDecimalMensaje(
											text.substring(posOperator + 1, text.length()));
									result = new BigDecimal(leftOperator.compareTo(righOperator));
								} else {
									result = crearBigDecimalMensaje(text);
								}
							}
						}
					}
				}
			}
		}
		return result;
	}

	private static BigDecimal crearBigDecimalMensaje(String text) throws ServerException {
		try {
			text = text.replaceAll("\n", "0");
			if (text.isEmpty())
				text = "0";
			return new BigDecimal(text);
		} catch (NumberFormatException e) {
			throw new ServerException("Error al convertir a numero el texto " + text + ". Revisa los dependientes");
		}
	}
}
