package fr.lordfinn.finnoutools.gui;

import fr.lordfinn.finnoutools.customitems.CustomItem;
import fr.lordfinn.finnoutools.customitems.CustomItemsManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class DeleteAction implements CustomItemsGUIItem.Action {
    private final CustomItem customItem;
    private final Plugin plugin;
    private final CustomItemsEditGUI customItemsEditGUI;
    private final CustomItemsManager itemManager;

    public DeleteAction(CustomItem customItem, Plugin plugin, CustomItemsEditGUI customItemsEditGUI, CustomItemsManager itemManager) {
        this.customItem = customItem;
        this.plugin = plugin;
        this.customItemsEditGUI = customItemsEditGUI;
        this.itemManager = itemManager;
    }

    @Override
    public void run(Player player) {
        // Open a new confirmation inventory
        InteractiveGUIBase confirmationGUI = new InteractiveGUIBase(plugin, 27, Component.text("Confirm Deletion", NamedTextColor.RED));

        // Add confirm and cancel buttons
        confirmationGUI.addItem(createConfirmItem(), 11);
        confirmationGUI.addItem(createCancelItem(), 15);

        // Close the current inventory and open the confirmation inventory
        player.closeInventory();
        confirmationGUI.open(player);
    }

    private CustomItemsGUIItem createConfirmItem() {
        ItemStack itemStack = new ItemStack(Material.GREEN_CONCRETE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.text("Confirm", NamedTextColor.GREEN));
        itemStack.setItemMeta(itemMeta);
        return new CustomItemsGUIItem(itemStack, player -> {
            itemManager.deleteCustomItem(customItem);
            player.sendMessage(Component.text("Successfully deleted custom item.", NamedTextColor.GREEN));
            player.closeInventory();
            CustomItemsGUI customItemsGUI = new CustomItemsGUI(plugin, itemManager);
            customItemsGUI.openGUI(player, 0);
        });
    }

    private CustomItemsGUIItem createCancelItem() {
        ItemStack itemStack = new ItemStack(Material.RED_CONCRETE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.text("Cancel", NamedTextColor.RED));
        itemStack.setItemMeta(itemMeta);
        return new CustomItemsGUIItem(itemStack, player -> {
            player.closeInventory();
            customItemsEditGUI.openGUI(player, customItem);
        });
    }
}