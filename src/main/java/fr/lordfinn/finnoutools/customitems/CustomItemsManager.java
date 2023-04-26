package fr.lordfinn.finnoutools.customitems;

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

    public Optional<CustomItem> getCustomItem(String itemNamespace, int customModelData) {
        return customItems.stream()
                .filter(item -> item.getItemNamespace().equalsIgnoreCase(itemNamespace) && item.getCustomModelData() == customModelData)
                .findFirst();
    }

    public void editCustomItem(String itemNamespace, int customModelData, String field, String newValue) {
        Optional<CustomItem> customItemOptional = getCustomItem(itemNamespace, customModelData);

        if (!customItemOptional.isPresent()) {
            return;
        }
        CustomItem customItem = customItemOptional.get();
        switch (field.toLowerCase()) {
            case "type":
                customItem.setType(newValue);
                break;
            case "item_namespace":
                customItem.setItemNamespace(newValue);
                break;
            case "custom_model_data":
                customItem.setCustomModelData(Integer.parseInt(newValue));
                break;
            case "project":
                customItem.setProject(newValue);
                break;
            case "description":
                customItem.setDescription(newValue);
                break;
            case "name":
                customItem.setName(newValue);
                break;
            default:
                throw new IllegalArgumentException("Invalid field specified.");
        }
        this.storage.saveCustomItemsToConfig(customItems);
    }

    public List<CustomItem> getCustomItems() {
        return customItems;
    }
}
