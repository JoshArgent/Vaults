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
	
	public static void openVault(Player viewer, String name, int id) throws NotEnoughVaultsException
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
				throw new Exceptions.NotEnoughVaultsException(name, id);
			}
		}
		Vault vault = Backend.getPlayerVault(name, id);
		viewer.openInventory(vault.getInventory());
		viewing.put(viewer, vault);
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
			Backend.savePlayerVault(vault);
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
	
	public static boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");
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
