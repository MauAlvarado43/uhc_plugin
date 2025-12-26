package vch.uhc.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameRule;
import org.bukkit.Location;
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

    private boolean isCheckingProximity = true;
    private boolean teamsFormed = false;
    private GameStats gameStats;
    

    private org.bukkit.scoreboard.Scoreboard mainScoreboard;
    private org.bukkit.scoreboard.Objective mainObjective;
    private org.bukkit.scoreboard.Objective healthObjective;

    public UHCManager() {
        plugin = UHC.getPlugin();
        settings = plugin.getSettings();
        playerManager = plugin.getPlayerManager();
        teamManager = plugin.getTeamManager();
    }

    public void start() {

        settings.setGameStatus(Settings.GameStatus.IN_PROGRESS);
        currentWorldSize = settings.getMaxWorldSize();
        teamsFormed = false;


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
        
        plugin.getSkinManager().shuffleAndAssignSkins();
        
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


        org.bukkit.scoreboard.ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) return;

        mainScoreboard = manager.getNewScoreboard();
        mainObjective = mainScoreboard.registerNewObjective(
            "uhc", 
            "dummy", 
            org.bukkit.ChatColor.GOLD + "" + org.bukkit.ChatColor.BOLD + "UHC"
        );
        mainObjective.setDisplaySlot(org.bukkit.scoreboard.DisplaySlot.SIDEBAR);
        









        Bukkit.getOnlinePlayers().forEach(p -> p.setScoreboard(mainScoreboard));


    }

    public void updateScoreboard() {
        

        if (mainScoreboard == null || mainObjective == null || healthObjective == null) return;
        

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getScoreboard() != mainScoreboard) {
                player.setScoreboard(mainScoreboard);
            }
        }
        

        mainScoreboard.getEntries().forEach(mainScoreboard::resetScores);
        

        Player referencePlayer = Bukkit.getOnlinePlayers().stream().findFirst().orElse(null);
        if (referencePlayer == null) return;

        int score = 15;


        mainObjective.getScore(" ").setScore(score--);


        String timeStr = String.format("%02d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds);
        mainObjective.getScore(org.bukkit.ChatColor.YELLOW + "Tiempo: " + org.bukkit.ChatColor.WHITE + timeStr).setScore(score--);


        String pvpStatus = agreementActive ? 
                         org.bukkit.ChatColor.RED + "✖ Desactivado" : 
                         org.bukkit.ChatColor.GREEN + "✔ Activado";
        mainObjective.getScore(org.bukkit.ChatColor.YELLOW + "PvP: " + pvpStatus).setScore(score--);


        mainObjective.getScore("  ").setScore(score--);


        long alivePlayers = playerManager.getPlayers().stream().filter(vch.uhc.models.Player::isAlive).count();
        long totalPlayers = playerManager.getPlayers().size();
        mainObjective.getScore(org.bukkit.ChatColor.AQUA + "Vivos: " + org.bukkit.ChatColor.WHITE + alivePlayers + "/" + totalPlayers).setScore(score--);


        if (settings.getTeamMode() != Settings.TeamMode.MANUAL || !teamManager.getTeams().isEmpty()) {
            long aliveTeams = teamManager.getTeams().stream()
                .filter(team -> team.getMembers().stream().anyMatch(vch.uhc.models.Player::isAlive))
                .count();
            mainObjective.getScore(org.bukkit.ChatColor.GREEN + "Equipos: " + org.bukkit.ChatColor.WHITE + aliveTeams).setScore(score--);
        }


        mainObjective.getScore("   ").setScore(score--);


        World world = referencePlayer.getWorld();
        int borderSize = (int) (world.getWorldBorder().getSize() / 2);
        mainObjective.getScore(org.bukkit.ChatColor.LIGHT_PURPLE + "Border: " + org.bukkit.ChatColor.WHITE + "±" + borderSize).setScore(score--);
        

        mainObjective.getScore("    ").setScore(score--);
    }
    
    public void applyScoreboardToPlayer(Player player) {
        if (mainScoreboard != null) {
            player.setScoreboard(mainScoreboard);
        }
    }

    private List<Location> getSpawns() {

        String worldName = "world";
        int playerCount = playerManager.getPlayers().size();
        int size = settings.getMaxWorldSize();
        

        int spawnCount;
        if (settings.getTeamMode() == Settings.TeamMode.MANUAL || settings.getTeamMode() == Settings.TeamMode.AUTO) {
            spawnCount = teamManager.getTeams().size();
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

    private void prepareTeams() {

        if (settings.getTeamMode() != Settings.TeamMode.AUTO) {
            return;
        }

        ArrayList<vch.uhc.models.Player> uhcPlayers = new ArrayList<>(playerManager.getPlayers());
        int playerCount = uhcPlayers.size();
        int teamCount = (int) Math.ceil(playerCount / (double) settings.getTeamSize());
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
                }

            }

        }

    }

    private void startTimedTasks() {
        gameTimeTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            checkInGameTeamsState();
            checkGameState();
            checkAgreementState();
            checkWorldBorderState();
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
                        new TextComponent(org.bukkit.ChatColor.YELLOW + "Jugador cerca: " + 
                        org.bukkit.ChatColor.WHITE + p2.getName() + 
                        org.bukkit.ChatColor.GRAY + " (" + distanceBlocks + " bloques)"));
                    bukkitP2.spigot().sendMessage(ChatMessageType.ACTION_BAR, 
                        new TextComponent(org.bukkit.ChatColor.YELLOW + "Jugador cerca: " + 
                        org.bukkit.ChatColor.WHITE + p1.getName() + 
                        org.bukkit.ChatColor.GRAY + " (" + distanceBlocks + " bloques)"));
                }
                

                if (distance > 5) {
                    continue;
                }
                

                Team team1 = p1.getTeam();
                Team team2 = p2.getTeam();
                
                if (team1 == null && team2 == null) {

                    Team newTeam = teamManager.createTeam(p1, "Team " + (teamManager.getTeams().size() + 1));
                    teamManager.addPlayer(newTeam, p2);
                    bukkitP1.sendMessage(ChatColor.GREEN + "Team formed with " + p2.getName() + "!");
                    bukkitP2.sendMessage(ChatColor.GREEN + "Team formed with " + p1.getName() + "!");
                } else if (team1 == null && team2 != null) {

                    if (team2.getMembers().size() < settings.getTeamSize()) {
                        teamManager.addPlayer(team2, p1);
                        bukkitP1.sendMessage(ChatColor.GREEN + "You joined " + team2.getName() + "!");
                        bukkitP2.sendMessage(ChatColor.GREEN + p1.getName() + " joined your team!");
                    }
                } else if (team1 != null && team2 == null) {

                    if (team1.getMembers().size() < settings.getTeamSize()) {
                        teamManager.addPlayer(team1, p2);
                        bukkitP2.sendMessage(ChatColor.GREEN + "You joined " + team1.getName() + "!");
                        bukkitP1.sendMessage(ChatColor.GREEN + p2.getName() + " joined your team!");
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
                                bukkitMember.sendMessage(ChatColor.GREEN + "Teams merged! Now part of " + team1.getName());
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
                Bukkit.broadcastMessage(org.bukkit.ChatColor.RED + "⚠ PvP se activará en " + remainingSeconds + " segundos!");
                Bukkit.getOnlinePlayers().forEach(p -> 
                    p.playSound(p.getLocation(), org.bukkit.Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.5f)
                );
            }

        } else if (remainingSeconds == 0) {

            agreementActive = false;
            
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(org.bukkit.ChatColor.DARK_RED + "════════════════════════════════");
            Bukkit.broadcastMessage(org.bukkit.ChatColor.RED + "⚔ " + org.bukkit.ChatColor.BOLD + "PVP ACTIVADO" + org.bukkit.ChatColor.RED + " ⚔");
            Bukkit.broadcastMessage(org.bukkit.ChatColor.DARK_RED + "════════════════════════════════");
            Bukkit.broadcastMessage("");
            

            Bukkit.getOnlinePlayers().forEach(p -> {
                p.sendTitle(
                    org.bukkit.ChatColor.DARK_RED + "⚔ PVP ACTIVADO ⚔",
                    org.bukkit.ChatColor.YELLOW + "¡El acuerdo ha terminado!",
                    10, 70, 20
                );
                p.playSound(p.getLocation(), org.bukkit.Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
            });
            
        } else {

            agreementActive = false;
        }

    }

    private void checkWorldBorderState() {

        int maxWorldSize = settings.getMaxWorldSize();
        int maxHours = settings.getGameHours();
        int maxMinutes = settings.getGameMinutes();
        int maxSeconds = settings.getGameSeconds();

        double totalGameTime = maxHours * 3600 + maxMinutes * 60 + maxSeconds;
        double worldBorderSize = maxWorldSize * 2;
        double borderStep = worldBorderSize / totalGameTime;

        for (World world : Bukkit.getWorlds()) {
            WorldBorder border = world.getWorldBorder();
            border.setSize(border.getSize() - borderStep);
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


        if (teamsFormed && !teamManager.getTeams().isEmpty()) {
            
            List<Team> aliveTeams = teamManager.getTeams().stream()
                .filter(team -> team.getMembers().stream().anyMatch(vch.uhc.models.Player::isAlive))
                .collect(java.util.stream.Collectors.toList());

            if (aliveTeams.size() == 1 && alivePlayers == 1) {

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
        Bukkit.broadcastMessage(org.bukkit.ChatColor.GOLD + "════════════════════════════════");
        Bukkit.broadcastMessage(org.bukkit.ChatColor.YELLOW + "       🏆 " + org.bukkit.ChatColor.BOLD + "VICTORY!" + org.bukkit.ChatColor.YELLOW + " 🏆");
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(org.bukkit.ChatColor.GREEN + "Winner: " + org.bukkit.ChatColor.GOLD + org.bukkit.ChatColor.BOLD + winner.getName());
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(org.bukkit.ChatColor.AQUA + "Game duration: " + org.bukkit.ChatColor.WHITE + gameStats.getFormattedDuration());
        Bukkit.broadcastMessage(org.bukkit.ChatColor.GOLD + "════════════════════════════════");
        Bukkit.broadcastMessage("");


        Bukkit.getOnlinePlayers().forEach(p -> {
            p.sendTitle(
                org.bukkit.ChatColor.GOLD + "🏆 " + winner.getName() + " 🏆",
                org.bukkit.ChatColor.YELLOW + "Has won the UHC!",
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


        gameStats.setGameEndTime(System.currentTimeMillis());
        gameStats.setWinner("Equipo " + winningTeam.getName());


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
                org.bukkit.ChatColor.GOLD + "🏆 " + winningTeam.getName() + " 🏆",
                org.bukkit.ChatColor.YELLOW + "Has won the UHC!",
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