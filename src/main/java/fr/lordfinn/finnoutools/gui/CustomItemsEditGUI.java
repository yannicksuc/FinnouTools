package fr.lordfinn.finnoutools.gui;

import fr.lordfinn.finnoutools.FinnouTools;
import fr.lordfinn.finnoutools.command.CustomItemsGUICommand;
import fr.lordfinn.finnoutools.customitems.CustomItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

public class CustomItemsEditGUI {
    private FinnouTools plugin;
    public CustomItemsEditGUI(FinnouTools plugin) {
        this.plugin = plugin;
    }
    public void openGUI(Player player, CustomItem customItem) {
        Component titleComponent = Component.text("Edit Custom Item ", NamedTextColor.DARK_RED)
                .append(Component.text(customItem.getName(), NamedTextColor.WHITE));
        InteractiveGUIBase gui = new InteractiveGUIBase(plugin, 54, titleComponent);

        // Ajouter un item pour le type
        ItemStack typeItemStack = new ItemStack(Material.ANVIL);
        ItemMeta typeItemMeta = typeItemStack.getItemMeta();
        typeItemMeta.displayName(Component.text("Type", NamedTextColor.GOLD));
        typeItemMeta.lore(Collections.singletonList(Component.text(customItem.getType(), NamedTextColor.GRAY)));
        typeItemStack.setItemMeta(typeItemMeta);
        gui.addItem(new CustomItemsGUIItem(typeItemStack, new CustomItemsGUICommand.EditAction(customItem, "type")), 10);

        // Ajouter un item pour le nom
        ItemStack nameItemStack = new ItemStack(Material.NAME_TAG);
        ItemMeta nameItemMeta = nameItemStack.getItemMeta();
        nameItemMeta.displayName(Component.text("Nom", NamedTextColor.GOLD));
        nameItemMeta.lore(Collections.singletonList(Component.text(customItem.getName(), NamedTextColor.GRAY)));
        nameItemStack.setItemMeta(nameItemMeta);
        gui.addItem(new CustomItemsGUIItem(nameItemStack, new CustomItemsGUICommand.EditAction(customItem, "name")), 12);

        // Ajouter un item pour le custommodeldata
        ItemStack cmdItemStack = new ItemStack(Material.WRITABLE_BOOK);
        ItemMeta cmdItemMeta = cmdItemStack.getItemMeta();
        cmdItemMeta.displayName(Component.text("CustomModelData", NamedTextColor.GOLD));
        cmdItemMeta.lore(Collections.singletonList(Component.text(customItem.getCustomModelData(), NamedTextColor.GRAY)));
        cmdItemStack.setItemMeta(cmdItemMeta);
        gui.addItem(new CustomItemsGUIItem(cmdItemStack, new CustomItemsGUICommand.EditAction(customItem, "custommodeldata")), 14);

        // Ajouter un item pour le material
        ItemStack materialItemStack = customItem.toItemStack();
        ItemMeta materialItemMeta = materialItemStack.getItemMeta();
        materialItemMeta.displayName(Component.text("Material", NamedTextColor.GOLD));
        materialItemMeta.lore(Collections.singletonList(Component.text(materialItemStack.getType().toString(), NamedTextColor.GRAY)));
        materialItemStack.setItemMeta(materialItemMeta);
        gui.addItem(new CustomItemsGUIItem(materialItemStack, new CustomItemsGUICommand.EditAction(customItem, "material")), 16);

        // Ajouter un item pour la description
        ItemStack descriptionItemStack = new ItemStack(Material.PAPER);
        ItemMeta descriptionItemMeta = descriptionItemStack.getItemMeta();
        descriptionItemMeta.displayName(Component.text("Description", NamedTextColor.GOLD));
        descriptionItemMeta.lore(Collections.singletonList(Component.text(customItem.getDescription(), NamedTextColor.GRAY)));
        descriptionItemStack.setItemMeta(descriptionItemMeta);
        gui.addItem(new CustomItemsGUIItem(descriptionItemStack, new CustomItemsGUICommand.EditAction(customItem, "description")), 28);

        // Ajouter un item pour le projet
        ItemStack projectItemStack = new ItemStack(Material.MAP);
        ItemMeta projectItemMeta = projectItemStack.getItemMeta();
        projectItemMeta.displayName(Component.text("Projet", NamedTextColor.GOLD));
        projectItemMeta.lore(Collections.singletonList(Component.text(customItem.getProject(), NamedTextColor.GRAY)));
        projectItemStack.setItemMeta(projectItemMeta);
        gui.addItem(new CustomItemsGUIItem(projectItemStack, new CustomItemsGUICommand.EditAction(customItem, "project")), 30);

        gui.open(player);
    }
}
