package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class App extends AbstractVerticle {

    private Set<Entity> entities = new HashSet<Entity>();
    private ObjectMapper mapper;
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(org.example.App.class.getName());
    }

    @Override
    public void start() throws Exception {
        mapper = new ObjectMapper();
        mapper.registerSubtypes(User.class);
        mapper.registerSubtypes(Account.class);

        Router router = Router.router(vertx);
        router.route("/api/entities*").handler(BodyHandler.create());
        router.get("/api/entities").handler(this::getAll);
        router.post("/api/entities").handler(this::addEntity);
        router.get("/api/entities/:id").handler(this::getEntity);
        router.get("/api/whiskies").handler(this::getAll);
        router.put("/api/entities/:id").handler(this::updateEntity);
        router.delete("/api/entities/:id").handler(this::deleteEntity);

        vertx
                .createHttpServer()
                .requestHandler(router)
                .listen(8081);
    }

    private void getAll(RoutingContext routingContext) {
        Log("received a getall request");
        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(entities));
    }

    private void addEntity(RoutingContext routingContext) {
        String body = routingContext.getBodyAsString();
        if (body != null) {
            Log("received an entity");
            try {
                User entity = mapper.readValue(body, User.class);
                entities.add(entity);

                routingContext.response()
                        .setStatusCode(201)
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(entity));
            } catch (Exception e) {
                Error(String.format("[ERROR] Error parsing the entity %s", e));
            }
        } else {
            Error("received a null entity");
            routingContext.response()
                    .setStatusCode(400)
                    .end();
        }

    }

    private void getEntity(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        if (id == null) {
           Error("received a get request with no id");
            routingContext.response().setStatusCode(400).end();
        } else {
            Log(String.format("received a get request for %s", id));

            for (Entity entity : entities) {
                if (entity.getID().equals(id)) {
                    routingContext.response()
                            .putHeader("content-type", "application/json; charset=utf-8")
                            .end(Json.encodePrettily(entity));
                    return;
                }
            }
        }
        routingContext.response().setStatusCode(404).end();
    }

    private void updateEntity(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        JsonObject json = routingContext.getBodyAsJson();
        if (id == null || json == null) {
            Error("Received an update request that couldnt be parsed");
            routingContext.response().setStatusCode(400).end();
        } else {
            Log(String.format("Received an update request for %s", id));
            for (Entity entity : entities) {
                if (entity.getID().equals(id)) {
                    try {
                        User newInfo = mapper.readValue(routingContext.getBodyAsString(), User.class);
                        entity.setData(newInfo.data);
                        entity.setSubEntities(newInfo.subEntities);
                        routingContext.response()
                                .putHeader("content-type", "application/json; charset=utf-8")
                                .end(Json.encodePrettily(entity));
                        return;
                    } catch (Exception e) {
                        Error(String.format("[ERROR] Error parsing the entity %s", e));
                    }
                }
            }
            Error("Could not find requested entity");
            routingContext.response().setStatusCode(404).end();
        }
    }

    private void deleteEntity(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        if (id == null) {
            Error("Received a delete request that couldnt be parsed");
            routingContext.response().setStatusCode(400).end();
        } else {
            Log(String.format("Received a delete request for %s", id));
            entities.removeIf(element -> element.getID().equals(id));
        }
        routingContext.response().setStatusCode(204).end();
    }


    //util
    public static void Log(String data) {
        System.out.println(String.format("[INFO] %s", data));
    }
    public static void Error(String data) {
        System.out.println(String.format("[ERROR] %s", data));
    }
}
