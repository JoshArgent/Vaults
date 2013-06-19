package org.vanillaworld.Vaults;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Config {

	public static JavaPlugin plugin;
	
	public static FileConfiguration getConfig()
	{
		return plugin.getConfig();
	}
	
	public static void saveConfig()
	{
		File configFile = new File(plugin.getDataFolder(), "config.yml");     
		try {
			getConfig().save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void generate()
	{
		File configFile = new File(plugin.getDataFolder(), "config.yml");     
		if(!configFile.exists())
		{
			getConfig().set("version", "1.0.0");
			getConfig().set("inventory-type", "medium");
			getConfig().set("caching", true);
			getConfig().set("messages.no-vault", "&4You do not have that many vaults!");
			getConfig().set("messages.no-permission", "&4You do not have permission to do that!");
		}
		saveConfig();
	}
	
	
	
}
