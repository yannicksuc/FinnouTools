package fr.lordfinn.finnoutools;

import com.fastasyncworldedit.core.command.tool.brush.BrushSettings;
import com.fastasyncworldedit.core.configuration.Caption;
import com.fastasyncworldedit.core.extent.ResettableExtent;
import com.fastasyncworldedit.core.function.pattern.PatternTraverser;
import com.fastasyncworldedit.core.util.StringMan;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.command.tool.BrushTool;
import com.sk89q.worldedit.command.tool.brush.Brush;
import com.sk89q.worldedit.entity.Player;
import com.sk89q.worldedit.extent.inventory.BlockBag;
import com.sk89q.worldedit.function.mask.Mask;
import com.sk89q.worldedit.function.mask.MaskIntersection;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.request.Request;
import com.sk89q.worldedit.util.Location;

public class CustomBrushToolWrapper extends BrushTool {

    public CustomBrushToolWrapper(BrushTool brushTool) {

        this.setContext(brushTool.getContext());
        this.setPrimary(brushTool.getPrimary());
        this.setSecondary(brushTool.getSecondary());
        this.setMask(brushTool.getMask());
        this.setBrush(brushTool.getBrush(), brushTool.getContext().getPermissions().toString());
        this.update();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return new CustomBrushToolWrapper((BrushTool) super.clone());
    }
    public boolean act(Player player, LocalSession session, BlockVector3 blockTarget) {
        BrushSettings current = this.getContext();
        Brush brush = current.getBrush();
        if (brush == null) {
            return false;
        }
        if (!current.canUse(player)) {
            player.print(Caption.of("fawe.error.no-perm", new Object[]{StringMan.join(current.getPermissions(), ",")}));
            return false;
        } else {
            try {
                EditSession editSession = session.createEditSession(player, current.toString());

                boolean var31;
                label314: {
                    try {
                        Location target = player.getBlockTrace(this.getRange(), true, this.getTraceMask());
                        if (target == null) {
                            editSession.cancel();
                            player.print(Caption.of("worldedit.tool.no-block", new Object[0]));
                            var31 = true;
                            break label314;
                        }

                        BlockBag bag = session.getBlockBag(player);
                        Request.request().setEditSession(editSession);
                        Mask mask = current.getMask();
                        Mask existingMask;
                        if (mask != null) {
                            existingMask = editSession.getMask();
                            if (existingMask == null) {
                                editSession.setMask(mask);
                            } else if (existingMask instanceof MaskIntersection) {
                                ((MaskIntersection)existingMask).add(new Mask[]{mask});
                            } else {
                                MaskIntersection newMask = new MaskIntersection(new Mask[]{existingMask});
                                newMask.add(new Mask[]{mask});
                                editSession.setMask(newMask);
                            }
                        }

                        existingMask = current.getSourceMask();
                        if (existingMask != null) {
                            editSession.addSourceMask(existingMask);
                        }

                        ResettableExtent transform = current.getTransform();
                        if (transform != null) {
                            editSession.addTransform(transform);
                        }
                        try {
                            (new PatternTraverser(current)).reset(editSession);
                            double size = current.getSize();
                            WorldEdit.getInstance().checkMaxBrushRadius(size);
                            brush.build(editSession, blockTarget, current.getMaterial(), size);
                        } catch (MaxChangedBlocksException var27) {
                            player.print(Caption.of("worldedit.tool.max-block-changes", new Object[0]));
                        } finally {
                            session.remember(editSession);
                            if (bag != null) {
                                bag.flushChanges();
                            }

                        }
                    } catch (Throwable var29) {
                        if (editSession != null) {
                            try {
                                editSession.close();
                            } catch (Throwable var26) {
                                var29.addSuppressed(var26);
                            }
                        }

                        throw var29;
                    }

                    if (editSession != null) {
                        editSession.close();
                    }

                    return true;
                }

                if (editSession != null) {
                    editSession.close();
                }

                return var31;
            } finally {
                Request.reset();
            }
        }
    }
}
