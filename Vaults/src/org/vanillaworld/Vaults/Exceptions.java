package org.vanillaworld.Vaults;

public class Exceptions {
	
	@SuppressWarnings("serial")
	public static class NotEnoughVaultsException extends Exception
	{
		public String player;
		public int vaultID;
		public NotEnoughVaultsException(String player, int vaultID)
	    {
	       this.player = player;
	       this.vaultID = vaultID;
	    }
	}
	
	@SuppressWarnings("serial")
	public static class VaultNotFoundException extends Exception
	{
		public String player;
		public int vaultID;
		public VaultNotFoundException(String player, int vaultID)
	    {
	       this.player = player;
	       this.vaultID = vaultID;
	    }
	}

}
