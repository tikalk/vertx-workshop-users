package com.tikal.workshop.vertx;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UsersMapRepository extends AbstractVerticle {
    private Map<String,JsonObject> usersStore = new HashMap<>();
    @Override
    public void start() {
        vertx.eventBus().<String>consumer("save",this::saveMessage);
        vertx.eventBus().<String>consumer("get",this::getMessage);
    }

    private <T extends String> void saveMessage(Message<T> message) {
        System.out.println(message.body());
        String key = UUID.randomUUID().toString();
        T name = message.body();
        JsonObject user = new JsonObject().put("key", key).put("name", name);
        usersStore.put(key, user);
        message.reply(user);
    }

    private <T extends String> void getMessage(Message<T> message) {
        System.out.println(message.body());
        T key = message.body();
        JsonObject user = usersStore.get(key);
        message.reply(user);
    }
}
