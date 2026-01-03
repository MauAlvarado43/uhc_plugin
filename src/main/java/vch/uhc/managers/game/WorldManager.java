package vch.uhc.managers.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.block.Block;

import vch.uhc.UHC;
import vch.uhc.misc.Settings;

/**
 * Manages world interactions, spawns, and border logic. Ensures safe player
 * distribution and handled shrinking boundaries.
 */
public class WorldManager {

    private final UHC plugin;
    private final Settings settings;

    public WorldManager() {
        this.plugin = UHC.getPlugin();
        this.settings = plugin.getSettings();
    }

    /**
     * Calculates and returns a list of spawn locations for teams. The algorithm
     * distributes spawns evenly along the world border.
     *
     * @return A list of Location objects for spawns.
     */
    public List<Location> getSpawns() {
        World world = Bukkit.getWorld("world");
        if (world == null) {
            return new ArrayList<>();
        }

        int spawnCount = plugin.getTeamManager().getTeams().size();
        if (spawnCount == 0) {
            spawnCount = 1;
        }

        List<Location> spawns = new ArrayList<>();
        int size = settings.getMaxWorldSize() - 50;

        if (spawnCount == 1) {
            spawns.add(createSpawn(world, 0, 0));
            return spawns;
        }

        if (spawnCount == 2) {
            spawns.add(createSpawn(world, -size, 0));
            spawns.add(createSpawn(world, size, 0));
            return spawns;
        }

        if (spawnCount == 4) {
            spawns.add(createSpawn(world, -size, -size));
            spawns.add(createSpawn(world, size, -size));
            spawns.add(createSpawn(world, size, size));
            spawns.add(createSpawn(world, -size, size));
            return spawns;
        }

        spawns.add(createSpawn(world, -size, -size));
        spawns.add(createSpawn(world, size, -size));
        spawns.add(createSpawn(world, size, size));
        spawns.add(createSpawn(world, -size, size));

        if (spawnCount <= 4) {
            return new ArrayList<>(spawns.subList(0, spawnCount));
        }

        int remainingSpawns = spawnCount - 4;
        int[] pointsPerSide = new int[4];
        for (int i = 0; i < remainingSpawns; i++) {
            pointsPerSide[i % 4]++;
        }

        // North
        for (int i = 1; i <= pointsPerSide[0]; i++) {
            double fraction = i / (double) (pointsPerSide[0] + 1);
            int x = (int) (-size + 2 * size * fraction);
            spawns.add(createSpawn(world, x, -size));
        }

        // East
        for (int i = 1; i <= pointsPerSide[1]; i++) {
            double fraction = i / (double) (pointsPerSide[1] + 1);
            int z = (int) (-size + 2 * size * fraction);
            spawns.add(createSpawn(world, size, z));
        }

        // South
        for (int i = 1; i <= pointsPerSide[2]; i++) {
            double fraction = i / (double) (pointsPerSide[2] + 1);
            int x = (int) (size - 2 * size * fraction);
            spawns.add(createSpawn(world, x, size));
        }

        // West
        for (int i = 1; i <= pointsPerSide[3]; i++) {
            double fraction = i / (double) (pointsPerSide[3] + 1);
            int z = (int) (size - 2 * size * fraction);
            spawns.add(createSpawn(world, -size, z));
        }

        return spawns;
    }

    /**
     * Creates a spawn point at specified coordinates.
     *
     * @param world The world where to create the spawn.
     * @param x X coordinate.
     * @param z Z coordinate.
     * @return The safe spawn Location.
     */
    public Location createSpawn(World world, int x, int z) {
        return findSafeSpawnLocation(world, x, z);
    }

    /**
     * Finds a safe location and prepares a bedrock platform.
     */
    private Location findSafeSpawnLocation(World world, int x, int z) {
        int highestY = world.getHighestBlockYAt(x, z);

        // Create a small 3x3 bedrock platform at the surface
        for (int xOffset = -1; xOffset <= 1; xOffset++) {
            for (int zOffset = -1; zOffset <= 1; zOffset++) {
                Block platformBlock = world.getBlockAt(x + xOffset, highestY, z + zOffset);
                platformBlock.setType(Material.BEDROCK);

                // Clear blocks above platform
                world.getBlockAt(x + xOffset, highestY + 1, z + zOffset).setType(Material.AIR);
                world.getBlockAt(x + xOffset, highestY + 2, z + zOffset).setType(Material.AIR);
            }
        }

        // Add invisible light block in the center for illumination
        Block lightBlock = world.getBlockAt(x, highestY + 2, z);
        lightBlock.setType(Material.LIGHT);
        if (lightBlock.getBlockData() instanceof org.bukkit.block.data.type.Light lightData) {
            lightData.setLevel(15);
            lightBlock.setBlockData(lightData);
        }

        return new Location(world, x + 0.5, highestY + 1, z + 0.5);
    }

    /**
     * Calculates a safe respawn location within the current border.
     *
     * @param originalSpawn The original spawn of the player.
     * @return A safe Location within boundaries.
     */
    public Location getSafeRespawnLocation(Location originalSpawn) {
        if (originalSpawn == null) {
            return null;
        }
        World world = originalSpawn.getWorld();
        if (world == null) {
            return null;
        }

        WorldBorder border = world.getWorldBorder();
        double borderSize = border.getSize() / 2.0;

        double x = originalSpawn.getX();
        double z = originalSpawn.getZ();

        if (Math.abs(x) <= borderSize && Math.abs(z) <= borderSize) {
            return originalSpawn;
        }

        int safeSize = (int) (borderSize * 0.9);
        int newX = (int) (Math.random() * safeSize * 2 - safeSize);
        int newZ = (int) (Math.random() * safeSize * 2 - safeSize);

        return createSpawn(world, newX, newZ);
    }

    public void updateWorldBorder(int elapsedSeconds, int currentWorldSize) {
        // Implementation can be added if needed for more complex transitions
    }

    /**
     * Sets the center for all world borders.
     */
    public void setBorderCenter(double x, double z) {
        for (World world : Bukkit.getWorlds()) {
            world.getWorldBorder().setCenter(x, z);
        }
    }

    /**
     * Sets the size for all world borders.
     *
     * @param size The new size in blocks.
     */
    public void setBorderSize(double size) {
        for (World world : Bukkit.getWorlds()) {
            world.getWorldBorder().setSize(Math.max(1.0, Math.min(size, 5.9999968E7)));
        }
    }

    /**
     * Gradually shrinks the world border by a specific amount.
     *
     * @param amount The amount to reduce from current size.
     */
    public void shrinkBorder(double amount) {
        for (World world : Bukkit.getWorlds()) {
            WorldBorder border = world.getWorldBorder();
            double newSize = border.getSize() - amount;
            border.setSize(Math.max(1.0, Math.min(newSize, 5.9999968E7)));
        }
    }
}
