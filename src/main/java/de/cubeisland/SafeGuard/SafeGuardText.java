package de.cubeisland.SafeGuard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    Class clazz;
    
    public SafeGuardText(Server server, Class clazz) 
    {
        this.server = server;
        this.clazz = clazz;
        this.addFile("test");
    }
    
    private void addFile(String file)
    {
        this.texts.put(file, this.readFile(file));
    }
        
    private ArrayList<String> readFile(String filename)
    {
        InputStream is = clazz.getResourceAsStream("/texts/"+filename+".txt");
        StringBuilder sb = new StringBuilder();
        byte[] buffer = new byte[512];
        int bytesRead;
        ArrayList<String> filetext = new ArrayList<String>();
        try
        {
            while ((bytesRead = is.read(buffer)) > 0)
            {
                sb.append(new String(buffer, 0, bytesRead, "UTF-8"));
            }
            is.close();
            for (String line : this.split(sb.toString()))
            {
                if (line.length()==0) continue;
                filetext.add(line);
            }
        }
        catch(IOException ex)
        {
            SafeGuard.error("Failed to read configurable textfiles",ex);
            return null;
        }

        
    /*    
        filename += ".txt";
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
        * 
        */
        
        return filetext;
    }


    private static String[] split(String string)
    {
        int pos, offset = 0, delimLen = "\n".length();
        List<String> tokens = new ArrayList<String>();
        String part;

        while ((pos = string.indexOf("\n", offset)) > -1)
        {
            part = string.substring(offset, pos);
            if (part.length() > 0)
            {
                tokens.add(part);
            }
            offset = pos + delimLen;
        }
        part = string.substring(offset);
        if (part.length() > 0)
        {
            tokens.add(part);
        }

        return tokens.toArray(new String[tokens.size()]);
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
            //TODO Farben
            SafeGuard.log(message+" | "+formatText);
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
