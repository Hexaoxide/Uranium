package net.draycia.uranium.storage;

import co.aikar.idb.BukkitDB;
import co.aikar.idb.DB;
import co.aikar.idb.PooledDatabaseOptions;
import net.draycia.uranium.Uranium;
import net.draycia.uranium.games.GameType;
import net.draycia.uranium.util.DatabaseCredentials;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MysqlStorage implements Storage, Listener {

    private final Uranium plugin;
    private final Map<UUID, Map<GameType, GameStats>> playerData;

    public MysqlStorage(Uranium plugin) throws SQLException {
        this.plugin = plugin;
        init();
        playerData = new ConcurrentHashMap<>();
    }

    private void init() throws SQLException {
        DatabaseCredentials creds = plugin.getSettings().getDatabaseCredentials();

        PooledDatabaseOptions options = BukkitDB.getRecommendedOptions(plugin,
                creds.getUser(), creds.getPassword(), creds.getDatabase(),
                creds.getHost() + ":" + creds.getPort());

        options.getOptions().setDriverClassName("com.mysql.cj.jdbc.Driver");
        BukkitDB.createHikariDatabase(plugin, options);

        DB.executeUpdate("CREATE TABLE IF NOT EXISTS `game_stats` (" +
                "`uuid` binary(16) NOT NULL," +
                "`gametype` varchar(16) NOT NULL," +
                "`time` decimal(6, 2) DEFAULT 0, " +
                "`count` int DEFAULT 0," +
                "CONSTRAINT uuid_gametype UNIQUE (`uuid`, `gametype`));");
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        loadPlayer(event.getPlayer().getUniqueId());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        unloadPlayer(event.getPlayer().getUniqueId());
    }

    private void loadPlayer(UUID uuid) {
        DB.getResultsAsync("SELECT * FROM `game_stats` WHERE `uuid`=?;", getBytesFromUUID(uuid)).thenAcceptAsync(res -> {
            if (res == null || res.size() == 0) {
                Arrays.stream(GameType.values()).forEach(type -> savePlayer(uuid, type, 0, false));
            } else {
                Map<GameType, GameStats> stats = new HashMap<>();

                res.forEach(r -> {
                    String type = r.getString("gametype");
                    GameType gameType = GameType.valueOf(type);
                    double record = r.getDbl("time");
                    int count = r.getInt("count");

                    stats.put(gameType, new GameStats(count, record));
                });

                Arrays.stream(GameType.values())
                        .filter(type -> !stats.containsKey(type))
                        .forEach(type -> {
                            GameStats stat = new GameStats(0, 0);
                            saveStat(uuid, type, stat);
                            stats.put(type, stat);
                        });

                playerData.put(uuid, stats);
            }
        });
    }

    private void unloadPlayer(UUID uuid) {
        Map<GameType, GameStats> data = playerData.get(uuid);
        if (data == null) {
            return;
        }

        data.forEach((type, stats) -> saveStat(uuid, type, stats));
        playerData.remove(uuid);
    }

    private void saveStat(UUID uuid, GameType type, GameStats stats) {
        //Ehhh...
        DB.executeUpdateAsync("INSERT INTO `game_stats` (`uuid`, `gametype`, `time`, `count`) VALUES(?, ?, ?, ?)" +
                        " ON DUPLICATE KEY UPDATE `time` = IF(`time` = 0, ?, LEAST(`time`, ?)), `count` = ?;",
                getBytesFromUUID(uuid),
                type.toString(),
                stats.getRecordTime(),
                stats.getTimesWon(),
                stats.getRecordTime(),
                stats.getRecordTime(),
                stats.getTimesWon())
                .exceptionally(e -> {
                    e.printStackTrace();
                    return null;
                });
    }

    @Override
    public void savePlayer(UUID playerUuid, GameType type, double time, boolean won) {
        GameStats stats = playerData.computeIfAbsent(playerUuid, k -> new HashMap<>())
                .computeIfAbsent(type, k -> new GameStats(0, time));

        stats.setRecordTime(time);
        if (won) {
            stats.setTimesWon(stats.getTimesWon() + 1);
        }

        saveStat(playerUuid, type, stats);
    }

    @Override
    public GameStats getGameStats(UUID playerUuid, GameType gameType) {
        return playerData.get(playerUuid).get(gameType);
    }

    @Override
    public int getTotalWins(UUID playerUuid) {
        return playerData.get(playerUuid).values()
                .stream()
                .mapToInt(GameStats::getTimesWon)
                .sum();
    }

    public static byte[] getBytesFromUUID(UUID uuid) {
        ByteBuffer buf = ByteBuffer.allocate(16);
        buf.putLong(uuid.getMostSignificantBits());
        buf.putLong(uuid.getLeastSignificantBits());

        return buf.array();
    }

    public static UUID getUUIDFromBytes(byte[] bytes) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
        long high = byteBuffer.getLong();
        long low = byteBuffer.getLong();

        return new UUID(high, low);
    }

}
