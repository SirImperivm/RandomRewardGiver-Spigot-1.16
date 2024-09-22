package me.sirimperivm.randomRewardGiver.commands;

import me.sirimperivm.randomRewardGiver.Main;
import me.sirimperivm.randomRewardGiver.entities.RewardsPack;
import me.sirimperivm.randomRewardGiver.utils.ConfigManager;
import me.sirimperivm.randomRewardGiver.utils.ModuleManager;
import me.sirimperivm.randomRewardGiver.utils.colors.Colors;
import me.sirimperivm.randomRewardGiver.utils.others.Errors;
import me.sirimperivm.randomRewardGiver.utils.others.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class RewardCommand implements CommandExecutor, TabCompleter {

    private Main plugin;
    private Colors colors;
    private Logger log;
    private ConfigManager configManager;
    private Errors errors;
    private ModuleManager modules;

    public RewardCommand(Main plugin) {
        this.plugin = plugin;
        colors = plugin.getColors();
        log = plugin.getLog();
        configManager = plugin.getConfigManager();
        errors = plugin.getErrors();
        modules = plugin.getModules();
    }

    private void getUsage(CommandSender s, String pageTarget) {
        modules.getHelp(s, "main-command", pageTarget);
    }

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {
        if (errors.noPermCommand(s, configManager.getSettings().getString("permissions.commands.main-command.main"))) {
            return false;
        } else {
            if (a.length == 0) {
                getUsage(s, "1");
            } else if (a.length == 1) {
                if (a[0].equalsIgnoreCase("list")) {
                    if (errors.noPermCommand(s, configManager.getSettings().getString("permissions.commands.main-command.list"))) {
                        return false;
                    } else {
                        List<RewardsPack> rewardsPacks = modules.getRewardsPacks();
                        if (rewardsPacks.size() < 1) {
                            s.sendMessage(configManager.getTranslatedString(configManager.getMessages(), "list.none"));
                        } else {
                            for (RewardsPack rewardsPack : rewardsPacks) {
                                s.sendMessage(configManager.getTranslatedString(configManager.getMessages(), "list.format")
                                        .replace("%pack-identifier%", rewardsPack.getPackIdentifier())
                                );
                            }
                        }
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
            } else if (a.length == 3) {
                if (a[0].equalsIgnoreCase("give")) {
                    if (errors.noPermCommand(s, configManager.getSettings().getString("permissions.commands.main-command.give"))) {
                        return false;
                    } else {
                        String playerTarget = a[1];
                        Player t = Bukkit.getPlayerExact(playerTarget);

                        if (t == null || !Bukkit.getOnlinePlayers().contains(t)) {
                            s.sendMessage(configManager.getTranslatedString(configManager.getMessages(), "give.player-offline")
                                    .replace("%player%", playerTarget));
                        } else {
                            String packIdentifier = a[2];
                            List<RewardsPack> rewardsPacks = modules.getRewardsPacks();
                            RewardsPack pack = null;
                            for (RewardsPack rewardsPack : rewardsPacks) {
                                if (rewardsPack.getPackIdentifier().equals(packIdentifier)) {
                                    pack = rewardsPack;
                                    break;
                                }
                            }
                            if (pack == null) {
                                s.sendMessage(configManager.getTranslatedString(configManager.getMessages(), "give.pack-not-found")
                                        .replace("%pack%", packIdentifier)
                                );
                            } else {
                                String identifier = pack.getPackIdentifier();
                                String title = pack.getPackTitle();
                                s.sendMessage(configManager.getTranslatedString(configManager.getMessages(), "give.correctly-given.staffer")
                                        .replace("%pack%", identifier)
                                        .replace("%pack-title%", title)
                                );
                                t.sendMessage(configManager.getTranslatedString(configManager.getMessages(), "give.correctly-given.player")
                                        .replace("%pack%", identifier)
                                        .replace("%pack-title%", title)
                                );
                                pack.giveRandomReward(t);
                            }
                        }
                    }
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
        if (s.hasPermission(configManager.getSettings().getString("permissions.commands.main-command.main"))) {
            if (a.length == 1) {
                List<String> toReturn = new ArrayList<>();
                toReturn.add("help");
                if (s.hasPermission(configManager.getSettings().getString("permissions.commands.main-command.list"))) {
                    toReturn.add("list");
                }
                if (s.hasPermission(configManager.getSettings().getString("permissions.commands.main-command.give"))) {
                    toReturn.add("give");
                }
                return toReturn;
            } else if (a.length == 2) {
                List<String> toReturn = new ArrayList<>();
                if (a[0].equalsIgnoreCase("help")) {
                    for (int i=0; i<1000; i++) {
                        toReturn.add("" + i);
                    }
                }
                if (a[0].equalsIgnoreCase("give") && s.hasPermission(configManager.getSettings().getString("permissions.commands.main-command.give"))) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        toReturn.add(p.getName());
                    }
                }
                return toReturn;
            } else if (a.length == 3) {
                List<String> toReturn = new ArrayList<>();
                if (a[0].equalsIgnoreCase("give") && s.hasPermission(configManager.getSettings().getString("permissions.commands.main-command.give"))) {
                    List<RewardsPack> rewardsPacks = modules.getRewardsPacks();
                    for (RewardsPack rewardsPack : rewardsPacks) {
                        toReturn.add(rewardsPack.getPackIdentifier());
                    }
                }
                return toReturn;
            }
        }
        return new ArrayList<>();
    }
}
