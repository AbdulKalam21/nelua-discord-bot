package AKDev;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class main extends ListenerAdapter{
    
    public static Process process;
    public static Message msg;
    public static File error;
    public static File output;
    public static String code;
    public static FileWriter filewriter;
    
    public static void main(String[] args) {
        
        try {
            JDA jda = JDABuilder.createDefault("BOT_TOKEN").addEventListeners(new main()).build();
            jda.awaitReady();
        } catch (LoginException | InterruptedException ex){
            ex.printStackTrace();
        }
        
    }
    
    // BUG: The result is empty no matter what's the input
    // BUG: if there's a while loop runing, if another code starts running it won't run because the code.exe is already running
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        msg = event.getMessage();
        if(msg.getContentRaw().startsWith("-compile")){
            code = msg.getContentRaw().substring(15, msg.getContentRaw().lastIndexOf("```"));
 
            try {
                filewriter = new FileWriter("code.nelua");
                filewriter.write(code);
                filewriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
            msg.reply("```Compiling...```").mentionRepliedUser(false).queue(response -> {    
                try {
                    process = Runtime.getRuntime().exec("cmd /c nelua code.nelua > output.txt 2> error.txt");
                    
                    if(process.waitFor(300, TimeUnit.SECONDS)){
                        error = new File("error.txt");
                        output = new File("output.txt");
                        if(error.length() != 0){
                            String result = Files.readString(Path.of("error.txt"));
                            response.editMessage("```" + result + "```").mentionRepliedUser(false).queue();
                        }else if(output.length() != 0){
                            String result = Files.readString(Path.of("output.txt"));
                            response.editMessage("```" + result + "```").mentionRepliedUser(false).queue();
                        }
                    }else{
                        process.destroyForcibly();
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                });
             
        }
        
    }
    
}