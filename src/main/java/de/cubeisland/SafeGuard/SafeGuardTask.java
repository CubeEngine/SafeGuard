package de.cubeisland.SafeGuard;

import java.util.Timer;
import java.util.TimerTask;


/**
 *
 * @author CodeInfection
 */
public abstract class SafeGuardTask extends TimerTask
{
    private Timer timer;
    private int interval;

    public SafeGuardTask(int interval)
    {
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
}
