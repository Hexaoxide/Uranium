package net.draycia.uranium.games;

import net.draycia.uranium.Uranium;
import net.draycia.uranium.util.Config;
import net.draycia.uranium.util.GameConfig;
import net.draycia.uranium.util.MessageKey;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.Component;
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

    ChatGame(Uranium main) {
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

    abstract long getReward();

    abstract GameConfig getGameConfig();

    abstract List<String> getRewardCommands(int place);

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

    String getRandomLine(String def) {
        File words = new File(this.main.getDataFolder(), getGameConfig().getSupplementaryFile());

        try {
            RandomAccessFile file = new RandomAccessFile(words, "r");

            String line;
            for (line = null; line == null; line = file.readLine()) {
                long result = ThreadLocalRandom.current().nextLong(words.length());
                file.seek(result);
                file.readLine();
            }

            return line;
        } catch (IOException e) {
            e.printStackTrace();
            return def;
        }
    }

    public void onStart(String message) {
        Component component = MiniMessage.get().parse(message.replace("<word>", getDisplayText()), "hover", this.getDisplayText());

        for (Player player : Bukkit.getOnlinePlayers()) {
            Audience audience = getPlugin().getAudiences().player(player);

            audience.sendMessage(component);
        }
    }

    public void onSuccess(Player player) {

        if (playersWon.containsKey(player.getUniqueId())) {
            return;
        }

        double duration = (double) (System.currentTimeMillis() - this.getStartTime()) / 1000.0D;
        playersWon.put(player.getUniqueId(), duration);
        int place = playersWon.size();

        String time = this.main.getDecimalFormat().format(duration);

        getRewardCommands(place).forEach(reward -> {
            reward = reward.replace("%player%", player.getName())
                    .replace("%reward%", Long.toString(this.getReward()))
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

        Component component = MiniMessage.get().parse(message, "word", this.getAnswer());

        for (Player player : Bukkit.getOnlinePlayers()) {
            Audience audience = getPlugin().getAudiences().player(player);

            audience.sendMessage(component);
        }

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

        int i = 0;
        for (Map.Entry<UUID, Double> en : playersWon.entrySet()) {
            main.getStorage().savePlayer(en.getKey(), getGameType(), en.getValue(), i == 0);
            i++;
        }

        playersWon.clear();
        setFinished(true);
        gameManager.startNewGame();
    }

    private String getPlaceFromNumeric(int place) {
        switch (place) {
            case 1:
                return "1st";
            case 2:
                return "2nd";
            case 3:
                return "3rd";
            default:
                return "";
        }
    }

}
