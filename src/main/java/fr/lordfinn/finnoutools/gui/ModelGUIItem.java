package fr.lordfinn.finnoutools.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ModelGUIItem {

    private final ItemStack itemStack;
    private final int slot;
    private final Action action;

    public ModelGUIItem(Material material, String displayName, List<String> lore, int slot, Action action) {
        this.itemStack = createItemStack(material, displayName, lore);
        this.slot = slot;
        this.action = action;
    }

    private ItemStack createItemStack(Material material, String displayName, List<String> lore) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta meta = itemStack.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
        }

        return itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public int getSlot() {
        return slot;
    }

    public Action getAction() {
        return action;
    }

    public interface Action {
        void run(Player player);
    }
}