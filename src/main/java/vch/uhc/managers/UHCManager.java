package vch.uhc.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatColor;
import vch.uhc.UHC;
import vch.uhc.misc.Settings;
import vch.uhc.models.Team;

public class UHCManager {

    final UHC plugin;
    final Settings settings;
    final private PlayerManager playerManager;
    final private TeamManager teamManager;

    private BukkitTask proximityTeamTask;
    private BukkitTask gameTimeTask;

    public UHCManager() {
        plugin = UHC.getPlugin();
        settings = plugin.getSettings();
        playerManager = plugin.getPlayerManager();
        teamManager = plugin.getTeamManager();
    }

    public void start() {

        settings.setGameStatus(Settings.GameStatus.IN_PROGRESS);

        settings.getItems().forEach(item -> {
            if (Bukkit.getRecipe(item.getKey()) == null && item.isEnabled()) {
                item.register();
            }
        });

        List<Location> spawns = getSpawns();
        prepareGame();
        // prepareTeams();
        // preparePlayers(spawns);
        // startTimedTasks();

    }

    public void pause() {
        settings.setGameStatus(Settings.GameStatus.PAUSED);
        if (proximityTeamTask != null) {
            proximityTeamTask.cancel();
        }
        if (gameTimeTask != null) {
            gameTimeTask.cancel();
        }
        settings.save();
    }

    public void resume() {
        settings.setGameStatus(Settings.GameStatus.IN_PROGRESS);
        settings.load();
        startTimedTasks();
    }

    public void cancel() {
        settings.setGameStatus(Settings.GameStatus.NONE);
        if (proximityTeamTask != null) {
            proximityTeamTask.cancel();
        }
        if (gameTimeTask != null) {
            gameTimeTask.cancel();
        }
    }

    private void prepareGame() {

        Bukkit.getWorlds().forEach(world -> {
            world.setGameRule(GameRule.NATURAL_REGENERATION, false);
        });

        Bukkit.getWorlds().forEach(world -> {
            world.getWorldBorder().setCenter(0, 0);
            world.getWorldBorder().setSize(settings.getMaxWorldSize());
        });

    }

    private void prepareScoreboard() {

    }

    private List<Location> getSpawns() {

        String worldName = "world";
        int players = playerManager.getPlayers().size();
        int size = settings.getMaxWorldSize();
        int teams = (int) Math.ceil(players / (double) settings.getTeamSize());
        int perSide = (int) Math.ceil(teams / 4.0);
        List<Location> spawns = new ArrayList<>();

        for (int side = 0; side < 4; side++) {

            for (int i = 0; i < perSide; i++) {

                if (spawns.size() >= teams) {
                    break;
                }

                double fraction = (i + 1.0) / (perSide + 1.0);
                int x = 0, z = 0;

                switch (side) {
                    case 0:
                        x = (int) (-size + 2 * size * fraction);
                        z = size;
                        break;
                    case 1:
                        x = size;
                        z = (int) (size - 2 * size * fraction);
                        break;
                    case 2:
                        x = (int) (size - 2 * size * fraction);
                        z = -size;
                        break;
                    case 3:
                        x = -size;
                        z = (int) (-size + 2 * size * fraction);
                        break;
                }

                World world = Bukkit.getWorld(worldName);
                int worldY = world.getHighestBlockYAt(x, z);

                spawns.add(new Location(world, x, worldY, z));

            }

        }

        return spawns;

    }

    private void prepareTeams() {

        if (settings.getTeamMode() != Settings.TeamMode.AUTO) {
            return;
        }

        ArrayList<vch.uhc.models.Player> uhcPlayers = new ArrayList<>(playerManager.getPlayers());
        int players = uhcPlayers.size();
        int teams = (int) Math.ceil(players / (double) settings.getTeamSize());
        Collections.shuffle(uhcPlayers);

        for (int i = 0; i < players; i += teams) {

            Team team = teamManager.createTeam(uhcPlayers.get(i), "Team " + (i + 1));

            for (int j = 0; j < teams; j++) {

                if (i + j >= players) {
                    break;
                }

                teamManager.addPlayer(team, uhcPlayers.get(i + j));

            }

        }

    }

    private void preparePlayers(List<Location> spawns) {

        if (settings.getTeamMode() == Settings.TeamMode.MANUAL || settings.getTeamMode() == Settings.TeamMode.AUTO) {

            ArrayList<Team> teams = teamManager.getTeams();

            for (int i = 0; i < teams.size(); i++) {

                Team team = teams.get(i);
                Location loc = spawns.get(i % spawns.size()).clone();

                for (vch.uhc.models.Player player : team.getMembers()) {

                    player.setSpawn(loc);

                    Player bukkitPlayer = Bukkit.getPlayer(player.getUuid());
                    if (bukkitPlayer != null) {
                        bukkitPlayer.teleport(loc);
                    }

                }

            }

        } else {

            ArrayList<vch.uhc.models.Player> players = new ArrayList<>(plugin.getPlayerManager().getPlayers());

            for (int i = 0; i < players.size(); i++) {

                vch.uhc.models.Player player = players.get(i);
                Location loc = spawns.get(i % spawns.size()).clone();

                player.setSpawn(loc);
                Player bukkitPlayer = Bukkit.getPlayer(player.getUuid());
                if (bukkitPlayer != null) {
                    bukkitPlayer.teleport(loc);
                }

            }

        }

    }

    private void startTimedTasks() {

        if (settings.getTeamMode() == Settings.TeamMode.IN_GAME) {

            proximityTeamTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
                checkInGameTeamsState();
            }, 0, 20);

        }

        gameTimeTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            checkGameState();
            checkAgreementState();
            checkWorldBorderState();
        }, 0, 20);

    }

    private void checkGameState() {

        int hours = settings.getGameHours();
        int minutes = settings.getGameMinutes();
        int seconds = settings.getGameSeconds();
        boolean hasFinished = false;

        if (hours == 0 && minutes == 0 && seconds == 0) {

        }

        if (seconds > 0) {
            settings.setGameSeconds(seconds - 1);
        } else if (minutes > 0) {
            settings.setGameMinutes(minutes - 1);
            settings.setGameSeconds(59);
        } else if (hours > 0) {
            settings.setGameHours(hours - 1);
            settings.setGameMinutes(59);
            settings.setGameSeconds(59);
        }

    }

    private void checkInGameTeamsState() {

        boolean canCreateTeams = false;

        for (Player p1 : Bukkit.getOnlinePlayers()) {
            for (Player p2 : Bukkit.getOnlinePlayers()) {

                boolean samePlayer = p1.equals(p2);
                if (samePlayer) {
                    continue;
                }

                boolean sameWorld = p1.getWorld().equals(p2.getWorld());
                if (!sameWorld) {
                    continue;
                }

                Team teamp1 = plugin.getPlayerManager().getPlayerByUUID(p1.getUniqueId()).getTeam();
                Team teamp2 = plugin.getPlayerManager().getPlayerByUUID(p2.getUniqueId()).getTeam();

                if (teamp1 != null && teamp1.getMembers().size() < settings.getTeamSize()) {
                    continue;
                }

                if (teamp2 != null && teamp2.getMembers().size() < settings.getTeamSize()) {
                    continue;
                }

                if (teamp1 != null && teamp2 != null && teamp1.getMembers().size() + teamp2.getMembers().size() > settings.getTeamSize()) {
                    continue;
                }

                canCreateTeams = true;

                boolean areClose = p1.getLocation().distance(p2.getLocation()) <= 200;
                if (!areClose) {
                    continue;
                }

                p1.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 200, 1));
                p2.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 200, 1));

                // show message above experience bar
                p1.sendMessage(ChatColor.GREEN + "You are close to a player");
                p2.sendMessage(ChatColor.GREEN + "You are close to a player");

            }
        }

        if (!canCreateTeams) {
            proximityTeamTask.cancel();
        }

    }

    private void checkAgreementState() {

    }

    private void checkWorldBorderState() {

    }

}
