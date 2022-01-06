package net.draycia.uranium.games;

import io.papermc.paper.event.player.AsyncChatEvent;
import net.draycia.uranium.Uranium;
import net.draycia.uranium.util.Config;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

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
    public void onPlayerChat(final AsyncChatEvent event) {
        // No game is active
        if (this.chatGame == null) {
            return;
        }

        final String message = PlainTextComponentSerializer.plainText().serialize(event.originalMessage());

        // Message is not the correct answer, ignore
        if (!message.equalsIgnoreCase(this.chatGame.getAnswer())) {
            return;
        }

        if (config.shouldCancelWinningMessages()) {
            event.setCancelled(true);
        }

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            this.chatGame.onSuccess(event.getPlayer());
            if (chatGame.isFinished()) {
                this.chatGame = null;
                Bukkit.getScheduler().cancelTask(autoEndTask);
            }
        });
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

                }, config.getAutoEndTime() * 20L);
            }, config.getTimeBetweenGames() * 20L);

        }
    }

    public ChatGame getRandomGame() {
        List<GameType> games = new ArrayList<>();

        if (config.getGameConfig(GameType.HANGMAN).isEnabled()) {
            games.add(GameType.HANGMAN);
        }

        if (config.getGameConfig(GameType.HOVER).isEnabled()) {
            games.add(GameType.HOVER);
        }

        if (config.getGameConfig(GameType.MATH).isEnabled()) {
            games.add(GameType.MATH);
        }

        if (config.getGameConfig(GameType.TRIVIA).isEnabled()) {
            games.add(GameType.TRIVIA);
        }

        if (config.getGameConfig(GameType.UNSCRAMBLE).isEnabled()) {
            games.add(GameType.HOVER);
        }

        GameType gameType = games.get(random.nextInt(games.size()));

        return switch (gameType) {
            case HANGMAN -> new HangmanGame(plugin);
            case HOVER -> new HoverGame(plugin);
            case MATH -> new MathGame(plugin);
            case TRIVIA -> new TriviaGame(plugin);
            default -> new UnscrambleGame(plugin);
        };
    }

}
