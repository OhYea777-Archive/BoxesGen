package com.ohyea777.boxesgen;

import com.ohyea777.boxesgen.game.Game;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class BoxesGen extends JavaPlugin {

    private static BoxesGen instance;

    private Game game;

    @Override
    public void onEnable() {
        instance = this;
    }

    @Override
    public void onDisable() {

    }

    public static BoxesGen getInstance() {
        return instance;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (command.getName().equalsIgnoreCase("join")) {
                if (game == null) {
                    int playerHeight = Integer.valueOf(args[0]);
                    int blocksPerSecond = Integer.valueOf(args[1]);
                    int maxBlocks = Integer.valueOf(args[2]);
                    long seed = Long.valueOf(args[3]);

                    game = new Game(new Location(player.getWorld(), 0, 100, 0, -90.0f, 90.0f), player, playerHeight, blocksPerSecond, maxBlocks, seed);
                }
            } else if (command.getName().equalsIgnoreCase("leave")) {
                if (game != null) {
                    game.end();

                    game = null;
                }
            } else if (command.getName().equalsIgnoreCase("loc")) {
                Location location = player.getLocation();

                player.sendMessage("World: '" + location.getWorld().getName() + "', x: " + location.getX() + ", y: " + location.getY() + ", z: " + location.getZ() + ", yaw: " + location.getYaw() + ", pitch: " + location.getPitch());
            } else if (command.getName().equalsIgnoreCase("pause")) {
                if (game != null) game.pause();
            } else if (command.getName().equalsIgnoreCase("play")) {
                if (game != null) game.play();
            }
        }

        return true;
    }

}
