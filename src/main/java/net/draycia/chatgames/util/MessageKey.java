package net.draycia.chatgames.util;

import java.util.*;
import java.util.stream.Collectors;

public enum MessageKey {

    FAILURE("<yellow>Games <dark_gray>» <gray>Nobody typed the word (<word>) in time!"),
    TYPE_START("<yellow>Games <dark_gray>» <green>Hover and type the word shown!"),
    UNSCRAMBLE_START("<yellow>Games <dark_gray>» <green>Hover and unscramble the word shown!"),
    MATH_START("<yellow>Games <dark_gray>» <green>Hover and solve the math problem shown!"),
    HANGMAN_START("<yellow>Games <dark_gray>» <green>Hover and fill in the blanks in the word shown!"),
    TYPE_INCOMPLETE("<yellow>Games <dark_gray>» <green>Game ended. Winners: <winners>"),
    UNSCRAMBLE_INCOMPLETE("<yellow>Games <dark_gray>» <green>Game ended. Winners: <winners>"),
    MATH_INCOMPLETE("<yellow>Games <dark_gray>» <green>Game ended. Winners: <winners>"),
    HANGMAN_INCOMPLETE("<yellow>Games <dark_gray>» <green>Game ended. Winners: <winners>"),
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
