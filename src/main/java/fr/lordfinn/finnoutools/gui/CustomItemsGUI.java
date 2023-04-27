package fr.lordfinn.finnoutools.gui;

import fr.lordfinn.finnoutools.FinnouTools;
import fr.lordfinn.finnoutools.command.CustomItemsGUICommand;
import fr.lordfinn.finnoutools.customitems.CustomItem;
import fr.lordfinn.finnoutools.customitems.CustomItemsManager;
import fr.lordfinn.finnoutools.utils.Heads;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class CustomItemsGUI {
    private FinnouTools plugin;
    private CustomItemsManager itemManager;
    private CustomItemsEditGUI customItemsEditGUI;
    public CustomItemsGUI(FinnouTools plugin, CustomItemsManager customItemsManager) {
        this.plugin = plugin;
        this.itemManager = customItemsManager;
        this.customItemsEditGUI = new CustomItemsEditGUI(plugin, itemManager);
    }


    public void openGUI(Player player, int page, boolean displayMode) {
        List<CustomItem> customItems = this.itemManager.getCustomItems();
        int itemsPerPage = 45;
        int totalPages = (int) Math.ceil(customItems.size() / (double) itemsPerPage);

        if (page > totalPages) {
            page = totalPages;
        }

        if (page <= 0) {
            page = 1;
        }

        int startIndex = (page - 1) * itemsPerPage;
        int endIndex = Math.min(startIndex + itemsPerPage, customItems.size());
        Component titleComponent = Component.text("Custom Items ", NamedTextColor.DARK_RED)
                .append(Component.text(page, NamedTextColor.DARK_GRAY))
                .append(Component.text("/"+ totalPages, NamedTextColor.GRAY));
        InteractiveGUIBase gui = new InteractiveGUIBase(plugin, 54,titleComponent);

        for (int i = startIndex; i < endIndex; i++) {
            CustomItem customItem = customItems.get(i);
            ItemStack itemStack = customItem.toItemStack();
            OpenEditGUIAction openEditGUIAction = new OpenEditGUIAction(customItemsEditGUI, customItem);
            gui.addItem(new CustomItemsGUIItem(itemStack, new CustomItemsGUICommand.GiveAction(itemStack), openEditGUIAction));
        }

        CustomItemsGUIItem nextPageItem = new CustomItemsGUIItem(Heads.RIGHT_ARROW.getItemStack(), new NextPageAction(this, page));
        CustomItemsGUIItem prevPageItem = new CustomItemsGUIItem(Heads.LEFT_ARROW.getItemStack(), new PrevPageAction(this, page));
        gui.addItem(nextPageItem, 53);
        gui.addItem(prevPageItem, 52);

        gui.open(player);
    }

}
