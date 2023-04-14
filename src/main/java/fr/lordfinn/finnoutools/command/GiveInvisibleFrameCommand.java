package fr.lordfinn.finnoutools.command;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.apache.logging.log4j.LogManager.getLogger;

public class GiveInvisibleFrameCommand implements CommandExecutor, TabCompleter {

    private static final Component PLAYER_ONLY_MESSAGE = Component.text("This command can only be used by a player.", NamedTextColor.RED);
    private static final Component ITEM_RECEIVED_MESSAGE = Component.text("You have received an invisible item frame.", NamedTextColor.GREEN);
    private static final NamespacedKey IS_INVISIBLE_KEY = NamespacedKey.minecraft("invisible");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(PLAYER_ONLY_MESSAGE);
            return true;
        }

        boolean isGlowing = args.length > 0 && args[0].equalsIgnoreCase("glowing");

        Player player = (Player) sender;
        giveInvisibleItemFrame(player, isGlowing);
        player.sendMessage(ITEM_RECEIVED_MESSAGE);
        return true;
    }

    private void giveInvisibleItemFrame(Player player, boolean isGlowing) {
//        String vanillaCommand = "minecraft:give " + player.getName() + " item_frame{EntityTag:{Invisible:1b},display:{Name:'[{\"text\":\"Invisible Item frame\",\"italic\":false,\"color\":\"dark_purple\"}]'},Enchantments:[{}],HideFlags:1} 1";
//        player.getServer().dispatchCommand(player.getServer().getConsoleSender(), vanillaCommand);
        ItemStack item;
        if (isGlowing) {
            item = createInvisibleItemFrame(Material.GLOW_ITEM_FRAME);
        } else {
            item = createInvisibleItemFrame(Material.ITEM_FRAME);
        }
        player.getInventory().addItem(item);
    }

    private ItemStack createInvisibleItemFrame(Material material) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
//        assert meta != null;
        meta.displayName(Component.text("Invisible Item Frame", NamedTextColor.AQUA));
        meta.lore(List.of(
                Component.text("An item frame that is", NamedTextColor.GRAY),
                Component.text("invisible when placed.", NamedTextColor.GRAY)));
        meta.addEnchant(Enchantment.LURE, 1, true);
        PersistentDataContainer newContainer = meta.getPersistentDataContainer().getAdapterContext().newPersistentDataContainer();
        newContainer.set(IS_INVISIBLE_KEY, PersistentDataType.BYTE, (byte) 1);
        meta.getPersistentDataContainer().set(NamespacedKey.minecraft("EntityTag"), PersistentDataType.TAG_CONTAINER, newContainer);
        item.setItemMeta(meta);
        return item;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            return List.of("glowing");
        }
        return Collections.emptyList();
    }
}
