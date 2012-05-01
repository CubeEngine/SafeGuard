package de.cubeisland.SafeGuard;
import de.cubeisland.libMinecraft.StringUtils;

/**
 *
 * @author CodeInfection
 */
public class Message
{
    private final String name;
    private final String message;

    private Message(String name, String message)
    {
        this.name = name;
        this.message = message;
    }

    public String getName()
    {
        return this.name;
    }

    public String[] getLines(Object... params)
    {
        return StringUtils.explode("\n", String.format(this.message, params), true);
    }
}
