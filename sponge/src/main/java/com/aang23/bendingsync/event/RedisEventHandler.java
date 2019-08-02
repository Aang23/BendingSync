package com.aang23.bendingsync.event;

import redis.clients.jedis.JedisPubSub;

/**
 * Class used to handle all redis-related events (subscriber)
 * 
 * @author Aang23
 */
public class RedisEventHandler extends JedisPubSub {
    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
    }

    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {
    }

    @Override
    public void onPMessage(String pattern, String channel, String message) {
    }

    @Override
    public void onMessage(String channel, String message) {
        if (!channel.equals("bendingsync"))
            return;
        String args[] = message.split(":");
        // Parse
    }
}