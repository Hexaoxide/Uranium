package net.draycia.chatgames.util;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.List;
import java.util.Map;

public class GameConfigSerializer implements TypeSerializer<GameConfig> {

    @Nullable
    @Override
    public GameConfig deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) {

        String fileName = value.getNode("supplementary-file").getString();
        Map<Integer, List<String>> commandRewards = (Map<Integer, List<String>>) value.getNode("command-rewards").getValue();

        ConfigurationNode dashNode = value.getNode("dash-percentage");
        if (!dashNode.isVirtual()) {
            return new HangmanConfig(fileName, commandRewards, dashNode.getDouble());
        }

        return new GameConfig(fileName, commandRewards);
    }

    @Override
    public void serialize(@NonNull TypeToken<?> type, @Nullable GameConfig obj, @NonNull ConfigurationNode value) {
        if (obj == null) {
            return;
        }

        value.getNode("supplementary-file").setValue(obj.getSupplementaryFile());
        value.getNode("command-rewards").setValue(obj.getAllCommandRewards());

        if (obj instanceof HangmanConfig) {
            value.getNode("dash-percentage").setValue(((HangmanConfig) obj).getDashPercentage());
        }
    }

}
