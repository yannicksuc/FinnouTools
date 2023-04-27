package fr.lordfinn.finnoutools.command;

import fr.lordfinn.finnoutools.FinnouTools;
import fr.lordfinn.finnoutools.customitems.CustomItem;
import fr.lordfinn.finnoutools.customitems.CustomItemsManager;
import fr.lordfinn.finnoutools.gui.CustomItemsEditGUI;
import fr.lordfinn.finnoutools.gui.CustomItemsGUI;
import fr.lordfinn.finnoutools.gui.CustomItemsGUIItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class CustomItemsGUICommand implements SubCommand  {

    private final CustomItemsGUI customItemsGUI;
    public CustomItemsGUICommand(FinnouTools plugin, CustomItemsManager customItemsManager) {
        this.customItemsGUI = new CustomItemsGUI(plugin, customItemsManager);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        customItemsGUI.openGUI((Player) commandSender, 0);
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
        private final CustomItemsManager itemManager;
        private final CustomItemsEditGUI customItemsGUI;

        public EditAction(CustomItem customItem, String field, Plugin plugin, CustomItemsManager itemManager, CustomItemsEditGUI customItemsGUI) {
            this.customItem = customItem;
            this.field = field;
            this.plugin = plugin;
            this.itemManager = itemManager;
            this.customItemsGUI = customItemsGUI;
        }

        @Override
        public void run(Player player) {
            ItemStack leftItem = customItem.toItemStack();
            String fieldValue = customItem.getValueByFieldName(field);
            leftItem.getItemMeta().displayName(LegacyComponentSerializer.legacyAmpersand().deserialize(fieldValue));
            leftItem.lore(Collections.singletonList(Component.text("Click to validate")));
            new AnvilGUI.Builder()
                    .onClose(locPlayer -> customItemsGUI.openGUI(player, customItem))
                    .interactableSlots(AnvilGUI.Slot.OUTPUT)
                    .text(fieldValue)
                    .onComplete(this::toto)
                    .itemLeft(leftItem)
                    .title("§7Edit §8>§6 " + field)
                    .plugin(this.plugin)
                    .open(player);
        }

        private List<AnvilGUI.ResponseAction> toto(AnvilGUI.Completion completion) {
            String newValue = completion.getText();
            Player player = completion.getPlayer();
            try {
                itemManager.editCustomItem(customItem, field, newValue);
                player.sendMessage(Component.text("Successfully edited the " + field + " field to " + newValue + ".", NamedTextColor.GREEN));
            } catch (IllegalArgumentException e) {
                player.sendMessage(Component.text(e.getMessage(), NamedTextColor.RED));
            }
            return Collections.singletonList(AnvilGUI.ResponseAction.close());
        }
    }
}

