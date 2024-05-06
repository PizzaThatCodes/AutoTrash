package me.pizzalover.autotrash.gui;

import me.pizzalover.autotrash.Main;
import me.pizzalover.autotrash.db.model.trashableItems;
import me.pizzalover.autotrash.gui.buttons.BackItem;
import me.pizzalover.autotrash.gui.buttons.ForwardItem;
import me.pizzalover.autotrash.utils.config.settingConfig;
import me.pizzalover.autotrash.utils.utils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.gui.PagedGui;
import xyz.xenondevs.invui.gui.structure.Markers;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.util.ArrayList;
import java.util.List;

public class listGUI {

    private static Item border = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE).setDisplayName("§r"));

    public static void openListGUI(Player player) {

        List<Item> items = new ArrayList<>();
        ArrayList<trashableItems> itemsLmao;


        itemsLmao = Main.getInstance().db.getAllItems();
        for(trashableItems shopItem : itemsLmao) {
            trashableItems oldShopItem = new trashableItems(
                    shopItem.getItemUuid(),
                    shopItem.encodeItem(shopItem.getItem()),
                    shopItem.getPermission()
            );
            ItemStack itemStack = shopItem.getItem();
            ItemMeta itemMeta = itemStack.getItemMeta();

            itemStack.setAmount(1);

            ArrayList<String> lore = new ArrayList<>();

            if(itemStack.hasItemMeta() && itemMeta != null && itemMeta.hasLore()) {
                for (String yesLore : itemMeta.getLore()) {
                    lore.add(yesLore);
                }
            }

            lore.add("");
            lore.add(utils.translate("&fUUID: " + shopItem.getItemUuid()));
            lore.add(utils.translate("&fPermission: " + shopItem.getPermission()));
            lore.add("");
            lore.add(utils.translate("&f&lLeft click to delete!"));

            itemMeta.setLore(lore);


            itemStack.setItemMeta(itemMeta);




            items.add( new AbstractItem() {
                @Override
                public ItemProvider getItemProvider() {
                    return new ItemBuilder(itemStack);
                }

                @Override
                public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
                    if(!clickType.isLeftClick()) return;
                    if(!settingConfig.getConfig().getBoolean("settings.confirm-delete")) {
                        itemsLmao.remove(shopItem);
                        items.remove(this);
                        Main.getInstance().db.deleteInformation(shopItem);
                        Main.getInstance().db.updateInformation(shopItem);
                        notifyWindows();
                        openListGUI(player);
                    } else {
                        confirmationGUI.openConfirmationGUI(player, oldShopItem);
                    }
                }
            });

        }

        Gui gui = PagedGui.items()
                .setStructure(
                        "# # # # # # # # #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# x x x x x x x #",
                        "# # # < # > # # #")
                .addIngredient('x', Markers.CONTENT_LIST_SLOT_HORIZONTAL) // where paged items should be put
                .addIngredient('#', border)
                .addIngredient('<', new BackItem())
                .addIngredient('>', new ForwardItem())
                .setContent(items)
                .build();
        
        
        
        Window window = Window.single()
                .setViewer(player)
                .setGui(gui)
                .setTitle(utils.translate( utils.addPlaceholderToText(player, "List GUI")))
                .build();
        window.open();
        
    }
    
}
