package net.draycia.uranium;

import java.nio.file.Files;
import java.nio.file.Path;
import net.draycia.uranium.games.GameManager;
import net.draycia.uranium.hooks.Placeholders;
import net.draycia.uranium.storage.MysqlStorage;
import net.draycia.uranium.storage.Storage;
import net.draycia.uranium.util.Config;
import net.draycia.uranium.util.GameConfig;
import net.draycia.uranium.util.GameConfigSerializer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.logging.Level;

public final class Uranium extends JavaPlugin {

    private Storage storage = null;

    private GameManager gameManager;
    private Config config;
    private final DecimalFormat decimalFormat = new DecimalFormat("0.00");

    @Override
    public void onEnable() {
        saveResource("words.txt", false);
        saveResource("problems.txt", false);
        saveResource("trivia.txt", false);

        try {
            config = loadSettings();
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Failed to load config. See below for exception.");
            e.printStackTrace();
        }

        if (config.getDatabaseCredentials().isEnabled()) {
            try {
                storage = new MysqlStorage(this);
                getServer().getPluginManager().registerEvents((MysqlStorage) storage, this);
            } catch (SQLException e) {
                e.printStackTrace();
                getLogger().log(Level.SEVERE, "Database initialization failed! Check logs.");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
        }

        if (getServer().getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            new Placeholders(this).register();
        }

        gameManager = new GameManager(this);
        getServer().getPluginManager().registerEvents(gameManager, this);
        gameManager.startNewGame();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelTasks(this);
    }

    public DecimalFormat getDecimalFormat() {
        return this.decimalFormat;
    }

    public Config getSettings() { //getConfig clashes with method from JavaPlugin :( So maybe we should call it Settings too?
        return config;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    private Config loadSettings() throws IOException {
        final Path dataDirectory = getDataFolder().toPath();

        if (!Files.exists(dataDirectory)) {
            Files.createDirectories(dataDirectory);
        }

        Path configFile = dataDirectory.resolve("config.yml");

        YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .path(configFile)
                .nodeStyle(NodeStyle.BLOCK)
                .defaultOptions(options -> {
                    return options.shouldCopyDefaults(true).serializers(serializerBuilder ->
                            serializerBuilder.register(GameConfig.class, new GameConfigSerializer())
                    );
                })
                .build();

        try {
            final var node = loader.load();
            final Config gameConfig = node.get(Config.class);

            if (!Files.exists(configFile)) {
                node.set(Config.class, gameConfig);
                loader.save(node);
            }

            return gameConfig;
        } catch (final ConfigurateException exception) {
            exception.printStackTrace();
            return new Config();
        }
    }

    public Storage getStorage() {
        return storage;
    }
}
