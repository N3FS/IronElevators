package me.ScarleTomato.IronElevators;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public final class EventListener implements Listener {

    private final IronElevators plugin;

    public EventListener(IronElevators plugin) {
        this.plugin = plugin;
    }

    private boolean searchFloor(Block floor) {
        return floor.getType() == plugin.ELEVATOR_MATERIAL
                && floor.getRelative(BlockFace.UP).getType().isTransparent()
                && floor.getRelative(BlockFace.UP, 2).getType().isTransparent();
    }

    private void teleport(Player player, Location to) {
        player.teleport(to);
        player.getWorld().playSound(to, plugin.ELEVATOR_WHOOSH, 1, 0);
    }

    @EventHandler
    public void downElevator(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (player.hasPermission("ironelevators.use") && !player.isSneaking() && block.getType() == plugin.ELEVATOR_MATERIAL) {
            block = block.getRelative(BlockFace.DOWN, plugin.MIN_ELEVATION);
            int search = plugin.MAX_ELEVATION;
            while (search > 0 && !(searchFloor(block))) {
                search--;
                block = block.getRelative(BlockFace.DOWN);
            }
            if (search > 0) {
                Location loc = player.getLocation();
                loc.setY(loc.getY() - plugin.MAX_ELEVATION - 3 + search);
                teleport(player, loc);
            }
        }
    }

    @EventHandler
    public void upElevator(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Block block = event.getTo().getBlock().getRelative(BlockFace.DOWN);
        if (player.hasPermission("ironelevators.use") && event.getFrom().getY() < event.getTo().getY() && block.getType() == plugin.ELEVATOR_MATERIAL) {
            block = block.getRelative(BlockFace.UP, plugin.MIN_ELEVATION);
            int search = plugin.MAX_ELEVATION;
            while (search > 0 && !(searchFloor(block))) {
                search--;
                block = block.getRelative(BlockFace.UP);
            }
            if (search > 0) {
                Location loc = player.getLocation();
                loc.setY(loc.getY() + plugin.MAX_ELEVATION + 3 - search);
                teleport(player, loc);
            }
        }
    }
}
