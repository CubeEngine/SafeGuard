package de.cubeisland.SafeGuard;

//import de.codeinfection.quickwango.SafeGuard.Runnables.Backup.BackupTask;
import de.cubeisland.SafeGuard.Tasks.HangDetectionTask;
import de.cubeisland.SafeGuard.Tasks.LowMemoryTask;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SafeGuard extends JavaPlugin
{
    private static SafeGuard instance = null;
    protected static Logger logger = null;
    public static boolean debugMode = false;
    
    protected Server server;
    protected PluginManager pm;
    protected SafeGuardConfiguration config;
    protected File dataFolder;
    protected SafeGuardText textsender;

    public SafeGuard()
    {
        instance = this;
    }

    // hier ists kein singleton!
    public static SafeGuard getInstance()
    {
        return instance;
    }

    @Override
    public void onEnable()
    {
        logger = this.getLogger();
        this.server = this.getServer();
        this.pm = this.server.getPluginManager();
        this.dataFolder = this.getDataFolder();

        this.dataFolder.mkdirs();

        this.reloadConfig();
        Configuration configuration = this.getConfig();
        configuration.options().copyDefaults(true);
        debugMode = configuration.getBoolean("debug");
        this.config = new SafeGuardConfiguration(configuration);
        this.saveConfig();

        SafeGuardManager.getInstance()
            .registerTask("lowmem", new LowMemoryTask(this.server, 0.3, 0.15))
            //.registerTask("hang", new HangDetectionTask(this.server, Thread.currentThread()))
            //.registerTask("backup", new BackupTask(1000 * 3, this.server)) - rework for scripts
        .startAll();
        this.textsender = new SafeGuardText(this.server,this.getClass());
        //TODO test klappt :) noch ohne Farbe
        textsender.broadcast("test", 123, "abc");
        
        if (debugMode)
        {
            this.getCommand("hang").setExecutor(new CommandExecutor()
            {
                public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings)
                {
                    try
                    {
                        Thread.sleep(1000 * 60 * 60 * 24);
                    }
                    catch (InterruptedException ex)
                    {}
                    return true;
                }
            });

            this.getCommand("fillram").setExecutor(new CommandExecutor()
            {
                public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] strings)
                {
                    (new Thread(new Runnable()
                    {
                        public void run()
                        {
                            List<List<String>> junkMap = new ArrayList<List<String>>();
                            while (true)
                            {
                                ArrayList<String> lst = new ArrayList<String>();
                                for (int i = 0; i < 1000 * 1000; ++i)
                                {
                                    lst.add("aasdfysgydsfgydsfgydfyshlgkyjdsgfbkyjdfbgökjsdfblgksjdfbgklsjdfbglksdjfgbklsjdfgblskdjfgbklsjdfbglksjdfgbklsjdfbglksjdfgblksjdfbgklsjdfbgklsjdfbklgsjdfbglksjdfbglksjdfgbklsdjfgbklsjdfgbklsjdfgbklsjdfbgklsjdfgblksjdfbglskdjfgblskdjfgbklsdjfgblskdjfgbkldsjfgbksldjfgbklsdjfbgklsjdfbgklsjdfgbklsjdfbgklsjdbfgkljdsfblgskdjfbglskdjfgblskdjfgblskdjfgbklsdjfbglksdjfgbklsdjfgbklsjdgfb");
                                }
                                junkMap.add(lst);
                                try {
                                    Thread.sleep(400);
                                }
                                catch (InterruptedException ex)
                                {}
                            }
                        }
                    }))
                    .start();
                    return true;
                }
            });

            this.pm.registerEvents(new SafeGuardListener(), this);
        }

        log("Version " + this.getDescription().getVersion() + " enabled");
    }

    @Override
    public void onDisable()
    {
        SafeGuardManager.getInstance()
            .stopAll()
            .clearTasks();
        log("Version " + this.getDescription().getVersion() + " disabled");
    }

    public static void log(String msg)
    {
        logger.log(Level.INFO, msg);
    }

    public static void error(String msg)
    {
        logger.log(Level.SEVERE, msg);
    }

    public static void error(String msg, Throwable t)
    {
        logger.log(Level.SEVERE, msg, t);
    }

    public static void debug(String msg)
    {
        if (debugMode)
        {
            log("[debug] " + msg);
        }
    }
}
