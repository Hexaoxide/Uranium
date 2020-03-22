package net.draycia.chatgames.games;

import net.draycia.chatgames.ChatGames;
import net.draycia.chatgames.util.Config;
import net.kyori.text.TextComponent;
import net.kyori.text.adapter.bukkit.TextAdapter;
import net.kyori.text.event.HoverEvent;
import net.kyori.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class ChatGame {

    private ChatGames main;
    private final Config config;
    private List<String> rewardCommands;

    ChatGame(ChatGames main) {
        this.main = main;
        this.config = main.getSettings(); //Once again multiple names for config
        this.rewardCommands = main.getConfig().getStringList("CommandsOnWin");
    }

    public abstract String getWord();

    public abstract String getDisplayWord();

    abstract long getStartTime();

    abstract long getReward();

    public List<String> getRewardCommands() {
        return this.rewardCommands;
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
        } catch (IOException var6) {
            var6.printStackTrace();
            return "pizza";
        }
    }

    public void onStart(String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);
        TextComponent component = LegacyComponentSerializer.INSTANCE.deserialize(message).hoverEvent(HoverEvent.of(HoverEvent.Action.SHOW_TEXT, TextComponent.of(this.getDisplayWord())));
        TextAdapter.sendComponent(Bukkit.getOnlinePlayers(), component);
    }

    public void onSuccess(Player player) {
        String message;
        if (this instanceof HoverGame) {
            message = this.main.getConfig().getString("Language.TypeSuccess");
        } else {
            message = this.main.getConfig().getString("Language.UnscrambleSuccess");
        }

        if (message == null) {
            message = "&eGames &8» &a%name% &6has typed %word% in %seconds% seconds!";
        }

        double duration = (double) (System.currentTimeMillis() - this.getStartTime()) / 1000.0D;
        message = message.replace("%name%", player.getName()).replace("%seconds%", this.main.getDecimalFormat().format(duration)).replace("%word%", this.getWord());
        message = ChatColor.translateAlternateColorCodes('&', message);

        String finalMessage = message;
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(finalMessage));

        getRewardCommands().forEach(reward -> {
            reward = reward.replace("%player%", player.getName()).replace("%reward%", Long.toString(this.getReward()));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), reward);
        });

        this.onFinish();
    }

    public void onFailure() {
        String message = this.main.getConfig().getString("Language.Failure");
        if (message == null) {
            message = "&eGames &8» &7Nobody typed the word in time!";
        }

        message = message.replace("%word%", this.getWord());
        message = ChatColor.translateAlternateColorCodes('&', message);

        String finalMessage = message;
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(finalMessage));

        this.onFinish();
    }

    private void onFinish() {
        this.main.startNewGame();
    }
}
