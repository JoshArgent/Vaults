package org.vanillaworld.Vaults;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.vanillaworld.Vaults.Exceptions.NotEnoughVaultsException;

public class Functions {
	
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
		vault.viewInventory(viewer);
	}
	

}
