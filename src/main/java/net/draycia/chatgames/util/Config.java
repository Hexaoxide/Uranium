package net.draycia.chatgames.util;

import net.draycia.chatgames.games.GameType;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ConfigSerializable
public class Config {

    @Setting(value = "time-between-games", comment = "Time between chat games in seconds")
    private int timeBetweenGames = 300;

    @Setting(value = "auto-end-time", comment = "Time in seconds before the games will automatically end")
    private int autoEndTime = 60;

    @Setting(value = "cancel-winning-messages", comment = "Should messages that won the game be cancelled?")
    private boolean cancelWinningMessages = true;

    @Setting(value = "game-config")
    private Map<GameType, GameConfig> gameConfig = Arrays.stream(GameType.values())
            .collect(Collectors.toMap(t -> t, t -> new GameConfig(), (e1, e2) -> e1, LinkedHashMap::new));

    @Setting
    private Map<MessageKey, MessageList> language = MessageKey.getDefaultMessages();

    public int getTimeBetweenGames() {
        return timeBetweenGames;
    }

    public int getAutoEndTime() {
        return autoEndTime;
    }

    public boolean isCancelWinningMessages() {
        return cancelWinningMessages;
    }

    public MessageList getMessage(MessageKey key) {
        return language.get(key); //Maybe getOrDefault(..., key.getDefaultMessage()) would be better? Not sure
    }

    public GameConfig getGameConfig(GameType type) {
        return gameConfig.get(type);
    }

}
