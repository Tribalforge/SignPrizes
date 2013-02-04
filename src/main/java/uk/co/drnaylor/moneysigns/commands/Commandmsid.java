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
package uk.co.drnaylor.moneysigns.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import uk.co.drnaylor.moneysigns.MoneySigns;
import uk.co.drnaylor.moneysigns.Util;

public class Commandmsid implements CommandExecutor {

    // For /msid
    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args) {
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("get")) {
                if (MoneySigns.plugin.getConfig().isLong("timeouts." + args[1].toLowerCase())) {
                    sender.sendMessage(ChatColor.GREEN + "Identifier: " + args[1].toLowerCase() + " - Timeout: " + Util.toDuration(MoneySigns.plugin.getConfig().getLong("timeouts." + args[1].toLowerCase())));
                }
                else {
                    sender.sendMessage(ChatColor.RED + "Identifier " + args[1].toLowerCase() + " does not exist!");
                }
                return true;
            }
            else if (args[0].equalsIgnoreCase("remove")) {
                if (MoneySigns.plugin.getConfig().isSet("timeouts." + args[1].toLowerCase())) {
                    MoneySigns.plugin.removeIdentifier(args[1].toLowerCase());  
                    sender.sendMessage(ChatColor.GREEN + "Identifier " + args[1].toLowerCase() + " removed");
                }
                else {
                    sender.sendMessage(ChatColor.RED + "Identifier " + args[1].toLowerCase() + " does not exist!");
                }
                return true;
            }
            printUsage(sender);
            return true;
        }
        else if (args.length == 3 && args[0].equalsIgnoreCase("set")) {
            long timeout;
            try {
                timeout = Long.valueOf(args[2]);
            }
            catch (NumberFormatException e) {
                // Invalid number - throw it out
                sender.sendMessage(ChatColor.RED + "The timeout must be a number (in seconds)");
                return true;
            }
            MoneySigns.plugin.setIdentifier(args[1].toLowerCase(), timeout);  
            sender.sendMessage(ChatColor.GREEN + "Identifier " + args[1].toLowerCase() + " has been created with a timeout of " + Util.toDuration(timeout));
            MoneySigns.plugin.saveConfig();
            return true;
        }
        else {
            printUsage(sender);
            return true;
        }
    }
    
   /**
    * Prints the command usage for /msid.
    * 
    * @param sender CommandSender that needs the message!
    */
    private void printUsage(CommandSender sender) {
        sender.sendMessage(ChatColor.AQUA + "/msid Usage");
        sender.sendMessage(ChatColor.YELLOW + "/msid get <id> " + ChatColor.GREEN + "- Get timeout for the identifier <id>");
        sender.sendMessage(ChatColor.YELLOW + "/msid set <id> <timeout>" + ChatColor.GREEN + "- Set timeout (in seconds) for the identifier <id>");
        sender.sendMessage(ChatColor.YELLOW + "/msid remove <id>" + ChatColor.GREEN + "- Remove identifier <id>");
    }
    
}