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
 * $Id: BotBuilder.java 12 2020-01-21 11:40:54Z  $
 *
 */
package de.citunius.businesslogic.calculator.botbuilder;

import java.lang.invoke.MethodHandles;
import java.util.HashMap;

import org.apache.log4j.Logger;

import de.citunius.businesslogic.calculator.services.CalculatorService;
import de.citunius.businesslogicapi.common.ConstantsPlugin;
import de.citunius.businesslogicapi.common.botbuilder.BotBuilderService;
import de.citunius.businesslogicapi.common.services.LocalisationService;

/**
 * BotBuilderService is the class represents the services for the Bot Builder Model.
 * This service calls the use case specific functions of this business bot plugin.
 * This class is called using Java reflections.
 *
 * @author me
 * @version %I%, %G%
 * @since   1.0
*/
public class BotBuilder implements BotBuilderService {
	static Logger logger = Logger.getLogger(MethodHandles.lookup().lookupClass());
	
	// Defines the syntax for function parameter field
	public static final String REQUIRED_PLUGINMAP_FUNCTION_PARAMS_FIELD = "REQUIRED_PLUGINMAP_FUNCTION_PARAMS_";
	
	/**
	 * Class constructor
	 * 
	 * @param pluginMap
	 * @param ls
	 * @param defaultLanguage
	 */
	public BotBuilder() {
		
	}
	
	/**
	 * Bot builder function for the dialogue designer
	 * 
	 * <p>This function calls the calculator service in order to 
	 * calculate the math task. The result is formatted as returned 
	 * to the business bot platform for further processing</p>
	 * 
	 * @param pluginMap  the plugin hashmap
	 * @param ls  the language service instance
	 * @param defaultLanguage  the default user language
	 * @return  the result
	 */
	public String getMathResult(HashMap<String, String> pluginMap, LocalisationService ls, String defaultLanguage) {
		String result = null;
		
		if (pluginMap.containsKey(ConstantsPlugin.BOTBUILDER_FUNCTION_PARAM_1.toString())) {
			String mathTask = pluginMap.get(ConstantsPlugin.BOTBUILDER_FUNCTION_PARAM_1.toString());
			logger.info("mathTask: ["+mathTask+"]");
			
	    	CalculatorService cs = new CalculatorService(pluginMap);
    		if (cs.isMathTask(mathTask)) {
    			logger.debug("User message contains math"); 
    			double mathResult;
				try {
					mathResult = cs.calculate(mathTask);
					result = ls.getString("Calculator.Result", defaultLanguage)+": "+mathResult;
				} catch (Exception e) {
					logger.error(e);
				}    			
    		} else {
    			logger.info("User message does not contains math.");
    			result = ls.getString("Calculator.NoMath", defaultLanguage);
    		}
		} else {
			logger.warn("Math task not found in pluginMap -> expected as key ["+ConstantsPlugin.BOTBUILDER_FUNCTION_PARAM_1+"]");
		}
		
    	String replyMessage = "No reply content";
    	if (result != null) {
    		replyMessage = result;
    	} else {
    		replyMessage = ls.getString("Common.NoResultsFound", defaultLanguage);
    	}
    	return replyMessage;
	}
	
}
