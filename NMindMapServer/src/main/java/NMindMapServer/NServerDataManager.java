package NMindMapServer;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonValue;
import java.util.Map;

/**
 * Created by sasch on 5/7/2016.
 */
public class NServerDataManager {
    private NServerData data;
    private int lastId = -1;

    public NServerDataManager(NServerData data) {
        this.data = data;
    }

    public JsonObject processCommand(JsonObject command) {
        if (!command.containsKey("type")) {
            return generateError("Required 'type' field is not found.", command);
        }
        try {
            switch (command.getString("type")) {
                case "refresh": {
                    return Json.createObjectBuilder()
                            .add("type", "refresh")
                            .add("data", data.toJson())
                            .build();
                }
                case "add_vertex": {
                    return this.addVertex(command);
                }
                case "remove_vertex": {
                    this.data.removeVertex(command.getInt("id"));
                    return command;
                }
                case "add_edge": {
                    this.data.createEdge(command.getInt("start_id"), command.getInt("end_id"));
                    return command;
                }
                case "remove_edge": {
                    this.data.removeEdge(command.getInt("start_id"), command.getInt("end_id"));
                    return command;
                }
                case "edit_vertex": {
                    return this.editVertex(command);
                }
                case "move_vertex": {
                    return this.moveVertex(command);
                }
                default:
                    return generateError("Unsupported type.", command);
            }
        }
        catch (Exception e) {
            return generateError("Data error:\r\n" + e.getMessage(), command);
        }
    }

    private JsonObject generateError(String message, JsonObject command) {
        return Json.createObjectBuilder()
                .add("type", "error")
                .add("message", message)
                .add("data", command)
                .build();
    }

    private JsonObjectBuilder jsonObjectToBuilder(JsonObject jo) {
        JsonObjectBuilder job = Json.createObjectBuilder();

        for (Map.Entry<String, JsonValue> entry : jo.entrySet()) {
            job.add(entry.getKey(), entry.getValue());
        }

        return job;
    }

    private JsonObject addVertex(JsonObject command) {
        lastId++;
        this.data.createVertex(
                lastId,
                command.getString("content"),
                command.getInt("x"),
                command.getInt("y"),
                command.getInt("width"),
                command.getInt("height")
        );
        return jsonObjectToBuilder(command)
                .add("id", lastId)
                .build();
    }

    private JsonObject editVertex(JsonObject command) {
        NServerVertex vertex = this.data.getVertex(command.getInt("id"));
        vertex.modifyContent(command.getString("content"));
        vertex.modifySize(command.getInt("width"), command.getInt("height"));
        return command;
    }

    private JsonObject moveVertex(JsonObject command) {
        NServerVertex vertex = this.data.getVertex(command.getInt("id"));
        vertex.modifyCoordinate(command.getInt("x"), command.getInt("y"));
        return command;
    }
}
