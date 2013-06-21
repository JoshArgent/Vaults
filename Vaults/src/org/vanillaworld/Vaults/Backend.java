package org.vanillaworld.Vaults;

import java.io.File;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
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
	private static Map<String, Integer> ymlToUpdate = new HashMap<String, Integer>();
	
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
				// TODO Auto-generated catch block
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
					// TODO Auto-generated catch block
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
	        		for (String sql : sqlToExecute)
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
	        		for (String player : ymlToUpdate.keySet())
	        		{
	        			VaultsConfig.set(player, ymlToUpdate.get(player));
	        		}
	        		try {
	        			VaultsConfig.save(VaultsFile);
	    			} catch (IOException e) {
	    				// TODO Auto-generated catch block
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
					vault.setInventoryFromString(results.getString(3));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
		return vault;
	}
	
	public static void savePlayerVault(Vault vault)
	{
		
	}
	
	
	

}
