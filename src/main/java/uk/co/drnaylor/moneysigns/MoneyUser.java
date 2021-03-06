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

import java.io.File;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import uk.co.drnaylor.configaccessor.ConfigAccessor;

public class MoneyUser {
    
    final Player player;
    final ConfigAccessor config;

    
    
    public MoneyUser(Player a)
    {
        this.player = a;
       
        config = new ConfigAccessor(MoneySigns.plugin, "userdata/" + a.getName() + ".yml");

        if (!(new File(MoneySigns.plugin.getDataFolder().toString() + "/userdata/" + a.getName() + ".yml").exists())) {
            config.saveConfig();
        }
        
        reloadConfig();
        
    }

   /**
    * Gets whether the player is able to claim prizes.
    * 
    * @return true if the player is able to use prize signs.
    */
    public boolean canGetPrizes() {
        if (player.isOp()) {
            return true;
        }
        return player.hasPermission("moneysigns.signs.use");
    }
    
   /**
    * Reloads the config file from disk.
    */
    public void reloadConfig() {
        config.reloadConfig();
    }
    
   /**
    * Saves the config file to disk.
    */
    public void saveConfig() {
        config.saveConfig();
    }    
    
   /**
    * Checks whether the player can get a prize.
    * 
    * @param prize Prize to check for
    * @returns true if the player can get the prize, false otherwise.
    * 
    */ 
    public boolean canGetPrize(String identifier) {
        if (!canGetPrizes()) {
            return false;
        }
        
        if (!config.getConfig().isSet("prizes." + identifier)) {
            return true;
        }
        
        try {
            if (getTimeToWait(identifier) == 0) {
                return true;
            } else {
                return false;
            }
        }
        catch (IdentifierException e) {
            return false;
        }
    }   
    
   /**
    * Gets the time to wait before a prize becomes available again
    * 
    * @param identifier Identifier to check
    * @return Time in seconds to wait
    */ 
    public long getTimeToWait(String identifier) throws IdentifierException {
        if (!MoneySigns.plugin.checkIdentifier(identifier)) {
            throw new IdentifierException(identifier);
        }
        
        long timestamp = config.getConfig().getLong("prizes." + identifier, 0) / 1000;
        long timedelay = MoneySigns.plugin.getIdentifier(identifier);
       
        
        long now = new java.util.Date().getTime() / 1000;
        
        if (timestamp + timedelay < now) {
            return 0;
        } else {
            return (timestamp + timedelay) - now;
        }
    }
    
   /**
    * Claim a prize
    * 
    * @param identifier Identifier of the prize group
    * @param amount Amount to give player
    * @return true if successful
    */ 
    public boolean claimPrize(String identifier, int amount) {
        if (!canGetPrize(identifier)) {
            return false;
        }
        
        EconomyResponse resp = MoneySigns.economy.depositPlayer(this.player.getName(), amount);
        
        if (resp.transactionSuccess()) {
            config.getConfig().set("prizes." + identifier, new java.util.Date().getTime());
            config.saveConfig();
            
            player.sendMessage(ChatColor.GREEN + "You have obtained " + MoneySigns.economy.format(resp.amount) + ". New balance: " + MoneySigns.economy.format(resp.balance));
            
        }
        return resp.transactionSuccess();
    }
    
   /**
    * Checks to see if the player can create MoneyPrize signs.
    * 
    * @returns Whether the player can create MoneyPrize signs
    */ 
    public boolean canCreateSign() {
        if (player.isOp()) {
            return true;
        }
        return player.hasPermission("moneysigns.signs.create");
    }
    
   /**
    * Checks to see if the player can remove MoneyPrize signs.
    * 
    * @returns Whether the player can remove MoneyPrize signs
    */  
    public boolean canRemoveSign() {
        if (player.isOp()) {
            return true;
        }
        return player.hasPermission("moneysigns.signs.remove");
    }
    
   /**
    * Removes the player's cooldown for the selected identifier, allowing them
    * to get the prize immediately. This is a one time action, and the timeout
    * will kick in again next time the player claims a prize with this 
    * identifier.
    * 
    * @param identifier Identifier to clear the cooldown.
    */
    public void clearTimeout(String identifier) {
            config.getConfig().set("prizes." + identifier, null);
            config.saveConfig();
    }
}
