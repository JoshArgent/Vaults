package org.vanillaworld.Vaults;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
			bigInv = (DoubleChestInventory) smallInv;
		}
		
	}
	
	public String toString()
	{
		if(type.equals(VaultType.Large))
		{
			return SerializationUtil.saveInventory(bigInv);
		}
		else
		{
			return SerializationUtil.saveInventory(smallInv);
		}
	}
	
	public void setInventoryFromString(String text)
	{
		ItemStack items[];
		try {
			items = SerializationUtil.loadInventory(text);
		} catch (InvalidConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		if(items == null)
		{
			return;
		}
		if(type.equals(VaultType.Large))
		{
			bigInv.addItem(items);
		}
		else
		{
			smallInv.addItem(items);
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
