package fr.lordfinn.finnoutools.command;

import fr.lordfinn.finnoutools.customitems.CustomItem;
import fr.lordfinn.finnoutools.customitems.CustomItemsManager;
import fr.lordfinn.finnoutools.gui.CustomItemsEditGUI;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CustomItemsAddCommand implements SubCommand {
    private final CustomItemsManager customItemsManager;
    private Plugin plugin;

    public CustomItemsAddCommand(CustomItemsManager customItemsManager, Plugin plugin) {
        this.customItemsManager = customItemsManager;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) commandSender;

        if (args.length < 2) {
            player.sendMessage("Usage: /customItem add <material> <customModelData> [name] [type] [project] [description]");
            return true;
        }
        Material material = Material.matchMaterial(args[0]);
        if (material == null) {
            player.sendMessage("Invalid material specified.");
            return true;
        }

        int customModelData;
        try {
            customModelData = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            player.sendMessage("Invalid custom model data specified.");
            return true;
        }

        String name = args.length > 2 ? args[2] : "";
        String type = args.length > 3 ? args[3] : "item";
        String project = args.length > 4 ? args[4] : "general";
        String description = args.length > 5 ? args[5] : "";

        CustomItem customItem = new CustomItem(material.toString(), customModelData, name, type, project, description);
        customItemsManager.addCustomItem(customItem);
        player.sendMessage(String.format("Created a custom item: \"%s\" (Material: %s, CustomModelData: %d)", name, material, customModelData));
        CustomItemsEditGUI customItemsEditGUI = new CustomItemsEditGUI(plugin, customItemsManager);
        customItemsEditGUI.openGUI(player, customItem);
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (args.length == 1) {
            String typedMaterial = args[0];
            return Arrays.stream(Material.values()).map(Objects::toString)
                    .filter(mat -> mat.startsWith(typedMaterial.toUpperCase())).toList();
        } else if (args.length == 2) {
            if (args[1].isEmpty())
                return Collections.singletonList("");
            else if (customItemsManager.getCustomItems().stream()
                    .filter(mat -> mat.getMaterial().equalsIgnoreCase(args[0]))
                    .map(CustomItem::getCustomModelData).map(Object::toString).distinct().noneMatch(cmd -> cmd.equalsIgnoreCase(args[1].toUpperCase())))
                return Collections.singletonList("VALID : This Custom Model Data is not used.");
            return Collections.singletonList("INVALID : This Custom Model Data is already used.");
        }

        return null;
    }
}