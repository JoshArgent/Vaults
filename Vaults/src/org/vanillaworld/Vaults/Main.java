package org.vanillaworld.Vaults;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.vanillaworld.Vaults.Exceptions.NotEnoughVaultsException;

public class Main extends JavaPlugin implements Listener {

	public void onEnable()
	{
		Config.plugin = this;
		Config.generate();
		Backend.plugin = this;
		Backend.loadData();
		for(Player p : Bukkit.getOnlinePlayers())
		{
			Backend.clearCache(p);
		}
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
				} catch (NotEnoughVaultsException e) {
					sender.sendMessage(Functions.convertColours(Config.getConfig().getString("messages.no-vault")));
				}
			}
			else if(args.length == 1)
			{
				if(Functions.isNumeric(args[0]))
				{
					// Open own player vault args[0]
					try {
						Functions.openVault((Player) sender, sender.getName(), Integer.valueOf(args[0]));
					} catch (NotEnoughVaultsException e) {
						sender.sendMessage(Functions.convertColours(Config.getConfig().getString("messages.no-vault")));
					}
				}
				else
				{
					// View someone elses first vault
					if(Functions.isAdmin((Player) sender))
					{
						if(Bukkit.getOfflinePlayer(args[0]) == null)
						{
							sender.sendMessage(ChatColor.DARK_RED + "Player doesn't exist!");
							return true;
						}
						try {
							Functions.openVault((Player) sender, args[0], 1);
						} catch (NotEnoughVaultsException e) {
							sender.sendMessage(Functions.convertColours(Config.getConfig().getString("messages.admin-view-no-vault"), args[0]));
						}
					}
					else
					{
						sender.sendMessage(Functions.convertColours(Config.getConfig().getString("messages.need-to-specify-number")));
					}
				}
			}
			else if(args.length == 2)
			{
				if(Functions.isAdmin((Player) sender))
				{
					if(Bukkit.getOfflinePlayer(args[0]) == null)
					{
						sender.sendMessage(ChatColor.DARK_RED + "Player doesn't exist!");
						return true;
					}
					if(Functions.isNumeric(args[1]))
					{
						try {
							Functions.openVault((Player) sender, args[0], Integer.valueOf(args[1]));
						} catch (NotEnoughVaultsException e) {
							sender.sendMessage(Functions.convertColours(Config.getConfig().getString("messages.admin-view-no-vault"), args[0]));
						}
					}
					else
					{
						sender.sendMessage(Functions.convertColours(Config.getConfig().getString("messages.need-to-specify-number")));
					}
				}
				else
				{
					sender.sendMessage(Functions.convertColours(Config.getConfig().getString("messages.no-permission")));
				}
			}
			return true;
		}
		return false;
	}
	
	@EventHandler
	private void inventoryClose(InventoryCloseEvent event)
	{
		Functions.inventoryClosed((Player) event.getPlayer(), event.getInventory());
	}
	
	@EventHandler
	private void playerJoin(PlayerJoinEvent event)
	{
		Backend.clearCache(event.getPlayer());
	}
	
}
