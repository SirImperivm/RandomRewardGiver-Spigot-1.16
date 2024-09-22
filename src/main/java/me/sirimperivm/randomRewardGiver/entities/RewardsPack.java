package me.sirimperivm.randomRewardGiver.entities;

import me.sirimperivm.randomRewardGiver.utils.ConfigManager;
import me.sirimperivm.randomRewardGiver.utils.colors.Colors;
import me.sirimperivm.randomRewardGiver.utils.others.Logger;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SuppressWarnings("all")
public class RewardsPack {

    private ConfigManager configManager;

    private String packIdentifier;
    private String packTitle;
    private int size;
    private List<Reward> rewards;

    private String path;

    public RewardsPack(ConfigManager configManager, String packIdentifier) {
        this.configManager = configManager;
        this.packIdentifier = packIdentifier;
        path = "packs." + packIdentifier;
        resetTitle();
        resetSize();
        resetRewards();
    }

    public void resetTitle() {
        packTitle = configManager.getTranslatedString(configManager.getRewards(), path + ".title", "&cNot Set Correctly.");
    }

    public void resetSize() {
        size = configManager.getRewards().getInt(path + ".size", -1);
    }

    public void resetRewards() {
        rewards = new ArrayList<>();
        for (String rewardSection : configManager.getRewards().getConfigurationSection(path + ".rewards").getKeys(false)) {
            String rewardPath = path + ".rewards." + rewardSection;
            String rewardTitle = configManager.getTranslatedString(configManager.getRewards(), rewardPath + ".title");
            List<String> rewardCommands = configManager.getRewards().getStringList(rewardPath + ".commands");
            List<ItemStack> rewardItems = new ArrayList<>();
            for (String itemSection : configManager.getRewards().getConfigurationSection(rewardPath + ".items").getKeys(false)) {
                String itemPath = rewardPath + ".items." + itemSection;
                String materialName = configManager.getRewards().getString(itemPath + ".material");
                Material material = Material.getMaterial(materialName);
                int amount = configManager.getRewards().getInt(itemPath + ".amount");
                ItemStack is = new ItemStack(material, amount);
                ItemMeta meta = is.getItemMeta();
                String displayName = configManager.getRewards().getString(itemPath + ".display-name");
                if (!displayName.equals("null")) {
                    meta.setDisplayName(Colors.translateString(displayName));
                }
                int customModelData = configManager.getRewards().getInt(itemPath + ".model", -1);
                if (customModelData > 0) {
                    meta.setCustomModelData(customModelData);
                }
                meta.setLore(configManager.getRewards().getStringList(itemPath + ".lore"));
                is.setItemMeta(meta);
                rewardItems.add(is);
            }
            Reward reward = new Reward(rewardSection, rewardTitle, rewardItems, rewardCommands);
            rewards.add(reward);
        }
    }

    public void giveRandomReward(Player p) {
        for (int i=0; i<size; i++) {
            if (rewards.size() < 1) {
                Logger.staticFail("Nel pack " + packIdentifier + " non ci sono premi validi.");
            } else {
                String inventoryFullMessage = configManager.getTranslatedString(configManager.getMessages(), "inventory-full");
                Reward lastReward = null;
                Random rand = new Random();
                Reward reward = rewards.get(rand.nextInt(rewards.size()));
                do {
                    reward = rewards.get(rand.nextInt(rewards.size()));
                } while (lastReward.equals(reward));

                reward.giveToPlayer(p, inventoryFullMessage);
            }
        }
    }

    public List<Reward> getRewards() {
        return rewards;
    }
}
