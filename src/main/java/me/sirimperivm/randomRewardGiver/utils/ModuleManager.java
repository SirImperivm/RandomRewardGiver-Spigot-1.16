package me.sirimperivm.randomRewardGiver.utils;

import me.sirimperivm.randomRewardGiver.Main;
import me.sirimperivm.randomRewardGiver.entities.RewardsPack;
import me.sirimperivm.randomRewardGiver.utils.colors.Colors;
import me.sirimperivm.randomRewardGiver.utils.others.Logger;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class ModuleManager {

    private Main plugin;
    private Colors colors;
    private Logger log;
    private ConfigManager configManager;

    private List<RewardsPack> rewardsPacks;

    public ModuleManager(Main plugin) {
        this.plugin = plugin;
        colors = plugin.getColors();
        log = plugin.getLog();
        configManager = plugin.getConfigManager();

        resetRewardsPacks();
    }

    public void resetRewardsPacks() {
        rewardsPacks = new ArrayList<>();
        for (String rewardPackSection : configManager.getRewards().getConfigurationSection("packs").getKeys(false)) {
            String path = "packs." + rewardPackSection;
            RewardsPack rewardsPack = new RewardsPack(configManager, rewardPackSection);
            rewardsPack.resetTitle();
            rewardsPack.resetSize();
            rewardsPack.resetRewards();
            rewardsPacks.add(rewardsPack);
            log.success("Registrato rewardPack: " + rewardPackSection + "!");
        }
    }

    public void getHelp(CommandSender s, String helpTarget, String pageTarget) {
        if (!containsOnlyNumbers(pageTarget)) {
            s.sendMessage(configManager.getTranslatedString(configManager.getMessages(), "invalid-args.number-required"));
            return;
        }
        int page = Integer.parseInt(pageTarget);
        createHelp(s, helpTarget, page);
    }

    public void createHelp(CommandSender s, String helpTarget, int page) {
        int visualizedPage = page;
        page--;

        List<String> totalLines = configManager.getMessages().getStringList("helps-creator." + helpTarget + ".lines");
        int commandsPerPage = configManager.getMessages().getInt("helps-creator." + helpTarget + ".max-lines-per-page", 5);
        int startIndex = page*commandsPerPage;
        int totalCommands = totalLines.size();
        int endIndex = Math.min((page+1) * commandsPerPage, totalCommands);

        if (visualizedPage <= 0 || visualizedPage > (int) Math.floor((double) totalCommands/commandsPerPage)+1) {
            s.sendMessage(configManager.getTranslatedString(configManager.getMessages(),"page-not-found")
                    .replace("%page%", String.valueOf(visualizedPage))
            );
            return;
        }

        s.sendMessage(configManager.getTranslatedString(configManager.getMessages(), "helps-creator." + helpTarget + ".header"));
        s.sendMessage(configManager.getTranslatedString(configManager.getMessages(), "helps-creator." + helpTarget + ".title"));
        s.sendMessage(configManager.getTranslatedString(configManager.getMessages(), "helps-creator." + helpTarget + ".spacer"));

        for (int i=startIndex; i<endIndex; i++) {
            String line = totalLines.get(i);
            if (line != null) {
                String[] parts = line.split("-");
                if (parts.length == 2) {
                    String commandName = parts[0].trim();
                    String commandDescription = parts[1].trim();
                    s.sendMessage(configManager.getTranslatedString(configManager.getMessages(),"helps-creator." + helpTarget + ".line-format")
                            .replace("%command-name%", colors.translatedString(commandName))
                            .replace("%command-description%", colors.translatedString(commandDescription))
                    );
                }
            }
        }

        s.sendMessage(configManager.getTranslatedString(configManager.getMessages(), "helps-creator." + helpTarget + ".spacer"));
        s.sendMessage(configManager.getTranslatedString(configManager.getMessages(), "helps-creator." + helpTarget + ".page-format")
                .replace("{currentpage}", String.valueOf(visualizedPage))
        );
        s.sendMessage(configManager.getTranslatedString(configManager.getMessages(), "helps-creator." + helpTarget + ".footer"));
    }

    public boolean containsOnlyNumbers(String target) {
        try {
            Integer.parseInt(target);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public List<RewardsPack> getRewardsPacks() {
        return rewardsPacks;
    }
}
