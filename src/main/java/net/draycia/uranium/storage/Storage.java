package net.draycia.uranium.storage;

import net.draycia.uranium.games.GameType;

import java.util.UUID;

public interface Storage {

    void savePlayer(UUID playerUuid, GameType type, double time, boolean won);

    GameStats getGameStats(UUID playerUuid, GameType gameType);

    int getTotalWins(UUID playerUuid);

}
