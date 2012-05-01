package de.cubeisland.SafeGuard.Tasks;

import de.cubeisland.SafeGuard.Events.BackupBeforeEvent;
import de.cubeisland.SafeGuard.Events.BackupCompleteEvent;
import de.cubeisland.SafeGuard.Events.BackupFailedEvent;
import de.cubeisland.SafeGuard.SafeGuard;
import de.cubeisland.SafeGuard.SafeGuardTask;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

/**
 *
 * @author CodeInfection
 */
public class BackupTask extends SafeGuardTask
{
    private final Server server;
    private final PluginManager pm;
    private final static SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

    public BackupTask(Plugin plugin, int interval)
    {
        super(plugin, interval);
        this.server = plugin.getServer();
        this.pm = server.getPluginManager();
        try
        {
            // TODO implement a new backup system, incremental would be prefered
        }
        catch (Throwable t)
        {
            SafeGuard.error(t.getLocalizedMessage(), t);
        }
    }

    public void run()
    {
        BackupBeforeEvent event = new BackupBeforeEvent();
        this.pm.callEvent(event);

        if (!event.isCancelled())
        {
            final long startTime = System.currentTimeMillis();
            // Hier wird hingegangen und ein backup erstellt
            // dafür wird als erstes das autosaving vom server deaktiviert
            // und die welten werden gespeichert
            List<World> worlds = this.server.getWorlds();
            List<Boolean> autoSaveStates = new ArrayList<Boolean>(worlds.size());
            for (World world : worlds)
            {
                autoSaveStates.add(world.isAutoSave());
                world.setAutoSave(false);
                world.save();
            }

            // dann wird das backup erstellt
            // in unserem fall: ein git commit
            try
            {
                // TODO implement a new backup system, incremental would be prefered
            }
            catch (Throwable t)
            {
                SafeGuard.log("Failed to do the backup!");
                this.pm.callEvent(new BackupFailedEvent(t));
            }

            // zum schluss wieder das autosaving aktivieren
            for (int i = 0; i < worlds.size(); ++i)
            {
                worlds.get(i).setAutoSave(autoSaveStates.get(i));
            }

            this.pm.callEvent(new BackupCompleteEvent(System.currentTimeMillis() - startTime));
        }
    }
}
