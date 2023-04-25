package fr.lordfinn.finnoutools.gui;

import fr.lordfinn.finnoutools.FinnouTools;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomGUI implements Listener {

    private final FinnouTools plugin;
    private final int size;
    private String title;
    private final HashMap<Integer, ModelGUIItem> items;
    private Inventory modelInventory;

    private Player player;


    public CustomGUI(FinnouTools plugin, int size, String title) {
        this.plugin = plugin;
        this.size = size;
        this.title = title;
        this.items = new HashMap<Integer, ModelGUIItem>();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

    }

    public void addItem(ModelGUIItem item, int slot) {
        items.put(slot, item);
    }
    public void addItem(ModelGUIItem item) {
        int slot = 0;
        while (items.containsKey(slot)) {
            slot++;
        }
        items.put(slot, item);
    }

    public void open(Player player) {
        Component titleComponent = Component.text(this.title, NamedTextColor.DARK_PURPLE);
        modelInventory = Bukkit.createInventory(player, this.size, titleComponent);

        for (Map.Entry<Integer, ModelGUIItem> entry : items.entrySet()) {
            int slot = entry.getKey();
            ModelGUIItem item = entry.getValue();
            modelInventory.setItem(slot, item.getItemStack());
        }

        player.openInventory(modelInventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player))
            return;
        Player player = (Player) event.getWhoClicked();
        Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null || !clickedInventory.equals(modelInventory) || !clickedInventory.getViewers().contains(player)) {
            return;
        }
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR)
            return;
        event.setCancelled(true);
        for (ModelGUIItem item : items.values()) {
            if (item.getItemStack().equals(clickedItem)) {
                item.getAction().run((Player) event.getWhoClicked());
                return;
            }
        }
    }
}
