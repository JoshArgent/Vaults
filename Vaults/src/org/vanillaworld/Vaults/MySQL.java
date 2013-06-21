package org.vanillaworld.Vaults;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class MySQL {
	 
    public Connection conn = null;
    public Statement stmt = null;
    public boolean dbConnSuccess = false;
   
    public String strUri = "jdbc:mysql://" + Config.getConfig().getString("backend.mysql.host") + ":" + Config.getConfig().getString("backend.mysql.port") + "/" + Config.getConfig().getString("backend.mysql.database");
    public String strUser = Config.getConfig().getString("backend.mysql.username");
    public String strPassword = Config.getConfig().getString("backend.mysql.password");
    public String strTbl = Config.getConfig().getString("backend.mysql.table");
   
    public void dbConnect() throws SQLException{
        	try {
				Class.forName("com.mysql.jdbc.Driver");
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
            conn = DriverManager.getConnection(strUri, strUser, strPassword);
            stmt = conn.createStatement();
            System.out.print("[Vaults] Successfully connected to MySQL.");
            dbConnSuccess = true;
    }
    public ResultSet dbStm(String s){
    	ResultSet results = null;
        try {
        	results = stmt.executeQuery(s);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
    
    public void executeUpdate(String s){
        try {
        	stmt.executeUpdate(s);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void checkConnection()
    {
    	try {
			if(this.conn.isClosed())
			{
				dbConnect();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
 
}
