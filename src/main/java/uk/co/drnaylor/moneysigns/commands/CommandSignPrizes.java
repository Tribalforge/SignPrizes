package uk.co.drnaylor.moneysigns.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import uk.co.drnaylor.moneysigns.MoneySigns;
import uk.co.drnaylor.moneysigns.Util;
import org.bukkit.ChatColor;

/**
 * Handles the /sp or /signprizes command.
 *
 * Syntax:
 * /sp define <id> <timeout>
 * /sp modify <id> <timeout>
 * /sp <info|remove> <id>
 * /sp defineset <id> <set info...|inventory>
 * /sp modifyset <id> <set info...|inventory>
 * /sp definesetmoney <id> <money>
 * /sp <setinfo|setremove> <id> <set>
 */
public class CommandSignPrizes implements CommandExecutor {
	
	// Handles /sp or /signprizes.
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			//sender.sendMessage(ChatColor.GREEN + "SignPrizes info");
			return true;
		}
		
		
		
		
		
		return false;
	}
	
	
	
}
