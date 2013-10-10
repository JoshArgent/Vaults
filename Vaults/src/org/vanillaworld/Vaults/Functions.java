package org.vanillaworld.Vaults;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.vanillaworld.Vaults.Exceptions.NotEnoughVaultsException;

public class Functions {
	
	private static Map<Player, Vault> viewing = new HashMap<Player, Vault>();
	
	
	//Fixed lag issue, could be cleaned up
	public static void openVault(final Player viewer, final String name, final int id) throws NotEnoughVaultsException
	{
		Bukkit.getScheduler().runTaskAsynchronously(Config.Plugin, new Runnable()
		{
			@Override
			public void run() 
			{
				if(Bukkit.getPlayerExact(name) != null)
				{
					Player p = Bukkit.getPlayerExact(name);
					boolean hasPerm = false;
					int num = id;
					
					while (num != 1000)
					{
						if(p.hasPermission("vaults." + num))
						{
							hasPerm = true;
							break;
						}
						num += 1;
					}
					
					if(!hasPerm && !p.isOp())
					{
						try 
						{
							throw new Exceptions.NotEnoughVaultsException(name, id);
						} catch (NotEnoughVaultsException e) 
						{
							e.printStackTrace();
						}
					}
				}
				
				final Vault vault = Backend.getPlayerVault(name, id);
				Bukkit.getScheduler().runTask(Config.Plugin, new Runnable()
				{
					@Override
					public void run() 
					{
						viewer.openInventory(vault.getInventory());
						viewing.put(viewer, vault);
					}
				});
			}
		});
	}
	
	public static void inventoryClosed(Player player, Inventory inventory)
	{
		if(viewing.containsKey(player))
		{
			Vault vault = viewing.get(player);
			
			if(vault.getInventory().equals(inventory))
			{
				viewing.remove(player);
				return; // No changes
			}
			
			vault.setInventory(inventory);
			
			final Vault vault1 = vault;
			Bukkit.getScheduler().runTaskAsynchronously(Config.Plugin, new Runnable()
			{
				@Override
				public void run() 
				{
					Backend.savePlayerVault(vault1);
				}
			});
			
			viewing.remove(player);
		}
	}
	
	
	public static String convertColours(String t1)
	{
		String t2 = ChatColor.translateAlternateColorCodes("&".toCharArray()[0], t1);
		return t2;
	}
	
	public static String convertColours(String t1, String player)
	{
		String t2 = ChatColor.translateAlternateColorCodes("&".toCharArray()[0], t1);
		t2 = t2.replace("<player>", player);
		return t2;
	}
	
	
	public static boolean shouldOpenVault(int pageNumber)
	{
		//Added a range of 1 to 100 (end points inclusive)
		return (pageNumber >= 1) && (pageNumber <= 100);
	}
	
	public static boolean isNumeric(String str)
	{
		//Fixes the negative number and using large numbers to crash servers
		boolean match = str.matches("^(?!^0)\\d{1,9}$");
		
		if(match)
		{
			int i = Integer.parseInt(str);
			match = match && shouldOpenVault(i);
		}
		
		return match;
	}
	
	public static boolean isAdmin(Player p)
	{
		if(p.hasPermission("vaults.admin") || p.isOp())
		{
			return true;
		}
		else
		{
			return false;
		}
	}

}
