package vch.uhc.misc;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

import com.google.gson.Gson;

import vch.uhc.UHC;
import vch.uhc.items.DragonBreath;
import vch.uhc.items.GlisteringMelonSlice;
import vch.uhc.items.HyperGoldenApple;
import vch.uhc.items.PlayerGoldenApple;
import vch.uhc.items.SuperGoldenApple;

public class Settings {

  public enum TeamMode {
    IN_GAME,
    AUTO,
    MANUAL,
  }

  public enum GameStatus {
    NONE,
    IN_PROGRESS,
    PAUSED,
    ENDED
  }

  public enum GameMode {
    PVD,
    PVP,
    RESOURCE_RUSH
  }

  
  private transient final Gson gson = new Gson();
  private transient final File settingsFile = new File(UHC.getPlugin().getDataFolder(), "settings.json");

  private int maxWorldSize = 1000;
  private int minWorldSize = 100;

  private int gameHours = 0;
  private int gameMinutes = 0;
  private int gameSeconds = 0;

  private int agreementHours = 0;
  private int agreementMinutes = 0;
  private int agreementSeconds = 0;

  private int minWorldBorderHours = 0;
  private int minWorldBorderMinutes = 0;
  private int minWorldBorderSeconds = 0;

  private int maxTeamInGameHours = 0;
  private int maxTeamInGameMinutes = 0;
  private int maxTeamInGameSeconds = 0;

  private int endPortalHours = 0;
  private int endPortalMinutes = 10;
  private int endPortalSeconds = 0;

  private boolean shulkerEnabled = false;
  private int shulkerHours = 0;
  private int shulkerMinutes = 15;
  private int shulkerSeconds = 0;

  private boolean locatorBarEnabled = false;
  private int locatorBarHours = 0;
  private int locatorBarMinutes = 20;
  private int locatorBarSeconds = 0;

  private boolean buffsEnabled = false;
  private int buffsHours = 0;
  private int buffsMinutes = 30;
  private int buffsSeconds = 0;
  private double extraHearts = 10.0;

  private boolean skinShuffleEnabled = true;
  private int skinShuffleMinutes = 5;
  private int skinShuffleSeconds = 0;

  private boolean gradualBorderEnabled = true;

  private GameStatus gameStatus = GameStatus.NONE;
  private GameMode gameMode = GameMode.PVP;

  private TeamMode teamMode = TeamMode.MANUAL;
  private int teamSize = 2;

  private int playerLives = 1;

  private transient ArrayList<BaseItem> items = new ArrayList<>(Arrays.asList(
    new DragonBreath(),
    new GlisteringMelonSlice(),
    new SuperGoldenApple(),
    new HyperGoldenApple(),
    new PlayerGoldenApple()
  ));

  public void setMaxWorldSize(int maxWorldSize) {
    this.maxWorldSize = maxWorldSize;
  }

  public int getMaxWorldSize() {
    return maxWorldSize;
  }

  public void setMinWorldSize(int minWorldSize) {
    this.minWorldSize = minWorldSize;
  }

  public int getMinWorldSize() {
    return minWorldSize;
  }

  public void setGameHours(int gameHours) {
    this.gameHours = gameHours;
  }

  public int getGameHours() {
    return gameHours;
  }

  public void setGameMinutes(int gameMinutes) {
    this.gameMinutes = gameMinutes;
  }

  public int getGameMinutes() {
    return gameMinutes;
  }

  public void setGameSeconds(int gameSeconds) {
    this.gameSeconds = gameSeconds;
  }

  public int getGameSeconds() {
    return gameSeconds;
  }

  public void setAgreementHours(int agreementHours) {
    this.agreementHours = agreementHours;
  }

  public int getAgreementHours() {
    return agreementHours;
  }

  public void setAgreementMinutes(int agreementMinutes) {
    this.agreementMinutes = agreementMinutes;
  }

  public int getAgreementMinutes() {
    return agreementMinutes;
  }

  public void setAgreementSeconds(int agreementSeconds) {
    this.agreementSeconds = agreementSeconds;
  }

  public int getAgreementSeconds() {
    return agreementSeconds;
  }

  public void setMinWorldBorderHours(int minWorldBorderHours) {
    this.minWorldBorderHours = minWorldBorderHours;
  }

  public int getMinWorldBorderHours() {
    return minWorldBorderHours;
  }

  public void setMinWorldBorderMinutes(int minWorldBorderMinutes) {
    this.minWorldBorderMinutes = minWorldBorderMinutes;
  }

  public int getMinWorldBorderMinutes() {
    return minWorldBorderMinutes;
  }

  public void setMinWorldBorderSeconds(int minWorldBorderSeconds) {
    this.minWorldBorderSeconds = minWorldBorderSeconds;
  }

  public int getMinWorldBorderSeconds() {
    return minWorldBorderSeconds;
  }

  public void setMaxTeamInGameHours(int maxTeamInGameHours) {
    this.maxTeamInGameHours = maxTeamInGameHours;
  }

  public int getMaxTeamInGameHours() {
    return maxTeamInGameHours;
  }

  public void setMaxTeamInGameMinutes(int maxTeamInGameMinutes) {
    this.maxTeamInGameMinutes = maxTeamInGameMinutes;
  }

  public int getMaxTeamInGameMinutes() {
    return maxTeamInGameMinutes;
  }

  public void setMaxTeamInGameSeconds(int maxTeamInGameSeconds) {
    this.maxTeamInGameSeconds = maxTeamInGameSeconds;
  }

  public int getMaxTeamInGameSeconds() {
    return maxTeamInGameSeconds;
  }

  public void setGameStatus(GameStatus gameStatus) {
    this.gameStatus = gameStatus;
  }

  public GameStatus getGameStatus() {
    return gameStatus;
  }

  public void setGameMode(GameMode gameMode) {
    this.gameMode = gameMode;
  }

  public GameMode getGameMode() {
    return gameMode;
  }

  public void setTeamMode(TeamMode teamMode) {
    this.teamMode = teamMode;
  }

  public TeamMode getTeamMode() {
    return teamMode;
  }

  public void setTeamSize(int teamSize) {
    this.teamSize = teamSize;
  }

  public int getTeamSize() {
    return teamSize;
  }

  public void setPlayerLives(int playerLives) {
    this.playerLives = playerLives;
  }

  public int getPlayerLives() {
    return playerLives;
  }

  public ArrayList<BaseItem> getItems() {
    return items;
  }

  public void setEndPortalHours(int endPortalHours) {
    this.endPortalHours = endPortalHours;
  }

  public int getEndPortalHours() {
    return endPortalHours;
  }

  public void setEndPortalMinutes(int endPortalMinutes) {
    this.endPortalMinutes = endPortalMinutes;
  }

  public int getEndPortalMinutes() {
    return endPortalMinutes;
  }

  public void setEndPortalSeconds(int endPortalSeconds) {
    this.endPortalSeconds = endPortalSeconds;
  }

  public int getEndPortalSeconds() {
    return endPortalSeconds;
  }

  public void setShulkerEnabled(boolean shulkerEnabled) {
    this.shulkerEnabled = shulkerEnabled;
  }

  public boolean isShulkerEnabled() {
    return shulkerEnabled;
  }

  public void setShulkerHours(int shulkerHours) {
    this.shulkerHours = shulkerHours;
  }

  public int getShulkerHours() {
    return shulkerHours;
  }

  public void setShulkerMinutes(int shulkerMinutes) {
    this.shulkerMinutes = shulkerMinutes;
  }

  public int getShulkerMinutes() {
    return shulkerMinutes;
  }

  public void setShulkerSeconds(int shulkerSeconds) {
    this.shulkerSeconds = shulkerSeconds;
  }

  public int getShulkerSeconds() {
    return shulkerSeconds;
  }

  public void setLocatorBarEnabled(boolean locatorBarEnabled) {
    this.locatorBarEnabled = locatorBarEnabled;
  }

  public boolean isLocatorBarEnabled() {
    return locatorBarEnabled;
  }

  public void setLocatorBarHours(int locatorBarHours) {
    this.locatorBarHours = locatorBarHours;
  }

  public int getLocatorBarHours() {
    return locatorBarHours;
  }

  public void setLocatorBarMinutes(int locatorBarMinutes) {
    this.locatorBarMinutes = locatorBarMinutes;
  }

  public int getLocatorBarMinutes() {
    return locatorBarMinutes;
  }

  public void setLocatorBarSeconds(int locatorBarSeconds) {
    this.locatorBarSeconds = locatorBarSeconds;
  }

  public int getLocatorBarSeconds() {
    return locatorBarSeconds;
  }

  public void setSkinShuffleEnabled(boolean skinShuffleEnabled) {
    this.skinShuffleEnabled = skinShuffleEnabled;
  }

  public boolean isSkinShuffleEnabled() {
    return skinShuffleEnabled;
  }

  public void setSkinShuffleMinutes(int skinShuffleMinutes) {
    this.skinShuffleMinutes = skinShuffleMinutes;
  }

  public int getSkinShuffleMinutes() {
    return skinShuffleMinutes;
  }

  public void setSkinShuffleSeconds(int skinShuffleSeconds) {
    this.skinShuffleSeconds = skinShuffleSeconds;
  }

  public int getSkinShuffleSeconds() {
    return skinShuffleSeconds;
  }

  public void setGradualBorderEnabled(boolean gradualBorderEnabled) {
    this.gradualBorderEnabled = gradualBorderEnabled;
  }

  public boolean isGradualBorderEnabled() {
    return gradualBorderEnabled;
  }

  public void setBuffsEnabled(boolean buffsEnabled) {
    this.buffsEnabled = buffsEnabled;
  }

  public boolean isBuffsEnabled() {
    return buffsEnabled;
  }

  public void setBuffsHours(int buffsHours) {
    this.buffsHours = buffsHours;
  }

  public int getBuffsHours() {
    return buffsHours;
  }

  public void setBuffsMinutes(int buffsMinutes) {
    this.buffsMinutes = buffsMinutes;
  }

  public int getBuffsMinutes() {
    return buffsMinutes;
  }

  public void setBuffsSeconds(int buffsSeconds) {
    this.buffsSeconds = buffsSeconds;
  }

  public int getBuffsSeconds() {
    return buffsSeconds;
  }

  public void setExtraHearts(double extraHearts) {
    this.extraHearts = extraHearts;
  }

  public double getExtraHearts() {
    return extraHearts;
  }

  public void load() {
    try {
      if (!settingsFile.exists()) {
        UHC.getPlugin().getLogger().info("Settings file not found, using defaults");
        return;
      }
      
      String json = new String(java.nio.file.Files.readAllBytes(settingsFile.toPath()));
      Settings loadedSettings = gson.fromJson(json, Settings.class);
      
      this.maxWorldSize = loadedSettings.maxWorldSize;
      this.minWorldSize = loadedSettings.minWorldSize;
      this.gameHours = loadedSettings.gameHours;
      this.gameMinutes = loadedSettings.gameMinutes;
      this.gameSeconds = loadedSettings.gameSeconds;
      this.agreementHours = loadedSettings.agreementHours;
      this.agreementMinutes = loadedSettings.agreementMinutes;
      this.agreementSeconds = loadedSettings.agreementSeconds;
      this.minWorldBorderHours = loadedSettings.minWorldBorderHours;
      this.minWorldBorderMinutes = loadedSettings.minWorldBorderMinutes;
      this.minWorldBorderSeconds = loadedSettings.minWorldBorderSeconds;
      this.maxTeamInGameHours = loadedSettings.maxTeamInGameHours;
      this.maxTeamInGameMinutes = loadedSettings.maxTeamInGameMinutes;
      this.maxTeamInGameSeconds = loadedSettings.maxTeamInGameSeconds;
      this.endPortalHours = loadedSettings.endPortalHours;
      this.endPortalMinutes = loadedSettings.endPortalMinutes;
      this.endPortalSeconds = loadedSettings.endPortalSeconds;
      this.shulkerEnabled = loadedSettings.shulkerEnabled;
      this.shulkerHours = loadedSettings.shulkerHours;
      this.shulkerMinutes = loadedSettings.shulkerMinutes;
      this.shulkerSeconds = loadedSettings.shulkerSeconds;
      this.locatorBarEnabled = loadedSettings.locatorBarEnabled;
      this.locatorBarHours = loadedSettings.locatorBarHours;
      this.locatorBarMinutes = loadedSettings.locatorBarMinutes;
      this.locatorBarSeconds = loadedSettings.locatorBarSeconds;
      this.buffsEnabled = loadedSettings.buffsEnabled;
      this.buffsHours = loadedSettings.buffsHours;
      this.buffsMinutes = loadedSettings.buffsMinutes;
      this.buffsSeconds = loadedSettings.buffsSeconds;
      this.extraHearts = loadedSettings.extraHearts;
      this.skinShuffleEnabled = loadedSettings.skinShuffleEnabled;
      this.skinShuffleMinutes = loadedSettings.skinShuffleMinutes;
      this.skinShuffleSeconds = loadedSettings.skinShuffleSeconds;
      this.gradualBorderEnabled = loadedSettings.gradualBorderEnabled;
      this.gameMode = loadedSettings.gameMode;
      this.teamMode = loadedSettings.teamMode;
      this.teamSize = loadedSettings.teamSize;
      this.playerLives = loadedSettings.playerLives;
      
      UHC.getPlugin().getLogger().info("Settings loaded successfully from " + settingsFile.getAbsolutePath());
    } catch (Exception e) {
      UHC.getPlugin().getLogger().log(Level.SEVERE, "Failed to load settings", e);
    }
  }

  public void save() {
    try {
      if(!UHC.getPlugin().getDataFolder().exists()) {
        UHC.getPlugin().getDataFolder().mkdirs();
      }
      
      String json = gson.toJson(this);
      
      try (FileWriter writer = new FileWriter(settingsFile)) {
        writer.write(json);
        writer.flush();
      }
      
      UHC.getPlugin().getLogger().info("Settings saved successfully to " + settingsFile.getAbsolutePath());
    } catch (Exception e) {
      UHC.getPlugin().getLogger().log(Level.SEVERE, "Failed to save settings", e);
    }
  }

}