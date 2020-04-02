package net.draycia.chatgames.util;

import net.draycia.chatgames.games.GameType;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ConfigSerializable
public class Config {

    @Setting(value = "time-between-games", comment = "Time between chat games in seconds")
    private int timeBetweenGames = 300;

    @Setting(value = "auto-end-time", comment = "Time in seconds before the games will automatically end")
    private int autoEndTime = 60;

    @Setting(value = "cancel-winning-messages", comment = "Should messages that won the game be cancelled?")
    private boolean cancelWinningMessages = true;

    @Setting(value = "database")
    private DatabaseCredentials databaseCredentials = new DatabaseCredentials();

    @Setting(value = "game-config")
    private Map<GameType, GameConfig> gameConfig = new LinkedHashMap<>();
    {
        gameConfig.put(GameType.MATH, new GameConfig("problems.txt"));
        gameConfig.put(GameType.HOVER, new GameConfig());
        gameConfig.put(GameType.UNSCRAMBLE, new GameConfig());
        gameConfig.put(GameType.HANGMAN, new HangmanConfig());
    }

    @Setting
    private Map<MessageKey, List<String>> language = MessageKey.getDefaultMessages();

    public int getTimeBetweenGames() {
        return timeBetweenGames;
    }

    public int getAutoEndTime() {
        return autoEndTime;
    }

    public boolean shouldCancelWinningMessages() {
        return cancelWinningMessages;
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
