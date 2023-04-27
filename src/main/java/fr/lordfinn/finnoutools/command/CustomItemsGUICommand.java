package fr.lordfinn.finnoutools.command;

import fr.lordfinn.finnoutools.FinnouTools;
import fr.lordfinn.finnoutools.gui.CustomItemsEditGUI;
import fr.lordfinn.finnoutools.gui.CustomItemsGUI;
import fr.lordfinn.finnoutools.gui.CustomItemsGUIItem;
import fr.lordfinn.finnoutools.customitems.CustomItem;
import fr.lordfinn.finnoutools.customitems.CustomItemsManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
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
        private CustomItemsManager itemManager;
        private CustomItemsEditGUI customItemsGUI;

        public EditAction(CustomItem customItem, String field, Plugin plugin, CustomItemsManager itemManager, CustomItemsEditGUI customItemsGUI) {
            this.customItem = customItem;
            this.field = field;
            this.plugin = plugin;
            this.itemManager = itemManager;
            this.customItemsGUI = customItemsGUI;
        }

        @Override
        public void run(Player player) {
            // Open an anvil inventory
            Inventory anvil = Bukkit.createInventory(player, InventoryType.ANVIL, Component.text("Edit", NamedTextColor.GRAY)
                    .append(Component.text(" > ", NamedTextColor.DARK_GRAY))
                    .append(Component.text(field, NamedTextColor.GOLD)));

            customItem.setName("The " + field + "is beeing modified");
            ItemStack leftItem = customItem.toItemStack();
            leftItem.lore(Collections.singletonList(Component.text("Click to validate")));
            // Set the left item slot
            anvil.setItem(0, leftItem);

            // Set the right item slot with a placeholder
            ItemStack placeholder = new ItemStack(Material.PAPER);
            ItemMeta meta = placeholder.getItemMeta();
            meta.displayName(Component.text("Enter a new value", NamedTextColor.GRAY));
            placeholder.setItemMeta(meta);
            anvil.setItem(2, placeholder);

            // Open the anvil inventory
            player.openInventory(anvil);

            // Create and register the listener for the InventoryClickEvent
            Listener listener = new Listener() {
                @EventHandler
                public void onInventoryClick(InventoryClickEvent event) {
                    if (!(event.getWhoClicked() instanceof Player currentPlayer)) {
                        return;
                    }

                    if (!currentPlayer.equals(player) || !event.getInventory().equals(anvil)) {
                        return;
                    }

                    if (event.getSlot() == 2 || event.getSlot() == 0) { // If the right item slot is clicked
                        ItemStack clickedItem = event.getCurrentItem();

                        if (clickedItem != null && clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasDisplayName()) {
                            Component displayName = clickedItem.getItemMeta().displayName();
                            String newValue = null;
                            if (displayName == null) {
                                newValue = "";
                            } else
                                newValue = LegacyComponentSerializer.legacySection().serialize(displayName);

                            try {
                                itemManager.editCustomItem(customItem, field, newValue);
                                player.sendMessage(Component.text("Successfully edited the " + field + " field.", NamedTextColor.GREEN));
                            } catch (IllegalArgumentException e) {
                                player.sendMessage(Component.text(e.getMessage(), NamedTextColor.RED));
                            }

                            player.closeInventory(); // Close the anvil inventory
                            customItemsGUI.openGUI(player, customItem); // Open the previous edit item GUI
                        }
                    }
                    event.setCancelled(true);
                }

                @EventHandler
                public void onInventoryClose(InventoryCloseEvent event) {
                    if (event.getInventory().equals(anvil) && event.getPlayer().equals(player)) {
                        InventoryClickEvent.getHandlerList().unregister(this);
                        InventoryCloseEvent.getHandlerList().unregister(this);
                    }
                }
            };

            // Register the listener
            plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }
}

