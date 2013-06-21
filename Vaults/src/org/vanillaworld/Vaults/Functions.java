package org.vanillaworld.Vaults;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
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
			if(!p.hasPermission("vaults." + id) && !p.isOp())
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
			vault.setInventory(inventory);
			Backend.savePlayerVault(vault);
			viewing.remove(player);
		}
	}

}
