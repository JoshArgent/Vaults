package org.vanillaworld.Vaults;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.vanillaworld.Vaults.Exceptions.NotEnoughVaultsException;
import org.vanillaworld.Vaults.Exceptions.VaultNotFoundException;

public class Functions {
	
	public static void openVault(Player viewer, String name, int id) throws VaultNotFoundException, NotEnoughVaultsException
	{
		if(Bukkit.getPlayerExact(name) != null)
		{
			Player p = Bukkit.getPlayerExact(name);
			if(!p.hasPermission("vaults." + id) && !p.isOp())
			{
				throw new Exceptions.NotEnoughVaultsException(name, id);
			}
		}
		
		
		
		
		
		
		
		/*
		
		if(id == 1)
		{
		throw new Exceptions.VaultNotFoundException(name, id);
		}
		throw new Exceptions.NotEnoughVaultsException(name, id); */
	}

}
