package net.draycia.chatgames.util;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GameConfig {

    private String supplementaryFile = "words.txt";

    private Map<Integer, List<String>> commandRewards = new LinkedHashMap<>();
    {
        commandRewards.put(1, Arrays.asList("eco give %player% 1500",
                "broadcast &6%player% won the game in %time% finishing %place%"));
        commandRewards.put(2, Arrays.asList("eco give %player% 1000",
                "broadcast &a%player% won the game in %time% finishing %place%"));
        commandRewards.put(3, Arrays.asList("eco give %player% 500",
                "broadcast &7%player% won the game in %time% finishing %place%"));
    }

    public GameConfig() {
    }

    public GameConfig(String supplementaryFile, Map<Integer, List<String>> commandRewards) {
        this.supplementaryFile = supplementaryFile;
        this.commandRewards = commandRewards;
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
}
