package me.sirimperivm.randomRewardGiver;

import me.sirimperivm.randomRewardGiver.utils.ConfigManager;
import me.sirimperivm.randomRewardGiver.utils.ModuleManager;
import me.sirimperivm.randomRewardGiver.utils.colors.Colors;
import me.sirimperivm.randomRewardGiver.utils.others.Errors;
import me.sirimperivm.randomRewardGiver.utils.others.Logger;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("all")
public final class Main extends JavaPlugin {

    private Main plugin;
    private static Main instance;

    private Colors colors;
    private Logger log;
    private ConfigManager configManager;
    private Errors errors;
    private ModuleManager modules;

    @Override
    public void onEnable() {
        plugin = this;
        instance = this;

        colors = new Colors(plugin);
        log = new Logger(plugin, "RandomRewardGiver");
        configManager = new ConfigManager(plugin);
        errors = new Errors(plugin);
        modules = new ModuleManager(plugin);

        log.stamp();
        log.success("Plugin attivato correttamente.");
    }

    @Override
    public void onDisable() {
        log.stamp();
        log.success("Plugin disattivato correttamente.");
    }

    public Main getPlugin() {
        return plugin;
    }

    public static Main getInstance() {
        return instance;
    }

    public Colors getColors() {
        return colors;
    }

    public Logger getLog() {
        return log;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public Errors getErrors() {
        return errors;
    }

    public ModuleManager getModules() {
        return modules;
    }
}
