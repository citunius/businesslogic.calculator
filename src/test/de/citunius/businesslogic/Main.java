package test.de.citunius.businesslogic;

import java.util.Date;
import java.util.HashMap;

import de.citunius.bbp.im.gm.objects.Data;
import de.citunius.bbp.im.gm.objects.Message;
import de.citunius.bbp.objects.AnonymousUserAccount;
import de.citunius.bbp.objects.MobileAppProvider;
import de.citunius.businesslogic.calculator.CalculatorBot;
import de.citunius.businesslogic.calculator.plugin.PluginClient.BusinessLogic;
import de.citunius.businesslogicapi.common.ConstantsPlugin;

public class Main {

	// API account credentials (if business logic want to call the BBP webservices)
	public static String BBP_APIACCOUNT_APIID_PARAM = "BusinessBotPlatform.Account.ApiId";
	public static String BBP_APIACCOUNT_APIKEY_PARAM = "BusinessBotPlatform.Account.ApiKey";
	
	// Test Values
	public static String TENANTID = "demo";
	public static String ACCOUNTID = "ALL_ACCOUNTS";
	
	public static String USERNAME = "john";
	public static String FIRSTNAME = "John";
	public static String LASTNAME = "Doe";
	
	public static String MESSENGER_NAME = "Telegram";
	public static String PLUGIN_ID = "1";
	public static String PLUGIN_NAME = "demo.ALL_ACCOUNTS.de.citunius.generic.calculator";
	public static String PLUGIN_WEBUI_LANGUAGE = "en";
	public static String PLUGIN_RESOURCE_PATH = "BusinessLogic.Component.ConversationalEditor\\resources";
	public static String BBP_WEBSERVICES_URL = "https://demo.bbp.local:450/bbp/rest/api/1.0/";
	public static String PLUGIN_WORKDIR = "C:\\temp";
	public static String PLUGIN_SYSTEMDIR = "C:\\Citunius.DevEnv\\eclipse-workspace-java";
	public static String INSTANTMESSENGER_FILESTORE_PATH = "c:\\temp\\bbp\\filestore";
	public static String BBP_APIACCOUNT_APIID = "u-98edf8d";
	public static String BBP_APIACCOUNT_APIKEY = "5407807";
			
	public static void main(String[] args) {
		String userMessage = "4 + 2";
		
		BusinessLogic bl = new BusinessLogic();
		HashMap<String, String> pluginMap = getPluginMap();
		
		de.citunius.bbp.im.gm.objects.User fromUser = new de.citunius.bbp.im.gm.objects.User();
		fromUser.setUserName("john");
		fromUser.setFirstName("John");
		fromUser.setLastName("Doe");
		
		de.citunius.bbp.im.gm.objects.User toUser = new de.citunius.bbp.im.gm.objects.User();
		toUser.setUserName("jane");
		toUser.setFirstName("jane");
		toUser.setLastName("Doe");
		Data data = new Data();
		
		Message message = new Message();
		message.setFrom(fromUser);
		message.setTo(toUser);
		message.setData(data);				
		message.setText(userMessage);

		boolean hasAnonymousUserAccount = true;
		MobileAppProvider mobileAppProvider = new MobileAppProvider(TENANTID, ACCOUNTID, 0, MESSENGER_NAME, MESSENGER_NAME, "Enabled");
		AnonymousUserAccount anonymousUserAccount = new AnonymousUserAccount(TENANTID, ACCOUNTID, 0, USERNAME, FIRSTNAME, LASTNAME, mobileAppProvider, new Date());
		boolean hasMobileUserAccount = false;
		String jsonMobileUserAccount = null;
		
		System.out.println("Sending user message: "+userMessage);
		String result = bl.handleIncomingMessage(TENANTID, ACCOUNTID, pluginMap, message.toJson().toString(), hasAnonymousUserAccount, anonymousUserAccount.toJson().toString(), hasMobileUserAccount, jsonMobileUserAccount);
		System.out.println("Business Logic replied: "+result);
	}
	
	/**
	 * Compose plugin map passed to business logic
	 * 
	 * @return
	 */
	public static HashMap<String, String> getPluginMap() {
		HashMap<String, String> pluginMap = new HashMap<>();
		pluginMap.put(ConstantsPlugin.MESSENGER_NAME, MESSENGER_NAME);
		pluginMap.put(ConstantsPlugin.PLUGIN_ID, PLUGIN_ID);
		pluginMap.put(ConstantsPlugin.PLUGIN_NAME, PLUGIN_NAME);
		
		pluginMap.put(ConstantsPlugin.TENANTID, TENANTID);
		pluginMap.put(ConstantsPlugin.ACCOUNTID, ACCOUNTID);
		pluginMap.put(ConstantsPlugin.PLUGIN_WEBUI_LANGUAGE, PLUGIN_WEBUI_LANGUAGE);
		pluginMap.put(ConstantsPlugin.PLUGIN_RESOURCE_PATH, PLUGIN_RESOURCE_PATH);
		pluginMap.put(ConstantsPlugin.BBP_WEBSERVICES_URL, BBP_WEBSERVICES_URL);
		pluginMap.put(ConstantsPlugin.PLUGIN_WORKDIR, PLUGIN_WORKDIR);
		pluginMap.put(ConstantsPlugin.PLUGIN_SYSTEMDIR, PLUGIN_SYSTEMDIR);
		pluginMap.put(ConstantsPlugin.INSTANTMESSENGER_FILESTORE_PATH, INSTANTMESSENGER_FILESTORE_PATH);
		pluginMap.put(BBP_APIACCOUNT_APIID_PARAM, BBP_APIACCOUNT_APIID);
		pluginMap.put(BBP_APIACCOUNT_APIKEY_PARAM, BBP_APIACCOUNT_APIKEY);
		return pluginMap;
	}

}
