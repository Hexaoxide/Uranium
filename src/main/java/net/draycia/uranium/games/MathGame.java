package net.draycia.uranium.games;

import net.draycia.uranium.Uranium;
import net.draycia.uranium.util.GameConfig;
import net.draycia.uranium.util.MessageKey;

import java.util.List;

public class MathGame extends ChatGame {

    private final String problem;
    private final String solution;

    MathGame(Uranium main) {
        super(main);

        String[] in = getRandomLine("3+3*2").split("\\s*:\\s*");

        if (in.length < 2) {
            throw new RuntimeException("Incorrect math problem format! It should be formatted in this format: 'problem: solution'");
        }

        problem = in[0];
        solution = in[1];

        super.onStart(getConfig().getMessage(MessageKey.MATH_START).get(0));
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
