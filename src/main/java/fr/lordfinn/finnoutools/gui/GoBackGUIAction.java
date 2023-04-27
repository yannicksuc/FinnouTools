package fr.lordfinn.finnoutools.gui;

import fr.lordfinn.finnoutools.customitems.CustomItemsManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class GoBackGUIAction implements CustomItemsGUIItem.Action {
    private final Plugin plugin;
    private final CustomItemsManager itemManager;

    GoBackGUIAction(Plugin plugin, CustomItemsManager itemManager) {
        this.plugin = plugin;
        this.itemManager = itemManager;
    }

    @Override
    public void run(Player player) {
        player.closeInventory();
        CustomItemsGUI customItemsGUI = new CustomItemsGUI(plugin, itemManager);
        customItemsGUI.openGUI(player, 1); // Open CustomItemsGUI at the first page
    }
}