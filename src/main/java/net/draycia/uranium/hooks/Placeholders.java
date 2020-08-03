package net.draycia.uranium.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.draycia.uranium.Uranium;
import net.draycia.uranium.games.GameType;
import net.draycia.uranium.storage.GameStats;
import net.draycia.uranium.storage.Storage;
import org.bukkit.entity.Player;

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
    public String onPlaceholderRequest(Player player, String identifier) {

        if (player == null) {
            return "";
        }

        if (identifier.equals("total_wins")) {
            return String.valueOf(storage.getTotalWins(player.getUniqueId()));
        } else {

            GameType gameType = getGameType(identifier);
            if (gameType == null) {
                return null;
            }

            GameStats stats = storage.getGameStats(player.getUniqueId(), gameType);

            if(identifier.endsWith("wins")) {
                return String.valueOf(stats.getTimesWon());
            } else if (identifier.endsWith("record")) {
                return stats.getRecordTime() == 0 ? "N/A" : String.valueOf(stats.getRecordTime());
            }
        }

        return null;
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
