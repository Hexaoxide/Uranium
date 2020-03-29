package net.draycia.chatgames.games;

import net.draycia.chatgames.ChatGames;
import net.draycia.chatgames.util.GameConfig;
import net.draycia.chatgames.util.MessageKey;

import java.util.List;

public class HoverGame extends ChatGame {

    private String word;
    private long reward;

    public HoverGame(ChatGames main) {
        super(main);
        this.word = this.getRandomWord();

        String message = getConfig().getMessage(MessageKey.TYPE_START).get(0);
        if (message == null) {
            message = "&eGames &8» &aHover and type the word shown!";
        }

        super.onStart(message);
    }

    public String getAnswer() {
        return this.word;
    }

    public String getDisplayText() {
        return this.getAnswer();
    }

    long getReward() {
        return this.reward;
    }

    @Override
    GameConfig getGameConfig() {
        return getConfig().getGameConfig(GameType.HOVER);
    }

    @Override
    List<String> getRewardCommands(int place) {
        return getConfig().getGameConfig(GameType.HOVER).getCommandRewards(place);
    }

}
