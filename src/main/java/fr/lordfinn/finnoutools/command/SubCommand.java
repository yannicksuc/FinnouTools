package fr.lordfinn.finnoutools.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface SubCommand {
    boolean onCommand(CommandSender sender, Command command, String label, String[] args);
}