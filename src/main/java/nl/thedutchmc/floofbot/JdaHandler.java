package nl.thedutchmc.floofbot;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import nl.thedutchmc.floofbot.jdaEvents.MessageReceivedEventListener;

public class JdaHandler {

	private static JDA jda;
	
	public void setupJda(String token) {
		
		try {
			
			List<GatewayIntent> intents = new ArrayList<>();
			intents.add(GatewayIntent.GUILD_MESSAGES);
			
			jda = JDABuilder.createDefault(token)
					.setActivity(Activity.playing("FloofBot-ing"))
					.enableIntents(intents)
					.build();
			
			jda.awaitReady();
		
			jda.addEventListener(new MessageReceivedEventListener());
			
		} catch (LoginException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static JDA getJda() {
		return jda;
	}
}
