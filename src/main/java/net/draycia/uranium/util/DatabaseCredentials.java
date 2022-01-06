package net.draycia.uranium.util;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
public class DatabaseCredentials {

    @Setting
    private boolean isEnabled = false;

    @Setting
    private String host = "localhost";

    @Setting
    private int port = 3306;

    @Setting
    private String database = "database";

    @Setting
    private String user = "user";

    @Setting
    private String password = "pass";

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
