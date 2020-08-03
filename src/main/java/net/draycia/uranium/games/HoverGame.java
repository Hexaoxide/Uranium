package net.draycia.uranium.games;

import net.draycia.uranium.Uranium;
import net.draycia.uranium.util.GameConfig;
import net.draycia.uranium.util.MessageKey;

import java.util.List;

public class HoverGame extends ChatGame {

    private String word;

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
        return getConfig().getGameConfig(GameType.HOVER);
    }

    @Override
    List<String> getRewardCommands(int place) {
        return getConfig().getGameConfig(GameType.HOVER).getCommandRewards(place);
    }

    @Override
    GameType getGameType() {
        return GameType.HOVER;
    }

}
