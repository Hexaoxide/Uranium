package net.draycia.uranium.games;

import net.draycia.uranium.Uranium;
import net.draycia.uranium.util.GameConfig;

import java.util.List;

public class HoverGame extends ChatGame {

    private final String word;

    public HoverGame(Uranium main) {
        super(main);
        this.word = this.getRandomWord();

        super.broadcastFormat(getConfig().getMessages().typeStart());
    }

    public String getAnswer() {
        return this.word;
    }

    public String getDisplayText() {
        return this.getAnswer();
    }

    @Override
    public GameConfig getGameConfig() {
        return this.getConfig().getGameConfig(this.getGameType());
    }

    @Override
    public List<String> getRewardCommands(int place) {
        return this.getConfig().getGameConfig(this.getGameType()).getCommandRewards(place);
    }

    @Override
    public String getCompleteMessage() {
        return getConfig().getMessages().typeComplete();
    }

    @Override
    public GameType getGameType() {
        return GameType.HOVER;
    }

}
