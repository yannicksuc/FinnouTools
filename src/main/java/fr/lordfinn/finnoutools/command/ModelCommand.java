package fr.lordfinn.finnoutools.command;

        import fr.lordfinn.finnoutools.FinnouTools;
        import fr.lordfinn.finnoutools.models.CustomItemManager;
        import org.bukkit.command.Command;
        import org.bukkit.command.CommandExecutor;
        import org.bukkit.command.CommandSender;
        import org.jetbrains.annotations.NotNull;

        import java.util.HashMap;
        import java.util.Map;

public class ModelCommand implements CommandExecutor {
    private final Map<String, SubCommand> subCommands;

    public ModelCommand(FinnouTools finnouTools, CustomItemManager customItemManager) {
        subCommands = new HashMap<>();
        subCommands.put("gui", new ModelGUICommand(finnouTools, customItemManager));
     //   subCommands.put("add", new ModelAddCommand(customItemManager));
     //   subCommands.put("set", new ModelEditCommand(customItemManager));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            // Aucune sous-commande spécifiée, affichez l'aide
            return false;
        }

        SubCommand subCommand = subCommands.get(args[0].toLowerCase());
        if (subCommand == null) {
            // Sous-commande inconnue, affichez l'aide
            return false;
        }

        // Exécutez la sous-commande correspondante
        return subCommand.onCommand(sender, command, label, args);
    }
}
