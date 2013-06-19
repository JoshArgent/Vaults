package org.vanillaworld.Vaults;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.vanillaworld.Vaults.Exceptions.NotEnoughVaultsException;
import org.vanillaworld.Vaults.Exceptions.VaultNotFoundException;

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
				try {
					Functions.openVault((Player) sender, sender.getName(), 1);
				} catch (VaultNotFoundException e) {
					sender.sendMessage(ChatColor.RED + "You're vault is empty!");
				} catch (NotEnoughVaultsException e) {
					sender.sendMessage(ChatColor.RED + "You are not allowed to have " + e.vaultID + " vaults!");
				}
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
