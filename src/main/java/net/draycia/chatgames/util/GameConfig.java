package net.draycia.chatgames.util;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@ConfigSerializable
public class GameConfig {

    @Setting(value = "command-rewards", comment = "Commands that will be execute when player wins the game")
    private Map<Integer, List<String>> commandRewards = new LinkedHashMap<>();
    {
        commandRewards.put(1, Arrays.asList("eco give %player% 1500",
                "broadcast &6%player% won the game in %time% finishing %place%"));
        commandRewards.put(2, Arrays.asList("eco give %player% 1000",
                "broadcast &a%player% won the game in %time% finishing %place%"));
        commandRewards.put(3, Arrays.asList("eco give %player% 500",
                "broadcast &7%player% won the game in %time% finishing %place%"));
    }

    public List<String> getCommandRewards(int place) {
        return commandRewards.get(place - 1);
    }

}
