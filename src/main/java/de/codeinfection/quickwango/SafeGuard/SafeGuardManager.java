package de.codeinfection.quickwango.SafeGuard;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Wenn es interessiert: das ist ein sogenanntes Singleton, eine klasse die nur einmal instanzoert werden kann
 *
 *
 * @author CodeInfection
 */
public class SafeGuardManager
{
    private static SafeGuardManager instance = null;

    private Map<String, SafeGuardTask> safeguards;

    private SafeGuardManager()
    {
        this.safeguards = new ConcurrentHashMap<String, SafeGuardTask>();
    }

    public static SafeGuardManager getInstance()
    {
        if (instance == null)
        {
            instance = new SafeGuardManager();
        }
        return instance;
    }

    public SafeGuardManager registerTask(String name, SafeGuardTask task)
    {
        this.safeguards.put(name, task);
        return this; // <-- das wäre ein fluent interface
    }

    public SafeGuardManager unregisterTask(String name)
    {
        this.safeguards.remove(name);
        return this;
    }

    public boolean isTaskRegistered(String name)
    {
        return this.safeguards.containsKey(name);
    }

    public boolean isTaskRegistered(SafeGuardTask task)
    {
        return this.safeguards.containsValue(task);
    }

    public SafeGuardTask getTask(String name)
    {
        return this.safeguards.get(name);
    }

    public SafeGuardManager clearTasks()
    {
        this.safeguards.clear();
        return this;
    }

    public SafeGuardManager startAll()
    {
        for (SafeGuardTask task : this.safeguards.values())
        {
            task.start();
        }
        return this;
    }

    public SafeGuardManager stopAll()
    {
        for (SafeGuardTask task : this.safeguards.values())
        {
            task.stop();
        }
        return this;
    }
}
