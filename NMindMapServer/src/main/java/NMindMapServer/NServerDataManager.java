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
    private JsonObject data = Json.createObjectBuilder().build();
    private int lastId = -1;

    public JsonObject processCommand(JsonObject command) {
        if (!command.containsKey("type")) {
            return generateError("Required 'type' field is not found.", command);
        }
        switch (command.getString("type")) {
            case "refresh": {
                return Json.createObjectBuilder()
                        .add("type", "refresh")
                        .add("data", data)
                        .build();
            }
            case "add_vertex": {
                lastId++;
                return jsonObjectToBuilder(command)
                        .add("id", lastId)
                        .build();
            }
            case "remove_vertex": {
                return command;
            }
            case "add_edge": {
                lastId++;
                return jsonObjectToBuilder(command)
                        .add("id", lastId)
                        .build();
            }
            case "remove_edge": {
                return command;
            }
            case "edit_vertex": {
                return command;
            }
            case "move_vertex": {
                return command;
            }
            default:
                return generateError("Unsupported type.", command);
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

    public NServerDataManager() {
    }
}
