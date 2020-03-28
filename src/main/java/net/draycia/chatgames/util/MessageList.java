package net.draycia.chatgames.util;

import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Simple "wrapper" for messages backed by ArrayList. There might be a better way than doing this though
 */
@ConfigSerializable
public class MessageList extends ArrayList<String> {

    public MessageList(String ... messages) {
        this.addAll(Arrays.asList(messages));
    }

    /**
     * Method to get the default message
     * @return default message
     */
    public String get() {
        return get(0);
    }

    @Override
    public String get(int pos) {
        return super.get(pos > size() ? 0 : pos);
    }

}
