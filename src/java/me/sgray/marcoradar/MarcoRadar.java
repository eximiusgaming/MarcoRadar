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
        saveDefaultConfig();
        if (getServer().getPluginManager().getPlugin("VanishNoPacket") != null) {
            vnp = new VanishNoPacketUtil(this);
        }
        getCommand("marcoradar").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        String cmdName = cmd.getName().toLowerCase();

        if (!cmdName.equals("marcoradar")) {
            return false;
        }

        if (args.length == 1 && args[0].equals("reload")) {
            if (!sender.hasPermission("marco.admin")) {
                return false;
            } else {
                reloadConfig();
                sender.sendMessage("Config reloaded");
                return true;
            }
        }

        if (sender.hasPermission("marco.list")) {
            if (!getServer().getOnlinePlayers().isEmpty()) {
                boolean seeAll = false;
                if (vnp != null) {
                    seeAll = (!(sender instanceof Player) || vnp.canSeeAll((Player) sender)) ? true : false;
                }
                for (Player target : getServer().getOnlinePlayers()) {
                    if (vnp != null && vnp.isVanished(target)) {
                        if (seeAll) {
                            giveLocation(sender, target, getNearbyEntityCount(target));
                        }
                    } else {
                        giveLocation(sender, target, getNearbyEntityCount(target));
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

    private void giveLocation(CommandSender sender, Player target, int entities) {
        String tName = target.getName();
        Location loc = target.getLocation();
        sender.sendMessage(ChatColor.GREEN + tName + ChatColor.YELLOW + " is at " 
                + ChatColor.GREEN + getFriendlyLocation(loc) 
                + " - " + String.valueOf(entities) + " entities nearby");
    }

    private int getNearbyEntityCount(Player player) {
        Double distance = Double.valueOf(getConfig().getDouble("count-entity-radius"));
        return player.getNearbyEntities(distance, distance, distance).size();
    }

    private String getFriendlyLocation(Location loc) {
        return String.valueOf((int) loc.getX()) 
                + " " + String.valueOf((int) loc.getY()) 
                + " " + String.valueOf((int) loc.getZ()) 
                + " (" + String.valueOf(loc.getWorld().getName()) + ")";
    }
}
