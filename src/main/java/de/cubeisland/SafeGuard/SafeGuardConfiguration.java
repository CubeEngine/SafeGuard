package de.cubeisland.SafeGuard;

import org.bukkit.configuration.Configuration;

public class SafeGuardConfiguration
{
    public final String saveguard_warning1;
    public final String saveguard_warning2;
    public final String saveguard_warning3;
  
    public SafeGuardConfiguration(Configuration config)
    {
        this.saveguard_warning1 = config.getString("saveguard.warning1");
        this.saveguard_warning2 = config.getString("saveguard.warning2");
        this.saveguard_warning3 = config.getString("saveguard.warning3");
    }
}
