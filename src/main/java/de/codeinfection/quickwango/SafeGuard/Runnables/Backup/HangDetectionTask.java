package de.codeinfection.quickwango.SafeGuard.Runnables.Backup;

import de.codeinfection.quickwango.SafeGuard.Events.ServerHangEvent;
import de.codeinfection.quickwango.SafeGuard.SafeGuard;
import de.codeinfection.quickwango.SafeGuard.SafeGuardTask;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author CodeInfection
 */
public class HangDetectionTask extends SafeGuardTask
{
    private final Server server;
    private final Thread mainThread;
    private long oldTime;
    private int frozenTicks;
    private World firstWorld;

    public HangDetectionTask(Server server, Thread mainThread)
    {
        super(1000);
        this.server = server;
        this.mainThread = mainThread;
        this.frozenTicks = 0;
        this.oldTime = 0;
    }

    @Override
    public void beforeStart()
    {
        this.oldTime = 0;
        this.firstWorld = this.server.getWorlds().get(0);
    }
    
    public void run()
    {
        long time = this.firstWorld.getFullTime();
        int delta = (int)(time - this.oldTime);
        if (this.frozenTicks < 5)
        {
            if (delta <= 0)
            {
                this.frozenTicks++;
            }
            else
            {
                this.frozenTicks = 0;
            }
        }
        else
        {
            StackTraceElement[] stacktrace = this.mainThread.getStackTrace();
            SafeGuard.error("Stacktrace of Thread '" + this.mainThread.getName() + "'");
            for (StackTraceElement stacktraceElem : stacktrace)
            {
                SafeGuard.error("\t" + stacktraceElem.getClassName() + "." + stacktraceElem.getMethodName() + "(" + stacktraceElem.getFileName() + ":" + stacktraceElem.getLineNumber() + ")");
            }

            this.server.getPluginManager().callEvent(new ServerHangEvent(stacktrace));

            this.gracefullyDie();
        }

        this.oldTime = time;
    }

    private static int disabledPlugins;
    private static int savedWorlds;
    
    private void gracefullyDie()
    {
        for (Player player : this.server.getOnlinePlayers())
        {
            // TODO make configurable
            player.kickPlayer("The server is restarting now...");
        }

        Plugin[] plugins = this.server.getPluginManager().getPlugins();
        final int availablePlugins = plugins.length - 1;
        disabledPlugins = 0;

        for (final Plugin plugin : plugins)
        {
            if (plugin == SafeGuard.getInstance())
            {
                continue;
            }
            // a own thread for every plugin to not hang our self
            (new Thread(new Runnable()
            {
                public void run()
                {
                    SafeGuard.log("Disabling plugin: " + plugin.getDescription().getName());
                    plugin.onDisable();
                    ++disabledPlugins;
                }
            }))
            .start();
        }

        List<World> worlds = this.server.getWorlds();
        final int availableWorlds = worlds.size();
        savedWorlds = 0;
        
        for (final World world : worlds)
        {
            // a own thread for every world to not hang our self
            (new Thread(new Runnable()
            {
                public void run()
                {
                    try
                    {
                        SafeGuard.log("Saving world: " + world.getName());
                        world.save();
                    }
                    catch (Throwable t)
                    {
                        SafeGuard.error("Failed to save this world!", t);
                    }
                }
            }))
            .start();
        }

        final long timeout = System.currentTimeMillis() + 1000 * 30;

        while (disabledPlugins < availablePlugins && savedWorlds < availableWorlds && System.currentTimeMillis() < timeout)
        {
            try
            {
                Thread.sleep(10);
            }
            catch (InterruptedException e)
            {}
        }

        System.exit(1);
    }

    private void warn(int delta)
    {
        this.server.broadcastMessage(ChatColor.DARK_RED + "#### Warning ####");
        this.server.broadcastMessage("The server seems to start lagging!");
        this.server.broadcastMessage("The server did only reach " + delta + " ticks");
        this.server.broadcastMessage("Prepare for a restart or timeout...");
        this.server.broadcastMessage("");
    }
}
