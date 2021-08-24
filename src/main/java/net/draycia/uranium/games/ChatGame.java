package net.draycia.uranium.games;

import net.draycia.uranium.Uranium;
import net.draycia.uranium.util.Config;
import net.draycia.uranium.util.GameConfig;
import net.draycia.uranium.util.MessageKey;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.Template;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public abstract class ChatGame {

    private final Uranium main;
    private final GameManager gameManager;
    private final Config config;

    //UUID, time in which player solved the challenge
    private final Map<UUID, Double> playersWon;
    private final long startTime;

    private boolean finished;

    ChatGame(final Uranium main) {
        this.main = main;
        this.config = main.getSettings(); //Once again multiple names for config
        this.gameManager = main.getGameManager();
        this.playersWon = new LinkedHashMap<>(); //Insertion order is important for us unless we sort by time
        this.startTime = System.currentTimeMillis();
    }

    public Uranium getPlugin() {
        return main;
    }

    public Config getConfig() {
        return config;
    }

    public abstract String getAnswer();

    public abstract String getDisplayText();

    public long getStartTime() {
        return startTime;
    }

    abstract GameConfig getGameConfig();

    abstract List<String> getRewardCommands(final int place);

    abstract GameType getGameType();

    public String getFailureMessage() {
        return config.getMessage(MessageKey.FAILURE).get(0);
    }

    public String getIncompleteMessage() {
        return config.getMessage(MessageKey.TYPE_INCOMPLETE).get(0);
    }

    String getRandomWord() {
        return getRandomLine("pizza");
    }

    String getRandomLine(final String def) {
        final File words = new File(this.main.getDataFolder(), getGameConfig().getSupplementaryFile());

        try {
            RandomAccessFile file = new RandomAccessFile(words, "r");

            String line;
            for (line = null; line == null; line = file.readLine()) {
                final long result = ThreadLocalRandom.current().nextLong(words.length());
                file.seek(result);
                file.readLine();
            }

            return line;
        } catch (final IOException e) {
            e.printStackTrace();
            return def;
        }
    }

    public void onStart(final String message) {
        Bukkit.broadcast(MiniMessage.get().parse(message,
                Template.of("word", this.getDisplayText()),
                Template.of("hover", this.getDisplayText())));
    }

    public void onSuccess(final Player player) {
        if (playersWon.containsKey(player.getUniqueId())) {
            return;
        }

        final double duration = (double) (System.currentTimeMillis() - this.getStartTime()) / 1000.0D;
        playersWon.put(player.getUniqueId(), duration);
        final int place = playersWon.size();

        final String time = this.main.getDecimalFormat().format(duration);

        getRewardCommands(place).forEach(reward -> {
            reward = reward.replace("%player%", player.getName())
                    .replace("%time%", time)
                    .replace("%place%", getPlaceFromNumeric(place));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), reward);
        });

        if (place == 3) {
            this.onFinish();
        }
    }

    public void onFailure() {
        String message;

        if (hasAnyWins()) {
            message = setPlaceholders(getIncompleteMessage());
        } else {
            message = getFailureMessage();
        }

        Bukkit.broadcast(MiniMessage.get().parse(message, "word", this.getAnswer()));

        this.onFinish();
    }

    public String setPlaceholders(String string) {

        String format = config.getMessage(MessageKey.WINNERS_FORMAT).get(0);
        String sep = config.getMessage(MessageKey.WINNERS_SEPARATOR).get(0);

        List<String> winnerEntries = new ArrayList<>();

        for (Map.Entry<UUID, Double> en : playersWon.entrySet()) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(en.getKey());

            String name = player.getName();
            name = name == null ? "Unknown" : name;

            String time = main.getDecimalFormat().format(en.getValue());

            winnerEntries.add(format.replace("<name>", name).replace("<time>", time));
        }

        return string.replace("<winners>", String.join(sep, winnerEntries));
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public boolean hasAnyWins() {
        return playersWon.size() > 0;
    }

    private void onFinish() {
        if (config.getDatabaseCredentials().isEnabled()) {
            int i = 0;

            for (Map.Entry<UUID, Double> en : playersWon.entrySet()) {
                main.getStorage().savePlayer(en.getKey(), getGameType(), en.getValue(), i == 0);
                i++;
            }
        }

        playersWon.clear();
        setFinished(true);
        gameManager.startNewGame();
    }

    private String getPlaceFromNumeric(int place) {
        return switch (place) {
            case 1 -> "1st";
            case 2 -> "2nd";
            case 3 -> "3rd";
            default -> "";
        };
    }

}
