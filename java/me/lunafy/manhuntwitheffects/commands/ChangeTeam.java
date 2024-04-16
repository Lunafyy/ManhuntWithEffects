package me.lunafy.manhuntwitheffects.commands;

import me.lunafy.manhuntwitheffects.ManhuntWithEffects;
import me.lunafy.manhuntwitheffects.enums.PlayerTeam;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChangeTeam implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;

            if(p.isOp()) {
                if(args.length == 2) {
                    Player toChange = Bukkit.getPlayer(args[0]);

                    if(toChange != null) {
                        switch(args[1].toLowerCase()) {
                            case "hunter":
                                ManhuntWithEffects.getInstance().changeTeam(toChange, PlayerTeam.HUNTER);
                                break;
                            case "runner":
                                ManhuntWithEffects.getInstance().changeTeam(toChange, PlayerTeam.RUNNER);
                                break;
                            case "spectator":
                                ManhuntWithEffects.getInstance().changeTeam(toChange, PlayerTeam.SPECTATOR);
                                break;
                            default:
                                p.sendMessage(ChatColor.RED + "Invalid team!");
                                break;
                        }
                    } else {
                        p.sendMessage(ChatColor.RED + "Invalid player!");
                    }
                } else {
                    p.sendMessage(ChatColor.RED + "Invalid amount of arguments!");
                }
            } else {
                p.sendMessage(ChatColor.RED + "You do not have permission to do this!");
            }
        }
        return true;
    }
}
