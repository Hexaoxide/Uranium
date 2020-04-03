package net.draycia.chatgames.util;

import java.util.*;
import java.util.stream.Collectors;

public enum MessageKey {

    FAILURE("&eGames &8» &7Nobody typed the word (%word%) in time!"),
    TYPE_START("&eGames &8» &aHover and type the word shown!"),
    UNSCRAMBLE_START("&eGames &8» &aHover and unscramble the word shown!"),
    MATH_START("&eGames &8» &aHover and solve the math problem shown!"),
    HANGMAN_START("&eGames &8» &aHover and fill in the blanks in the word shown!"),
    TYPE_INCOMPLETE("&eGames &8» &aGame ended. Winners: %winners%"),
    UNSCRAMBLE_INCOMPLETE("&eGames &8» &aGame ended. Winners: %winners%"),
    MATH_INCOMPLETE("&eGames &8» &aGame ended. Winners: %winners%"),
    HANGMAN_INCOMPLETE("&eGames &8» &aGame ended. Winners: %winners%"),
    WINNERS_FORMAT("&e%name%&7(&a%time%&7)"),
    WINNERS_SEPARATOR(", ");

    private final String defaultMessage;

    MessageKey(String defaultMessage) {
        this.defaultMessage = defaultMessage;
    }

    public List<String> getDefaultMessage() {
        return Collections.singletonList(defaultMessage);
    }

    public static Map<MessageKey, List<String>> getDefaultMessages() {
        return Arrays.stream(values()).collect(Collectors.toMap(m -> m, MessageKey::getDefaultMessage));
    }

}
