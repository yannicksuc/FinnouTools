package fr.lordfinn.finnoutools.customitems;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.util.List;
import java.util.Optional;

public class CustomItemsManager {
    private final List<CustomItem> customItems;
     private final CustomItemsStorage storage;

    public CustomItemsManager(CustomItemsStorage customItemsStorage) {
        storage = customItemsStorage;
        customItems = storage.loadCustomItemsFromConfig();
    }

    public void addCustomItem(CustomItem customItem) {
        customItems.add(customItem);
    }

    public Optional<CustomItem> getCustomItem(String material, int customModelData) {
        return customItems.stream()
                .filter(item -> item.getMaterial().equalsIgnoreCase(material) && item.getCustomModelData() == customModelData)
                .findFirst();
    }

    public Integer getIndex(CustomItem customItem) {
        return customItems.indexOf(customItem);
    }

//    public void editCustomItem(String material, int customModelData, String field, String newValue) {
//        Optional<CustomItem> customItemOptional = this.getCustomItem(material, customModelData);
//        if (customItemOptional.isEmpty()) {
//            throw new IllegalArgumentException("The specified customItem wasn't found.");
//            return;
//        }
//        CustomItem customItem = customItemOptional.get();
//        editCustomItem(customItem, field, newValue);
//    }

    public void editCustomItem(CustomItem customItem, String field, String newValue) {
        Integer customItemIndex = this.getIndex(customItem);
        if (customItemIndex < 0) {
            throw new IllegalArgumentException("The specified customItem wasn't found.");
        }

        switch (field.toLowerCase()) {
            case "type" -> customItem.setType(newValue);
            case "material" -> {
                Material newMaterial = Material.matchMaterial(newValue);
                if (newMaterial == null) {
                    throw new IllegalArgumentException(ChatColor.RED + "The specified new material does not exist.");
                }
                customItem.setMaterial(newValue);
            }
            case "custom_model_data" -> {
                int newCustomModelData;
                try {
                    newCustomModelData = Integer.parseInt(newValue);
                    customItem.setCustomModelData(newCustomModelData);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("The specified new custom_model_data is invalid.");
                }
            }
            case "project" -> customItem.setProject(newValue);
            case "description" -> customItem.setDescription(newValue);
            case "name" -> customItem.setName(newValue);
            default -> throw new IllegalArgumentException("Invalid field specified.");
        }
        customItems.set(customItemIndex, customItem);
        this.storage.saveCustomItemsToConfig(customItems);
    }

    public List<CustomItem> getCustomItems() {
        return customItems;
    }
}
