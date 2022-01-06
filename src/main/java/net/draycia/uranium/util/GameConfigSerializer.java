package net.draycia.uranium.util;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Map;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.serialize.SerializationException;
import org.spongepowered.configurate.serialize.TypeSerializer;

public class GameConfigSerializer implements TypeSerializer<GameConfig> {

    private Type commandRewardsType = new TypeToken<Map<Integer, List<String>>>() {}.getType();

    @Override
    public GameConfig deserialize(Type type, ConfigurationNode value) throws SerializationException {
        String fileName = value.node("supplementary-file").getString();
        Map<Integer, List<String>> commandRewards = (Map<Integer, List<String>>) value.node("command-rewards").get(commandRewardsType);
        boolean isEnabled = value.node("is-enabled").getBoolean();

        ConfigurationNode dashNode = value.node("dash-percentage");

        if (!dashNode.virtual()) {
            return new HangmanConfig(fileName, isEnabled, commandRewards, dashNode.getDouble());
        }

        return new GameConfig(fileName, isEnabled, commandRewards);
    }

    @Override
    public void serialize(Type type, @Nullable GameConfig obj, ConfigurationNode value) throws SerializationException {
        if (obj == null) {
            return;
        }

        value.node("supplementary-file").set(obj.getSupplementaryFile());
        value.node("command-rewards").set(obj.getAllCommandRewards());
        value.node("is-enabled").set(obj.isEnabled());

        if (obj instanceof HangmanConfig) {
            value.node("dash-percentage").set(((HangmanConfig) obj).getDashPercentage());
        }
    }
}
