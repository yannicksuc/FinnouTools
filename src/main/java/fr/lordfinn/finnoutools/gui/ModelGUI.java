package fr.lordfinn.finnoutools.gui;

import fr.lordfinn.finnoutools.FinnouTools;
import fr.lordfinn.finnoutools.command.ModelGUICommand;
import fr.lordfinn.finnoutools.models.CustomItem;
import fr.lordfinn.finnoutools.models.CustomItemManager;
import fr.lordfinn.finnoutools.utils.Heads;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ModelGUI {
    private FinnouTools plugin;
    private CustomItemManager itemManager;
    public ModelGUI(FinnouTools plugin, CustomItemManager customItemManager) {
        this.plugin = plugin;
        this.itemManager = customItemManager;
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
        CustomGUI gui = new CustomGUI(plugin, 54,titleComponent);

        for (int i = startIndex; i < endIndex; i++) {
            CustomItem customItem = customItems.get(i);
            ItemStack itemStack = customItem.toItemStack();
            gui.addItem(new ModelGUIItem(itemStack, new ModelGUICommand.GiveAction(itemStack)));
        }

        // Ajouter les éléments spéciaux
        ModelGUIItem nextPageItem = new ModelGUIItem(Heads.RIGHT_ARROW.getItemStack(), new NextPageAction(this, page));
        ModelGUIItem prevPageItem = new ModelGUIItem(Heads.LEFT_ARROW.getItemStack(), new PrevPageAction(this, page));
        gui.addItem(nextPageItem, 53);
        gui.addItem(prevPageItem, 52);

        gui.open(player);
    }

}
