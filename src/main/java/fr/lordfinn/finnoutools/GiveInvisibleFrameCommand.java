package fr.lordfinn.finnoutools;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class GiveInvisibleFrameCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by a player.");
            return true;
        }

        Player player = (Player) sender;
        giveInvisibleItemFrame(player);
        player.sendMessage("§aYou have been given an invisible item frame.");
        return true;
    }

    private void giveInvisibleItemFrame(Player player) {
        String vanillaCommand = "minecraft:give " + player.getName() + " item_frame{EntityTag:{Invisible:1b},display:{Name:'[{\"text\":\"Invisible Item frame\",\"italic\":false,\"color\":\"dark_purple\"}]'},Enchantments:[{}],HideFlags:1} 1";
        player.getServer().dispatchCommand(player.getServer().getConsoleSender(), vanillaCommand);
    }
}
