package fr.lordfinn.finnoutools.gui;

import fr.lordfinn.finnoutools.FinnouTools;
import fr.lordfinn.finnoutools.models.CustomItem;
import fr.lordfinn.finnoutools.models.CustomItemManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentBuilder;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ModelGUI {
    private FinnouTools plugin;
    private CustomItemManager customItemManager;
    public ModelGUI(FinnouTools plugin, CustomItemManager customItemManager) {
        this.plugin = plugin;
    }


    public void openGUI(Player player, int page, boolean displayMode) {
        List<CustomItem> customItems = this.customItemManager.getCustomItems();
        int itemsPerPage = 45;
        int totalPages = (int) Math.ceil(customItems.size() / (double) itemsPerPage);

        if (page > totalPages) {
            page = totalPages;
        }

        CustomGUI gui = new CustomGUI(plugin, 54,"Custom Items - Page " + page);
        int startIndex = (page - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, customItems.size());

        for (int i = startIndex; i < endIndex; i++) {
            CustomItem customItem = customItems.get(i);
            ItemStack itemStack = customItem.toItemStack();
            ItemMeta itemMeta = itemStack.getItemMeta();
            if (itemMeta != null) {
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.YELLOW + "Custom Model Data: " + customItem.getCustomModelData());
                lore.add(ChatColor.YELLOW + "Type: " + customItem.getType());
                lore.add(ChatColor.YELLOW + "Project: " + customItem.getProject());
                itemMeta.setLore(lore);
                itemStack.setItemMeta(itemMeta);
            }
            inventory.addItem(itemStack);
        }

        // Ajouter les éléments spéciaux
        ItemStack nextPageItem = ... // Créez l'item pour aller à la page suivante
        ItemStack prevPageItem = ... // Créez l'item pour aller à la page précédente
        ItemStack changeDisplayModeItem = ... // Créez l'item pour changer le mode d'affichage

        inventory.setItem(52, nextPageItem);
        inventory.setItem(45, prevPageItem);
        inventory.setItem(53, changeDisplayModeItem);

        player.openInventory(inventory);
    }
}
