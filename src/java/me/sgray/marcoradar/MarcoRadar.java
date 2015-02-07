package me.sgray.marcoradar;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MarcoRadar extends JavaPlugin {
    protected VanishNoPacketUtil vnp;

    @Override
    public void onEnable() {
        if (getServer().getPluginManager().getPlugin("VanishNoPacket") != null) {
            vnp = new VanishNoPacketUtil(this);
        }
        getCommand("marcoradar").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String cmdName = cmd.getName().toLowerCase();
        if (cmdName.equals("marcoradar")) {
            if (sender.hasPermission("marco.list")) {
                if (!getServer().getOnlinePlayers().isEmpty()) {
                    boolean seeAll = false;
                    if (vnp != null) {
                        seeAll = (!(sender instanceof Player) || vnp.canSeeAll((Player) sender)) ? true : false;
                    }
                    for (Player target : getServer().getOnlinePlayers()) {
                        if (vnp != null && vnp.isVanished(target)) {
                            if (seeAll) {
                                giveLocation(sender, target);
                            }
                        } else {
                            giveLocation(sender, target);
                        }
                    }
                } else {
                    if (sender.hasPermission("marco.list")) {
                        sender.sendMessage(ChatColor.GOLD + "Hmm, seems nobody is online.");
                    }
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Sorry, can't do that for you.");
            }
            return true;
        }
        return false;
    }

    private void giveLocation(CommandSender sender, Player target) {
        String tName = target.getName();
        Location loc = target.getLocation();
        sender.sendMessage(ChatColor.GREEN + tName + ChatColor.YELLOW + " is at " + ChatColor.GREEN + getFriendlyLocation(loc));
    }

    private String getFriendlyLocation(Location loc) {
        return String.valueOf((int) loc.getX()) 
                + " " + String.valueOf((int) loc.getY()) 
                + " " + String.valueOf((int) loc.getZ()) 
                + " (" + String.valueOf(loc.getWorld().getName()) + ")";
    }
}
