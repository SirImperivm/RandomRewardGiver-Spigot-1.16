package me.sirimperivm.randomRewardGiver;

import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("all")
public final class Main extends JavaPlugin {

    private Main plugin;
    private static Main instance;

    @Override
    public void onEnable() {
        plugin = this;
        instance = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Main getPlugin() {
        return plugin;
    }

    public static Main getInstance() {
        return instance;
    }
}
