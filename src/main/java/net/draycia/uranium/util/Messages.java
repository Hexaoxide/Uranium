package net.draycia.uranium.util;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public class Messages {

    private String failure = "<green>Games <dark_gray>» <gray>Nobody typed <green><word><gray> in time!";
    private String typeStart = "<hover:show_text:'<white><word>'><green>Games <dark_gray>» <green>Hover and type the word!";
    private String unscrambleStart = "<hover:show_text:'<white><word>'><green>Games <dark_gray>» <green>Hover and unscramble the word!";
    private String mathStart = "<hover:show_text:'<white><word>'><green>Games <dark_gray>» <green>Hover and solve the math problem!";
    private String hangmanStart = "<hover:show_text:'<white><word>'><green>Games <dark_gray>» <green>Hover and fill in the blanks!";
    private String triviaStart = "<hover:show_text:'<white><word>'><green>Games <dark_gray>» <green>Hover and answer this trivia question!";
    private String typeComplete = "<green>Games <dark_gray>» <gray>Game ended. Winners: <winners>";
    private String unscrambleComplete = "<green>Games <dark_gray>» <gray>Game ended. Winners: <winners>";
    private String mathComplete = "<green>Games <dark_gray>» <gray>Game ended. Winners: <winners>";
    private String hangmanComplete = "<green>Games <dark_gray>» <gray>Game ended. Winners: <winners>";
    private String triviaComplete = "<green>Games <dark_gray>» <gray>Game ended. Winners: <winners>";
    private String winners = "<green><name><gray>[<green><time>s<gray>]";
    private String winnersSeparator = ", ";

    public String failure() {
        return this.failure;
    }

    public String typeStart() {
        return this.typeStart;
    }

    public String unscrambleStart() {
        return this.unscrambleStart;
    }

    public String mathStart() {
        return this.mathStart;
    }

    public String hangmanStart() {
        return this.hangmanStart;
    }

    public String triviaStart() {
        return this.triviaStart;
    }

    public String typeComplete() {
        return this.typeComplete;
    }

    public String unscrambleComplete() {
        return this.unscrambleComplete;
    }

    public String mathComplete() {
        return this.mathComplete;
    }

    public String hangmanComplete() {
        return this.hangmanComplete;
    }

    public String triviaComplete() {
        return this.triviaComplete;
    }

    public String winners() {
        return this.winners;
    }

    public String winnersSeparator() {
        return this.winnersSeparator;
    }

}
