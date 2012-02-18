package de.codeinfection.quickwango.SafeGuard.Events;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author CodeInfection
 */
public class BackupBeforeEvent extends Event implements Cancellable
{
    private static final HandlerList handlers = new HandlerList();
    private boolean cancelled;

    public BackupBeforeEvent()
    {
        this.cancelled = false;
    }

    public boolean isCancelled()
    {
        return this.cancelled;
    }

    public void setCancelled(boolean cancel)
    {
        this.cancelled = cancel;
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
