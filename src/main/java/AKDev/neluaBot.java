package AKDev;

import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class neluaBot extends ListenerAdapter{
    
    public static Message message;
    public static String code = "";
    public static String flags = "";
    public static String BOT_TOKEN = "";
    
    public static void main(String[] args) {
        
        try {
            JDA jda = JDABuilder.createDefault(BOT_TOKEN).addEventListeners(new neluaBot()).build();
            jda.awaitReady();
        } catch (LoginException ex) {
            ex.printStackTrace();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

    }
    
    // BUG: The result is empty no matter what's the input
    // BUG: if there's a while loop runing, if another code starts running it won't run because the code.exe is already running
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
    	
    	message = event.getMessage();

        if(message.getContentRaw().startsWith("./nelua")){
        	
        	if(message.getContentRaw().endsWith("```")) {
        		
            	code = getCode(message.getContentRaw());
            	flags = getFlags(message.getContentRaw());
            	
            	if(!code.isBlank()) {
            		
            		
            	} else {
            		message.reply("```The message does not contain code```").mentionRepliedUser(false).queue();
            	}
            	
        	} else {
        		message.reply("```The format of the message is incorrect```").mentionRepliedUser(false).queue();
        	}
            
        }
    }
    
    public String getCode(String message) {
    	String code = message.substring(message.indexOf("```lua") + 6, message.lastIndexOf("```")).strip();
    	return code;
    }
    
    public String getFlags(String message) {
    	String flags = message.substring(message.indexOf("./nelua") + 7, message.indexOf("```")).strip();
    	return flags;
    }
    
    
}