package vch.uhc.managers;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.GameRules;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.title.Title;
import vch.uhc.UHC;
import vch.uhc.misc.Messages;
import vch.uhc.misc.Settings;
import vch.uhc.misc.constants.GameConstants;
import vch.uhc.misc.enums.GameMode;
import vch.uhc.misc.enums.GameState;
import vch.uhc.misc.enums.TeamMode;
import vch.uhc.models.GameStats;
import vch.uhc.models.UHCPlayer;
import vch.uhc.models.UHCTeam;

public class UHCManager {

    final UHC plugin;
    final Settings settings;
    final private PlayerManager playerManager;
    final private TeamManager teamManager;

    private BukkitTask gameTimeTask;

    private int currentWorldSize;
    private int elapsedHours = 0;
    private int elapsedMinutes = 0;
    private int elapsedSeconds = 0;

    private int agreementElapsedSeconds = 0;
    private boolean agreementActive = false;

    private int buffsElapsedSeconds = 0;
    private boolean buffsApplied = false;

    private int skinShuffleElapsedSeconds = 0;

    private boolean isCheckingProximity = true;
    private boolean teamsFormed = false;
    private GameStats gameStats;

    public UHCManager() {
        plugin = UHC.getPlugin();
        settings = plugin.getSettings();
        playerManager = plugin.getPlayerManager();
        teamManager = plugin.getTeamManager();
    }

    /**
     * Starts a new UHC game session.
     * Initializes game state, prepares world, and starts timers.
     */
    public void start() {

        settings.setGameState(GameState.IN_PROGRESS);

        plugin.getGameModeManager().initializeGameMode();
        currentWorldSize = settings.getMaxWorldSize();
        teamsFormed = false;

        // Calculate total agreement phase duration using constants
        int totalAgreementSeconds = settings.getAgreementHours() * GameConstants.SECONDS_PER_HOUR
                + settings.getAgreementMinutes() * GameConstants.SECONDS_PER_MINUTE
                + settings.getAgreementSeconds();
        agreementActive = totalAgreementSeconds > GameConstants.ZERO;
        agreementElapsedSeconds = GameConstants.ZERO;
        gameStats = new GameStats();

        settings.getItems().forEach(item -> {
            if (org.bukkit.Bukkit.getRecipe(item.getKey()) == null && item.isEnabled()) {
                item.register();
            }
        });

        prepareGame();
        prepareScoreboard();
        prepareTeams();
        preparePlayers();

        // Prepare world with default conditions
        World world = Bukkit.getWorld("world");
        if (world != null) {
            world.setStorm(false);
            world.setThundering(false);
            world.setWeatherDuration(GameConstants.MAX_WEATHER_DURATION);
            world.setTime(GameConstants.DAY_TIME);
        }

        startTimedTasks();

    }

    /**
     * Pauses the current game session.
     * Stops timers while preserving game state.
     */
    public void pause() {
        settings.setGameState(GameState.PAUSED);
        if (gameTimeTask != null) {
            gameTimeTask.cancel();
        }
        settings.save();
    }

    public void resume() {
        settings.setGameState(GameState.IN_PROGRESS);
        settings.load();
        startTimedTasks();
    }

    public void cancel() {
        settings.setGameState(GameState.NONE);
        if (gameTimeTask != null) {
            gameTimeTask.cancel();
        }

        plugin.getSkinManager().restoreAllSkins();
        plugin.getSkinManager().clearAssignments();
    }

    public void reload() {

        if (settings.getGameState() != GameState.NONE) {
            cancel();
        }

        if (gameTimeTask != null) {
            gameTimeTask.cancel();
            gameTimeTask = null;
        }

        plugin.getSkinManager().restoreAllSkins();
        plugin.getSkinManager().clearAssignments();

        Bukkit.getOnlinePlayers().forEach(p -> {
            Scoreboard scoreboard = p.getScoreboard();
            Objective objective = scoreboard.getObjective("uhc");
            if (objective != null) {
                objective.unregister();
            }
            Objective healthObjective = scoreboard.getObjective("health");
            if (healthObjective != null) {
                healthObjective.unregister();
            }
            p.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        });

        teamManager.getTeams().clear();

        playerManager.getPlayers().forEach(player -> {
            player.setTeam(null);
            player.setLives(1);
            player.setPlaying(true);
        });

        elapsedSeconds = 0;
        elapsedMinutes = 0;
        elapsedHours = 0;
        agreementElapsedSeconds = 0;
        agreementActive = false;
        buffsElapsedSeconds = 0;
        buffsApplied = false;
        skinShuffleElapsedSeconds = 0;
        isCheckingProximity = true;
        teamsFormed = false;
        gameStats = null;
        currentWorldSize = 0;

        settings.load();

        Bukkit.getWorlds().forEach(world -> {
            world.setGameRule(GameRules.NATURAL_HEALTH_REGENERATION, true);
            world.getWorldBorder().setSize(29999984);
            world.getWorldBorder().setCenter(0, 0);
        });

        Bukkit.getOnlinePlayers().forEach(p -> {
            if (p.getGameMode() != org.bukkit.GameMode.CREATIVE) {
                p.setGameMode(org.bukkit.GameMode.SURVIVAL);
            }
            p.getActivePotionEffects().forEach(effect -> {
                p.removePotionEffect(effect.getType());
            });
            AttributeInstance maxHealthAttr = p.getAttribute(Attribute.MAX_HEALTH);
            if (maxHealthAttr != null) {
                maxHealthAttr.setBaseValue(20.0);
            }
            p.setHealth(20.0);
        });
    }

    private void prepareGame() {

        Bukkit.getWorlds().forEach(world -> {
            world.setGameRule(GameRules.NATURAL_HEALTH_REGENERATION, false);
            world.setGameRule(GameRules.LOCATOR_BAR, false);
        });

        Bukkit.getWorlds().forEach(world -> {
            world.getWorldBorder().setCenter(0, 0);
            double newSize = (currentWorldSize * 2) + 4;
            newSize = Math.max(1.0, Math.min(newSize, 5.9999968E7));
            world.getWorldBorder().setSize(newSize);
        });

    }

    private void prepareScoreboard() {
        Bukkit.getOnlinePlayers().forEach(this::createPlayerScoreboard);
    }

    private void createPlayerScoreboard(Player player) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();

        Objective objective = scoreboard.registerNewObjective(
                "uhc",
                Criteria.DUMMY,
                Component.text(Messages.UHC_SCOREBOARD_TITLE()));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        player.setScoreboard(scoreboard);
    }

    public void updateScoreboard() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updatePlayerScoreboard(player);
        }
    }

    private void updatePlayerScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Objective objective = scoreboard.getObjective("uhc");

        if (objective == null) {
            createPlayerScoreboard(player);
            scoreboard = player.getScoreboard();
            objective = scoreboard.getObjective("uhc");
            if (objective == null) {
                return;
            }
        }

        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }

        int score = 15;

        objective.getScore(" ").setScore(score--);
        objective.getScore(Messages.UHC_SCOREBOARD_TIME(elapsedHours, elapsedMinutes, elapsedSeconds))
                .setScore(score--);
        objective.getScore("  ").setScore(score--);

        UHCPlayer uhcPlayer = playerManager.getPlayerByUUID(player.getUniqueId());
        if (uhcPlayer != null) {
            objective.getScore(Messages.SCOREBOARD_YOUR_LIVES(uhcPlayer.getLives())).setScore(score--);
            objective.getScore("    ").setScore(score--);
        }

        if (uhcPlayer != null && uhcPlayer.getTeam() != null) {
            UHCTeam team = uhcPlayer.getTeam();
            String teamName = LegacyComponentSerializer.legacyAmpersand().serialize(
                    LegacyComponentSerializer.legacyAmpersand().deserialize(team.getName()));
            objective.getScore(Messages.SCOREBOARD_TEAM(teamName)).setScore(score--);

            for (UHCPlayer member : team.getMembers()) {
                if (!member.getUuid().equals(player.getUniqueId())) {
                    Player memberPlayer = Bukkit.getPlayer(member.getUuid());
                    if (memberPlayer != null && member.isAlive()) {
                        double health = memberPlayer.getHealth();
                        objective.getScore(Messages.SCOREBOARD_HEALTH(health) + " §f" + member.getName())
                                .setScore(score--);
                    } else if (!member.isAlive()) {
                        objective.getScore(Messages.SCOREBOARD_MEMBER_DEAD(member.getName())).setScore(score--);
                    }
                }
            }
            objective.getScore("   ").setScore(score--);
        }

        if (settings.getGameMode() == GameMode.RESOURCE_RUSH) {
            List<Material> items = plugin.getGameModeManager().getResourceRushItems();
            if (items != null && !items.isEmpty()) {
                objective.getScore(Messages.SCOREBOARD_OBJECTIVES()).setScore(score--);

                int itemsToShow = Math.min(items.size(), 5);
                for (int i = 0; i < itemsToShow; i++) {
                    Material item = items.get(i);
                    boolean hasItem = player.getInventory().contains(item);
                    String status = hasItem ? Messages.SCOREBOARD_OBJECTIVE_CHECKED()
                            : Messages.SCOREBOARD_OBJECTIVE_UNCHECKED();
                    String itemName = item.name().replace("_", " ");
                    objective.getScore(status + "§f " + itemName).setScore(score--);
                }

                if (items.size() > 5) {
                    objective.getScore(Messages.SCOREBOARD_MORE_ITEMS(items.size() - 5)).setScore(score--);
                }
                objective.getScore("    ").setScore(score--);
            }
        }

        int totalAgreementSeconds = settings.getAgreementHours() * 3600
                + settings.getAgreementMinutes() * 60
                + settings.getAgreementSeconds();

        if (totalAgreementSeconds > 0) {
            objective.getScore(Messages.SCOREBOARD_AGREEMENT()).setScore(score--);
            int remainingSeconds = totalAgreementSeconds - agreementElapsedSeconds;

            if (remainingSeconds > 0) {
                int remHours = remainingSeconds / 3600;
                int remMinutes = (remainingSeconds % 3600) / 60;
                int remSeconds = remainingSeconds % 60;
                String timeStr = String.format("%02d:%02d:%02d", remHours, remMinutes, remSeconds);
                objective.getScore(Messages.SCOREBOARD_ENDS_IN(timeStr)).setScore(score--);
            } else {
                objective.getScore(Messages.SCOREBOARD_FINISHED()).setScore(score--);
            }
            objective.getScore("     ").setScore(score--);
        }
    }

    public void applyScoreboardToPlayer(Player player) {
        createPlayerScoreboard(player);
        updatePlayerScoreboard(player);
    }

    private List<Location> getSpawns() {

        String worldName = "world";
        int playerCount = playerManager.getPlayers().size();
        int size = settings.getMaxWorldSize();

        int spawnCount = switch (settings.getTeamMode()) {
            case MANUAL -> {
                int teamsCount = teamManager.getTeams().size();
                int playersWithoutTeam = (int) playerManager.getPlayers().stream()
                        .filter(p -> p.getTeam() == null)
                        .count();
                yield teamsCount + playersWithoutTeam;
            }
            case AUTO ->
                (int) Math.ceil(playerCount / (double) settings.getTeamSize());
            case IN_GAME ->
                playerCount;
            default ->
                playerCount;
        };

        List<Location> spawns = new ArrayList<>();
        World world = Bukkit.getWorld(worldName);

        if (spawnCount == 0) {
            return spawns;
        }

        if (spawnCount == 2) {
            spawns.add(createSpawn(world, -size, -size));
            spawns.add(createSpawn(world, size, size));
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
        int pointsPerSide = (int) Math.ceil(remainingSpawns / 4.0);

        for (int i = 1; i <= pointsPerSide && spawns.size() < spawnCount; i++) {
            double fraction = i / (double) (pointsPerSide + 1);
            int x = (int) (-size + 2 * size * fraction);
            spawns.add(createSpawn(world, x, -size));
        }

        for (int i = 1; i <= pointsPerSide && spawns.size() < spawnCount; i++) {
            double fraction = i / (double) (pointsPerSide + 1);
            int z = (int) (-size + 2 * size * fraction);
            spawns.add(createSpawn(world, size, z));
        }

        for (int i = 1; i <= pointsPerSide && spawns.size() < spawnCount; i++) {
            double fraction = i / (double) (pointsPerSide + 1);
            int x = (int) (size - 2 * size * fraction);
            spawns.add(createSpawn(world, x, size));
        }

        for (int i = 1; i <= pointsPerSide && spawns.size() < spawnCount; i++) {
            double fraction = i / (double) (pointsPerSide + 1);
            int z = (int) (size - 2 * size * fraction);
            spawns.add(createSpawn(world, -size, z));
        }

        return spawns;
    }

    private Location createSpawn(World world, int x, int z) {
        int worldY = world.getHighestBlockYAt(x, z) + 1;
        return new Location(world, x + 0.5, worldY, z + 0.5);
    }

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

    private void prepareTeams() {

        if (settings.getTeamMode() != TeamMode.AUTO) {
            if (settings.getTeamMode() == TeamMode.MANUAL && !teamManager.getTeams().isEmpty()) {
                teamsFormed = true;
            }
            return;
        }

        ArrayList<UHCPlayer> uhcPlayers = new ArrayList<>(playerManager.getPlayers());
        int playerCount = uhcPlayers.size();
        int teamSize = settings.getTeamSize();
        int teamCount = (int) Math.ceil(playerCount / (double) teamSize);
        Collections.shuffle(uhcPlayers);

        List<UHCTeam> teams = new ArrayList<>();
        for (int i = 0; i < teamCount; i++) {
            teams.add(null);
        }

        for (int i = 0; i < playerCount; i++) {
            int teamIndex = i % teamCount;
            UHCPlayer player = uhcPlayers.get(i);

            if (teams.get(teamIndex) == null) {
                UHCTeam team = teamManager.createTeam(player, "Team " + (teamIndex + 1));
                teams.set(teamIndex, team);
            } else {

                teamManager.addPlayer(teams.get(teamIndex), player);
            }
        }

        teamsFormed = true;

    }

    private void preparePlayers() {

        List<Location> spawns = getSpawns();

        if (teamsFormed && !teamManager.getTeams().isEmpty()) {

            ArrayList<UHCTeam> teams = teamManager.getTeams();

            for (int i = 0; i < teams.size(); i++) {

                UHCTeam team = teams.get(i);
                Location loc = spawns.get(i % spawns.size()).clone();

                for (UHCPlayer player : team.getMembers()) {

                    player.setSpawn(loc);
                    player.setLives(settings.getPlayerLives());

                    Player bukkitPlayer = Bukkit.getPlayer(player.getUuid());
                    if (bukkitPlayer != null) {
                        bukkitPlayer.teleport(loc);
                        bukkitPlayer.getInventory().addItem(new ItemStack(Material.OAK_BOAT));
                    }

                }

            }

            if (settings.getTeamMode() == TeamMode.MANUAL) {
                ArrayList<UHCPlayer> playersWithoutTeam = new ArrayList<>();
                for (UHCPlayer p : playerManager.getPlayers()) {
                    if (p.getTeam() == null) {
                        playersWithoutTeam.add(p);
                    }
                }

                if (!playersWithoutTeam.isEmpty()) {
                    int spawnOffset = teams.size();
                    for (int i = 0; i < playersWithoutTeam.size(); i++) {
                        UHCPlayer player = playersWithoutTeam.get(i);
                        Location loc = spawns.get((spawnOffset + i) % spawns.size()).clone();

                        player.setSpawn(loc);
                        player.setLives(settings.getPlayerLives());

                        Player bukkitPlayer = Bukkit.getPlayer(player.getUuid());
                        if (bukkitPlayer != null) {
                            bukkitPlayer.teleport(loc);
                            bukkitPlayer.getInventory().addItem(new ItemStack(Material.OAK_BOAT));
                        }
                    }
                }
            }

        } else {

            ArrayList<UHCPlayer> players = new ArrayList<>(plugin.getPlayerManager().getPlayers());

            for (int i = 0; i < players.size(); i++) {

                UHCPlayer player = players.get(i);
                Location loc = spawns.get(i % spawns.size()).clone();

                player.setSpawn(loc);
                player.setLives(settings.getPlayerLives());

                Player bukkitPlayer = Bukkit.getPlayer(player.getUuid());
                if (bukkitPlayer != null) {
                    bukkitPlayer.teleport(loc);
                    bukkitPlayer.getInventory().addItem(new ItemStack(Material.OAK_BOAT));
                }

            }

        }

    }

    private void startTimedTasks() {
        gameTimeTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            checkInGameTeamsState();
            checkGameState();
            checkAgreementState();
            checkBuffsState();
            checkWorldBorderState();
            checkSkinShuffleState();
            checkWinCondition();
        }, 0, 20);
    }

    private void checkGameState() {

        elapsedSeconds++;

        if (elapsedSeconds >= 60) {
            elapsedMinutes++;
            elapsedSeconds = 0;
        }

        if (elapsedMinutes >= 60) {
            elapsedHours++;
            elapsedMinutes = 0;
        }

        updateScoreboard();

    }

    private void checkSkinShuffleState() {
        if (!settings.isSkinShuffleEnabled()) {
            return;
        }

        skinShuffleElapsedSeconds++;

        int targetSeconds = settings.getSkinShuffleMinutes() * 60 + settings.getSkinShuffleSeconds();

        if (targetSeconds > 0 && skinShuffleElapsedSeconds >= targetSeconds) {
            skinShuffleElapsedSeconds = 0;
            plugin.getSkinManager().shuffleAndAssignSkins();
        }
    }

    private void checkInGameTeamsState() {

        if (settings.getTeamMode() != TeamMode.IN_GAME || !isCheckingProximity || settings.getTeamSize() <= 1) {
            return;
        }

        ArrayList<UHCPlayer> activePlayers = new ArrayList<>();
        for (UHCPlayer p : playerManager.getPlayers()) {
            if (p.isPlaying() && p.isAlive()) {
                activePlayers.add(p);
            }
        }

        for (UHCPlayer p1 : activePlayers) {
            for (UHCPlayer p2 : activePlayers) {

                if (p1.getUuid().equals(p2.getUuid())) {
                    continue;
                }

                Player bukkitP1 = Bukkit.getPlayer(p1.getUuid());
                Player bukkitP2 = Bukkit.getPlayer(p2.getUuid());

                if (bukkitP1 == null || bukkitP2 == null) {
                    continue;
                }

                if (bukkitP1.getGameMode() == org.bukkit.GameMode.SPECTATOR ||
                        bukkitP2.getGameMode() == org.bukkit.GameMode.SPECTATOR) {
                    continue;
                }

                Location loc1 = bukkitP1.getLocation();
                Location loc2 = bukkitP2.getLocation();
                if (loc1 == null || loc2 == null) {
                    continue;
                }

                if (!loc1.getWorld().equals(loc2.getWorld())) {
                    continue;
                }

                double distance = loc1.distance(loc2);

                UHCTeam team1 = p1.getTeam();
                UHCTeam team2 = p2.getTeam();

                boolean canTeamUp = false;

                if (team1 == null && team2 == null) {
                    canTeamUp = true;
                } else if (team1 == null && team2 != null) {
                    canTeamUp = team2.getMembers().size() < settings.getTeamSize();
                } else if (team1 != null && team2 == null) {
                    canTeamUp = team1.getMembers().size() < settings.getTeamSize();
                } else if (team1 != null && team2 != null && team1 != team2) {
                    int totalMembers = team1.getMembers().size() + team2.getMembers().size();
                    canTeamUp = totalMembers <= settings.getTeamSize();
                }

                if (!canTeamUp) {
                    continue;
                }

                if (distance <= 200) {
                    bukkitP1.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20, 1));
                    bukkitP2.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20, 1));

                    int distanceBlocks = (int) Math.round(distance);
                    bukkitP1.sendActionBar(Component.text(Messages.LOCATOR_BAR_NEARBY_PLAYER(distanceBlocks)));
                    bukkitP2.sendActionBar(Component.text(Messages.LOCATOR_BAR_NEARBY_PLAYER(distanceBlocks)));
                }

                if (distance > 5) {
                    continue;
                }

                if (team1 == null && team2 == null) {

                    UHCTeam newTeam = teamManager.createTeam(p1, "Team " + (teamManager.getTeams().size() + 1));
                    teamManager.addPlayer(newTeam, p2);
                    bukkitP1.sendMessage(Messages.UHC_TEAM_FORMED(p2.getName()));
                    bukkitP2.sendMessage(Messages.UHC_TEAM_FORMED(p1.getName()));
                } else if (team1 == null && team2 != null) {

                    if (team2.getMembers().size() < settings.getTeamSize()) {
                        teamManager.addPlayer(team2, p1);
                        bukkitP1.sendMessage(Messages.UHC_YOU_JOINED(team2.getName()));
                        bukkitP2.sendMessage(Messages.UHC_PLAYER_JOINED_TEAM(p1.getName()));
                    }
                } else if (team1 != null && team2 == null) {

                    if (team1.getMembers().size() < settings.getTeamSize()) {
                        teamManager.addPlayer(team1, p2);
                        bukkitP2.sendMessage(Messages.UHC_YOU_JOINED(team1.getName()));
                        bukkitP1.sendMessage(Messages.UHC_PLAYER_JOINED_TEAM(p2.getName()));
                    }
                } else if (team1 != null && team2 != null && team1 != team2) {

                    int totalMembers = team1.getMembers().size() + team2.getMembers().size();
                    if (totalMembers <= settings.getTeamSize()) {

                        ArrayList<UHCPlayer> team2Members = new ArrayList<>(team2.getMembers());
                        for (UHCPlayer member : team2Members) {
                            teamManager.removePlayer(team2, member);
                            teamManager.addPlayer(team1, member);
                            Player bukkitMember = Bukkit.getPlayer(member.getUuid());
                            if (bukkitMember != null) {
                                bukkitMember.sendMessage(Messages.UHC_TEAMS_MERGED(team1.getName()));
                            }
                        }
                    }
                }

            }
        }

        int maxTeamFormationSeconds = settings.getMaxTeamInGameHours() * 3600
                + settings.getMaxTeamInGameMinutes() * 60
                + settings.getMaxTeamInGameSeconds();

        int currentElapsedSeconds = elapsedHours * 3600 + elapsedMinutes * 60 + elapsedSeconds;

        if (maxTeamFormationSeconds > 0 && currentElapsedSeconds >= maxTeamFormationSeconds) {
            isCheckingProximity = false;
            teamsFormed = true;
        }

    }

    private void checkAgreementState() {

        int totalAgreementSeconds = settings.getAgreementHours() * 3600
                + settings.getAgreementMinutes() * 60
                + settings.getAgreementSeconds();

        if (totalAgreementSeconds <= 0) {
            agreementActive = false;
            return;
        }

        agreementElapsedSeconds++;

        int remainingSeconds = totalAgreementSeconds - agreementElapsedSeconds;

        if (remainingSeconds > 0) {
            agreementActive = true;

            if (remainingSeconds < 11) {
                broadcast(Messages.PVP_WARNING_SECONDS(remainingSeconds));
                Bukkit.getOnlinePlayers().forEach(p -> {
                    Location loc = p.getLocation();
                    if (loc != null) {
                        p.playSound(loc, Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.5f);
                    }
                });
            }

        } else if (remainingSeconds == 0) {

            agreementActive = false;

            broadcast("");
            broadcast(Messages.PVP_ACTIVATED());
            broadcast(Messages.PVP_ACTIVATED_LINE());
            broadcast(Messages.PVP_ACTIVATED());
            broadcast("");

            Bukkit.getOnlinePlayers().forEach(p -> {
                sendTitle(p,
                        Messages.PVP_ACTIVATED_TITLE(),
                        Messages.PVP_ACTIVATED_SUBTITLE());
                Location loc = p.getLocation();
                if (loc != null) {
                    p.playSound(loc, Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
                }
            });

        } else {

            agreementActive = false;
        }

    }

    private void checkBuffsState() {
        if (!settings.isBuffsEnabled()) {
            return;
        }

        int totalBuffsSeconds = settings.getBuffsHours() * 3600
                + settings.getBuffsMinutes() * 60
                + settings.getBuffsSeconds();

        if (totalBuffsSeconds <= 0) {
            return;
        }

        buffsElapsedSeconds++;

        int remainingSeconds = totalBuffsSeconds - buffsElapsedSeconds;

        if (remainingSeconds == 0 && !buffsApplied) {
            buffsApplied = true;

            broadcast("");
            broadcast(Messages.BUFFS_ACTIVATED_BORDER());
            broadcast("");
            broadcast(Messages.BUFFS_ACTIVATED_TITLE());
            broadcast("");
            broadcast(Messages.BUFFS_ACTIVATED_HEARTS((int) settings.getExtraHearts()));
            broadcast(Messages.BUFFS_ACTIVATED_RESISTANCE());
            broadcast("");
            broadcast(Messages.BUFFS_ACTIVATED_BORDER());
            broadcast("");

            Bukkit.getOnlinePlayers().forEach(p -> {
                AttributeInstance maxHealthAttr = p.getAttribute(Attribute.MAX_HEALTH);
                if (maxHealthAttr != null) {
                    double currentMaxHealth = maxHealthAttr.getValue();
                    double newMaxHealth = currentMaxHealth + (settings.getExtraHearts() * 2.0);
                    maxHealthAttr.setBaseValue(newMaxHealth);
                    p.setHealth(newMaxHealth);
                }

                p.addPotionEffect(new PotionEffect(
                        PotionEffectType.RESISTANCE,
                        Integer.MAX_VALUE,
                        1,
                        false,
                        true,
                        true));

                sendTitle(p,
                        Messages.BUFFS_ACTIVATED_TITLE_SCREEN(),
                        Messages.BUFFS_ACTIVATED_SUBTITLE((int) settings.getExtraHearts()));

                Location loc = p.getLocation();
                if (loc != null) {
                    p.playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                }
            });

        }

    }

    private void checkWorldBorderState() {
        int maxWorldSize = settings.getMaxWorldSize();
        int minWorldSize = settings.getMinWorldSize();
        int maxHours = settings.getGameHours();
        int maxMinutes = settings.getGameMinutes();
        int maxSeconds = settings.getGameSeconds();

        int totalGameSeconds = maxHours * 3600 + maxMinutes * 60 + maxSeconds;
        int currentElapsedSeconds = elapsedHours * 3600 + elapsedMinutes * 60 + elapsedSeconds;

        if (settings.isGradualBorderEnabled()) {
            // Legacy mode - keep for backwards compatibility
            double totalBorderChange = (maxWorldSize - minWorldSize) * 2.0;
            double borderStep = totalBorderChange / totalGameSeconds;

            for (World world : Bukkit.getWorlds()) {
                WorldBorder border = world.getWorldBorder();
                double newSize = border.getSize() - borderStep;
                newSize = Math.max(1.0, Math.min(newSize, 5.9999968E7));
                border.setSize(newSize);
            }
        } else {
            // New BorderType system
            switch (settings.getBorderType()) {
                case NONE -> {
                    // Border never shrinks - do nothing
                }
                case GRADUAL -> {
                    double totalBorderChange = (maxWorldSize - minWorldSize) * 2.0;
                    double borderStep = totalBorderChange / totalGameSeconds;

                    for (World world : Bukkit.getWorlds()) {
                        WorldBorder border = world.getWorldBorder();
                        double newSize = border.getSize() - borderStep;
                        newSize = Math.max(1.0, Math.min(newSize, 5.9999968E7));
                        border.setSize(newSize);
                    }
                }
                case INSTANT -> {
                    if (currentElapsedSeconds >= totalGameSeconds) {
                        for (World world : Bukkit.getWorlds()) {
                            WorldBorder border = world.getWorldBorder();
                            double newSize = minWorldSize * 2.0;
                            newSize = Math.max(1.0, Math.min(newSize, 5.9999968E7));
                            border.setSize(newSize);
                        }
                    }
                }
                case THRESHOLD -> {
                    int thresholdStartSeconds = settings.getThresholdStartHours() * 3600
                            + settings.getThresholdStartMinutes() * 60
                            + settings.getThresholdStartSeconds();
                    int thresholdEndSeconds = settings.getThresholdEndHours() * 3600
                            + settings.getThresholdEndMinutes() * 60
                            + settings.getThresholdEndSeconds();

                    // Only shrink border if we've passed the start threshold
                    if (currentElapsedSeconds >= thresholdStartSeconds) {
                        if (currentElapsedSeconds >= thresholdEndSeconds) {
                            // Border should be at minimum size
                            for (World world : Bukkit.getWorlds()) {
                                WorldBorder border = world.getWorldBorder();
                                double newSize = minWorldSize * 2.0;
                                newSize = Math.max(1.0, Math.min(newSize, 5.9999968E7));
                                border.setSize(newSize);
                            }
                        } else {
                            // Gradually shrink from start to end time
                            int shrinkDuration = thresholdEndSeconds - thresholdStartSeconds;
                            if (shrinkDuration > 0) {
                                double totalBorderChange = (maxWorldSize - minWorldSize) * 2.0;
                                double borderStep = totalBorderChange / shrinkDuration;

                                for (World world : Bukkit.getWorlds()) {
                                    WorldBorder border = world.getWorldBorder();
                                    double newSize = border.getSize() - borderStep;
                                    newSize = Math.max(1.0, Math.min(newSize, 5.9999968E7));
                                    border.setSize(newSize);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean isAgreementActive() {
        return agreementActive;
    }

    public int getAgreementRemainingSeconds() {
        int totalAgreementSeconds = settings.getAgreementHours() * 3600
                + settings.getAgreementMinutes() * 60
                + settings.getAgreementSeconds();
        return Math.max(0, totalAgreementSeconds - agreementElapsedSeconds);
    }

    public int getElapsedHours() {
        return elapsedHours;
    }

    public int getElapsedMinutes() {
        return elapsedMinutes;
    }

    public int getElapsedSeconds() {
        return elapsedSeconds;
    }

    public void setTeamsFormed(boolean formed) {
        this.teamsFormed = formed;
    }

    public boolean areTeamsFormed() {
        return teamsFormed;
    }

    private void checkWinCondition() {

        int totalElapsedSeconds = elapsedHours * 3600 + elapsedMinutes * 60 + elapsedSeconds;
        if (totalElapsedSeconds < 5) {
            return;
        }

        long alivePlayers = playerManager.getPlayers().stream()
                .filter(p -> p.isPlaying() && p.isAlive())
                .count();

        boolean hasActualTeams = false;
        if (!teamManager.getTeams().isEmpty()) {
            if (settings.getTeamMode() == TeamMode.MANUAL) {
                hasActualTeams = true;
            } else if (settings.getTeamMode() == TeamMode.IN_GAME) {
                hasActualTeams = teamManager.getTeams().stream()
                        .anyMatch(team -> team.getMembers().size() > 1);
            } else if (teamsFormed) {
                hasActualTeams = teamManager.getTeams().stream()
                        .anyMatch(team -> team.getMembers().size() > 1);
            }
        }

        if (hasActualTeams) {
            List<UHCTeam> aliveTeams = teamManager.getTeams().stream()
                    .filter(team -> team.getMembers().stream().anyMatch(p -> p.isPlaying() && p.isAlive()))
                    .collect(java.util.stream.Collectors.toList());

            long alivePlayersWithoutTeam = playerManager.getPlayers().stream()
                    .filter(p -> p.isPlaying() && p.isAlive() && p.getTeam() == null)
                    .count();

            if (aliveTeams.size() == 1 && alivePlayersWithoutTeam == 0) {
                declareTeamWinner(aliveTeams.get(0));
            } else if (aliveTeams.isEmpty() && alivePlayersWithoutTeam == 1) {
                UHCPlayer winner = playerManager.getPlayers().stream()
                        .filter(p -> p.isPlaying() && p.isAlive() && p.getTeam() == null)
                        .findFirst()
                        .orElse(null);
                if (winner != null) {
                    declarePlayerWinner(winner);
                }
            } else if (aliveTeams.isEmpty() && alivePlayersWithoutTeam == 0) {
                declareDraw();
            }

        } else {
            if (alivePlayers == 1) {
                UHCPlayer winner = playerManager.getPlayers().stream()
                        .filter(p -> p.isPlaying() && p.isAlive())
                        .findFirst()
                        .orElse(null);

                if (winner != null) {
                    declarePlayerWinner(winner);
                }
            } else if (alivePlayers == 0) {
                declareDraw();
            }
        }
    }

    private void declarePlayerWinner(UHCPlayer winner) {

        settings.setGameState(GameState.ENDED);

        if (gameTimeTask != null) {
            gameTimeTask.cancel();
        }

        plugin.getSkinManager().restoreAllSkins();

        gameStats.setGameEndTime(System.currentTimeMillis());
        gameStats.setWinner(winner.getName());

        broadcast("");
        broadcast(Messages.VICTORY_HEADER());
        broadcast(Messages.VICTORY_TITLE_LINE());
        broadcast("");
        broadcast(Messages.VICTORY_WINNER_SOLO(winner.getName()));
        broadcast("");
        broadcast(Messages.VICTORY_GAME_DURATION(gameStats.getFormattedDuration()));
        broadcast(Messages.VICTORY_FOOTER());
        broadcast("");

        Bukkit.getOnlinePlayers().forEach(p -> {
            sendTitle(p,
                    Messages.VICTORY_PLAYER_TITLE(winner.getName()),
                    Messages.VICTORY_TITLE_WON());
        });

        Player bukkitWinner = winner.getBukkitPlayer();
        if (bukkitWinner != null && bukkitWinner.isOnline()) {
            Location winnerLoc = bukkitWinner.getLocation();
            if (winnerLoc != null) {
                launchFireworks(winnerLoc, 10);
                bukkitWinner.playSound(winnerLoc, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
            }
        }

        Bukkit.getOnlinePlayers().forEach(p -> {
            Location loc = p.getLocation();
            if (loc != null) {
                p.playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            }
        });
    }

    private void declareTeamWinner(UHCTeam winningTeam) {

        settings.setGameState(GameState.ENDED);

        if (gameTimeTask != null) {
            gameTimeTask.cancel();
        }

        plugin.getSkinManager().restoreAllSkins();

        gameStats.setGameEndTime(System.currentTimeMillis());
        gameStats.setWinner(Messages.VICTORY_TEAM_PREFIX() + winningTeam.getName());

        broadcast("");
        broadcast(Messages.VICTORY_HEADER());
        broadcast(Messages.VICTORY_TITLE_LINE());
        broadcast("");
        broadcast(Messages.VICTORY_WINNING_TEAM(winningTeam.getName()));
        broadcast("");
        broadcast(Messages.VICTORY_TEAM_MEMBERS());

        for (UHCPlayer member : winningTeam.getMembers()) {
            String status = member.isAlive() ? Messages.VICTORY_MEMBER_ALIVE() : Messages.VICTORY_MEMBER_DEAD();
            broadcast(Messages.VICTORY_MEMBER_STATUS(status, member.getName()));
        }

        broadcast("");
        broadcast(Messages.VICTORY_GAME_DURATION(gameStats.getFormattedDuration()));
        broadcast(Messages.VICTORY_FOOTER());
        broadcast("");

        Bukkit.getOnlinePlayers().forEach(p -> {
            sendTitle(p,
                    Messages.VICTORY_TEAM_TITLE(winningTeam.getName()),
                    Messages.VICTORY_TITLE_WON());
        });

        for (UHCPlayer member : winningTeam.getMembers()) {
            Player bukkitPlayer = member.getBukkitPlayer();
            if (bukkitPlayer != null && bukkitPlayer.isOnline()) {
                Location memberLoc = bukkitPlayer.getLocation();
                if (memberLoc != null) {
                    launchFireworks(memberLoc, 5);
                    bukkitPlayer.playSound(memberLoc, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
                }
            }
        }

        Bukkit.getOnlinePlayers().forEach(p -> {
            Location loc = p.getLocation();
            if (loc != null) {
                p.playSound(loc, Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            }
        });
    }

    private void declareDraw() {

        settings.setGameState(GameState.ENDED);

        plugin.getSkinManager().restoreAllSkins();

        if (gameTimeTask != null) {
            gameTimeTask.cancel();
        }

        gameStats.setWinner(Messages.DRAW_WINNER_LABEL());

        broadcast("");
        broadcast(Messages.DRAW_HEADER());
        broadcast(Messages.DRAW_TITLE_LINE());
        broadcast("");
        broadcast(Messages.DRAW_ALL_ELIMINATED());
        broadcast(Messages.DRAW_DURATION(gameStats.getFormattedDuration()));
        broadcast(Messages.DRAW_FOOTER());
        broadcast("");

        Bukkit.getOnlinePlayers().forEach(p -> {
            sendTitle(p, Messages.DRAW_TITLE_SCREEN(), Messages.DRAW_NO_WINNER());
        });
    }

    private void launchFireworks(Location location, int count) {
        for (int i = 0; i < count; i++) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                Firework firework = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK_ROCKET);
                FireworkMeta meta = firework.getFireworkMeta();

                FireworkEffect effect = FireworkEffect.builder()
                        .with(FireworkEffect.Type.BALL_LARGE)
                        .withColor(Color.YELLOW, Color.ORANGE, Color.RED)
                        .withFade(Color.PURPLE)
                        .flicker(true)
                        .trail(true)
                        .build();

                meta.addEffect(effect);
                meta.setPower(1);
                firework.setFireworkMeta(meta);
            }, i * 10L);
        }
    }

    private void sendTitle(Player player, String title, String subtitle) {
        Title adventureTitle = Title.title(
                LegacyComponentSerializer.legacySection().deserialize(title),
                LegacyComponentSerializer.legacySection().deserialize(subtitle),
                Title.Times.times(Duration.ofMillis(500), Duration.ofMillis(3500), Duration.ofMillis(1000)));
        player.showTitle(adventureTitle);
    }

    private void broadcast(String message) {
        if (message == null || message.isEmpty()) {
            Bukkit.getServer().broadcast(Component.empty());
            return;
        }
        Bukkit.getServer().broadcast(LegacyComponentSerializer.legacySection().deserialize(message));
    }

}
