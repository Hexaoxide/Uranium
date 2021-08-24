package net.draycia.uranium.util;

import java.util.List;
import java.util.Map;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class HangmanConfig extends GameConfig {

    @Setting("dash-percentage")
    private double dashPercentage = 0.5;

    public HangmanConfig() {
    }

    public HangmanConfig(double dashPercentage) {
        this.dashPercentage = dashPercentage;
    }

    public HangmanConfig(String supplementaryFile, boolean isEnabled, Map<Integer, List<String>> commandRewards, double dashPercentage) {
        super(supplementaryFile, isEnabled, commandRewards);
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
