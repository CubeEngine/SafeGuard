package de.cubeisland.SafeGuard.Events;

import org.bukkit.Server;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author CodeInfection
 */
public class ServerLowMemoryEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();

    private final Server server;
    private final long maximum;
    private final long free;
    private final double percentageFree;

    public ServerLowMemoryEvent(Server server, long maximum, long free, double percentageFree)
    {
        this.server = server;
        this.maximum = maximum;
        this.free = free;
        this.percentageFree = percentageFree;
    }

    public Server getServer()
    {
        return this.server;
    }

    public long getMaximumMemory()
    {
        return this.maximum;
    }

    public long getFreeMemory()
    {
        return this.free;
    }

    public double getFreeMemoryPercentage()
    {
        return this.percentageFree;
    }

    @Override
    public HandlerList getHandlers()
    {
        return handlers;
    }

    public static HandlerList getHandlerList()
    {
        return handlers;
    }
}
