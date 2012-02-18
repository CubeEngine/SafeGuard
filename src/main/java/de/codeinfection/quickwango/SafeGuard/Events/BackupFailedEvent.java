package de.codeinfection.quickwango.SafeGuard.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author CodeInfection
 */
public class BackupFailedEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();

    private final Throwable cause;

    public BackupFailedEvent(Throwable cause)
    {
        this.cause = cause;
    }

    public Throwable getCause()
    {
        return this.cause;
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
