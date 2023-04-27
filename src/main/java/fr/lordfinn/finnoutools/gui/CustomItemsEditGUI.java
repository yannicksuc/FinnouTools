package fr.lordfinn.finnoutools.gui;

import fr.lordfinn.finnoutools.FinnouTools;
import fr.lordfinn.finnoutools.command.CustomItemsGUICommand;
import fr.lordfinn.finnoutools.customitems.CustomItem;
import fr.lordfinn.finnoutools.customitems.CustomItemsManager;
import fr.lordfinn.finnoutools.utils.Heads;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import static fr.lordfinn.finnoutools.utils.TextUtil.createWrappedComponent;
import static fr.lordfinn.finnoutools.utils.TextUtil.truncateStringIgnoringFormatting;

public class CustomItemsEditGUI {
    private FinnouTools plugin;
    private final CustomItemsManager itemManager;

    public CustomItemsEditGUI(FinnouTools plugin, CustomItemsManager itemManager) {
        this.plugin = plugin;
        this.itemManager = itemManager;
    }

    public void openGUI(Player player, CustomItem customItem) {
        Component titleComponent = LegacyComponentSerializer.legacyAmpersand().deserialize(truncateStringIgnoringFormatting(customItem.getName(),20, true))
                .append(Component.text(" > ", NamedTextColor.DARK_GRAY))
                .append(Component.text("Edit", NamedTextColor.GRAY));
        InteractiveGUIBase gui = new InteractiveGUIBase(plugin, 54, titleComponent);

        gui.addItem(createItemStack(customItem.toItemStack().getType(), "Material", customItem.toItemStack().getType().toString(), customItem, "material"), 11);
        gui.addItem(createItemStack(Material.WRITABLE_BOOK, "CustomModelData", String.valueOf(customItem.getCustomModelData()), customItem, "custommodeldata"),13);
        gui.addItem(createItemStack(Material.NAME_TAG, "Nom", customItem.getName(),  customItem, "name"),15);
        gui.addItem(createItemStack(getTypeMaterial(0), "Type", customItem.getType(),  customItem, "type"),29);
        gui.addItem(createItemStack(Material.NETHERITE_PICKAXE, "Projet", customItem.getProject(), customItem, "project"), 31);
        gui.addItem(createItemStack(Material.PAPER, "Description", customItem.getDescription(), customItem, "description"), 33);

        gui.addItem(createBackItemStack(), 49);

        gui.open(player);
        new BukkitRunnable() {
            static int typeIndex = 0;
            @Override
            public void run() {
                if (gui.isViewed()) {
                    this.cancel(); // Cancel the task if the inventory is not being viewed
                } else {
                    CustomItemsGUIItem itemStack = createItemStack(getTypeMaterial(typeIndex), "Type", customItem.getType(),  customItem, "type");
                    gui.addItem(itemStack, 29);
                    this.typeIndex+=1;
                }
            }
        }.runTaskTimer(plugin, 0, 50);
    }

    private CustomItemsGUIItem createBackItemStack() {
        ItemStack itemStack = Heads.BACK_ARROW.getItemStack();
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.text("Back", NamedTextColor.RED));
        itemStack.setItemMeta(itemMeta);
        return new CustomItemsGUIItem(itemStack, new GoBackGUIAction(this.plugin, this.itemManager));
    }

    private CustomItemsGUIItem createItemStack(Material material, String displayName, String lore, CustomItem customItem, String action) {
        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.displayName(Component.text(displayName, NamedTextColor.GOLD));
        itemMeta.lore(createWrappedComponent(lore, 25, NamedTextColor.GRAY));
        itemStack.setItemMeta(itemMeta);
        return new CustomItemsGUIItem(itemStack, new CustomItemsGUICommand.EditAction(customItem, action, this.plugin));
    }

    private Material getTypeMaterial(int index) {
        System.out.println(index);
        Material[] materials = {Material.LEATHER_HELMET, Material.STICK, Material.OAK_PLANKS};
        return materials[index % 3];
    }
}
