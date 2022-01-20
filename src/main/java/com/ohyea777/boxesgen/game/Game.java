package com.ohyea777.boxesgen.game;

import com.ohyea777.boxesgen.BoxesGen;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game implements Runnable {

    private final int PLAYER_HEIGHT;
    private final int BLOCKS_PER_SECOND;
    private final int MAX_BLOCKS;

    private Location playerLoc;
    private Player player;
    private Random random;
    private boolean isPositive;
    private int count, amount;
    private int scheduleId;
    private List<Location> blockLocations;

    public Game(Location spawn, Player player, int playerHeight, int blocksPerSecond, int maxBlocks, long seed) {
        PLAYER_HEIGHT = playerHeight;
        BLOCKS_PER_SECOND = blocksPerSecond;
        MAX_BLOCKS = maxBlocks;

        if (seed == -1) random = new Random();
        else random = new Random(seed);

        this.playerLoc = spawn;
        this.player = player;
        this.isPositive = true;
        this.count = 0;
        this.amount = random.nextInt(MAX_BLOCKS - 1) + 1;
        this.blockLocations = new ArrayList<Location>();

        player.setAllowFlight(true);
        player.setFlying(true);
        player.teleport(spawn);

        this.scheduleId = getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(getPlugin(), this, (int) (1.0 / BLOCKS_PER_SECOND * 20), (int) (1.0 / BLOCKS_PER_SECOND * 20));
    }

    private BoxesGen getPlugin() {
        return BoxesGen.getInstance();
    }

    private Material getMaterial() {
        return Material.CARPET;
    }

    private short getDamage() {
        return isPositive ? (short) 14 : (short) 0;
    }

    private void setBlock() {
        Location location = playerLoc.clone();

        location.subtract(0, PLAYER_HEIGHT, 0).getBlock().setType(getMaterial());
        location.getBlock().setData((byte) getDamage());
        blockLocations.add(location);
    }

    private void debugMessage() {
        player.sendMessage("Amount: " + amount + ", isPositive: " + isPositive);
    }

    @Override
    public void run() {
        doNext();
    }

    private void doNext() {
        if (count == 0) {
            debugMessage();

            if (isPositive) playerLoc.add(-1, 0, 1);
            else playerLoc.add(1, 0, 1);

            count ++;
        } else {
            if (isPositive) playerLoc.add(-1, 0, 0);
            else playerLoc.add(1, 0, 0);
        }

        // player.teleport(playerLoc);

        setBlock();

        if (count == amount) {
            count = 0;
            amount = random.nextInt(MAX_BLOCKS - 1) + 1;
            isPositive = !isPositive;
        } else count ++;
    }

    public void pause() {
        getPlugin().getServer().getScheduler().cancelTask(scheduleId);
    }

    public void play() {
        player.teleport(playerLoc);

        scheduleId = getPlugin().getServer().getScheduler().scheduleSyncRepeatingTask(getPlugin(), this, (int) (1.0 / BLOCKS_PER_SECOND * 20), (int) (1.0 / BLOCKS_PER_SECOND * 20));
    }

    public void end() {
        getPlugin().getServer().getScheduler().cancelTask(scheduleId);

        for (Location location : blockLocations) location.getBlock().setType(Material.AIR);
    }

}
