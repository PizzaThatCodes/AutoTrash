package me.pizzalover.autotrash.commands;

import me.pizzalover.autotrash.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommandsTabCompletion implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        Player p = (Player) commandSender;

        if(args.length == 1) {
            List<String> list = new ArrayList<>();
            if(p.hasPermission(Main.getInstance().getConfig().getString("commands.admin.permission"))) {
                list.add("add");
                list.add("list");
                list.add("reload");
            }
            return list;
        }

        return null;
    }
}
