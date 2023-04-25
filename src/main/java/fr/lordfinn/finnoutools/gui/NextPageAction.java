package fr.lordfinn.finnoutools.gui;

import org.bukkit.entity.Player;

public class NextPageAction implements ModelGUIItem.Action {
    private final ModelGUI modelGUI;
    private final int currentPage;

    public NextPageAction(ModelGUI modelGUI, int currentPage) {
        this.modelGUI = modelGUI;
        this.currentPage = currentPage;
    }

    @Override
    public void run(Player player) {
        modelGUI.openGUI(player, currentPage + 1, false);
    }
}