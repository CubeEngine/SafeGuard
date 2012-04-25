package de.cubeisland.SafeGuard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;

/**
 *
 * @author Faithcaio
 */
public class SafeGuardText {

    Map<String,ArrayList<String>> texts = new HashMap<String,ArrayList<String>>();
    Server server;
    
    public SafeGuardText(Server server) 
    {
        this.server = server;
        this.addFile("test");
    }
    
    private void addFile(String file)
    {
        this.texts.put(file, this.readFile(file));
    }
        
    private ArrayList<String> readFile(String filename)
    {
        filename += ".txt";
        //TODO Default-Dateien in Unterordner schieben!
        File file = new File(SafeGuard.getInstance().getDataFolder(), "texts\\"+filename);
        ArrayList<String> filetext = null;
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String text;
            filetext = new ArrayList<String>();
            while ((text = reader.readLine()) != null)
            {
                filetext.add(text);
            }
        }
        catch(IOException ex)
        {
            SafeGuard.error("Failed to read configurable textfiles",ex);
            return null;
        }
        return filetext;
    }
    
    public void send(CommandSender sender, String message, Object... args)
    {
        String formatText = this.fillArgs(message, args);
        while (formatText.length()>0)
        {
            sender.sendMessage(formatText.substring(0, formatText.indexOf("|")-1));
            formatText.replace(formatText.substring(0, formatText.indexOf("|")), "" );
        }
    }
    
    public void broadcast(String message, Object... args)
    {
        String formatText = this.fillArgs(message, args);
        while (formatText.length()>0)
        {
            int end = formatText.indexOf("|");
            server.broadcastMessage(formatText.substring(0, end));
            formatText = formatText.substring(end+1);
        }
    }
    
    private String fillArgs(String message, Object... args)
    {
        ArrayList<String> textlist = texts.get(message);
        int max = textlist.size();
        String formatText ="";
        for (int i=0; i<max ; ++i)
        {
            formatText = textlist.get(i) + "|";
        }
        formatText = String.format(formatText, args);
        return formatText;
    }
}
