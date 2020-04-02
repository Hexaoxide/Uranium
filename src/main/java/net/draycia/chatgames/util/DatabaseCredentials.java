package net.draycia.chatgames.util;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class DatabaseCredentials {

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

}
