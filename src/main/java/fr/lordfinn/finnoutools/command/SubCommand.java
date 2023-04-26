package fr.lordfinn.finnoutools.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public interface SubCommand extends TabCompleter, CommandExecutor {
}