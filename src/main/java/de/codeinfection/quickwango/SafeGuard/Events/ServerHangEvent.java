package de.codeinfection.quickwango.SafeGuard.Events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author CodeInfection
 */
public class ServerHangEvent extends Event
{
    private static final HandlerList handlers = new HandlerList();
    private final StackTraceElement[] stacktrace;

    public ServerHangEvent(StackTraceElement[] stacktrace)
    {
        this.stacktrace = stacktrace;
    }

    public StackTraceElement[] getStacktrace()
    {
        return this.stacktrace;
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
