package net.draycia.chatgames.games;

import net.draycia.chatgames.ChatGames;
import net.draycia.chatgames.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Random;

public class GameManager implements Listener {

    private final ChatGames plugin;
    private final Config config;
    private final Random random = new Random();

    private ChatGame chatGame = null;
    private int autoEndTask;
    private boolean lock = false;

    public GameManager(ChatGames plugin) {
        this.plugin = plugin;
        this.config = plugin.getSettings();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (this.chatGame != null && !this.lock) {
            if (event.getMessage().equalsIgnoreCase(this.chatGame.getAnswer())) {
                this.lock = true;
                event.setCancelled(true);
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    this.chatGame.onSuccess(event.getPlayer());
                    if (chatGame.isFinished()) {
                        this.chatGame = null;
                        Bukkit.getScheduler().cancelTask(autoEndTask);
                    }
                    this.lock = false;
                });
            }

        }
    }

    public void startNewGame() {
        Bukkit.getScheduler().scheduleAsyncDelayedTask(plugin, () -> {
            this.chatGame = getRandomGame();

            //Ehh a bit ugly but I guess this works?
            autoEndTask = Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                if (this.chatGame != null) {
                    this.chatGame.onFailure();
                    this.chatGame = null;
                    autoEndTask = -1;
                }

            }, config.getAutoEndTime() * 20);
        }, config.getTimeBetweenGames() * 20L);
    }

    public ChatGame getRandomGame() {
        switch (random.nextInt(4)) {
            case 0:
                return new HangmanGame(plugin);
            case 1:
                return new MathGame(plugin);
            case 2:
                return new HoverGame(plugin);
            default:
                return new UnscrambleGame(plugin);
        }
    }

}
