/*
 * Copyright: (c) 2015-2019, Citunius GmbH. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * Licence: This program contains proprietary and trade secret information of Citunius GmbH.
 *          Copyright notice is precautionary only and does not evidence any actual or intended 
 *          publication of such program
 *          See: https://www.citunius.de/en/legal
 *
 * Requires: JDK 1.8+
 * $Id: CalculatorService.java 12 2020-01-21 11:40:54Z  $
 *
 */
package de.citunius.businesslogic.calculator.services;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * This class holds the services
 *
 * @author me
 * @version %I%, %G%
 * @since   1.0
 */
public class CalculatorService {
	static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
	public HashMap<String, String> pluginMap = null;
	
	/**
	 * Constructor
	 * 
	 * @param pluginMap  the plugin HashMap
	 */
	public CalculatorService(HashMap<String, String> pluginMap) {
		this.pluginMap = pluginMap;
	}
	
	/**
	 * Check if given message is a math task
	 * 
	 * @param message  the math task
	 * @return true is message is a math task; otherwise false
	 */
	public boolean isMathTask(String message) {
		boolean atleastOneAlpha = message.matches(".*[a-zA-Z]+.*");
		if (!(atleastOneAlpha)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Calculate the math task
	 * 
	 * @param message  the math task
	 * @param defaultLanguage  the user language
	 * @param ls  the location service object
	 * @return the math result
	 * @throws Exception is thrown when message is not a math task
	 */
	public double calculate(String message) throws Exception {
		// Check if string is math
		if (isMathTask(message)) {
			logger.info("Calculate: ["+message+"]");
	    	double result = eval(message);
	    	logger.info("Result: ["+result+"]");
	    	return result;
	    	
		} else {
			throw new Exception("Invalid math task");
		}
	}
	
	/**
	 * Calculation process
	 * 
	 * @param str
	 * @return
	 */
	public static double eval(final String str) {
	    return new Object() {
	        int pos = -1, ch;

	        void nextChar() {
	            ch = (++pos < str.length()) ? str.charAt(pos) : -1;
	        }

	        boolean eat(int charToEat) {
	            while (ch == ' ') nextChar();
	            if (ch == charToEat) {
	                nextChar();
	                return true;
	            }
	            return false;
	        }

	        double parse() {
	            nextChar();
	            double x = parseExpression();
	            if (pos < str.length()) throw new RuntimeException("Unexpected: " + (char)ch);
	            return x;
	        }

	        // Grammar:
	        // expression = term | expression `+` term | expression `-` term
	        // term = factor | term `*` factor | term `/` factor
	        // factor = `+` factor | `-` factor | `(` expression `)`
	        //        | number | functionName factor | factor `^` factor

	        double parseExpression() {
	            double x = parseTerm();
	            for (;;) {
	                if      (eat('+')) x += parseTerm(); // addition
	                else if (eat('-')) x -= parseTerm(); // subtraction
	                else return x;
	            }
	        }

	        double parseTerm() {
	            double x = parseFactor();
	            for (;;) {
	                if      (eat('*')) x *= parseFactor(); // multiplication
	                else if (eat('/')) x /= parseFactor(); // division
	                else return x;
	            }
	        }

	        double parseFactor() {
	            if (eat('+')) return parseFactor(); // unary plus
	            if (eat('-')) return -parseFactor(); // unary minus

	            double x;
	            int startPos = this.pos;
	            if (eat('(')) { // parentheses
	                x = parseExpression();
	                eat(')');
	            } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
	                while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
	                x = Double.parseDouble(str.substring(startPos, this.pos));
	            } else if (ch >= 'a' && ch <= 'z') { // functions
	                while (ch >= 'a' && ch <= 'z') nextChar();
	                String func = str.substring(startPos, this.pos);
	                x = parseFactor();
	                if (func.equals("sqrt")) x = Math.sqrt(x);
	                else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
	                else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
	                else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
	                else throw new RuntimeException("Unknown function: " + func);
	            } else {
	                throw new RuntimeException("Unexpected: " + (char)ch);
	            }

	            if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

	            return x;
	        }
	    }.parse();
	}
}
