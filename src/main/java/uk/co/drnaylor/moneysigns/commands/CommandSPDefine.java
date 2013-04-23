package uk.co.drnaylor.moneysigns.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import uk.co.drnaylor.moneysigns.MoneySigns;
import uk.co.drnaylor.moneysigns.Util;
import org.bukkit.ChatColor;

/**
 * Handles the /spdefine command.
 *
 * Syntax:
 * /spdefine ID <id> <timeout>
 * /spdefine set <id> <set> items:<set info...>
 * /spdefine set <id> <set> money:<money>
 * 
 * Aliases: /signprizesdefine, /spdef, /signprizesdef
 * 
 * Permission: signprizes.commands.spdefine
 */
public class CommandSPDefine implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if ((!(sender instanceof Player)) || sender.isOp() || sender.hasPermission("signprizes.commands.spdefine")) {
			
			if (args.length == 0) { // If they provide no arguments whatsoever
				sender.sendMessage(ChatColor.YELLOW + "Command syntax for " + ChatColor.GREEN + "/spdefine" + ChatColor.YELLOW + ":");
				sender.sendMessage(ChatColor.WHITE + "/spdefine ID <ID name> <timeout>");
				sender.sendMessage(ChatColor.WHITE + "/spdefine set <ID name> <set name> <items:(...)|money:(value)>");
				return true;
			}
			
			if (args.length == 1) { // If the command given is "/spdefine blah"
				if (args[0].equalsIgnoreCase("ID")) {
					sender.sendMessage(ChatColor.YELLOW + "Please provide an ID name and timeout in seconds to make!");
					sender.sendMessage(ChatColor.YELLOW + "/spdefine ID " + ChatColor.RED + "<ID name> <timeout>");
					return true;
				} else if (args[0].equalsIgnoreCase("set")) {
					sender.sendMessage(ChatColor.YELLOW + "Please provide some more information!");
					sender.sendMessage(ChatColor.YELLOW + "/spdefine set " + ChatColor.RED + "<ID name> <set name> <items:(...)|money:(value)>");
					return true;
				} else {
					sender.sendMessage(ChatColor.YELLOW + "\"" + ChatColor.RED + args[0] + ChatColor.YELLOW + "\" isn't an acceptable argument!");
					sender.sendMessage(ChatColor.YELLOW + "Please use \"ID\" or \"set\"!");
					return true;
				}
			}
			
			if (args.length == 2) { // If they gave something like "/spdefine ID id" - still too short!
				if (args[0].equalsIgnoreCase("ID")) {
					sender.sendMessage(ChatColor.YELLOW + "Please provide a timeout in seconds!");
					sender.sendMessage(ChatColor.YELLOW + "/spdefine ID <ID name>" + ChatColor.RED + " <timeout>");
					return true;
					
				} else if (args[0].equalsIgnoreCase("set")) {
					if (!(MoneySigns.plugin.getConfig().isSet("prizes." + args[1].toLowerCase()))) {
						sender.sendMessage(ChatColor.YELLOW + "The prize ID \"" + ChatColor.RED + args[1] + ChatColor.YELLOW + "\" doesn't exist!");
						sender.sendMessage(ChatColor.YELLOW + "Create it with \"/spdefine ID " + args[1] + " <timeout>\"");
						return true;
					} else {
						sender.sendMessage(ChatColor.YELLOW + "Please provide some more information!");
						sender.sendMessage(ChatColor.YELLOW + "/spdefine set " + ChatColor.RED + "<ID name> <set name> <items:(...)|money:(value)>");
						return true;
					}
					
				} else {
					sender.sendMessage(ChatColor.YELLOW + "\"" + ChatColor.RED + args[0] + ChatColor.YELLOW + "\" isn't an acceptable argument!");
					sender.sendMessage(ChatColor.YELLOW + "Please use \"ID\" or \"set\"!");
					return true;
				}
			}
			
			if (args.length == 3) { // This is acceptable for defining an ID, but not a set!
				if (args[0].equalsIgnoreCase("ID")) { // They have all the arguments necessary for making a set!
					long timeout;
					try {
						timeout = Long.valueOf(args[2]);
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.YELLOW + "\"" + ChatColor.RED + args[2] + ChatColor.YELLOW + "\" isn't a number!");
						sender.sendMessage(ChatColor.YELLOW + "Please provide a number for the prize timeout, in seconds.");
						return true;
					}
					
					MoneySigns.plugin.setIdentifier(args[1].toLowerCase(), timeout);
					MoneySigns.plugin.saveConfig();
					sender.sendMessage(ChatColor.GREEN + "A prize identifier \"" + ChatColor.YELLOW + args[1] + ChatColor.GREEN + "\" with timeout " + ChatColor.GREEN + timeout + ChatColor.YELLOW + " has been created!");
					return true;
					
				} else if (args[0].equalsIgnoreCase("set")) { // Not enough arguments for a set definition!
					
				}
					
			}
			
			
			
			
			
		} else {
			sender.sendMessage(ChatColor.RED + "You don't have permission to perform that command!");
			return true;
		}
		
		
		return false;
	}
	
	
	
}
