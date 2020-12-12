package com.softure.java.services;

import java.math.BigDecimal;
import java.math.RoundingMode;

import com.softure.java.dto.exception.ServerException;

public class CalculatorUtil {

    public static BigDecimal calcular(String formula) throws ServerException{
        BigDecimal result;
        int signo = formula.lastIndexOf("?");
        if(signo!=-1){
            int dospunto = formula.lastIndexOf(":");
            result  = calcular(formula.substring(0,signo));
            if(formula.contains(">")) result= result.negate();
            if(BigDecimal.ZERO.compareTo(result)>0){
                result = calcular(formula.substring(dospunto+1, formula.length()));
            }else{
                result = calcular(formula.substring(signo+1,dospunto));
            }
        }else{
            int parentesisCierra =  formula.indexOf(")");
            if(parentesisCierra!=-1){
                int parentesisAbre = formula.substring(0,parentesisCierra).lastIndexOf("(");
                String formulaInterna = formula.substring(parentesisAbre +1, parentesisCierra);
                formula = formula.replace("(" + formulaInterna + ")", calcular(formulaInterna).toPlainString());
                result = calcular(formula);
            }else{
                result = calculateText(formula);
            }
        }
        return result;
    }

    private static BigDecimal calculateText(String text) throws ServerException{

        int posOperator;
        BigDecimal leftOperator;
        BigDecimal righOperator;
        BigDecimal result = null;


        posOperator = text.indexOf("-");
        if(posOperator!=-1){
        	leftOperator = crearBigDecimalMensaje(text.substring(0,posOperator));
        	righOperator = crearBigDecimalMensaje(text.substring(posOperator+1, text.length()));
        	result = leftOperator.add(righOperator.negate());
        }else{
        	posOperator = text.indexOf("+");
        	if(posOperator!=-1){
        		leftOperator = crearBigDecimalMensaje(text.substring(0,posOperator));
        		righOperator = crearBigDecimalMensaje(text.substring(posOperator+1, text.length()));
        		result = leftOperator.add(righOperator);
        	}else{
        		posOperator = text.indexOf("*");
        		if(posOperator!=-1){
        			leftOperator = crearBigDecimalMensaje(text.substring(0,posOperator));
        			righOperator = crearBigDecimalMensaje(text.substring(posOperator+1, text.length()));
        			result = leftOperator.multiply(righOperator);
        		}else{
        			posOperator = text.indexOf("/");
        			if(posOperator!=-1){
        				leftOperator = crearBigDecimalMensaje(text.substring(0,posOperator));
        				righOperator = crearBigDecimalMensaje(text.substring(posOperator+1, text.length()));
        				result = leftOperator.divide(righOperator,8, RoundingMode.CEILING);
        			}else{
        				posOperator = text.indexOf("%");
        				if(posOperator!=-1){
        					leftOperator = crearBigDecimalMensaje(text.substring(0,posOperator));
        					righOperator = crearBigDecimalMensaje(text.substring(posOperator+1, text.length()));
        					result = leftOperator.remainder(righOperator);
        				}else{
        					posOperator = text.indexOf("<");
        					if(posOperator!=-1){
        						leftOperator = crearBigDecimalMensaje(text.substring(0,posOperator));
        						righOperator = crearBigDecimalMensaje(text.substring(posOperator+1, text.length()));
        						result = new BigDecimal(righOperator.compareTo(leftOperator));
        					}else{
        						posOperator = text.indexOf(">");
        						if(posOperator!=-1){
        							leftOperator = crearBigDecimalMensaje(text.substring(0,posOperator));
        							righOperator = crearBigDecimalMensaje(text.substring(posOperator+1, text.length()));
        							result = new BigDecimal(righOperator.compareTo(leftOperator));
        						}else{
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
    
    private static BigDecimal crearBigDecimalMensaje(String text) throws ServerException{
    	try {
    		text = text.replaceAll("\n", "");
    		return new BigDecimal(text);
	    }catch (NumberFormatException e) {
			throw new ServerException("Error al convertir a numero el texto " + text);
		}
    }
}
