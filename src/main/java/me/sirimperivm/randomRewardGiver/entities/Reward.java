package me.sirimperivm.randomRewardGiver.entities;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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

        for (ItemStack is : items) {
            if (hasntSpace(p, is)) {
                p.sendMessage(inventoryFullMessage);
                p.getWorld().dropItem(p.getLocation(), is);
            } else {
                p.getInventory().addItem(is);
            }
        }
    }

    private boolean hasntSpace(Player p, ItemStack is) {
        Inventory inv = p.getInventory();
        int size = inv.getSize();
        for (int i=0; i<size; i++) {
            if (inv.getItem(i) == null || inv.getItem(i).getType() == Material.AIR || (inv.getItem(i).equals(is) && inv.getItem(i).getAmount() != inv.getItem(i).getMaxStackSize() && ((inv.getItem(i).getAmount() + is.getAmount()) > inv.getItem(i).getMaxStackSize())))
                return false;
        }
        return true;
    }
}
