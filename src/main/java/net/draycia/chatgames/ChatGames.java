package net.draycia.chatgames;

import net.draycia.chatgames.games.ChatGame;
import net.draycia.chatgames.games.HoverGame;
import net.draycia.chatgames.games.UnscrambleGame;
import net.draycia.chatgames.util.Config;
import ninja.leaping.configurate.SimpleConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.yaml.YAMLConfigurationLoader;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.DumperOptions;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.logging.Level;

public final class ChatGames extends JavaPlugin implements Listener {

    private Config config;

    private ChatGame chatGame = null;
    private DecimalFormat decimalFormat = new DecimalFormat("0.00");
    private Random random = new Random();
    private boolean lock = false;

    @Override
    public void onEnable() {
        this.saveResource("words.txt", false);

        try {
            config = loadSettings();
        } catch (ObjectMappingException | IOException e) {
            e.printStackTrace();
            getLogger().log(Level.SEVERE, "Failed to load config. Check logs.");
        }

        this.getServer().getPluginManager().registerEvents(this, this);
        this.startNewGame();
    }

    @Override
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

            }, config.getAutoEndTime() * 20);
        }, config.getTimeBetweenGames() * 20L);
    }

    public Config getSettings() { //getConfig clashes with method from JavaPlugin :( So maybe we should call it Settings too?
        return config;
    }

    private Config loadSettings() throws ObjectMappingException, IOException {
        File mainDir = getDataFolder().getAbsoluteFile();
        if (!mainDir.exists()) {
            //noinspection ResultOfMethodCallIgnored - Not interested in knowing the result
            mainDir.mkdirs();
        }

        File cfgFile = new File(getDataFolder().getAbsoluteFile(), "config.yml");

        ObjectMapper<Config>.BoundInstance instance = ObjectMapper.forClass(Config.class).bindToNew();
        YAMLConfigurationLoader loader = YAMLConfigurationLoader.builder()
                .setFile(cfgFile)
                .setFlowStyle(DumperOptions.FlowStyle.BLOCK)
                .build();

        //Pretty sure I'm doing this part wrong
        SimpleConfigurationNode node = SimpleConfigurationNode.root();
        if (!cfgFile.exists()) {
            instance.serialize(node);
            loader.save(node);
        }

        instance.populate(loader.load());

        return instance.getInstance();
    }

}
