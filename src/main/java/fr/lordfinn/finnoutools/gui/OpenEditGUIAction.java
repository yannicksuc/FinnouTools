package fr.lordfinn.finnoutools.gui;

import fr.lordfinn.finnoutools.customitems.CustomItem;
import fr.lordfinn.finnoutools.gui.CustomItemsEditGUI;
import fr.lordfinn.finnoutools.gui.CustomItemsGUIItem;
import org.bukkit.entity.Player;

public class OpenEditGUIAction implements CustomItemsGUIItem.Action {
    private CustomItemsEditGUI customItemsEditGUI;
    private CustomItem customItem;

    public OpenEditGUIAction(CustomItemsEditGUI customItemsEditGUI, CustomItem customItem) {
        this.customItemsEditGUI = customItemsEditGUI;
        this.customItem = customItem;
    }

    @Override
    public void run(Player player) {
        customItemsEditGUI.openGUI(player, customItem);
    }
}