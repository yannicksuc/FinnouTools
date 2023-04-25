package fr.lordfinn.finnoutools.command;

import fr.lordfinn.finnoutools.FinnouTools;
import fr.lordfinn.finnoutools.gui.ModelGUI;
import fr.lordfinn.finnoutools.gui.ModelGUIItem;
import fr.lordfinn.finnoutools.models.CustomItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ModelGUICommand implements SubCommand  {

    private CustomItemManager itemManager;
    private ModelGUI modelGUI;
    public ModelGUICommand(FinnouTools plugin, CustomItemManager customItemManager) {
        this.itemManager = customItemManager;
        this.modelGUI = new ModelGUI(plugin, this.itemManager);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        modelGUI.openGUI((Player) commandSender, 0, true);
        return false;
    }

    public static class GiveAction implements ModelGUIItem.Action {
        private final ItemStack item;

        public GiveAction(ItemStack item) {
            this.item = item;
        }

        @Override
        public void run(Player player) {
            player.getInventory().addItem(item.clone());
        }
    }

}
