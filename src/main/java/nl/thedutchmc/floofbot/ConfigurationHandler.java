package nl.thedutchmc.floofbot;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Properties;

public class ConfigurationHandler {

	private static HashMap<String, String> configOptions = new HashMap<>();
	
	public void load() throws URISyntaxException, IOException{
		
		//Not a one-liner because this is way more clear to the reader
		final File jarPath = new File(ConfigurationHandler.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		final File folderPath = new File(jarPath.getParentFile().getPath());
		final File configFile = new File(folderPath, "floofbot.properties");
		
		//Check if the configuration file exists.
		if(!configFile.exists()) {
			
			//It doesn't exist, so create it
			configFile.createNewFile();
			
			//Append the default options to the config file, and include a random UUID
			FileWriter fw = new FileWriter(configFile, true);
			fw.write("#FloofBot Configuration File\n");
			fw.write("#FloofBot will not verify this config file for you. So make sure it is correct or you might get errors!\n");
			fw.write("\n");
			fw.write("#The path of where we can find the Floof .jpg's. Make sure to NOT include a trailing slash! Use a double \\ on Windows!\n");
			fw.write("floofPath=\n");
			fw.write("#Token of the bot\n");
			fw.write("botToken=\n");
			fw.write("#Prefix token to use. E.g $ or ^\n");
			fw.write("botPrefix=$\n");
			fw.write("#What channel should floofs be allowed in. Requires Channel ID, not name!\n");
			fw.write("floofChannel=\n");
			fw.write("#How often should we walk the disk to check for new Floofs\n");
			fw.write("floofDiscoveryInterval=5\n");
			
			fw.close();
		}
		
		//Load the configuration file
		Properties properties = new Properties();
		properties.load(new FileInputStream(configFile));

		configOptions.put("floofPath", properties.getProperty("floofPath"));
		configOptions.put("botToken", properties.getProperty("botToken"));
		configOptions.put("botPrefix", properties.getProperty("botPrefix"));
		configOptions.put("floofChannel", properties.getProperty("floofChannel"));
		configOptions.put("floofDiscoveryInterval", properties.getProperty("floofDiscoveryInterval"));
		
		if(configOptions.get("floofPath").equals("")) {
			FloofBot.errorLog("floofPath is not set! Exiting");
			System.exit(-1);
		}
		
		FloofBot.infoLog("Using Floof folder: " + configOptions.get("floofPath"));
		
		if(configOptions.get("botToken").equals("")) {
			FloofBot.errorLog("botToken is not set! Exiting");
			System.exit(-1);
		}
		
		if(configOptions.get("floofChannel").equals("")) {
			FloofBot.errorLog("floofChannel is not set! Exiting");
			System.exit(-1);
		}
	}
	
	public static String get(String option) {
		return configOptions.get(option);
	}
}
