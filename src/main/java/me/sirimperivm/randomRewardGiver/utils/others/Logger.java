package me.sirimperivm.randomRewardGiver.utils.others;

import me.sirimperivm.randomRewardGiver.Main;
import me.sirimperivm.randomRewardGiver.utils.colors.Colors;
import org.bukkit.Bukkit;

@SuppressWarnings("all")
public class Logger {

    private Main plugin;

    private Colors colors;

    private String pluginPrefix;
    private static String staticPrefix;

    public Logger(Main plugin, String pluginPrefix) {
        this.plugin = plugin;
        this.pluginPrefix = pluginPrefix;
        staticPrefix = pluginPrefix;
        colors = plugin.getColors();
    }

    public void success(String message) {
        plugin.getServer().getConsoleSender().sendMessage(colors.translatedString("&a[" + pluginPrefix +"] " + message));
    }

    public void info(String message) {
        plugin.getServer().getConsoleSender().sendMessage(colors.translatedString("&e[" + pluginPrefix +"] " + message));
    }

    public void fail(String message) {
        plugin.getServer().getConsoleSender().sendMessage(colors.translatedString("&c[" + pluginPrefix +"] " + message));
    }

    public static void staticSuccess(String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(Colors.translateString("&a[" + staticPrefix +"] " + message));
    }

    public static void staticInfo(String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(Colors.translateString("&e[" + staticPrefix +"] " + message));
    }

    public static void staticFail(String message) {
        Bukkit.getServer().getConsoleSender().sendMessage(Colors.translateString("&c[" + staticPrefix +"] " + message));
    }

    public void stamp() {
        plugin.getServer().getConsoleSender().sendMessage(colors.translatedString("  ____                 _                 ____                            _  ____ _                "));
        plugin.getServer().getConsoleSender().sendMessage(colors.translatedString(" |  _ \\ __ _ _ __   __| | ___  _ __ ___ |  _ \\ _____      ____ _ _ __ __| |/ ___(_)_   _____ _ __ "));
        plugin.getServer().getConsoleSender().sendMessage(colors.translatedString(" | |_) / _` | '_ \\ / _` |/ _ \\| '_ ` _ \\| |_) / _ \\ \\ /\\ / / _` | '__/ _` | |  _| \\ \\ / / _ \\ '__|"));
        plugin.getServer().getConsoleSender().sendMessage(colors.translatedString(" |  _ < (_| | | | | (_| | (_) | | | | | |  _ <  __/\\ V  V / (_| | | | (_| | |_| | |\\ V /  __/ |   "));
        plugin.getServer().getConsoleSender().sendMessage(colors.translatedString(" |_| \\_\\__,_|_| |_|\\__,_|\\___/|_| |_| |_|_| \\_\\___| \\_/\\_/ \\__,_|_|  \\__,_|\\____|_| \\_/ \\___|_|   "));
        plugin.getServer().getConsoleSender().sendMessage(colors.translatedString("                                                                                                  "));
    }
}
