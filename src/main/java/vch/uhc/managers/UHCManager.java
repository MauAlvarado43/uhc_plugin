package vch.uhc.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import vch.uhc.UHC;
import vch.uhc.misc.Settings;
import vch.uhc.models.GameStats;
import vch.uhc.models.Team;

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

    public void start() {

        settings.setGameStatus(Settings.GameStatus.IN_PROGRESS);

        plugin.getGameModeManager().initializeGameMode();
        currentWorldSize = settings.getMaxWorldSize();
        teamsFormed = false;

        int totalAgreementSeconds = settings.getAgreementHours() * 3600 + 
                                   settings.getAgreementMinutes() * 60 + 
                                   settings.getAgreementSeconds();
        agreementActive = totalAgreementSeconds > 0;
        agreementElapsedSeconds = 0;

        gameStats = new GameStats();

        settings.getItems().forEach(item -> {
            if (Bukkit.getRecipe(item.getKey()) == null && item.isEnabled()) {
                item.register();
            }
        });

        prepareGame();
        prepareScoreboard();
        prepareTeams();
        preparePlayers();
        
        
        World world = Bukkit.getWorld("world");
        if (world != null) {
            world.setStorm(false);
            world.setThundering(false);
            world.setWeatherDuration(Integer.MAX_VALUE);
            world.setTime(1000);
        }
        
        startTimedTasks();

    }

    public void pause() {
        settings.setGameStatus(Settings.GameStatus.PAUSED);
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
        if (gameTimeTask != null) {
            gameTimeTask.cancel();
        }
        

        plugin.getSkinManager().restoreAllSkins();
        plugin.getSkinManager().clearAssignments();
    }

    public void reload() {
        
        if (settings.getGameStatus() != Settings.GameStatus.NONE) {
            cancel();
        }
        
        if (gameTimeTask != null) {
            gameTimeTask.cancel();
            gameTimeTask = null;
        }
        
        plugin.getSkinManager().restoreAllSkins();
        plugin.getSkinManager().clearAssignments();
        
        Bukkit.getOnlinePlayers().forEach(p -> {
            org.bukkit.scoreboard.Scoreboard scoreboard = p.getScoreboard();
            if (scoreboard != null) {
                org.bukkit.scoreboard.Objective objective = scoreboard.getObjective("uhc");
                if (objective != null) {
                    objective.unregister();
                }
                org.bukkit.scoreboard.Objective healthObjective = scoreboard.getObjective("health");
                if (healthObjective != null) {
                    healthObjective.unregister();
                }
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
            world.setGameRule(GameRule.NATURAL_REGENERATION, true);
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
            p.setMaxHealth(20.0);
            p.setHealth(20.0);
        });
    }

    private void prepareGame() {

        Bukkit.getWorlds().forEach(world -> {
            world.setGameRule(GameRule.NATURAL_REGENERATION, false);
        });

        Bukkit.getWorlds().forEach(world -> {
            world.getWorldBorder().setCenter(0, 0);
            world.getWorldBorder().setSize((currentWorldSize * 2) + 4);
        });

    }

    private void prepareScoreboard() {
        Bukkit.getOnlinePlayers().forEach(this::createPlayerScoreboard);
    }

    private void createPlayerScoreboard(Player player) {
        org.bukkit.scoreboard.ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) return;

        org.bukkit.scoreboard.Scoreboard scoreboard = manager.getNewScoreboard();
        
        org.bukkit.scoreboard.Objective objective = scoreboard.registerNewObjective(
            "uhc", 
            "dummy", 
            vch.uhc.misc.Messages.UHC_SCOREBOARD_TITLE()
        );
        objective.setDisplaySlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);

        player.setScoreboard(scoreboard);
    }

    public void updateScoreboard() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updatePlayerScoreboard(player);
        }
    }

    private void updatePlayerScoreboard(Player player) {
        org.bukkit.scoreboard.Scoreboard scoreboard = player.getScoreboard();
        org.bukkit.scoreboard.Objective objective = scoreboard.getObjective("uhc");
        
        if (objective == null) {
            createPlayerScoreboard(player);
            scoreboard = player.getScoreboard();
            objective = scoreboard.getObjective("uhc");
            if (objective == null) return;
        }

        for (String entry : scoreboard.getEntries()) {
            scoreboard.resetScores(entry);
        }

        int score = 15;

        objective.getScore(" ").setScore(score--);
        objective.getScore(vch.uhc.misc.Messages.UHC_SCOREBOARD_TIME(elapsedHours, elapsedMinutes, elapsedSeconds)).setScore(score--);
        objective.getScore("  ").setScore(score--);

        vch.uhc.models.Player uhcPlayer = playerManager.getPlayerByUUID(player.getUniqueId());
        if (uhcPlayer != null && uhcPlayer.getTeam() != null) {
            Team team = uhcPlayer.getTeam();
            objective.getScore("§a§lEquipo: §f" + team.getName()).setScore(score--);
            
            for (vch.uhc.models.Player member : team.getMembers()) {
                if (!member.getUuid().equals(player.getUniqueId())) {
                    Player memberPlayer = Bukkit.getPlayer(member.getUuid());
                    if (memberPlayer != null && member.isAlive()) {
                        double health = memberPlayer.getHealth();
                        String healthDisplay = String.format("§c❤ §f%.1f", health);
                        objective.getScore("§f" + member.getName() + " " + healthDisplay).setScore(score--);
                    } else if (!member.isAlive()) {
                        objective.getScore("§7§m" + member.getName() + " §c✖").setScore(score--);
                    }
                }
            }
            objective.getScore("   ").setScore(score--);
        }

        if (settings.getGameMode() == Settings.GameMode.RESOURCE_RUSH) {
            List<Material> items = plugin.getGameModeManager().getResourceRushItems();
            if (items != null && !items.isEmpty()) {
                objective.getScore("§e§lObjetivos:").setScore(score--);
                
                int itemsToShow = Math.min(items.size(), 5);
                for (int i = 0; i < itemsToShow; i++) {
                    Material item = items.get(i);
                    boolean hasItem = player.getInventory().contains(item);
                    String status = hasItem ? "§a✔" : "§7○";
                    String itemName = item.name().replace("_", " ");
                    objective.getScore(status + "§f " + itemName).setScore(score--);
                }
                
                if (items.size() > 5) {
                    objective.getScore("§7... y " + (items.size() - 5) + " más").setScore(score--);
                }
                objective.getScore("    ").setScore(score--);
            }
        }

        int totalAgreementSeconds = settings.getAgreementHours() * 3600 + 
                                   settings.getAgreementMinutes() * 60 + 
                                   settings.getAgreementSeconds();
        
        if (totalAgreementSeconds > 0) {
            objective.getScore("§6§lPacto de Caballeros:").setScore(score--);
            int remainingSeconds = totalAgreementSeconds - agreementElapsedSeconds;
            
            if (remainingSeconds > 0) {
                int remHours = remainingSeconds / 3600;
                int remMinutes = (remainingSeconds % 3600) / 60;
                int remSeconds = remainingSeconds % 60;
                objective.getScore("§eTermina en: §f" + 
                    String.format("%02d:%02d:%02d", remHours, remMinutes, remSeconds)).setScore(score--);
            } else {
                objective.getScore("§c¡Finalizado!").setScore(score--);
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
        

        int spawnCount;
        if (settings.getTeamMode() == Settings.TeamMode.MANUAL) {
            int teamsCount = teamManager.getTeams().size();
            int playersWithoutTeam = (int) playerManager.getPlayers().stream()
                .filter(p -> p.getTeam() == null)
                .count();
            spawnCount = teamsCount + playersWithoutTeam;
        } else if (settings.getTeamMode() == Settings.TeamMode.AUTO) {
            spawnCount = (int) Math.ceil(playerCount / (double) settings.getTeamSize());
        } else {
            spawnCount = playerCount;
        }
        
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
            double fraction = i / (double)(pointsPerSide + 1);
            int x = (int)(-size + 2 * size * fraction);
            spawns.add(createSpawn(world, x, -size));
        }
        

        for (int i = 1; i <= pointsPerSide && spawns.size() < spawnCount; i++) {
            double fraction = i / (double)(pointsPerSide + 1);
            int z = (int)(-size + 2 * size * fraction);
            spawns.add(createSpawn(world, size, z));
        }
        

        for (int i = 1; i <= pointsPerSide && spawns.size() < spawnCount; i++) {
            double fraction = i / (double)(pointsPerSide + 1);
            int x = (int)(size - 2 * size * fraction);
            spawns.add(createSpawn(world, x, size));
        }
        

        for (int i = 1; i <= pointsPerSide && spawns.size() < spawnCount; i++) {
            double fraction = i / (double)(pointsPerSide + 1);
            int z = (int)(size - 2 * size * fraction);
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
        
        
        int safeSize = (int)(borderSize * 0.9);
        int newX = (int)(Math.random() * safeSize * 2 - safeSize);
        int newZ = (int)(Math.random() * safeSize * 2 - safeSize);
        
        return createSpawn(world, newX, newZ);
    }

    private void prepareTeams() {

        if (settings.getTeamMode() != Settings.TeamMode.AUTO) {
            if (settings.getTeamMode() == Settings.TeamMode.MANUAL && !teamManager.getTeams().isEmpty()) {
                teamsFormed = true;
                for (Team team : teamManager.getTeams()) {
                }
            }
            return;
        }

        ArrayList<vch.uhc.models.Player> uhcPlayers = new ArrayList<>(playerManager.getPlayers());
        int playerCount = uhcPlayers.size();
        int teamSize = settings.getTeamSize();
        int teamCount = (int) Math.ceil(playerCount / (double) teamSize);
        Collections.shuffle(uhcPlayers);

        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < teamCount; i++) {
            teams.add(null);
        }


        for (int i = 0; i < playerCount; i++) {
            int teamIndex = i % teamCount;
            vch.uhc.models.Player player = uhcPlayers.get(i);
            

            if (teams.get(teamIndex) == null) {
                Team team = teamManager.createTeam(player, "Team " + (teamIndex + 1));
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

            ArrayList<Team> teams = teamManager.getTeams();

            for (int i = 0; i < teams.size(); i++) {

                Team team = teams.get(i);
                Location loc = spawns.get(i % spawns.size()).clone();

                for (vch.uhc.models.Player player : team.getMembers()) {

                    player.setSpawn(loc);
                    player.setLives(settings.getPlayerLives());

                    Player bukkitPlayer = Bukkit.getPlayer(player.getUuid());
                    if (bukkitPlayer != null) {
                        bukkitPlayer.teleport(loc);
                        bukkitPlayer.getInventory().addItem(new org.bukkit.inventory.ItemStack(org.bukkit.Material.OAK_BOAT));
                    }

                }

            }
            
            if (settings.getTeamMode() == Settings.TeamMode.MANUAL) {
                ArrayList<vch.uhc.models.Player> playersWithoutTeam = new ArrayList<>();
                for (vch.uhc.models.Player p : playerManager.getPlayers()) {
                    if (p.getTeam() == null) {
                        playersWithoutTeam.add(p);
                    }
                }
                
                if (!playersWithoutTeam.isEmpty()) {
                    int spawnOffset = teams.size();
                    for (int i = 0; i < playersWithoutTeam.size(); i++) {
                        vch.uhc.models.Player player = playersWithoutTeam.get(i);
                        Location loc = spawns.get((spawnOffset + i) % spawns.size()).clone();
                        
                        player.setSpawn(loc);
                        player.setLives(settings.getPlayerLives());
                        
                        Player bukkitPlayer = Bukkit.getPlayer(player.getUuid());
                        if (bukkitPlayer != null) {
                            bukkitPlayer.teleport(loc);
                            bukkitPlayer.getInventory().addItem(new org.bukkit.inventory.ItemStack(org.bukkit.Material.OAK_BOAT));
                        }
                    }
                }
            }

        } else {

            ArrayList<vch.uhc.models.Player> players = new ArrayList<>(plugin.getPlayerManager().getPlayers());

            for (int i = 0; i < players.size(); i++) {

                vch.uhc.models.Player player = players.get(i);
                Location loc = spawns.get(i % spawns.size()).clone();

                player.setSpawn(loc);
                player.setLives(settings.getPlayerLives());
                
                Player bukkitPlayer = Bukkit.getPlayer(player.getUuid());
                if (bukkitPlayer != null) {
                    bukkitPlayer.teleport(loc);
                    bukkitPlayer.getInventory().addItem(new org.bukkit.inventory.ItemStack(org.bukkit.Material.OAK_BOAT));
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
        }
    }

    private void checkInGameTeamsState() {

        if (settings.getTeamMode() != Settings.TeamMode.IN_GAME || !isCheckingProximity) {
            return;
        }

        boolean canCreateTeams = false;

        for (vch.uhc.models.Player p1 : playerManager.getPlayers()) {
            for (vch.uhc.models.Player p2 : playerManager.getPlayers()) {

                boolean canJoin = teamManager.canPlayersJoinTeam(p1, p2);
                if (!canJoin) {
                    continue;
                }

                canCreateTeams = true;
                
                Player bukkitP1 = Bukkit.getPlayer(p1.getUuid());
                Player bukkitP2 = Bukkit.getPlayer(p2.getUuid());
                

                if (bukkitP1 == null || bukkitP2 == null) {
                    continue;
                }
                
                double distance = bukkitP1.getLocation().distance(bukkitP2.getLocation());
                
                if (distance <= 200) {

                    bukkitP1.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20, 1));
                    bukkitP2.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20, 1));
                    

                    int distanceBlocks = (int) Math.round(distance);
                    bukkitP1.spigot().sendMessage(ChatMessageType.ACTION_BAR, 
                        new TextComponent(vch.uhc.misc.Messages.LOCATOR_BAR_NEARBY_PLAYER(p2.getName(), distanceBlocks)));
                    bukkitP2.spigot().sendMessage(ChatMessageType.ACTION_BAR, 
                        new TextComponent(vch.uhc.misc.Messages.LOCATOR_BAR_NEARBY_PLAYER(p1.getName(), distanceBlocks)));
                }
                

                if (distance > 5) {
                    continue;
                }
                

                Team team1 = p1.getTeam();
                Team team2 = p2.getTeam();
                
                if (team1 == null && team2 == null) {

                    Team newTeam = teamManager.createTeam(p1, "Team " + (teamManager.getTeams().size() + 1));
                    teamManager.addPlayer(newTeam, p2);
                    bukkitP1.sendMessage(vch.uhc.misc.Messages.UHC_TEAM_FORMED(p2.getName()));
                    bukkitP2.sendMessage(vch.uhc.misc.Messages.UHC_TEAM_FORMED(p1.getName()));
                } else if (team1 == null && team2 != null) {

                    if (team2.getMembers().size() < settings.getTeamSize()) {
                        teamManager.addPlayer(team2, p1);
                        bukkitP1.sendMessage(vch.uhc.misc.Messages.UHC_YOU_JOINED(team2.getName()));
                        bukkitP2.sendMessage(vch.uhc.misc.Messages.UHC_PLAYER_JOINED_TEAM(p1.getName()));
                    }
                } else if (team1 != null && team2 == null) {

                    if (team1.getMembers().size() < settings.getTeamSize()) {
                        teamManager.addPlayer(team1, p2);
                        bukkitP2.sendMessage(vch.uhc.misc.Messages.UHC_YOU_JOINED(team1.getName()));
                        bukkitP1.sendMessage(vch.uhc.misc.Messages.UHC_PLAYER_JOINED_TEAM(p2.getName()));
                    }
                } else if (team1 != team2) {

                    int totalMembers = team1.getMembers().size() + team2.getMembers().size();
                    if (totalMembers <= settings.getTeamSize()) {

                        java.util.ArrayList<vch.uhc.models.Player> team2Members = new java.util.ArrayList<>(team2.getMembers());
                        for (vch.uhc.models.Player member : team2Members) {
                            teamManager.removePlayer(team2, member);
                            teamManager.addPlayer(team1, member);
                            Player bukkitMember = Bukkit.getPlayer(member.getUuid());
                            if (bukkitMember != null) {
                                bukkitMember.sendMessage(vch.uhc.misc.Messages.UHC_TEAMS_MERGED(team1.getName()));
                            }
                        }
                    }
                }

            }
        }

        if (!canCreateTeams) {
            isCheckingProximity = false;
            teamsFormed = true;
        }

    }

    private void checkAgreementState() {

        int totalAgreementSeconds = settings.getAgreementHours() * 3600 + 
                                   settings.getAgreementMinutes() * 60 + 
                                   settings.getAgreementSeconds();


        if (totalAgreementSeconds <= 0) {
            agreementActive = false;
            return;
        }


        agreementElapsedSeconds++;


        int remainingSeconds = totalAgreementSeconds - agreementElapsedSeconds;


        if (remainingSeconds > 0) {
            agreementActive = true;


            if (remainingSeconds < 11) {
                Bukkit.broadcastMessage(vch.uhc.misc.Messages.PVP_WARNING_SECONDS(remainingSeconds));
                Bukkit.getOnlinePlayers().forEach(p -> 
                    p.playSound(p.getLocation(), org.bukkit.Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.5f)
                );
            }

        } else if (remainingSeconds == 0) {

            agreementActive = false;
            
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(vch.uhc.misc.Messages.PVP_ACTIVATED());
            Bukkit.broadcastMessage(vch.uhc.misc.Messages.PVP_ACTIVATED_LINE());
            Bukkit.broadcastMessage(vch.uhc.misc.Messages.PVP_ACTIVATED());
            Bukkit.broadcastMessage("");
            

            Bukkit.getOnlinePlayers().forEach(p -> {
                p.sendTitle(
                    vch.uhc.misc.Messages.PVP_ACTIVATED_TITLE(),
                    vch.uhc.misc.Messages.PVP_ACTIVATED_SUBTITLE(),
                    10, 70, 20
                );
                p.playSound(p.getLocation(), org.bukkit.Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
            });
            
        } else {

            agreementActive = false;
        }

    }

    private void checkBuffsState() {
        if (!settings.isBuffsEnabled()) {
            return;
        }

        int totalBuffsSeconds = settings.getBuffsHours() * 3600 + 
                               settings.getBuffsMinutes() * 60 + 
                               settings.getBuffsSeconds();

        if (totalBuffsSeconds <= 0) {
            return;
        }

        buffsElapsedSeconds++;

        int remainingSeconds = totalBuffsSeconds - buffsElapsedSeconds;

        if (remainingSeconds == 0 && !buffsApplied) {
            buffsApplied = true;
            
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("§6§l██████████████████████████████");
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("§e§l          ¡MEJORAS ACTIVADAS!");
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("§a» §fVida aumentada: §c+" + (int)settings.getExtraHearts() + " corazones");
            Bukkit.broadcastMessage("§a» §fResistencia II permanente");
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage("§6§l██████████████████████████████");
            Bukkit.broadcastMessage("");
            
            Bukkit.getOnlinePlayers().forEach(p -> {
                double newMaxHealth = p.getMaxHealth() + (settings.getExtraHearts() * 2.0);
                p.setMaxHealth(newMaxHealth);
                p.setHealth(newMaxHealth);
                
                p.addPotionEffect(new PotionEffect(
                    PotionEffectType.RESISTANCE, 
                    Integer.MAX_VALUE, 
                    1,
                    false, 
                    true, 
                    true
                ));
                
                p.sendTitle(
                    "§6§lMEJORAS ACTIVADAS",
                    "§e+" + (int)settings.getExtraHearts() + " corazones §7| §bResistencia II",
                    10, 70, 20
                );
                
                p.playSound(p.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
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
            double totalBorderChange = (maxWorldSize - minWorldSize) * 2.0;
            double borderStep = totalBorderChange / totalGameSeconds;

            for (World world : Bukkit.getWorlds()) {
                WorldBorder border = world.getWorldBorder();
                border.setSize(border.getSize() - borderStep);
            }
        } else {
            if (currentElapsedSeconds >= totalGameSeconds) {
                for (World world : Bukkit.getWorlds()) {
                    WorldBorder border = world.getWorldBorder();
                    border.setSize(minWorldSize * 2.0);
                }
            }
        }
    }

    public boolean isAgreementActive() {
        return agreementActive;
    }

    public int getAgreementRemainingSeconds() {
        int totalAgreementSeconds = settings.getAgreementHours() * 3600 + 
                                   settings.getAgreementMinutes() * 60 + 
                                   settings.getAgreementSeconds();
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
        
        long totalPlayers = playerManager.getPlayers().size();
        if (totalPlayers < 2) {
            return;
        }
        
        long alivePlayers = playerManager.getPlayers().stream()
            .filter(vch.uhc.models.Player::isAlive)
            .count();

        boolean hasActualTeams = false;
        if (!teamManager.getTeams().isEmpty()) {
            if (settings.getTeamMode() == Settings.TeamMode.MANUAL) {
                hasActualTeams = true;
            } else if (teamsFormed) {
                hasActualTeams = teamManager.getTeams().stream()
                    .anyMatch(team -> team.getMembers().size() > 1);
            }
        }

        if (hasActualTeams) {
            List<Team> aliveTeams = teamManager.getTeams().stream()
                .filter(team -> team.getMembers().stream().anyMatch(vch.uhc.models.Player::isAlive))
                .collect(java.util.stream.Collectors.toList());

            if (aliveTeams.size() == 1) {
                declareTeamWinner(aliveTeams.get(0));
                return;
            } else if (aliveTeams.isEmpty()) {
                declareDraw();
                return;
            }
            
        } else {
            if (alivePlayers == 1) {
                vch.uhc.models.Player winner = playerManager.getPlayers().stream()
                    .filter(vch.uhc.models.Player::isAlive)
                    .findFirst()
                    .orElse(null);
                
                if (winner != null) {
                    declarePlayerWinner(winner);
                }
                return;
            } else if (alivePlayers == 0) {
                declareDraw();
                return;
            }
        }
    }

    private void declarePlayerWinner(vch.uhc.models.Player winner) {
        
        settings.setGameStatus(Settings.GameStatus.ENDED);
        
        if (gameTimeTask != null) {
            gameTimeTask.cancel();
        }

        plugin.getSkinManager().restoreAllSkins();

        gameStats.setGameEndTime(System.currentTimeMillis());
        gameStats.setWinner(winner.getName());


        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(vch.uhc.misc.Messages.VICTORY_HEADER());
        Bukkit.broadcastMessage(vch.uhc.misc.Messages.VICTORY_TITLE_LINE());
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(vch.uhc.misc.Messages.VICTORY_WINNER_SOLO(winner.getName()));
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(vch.uhc.misc.Messages.VICTORY_GAME_DURATION(gameStats.getFormattedDuration()));
        Bukkit.broadcastMessage(vch.uhc.misc.Messages.VICTORY_FOOTER());
        Bukkit.broadcastMessage("");


        Bukkit.getOnlinePlayers().forEach(p -> {
            p.sendTitle(
                vch.uhc.misc.Messages.VICTORY_PLAYER_TITLE(winner.getName()),
                vch.uhc.misc.Messages.VICTORY_TITLE_WON(),
                20, 100, 20
            );
        });


        Player bukkitWinner = winner.getBukkitPlayer();
        if (bukkitWinner != null && bukkitWinner.isOnline()) {
            launchFireworks(bukkitWinner.getLocation(), 10);
            bukkitWinner.playSound(bukkitWinner.getLocation(), org.bukkit.Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
        }


        Bukkit.getOnlinePlayers().forEach(p -> 
            p.playSound(p.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)
        );
    }

    private void declareTeamWinner(Team winningTeam) {
        
        settings.setGameStatus(Settings.GameStatus.ENDED);
        
        if (gameTimeTask != null) {
            gameTimeTask.cancel();
        }

        plugin.getSkinManager().restoreAllSkins();

        gameStats.setGameEndTime(System.currentTimeMillis());
        gameStats.setWinner(vch.uhc.misc.Messages.VICTORY_TEAM_PREFIX() + winningTeam.getName());


        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(org.bukkit.ChatColor.GOLD + "════════════════════════════════");
        Bukkit.broadcastMessage(org.bukkit.ChatColor.YELLOW + "       🏆 " + org.bukkit.ChatColor.BOLD + "VICTORY!" + org.bukkit.ChatColor.YELLOW + " 🏆");
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(org.bukkit.ChatColor.GREEN + "Winning team: " + org.bukkit.ChatColor.GOLD + org.bukkit.ChatColor.BOLD + winningTeam.getName());
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(org.bukkit.ChatColor.AQUA + "Team members:");
        
        for (vch.uhc.models.Player member : winningTeam.getMembers()) {
            String status = member.isAlive() ? org.bukkit.ChatColor.GREEN + "✓" : org.bukkit.ChatColor.RED + "✗";
            Bukkit.broadcastMessage("  " + status + org.bukkit.ChatColor.WHITE + " " + member.getName());
        }
        
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(org.bukkit.ChatColor.AQUA + "Game duration: " + org.bukkit.ChatColor.WHITE + gameStats.getFormattedDuration());
        Bukkit.broadcastMessage(org.bukkit.ChatColor.GOLD + "════════════════════════════════");
        Bukkit.broadcastMessage("");


        Bukkit.getOnlinePlayers().forEach(p -> {
            p.sendTitle(
                vch.uhc.misc.Messages.VICTORY_TEAM_TITLE(winningTeam.getName()),
                vch.uhc.misc.Messages.VICTORY_TITLE_WON(),
                20, 100, 20
            );
        });


        for (vch.uhc.models.Player member : winningTeam.getMembers()) {
            Player bukkitPlayer = member.getBukkitPlayer();
            if (bukkitPlayer != null && bukkitPlayer.isOnline()) {
                launchFireworks(bukkitPlayer.getLocation(), 5);
                bukkitPlayer.playSound(bukkitPlayer.getLocation(), org.bukkit.Sound.UI_TOAST_CHALLENGE_COMPLETE, 1.0f, 1.0f);
            }
        }


        Bukkit.getOnlinePlayers().forEach(p -> 
            p.playSound(p.getLocation(), org.bukkit.Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f)
        );
    }

    private void declareDraw() {
        
        settings.setGameStatus(Settings.GameStatus.ENDED);
        

        plugin.getSkinManager().restoreAllSkins();
        
        if (gameTimeTask != null) {
            gameTimeTask.cancel();
        }

        gameStats.setGameEndTime(System.currentTimeMillis());
        gameStats.setWinner("Empate");

        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(org.bukkit.ChatColor.GRAY + "════════════════════════════════");
        Bukkit.broadcastMessage(org.bukkit.ChatColor.YELLOW + "         ⚔ " + org.bukkit.ChatColor.BOLD + "EMPATE" + org.bukkit.ChatColor.YELLOW + " ⚔");
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(org.bukkit.ChatColor.RED + "¡Todos los jugadores han sido eliminados!");
        Bukkit.broadcastMessage(org.bukkit.ChatColor.AQUA + "Duración del juego: " + org.bukkit.ChatColor.WHITE + gameStats.getFormattedDuration());
        Bukkit.broadcastMessage(org.bukkit.ChatColor.GRAY + "════════════════════════════════");
        Bukkit.broadcastMessage("");

        Bukkit.getOnlinePlayers().forEach(p -> {
            p.sendTitle(
                org.bukkit.ChatColor.GRAY + "⚔ EMPATE ⚔",
                org.bukkit.ChatColor.RED + "No hay ganador",
                20, 100, 20
            );
        });
    }

    private void launchFireworks(Location location, int count) {
        for (int i = 0; i < count; i++) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                org.bukkit.entity.Firework firework = (org.bukkit.entity.Firework) location.getWorld().spawnEntity(location, org.bukkit.entity.EntityType.FIREWORK_ROCKET);
                org.bukkit.inventory.meta.FireworkMeta meta = firework.getFireworkMeta();
                
                org.bukkit.FireworkEffect effect = org.bukkit.FireworkEffect.builder()
                    .with(org.bukkit.FireworkEffect.Type.BALL_LARGE)
                    .withColor(org.bukkit.Color.YELLOW, org.bukkit.Color.ORANGE, org.bukkit.Color.RED)
                    .withFade(org.bukkit.Color.PURPLE)
                    .flicker(true)
                    .trail(true)
                    .build();
                
                meta.addEffect(effect);
                meta.setPower(1);
                firework.setFireworkMeta(meta);
            }, i * 10L);
        }
    }

}