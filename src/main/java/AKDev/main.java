package AKDev;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class main extends ListenerAdapter{

    public static void main(String[] args) {
        
        try {
            JDA jda = JDABuilder.createDefault("OTAxMzE0ODQxMDc4MjgwMjIy.YXOE2w.g5J1Lc_inYW7o9PgkPpDMFA59ps").addEventListeners(new main()).build();
            jda.awaitReady();
        } catch (LoginException ex){
            ex.printStackTrace();
        }
        catch (InterruptedException ex){
            ex.printStackTrace();
        }
    }
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        
        Message msg = event.getMessage();
        String[] output;    
        FileWriter filewriter;
        Process p;
        String out;
        String code;
        
        if(event.getMessage().getContentRaw().startsWith("-compile")){
            
            code = msg.getContentRaw().substring(15, msg.getContentRaw().lastIndexOf("```"));
            
            try {

                filewriter = new FileWriter("code.nelua");
                filewriter.write(code);
                filewriter.close();
                msg.getChannel().sendMessage("Compiling...").queue();
                p = Runtime.getRuntime().exec("cmd /c nelua code.nelua");
            
                BufferedReader reader = new BufferedReader(new InputStreamReader( p.getInputStream()));
                
                while((out = reader.readLine()) != null) {
                    msg.getChannel().sendMessage(out).queue();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            
        }
        
    }
    
}