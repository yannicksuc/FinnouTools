package fr.lordfinn.finnoutools.models;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CustomItemStorage {
    private final JavaPlugin plugin;
    private final File configFile;
    private final FileConfiguration config;

    public CustomItemStorage(JavaPlugin plugin) {
        this.plugin = plugin;
        configFile = new File(plugin.getDataFolder(), "custom_items.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource("custom_items.yml", false);
        }

        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public List<CustomItem> loadCustomItemsFromConfig() {
        List<CustomItem> customItems = new ArrayList<>();
        ConfigurationSection itemsSection = getItemsSection();

        if (itemsSection != null) {
            Set<String> keys = itemsSection.getKeys(false);
            for (String key : keys) {
                ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                if (itemSection != null) {
                    CustomItem customItem = createCustomItemFromSection(itemSection);
                    customItems.add(customItem);
                }
            }
        }
        return customItems;
    }

    private ConfigurationSection getItemsSection() {
        return config.getConfigurationSection("items");
    }

    private CustomItem createCustomItemFromSection(ConfigurationSection itemSection) {
        String itemNamespace = itemSection.getString("item_namespace");
        int customModelData = itemSection.getInt("custom_model_data");
        String name = itemSection.getString("name");
        String type = itemSection.getString("type");
        String project = itemSection.getString("project");
        String description = itemSection.getString("description");

        return new CustomItem(itemNamespace, customModelData, name, type, project, description);
    }

    public void saveCustomItemsToConfig(List<CustomItem> customItems) {
        ConfigurationSection itemsSection = config.createSection("items");
        int index = 0;
        for (CustomItem customItem : customItems) {
            String key = "item_" + index;
            ConfigurationSection itemSection = itemsSection.createSection(key);
            saveCustomItemToSection(customItem, itemSection);
            index++;
        }
        save();
    }

    private void saveCustomItemToSection(CustomItem customItem, ConfigurationSection itemSection) {
        itemSection.set("item_namespace", customItem.getItemNamespace());
        itemSection.set("custom_model_data", customItem.getCustomModelData());
        itemSection.set("name", customItem.getName());
        itemSection.set("type", customItem.getType());
        itemSection.set("project", customItem.getProject());
        itemSection.set("description", customItem.getDescription());
    }

    public void save() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Could not save custom items to " + configFile);
            e.printStackTrace();
        }
    }
}