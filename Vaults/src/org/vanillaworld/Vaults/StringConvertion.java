package org.vanillaworld.Vaults;

import java.io.UnsupportedEncodingException;

public class StringConvertion {
	
	public static String stringToNumeric(String text)
	{
		if(text == null)
		{
			return "";
		}
		try {
			byte[] byteArray = text.getBytes("UTF-16");
			String encoded = "";
			for (Byte b : byteArray)
			{
				encoded += b.intValue() + ",";
			}
			encoded = Compression.compress(encoded);
			return encoded;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public static String numericToString(String text)
	{
		if(text.equalsIgnoreCase(""))
		{
			return "";
		}
			text = Compression.decompress(text);
			String[] split = text.split(",");
			byte[] byteArray = new byte[split.length];
			int num = 0;
			for (String s : split)
			{
				if(!s.equalsIgnoreCase(""))
				{
				int intvalue = Integer.valueOf(s);
				byte b = (byte) intvalue;
				byteArray[num] = b;
				num += 1;
				}
			}
			String decoded;
			try {
				decoded = new String(byteArray, "UTF-16");			
				return decoded;
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "";
	}

}
