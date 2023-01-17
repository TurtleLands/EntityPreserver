package me.mekb.entitypreserver;

import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;
import java.util.logging.Logger;

public final class EntityPreserver extends JavaPlugin {
    public Server server;
    private BukkitRunnable entityPreserverRunnable;
    private Logger logger;
    private World world;

    public void killPlugin(String msg) {
        logger.log(Level.SEVERE, msg);
        server.getPluginManager().disablePlugin(this);
    }

    @Override
    public void onEnable() {
        server = getServer();
        logger = getLogger();

        world = server.getWorld("world");
        if (world == null) {
            killPlugin("World 'world' doesn't exist");
            return;
        }

        entityPreserverRunnable = new BukkitRunnable() {
            public void run() {
                for (Entity entity : world.getEntities()) {
                    // prevent from despawning
                    if (entity.getType() == EntityType.FALLING_BLOCK)
                        entity.setTicksLived(1);
                    if (entity instanceof AreaEffectCloud aec) {
                        aec.setRadius(2.0f);
                        aec.setTicksLived(1);
                    }
                }
            }
        };
        entityPreserverRunnable.runTaskTimer(this, 0, 20);
    }

    @Override
    public void onDisable() {
        entityPreserverRunnable.cancel();
    }
}
