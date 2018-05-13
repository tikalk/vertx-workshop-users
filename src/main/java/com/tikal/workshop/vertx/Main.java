package com.tikal.workshop.vertx;

import io.vertx.core.Vertx;

public class Main {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(UsersMapRepository.class.getName());
        vertx.deployVerticle(UsersService.class.getName());
    }
}
