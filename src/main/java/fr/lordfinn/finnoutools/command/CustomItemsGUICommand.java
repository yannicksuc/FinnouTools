package fr.lordfinn.finnoutools.command;

import fr.lordfinn.finnoutools.FinnouTools;
import fr.lordfinn.finnoutools.gui.CustomItemsGUI;
import fr.lordfinn.finnoutools.gui.CustomItemsGUIItem;
import fr.lordfinn.finnoutools.customitems.CustomItem;
import fr.lordfinn.finnoutools.customitems.CustomItemsManager;
import fr.lordfinn.finnoutools.utils.ChatListener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CustomItemsGUICommand implements SubCommand  {

    private static CustomItemsManager itemManager;
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
        private final CustomItem customItem;
        private final String field;
        private final Plugin plugin;

        public EditAction(CustomItem customItem, String field, Plugin plugin) {
            this.customItem = customItem;
            this.field = field;
            this.plugin = plugin;
        }

        @Override
        public void run(Player player) {
            player.closeInventory();
            // Ask the player for a new value
            player.sendMessage(Component.text("Enter a new value or type '", NamedTextColor.YELLOW)
                    .append(Component.text("cancel", NamedTextColor.RED))
                    .append(Component.text("' to cancel:", NamedTextColor.YELLOW)));

            // Create a new chat listener to listen for the player's response
            ChatListener chatListener = new ChatListener(player) {
                @Override
                public void onChat(String input) {
                    if (input.equalsIgnoreCase("cancel")) {
                        // If the player cancels, do nothing and return
                        player.sendMessage(Component.text("Edit cancelled.", NamedTextColor.RED));
                        return;
                    }

                    // Update the custom item with the new value using the CustomItemsManager.editCustomItem() method
                    try {
                        itemManager.editCustomItem(customItem, field, input);
                        player.sendMessage(Component.text("Successfully edited " + field + ".", NamedTextColor.GREEN));
                    } catch (IllegalArgumentException e) {
                        player.sendMessage(Component.text(e.getMessage(), NamedTextColor.RED));
                    }
                }
            };

            // Register the chat listener
            plugin.getServer().getPluginManager().registerEvents(chatListener, plugin);
        }
    }
}

