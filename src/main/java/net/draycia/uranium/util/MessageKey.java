package net.draycia.uranium.util;

import java.util.*;
import java.util.stream.Collectors;

public enum MessageKey {

    FAILURE("<green>Games <dark_gray>» <gray>Nobody typed <green><word><gray> in time!"),
    TYPE_START("<hover:show_text:'<white><word>'><green>Games <dark_gray>» <green>Hover and type the word!"),
    UNSCRAMBLE_START("<hover:show_text:'<white><word>'><green>Games <dark_gray>» <green>Hover and unscramble the word!"),
    MATH_START("<hover:show_text:'<white><word>'><green>Games <dark_gray>» <green>Hover and solve the math problem!"),
    HANGMAN_START("<hover:show_text:'<white><word>'><green>Games <dark_gray>» <green>Hover and fill in the blanks!"),
    TRIVIA_START("<hover:show_text:'<white><word>'><green>Games <dark_gray>» <green>Hover and answer this trivia question!"),
    TYPE_INCOMPLETE("<green>Games <dark_gray>» <gray>Game ended. Winners: <winners>"),
    UNSCRAMBLE_INCOMPLETE("<green>Games <dark_gray>» <gray>Game ended. Winners: <winners>"),
    MATH_INCOMPLETE("<green>Games <dark_gray>» <gray>Game ended. Winners: <winners>"),
    HANGMAN_INCOMPLETE("<green>Games <dark_gray>» <gray>Game ended. Winners: <winners>"),
    WINNERS_FORMAT("<green><name><gray>[<green><time>s<gray>]"),
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
