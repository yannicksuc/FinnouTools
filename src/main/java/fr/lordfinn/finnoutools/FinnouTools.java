package fr.lordfinn.finnoutools;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class FinnouTools extends JavaPlugin {

    @Override
    public void onEnable() {
        registerCommand("brushToPattern", new BrushToPatternCommand());
    }

    private void registerCommand(String commandName, CommandExecutor executor) {
        PluginCommand command = getCommand(commandName);
        if (command != null) {
            command.setExecutor(executor);
        } else {
            getLogger().warning("The command \"" + commandName + "\" was not correctly registered in plugin.yml.");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
