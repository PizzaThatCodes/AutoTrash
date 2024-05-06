package me.pizzalover.autotrash.db.model;

import jakarta.xml.bind.DatatypeConverter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.nio.charset.StandardCharsets;

public class trashableItems {

    private String uuid;
    private ItemStack item;
    private String permission;


    /**
     * Encode an item to base64
     * @param itemStack the itemStack
     * @return the base64 string
     */
    public String encodeItem(ItemStack itemStack) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("i", itemStack);
        return DatatypeConverter.printBase64Binary(config.saveToString().getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Decode an item from base64
     * @param string base64 string
     * @return the itemStack
     */
    public ItemStack decodeItem(String string) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(new String(DatatypeConverter.parseBase64Binary(string), StandardCharsets.UTF_8));
        } catch (IllegalArgumentException | InvalidConfigurationException e) {
            e.printStackTrace();
            return null;
        }
        return config.getItemStack("i", null);
    }

    /**
     * Constructor for ShopItem
     * @param uuid The random ID of the item
     * @param item The itemStack
     * @param permission the permission needed to trash the item
     */
    public trashableItems(String uuid, String item, String permission) {
        this.uuid = uuid;
        this.item = decodeItem(item);
        this.item.setAmount(1);
        this.permission = permission;
    }

    /**
     * Get the item uuid
     * @return the item uuid
     */
    public String getItemUuid() {
        return uuid;
    }

    /**
     * Get the item
     * @return the item
     */
    public ItemStack getItem() {
        return item;
    }

    /**
     * Get the permission
     * @return the permission
     */
    public String getPermission() {
        return permission;
    }

    /**
     * Set the permission
     * @param permission the permission
     */
    public void setPermission(String permission) {
        this.permission = permission;
    }

    /**
     * Set the item
     * @param item the item
     */
    public void setItem(ItemStack item) {
        this.item = item;
    }

    /**
     * Set the item uuid
     * @param uuid the item uuid
     */
    public void setItemUuid(String uuid) {
        this.uuid = uuid;
    }


}
