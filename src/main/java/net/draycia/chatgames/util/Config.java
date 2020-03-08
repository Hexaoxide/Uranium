package net.draycia.chatgames.util;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.Map;

@ConfigSerializable
public class Config {

    @Setting
    private Map<MessageKey, String> language = MessageKey.getDefaultMessages();

    public String getMessage(MessageKey key) {
        return language.get(key); //Maybe getOrDefault(..., key.getDefaultMessage()) would be better? Not sure
    }

}
