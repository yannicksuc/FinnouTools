package fr.lordfinn.finnoutools.command;

import fr.lordfinn.finnoutools.customitems.CustomItem;
import fr.lordfinn.finnoutools.customitems.CustomItemsManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static fr.lordfinn.finnoutools.utils.TextUtil.parseArgsArray;

public class CustomItemsEditCommand implements SubCommand  {
    private final CustomItemsManager itemsManager;
    public CustomItemsEditCommand(CustomItemsManager customItemsManager) {
        this.itemsManager = customItemsManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        args = parseArgsArray(args);
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be executed by a player.");
            return true;
        }

        if (args.length != 4) {
            sender.sendMessage(ChatColor.RED + "Usage: /customItems edit [material] [custom_model_data] [field (material, custom_model_data, type, name, project, description)] [new_value]");
            if (args.length > 4)
                sender.sendMessage(ChatColor.GRAY + "Use quotes \"like this\" if your new_value needs spaces");
            return true;
        }
        String materialName = args[0];
        String customModelDataString = args[1];
        String field = args[2];
        String value = args[3];

        Material material = Material.matchMaterial(materialName);

        if (material == null) {
            sender.sendMessage(ChatColor.RED + "The specified material does not exist.");
            return true;
        }

        int customModelData;

        try {
            customModelData = Integer.parseInt(customModelDataString);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "The specified custom_model_data is invalid.");
            return true;
        }
        Optional<CustomItem> customItem = itemsManager.getCustomItem(materialName, customModelData);
        if (customItem.isEmpty()) {
            sender.sendMessage(ChatColor.RED + "No custom item was found for the specified material and custom_model_data.");
            return true;
        }

        try {
            itemsManager.editCustomItem(customItem.get(), field, value);
        } catch (IllegalArgumentException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            return true;
        }
        sender.sendMessage(ChatColor.GREEN + "The " + field + " property was successfully updated.");
        return true;
    }


    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            String typedMaterial = args[0];
            return itemsManager.getCustomItems().stream()
                    .map(CustomItem::getMaterial).map(String::toUpperCase)
                    .distinct()
                    .filter(mat -> mat.startsWith(typedMaterial.toUpperCase())).toList();
        } else if (args.length == 2) {
            return itemsManager.getCustomItems().stream()
                    .filter(mat -> mat.getMaterial().equalsIgnoreCase(args[0]))
                    .map(CustomItem::getCustomModelData).map(Object::toString).distinct()
                    .filter(cmd -> cmd.startsWith(args[1].toUpperCase())).toList();
        } else if (args.length == 3) {
            return Arrays.asList("material", "custom_model_data", "type", "name", "project", "description");
        }else if (args.length == 4 && !args[2].equals("custom_model_data")) {
            return Collections.singletonList("\"\"");
        } else {
            return Collections.emptyList();
        }
    }
}
