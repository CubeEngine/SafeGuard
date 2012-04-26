package de.cubeisland.SafeGuard;

import org.bukkit.configuration.Configuration;

public class SafeGuardConfiguration
{
    public final double safeguard_mem_warn;
    public final double safeguard_mem_reboot;
    public final int    safeguard_hang_frozenticks;
    
    public SafeGuardConfiguration(Configuration config)
    {
         this.safeguard_mem_warn   = config.getDouble("safeguard.mem.warn");
         this.safeguard_mem_reboot = config.getDouble("safeguard.mem.reboot");
         this.safeguard_hang_frozenticks = config.getInt("safeguard.hang.frozenticks");
         
    }
       
}
