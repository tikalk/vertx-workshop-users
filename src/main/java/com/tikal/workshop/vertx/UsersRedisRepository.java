package com.tikal.workshop.vertx;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UsersRedisRepository extends AbstractVerticle {
    private RedisClient redis;
    @Override
    public void start() {
        redis = RedisClient.create(vertx, new RedisOptions().setHost("18.237.133.254"));
        vertx.eventBus().<String>consumer("save",this::saveMessage);
        vertx.eventBus().<String>consumer("get",this::getMessage);
    }

    private <T extends String> void saveMessage(Message<T> message) {
        System.out.println(message.body());
        String key = UUID.randomUUID().toString();
        T name = message.body();
        JsonObject user = new JsonObject().put("key", key).put("name", name);
        redis.set(key, user.toString(), res -> {
            if (res.succeeded())
                message.reply(user);
            else
                System.out.println(res.cause());
        });
    }

    private <T extends String> void getMessage(Message<T> message) {
        System.out.println(message.body());
        T key = message.body();
        redis.get(key, res -> {
            if (res.succeeded()) {
                String userStr = res.result();
                JsonObject user = (userStr==null)?null:new JsonObject(userStr);
                message.reply(user);
            }else {
                System.out.println(res.cause());
            }
        });
    }
}
