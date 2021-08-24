package net.draycia.uranium;

import com.google.common.reflect.TypeToken;
import net.draycia.uranium.games.GameManager;
import net.draycia.uranium.hooks.Placeholders;
import net.draycia.uranium.storage.MysqlStorage;
import net.draycia.uranium.storage.Storage;
import net.draycia.uranium.util.Config;
import net.draycia.uranium.util.GameConfig;
import net.draycia.uranium.util.GameConfigSerializer;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializerCollection;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.DumperOptions;

import java.io.File;
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
        } catch (ObjectMappingException | IOException e) {
            e.printStackTrace();
            getLogger().log(Level.SEVERE, "Failed to load config. Check logs.");
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

    private Config loadSettings() throws ObjectMappingException, IOException {
        File mainDir = getDataFolder().getAbsoluteFile();
        if (!mainDir.exists()) {
            //noinspection ResultOfMethodCallIgnored - Not interested in knowing the result
            mainDir.mkdirs();
        }

        File cfgFile = new File(getDataFolder().getAbsoluteFile(), "config.yml");

        //noinspection UnstableApiUsage
        TypeSerializerCollection.defaults().register(TypeToken.of(GameConfig.class), new GameConfigSerializer());

        ObjectMapper<Config>.BoundInstance instance = ObjectMapper.forClass(Config.class).bindToNew();
        YAMLConfigurationLoader loader = YAMLConfigurationLoader.builder()
                .setFile(cfgFile)
                .setFlowStyle(DumperOptions.FlowStyle.BLOCK)
                .build();

        // Pretty sure I'm doing this part wrong
        ConfigurationNode node = ConfigurationNode.root();
        if (!cfgFile.exists()) {
            instance.serialize(node);
            loader.save(node);
        }

        instance.populate(loader.load());

        return instance.getInstance();
    }

    public Storage getStorage() {
        return storage;
    }
}
