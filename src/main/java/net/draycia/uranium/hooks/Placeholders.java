package net.draycia.uranium.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.draycia.uranium.Uranium;
import net.draycia.uranium.games.GameType;
import net.draycia.uranium.storage.GameStats;
import net.draycia.uranium.storage.Storage;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.Nullable;

public class Placeholders extends PlaceholderExpansion {

    private final Uranium plugin;
    private final Storage storage;

    public Placeholders(Uranium plugin) {
        this.plugin = plugin;
        this.storage = plugin.getStorage();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "uranium";
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player player, String identifier) {
        if (storage == null) {
            return null;
        }

        if (player == null) {
            return null;
        }

        if (identifier.equals("total_wins")) {
            return getTotalWins(player);
        } else if (identifier.endsWith("wins")) {
            return getGameWins(player, identifier);
        } else if (identifier.endsWith("record")) {
            return  getGameRecordTime(player, identifier);
        } else {
            return null;
        }
    }

    private String getTotalWins(final Player player) {
        return String.valueOf(storage.getTotalWins(player.getUniqueId()));
    }

    private String getGameWins(final Player player, final String identifier) {
        GameType gameType = getGameType(identifier);

        if (gameType == null) {
            return null;
        }

        GameStats stats = storage.getGameStats(player.getUniqueId(), gameType);

        return String.valueOf(stats.getTimesWon());
    }

    private String getGameRecordTime(final Player player, final String identifier) {
        GameType gameType = getGameType(identifier);

        if (gameType == null) {
            return null;
        }

        GameStats stats = storage.getGameStats(player.getUniqueId(), gameType);

        return stats.getRecordTime() == 0 ? "N/A" : String.valueOf(stats.getRecordTime());
    }

    private GameType getGameType(String placeholder) {
        String[] parts = placeholder.split("_");
        if (parts.length != 2) {
            return null;
        }

        try {
            return GameType.valueOf(parts[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

}
