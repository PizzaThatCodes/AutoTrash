package me.pizzalover.autotrash;

import com.github.Anon8281.universalScheduler.UniversalScheduler;
import com.github.Anon8281.universalScheduler.scheduling.schedulers.TaskScheduler;
import me.pizzalover.autotrash.commands.Commands;
import me.pizzalover.autotrash.commands.CommandsTabCompletion;
import me.pizzalover.autotrash.db.database;
import me.pizzalover.autotrash.db.model.trashableItems;
import me.pizzalover.autotrash.events.PlayerItemEvents;
import me.pizzalover.autotrash.utils.config.databaseConfig;
import me.pizzalover.autotrash.utils.config.messageConfig;
import me.pizzalover.autotrash.utils.config.settingConfig;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.xenondevs.invui.InvUI;

public final class Main extends JavaPlugin {

    private static Main instance;

    private static TaskScheduler scheduler;

    public database db = new database();

    @Override
    public void onEnable() {
        instance = this;

        // Creating the scheduler depending on the server type
        scheduler = UniversalScheduler.getScheduler(this);

        InvUI.getInstance().setPlugin(instance);

        // Updating the config files
        databaseConfig.updateConfig();
        messageConfig.updateConfig();
        settingConfig.updateConfig();

        databaseConfig.saveConfig();
        databaseConfig.reloadConfig();

        messageConfig.saveConfig();
        messageConfig.reloadConfig();

        settingConfig.saveConfig();
        settingConfig.reloadConfig();

        if(!db.initializeDatabase()) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if(!databaseConfig.configFile.exists()) {
            saveResource("database.yml", false);
        }

        if(!messageConfig.configFile.exists()) {
            saveResource("message.yml", false);
        }

        if(!settingConfig.configFile.exists()) {
            saveResource("config.yml", false);
        }

        getCommand("autotrash").setExecutor(new Commands());
        getCommand("autotrash").setTabCompleter(new CommandsTabCompletion());

        getServer().getPluginManager().registerEvents(new PlayerItemEvents(), this);


        getLogger().info("AutoTrash has been enabled!");

        getScheduler().runTaskTimerAsynchronously(() -> {
            for(Player player : getServer().getOnlinePlayers()) {
                for(trashableItems item : db.getAllItems()) {
                    if(!player.hasPermission(item.getPermission())) return;
                    if(player.getInventory().containsAtLeast(item.getItem(), 1)) {
                        ItemStack tempItem = item.getItem().clone();
                        tempItem.setAmount(999999999);
                        player.getInventory().removeItem(tempItem);
                    }
                }
            }
        }, 0, 5); // 5 seconds (20 ticks per second
    }

    @Override
    public void onDisable() {
        if(scheduler != null)
            scheduler.cancelTasks();
        getLogger().info("AutoTrash has been disabled!");
    }

    /**
     * Get the instance of the plugin
     */
    public static Main getInstance() {
        return instance;
    }

    /**
     * Get the scheduler of the plugin
     */
    public static TaskScheduler getScheduler() {
        return scheduler;
    }



}
