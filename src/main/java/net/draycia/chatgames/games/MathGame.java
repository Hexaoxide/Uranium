package net.draycia.chatgames.games;

import net.draycia.chatgames.ChatGames;
import net.draycia.chatgames.util.GameConfig;
import net.draycia.chatgames.util.MessageKey;

import java.util.List;

public class MathGame extends ChatGame {

    private final String problem;
    private final String solution;

    MathGame(ChatGames main) {
        super(main);

        String[] in = getRandomLine("3+3*2").split("\\s*:\\s*");
        if (in.length < 2) {
            throw new RuntimeException("Incorrect math problem format! It should be formatted in this format: 'problem: solution'");
        }

        problem = in[0];
        solution = in[1];

        String message = getConfig().getMessage(MessageKey.MATH_START).get(0);
        if (message == null) {
            message = "&eGames &8» &aHover and solve the math problem shown!";
        }

        super.onStart(message);
    }

    @Override
    public String getAnswer() {
        return solution;
    }

    @Override
    public String getDisplayText() {
        return problem;
    }

    @Override
    long getReward() {
        //unused
        return 0;
    }

    @Override
    GameConfig getGameConfig() {
        return getConfig().getGameConfig(GameType.MATH);
    }

    @Override
    List<String> getRewardCommands(int place) {
        return getGameConfig().getCommandRewards(place);
    }

    @Override
    GameType getGameType() {
        return GameType.MATH;
    }

}
