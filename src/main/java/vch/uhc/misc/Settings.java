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

  
  private final Gson gson = new Gson();
  private final File settingsFile = new File(UHC.getPlugin().getDataFolder(), "settings.json");

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

  private GameStatus gameStatus = GameStatus.NONE;

  private TeamMode teamMode = TeamMode.MANUAL;
  private int teamSize = 2;

  private int playerLives = 1;

  private ArrayList<BaseItem> items = new ArrayList<>(Arrays.asList(
    new DragonBreath(),
    new GlisteringMelonSlice(),
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

  public void load() {}

  public void save() {
    if(!UHC.getPlugin().getDataFolder().exists()) UHC.getPlugin().getDataFolder().mkdirs();
    try (FileWriter writer = new FileWriter(settingsFile)) {
      writer.write(gson.toJson(this));
    } catch (Exception e) {
      UHC.getPlugin().getLogger().log(Level.SEVERE, "Failed to save settings: {0}", e.getMessage());
    }
  }

}