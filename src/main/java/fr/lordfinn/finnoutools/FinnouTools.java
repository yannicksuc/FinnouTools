package fr.lordfinn.finnoutools;

import fr.lordfinn.finnoutools.command.BrushToPatternCommand;
import fr.lordfinn.finnoutools.command.GiveInvisibleFrameCommand;
import fr.lordfinn.finnoutools.command.ModelCommand;
import fr.lordfinn.finnoutools.command.ModelAddCommand;
import fr.lordfinn.finnoutools.command.ModelEditCommand;
import fr.lordfinn.finnoutools.models.CustomItemManager;
import fr.lordfinn.finnoutools.models.CustomItemStorage;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class FinnouTools extends JavaPlugin {

    private CustomItemStorage customItemStorage;
    private CustomItemManager customItemManager;

    @Override
    public void onEnable() {
        customItemStorage = new CustomItemStorage(this);
        customItemManager = new CustomItemManager(customItemStorage);

        Objects.requireNonNull(getCommand("giveInvisibleFrame")).setExecutor(new GiveInvisibleFrameCommand());
        Objects.requireNonNull(getCommand("brushToPattern")).setExecutor(new BrushToPatternCommand());
        Objects.requireNonNull(getCommand("model")).setExecutor(new ModelCommand(this, customItemManager));
        Objects.requireNonNull(getCommand("modeladd")).setExecutor(new ModelAddCommand(customItemManager));
        Objects.requireNonNull(getCommand("modeledit")).setExecutor(new ModelEditCommand(customItemManager));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}