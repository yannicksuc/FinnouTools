package fr.lordfinn.finnoutools.command;

import com.sk89q.worldedit.*;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.command.tool.BrushTool;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extension.input.ParserContext;
import com.sk89q.worldedit.extension.platform.Actor;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.function.pattern.Pattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldedit.util.formatting.text.TextComponent;
import com.sk89q.worldedit.util.formatting.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class BrushToPatternCommand implements TabExecutor {

    private final WorldEdit worldEdit;

    public BrushToPatternCommand() {
        this.worldEdit = WorldEdit.getInstance();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Actor actor = BukkitAdapter.adapt(sender);

        if (isNotAPlayer(actor)) {
            actor.print(TextComponent.of("This command can only be used by players.", TextColor.RED));
            return true;
        }

        Player player = (Player) actor;

        if (args.length < 1) {
            actor.print(TextComponent.of("Usage: /brushtopattern <pattern>", TextColor.RED));
            return true;
        }

        String pattern = args[0];
        LocalSession localSession = worldEdit.getSessionManager().get(player);

        try {
            Pattern wePattern = parsePattern(actor, pattern);
            applyPatternToSelectedRegion(sender, localSession, wePattern);
        } catch (Exception e) {
            TextComponent.of("Invalid pattern: " + e.getMessage(), TextColor.RED);
        }

        return true;
    }

    private boolean isNotAPlayer(Actor actor) {
        return !(actor instanceof Player);
    }

    private Pattern parsePattern(Actor actor, String pattern) throws Exception {
        ParserContext context = new ParserContext();
        context.setActor(actor);
        return worldEdit.getPatternFactory().parseFromInput(pattern, context);
    }

    private void applyPatternToSelectedRegion(CommandSender sender, LocalSession localSession, Pattern wePattern) {
        org.bukkit.entity.Player bukkitPlayer = (org.bukkit.entity.Player) sender;
        Region selection = getSelection(localSession);
        if (selection == null) {
            sender.sendMessage(ChatColor.RED + "No active selection!");
            return;
        }

        Extent extent = BukkitAdapter.adapt(bukkitPlayer.getWorld());
        List<BlockVector3> anchorBlocks = findAnchorBlocks(wePattern, extent, selection);
        applyBrushToAnchorBlocks(sender, localSession, bukkitPlayer, anchorBlocks);
    }

    private Region getSelection(LocalSession localSession) {
        try {
            return localSession.getSelection(localSession.getSelectionWorld());
        } catch (Exception e) {
            return null;
        }
    }

    private List<BlockVector3> findAnchorBlocks(Pattern wePattern, Extent extent, Region selection) {
        List<BlockVector3> anchorBlocks = new ArrayList<>();
        for (BlockVector3 position : selection) {
            if (wePattern.applyBlock(position).equals(position.getFullBlock(extent))) {
                    anchorBlocks.add(BlockVector3.at(position.getX(), position.getY(), position.getZ()));
            }
        }
        return anchorBlocks;
    }

    private void applyBrushToAnchorBlocks(CommandSender sender, LocalSession localSession, org.bukkit.entity.Player bukkitPlayer, List<BlockVector3> anchorBlocks) {
        BukkitPlayer worldEditPlayer = BukkitAdapter.adapt(bukkitPlayer);
        BrushTool weBrushTool = localSession.getBrushTool(worldEditPlayer);
        if (weBrushTool == null) {
            return;
        }
        CustomBrushToolWrapper brushTool = new CustomBrushToolWrapper(weBrushTool);

        // Apply the brush at each anchor block location
        for (BlockVector3 anchorBlock : anchorBlocks) {
            try {
                brushTool.act(worldEditPlayer, localSession, anchorBlock);
            } catch (Exception e) {
                sender.sendMessage(ChatColor.RED + "An error occurred while applying the brush at " + anchorBlock + ": " + e.getMessage());
            }
        }
        sender.sendMessage(ChatColor.GREEN + "Brush applied to " + anchorBlocks.size() + " blocks");
    }
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    private void applyBrushToAnchorBlocksAsync(CommandSender sender, LocalSession localSession, org.bukkit.entity.Player bukkitPlayer, List<BlockVector3> anchorBlocks) {
        BukkitPlayer worldEditPlayer = BukkitAdapter.adapt(bukkitPlayer);
        BrushTool weBrushTool = localSession.getBrushTool(worldEditPlayer);
        if (weBrushTool == null) {
            return;
        }
        CustomBrushToolWrapper brushTool = new CustomBrushToolWrapper(weBrushTool);

        executor.submit(() -> {
            int processedBlocks = 0;
            try (EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().fastMode(true).build()) {
                for (BlockVector3 anchorBlock : anchorBlocks) {
                    try {
                        brushTool.act(worldEditPlayer, localSession, anchorBlock);
                        processedBlocks++;
                    } catch (Exception e) {
                        sender.sendMessage(ChatColor.RED + "An error occurred while applying the brush at " + anchorBlock + ": " + e.getMessage());
                    }
                }
            }
            sender.sendMessage(ChatColor.GREEN + "Brush applied to " + processedBlocks + " blocks");
        });
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            Actor actor = BukkitAdapter.adapt(sender);
            if (actor instanceof Player) {
                return worldEdit.getPatternFactory().getSuggestions(args[0]).stream()
                        .filter(pattern -> pattern.startsWith(args[0]))
                        .collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }
}