package org.vanillaworld.Vaults;

import java.io.File;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Backend {
	
	public static Backends BackendType;
	private static MySQL sqlConnection;
	private static File VaultsFile;
	private static FileConfiguration VaultsConfig;
	public static JavaPlugin plugin;
	private static List<String> sqlToExecute = new ArrayList<String>();
	private static List<Vault> ymlToUpdate = new ArrayList<Vault>();
	
	public static void loadData()
	{
		if(Config.getConfig().getString("backend.type").equalsIgnoreCase("mysql"))
		{
			BackendType = Backends.MySQL;
		}
		else
		{
			BackendType = Backends.File;
		}
		if(BackendType.equals(Backends.MySQL))
		{
			sqlConnection = new MySQL();
			try {
				sqlConnection.dbConnect();
				DatabaseMetaData metadata = sqlConnection.conn.getMetaData();
				ResultSet resultSet;
				resultSet = metadata.getTables(null, null, Config.getConfig().getString("backend.mysql.table"), null);
				if(!resultSet.next())
				{
					 // Create the table
					System.out.print("[Vaults] Creating MySQL table.");
					sqlConnection.executeUpdate("CREATE TABLE " + Config.getConfig().getString("backend.mysql.table") + " (Player CHAR(16), InventoryID INT, Inventory TEXT);");
				}
				System.out.print("[Vaults] Using MySQL Backend.");
			} catch (SQLException e) {
				BackendType = Backends.File;
				System.out.print("[Vaults] " + e.getMessage());
			}
		}
		if(BackendType.equals(Backends.File))
		{
			VaultsFile = new File("plugins/Vaults", Config.getConfig().getString("backend.file.name"));     
			VaultsConfig = YamlConfiguration.loadConfiguration(VaultsFile);
			if(!VaultsFile.exists())
			{
				try {
					VaultsFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			System.out.print("[Vaults] Using File Backend.");
		}
		@SuppressWarnings("unused")
		BukkitTask task = new BackendUpdater().runTaskTimerAsynchronously(plugin, 0, Config.getConfig().getInt("backend.update-period"));
	}
	
	public static enum Backends
	{
		MySQL, File
	}
	
	static class BackendUpdater extends BukkitRunnable {
	    public void run() 
	    {
	        if(BackendType.equals(Backends.MySQL))
	        {
	        	if(sqlToExecute.size() > 0)
	        	{
	        		List<String> clone = sqlToExecute;
	        		for (String sql : clone)
	        		{
	        			sqlConnection.executeUpdate(sql);
	        		}
	        		sqlToExecute.clear();
	        	}
	        }
	        else
	        {
	        	if(ymlToUpdate.size() > 0)
	        	{
	        		List<Vault> clone = ymlToUpdate;
	        		for (Vault vault : clone)
	        		{
	        			VaultsConfig.set(vault.owner + "." + vault.id, vault.toFileConfiguration());
	        		}
	        		try {
						VaultsConfig.save(VaultsFile);
					} catch (IOException e) {
						e.printStackTrace();
					}
	        		ymlToUpdate.clear();
	        	}
	        }
	    }
	}

	public static Vault getPlayerVault(String player, int id)
	{
		Vault vault = new Vault(Config.getConfig().getInt("vault-rows"), player, id);
		if(BackendType.equals(Backends.MySQL))
		{
			ResultSet results = sqlConnection.dbStm("SELECT * FROM " + Config.getConfig().getString("backend.mysql.table") + " WHERE Player='" + player + "' AND InventoryID='" + id + "'");
			try {
				while (results.next()) 
				{
					vault.setInventoryFromString(StringConvertion.numericToString(results.getString(3)));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		else
		{
			try {
				ItemStack items[] = SerializationUtil.loadInventory(VaultsConfig.getConfigurationSection(player + "." + id));
				if(items != null)
				{
					for (ItemStack item : items)
					{
						if(item != null)
						{
							vault.getInventory().addItem(item);
						}
					}
				}			
			} catch (InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
		return vault;
	}
	
	public static void savePlayerVault(Vault vault)
	{
		if(BackendType.equals(Backends.MySQL))
		{
			String vaultText = StringConvertion.stringToNumeric(vault.toString());
			sqlToExecute.add("DELETE FROM " + Config.getConfig().getString("backend.mysql.table") + " WHERE Player='" + vault.owner + "' AND InventoryID='" + vault.id + "'");
			sqlToExecute.add("INSERT INTO " + Config.getConfig().getString("backend.mysql.table") + " (Player, InventoryID, Inventory) VALUES('" + vault.owner + "', '" + vault.id + "', '" + vaultText + "')");
		}
		else
		{
			ymlToUpdate.add(vault);
		}
	}
	
	private static void cachePlayerVault(Vault vault)
	{
		if(!Config.getConfig().getBoolean("caching"))
		{
			return;
		}
		String name = vault.owner;
		Player player = Bukkit.getPlayerExact(name);
		if(player != null)
		{
			MetadataValue value = new FixedMetadataValue(plugin, vault.toString());
			player.setMetadata("vaults_" + vault.id, value);
		}
	}
	
	private static boolean hasCachedVault(String player, int id)
	{
		if(!Config.getConfig().getBoolean("caching"))
		{
			return false;
		}
		if(Bukkit.getPlayerExact(player) != null)
		{
			if(Bukkit.getPlayerExact(player).getMetadata("vaults_" + id).size() == 0)
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		return false;
	}
	
	private static Vault getCachedVault(String player, int id)
	{
		Vault vault = new Vault(Config.getConfig().getInt("vault-rows"), player, id);
		if(!Config.getConfig().getBoolean("caching"))
		{
			return vault;
		}
		if(Bukkit.getPlayerExact(player) != null)
		{
			List<MetadataValue> values = Bukkit.getPlayerExact(player).getMetadata("vaults_" + id);
			for(MetadataValue value : values)
			{
				vault.setInventoryFromString(value.asString());
			}
		}
		return vault;
	}
	

}
