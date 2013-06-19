package org.vanillaworld.Vaults;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
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
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
	{
		if(cmd.getName().equalsIgnoreCase("vault"))
		{
			if(args.length == 0)
			{
				
			}
			else if(args.length == 1)
			{
				
			}
			else if(args.length == 2)
			{
				
			}
			return true;
		}
		return false;
	}
	
}
