package me.desertdweller.desertscooking;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements Listener{
	private Connection connection;
	public String host, database, username, password;
	public int port;
	private String monthYear;
	@SuppressWarnings("unused")
	private BukkitTask furnaceChecker;
	
	@Override 
	public void onEnable() {
		getCommand("tastes").setExecutor(new Commands());
		getCommand("foodstats").setExecutor(new Commands());
		saveDefaultConfig();
		getConfig();
		getServer().getPluginManager().registerEvents(new FoodCrafting(),this);
		getServer().getPluginManager().registerEvents(new FoodEating(), this);
		getServer().getPluginManager().registerEvents(new Flavor(), this); 
		getServer().getPluginManager().registerEvents(new IngredientStation(), this); 
		getServer().getPluginManager().registerEvents(new Station(), this); 
		furnaceChecker = new FurnaceLighter().runTaskTimer(this,0,1200);
		mySQLSetup();
		
		SimpleDateFormat formatter = new SimpleDateFormat("MM-yyyy");  
		Date date = new Date();
		monthYear = formatter.format(date);
		System.out.println(monthYear);
	}
	
	public void mySQLSetup() {
		host = this.getConfig().getString("host");
		database = this.getConfig().getString("database");
		username = this.getConfig().getString("username");
		password = this.getConfig().getString("password");
		port = this.getConfig().getInt("port");
		
		try {
			synchronized (this){
				if(connection != null && !connection.isClosed()) {
					return;
				}
				Class.forName("com.mysql.jdbc.Driver");
				connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useSSL=false", username, password);
				Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[DesertsCooking] Connected to MySQL Database.");
				
				if(!findTable(this.getConfig().getString("tablename1"))) {
					createTable(1, this.getConfig().getString("tablename1"));
				}
				if(!findTable(this.getConfig().getString("tablename2"))) {
					createTable(2, this.getConfig().getString("tablename2"));
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public boolean findTable(String tableName) {
		ResultSet result;
        try {
        	PreparedStatement statement = (PreparedStatement) this.connection.prepareStatement("");
            result = statement.executeQuery("SHOW TABLES LIKE '"+ tableName +"';");
            if(result.next()) {
                return true;
            } else {
                return false;
            }
            
        } catch (SQLException e) {
        	Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error with finding tables like "+tableName);
            e.printStackTrace();
        }
        return false;
    }
	
	private void createTable(int tableid, String table) {
        try {
        	PreparedStatement statement = (PreparedStatement) this.connection.prepareStatement("");
        	Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Creating table: " + table);
        	String createQuery;
        	if(tableid == 1) {
	        	createQuery = "CREATE TABLE IF NOT EXISTS "+table+" (" +
	            						"  `location` TEXT NULL DEFAULT NULL ," + 
	                                    "  `timeplaced` BIGINT(20) NULL DEFAULT NULL ," + 
	                                    "  `customMaterial` TEXT NULL DEFAULT NULL ," +
	                                    "  `food` TINYINT(4) NULL DEFAULT NULL," + 
	                                    "  `saturation` FLOAT NULL DEFAULT NULL ," + 
	                                    "  `experience` INT(11) NULL DEFAULT NULL ," +
	                                    "  `mainingredients` TINYINT(4) NULL DEFAULT NULL ," + 
	                                    "  `secondaryingredients` TINYINT(4) NULL DEFAULT NULL ," + 
	                                    "  `spices` TINYINT(4) NULL DEFAULT NULL ," + 
	                                    "  `material` TEXT NULL DEFAULT NULL ," + 
	                                    "  `amount` TINYINT(4) NULL DEFAULT NULL ," + 
	                                    "  `invaliditem` BOOLEAN NULL DEFAULT NULL ," +
	                                    "  `completed` BOOLEAN NULL DEFAULT NULL ," +
	                                    "  `poisoned` BOOLEAN NULL DEFAULT NULL ," +
	                                    "  `ingredients` TEXT NULL DEFAULT NULL ," +
	                                    "  `spicyness` TINYINT(4) NULL DEFAULT NULL ," +
	                                    "  `sweetness` TINYINT(4) NULL DEFAULT NULL ," +
	                                    "  `bitterness` TINYINT(4) NULL DEFAULT NULL ," +
	                                    "  `savoryness` TINYINT(4) NULL DEFAULT NULL ," +
	                                    "  `saltyness` TINYINT(4) NULL DEFAULT NULL ," +
	                                    "  `sourness` TINYINT(4) NULL DEFAULT NULL  ," +
	                                    "  `configVersion` TEXT NULL DEFAULT NULL )";
        	}else if(tableid == 2){
        		createQuery = "CREATE TABLE IF NOT EXISTS "+table+" (" +
        								"  `uuid` VARCHAR(100) DEFAULT NULL ," +
        								"  `preference1` TINYINT(4) NULL DEFAULT NULL ," +
        								"  `preference2` TINYINT(4) NULL DEFAULT NULL )";
        	}else {
        		createQuery = "CREATE TABLE IF NOT EXISTS "+table+" (" +
        								"  `ingredient` TEXT NULL DEFAULT NULL ," +
        								"  `"+monthYear+"` INT(11) NULL '0' )";
        	}
            statement.executeUpdate(createQuery);
        } catch (SQLException e) {
        	Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error creating table " + table);
            e.printStackTrace();
        }
	}
	
	public Connection getConnection() {
		return connection;
	}
}
