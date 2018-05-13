package com.tikal.workshop.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

public class UsersService extends AbstractVerticle {
    @Override
    public void start() {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.post("/users").handler(
                req-> {
                    String name = req.getBodyAsString();
                    vertx.eventBus().<JsonObject>send("save", name,
                            reply -> handleReply(req, reply));
                }

        );
        router.get("/users/:key").handler(
                req->vertx.eventBus().<JsonObject>send("get",req.pathParam("key"),
                        reply-> handleReply(req,reply))
        );
        vertx.createHttpServer().requestHandler(router::accept).listen(8081);
    }

    private <T extends JsonObject> void handleReply(RoutingContext rc, AsyncResult<Message<T>> ar) {
        if(ar.failed()){
            rc.fail(ar.cause());
        }else{
            rc.response().end(String.valueOf(ar.result().body()));
        }
    }
}
