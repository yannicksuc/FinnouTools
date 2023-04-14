package fr.lordfinn.finnoutools.command;

import fr.lordfinn.finnoutools.models.CustomItemManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ModelGUICommand implements SubCommand  {
    public ModelGUICommand(CustomItemManager customItemManager) {
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        return false;
    }
}
