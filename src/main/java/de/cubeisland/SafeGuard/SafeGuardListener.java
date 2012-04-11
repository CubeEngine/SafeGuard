package de.cubeisland.SafeGuard;

import de.cubeisland.SafeGuard.Events.BackupBeforeEvent;
import de.cubeisland.SafeGuard.Events.BackupCompleteEvent;
import de.cubeisland.SafeGuard.Events.BackupFailedEvent;
import de.cubeisland.SafeGuard.Events.ServerHangEvent;
import de.cubeisland.SafeGuard.Events.ServerLowMemoryEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

/**
 *
 * @author CodeInfection
 */
public class SafeGuardListener implements Listener
{
    @EventHandler(priority = EventPriority.MONITOR)
    public void handleBackupBefore(BackupBeforeEvent event)
    {
        if (!event.isCancelled())
        {
            SafeGuard.log("Backup will be started now...");
        }
    }

    @EventHandler
    public void handleBackupFailed(BackupFailedEvent event)
    {
        SafeGuard.error("Backup could not be completed!", event.getCause());
    }

    @EventHandler
    public void handleBackupCompleted(BackupCompleteEvent event)
    {
        SafeGuard.log("Backup completed after " + Math.round(event.getRuntime() / 1000D) + " seconds completed!");
    }

    public void handleLowMemory(ServerLowMemoryEvent event)
    {
        SafeGuard.log("The server is running low on memory, just " + Math.round(event.getFreeMemoryPercentage()) + "% over!");
    }

    public void handleHang(ServerHangEvent event)
    {
        SafeGuard.log("The server just freezed!");
        SafeGuard.log("I try to shutdown the process as clean as possible.");
    }
}
