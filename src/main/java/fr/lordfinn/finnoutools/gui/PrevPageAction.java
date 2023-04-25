package fr.lordfinn.finnoutools.gui;

import org.bukkit.entity.Player;

public class PrevPageAction implements ModelGUIItem.Action {
    private final ModelGUI modelGUI;
    private final int currentPage;

    public PrevPageAction(ModelGUI modelGUI, int currentPage) {
        this.modelGUI = modelGUI;
        this.currentPage = currentPage;
    }

    @Override
    public void run(Player player) {
        modelGUI.openGUI(player, currentPage - 1, false);
    }
}