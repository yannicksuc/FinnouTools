package fr.lordfinn.finnoutools.command;

import fr.lordfinn.finnoutools.FinnouTools;
import fr.lordfinn.finnoutools.gui.CustomItemsGUI;
import fr.lordfinn.finnoutools.gui.CustomItemsGUIItem;
import fr.lordfinn.finnoutools.customitems.CustomItem;
import fr.lordfinn.finnoutools.customitems.CustomItemsManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomItemsGUICommand implements SubCommand  {

    private CustomItemsManager itemManager;
    private CustomItemsGUI customItemsGUI;
    public CustomItemsGUICommand(FinnouTools plugin, CustomItemsManager customItemsManager) {
        this.itemManager = customItemsManager;
        this.customItemsGUI = new CustomItemsGUI(plugin, this.itemManager);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        customItemsGUI.openGUI((Player) commandSender, 0, true);
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return null;
    }

    public static class GiveAction implements CustomItemsGUIItem.Action {
        private final ItemStack item;
        public GiveAction(ItemStack item) {
            this.item = item;
        }
        @Override
        public void run(Player player) {
            player.getInventory().addItem(item.clone());
        }
    }

    public static class EditAction implements CustomItemsGUIItem.Action {
        public EditAction(CustomItem customItem, String material) {
        }

        @Override
        public void run(Player player) {

        }
    }
}
