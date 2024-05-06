package me.pizzalover.autotrash.db;

import me.pizzalover.autotrash.Main;
import me.pizzalover.autotrash.db.model.trashableItems;
import me.pizzalover.autotrash.utils.config.databaseConfig;
import org.bukkit.Bukkit;

import java.sql.*;
import java.util.ArrayList;

public class database {

    private Connection connection;

    /**
     * Get a connection to a database
     * @return the connection
     * @throws SQLException
     */
    public Connection getConnection() {

        try {
        if (connection != null) {
            if(!connection.isClosed() && connection.isValid(500)) {
                return connection;
            }
        }

        String schema = "schemas.trashable-items.";
        if(
                databaseConfig.getConfig().getString(schema + "username").equalsIgnoreCase("root")
                && databaseConfig.getConfig().getString(schema + "password").equalsIgnoreCase("password")
                && databaseConfig.getConfig().getString(schema + "host").equalsIgnoreCase("localhost")
                && databaseConfig.getConfig().getString(schema + "port").equalsIgnoreCase("3306")) {
            Bukkit.getServer().getLogger().info("Please configure the schema 1 in the config.yml!");
            return null;
        }

        //Try to connect to MySQL
        String username = databaseConfig.getConfig().getString("schemas.trashable-items.username");
        String password = databaseConfig.getConfig().getString("schemas.trashable-items.password");
        String host = databaseConfig.getConfig().getString("schemas.trashable-items.host");
        String port = databaseConfig.getConfig().getString("schemas.trashable-items.port");
        String database = databaseConfig.getConfig().getString("schemas.trashable-items.database");
        String url = "jdbc:mysql://" + host +":"+port+"/"+database + "?autoReconnect=true";

        Connection connection = DriverManager.getConnection(url, username, password);

        this.connection = connection;

        Bukkit.getServer().getLogger().info("Connected to database.");

        return connection;
        } catch (SQLException e) {
            Bukkit.getServer().getLogger().info("Cannot connect to database, maybe the configuration is wrong");
            Main.getInstance().getServer().getPluginManager().disablePlugin(Main.getInstance());
        }
        return null;
    }

    /**
     * Initialize the database
     * @throws SQLException
     */
    public boolean initializeDatabase() {
        try {
            if(getConnection() == null) {
                Bukkit.getServer().getLogger().info("Cannot initialize database, maybe the configuration is wrong");
                return false;
            }
            Statement statement = getConnection().createStatement();

            //Create the player_stats table
            String sql = "CREATE TABLE IF NOT EXISTS items ( itemID varchar(35) primary key, itemstack text, permission text )";

            statement.execute(sql);

            statement.close();
        } catch (SQLException e) {
            Bukkit.getServer().getLogger().info("Cannot initialize database, maybe the configuration is wrong");
            return false;
        }
        return true;
    }


    /**
     * Create a new item in the database
     * @param trashableItemsModel the item model
     * @throws SQLException
     */
    public void createInformation(trashableItems trashableItemsModel) {

        try {
            PreparedStatement statement = getConnection()
                    .prepareStatement("INSERT INTO items(itemID, itemstack, permission) VALUES (?, ?, ?)");
            statement.setString(1, trashableItemsModel.getItemUuid());
            statement.setString(2, trashableItemsModel.encodeItem(trashableItemsModel.getItem()));
            statement.setString(3, trashableItemsModel.getPermission());

            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Cannot create item in the database");
            e.printStackTrace();
        }
    }

    /**
     * Update the item information
     * @param trashableItemsModel the item model
     * @throws SQLException
     */
    public void updateInformation(trashableItems trashableItemsModel) {

        try {
            PreparedStatement statement = getConnection().prepareStatement("UPDATE items SET itemstack = ?, permission = ? WHERE itemID = ?");
            statement.setString(1, trashableItemsModel.encodeItem(trashableItemsModel.getItem()));
            statement.setString(2, trashableItemsModel.getPermission());
            statement.setString(3, trashableItemsModel.getItemUuid());

            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Cannot update item in the database");
            e.printStackTrace();
        }
    }

    /**
     * Delete the item information
     * @param trashableItemsModel the item model
     * @throws SQLException
     */
    public void deleteInformation(trashableItems trashableItemsModel) {

        try {
            PreparedStatement statement = getConnection().prepareStatement("DELETE FROM items WHERE itemID = ?");
            statement.setString(1, trashableItemsModel.getItemUuid());

            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            Bukkit.getLogger().info("Cannot delete item in the database");
            e.printStackTrace();
        }
    }

    /**
     * Find the item information by the item code
     * @param code the item code
     * @return the item model
     * @throws SQLException
     */
    public trashableItems findItemInformationByCode(String code) {

        try {
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM items WHERE itemID = ?");
            statement.setString(1, code);

            ResultSet resultSet = statement.executeQuery();

            trashableItems trashableItems;

            if (resultSet.next()) {

                trashableItems = new trashableItems(
                        resultSet.getString("itemID"),
                        resultSet.getString("itemstack"),
                        resultSet.getString("permission")
                );

                statement.close();

                return trashableItems;
            }

            statement.close();

        } catch (SQLException e) {
            Bukkit.getLogger().info("An error occurred while trying to find the item in the database");
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Gets all the items inside the database
     * @return the entire list of items!
     * @throws SQLException
     */
    public ArrayList<trashableItems> getAllItems() {
        try {
            ArrayList<trashableItems> shopItems = new ArrayList<>();
            PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM items");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                shopItems.add(new trashableItems(
                        resultSet.getString("itemID"),
                        resultSet.getString("itemstack"),
                        resultSet.getString("permission")
                ));
            }
            statement.close();
            return shopItems;
        } catch (SQLException e) {
            Bukkit.getLogger().info("An error occurred while trying to get all the items in the database");
            e.printStackTrace();
        }
        return null;
    }



}
