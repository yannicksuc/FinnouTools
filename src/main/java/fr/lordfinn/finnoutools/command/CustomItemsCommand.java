package fr.lordfinn.finnoutools.command;

        import fr.lordfinn.finnoutools.FinnouTools;
        import fr.lordfinn.finnoutools.customitems.CustomItemsManager;
        import org.bukkit.command.Command;
        import org.bukkit.command.CommandExecutor;
        import org.bukkit.command.CommandSender;
        import org.bukkit.command.TabCompleter;
        import org.jetbrains.annotations.NotNull;
        import org.jetbrains.annotations.Nullable;

        import java.util.Arrays;
        import java.util.HashMap;
        import java.util.List;
        import java.util.Map;
        import java.util.stream.Collectors;

public class CustomItemsCommand implements CommandExecutor, TabCompleter {
    private final Map<String, SubCommand> subCommands;

    public CustomItemsCommand(FinnouTools finnouTools, CustomItemsManager customItemsManager) {
        subCommands = new HashMap<>();
        subCommands.put("gui", new CustomItemsGUICommand(finnouTools, customItemsManager));
        subCommands.put("edit", new CustomItemsEditCommand(customItemsManager));
     //   subCommands.put("add", new ModelAddCommand(customItemManager));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            return subCommands.get("gui").onCommand(sender, command, label, args);
        }

        SubCommand subCommand = subCommands.get(args[0].toLowerCase());
        if (subCommand == null) {
            // Sous-commande inconnue, afficher l'aide #TODO
            return false;
        }

        return subCommand.onCommand(sender, command, label, args);
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        if (args.length == 1) {
            List<String> suggestions;
            String partialCommand = args[0].toLowerCase();
            suggestions = subCommands.keySet().stream()
                    .filter(cmd -> cmd.startsWith(partialCommand))
                    .collect(Collectors.toList());
            return suggestions;
        }
        SubCommand subCommand = subCommands.get(args[0].toLowerCase());
        if (subCommand != null) {
            return subCommand.onTabComplete(commandSender, command, alias, Arrays.copyOfRange(args, 1, args.length));
        }
        return null;
    }
}
