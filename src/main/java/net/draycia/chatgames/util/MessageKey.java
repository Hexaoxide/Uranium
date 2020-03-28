package net.draycia.chatgames.util;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum MessageKey {

    FAILURE("&eGames &8» &7Nobody typed the word (%word%) in time!"),
    TYPE_START("&eGames &8» &aHover and type the word shown!"),
    UNSCRAMBLE_START("&eGames &8» &aHover and unscramble the word shown!"),
    TYPE_SUCCESS("&eGames &8» &a%name% &6has typed %word% in %seconds% seconds!"),
    UNSCRAMBLE_SUCCESS("&eGames &8» &a%name% &6has unscrambled %word% in %seconds% seconds!");

    private final String defaultMessage;

    MessageKey(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public MessageList getDefaultMessage() {
        return new MessageList(defaultMessage);
    }

    public static Map<MessageKey, MessageList> getDefaultMessages() {
        return Arrays.stream(values()).collect(Collectors.toMap(m -> m, MessageKey::getDefaultMessage));
    }

}
