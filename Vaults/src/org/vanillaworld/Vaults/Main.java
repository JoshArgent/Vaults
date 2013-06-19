package org.vanillaworld.Vaults;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	public void onEnable()
	{
		Config.plugin = this;
		Config.generate();
		
		this.getServer().getPluginManager().registerEvents(this, this);
		System.out.print("[Vaults] Vaults enabled!");
	}
	
	public void onDisable()
	{
		
	}
	
	
}
