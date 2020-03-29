package net.draycia.chatgames.games;

import com.google.common.primitives.Chars;
import net.draycia.chatgames.ChatGames;
import net.draycia.chatgames.util.GameConfig;
import net.draycia.chatgames.util.MessageKey;

import java.util.Collections;
import java.util.List;

public class UnscrambleGame extends ChatGame {

    private String word;
    private String displayWord;
    private long reward;

    public UnscrambleGame(ChatGames main) {
        super(main);
        this.word = super.getRandomWord();
        this.displayWord = this.scrambleWord(this.getAnswer());

        String message = getConfig().getMessage(MessageKey.UNSCRAMBLE_START).get(0);
        if (message == null) {
            message = "&eGames &8» &aHover and unscramble the word shown!";
        }

        super.onStart(message);
    }

    public String getAnswer() {
        return this.word;
    }

    public String getDisplayText() {
        return this.displayWord;
    }

    private String scrambleWord(String word) {
        String shuffledWord = word;
        int iterations = 0;

        while (shuffledWord.equalsIgnoreCase(word)) {
            List<Character> chars = Chars.asList(word.toCharArray());
            Collections.shuffle(chars);
            shuffledWord = new String(Chars.toArray(chars));
            ++iterations;
            if (iterations > 5) {
                break;
            }
        }

        return shuffledWord;
    }

    public long getReward() {
        return this.reward;
    }

    @Override
    GameConfig getGameConfig() {
        return getConfig().getGameConfig(GameType.UNSCRAMBLE);
    }

    @Override
    List<String> getRewardCommands(int place) {
        return getConfig().getGameConfig(GameType.UNSCRAMBLE).getCommandRewards(place);
    }

}
