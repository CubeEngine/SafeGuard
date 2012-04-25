package de.cubeisland.SafeGuard.Tasks;

import de.cubeisland.SafeGuard.Events.ServerLowMemoryEvent;
import de.cubeisland.SafeGuard.SafeGuard;
import de.cubeisland.SafeGuard.SafeGuardConfiguration;
import de.cubeisland.SafeGuard.SafeGuardTask;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

/**
 *
 * @author CodeInfection
 */
public class LowMemoryTask extends SafeGuardTask
{
    private final static Runtime runtime = Runtime.getRuntime();
    private final Server server;
    private final double warnMinimum;
    private final double rebootMinimum;
    private long lastWarnedTick;
    private long tick;
    private final SafeGuardConfiguration config = SafeGuard.getInstance().getConfiguration();
    
    public LowMemoryTask(Server server, double warnMinimum, double rebootMinimum)
    {
        super(1000);
        this.server = server;
        this.warnMinimum = warnMinimum;
        this.rebootMinimum = rebootMinimum;
        this.lastWarnedTick = 0;
        this.tick = 0;
    }

    @Override
    public void beforeStart()
    {
        this.tick = 0;
    }

    public void run()
    {
        ++this.tick;
        final long used = runtime.totalMemory() - runtime.freeMemory();
        final long max = runtime.maxMemory();
        final long free = max - used;
        final double percentageFree = (double)free / (double)max;

        if (percentageFree < this.warnMinimum)
        {
            this.server.getPluginManager().callEvent(new ServerLowMemoryEvent(this.server, max, free, percentageFree));
            if (percentageFree <= this.rebootMinimum)
            {
                this.reboot();
            }
            else
            {
                if (this.tick - this.lastWarnedTick >= 10)
                {
                    this.warn(free, max, percentageFree);
                    this.lastWarnedTick = this.tick;
                }
            }
        }
    }

    /**
     *
     * @todo rework to post the contents of a file instead of a static message
     * @param free
     * @param maximum
     * @param percentage
     */
    private void warn(long free, long maximum, double percentage)
    {
        
        this.server.broadcastMessage(ChatColor.DARK_RED + "#### Warning ####");
        this.server.broadcastMessage(config.saveguard_warning1);
        this.server.broadcastMessage(String.format(config.saveguard_warning2, Math.round(percentage * 100), free / 1024 / 1024, maximum / 1024 / 1024));
        this.server.broadcastMessage(config.saveguard_warning3);
        this.server.broadcastMessage("");
    }

    private void reboot()
    {
        for (Player player : this.server.getOnlinePlayers())
        {
            // TODO make configurable
            player.kickPlayer("The server is restarting now...");
        }
        server.shutdown();
    }
}
