package me.sirimperivm.randomRewardGiver.commands;

import me.sirimperivm.randomRewardGiver.Main;
import me.sirimperivm.randomRewardGiver.utils.ConfigManager;
import me.sirimperivm.randomRewardGiver.utils.ModuleManager;
import me.sirimperivm.randomRewardGiver.utils.colors.Colors;
import me.sirimperivm.randomRewardGiver.utils.others.Errors;
import me.sirimperivm.randomRewardGiver.utils.others.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class AdminCommand implements CommandExecutor, TabExecutor {

    private Main plugin;
    private Colors colors;
    private Logger log;
    private ConfigManager configManager;
    private Errors errors;
    private ModuleManager modules;

    public AdminCommand(Main plugin) {
        this.plugin = plugin;
        colors = plugin.getColors();
        log = plugin.getLog();
        configManager = plugin.getConfigManager();
        errors = plugin.getErrors();
        modules = plugin.getModules();
    }

    private void getUsage(CommandSender s, String pageTarget) {
        modules.getHelp(s, "admin-command", pageTarget);
    }

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (errors.noPermCommand(s, configManager.getSettings().getString("permissions.commands.admin-command.main"))) {
            return false;
        } else {
            if (a.length == 0) {
                getUsage(s, "1");
            } else if (a.length == 1) {
                if (a[0].equalsIgnoreCase("reload")) {
                    if (errors.noPermCommand(s, configManager.getSettings().getString("permissions.commands.admin-command.reload"))) {
                        return false;
                    } else {
                        configManager.loadAll();
                        modules.resetRewardsPacks();
                        s.sendMessage(configManager.getTranslatedString(configManager.getMessages(), "plugin-reloaded"));
                    }
                } else {
                    getUsage(s, "1");
                }
            } else if (a.length == 2) {
                if (a[0].equalsIgnoreCase("help")) {
                    String page = a[1];
                    getUsage(s, page);
                } else {
                    getUsage(s, "1");
                }
            } else {
                getUsage(s, "1");
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) {
        if (s.hasPermission(configManager.getSettings().getString("permissions.commands.admin-command.main"))) {
            if (a.length == 1) {
                List<String> toReturn = new ArrayList<>();
                toReturn.add("help");
                if (s.hasPermission(configManager.getSettings().getString("permissions.commands.admin-command.reload"))) {
                    toReturn.add("reload");
                }
                return toReturn;
            } else if (a.length == 2) {
                List<String> toReturn = new ArrayList<>();
                if (a[0].equalsIgnoreCase("help")) {
                    for (int i=0; i<1000; i++) {
                        toReturn.add("" + i);
                    }
                }
                return toReturn;
            }
        }
        return new ArrayList<>();
    }
}
