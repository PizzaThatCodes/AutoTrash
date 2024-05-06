package me.pizzalover.autotrash.events;

import me.pizzalover.autotrash.Main;
import me.pizzalover.autotrash.db.model.trashableItems;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerItemEvents implements Listener {

//    @EventHandler
//    public void onPlayerItem(PlayerMoveEvent event) {
//        Player player = event.getPlayer();
//
//        for(trashableItems item : Main.getInstance().db.getAllItems()) {
//            if(player.getInventory().containsAtLeast(item.getItem(), 1)) {
//                player.sendMessage("You picked up a trashable item!");
//                item.getItem().setAmount(64);
//                player.getInventory().removeItem(item.getItem());
//                item.getItem().setAmount(1);
//            }
//        }
//    }

}
