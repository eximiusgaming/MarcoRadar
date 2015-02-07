package me.sgray.marcoradar;

import org.bukkit.entity.Player;
import org.kitteh.vanish.VanishPlugin;
import org.kitteh.vanish.VanishUser;

public class VanishNoPacketUtil {
    MarcoRadar plugin;
    private VanishPlugin vanish;
    private VanishUser vu;

    public VanishNoPacketUtil(MarcoRadar plugin) {
        this.plugin = plugin;
        vanish = (VanishPlugin) plugin.getServer().getPluginManager().getPlugin("VanishNoPacket");
    }

    protected boolean isVanished(Player player) {
        return vanish.getManager().isVanished(player);
    }

    protected boolean canSeeAll(Player player) {
        vu = new VanishUser(player);
        return vu.getSeeAll();
    }
}
