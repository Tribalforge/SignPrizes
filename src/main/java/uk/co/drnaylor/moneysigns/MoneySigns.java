/*
 * Copyright 2013 Dr Daniel R Naylor.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package uk.co.drnaylor.moneysigns;

import java.util.HashMap;
import java.util.Map;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import uk.co.drnaylor.moneysigns.commands.Commandmsclear;
import uk.co.drnaylor.moneysigns.commands.Commandmsid;

public class MoneySigns extends JavaPlugin
{
    private static Map<Player, MoneyUser> users;
    
    public static MoneySigns plugin;
    public static Permission permission = null;
    public static Economy economy = null;
    
            
    PlayerEventHandler eventHandler;
    Commandmsid mainCE;
    Commandmsclear clearCE;
	CommandSignPrizes spCE;
    
    @Override
    public void onEnable() {
        MoneySigns.plugin = this;
        if (users == null) {
            users = new HashMap<Player, MoneyUser>() {};
        }
        setupPermissions();
        if (!setupEconomy()) {
			this.getLogger().info("[SignPrizes] No economy plugin has been found!");
			this.getLogger().info("[SignPrizes] Any money in a prize set will not be awarded.");
			this.getLogger().info("[SignPrizes] Item awards in a prize set are still functional.");
        }

        eventHandler = new PlayerEventHandler();
        mainCE = new Commandmsid();
        clearCE = new Commandmsclear();
		// mainCE and clearCE will be replaced by spCE and spclearCE.
		spCE = new CommandSignPrizes();
        
        getCommand("msid").setExecutor(mainCE);
        getCommand("msclear").setExecutor(clearCE);
		getCommand("signprizes").setExecutor(spCE);
        getServer().getPluginManager().registerEvents(eventHandler, this);
        saveDefaultConfig();
        for (Player p : this.getServer().getOnlinePlayers()) {
            if (!users.containsKey(p)) {
                MoneyUser mu = new MoneyUser(p);
                users.put(p, mu);
            }
        }
    }
    
    @Override
    public void onDisable() {
        for (MoneyUser u : users.values()) {
            u.saveConfig();
        }
        users.clear();
        saveConfig();
    }
    
   /**
    * Get the MoneyUser associated with the player.
    * @param player Player to get the MoneyUser object for.
    * @return MoneyUser object for the player
    */
    public static MoneyUser getMoneyUser(Player player) {
        return users.get(player);
    }
    
   /**
    * Add player to MoneyUsers
    * @param player Player to add
    */ 
    static void addMoneyUser(Player player) {
        users.put(player, new MoneyUser(player));
    }
    
   /**
    * Remove player from MoneyUsers
    * @param player Player to remove
    */ 
    static void removeMoneyUser(Player player) {
        users.remove(player);
    }
    
    private boolean setupPermissions()
    {
        RegisteredServiceProvider<Permission> permissionProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.permission.Permission.class);
        if (permissionProvider != null) {
            permission = permissionProvider.getProvider();
        }
        return (permission != null);
    }

    private boolean setupEconomy()
    {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }

        return (economy != null);
    }
        
   /**
    * Checks to see if an identifier exists.
    * 
    * @param id Identifier to check
    * @return true if it exists
    */
    public boolean checkIdentifier(String id) {
	return (this.getConfig().isSet("prizes." + id));
    }
    
   /**
    * Checks to see if an identifier and set exist.
    * 
    * @param id Identifier to check
    * @param set The set name to check
    */
    public boolean checkIdentifier(String id, String set) {
	return (this.getConfig().isSet("prizes." + id + ".sets." + set));
    }
    
   /**
    * Gets an identifier's timeout
    * 
    * @param id Identifier
    * @returns Timeout in seconds
    */ 
    public long getIdentifier(String id) {
        return getConfig().getLong("prizes." + id);
    } 
    
   /**
    * Sets an identifier with a timeout
    * 
    * @param id Identifier
    * @param timeout Timeout in seconds
    */ 
    public void setIdentifier(String id, long timeout) {
        getConfig().set("prizes." + id + ".timeout", timeout);
        saveConfig();
    }
    
    /**
     * Adds or modifies a set for a prize identifier.
     * Still under development.
     */
	public void setPrizeSet(String id, String set/*, parameter about item and money information*/) {
		
	}
    
    /**
     * Removes an identifier.
     * 
     * @param id Identifier
     */ 
     public void removeIdentifier(String id) {
         getConfig().set("prizes." + id, null);
         saveConfig();
     }
    
    /**
     * Removes a specific set from a prize.
     * 
     * @param id The prize name
     * @param set The set name
     */
     public void removeSet(String id, String set) {
     	getConfig().set("prizes." + id + ".sets." + set, null);
     	saveConfig();
     }
}
