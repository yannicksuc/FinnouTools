package fr.lordfinn.finnoutools;

import fr.lordfinn.finnoutools.command.BrushToPatternCommand;
import fr.lordfinn.finnoutools.command.GiveInvisibleFrameCommand;
import fr.lordfinn.finnoutools.command.CustomItemsCommand;
import fr.lordfinn.finnoutools.customitems.CustomItemsManager;
import fr.lordfinn.finnoutools.customitems.CustomItemsStorage;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class FinnouTools extends JavaPlugin {

    private CustomItemsStorage customItemsStorage;
    private CustomItemsManager customItemsManager;

    @Override
    public void onEnable() {
        customItemsStorage = new CustomItemsStorage(this);
        customItemsManager = new CustomItemsManager(customItemsStorage);

        Objects.requireNonNull(getCommand("giveInvisibleFrame")).setExecutor(new GiveInvisibleFrameCommand());
        Objects.requireNonNull(getCommand("brushToPattern")).setExecutor(new BrushToPatternCommand());
        Objects.requireNonNull(getCommand("customItems")).setExecutor(new CustomItemsCommand(this, customItemsManager));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}