package me.pizzalover.autotrash.commands;

import me.pizzalover.autotrash.Main;
import me.pizzalover.autotrash.db.model.trashableItems;
import me.pizzalover.autotrash.gui.listGUI;
import me.pizzalover.autotrash.utils.config.databaseConfig;
import me.pizzalover.autotrash.utils.config.messageConfig;
import me.pizzalover.autotrash.utils.config.settingConfig;
import me.pizzalover.autotrash.utils.utils;
import me.pizzalover.autotrash.utils.variables;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Commands implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {

        if(!(sender instanceof Player)) {
            sender.sendMessage(utils.translate(messageConfig.getConfig().getString("messages.console-cannot-use-command").replace("{prefix}", variables.prefix)));
            return true;
        }

        Player player = (Player) sender;

        if(!player.hasPermission(Main.getInstance().getConfig().getString("commands.admin.permission"))) {
            player.sendMessage(utils.translate(messageConfig.getConfig().getString("messages.no-permission").replace("{prefix}", variables.prefix)));
            return true;
        }

        if(args.length == 0) {
            messageConfig.getConfig().getStringList("admin.commands.help").forEach(message -> {
                player.sendMessage(utils.translate(message.replace("{prefix}", variables.prefix)));
            });
            return true;
        }

        switch (args[0].toLowerCase()) {

            case "add": {
                if(args.length != 2) {
                    player.sendMessage(utils.translate(messageConfig.getConfig().getString("admin.addItem.usage").replace("{prefix}", variables.prefix)));
                    return true;
                }
                String permission = args[1];

                if(player.getInventory().getItemInMainHand().getType() == null || player.getInventory().getItemInMainHand().getType().isAir()) {
                    player.sendMessage(utils.translate(messageConfig.getConfig().getString("admin.addItem.no-item-in-hand").replace("{prefix}", variables.prefix)));
                    return true;
                }

                String code = utils.generateCode();
                trashableItems shopItem = new trashableItems(
                        code,
                        utils.encodeItem(player.getInventory().getItemInMainHand()),
                        permission
                );
                Main.getInstance().db.createInformation(shopItem);
                player.sendMessage(utils.translate(messageConfig.getConfig().getString("admin.addItem.success").replace("{prefix}", variables.prefix)));


                break;
            }

            case "list": {
                listGUI.openListGUI(player);
                break;
            }

            case "reload": {
                databaseConfig.reloadConfig();
                messageConfig.reloadConfig();
                settingConfig.reloadConfig();
                player.sendMessage(utils.translate(messageConfig.getConfig().getString("admin.commands.reload.success").replace("{prefix}", variables.prefix)));
                break;
            }


        }

        return true;
    }
}
