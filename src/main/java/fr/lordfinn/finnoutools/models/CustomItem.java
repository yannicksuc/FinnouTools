package fr.lordfinn.finnoutools.models;

import org.bukkit.inventory.ItemStack;

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
    }
}
