package de.cubeisland.SafeGuard.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author CodeInfection
 */
public class BackupCompleteEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();

    private final long runtime;

    public BackupCompleteEvent(long runtime)
    {
        this.runtime = runtime;
    }

    public long getRuntime()
    {
        return this.runtime;
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
