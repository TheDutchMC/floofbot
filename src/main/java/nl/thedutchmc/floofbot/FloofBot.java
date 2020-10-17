package nl.thedutchmc.floofbot;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FloofBot {

	public static void main(String[] args) {
		
		infoLog("Welcome to FloofBot by TheDutchMC.");
		infoLog("Reading configuration file...");
		
		ConfigurationHandler config = new ConfigurationHandler();
		try {
			config.load();
		} catch (Exception e) {
			errorLog("Something went wrong when reading the configuration file. Are your file permissions correct?");
			e.printStackTrace();
			System.exit(-1);
		}
		
		infoLog("Config file Read");
		infoLog("Starting FileHandler...");
		
		FileHandler fileHandler = new FileHandler();
		fileHandler.setup();
		
		infoLog("FileHandler started.");
		infoLog("Starting JDA...");
		
		JdaHandler jdaHandler = new JdaHandler();
		jdaHandler.setupJda(ConfigurationHandler.get("botToken"));
		
		infoLog("JDA Started.");
		infoLog("Bot is now ready to serve Floof.");
	}
	
	public static void infoLog(String log) {
		final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		
		System.out.println("[" + formatter.format(new Date()) + "][INFO] " + log);
	}
	
	public static void errorLog(String log) {
		final SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		
		System.err.println("[" + formatter.format(new Date()) + "][WARN] " + log);
	}
}
