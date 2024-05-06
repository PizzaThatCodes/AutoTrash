package me.pizzalover.autotrash.gui;

import me.pizzalover.autotrash.Main;
import me.pizzalover.autotrash.db.model.trashableItems;
import me.pizzalover.autotrash.utils.utils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import xyz.xenondevs.invui.gui.Gui;
import xyz.xenondevs.invui.item.Item;
import xyz.xenondevs.invui.item.ItemProvider;
import xyz.xenondevs.invui.item.builder.ItemBuilder;
import xyz.xenondevs.invui.item.impl.AbstractItem;
import xyz.xenondevs.invui.item.impl.SimpleItem;
import xyz.xenondevs.invui.window.Window;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class confirmationGUI {

    public static void openConfirmationGUI(Player player, trashableItems item) {
        Item border = new SimpleItem(new ItemBuilder(Material.BLACK_STAINED_GLASS_PANE));

        ItemStack itemStack = item.getItem();
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemStack.setAmount(1);

        ArrayList<String> lore = new ArrayList<>();

        if(itemStack.hasItemMeta() && itemMeta != null && itemMeta.hasLore()) {
            for (String yesLore : itemMeta.getLore()) {
                lore.add(yesLore);
            }
        }

        lore.add("");
        lore.add(utils.translate("&fUUID: " + item.getItemUuid()));
        lore.add(utils.translate("&fPermission: " + item.getPermission()));

        itemMeta.setLore(lore);


        itemStack.setItemMeta(itemMeta);

        Gui gui = Gui.normal()
                .setStructure(
                        "# # # # # # # # #",
                        "# . . . . . . . #",
                        "# . c . i . x . #",
                        "# . . . . . . . #",
                        "# # # # # # # # #")
                .addIngredient('#', border)
                .addIngredient('c', new AbstractItem() {
                    @Override
                    public ItemProvider getItemProvider() {
                        ItemStack confirmItem = new ItemStack(Material.GREEN_CONCRETE);
                        ItemMeta confirmItemMeta = confirmItem.getItemMeta();

                        confirmItemMeta.setDisplayName(utils.translate("&a&lConfirm"));
                        List<String> confirmationLore = new ArrayList<>();
                        confirmationLore.add(utils.translate("&fClick to confirm deletion!"));
                        confirmationLore.add(utils.translate("&f&lWARNING: &cThis action is irreversible!"));
                        confirmItemMeta.setLore(confirmationLore);
                        confirmItem.addUnsafeEnchantment(Enchantment.LURE, 1);
                        confirmItemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

                        confirmItem.setItemMeta(confirmItemMeta);
                        return new ItemBuilder(confirmItem);
                    }

                    @Override
                    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
                        Main.getInstance().db.deleteInformation(item);
                        Main.getInstance().db.updateInformation(item);
                        listGUI.openListGUI(player);
                    }
                })
                .addIngredient('i', new SimpleItem(new ItemBuilder(itemStack)))
                .addIngredient('x', new AbstractItem() {
                    @Override
                    public ItemProvider getItemProvider() {
                        ItemStack confirmItem = new ItemStack(Material.RED_CONCRETE);
                        ItemMeta confirmItemMeta = confirmItem.getItemMeta();

                        confirmItemMeta.setDisplayName(utils.translate("&c&lExit"));
                        List<String> confirmationLore = new ArrayList<>();
                        confirmationLore.add(utils.translate("&fClick to go back to the admin gui!"));
                        confirmItemMeta.setLore(confirmationLore);

                        confirmItem.setItemMeta(confirmItemMeta);
                        return new ItemBuilder(confirmItem);
                    }

                    @Override
                    public void handleClick(@NotNull ClickType clickType, @NotNull Player player, @NotNull InventoryClickEvent inventoryClickEvent) {
                        listGUI.openListGUI(player);
                    }
                })
                .build();

        Window window = Window.single()
                .setViewer(player)
                .setGui(gui)
                .setTitle("Confirm Deletion")
                .build();
        window.open();
    }
}
