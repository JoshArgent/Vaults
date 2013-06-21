package org.vanillaworld.Vaults;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Vault {
	
	
	public int rows;
	private Inventory inv;
	public int id;
	public String owner;
	
	Vault(int rows, String owner, int id)
	{
		this.rows = rows;
		this.owner = owner;
		this.id = id;
		if(9 * rows > 54 || rows <= 0)
		{
			rows = 3;
		}
		inv = Bukkit.getServer().createInventory(null, rows * 9, "Vault #" + id);
	}
	
	public String toString()
	{
		return SerializationUtil.saveInventory(inv);
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
		inv.addItem(items);
	}
	
	public Inventory getInventory()
	{
		return inv;
	}
	
	public void viewInventory(Player viewer)
	{
		viewer.openInventory(getInventory());
		
	}

}
