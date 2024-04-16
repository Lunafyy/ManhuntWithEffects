package me.lunafy.manhuntwitheffects.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TrackingCompass {
    public static ItemStack getItem() {
        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta compassMeta = compass.getItemMeta();

        List<String> meta = new ArrayList<String>();
        meta.add(ChatColor.WHITE + "This compass will track");
        meta.add(ChatColor.WHITE + "the nearest runner to you.");
        meta.add(" ");
        meta.add(ChatColor.RED + "⚠ This item does not work in the Nether ⚠");

        compassMeta.setDisplayName(ChatColor.LIGHT_PURPLE + "Runner Tracker");
        compassMeta.setLore(meta);

        compass.setItemMeta(compassMeta);

        return compass;
    }
}
