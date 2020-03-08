package net.draycia.chatgames.games;

import com.google.common.primitives.Chars;
import net.draycia.chatgames.ChatGames;

import java.util.Collections;
import java.util.List;

public class UnscrambleGame extends ChatGame {
    private String word;
    private String displayWord;
    private long startTime;
    private long reward;
    private ChatGames main;

    public UnscrambleGame(ChatGames main) {
        super(main);
        this.main = main;
        this.startTime = System.currentTimeMillis();
        this.word = super.getRandomWord();
        this.displayWord = this.scrambleWord(this.getWord());
        if (main.getConfig().getBoolean("StaticRewards")) {
            this.reward = main.getConfig().getLong("RewardPerWin");
        } else {
            this.reward = main.getConfig().getLong("RewardPerCharacter") * (long) this.getWord().length();
        }

        String message = main.getConfig().getString("Language.UnscrambleStart");
        if (message == null) {
            message = "&eGames &8» &aHover and unscramble the word shown!";
        }

        super.onStart(message);
    }

    public String getWord() {
        return this.word;
    }

    public String getDisplayWord() {
        return this.displayWord;
    }

    public long getStartTime() {
        return this.startTime;
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
}
