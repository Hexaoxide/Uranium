package net.draycia.uranium.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GameConfig {

    private String supplementaryFile = "words.txt";

    private Map<Integer, List<String>> commandRewards = Map.of(
            1, Arrays.asList("eco give %player% 1500",
                    "broadcast &a%player%&7 answered &a%place%&7 in &a%time%&7 seconds!"),
            2, Arrays.asList("eco give %player% 1000",
                    "broadcast &a%player%&7 answered &a%place%&7 in &a%time%&7 seconds!"),
            3, Arrays.asList("eco give %player% 500",
                    "broadcast &a%player%&7 answered &a%place%&7 in &a%time%&7 seconds!")
    );

    private boolean isEnabled = true;

    public GameConfig() {
    }

    public GameConfig(String supplementaryFile, boolean isEnabled, Map<Integer, List<String>> commandRewards) {
        this.supplementaryFile = supplementaryFile;
        this.commandRewards = commandRewards;
        this.isEnabled = isEnabled;
    }

    public GameConfig(String supplementaryFile) {
        this.supplementaryFile = supplementaryFile;
    }

    public String getSupplementaryFile() {
        return supplementaryFile;
    }

    public List<String> getCommandRewards(int place) {
        return commandRewards.get(place);
    }

    public Map<Integer, List<String>> getAllCommandRewards() {
        return commandRewards;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

}
