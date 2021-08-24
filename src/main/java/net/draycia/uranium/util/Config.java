package net.draycia.uranium.util;

import net.draycia.uranium.games.GameType;

import java.util.Map;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class Config {

    @Setting("time-between-games")
    @Comment("Time between chat games in seconds")
    private int timeBetweenGames = 300;

    @Setting("auto-end-time")
    @Comment("Time in seconds before the games will automatically end")
    private int autoEndTime = 30;

    @Setting("cancel-winning-messages")
    @Comment("Should messages that won the game be cancelled?")
    private boolean cancelWinningMessages = true;

    @Setting("database")
    private DatabaseCredentials databaseCredentials = new DatabaseCredentials();

    @Setting("game-config")
    private Map<GameType, GameConfig> gameConfig = Map.of(
            GameType.MATH, new GameConfig("problems.txt"),
            GameType.HOVER, new GameConfig(),
            GameType.UNSCRAMBLE, new GameConfig(),
            GameType.HANGMAN, new HangmanConfig(),
            GameType.TRIVIA, new GameConfig("trivia.txt")
    );

    @Setting
    private Messages language = new Messages();

    @Setting("require-player-count")
    @Comment("If games should be skipped if the minimum-players isn't met or exceeded")
    private boolean skipIfNotEnoughPlayers = false;

    @Setting("minimum-players")
    @Comment("The minimum amount of players required for games to start")
    private int minimumPlayers = 1;

    public int getTimeBetweenGames() {
        return timeBetweenGames;
    }

    public int getAutoEndTime() {
        return autoEndTime;
    }

    public boolean shouldCancelWinningMessages() {
        return cancelWinningMessages;
    }

    public boolean skipIfNotEnoughPlayers() {
        return skipIfNotEnoughPlayers;
    }

    public int getMinimumPlayers() {
        return minimumPlayers;
    }

    public Messages getMessages() {
        return language;
    }

    public DatabaseCredentials getDatabaseCredentials() {
        return databaseCredentials;
    }

    public GameConfig getGameConfig(GameType type) {
        return gameConfig.get(type);
    }

}
