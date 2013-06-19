package org.vanillaworld.Vaults;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;

public class Vault {
	
	public enum VaultType
	{
		Small, Medium, Large
	}
	
	public static VaultType stringToVaultType(String vaultType)
	{
		VaultType type = VaultType.Medium;
		if(vaultType.equalsIgnoreCase("small"))
		{
			type = VaultType.Small;
		}
		if(vaultType.equalsIgnoreCase("medium"))
		{
			type = VaultType.Medium;
		}
		if(vaultType.equalsIgnoreCase("large"))
		{
			type = VaultType.Large;
		}
		return type;
	}
	
	public VaultType type;
	private Inventory smallInv;
	private DoubleChestInventory bigInv;
	public int id;
	public String owner;
	
	Vault(VaultType type, String owner, int id)
	{
		this.type = type;
		this.owner = owner;
		this.id = id;
		
		smallInv = Bukkit.getServer().createInventory(null, InventoryType.CHEST);
		if(type.equals(VaultType.Small))
		{
			smallInv = Bukkit.getServer().createInventory(null, InventoryType.HOPPER);
		}
		else if(type.equals(VaultType.Large))
		{
			DoubleChestInventory bigInv = (DoubleChestInventory) smallInv;
		}
		
	}
	
	public String toString()
	{
		if(type.equals(VaultType.Large))
		{
			return InventorySerializer.InventoryToString(bigInv);
		}
		else
		{
			return InventorySerializer.InventoryToString(smallInv);
		}
	
	}
	
	public Inventory getInventory()
	{
		if(type.equals(VaultType.Large))
		{
			return bigInv;
		}
		else
		{
			return smallInv;
		}
	}

}
