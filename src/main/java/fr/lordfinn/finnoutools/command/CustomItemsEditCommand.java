package fr.lordfinn.finnoutools.command;

import fr.lordfinn.finnoutools.customitems.CustomItem;
import fr.lordfinn.finnoutools.customitems.CustomItemsManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CustomItemsEditCommand implements SubCommand  {
    private CustomItemsManager itemsManager;
    public CustomItemsEditCommand(CustomItemsManager customItemsManager) {
        this.itemsManager = customItemsManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return false;
    }


    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            List<String> completions = new ArrayList<>();

            for (Material material : Material.values()) {
                completions.add(material.toString());
            }

            return completions;
        } else if (args.length == 2) {
            List<String> completions = new ArrayList<>();

            for (CustomItem customItem : itemsManager.getCustomItems()) {
                completions.add(String.valueOf(customItem.getCustomModelData()));
            }

            return completions;
        } else if (args.length == 3) {
            return Arrays.asList("type", "nom", "custommodeldata", "material", "description", "projet");
        } else {
            return Collections.emptyList();
        }
    }
}
