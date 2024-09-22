package me.sirimperivm.randomRewardGiver.entities;

import me.sirimperivm.randomRewardGiver.utils.ConfigManager;
import me.sirimperivm.randomRewardGiver.utils.colors.Colors;
import me.sirimperivm.randomRewardGiver.utils.others.Logger;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
        size = configManager.getRewards().getInt(path + ".rewards-size", -1);
    }

    public void resetRewards() {
        rewards = new ArrayList<>();
        for (String rewardSection : configManager.getRewards().getConfigurationSection(path + ".rewards").getKeys(false)) {
            String rewardPath = path + ".rewards." + rewardSection;
            String rewardTitle = configManager.getTranslatedString(configManager.getRewards(), rewardPath + ".title");
            List<String> rewardCommands = configManager.getRewards().getStringList(rewardPath + ".commands");
            List<String> rewardMessages = configManager.getRewards().getStringList(rewardPath + ".messages");
            List<ItemStack> rewardItems = new ArrayList<>();
            for (String itemSection : configManager.getRewards().getConfigurationSection(rewardPath + ".items").getKeys(false)) {
                String itemPath = rewardPath + ".items." + itemSection;
                String type = configManager.getRewards().getString(itemPath + ".type", "null");
                if (!type.equalsIgnoreCase("normal") && !type.equalsIgnoreCase("potion")){
                    Logger.staticFail("Errore nel creare un premio fisico nel pacchetto: " + packIdentifier + ", tipo oggetto errato, si deve scegliere tra \"normal\" e \"potion\"");
                    return;
                }
                if (type.equalsIgnoreCase("normal")) {
                    itemPath = rewardPath + ".items." + itemSection + ".normal";
                    String materialName = configManager.getRewards().getString(itemPath + ".material");
                    Material material = Material.getMaterial(materialName);
                    int amount = configManager.getRewards().getInt(itemPath + ".amount", 1);
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
                    for (String enchTag : configManager.getRewards().getConfigurationSection(itemPath + ".enchantments").getKeys(false)) {
                        String enchPath = itemPath + ".enchantments." + enchTag;
                        String enchantName = configManager.getRewards().getString(enchPath + ".name");
                        int enchantLevel = configManager.getRewards().getInt(enchPath + ".level");
                        enchantLevel = enchantLevel > 0 ? enchantLevel-1 : 0;
                        Enchantment enchantment = Registry.ENCHANTMENT.get(NamespacedKey.minecraft(enchantName.toLowerCase()));
                        if (enchantment != null) {
                            meta.addEnchant(enchantment, enchantLevel, true);
                        } else {
                            Logger.staticFail("Invalido un rewards nel pack " + packIdentifier + ", l'incantesimo " + enchantName + " non esiste. ID: " + enchTag + "!");
                        }
                    }
                    for (String itemFlag : configManager.getRewards().getStringList(itemPath + ".flags")) {
                        meta.addItemFlags(ItemFlag.valueOf(itemFlag));
                    }
                    is.setItemMeta(meta);
                    rewardItems.add(is);
                } else {
                    itemPath = rewardPath + ".items." + itemSection + ".potion";
                    String potionType = configManager.getRewards().getString(itemPath + ".type", "normal");
                    String materialName = null;
                    switch (potionType) {
                        case "splash":
                            materialName = "SPLASH_POTION";
                            break;
                        case "lingering":
                            materialName = "LINGERING_POTION";
                            break;
                        default:
                            materialName = "POTION";
                            break;
                    }
                    Material material = Material.getMaterial(materialName);
                    int amount = configManager.getRewards().getInt(itemPath + ".amount", 1);
                    ItemStack is = new ItemStack(material, amount);
                    ItemMeta meta = is.getItemMeta();
                    PotionMeta potionMeta = (PotionMeta) meta;
                    for (String effectTag : configManager.getRewards().getConfigurationSection(itemPath + ".potion-effects").getKeys(false)) {
                        String effectPath = itemPath + ".potion-effects." + effectTag;
                        String effectName = configManager.getRewards().getString(effectPath + ".id");
                        int effectLevel = configManager.getRewards().getInt(effectPath + ".level", 1);
                        effectLevel = effectLevel > 0 ? effectLevel-1 : 0;
                        int duration = configManager.getRewards().getInt(effectPath + ".duration", 5);
                        PotionEffectType effectType = Registry.EFFECT.get(NamespacedKey.minecraft(effectName.toLowerCase()));
                        if (effectType != null) {
                            PotionEffect potionEffect = new PotionEffect(effectType, 20*duration, effectLevel);
                            potionMeta.addCustomEffect(potionEffect, true);
                        } else {
                            Logger.staticFail("Invalido un rewards nel pack " + packIdentifier + ", l'effetto " + effectName + " non esiste. ID: " + effectTag + "!");
                        }
                    }
                    int colorRed = configManager.getRewards().getInt(itemPath + ".color.red", 255);
                    int colorGreen = configManager.getRewards().getInt(itemPath + ".color.green", 255);
                    int colorBlue = configManager.getRewards().getInt(itemPath + ".color.blue", 255);
                    Color color = Color.fromRGB(colorRed, colorGreen, colorBlue);
                    potionMeta.setColor(color);
                    meta = potionMeta;
                    String displayName = configManager.getRewards().getString(itemPath + ".display-name", "null");
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
            }
            Reward reward = new Reward(rewardSection, rewardTitle, rewardItems, rewardCommands, rewardMessages);
            rewards.add(reward);
        }
    }

    public void giveRandomReward(Player p) {
        Reward lastReward = null;
        for (int i=0; i<size; i++) {
            if (rewards.size() < 1) {
                Logger.staticFail("Nel pack " + packIdentifier + " non ci sono premi validi.");
            } else {
                String inventoryFullMessage = configManager.getTranslatedString(configManager.getMessages(), "inventory-full");
                if (rewards.size() == 1) {
                    Reward reward = rewards.get(0);
                    reward.giveToPlayer(p, inventoryFullMessage);
                } else {
                    Random rand = new Random();
                    Reward reward = rewards.get(rand.nextInt(rewards.size()));
                    do {
                        reward = rewards.get(rand.nextInt(rewards.size()));
                    } while (lastReward == reward);
                    lastReward = reward;

                    reward.giveToPlayer(p, inventoryFullMessage);
                }
            }
        }
    }

    public List<Reward> getRewards() {
        return rewards;
    }

    public String getPackIdentifier() {
        return packIdentifier;
    }

    public String getPackTitle() {
        return packTitle;
    }
}
