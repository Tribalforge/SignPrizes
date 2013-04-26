package uk.co.drnaylor.moneysigns.commands;

import uk.co.drnaylor.moneysigns.MoneySigns;
import uk.co.drnaylor.moneysigns.Util;
import java.util.List;
import java.util.ArrayList;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;


/**
 * Handles the /spdefine command.
 *
 * Syntax:
 * /spdefine ID <id> <timeout>
 * /spdefine set <id> <set> <items|i> <set info...>
 * /spdefine set <id> <set> <money|m> <money>
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
				sender.sendMessage(ChatColor.WHITE + "/spdefine set <ID name> <set name> <items|i|money|m> <set parameters>");
				return true;
			}
			
			if (args.length == 1) { // If the command given is "/spdefine blah"
				if (args[0].equalsIgnoreCase("ID")) {
					sender.sendMessage(ChatColor.YELLOW + "Please provide an ID name and timeout in seconds to make!");
					sender.sendMessage(ChatColor.YELLOW + "/spdefine ID " + ChatColor.RED + "<ID name> <timeout>");
					return true;
				} else if (args[0].equalsIgnoreCase("set")) {
					sender.sendMessage(ChatColor.YELLOW + "Please provide some more information!");
					sender.sendMessage(ChatColor.YELLOW + "/spdefine set " + ChatColor.RED + "<ID name> <set name> <items|i|money|m> <set parameters>");
					return true;
				} else {
					sender.sendMessage(ChatColor.RED + "\"" + args[0] + "\" isn't an acceptable argument!");
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
						sender.sendMessage(ChatColor.YELLOW + "/spdefine set <ID name> " + ChatColor.RED + "<set name> <items|i|money|m> <set parameters>");
						return true;
					}
					
				} else {
					sender.sendMessage(ChatColor.RED + "\"" + args[0] + "\" isn't an acceptable argument!");
					sender.sendMessage(ChatColor.YELLOW + "Please use \"ID\" or \"set\"!");
					return true;
				}
			}
			
			if (args.length == 3) { // This is acceptable for defining an ID, but not a set!
				if (args[0].equalsIgnoreCase("ID")) { // They have all the arguments necessary for making a prize identifier!
					
					long timeout;
					try {
						timeout = Long.valueOf(args[2]);
					} catch (NumberFormatException e) {
						sender.sendMessage(ChatColor.RED + "\"" + args[2] + "\" isn't a number!");
						sender.sendMessage(ChatColor.YELLOW + "Please provide a number for the prize timeout, in seconds.");
						return true;
					}
					
					if (MoneySigns.plugin.checkIdentifier(args[1].toLowerCase())) { // If this identifier already exists...
						sender.sendMessage(ChatColor.RED + "The prize identifier \"" + args[1].toLowerCase() + "\" already exists with a timeout of " + MoneySigns.plugin.getIdentifier(args[1].toLowerCase()) + "!");
						if (sender.hasPermission("signprizes.commands.spmodify.id") && sender.hasPermission("signprizes.commands.spremove.id")) {
							sender.sendMessage(ChatColor.YELLOW + "Use \"/spmodify ID " + args[1].toLowerCase() + " " + timeout + " to modify it.");
							sender.sendMessage(ChatColor.YELLOW + "Alternatively, use \"/spremove ID " + args[1].toLowerCase() + " to remove the prize.");
						} else if (sender.hasPermission("signprizes.commands.spmodify.id")) {
							sender.sendMessage(ChatColor.YELLOW + "Use \"/spmodify ID " + args[1].toLowerCase() + " " + timeout + " to modify it.");
						} else if (sender.hasPermission("signprizes.commands.spremove.id")) {
							sender.sendMessage(ChatColor.YELLOW + "Use \"/spremove ID " + args[1].toLowerCase() + " to remove the prize.");
						}
						return true;
					}
					
					MoneySigns.plugin.setIdentifier(args[1].toLowerCase(), timeout);
					MoneySigns.plugin.saveConfig();
					sender.sendMessage(ChatColor.GREEN + "A prize identifier \"" + ChatColor.YELLOW + args[1] + ChatColor.GREEN + "\" with timeout " + ChatColor.YELLOW + timeout + ChatColor.GREEN + " has been created!");
					return true; // We're done!
					
				} else if (args[0].equalsIgnoreCase("set")) { // Not enough arguments for a set definition!
					// /spdefine set <id> <set> <items|i> <set info...>
					// /spdefine set <id> <set> <money|m> <money>
					sender.sendMessage(ChatColor.RED + "Please clarify what you want to define for this set!");
					sender.sendMessage(ChatColor.YELLOW + "/spdefine set <ID name> <set name> " + ChatColor.RED + "<items|i> <inventory|(set parameters)>");
					sender.sendMessage(ChatColor.YELLOW + "/spdefine set <ID name> <set name> " + ChatColor.RED + "<money|m> <value>");
					return true;
				} else {
					sender.sendMessage(ChatColor.RED + "\"" + args[0] + "\" isn't an acceptable argument!");
					sender.sendMessage(ChatColor.YELLOW + "Please use \"ID\" or \"set\"!");
					return true;
				}
			}
			
			if (args.length == 4) { // STILL not enough arguments!
				if (args[0].equalsIgnoreCase("ID")) {
					sender.sendMessage(ChatColor.RED + "Too many arguments!");
					sender.sendMessage(ChatColor.YELLOW + "/spdefine ID <ID name> <timeout>");
					return true;
					
				} else if (args[0].equalsIgnoreCase("set")) {
					if (args[3].equalsIgnoreCase("items") || args[3].equalsIgnoreCase("i")) {
						sender.sendMessage(ChatColor.YELLOW + "No item parameters provided!");
						sender.sendMessage(ChatColor.YELLOW + "/spdefine set <ID name> <set name> " + ChatColor.RED + "<items|i> <inventory|(set parameters)>");
						return true;
						
					} else if (args[3].equalsIgnoreCase("money") || args[3].equalsIgnoreCase("m")) {
						sender.sendMessage(ChatColor.YELLOW + "No money amount provided!");
						sender.sendMessage(ChatColor.YELLOW + "/spdefine set <ID name> <set name> " + ChatColor.RED + "<money|m> <value>");
						return true;
						
					} else {
						sender.sendMessage(ChatColor.RED + "Invalid argument \"" + args[3] + "\", expecting \"items\" or \"money\"!");
						return true;
					}
					
				} else {
					sender.sendMessage(ChatColor.RED + "\"" + args[0] + "\" isn't an acceptable argument!");
					sender.sendMessage(ChatColor.YELLOW + "Please use \"ID\" or \"set\"!");
					return true;
				}
			}
			
			if (args.length >= 5) { // Now we may have enough arguments...
			
				if (args[0].equalsIgnoreCase("ID")) {
					sender.sendMessage(ChatColor.RED + "Too many arguments!");
					sender.sendMessage(ChatColor.YELLOW + "/spdefine ID <ID name> <timeout>");
					return true;
					
				} else if (args[0].equalsIgnoreCase("set")) {
					if (args[3].equalsIgnoreCase("items") || args[3].equalsIgnoreCase("i")) {
						
						if (MoneySigns.plugin.checkPrizeSetItems(args[1].toLowerCase(), args[2].toLowerCase())) { // If this set's money prize already exists...
							sender.sendMessage(ChatColor.RED + "The set items \"" + args[2].toLowerCase() + "\" in prize ID \"" + args[1].toLowerCase() + "\" already exist.");
							if (sender.hasPermission("signprizes.commands.spmodify.set") && sender.hasPermission("signprizes.commands.spremove.set")) {
								sender.sendMessage(ChatColor.YELLOW + "Use \"/spmodify set " + args[1].toLowerCase() + " " + args[2].toLowerCase() + " items <parameters>\" to modify it.");
								sender.sendMessage(ChatColor.YELLOW + "Alternatively, use \"/spremove set" + args[1].toLowerCase() + " " + args[2].toLowerCase() + "\" to remove the prize set.");
								return true;
							} else if (sender.hasPermission("signprizes.commands.spmodify.set")) {
								sender.sendMessage(ChatColor.YELLOW + "Use \"/spmodify set " + args[1].toLowerCase() + " " + args[2].toLowerCase() + " items <parameters>\" to modify it.");
								return true;
							} else if (sender.hasPermission("signprizes.commands.spremove.set")) {
								sender.sendMessage(ChatColor.YELLOW + "Use \"/spremove set " + args[1].toLowerCase() + " " + args[2].toLowerCase() + "\" to remove the prize set.");
								return true;
							}
							return true;
						}
						
						List <ItemStack> items = new ArrayList <ItemStack> ();
						
						if (args[4].equalsIgnoreCase("inventory") || args[4].equalsIgnoreCase("inv")) {
							// Make their inventory into an item set.
							if (!(sender instanceof Player)) { // We can't get this information from the console!
								sender.sendMessage(ChatColor.RED + "The console doesn't have an inventory, so we can't make an item set from it!");
								sender.sendMessage(ChatColor.RED + "Please provide raw item information instead.");
								return true;
							}
							
							Player player = (Player) sender;
							ItemStack[] inv = player.getInventory().getContents();
							
							
						} else {
							// Assume they gave us a list of numbers and stuff.
							// I have yet to finally determine how I want this syntax to look.
							
							
							
						}
						
						if (items.isEmpty()) {
							sender.sendMessage(ChatColor.RED + "No valid item information was found.");
							return true;
						}
						
						return true;
						
					} else if (args[3].equalsIgnoreCase("money") || args[3].equalsIgnoreCase("m")) {
						// Define the money for a set.
						
						long amt;
						try {
							amt = Long.valueOf(args[4]);
						} catch (NumberFormatException e) {
							sender.sendMessage(ChatColor.RED + "\"" + args[4] + "\" isn't a number!");
							sender.sendMessage(ChatColor.YELLOW + "Please provide a number for the set's cash prize.");
							return true;
						}
						
						if (MoneySigns.plugin.checkPrizeSetMoney(args[1].toLowerCase(), args[2].toLowerCase())) { // If this set's money prize already exists...
							sender.sendMessage(ChatColor.RED + "The set \"" + args[2].toLowerCase() + "\" in prize ID \"" + args[1].toLowerCase() + "\" already exists as " + MoneySigns.plugin.getPrizeSetMoney(args[1].toLowerCase(), args[2].toLowerCase()) + ".");
							if (sender.hasPermission("signprizes.commands.spmodify.set") && sender.hasPermission("signprizes.commands.spremove.set")) {
								sender.sendMessage(ChatColor.YELLOW + "Use \"/spmodify set " + args[1].toLowerCase() + " " + args[2].toLowerCase() + " money " + amt + "\" to modify it.");
								sender.sendMessage(ChatColor.YELLOW + "Alternatively, use \"/spremove set" + args[1].toLowerCase() + " " + args[2].toLowerCase() + "\" to remove the prize set.");
								return true;
							} else if (sender.hasPermission("signprizes.commands.spmodify.set")) {
								sender.sendMessage(ChatColor.YELLOW + "Use \"/spmodify set " + args[1].toLowerCase() + " " + args[2].toLowerCase() + " money " + amt + "\" to modify it.");
								return true;
							} else if (sender.hasPermission("signprizes.commands.spremove.set")) {
								sender.sendMessage(ChatColor.YELLOW + "Use \"/spremove set " + args[1].toLowerCase() + " " + args[2].toLowerCase() + "\" to remove the prize set.");
								return true;
							}
							return true;
						}
						
						MoneySigns.plugin.setPrizeSetMoney(args[1].toLowerCase(), args[2].toLowerCase(), amt);
						MoneySigns.plugin.saveConfig();
						sender.sendMessage(ChatColor.GREEN + "The cash prize for prize set \"" + ChatColor.YELLOW + args[2] + ChatColor.GREEN + "\" in prize list \"" + ChatColor.YELLOW + args[1] + ChatColor.GREEN + "\" has been set to " + ChatColor.YELLOW + amt + ChatColor.GREEN + ".");
						
						return true;
						
					} else {
						sender.sendMessage(ChatColor.RED + "Invalid argument \"" + args[3] + "\", expecting \"items\" or \"money\"!");
						return true;
					}
				} else {
					sender.sendMessage(ChatColor.RED + "\"" + args[0] + "\" isn't an acceptable argument!");
					sender.sendMessage(ChatColor.YELLOW + "Please use \"ID\" or \"set\"!");
					return true;
				}
				
			}
			
			
			
		} else {
			sender.sendMessage(ChatColor.RED + "You don't have permission to perform that command!");
			return true;
		}
		
		
		return false;
	}
	
	
	
}
