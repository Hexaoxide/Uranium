package net.draycia.chatgames.games;

import net.draycia.chatgames.ChatGames;
import net.draycia.chatgames.util.Config;
import net.draycia.chatgames.util.MessageKey;
import net.kyori.text.TextComponent;
import net.kyori.text.adapter.bukkit.TextAdapter;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public abstract class ChatGame {

    private final ChatGames main;
    private final Config config;

    //UUID, time in which player solved the challenge
    private final Map<UUID, Double> playersWon;

    ChatGame(ChatGames main) {
        this.main = main;
        this.config = main.getSettings(); //Once again multiple names for config
        this.playersWon = new LinkedHashMap<>(); //Insertion order is important for us unless we sort by time
    }

    public ChatGames getPlugin() {
        return main;
    }

    public Config getConfig() {
        return config;
    }

    public abstract String getWord();

    public abstract String getDisplayWord();

    abstract long getStartTime();

    abstract long getReward();

    abstract List<String> getRewardCommands(int place);

    public String getSuccessMessage(int place) {
        List<String> messages = config.getMessage(MessageKey.TYPE_SUCCESS);
        return messages.get(messages.size() > 1 ? place - 1 : 0);
        //return "&eGames &8» &a%name% &6has typed %word% in %seconds% seconds!";
    }

    public String getFailureMessage() {
        return "&eGames &8» &7Nobody typed the word in time!";
    }

    String getRandomWord() {
        File words = new File(this.main.getDataFolder(), "words.txt");

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
            return "pizza";
        }
    }

    public void onStart(String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);
        TextComponent component = LegacyComponentSerializer.INSTANCE.deserialize(message).hoverEvent(HoverEvent.of(HoverEvent.Action.SHOW_TEXT, TextComponent.of(this.getDisplayWord())));
        TextAdapter.sendComponent(Bukkit.getOnlinePlayers(), component);
    }

    public void onSuccess(Player player) {

        double duration = (double) (System.currentTimeMillis() - this.getStartTime()) / 1000.0D;
        playersWon.put(player.getUniqueId(), duration);
        int place = playersWon.size();

        String time = this.main.getDecimalFormat().format(duration);

        String message = getSuccessMessage(place);
        message = message.replace("%name%", player.getName())
                .replace("%seconds%", time)
                .replace("%time%", time)
                .replace("%word%", this.getWord());
        message = setPlaceholders(message);
        message = ChatColor.translateAlternateColorCodes('&', message);

        String finalMessage = message;
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(finalMessage));

        getRewardCommands(place).forEach(reward -> {
            reward = reward.replace("%player%", player.getName())
                    .replace("%reward%", Long.toString(this.getReward()))
                    .replace("%time%", time)
                    .replace("%place%", Integer.toString(place));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), reward);
        });

        if (place == 3) {
            this.onFinish();
        }

    }

    public void onFailure() {
        String message = getFailureMessage();

        message = message.replace("%word%", this.getWord());
        message = ChatColor.translateAlternateColorCodes('&', message);

        String finalMessage = message;
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(finalMessage));

        this.onFinish();
    }

    public String setPlaceholders(String string) {
        int i = 1;
        for (UUID uuid : playersWon.keySet()) {
            OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);

            String name = player.getName();
            name = name == null ? "Unknown" : name;

            string = string.replace("%" + i + "_name%", name)
                    .replace("%" + i + "_time%", String.valueOf(playersWon.get(uuid)));
            i++;
        }

        return string;
    }

    private void onFinish() {
        playersWon.clear();
        this.main.startNewGame();
    }

}
