package net.draycia.chatgames;

import net.draycia.chatgames.games.ChatGame;
import net.draycia.chatgames.games.HoverGame;
import net.draycia.chatgames.games.UnscrambleGame;
import net.draycia.chatgames.util.Config;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.DecimalFormat;
import java.util.Random;

public final class ChatGames extends JavaPlugin implements Listener {
    private ChatGame chatGame = null;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private Random random = new Random();
    private boolean lock = false;

    public void onEnable() {
        this.saveDefaultConfig();
        this.saveResource("words.txt", false);
        this.getServer().getPluginManager().registerEvents(this, this);
        this.startNewGame();
    }

    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (this.chatGame != null && !this.lock) {
            if (event.getMessage().equalsIgnoreCase(this.chatGame.getWord())) {
                this.lock = true;
                event.setCancelled(true);
                Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
                    this.chatGame.onSuccess(event.getPlayer());
                    this.chatGame = null;
                    this.lock = false;
                });
            }

        }
    }

    public DecimalFormat getDecimalFormat() {
        return this.decimalFormat;
    }

    public void startNewGame() {
        Bukkit.getScheduler().scheduleAsyncDelayedTask(this, () -> {
            if (this.random.nextBoolean()) {
                this.chatGame = new HoverGame(this);
            } else {
                this.chatGame = new UnscrambleGame(this);
            }

            Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
                if (this.chatGame != null) {
                    this.chatGame.onFailure();
                    this.chatGame = null;
                }

            }, this.getConfig().getInt("AutoEndTime") * 20);
        }, this.getConfig().getLong("TimeBetweenGames") * 20L);
    }

    public Config getSettings() { //getConfig clashes with method from JavaPlugin :( So maybe we should call it Settings too?
        throw new NotImplementedException();
    }

}
