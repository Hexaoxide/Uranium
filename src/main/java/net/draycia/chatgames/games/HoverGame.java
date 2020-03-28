package net.draycia.chatgames.games;

import net.draycia.chatgames.ChatGames;
import net.draycia.chatgames.util.MessageKey;

import java.util.List;

public class HoverGame extends ChatGame {

    private String word;
    private long startTime;
    private long reward;

    public HoverGame(ChatGames main) {
        super(main);
        this.startTime = System.currentTimeMillis();
        this.word = this.getRandomWord();
        if (main.getConfig().getBoolean("StaticRewards")) {
            this.reward = main.getConfig().getLong("RewardPerWin");
        } else {
            this.reward = main.getConfig().getLong("RewardPerCharacter") * (long) this.getWord().length();
        }

        String message = getConfig().getMessage(MessageKey.TYPE_START).get();
        if (message == null) {
            message = "&eGames &8» &aHover and type the word shown!";
        }

        super.onStart(message);
    }

    public String getWord() {
        return this.word;
    }

    public String getDisplayWord() {
        return this.getWord();
    }

    public long getStartTime() {
        return this.startTime;
    }

    long getReward() {
        return this.reward;
    }

    @Override
    List<String> getRewardCommands(int place) {
        return getConfig().getGameConfig(GameType.HOVER).getCommandRewards(place);
    }

}
