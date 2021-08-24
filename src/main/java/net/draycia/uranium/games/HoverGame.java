package net.draycia.uranium.games;

import net.draycia.uranium.Uranium;
import net.draycia.uranium.util.GameConfig;
import net.draycia.uranium.util.MessageKey;

import java.util.List;

public class HoverGame extends ChatGame {

    private final String word;

    public HoverGame(Uranium main) {
        super(main);
        this.word = this.getRandomWord();

        super.onStart(getConfig().getMessage(MessageKey.TYPE_START).get(0));
    }

    public String getAnswer() {
        return this.word;
    }

    public String getDisplayText() {
        return this.getAnswer();
    }

    @Override
    GameConfig getGameConfig() {
        return this.getConfig().getGameConfig(this.getGameType());
    }

    @Override
    List<String> getRewardCommands(int place) {
        return this.getConfig().getGameConfig(this.getGameType()).getCommandRewards(place);
    }

    @Override
    GameType getGameType() {
        return GameType.HOVER;
    }

}
