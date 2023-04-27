package fr.lordfinn.finnoutools.gui;

import fr.lordfinn.finnoutools.FinnouTools;
import fr.lordfinn.finnoutools.customitems.CustomItemsManager;
import org.bukkit.entity.Player;

public class GoBackGUIAction implements CustomItemsGUIItem.Action {
    private final FinnouTools plugin;
    private final CustomItemsManager itemManager;

    GoBackGUIAction(FinnouTools plugin, CustomItemsManager itemManager) {
        this.plugin = plugin;
        this.itemManager = itemManager;
    }

    @Override
    public void run(Player player) {
        player.closeInventory();
        CustomItemsGUI customItemsGUI = new CustomItemsGUI(plugin, itemManager);
        customItemsGUI.openGUI(player, 1, false); // Open CustomItemsGUI at the first page
    }
}