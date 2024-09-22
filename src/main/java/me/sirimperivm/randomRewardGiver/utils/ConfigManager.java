package me.sirimperivm.randomRewardGiver.utils;

import me.sirimperivm.randomRewardGiver.Main;
import me.sirimperivm.randomRewardGiver.utils.colors.Colors;
import me.sirimperivm.randomRewardGiver.utils.others.Logger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;

@SuppressWarnings("all")
public class ConfigManager {

    private Main plugin;
    private Colors colors;
    private Logger log;

    private File folder;
    private File settingsFile, messagesFile, rewardsFile;
    private FileConfiguration settings, messages, rewards;

    public ConfigManager(Main plugin) {
        this.plugin = plugin;
        colors = plugin.getColors();
        log = plugin.getLog();

        folder = plugin.getDataFolder();
        settingsFile = new File(folder, "settings.yml");
        settings = new YamlConfiguration();
        messagesFile = new File(folder, "messages.yml");
        messages = new YamlConfiguration();
        rewardsFile = new File(folder, "rewards.yml");
        rewards = new YamlConfiguration();

        if (!folder.exists()) folder.mkdir();

        if (!settingsFile.exists()) create(settingsFile);
        if (!messagesFile.exists()) create(messagesFile);
        if (!rewardsFile.exists()) create(rewardsFile);

        loadAll();
    }

    private void create(File f) {
        String n = f.getName();
        try {
            Files.copy(plugin.getResource(n), f.toPath(), new CopyOption[0]);
        } catch (IOException e) {
            log.fail("Impossibile creare il file " + n + "!");
            e.printStackTrace();
        }
    }

    public void save(FileConfiguration c, File f) {
        String n = f.getName();
        try {
            c.save(f);
        } catch (IOException e) {
            log.fail("Impossibile salvare il file " + n + "!");
            e.printStackTrace();
        }
    }

    public void load(FileConfiguration c, File f) {
        String n = f.getName();
        try {
            c.load(f);
        } catch (IOException | InvalidConfigurationException e) {
            log.fail("Impossibile caricare il file " + n + "!");
            e.printStackTrace();
        }
    }

    public void saveAll() {
        save(settings, settingsFile);
        save(messages, messagesFile);
        save(rewards, rewardsFile);
    }

    public void loadAll() {
        load(settings, settingsFile);
        load(messages, messagesFile);
        load(rewards, rewardsFile);
    }

    public File getSettingsFile() {
        return settingsFile;
    }

    public FileConfiguration getSettings() {
        return settings;
    }

    public File getMessagesFile() {
        return messagesFile;
    }

    public FileConfiguration getMessages() {
        return messages;
    }

    public File getRewardsFile() {
        return rewardsFile;
    }

    public FileConfiguration getRewards() {
        return rewards;
    }

    public String getTranslatedString(FileConfiguration c, String p) {
        String prefix = colors.translatedString(getMessages().getString("default-prefix", "&5&lFusion&6&lRewards &8» &7"));
        return colors.translatedString(c.getString(p).replace("%p", prefix));
    }

    public String getTranslatedString(FileConfiguration c, String p, String def) {
        String prefix = colors.translatedString(getMessages().getString("default-prefix", "&5&lFusion&6&lRewards &8» &7"));
        return colors.translatedString(c.getString(p, def).replace("%p", prefix));
    }
}
