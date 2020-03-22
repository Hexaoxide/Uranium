package net.draycia.chatgames;

import com.typesafe.config.ConfigRenderOptions;
import net.draycia.chatgames.games.ChatGame;
import net.draycia.chatgames.games.HoverGame;
import net.draycia.chatgames.games.UnscrambleGame;
import net.draycia.chatgames.util.Config;
import ninja.leaping.configurate.commented.SimpleCommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

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
        this.saveDefaultConfig();
        this.saveResource("words.txt", false);
        this.getServer().getPluginManager().registerEvents(this, this);
        this.startNewGame();

        try {
            config = loadSettings();
        } catch (ObjectMappingException | IOException e) {
            e.printStackTrace();
            getLogger().log(Level.SEVERE, "Failed to load config. Check logs.");
        }
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

            }, this.getConfig().getInt("AutoEndTime") * 20);
        }, this.getConfig().getLong("TimeBetweenGames") * 20L);
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
        HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .setFile(cfgFile)
                .setRenderOptions(ConfigRenderOptions.defaults().setFormatted(true).setComments(true))
                .build();

        //Pretty sure I'm doing this part wrong
        SimpleCommentedConfigurationNode node = SimpleCommentedConfigurationNode.root();
        if (!cfgFile.exists()) {
            instance.serialize(node);
            loader.save(node);
        }

        instance.populate(loader.load());

        return instance.getInstance();
    }

}
