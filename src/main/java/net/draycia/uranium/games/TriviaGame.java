package net.draycia.uranium.games;

import net.draycia.uranium.Uranium;
import net.draycia.uranium.util.GameConfig;

import java.util.List;

public class TriviaGame extends ChatGame {

    private final String problem;
    private final String solution;

    TriviaGame(Uranium main) {
        super(main);

        String[] in = getRandomLine("Yes?:Yes.").split("\\s*:\\s*");

        if (in.length < 2) {
            throw new RuntimeException("Incorrect trivia format! It should be formatted in this format: 'trivia: answer'");
        }

        problem = in[0];
        solution = in[1];

        super.broadcastFormat(getConfig().getMessages().triviaStart());
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
    public GameConfig getGameConfig() {
        return this.getConfig().getGameConfig(this.getGameType());
    }

    @Override
    public List<String> getRewardCommands(int place) {
        return this.getGameConfig().getCommandRewards(place);
    }

    @Override
    public String getCompleteMessage() {
        return getConfig().getMessages().triviaComplete();
    }

    @Override
    public GameType getGameType() {
        return GameType.TRIVIA;
    }

}
