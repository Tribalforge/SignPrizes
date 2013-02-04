/*
 * The MIT License
 *
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
package uk.co.drnaylor.moneysigns.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import uk.co.drnaylor.moneysigns.MoneySigns;
import uk.co.drnaylor.moneysigns.MoneyUser;

public class Commandmsclear implements CommandExecutor {

    // For /msid
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (args.length == 2) {
            Player player = MoneySigns.plugin.getServer().getPlayer(args[0]);
            if (player == null) {
                sender.sendMessage(ChatColor.RED + "Player " + args[0] + " is not online.");
                return true;
            }
            
            MoneyUser mu = MoneySigns.getMoneyUser(player);
            
            if (MoneySigns.plugin.checkIdentifier(args[1])) {
                mu.clearTimeout(args[1]);
                sender.sendMessage(ChatColor.GREEN + "Player " + args[0] + "'s cooldown for " + args[1] + " has been cleared.");
            }
            else {
                sender.sendMessage(ChatColor.RED + "Identifier " + args[1] + " does not exist.");
            }
        }
        else {
            printUsage(sender);
        }
        return true;
    }
    
    
   /**
    * Prints the command usage for /msclear.
    * 
    * @param sender CommandSender that needs the message!
    */
    private void printUsage(CommandSender sender) {
        sender.sendMessage(ChatColor.AQUA + "/msclear Usage");
        sender.sendMessage(ChatColor.YELLOW + "/msclear [player] [id] " + ChatColor.GREEN + "- Clear the cooldown for player [player] for the identifier <id>");
    }
}
