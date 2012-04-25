package de.cubeisland.SafeGuard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Faithcaio
 */
public class SafeGuardText {

    Map<String,ArrayList<String>> texts = new HashMap<String,ArrayList<String>>();
    
    public SafeGuardText() 
    {
        this.addFile("test");
    }
    
    private void addFile(String file)
    {
        this.texts.put(file, this.readFile(file));
    }
        
    private ArrayList<String> readFile(String filename)
    {
        File file = new File(SafeGuard.getInstance().getDataFolder(), filename);
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
}
