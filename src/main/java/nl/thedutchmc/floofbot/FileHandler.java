package nl.thedutchmc.floofbot;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileHandler implements Runnable {

	private static List<String> floofPaths = new ArrayList<>();
	
	@Override
	public void run() {
		while(true) {
			
			FloofBot.infoLog("Discovering Floofs...");
			
			discoverFloof(ConfigurationHandler.get("floofPath"));
			
			FloofBot.infoLog("Discovered " + floofPaths.size() + " Floof files!");
			
			try {
				Thread.sleep(Integer.valueOf(ConfigurationHandler.get("floofDiscoveryInterval")) * 60 * 1000);
			} catch(InterruptedException e) {}
		}
	}
	
	public void setup() {
		Thread t = new Thread(this);
		t.start();
	}
	
	private void discoverFloof(String path) {
			
		//The Folder in which all our Floofs are stored. This path is set in the configuration.
		File storageFolder = new File(path);
	
		//Check if the storage folder exists, if not, create it.
		if(!storageFolder.exists()) {
			try {
			
				//Create the directory, and parent directories if those do not exist.
				Files.createDirectories(Paths.get(storageFolder.getAbsolutePath()));
			} catch (IOException | SecurityException e) {
				FloofBot.errorLog("Failed to create Floof directory! Please check your file permissions!");
			
				return;
			}
		}
	
		try {
			//Walk the provided Path
			Stream<Path> walk = Files.walk(Paths.get(storageFolder.getAbsolutePath()));
		
			//Filter on the file extension, must be .png, and put the found paths into the result list.
			List<String> result = walk.map(x -> x.toString()).filter(f -> f.endsWith(".jpg")).collect(Collectors.toList());
						
			//Close the walk
			walk.close();
		
			//Return the result since we are done
			floofPaths = result;
			
		} catch(IOException e) {
			FloofBot.errorLog("An IOException was thrown whilst discovering Floofs!");
			
			e.printStackTrace();
			return;
		}
	}
	
	public static File getFloofByIndex(int index) {
		return new File(floofPaths.get(index));
	}
	
	public static List<String> getFloofPaths() {
		return floofPaths;
	}


}
