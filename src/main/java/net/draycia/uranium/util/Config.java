package net.draycia.uranium.util;

import net.draycia.uranium.games.GameType;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;
import java.util.Map;

@ConfigSerializable
public class Config {

    @Setting(value = "time-between-games", comment = "Time between chat games in seconds")
    private int timeBetweenGames = 300;

    @Setting(value = "auto-end-time", comment = "Time in seconds before the games will automatically end")
    private int autoEndTime = 30;

    @Setting(value = "cancel-winning-messages", comment = "Should messages that won the game be cancelled?")
    private boolean cancelWinningMessages = true;

    @Setting(value = "database")
    private DatabaseCredentials databaseCredentials = new DatabaseCredentials();

    @Setting(value = "game-config")
    private Map<GameType, GameConfig> gameConfig = Map.of(
            GameType.MATH, new GameConfig("problems.txt"),
            GameType.HOVER, new GameConfig(),
            GameType.UNSCRAMBLE, new GameConfig(),
            GameType.HANGMAN, new HangmanConfig(),
            GameType.TRIVIA, new GameConfig("trivia.txt")
    );

    @Setting
    private Map<MessageKey, List<String>> language = MessageKey.getDefaultMessages();

    @Setting(value = "require-player-count", comment = "If games should be skipped if the minimum-players isn't met or exceeded")
    private boolean skipIfNotEnoughPlayers = false;

    @Setting(value = "minimum-players", comment = "The minimum amount of players required for games to start")
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

    public List<String> getMessage(MessageKey key) {
        return language.get(key); //Maybe getOrDefault(..., key.getDefaultMessage()) would be better? Not sure
    }

    public DatabaseCredentials getDatabaseCredentials() {
        return databaseCredentials;
    }

    public GameConfig getGameConfig(GameType type) {
        return gameConfig.get(type);
    }

}
