package nl.thedutchmc.floofbot.jdaEvents;

import java.io.File;
import java.util.Random;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import nl.thedutchmc.floofbot.ConfigurationHandler;
import nl.thedutchmc.floofbot.FileHandler;

public class MessageReceivedEventListener extends ListenerAdapter {

	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		
		if(event.getAuthor().isBot()) {
			return;
		}
		
		if(!event.getMessage().getChannel().getId().equals(ConfigurationHandler.get("floofChannel"))) {
			return;
		}
		
		if(!event.getMessage().getContentDisplay().equals(ConfigurationHandler.get("botPrefix") + "rawr")) {
			return;
		}
		
		event.getChannel().sendMessage("Preparing floof...").queue();
		
		int floofSize = FileHandler.getFloofPaths().size();
		int floofIndex = new Random().nextInt(floofSize);
		
		File floofFile = FileHandler.getFloofByIndex(floofIndex);		
		EmbedBuilder embed = new EmbedBuilder()
				.setImage("attachment://floof.jpg");
		event.getChannel().sendFile(floofFile, "floof.jpg").embed(embed.build()).queue();
		
		
	}
	
}
