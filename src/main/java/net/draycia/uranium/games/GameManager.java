package net.draycia.uranium.games;

import net.draycia.uranium.Uranium;
import net.draycia.uranium.util.Config;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameManager implements Listener {

    private final Uranium plugin;
    private final Config config;
    private final Random random = new Random();

    private ChatGame chatGame = null;
    private int autoEndTask;

    public GameManager(Uranium plugin) {
        this.plugin = plugin;
        this.config = plugin.getSettings();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (this.chatGame != null) {
            if (event.getMessage().equalsIgnoreCase(this.chatGame.getAnswer())) {
                if (config.shouldCancelWinningMessages()) {
                    event.getRecipients().clear();
                }

                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    this.chatGame.onSuccess(event.getPlayer());
                    if (chatGame.isFinished()) {
                        this.chatGame = null;
                        Bukkit.getScheduler().cancelTask(autoEndTask);
                    }
                });
            }

        }
    }

    public void startNewGame() {
        if (config.skipIfNotEnoughPlayers() && Bukkit.getOnlinePlayers().size() < config.getMinimumPlayers()) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, this::startNewGame,
                    config.getTimeBetweenGames() * 20L);
        } else {
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
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
    }

    public ChatGame getRandomGame() {
        List<Class<? extends ChatGame>> games = new ArrayList<>();

        if (config.getGameConfig(GameType.HANGMAN).isEnabled()) {
            games.add(HangmanGame.class);
        }

        if (config.getGameConfig(GameType.HOVER).isEnabled()) {
            games.add(HoverGame.class);
        }

        if (config.getGameConfig(GameType.MATH).isEnabled()) {
            games.add(MathGame.class);
        }

        if (config.getGameConfig(GameType.TRIVIA).isEnabled()) {
            games.add(TriviaGame.class);
        }

        if (config.getGameConfig(GameType.UNSCRAMBLE).isEnabled()) {
            games.add(UnscrambleGame.class);
        }

        Class<? extends ChatGame> game = games.get(random.nextInt(games.size()));

        try {
            return game.getConstructor(Uranium.class).newInstance(plugin);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException |
                InvocationTargetException e) {
            e.printStackTrace();
        }

        switch (random.nextInt(5)) {
            case 0:
                return new HangmanGame(plugin);
            case 1:
                return new MathGame(plugin);
            case 2:
                return new HoverGame(plugin);
            case 3:
                return new TriviaGame(plugin);
            default:
                return new UnscrambleGame(plugin);
        }
    }

}
