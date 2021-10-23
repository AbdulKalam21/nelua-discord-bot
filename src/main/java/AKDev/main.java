package AKDev;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class main extends ListenerAdapter{
    
    FileWriter file;

    public static void main(String[] args) {
        
        try {
            JDA jda = JDABuilder.createDefault("OTAxMzE0ODQxMDc4MjgwMjIy.YXOE2w.g5J1Lc_inYW7o9PgkPpDMFA59ps")
                    .addEventListeners(new main()).build();
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
        
        if(msg.getContentRaw().startsWith("-compile")){
            
            String code = msg.getContentRaw().substring(15, msg.getContentRaw().lastIndexOf("```"));
            try {
                file = new FileWriter("code.nelua");
                file.write(code);
            } catch (IOException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            msg.getChannel().sendMessage("Compiling...").queue();

            
        }
        
    }
    
}

