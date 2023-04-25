package fr.lordfinn.finnoutools.models;

import fr.lordfinn.finnoutools.utils.TextUtil;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CustomItem {
    private String itemNamespace;
    private int customModelData;
    private String name;
    private String type;
    private String project;
    private String description;

    public CustomItem(String itemNamespace, int customModelData, String name, String type, String project, String description) {
        this.itemNamespace = itemNamespace;
        this.customModelData = customModelData;
        this.name = name;
        this.type = type;
        this.project = project;
        this.description = description;
    }

    public String getItemNamespace() {
        return itemNamespace;
    }

    public void setItemNamespace(String itemNamespace) {
        this.itemNamespace = itemNamespace;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public void setCustomModelData(int customModelData) {
        this.customModelData = customModelData;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ItemStack toItemStack() {
        Material material = Material.getMaterial(itemNamespace);
        ItemStack item = new ItemStack(material == null ? Material.BARRIER : material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            setItemMeta(meta);
            item.setItemMeta(meta);
        }
        return item;
    }

    private void setItemMeta(ItemMeta meta) {
        setCustomModelData(meta);
        setDisplayName(meta);
        setLore(meta);
    }

    private void setCustomModelData(ItemMeta meta) {
        meta.setCustomModelData(customModelData);
    }

    private void setDisplayName(ItemMeta meta) {
        String displayName = ChatColor.translateAlternateColorCodes('&', name);
        Component displayNameComponent = Component.text(displayName);
        meta.displayName(displayNameComponent);
    }

    private void setLore(ItemMeta meta) {
        List<Component> lore = createLore();
        meta.lore(lore);
    }

    private List<Component> createLore() {
        List<Component> lore = new ArrayList<>();
        lore.add(createLoreComponent("♟ CMD: ", String.valueOf(customModelData),  NamedTextColor.DARK_GRAY, NamedTextColor.GRAY));
        lore.add(createLoreComponent("☸ Type: ", type,                            NamedTextColor.DARK_AQUA, NamedTextColor.AQUA));
        lore.add(createLoreComponent("⛏ Project: ", project,                      NamedTextColor.DARK_GREEN, NamedTextColor.GREEN));
        lore.add(Component.text(""));
        lore.addAll(createWrappedDescription());
        return lore;
    }

    private Component createLoreComponent(String labelText, String valueText, TextColor labelColor, TextColor valueColor) {
        return Component.text(labelText, labelColor)
                .append(Component.text(valueText, valueColor)
                        .style(style -> style.decoration(TextDecoration.ITALIC, true)));
    }

    private List<Component> createWrappedDescription() {
        return TextUtil.wrapText(description, 25, NamedTextColor.GRAY);
    }
}
