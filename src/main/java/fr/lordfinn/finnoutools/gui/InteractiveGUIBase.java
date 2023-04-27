package fr.lordfinn.finnoutools.gui;

import fr.lordfinn.finnoutools.FinnouTools;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

import static fr.lordfinn.finnoutools.utils.HeadUtil.arePlayerHeadsSimilar;

public class InteractiveGUIBase implements Listener {

    private final Plugin plugin;
    private final int size;
    private Component title;
    private final HashMap<Integer, CustomItemsGUIItem> items;
    private Inventory modelInventory = null;

    private Player player;


    public InteractiveGUIBase(Plugin plugin, int size, Component title) {
        this.plugin = plugin;
        this.size = size;
        this.title = title;
        this.items = new HashMap<Integer, CustomItemsGUIItem>();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

    }

    public void addItem(CustomItemsGUIItem item, int slot) {

        items.put(slot, item);
        if (this.modelInventory != null)
            this.modelInventory.setItem(slot, item.getItemStack());
    }
    public void addItem(CustomItemsGUIItem item) {
        int slot = 0;
        while (this.items.containsKey(slot)) {
            slot++;
        }
        this.items.put(slot, item);
    }

    public Inventory open(Player player) {
        this.modelInventory = Bukkit.createInventory(player, this.size, this.title);

        for (Map.Entry<Integer, CustomItemsGUIItem> entry : this.items.entrySet()) {
            int slot = entry.getKey();
            CustomItemsGUIItem item = entry.getValue();
            this.modelInventory.setItem(slot, item.getItemStack());
        }

        player.openInventory(this.modelInventory);
        return this.modelInventory;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player))
            return;
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null || !clickedInventory.equals(this.modelInventory) || !clickedInventory.getViewers().contains(player)) {
            return;
        }
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR)
            return;
        event.setCancelled(true);
        for (CustomItemsGUIItem item : items.values()) {
            if (item.getItemStack() != null && areItemsSimilar(item.getItemStack(), clickedItem) && item.getMainAction() != null) {
                CustomItemsGUIItem.Action action = event.isLeftClick() ? item.getleftAction() : item.getRightAction();
                action.run((Player) event.getWhoClicked());
                return;
            }
        }
    }

    public boolean areItemsSimilar(ItemStack item1, ItemStack item2) {
        if (item1 == null || item2 == null) {
            return false;
        }

        if (item1.getType() != item2.getType()) {
            return false;
        }

        if (!item1.hasItemMeta() || !item2.hasItemMeta()) {
            return false;
        }

        if (item1.getType() == Material.PLAYER_HEAD) {
            return arePlayerHeadsSimilar(item1, item2);
        } else {
            return item1.isSimilar(item2);
        }
    }

    public void addItem(ItemStack nextPageItem, int slot) {
        addItem(new CustomItemsGUIItem(nextPageItem, null), slot);
    }

    public boolean isViewed() {
        return this.modelInventory.getViewers().isEmpty();
    }
}
