package com.isnakebuzz.skywars.Scheduler;


import com.isnakebuzz.skywars.SkyWars;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class BukkitScheduler extends SchedulerBase {

    private SkyWars plugin;

    public BukkitScheduler(SkyWars plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @Override
    public void runAsync(Runnable runnable, boolean sameThread) {
        if (sameThread) {
            runnable.run();
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
        }
        super.runAsync(runnable, sameThread);
    }

    @Override
    public void runSync(Runnable runnable, boolean sameThread) {
        if (sameThread) {
            runnable.run();
        } else {
            Bukkit.getScheduler().runTask(plugin, runnable);
        }
        super.runSync(runnable, sameThread);
    }

    @Override
    public void runSync(Runnable runnable) {
        runSync(runnable, false);
    }

    @Override
    public int runRepeating(Runnable runnable, int seconds) {
        BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, runnable, 0, seconds * 20);
        return task.getTaskId();
    }

    @Override
    public void stopRepeating(int taskId) {
        Bukkit.getScheduler().cancelTask(taskId);
    }

    @Override
    public int runDelayed(Runnable runnable, Date when) {
        super.runDelayed(runnable, when);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(when.getTime() - (new Date()).getTime());
        return runDelayed(runnable, (seconds < 0 ? 0 : seconds));
    }

    @Override
    public int runDelayed(Runnable runnable, long seconds) {
        super.runDelayed(runnable, seconds);
        return Bukkit.getScheduler().runTaskLater(plugin, runnable, seconds * 20).getTaskId();
    }

}
