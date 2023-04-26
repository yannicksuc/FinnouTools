package fr.lordfinn.finnoutools.gui;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class CustomItemsGUIItem {

    private final ItemStack itemStack;
    private final Action rightAction;
    private final Action leftAction;

    public CustomItemsGUIItem(ItemStack itemStack, Action leftAction, Action rightAction) {
        this.itemStack = itemStack;
        this.leftAction = leftAction;
        this.rightAction = rightAction;
    }
    public CustomItemsGUIItem(Material material, String displayName, List<String> lore, Action leftAction, Action rightAction) {
        this(createItemStack(material, displayName, lore), leftAction, rightAction);
    }
    public CustomItemsGUIItem(Material material, String displayName, List<String> lore, Action action) {
        this(material, displayName, lore, action, action);
    }
    public CustomItemsGUIItem(ItemStack itemStack, Action action) {
        this(itemStack, action, action);
    }

    private static ItemStack createItemStack(Material material, String displayName, List<String> lore) {
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

    public Action getMainAction() {
        return this.getleftAction();
    }

    public Action getleftAction() {
        return leftAction;
    }

    public Action getRightAction() {
        return rightAction;
    }

    public interface Action {
        void run(Player player);
    }
}