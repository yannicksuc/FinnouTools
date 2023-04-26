package fr.lordfinn.finnoutools.gui;

import org.bukkit.entity.Player;

public class NextPageAction implements CustomItemsGUIItem.Action {
    private final CustomItemsGUI customItemsGUI;
    private final int currentPage;

    public NextPageAction(CustomItemsGUI customItemsGUI, int currentPage) {
        this.customItemsGUI = customItemsGUI;
        this.currentPage = currentPage;
    }

    @Override
    public void run(Player player) {
        customItemsGUI.openGUI(player, currentPage + 1, false);
    }
}