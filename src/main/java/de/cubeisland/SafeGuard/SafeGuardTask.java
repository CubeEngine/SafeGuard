package de.cubeisland.SafeGuard;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.plugin.Plugin;


/**
 *
 * @author CodeInfection
 */
public abstract class SafeGuardTask extends TimerTask
{
    private static final Map<String, Message> messages = new HashMap<String, Message>();

    private final Plugin plugin;
    private Timer timer;
    private int interval;

    public SafeGuardTask(Plugin plugin, int interval)
    {
        this.plugin = plugin;
        this.timer = new Timer(this.getClass().getSimpleName());
        this.interval = interval;
    }

    public final void setInterval(int interval)
    {
        this.interval = interval;
    }

    public final int getInterval()
    {
        return this.interval;
    }

    public abstract void run();

    public void beforeStart()
    {}

    public void beforeStop()
    {}

    public final void start()
    {
        this.beforeStart();
        this.timer.schedule(this, this.interval, this.interval);
    }

    public final void stop()
    {
        this.beforeStop();
        this.timer.cancel();
    }

    public void broadcastMessage(String name, Object... params)
    {
        for (String line : messages.get(name).getLines(params))
        {

        }
    }

    public static void addMessage(Message message)
    {
        messages.put(message.getName(), message);
    }

    public static void removeMessage(String name)
    {
        messages.remove(name);
    }

    public static void clearMessages()
    {
        messages.clear();
    }
}
