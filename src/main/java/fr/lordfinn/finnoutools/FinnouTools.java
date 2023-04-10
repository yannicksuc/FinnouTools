package fr.lordfinn.finnoutools;
import fr.lordfinn.finnoutools.command.BrushToPatternCommand;
import fr.lordfinn.finnoutools.command.GiveInvisibleFrameCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class FinnouTools extends JavaPlugin {

    @Override
    public void onEnable() {
        Objects.requireNonNull(getCommand("giveInvisibleFrame")).setExecutor(new GiveInvisibleFrameCommand());
        Objects.requireNonNull(getCommand("brushToPattern")).setExecutor(new BrushToPatternCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
