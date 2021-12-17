package AKDev;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

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
    public static Process process;
    public static String output;
    public static String error;
    public static int waitTime = 5;
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
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        
        message = event.getMessage();

        if(message.getContentRaw().startsWith("./nelua")){
            
            if(message.getContentRaw().endsWith("```")) {
                
                code = getCode(message.getContentRaw());
                flags = getFlags(message.getContentRaw());
                
                if(!code.isBlank()) {
                    
                    try {
                        
                        Files.writeString(Path.of("code.nelua"), code);

                        process = Runtime.getRuntime().exec("compile.bat");
                        
                        if(process.waitFor(waitTime, TimeUnit.SECONDS) ) {
                            
                            output = readFromFile("output.txt");
                            error = readFromFile("error.txt");                  
                            
                            if(Files.exists(Path.of("output.png"), LinkOption.NOFOLLOW_LINKS)) {
                            	message.reply(new File("output.png")).mentionRepliedUser(false).queue();
                            	Files.delete(Path.of("output.png"));
                            } else if(output.length() == 0) {
                                if(error.length() == 0) {
                                    message.reply("```Compiled Sucessfully ```").mentionRepliedUser(false).queue();
                                } else {
                                	try {
                                		message.reply("```" + error + "```").mentionRepliedUser(false).queue();
                                	} catch (IllegalArgumentException ex) {
                                		message.reply(new File("error.txt")).mentionRepliedUser(false).queue();
                                	}
                                }
                            } else {
                            	try {
                                    message.reply("```" + output + "```").mentionRepliedUser(false).queue();
                            	} catch (IllegalArgumentException ex) {
                            		message.reply(new File("output.txt")).mentionRepliedUser(false).queue();
                            	}
                            }
                            
                            
                        } else {
                            process.destroyForcibly();
                            message.reply("```Compilation was terminated because of time limit```").queue();
                        }
                        
                    } catch (IOException | InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    
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
    
    public String readFromFile(String file) throws IOException {
    	String data = "";
    	try {
    		data = Files.readString(Path.of(file), Charset.forName("UTF-8"));
		} catch (MalformedInputException a) {
			try {
				data = Files.readString(Path.of(file), Charset.forName("ISO-8859-1"));
			} catch (MalformedInputException b) {
				try {
					data = Files.readString(Path.of(file), Charset.forName("US-ASCII"));
				} catch (MalformedInputException c) {
					try {
			    		data = Files.readString(Path.of(file), Charset.forName("UTF-16BE"));
					} catch (MalformedInputException d) {
						try {
							data = Files.readString(Path.of(file), Charset.forName("UTF-16LE"));
						} catch (MalformedInputException e) {
							data = Files.readString(Path.of(file), Charset.forName("UTF-16"));
						}
					}
				}
			}
		}
    	return data;
    }
}