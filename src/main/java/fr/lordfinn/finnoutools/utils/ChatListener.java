package fr.lordfinn.finnoutools.utils;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;

public abstract class ChatListener implements Listener {
    private final Player player;

    public ChatListener(Player player) {
        this.player = player;
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent event) {
        event.setCancelled(true);
        if (!event.getPlayer().equals(player)) {
            return;
        }
        // Check if the player typed 'cancel' to cancel the edit
        String input = PlainTextComponentSerializer.plainText().serialize(event.message()).trim();
        if (input.equalsIgnoreCase("cancel")) {
            player.sendMessage("Edit cancelled.");
            unregister();
            return;
        }

        // Handle the player's response
        onChat(input);

        // Unregister the chat listener after handling the response
        unregister();
    }

    public abstract void onChat(String input);

    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}
