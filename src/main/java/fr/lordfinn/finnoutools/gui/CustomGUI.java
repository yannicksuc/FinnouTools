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
import java.util.List;

public class CustomGUI implements Listener {

    private final FinnouTools plugin;
    private final int size;
    private String title;
    private final List<ModelGUIItem> items;
    private Inventory modelInventory;

    private Player player;


    public CustomGUI(FinnouTools plugin, int size, String title) {
        this.plugin = plugin;
        this.size = size;
        this.title = title;
        this.items = new ArrayList<>();
        plugin.getServer().getPluginManager().registerEvents(this, plugin);

    }

    public void addItem(ModelGUIItem item) {
        items.add(item);
    }

    public void open(Player player) {
        Component titleComponent = Component.text(this.title, NamedTextColor.DARK_PURPLE);
        modelInventory = Bukkit.createInventory(player, this.size, titleComponent);

        for (ModelGUIItem item : items) {
            modelInventory.setItem(item.getSlot(), item.getItemStack());
        }

        player.openInventory(modelInventory);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player))
            return;
        Player player = (Player) event.getWhoClicked();
        if (!event.getInventory().getViewers().contains(player))
            return;
        event.setCancelled(true);
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR)
            return;
        for (ModelGUIItem item : items) {
            if (item.getItemStack().equals(clickedItem)) {
                item.getAction().run((Player) event.getWhoClicked());
                return;
            }
        }
    }
}
