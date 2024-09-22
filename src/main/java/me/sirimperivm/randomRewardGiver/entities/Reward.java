package me.sirimperivm.randomRewardGiver.entities;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;

@SuppressWarnings("all")
public class Reward {

    private String rewardIdentifier;
    private String title;
    private List<ItemStack> items;
    private List<String> commands;

    public Reward(String rewardIdentifier, String title, List<ItemStack> items, List<String> commands) {
        this.rewardIdentifier = rewardIdentifier;
        this.title = title;
        this.items = items;
        this.commands = commands;
    }

    public String getRewardIdentifier() {
        return rewardIdentifier;
    }

    public void setRewardIdentifier(String rewardIdentifier) {
        this.rewardIdentifier = rewardIdentifier;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    public void giveToPlayer(Player p, String inventoryFullMessage) {
        String playerName = p.getName();

        for (String command : commands) {
            command = command.replace("%player%", playerName);
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
        }

        boolean inventoryFull = false;

        for (ItemStack is : items) {
            if (hasntSpace(p, is)) {
                inventoryFull = true;
                p.getWorld().dropItem(p.getLocation(), is);
            } else {
                p.getInventory().addItem(is);
            }
        }

        if (inventoryFull) {
            p.sendMessage(inventoryFullMessage);
        }
    }


    private boolean hasntSpace(Player p, ItemStack is) {
        Inventory inv = p.getInventory();

        if (inv.firstEmpty() != -1) {
            return false;
        }

        for (ItemStack item : inv.getContents()) {
            if (item != null && item.isSimilar(is)) {
                int maxStackSize = is.getMaxStackSize();
                if (item.getAmount() < maxStackSize) {
                    return false;
                }
            }
        }

        return true;
    }
}
