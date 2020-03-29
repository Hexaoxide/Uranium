package net.draycia.chatgames.util;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;
import java.util.Map;

@ConfigSerializable
public class HangmanConfig extends GameConfig {

    @Setting("dash-percentage")
    private double dashPercentage = 0.5;

    public HangmanConfig() {
    }

    public HangmanConfig(double dashPercentage) {
        this.dashPercentage = dashPercentage;
    }

    public HangmanConfig(String supplementaryFile, Map<Integer, List<String>> commandRewards, double dashPercentage) {
        super(supplementaryFile, commandRewards);
        this.dashPercentage = dashPercentage;
    }

    public HangmanConfig(String supplementaryFile, double dashPercentage) {
        super(supplementaryFile);
        this.dashPercentage = dashPercentage;
    }

    public double getDashPercentage() {
        return dashPercentage;
    }

}
