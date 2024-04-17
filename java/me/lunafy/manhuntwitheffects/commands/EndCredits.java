package me.lunafy.manhuntwitheffects.commands;

import net.minecraft.network.protocol.game.ClientboundPlayerCombatKillPacket;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_20_R3.util.CraftChatMessage;
import org.bukkit.entity.Player;

public class EndCredits implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;

            if(!p.isOp()) return true;

            if(args.length > 0) {
                Player target = Bukkit.getPlayer(args[0]);

                if(target != null) {
                    ((CraftPlayer) target).getHandle().connection.send(new ClientboundPlayerCombatKillPacket(target.getEntityId(), CraftChatMessage.fromStringOrNull("Got fucked")));
                } else {
                    p.sendMessage(ChatColor.RED + "Invalid player!");
                }
            } else {
                p.sendMessage(ChatColor.RED + "You need to specify a player!");
            }

        }
        return true;
    }
}
