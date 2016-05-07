package NMindMapServer;

import javax.json.Json;
import javax.json.JsonObject;
import java.nio.channels.AsynchronousSocketChannel;

/**
 * Created by sasch on 5/7/2016.
 */
public class NServerDataManager {
    private JsonObject data = Json.createObjectBuilder().build();
    private int lastId = -1;

    public JsonObject processCommand(JsonObject command) {
        if (!command.containsKey("type")) {
            return Json.createObjectBuilder()
                    .add("type", "error")
                    .add("message", "Required 'type' field is not found.")
                    .add("data", command)
                    .build();
        }
        switch (command.getString("type")) {
            case "refresh":
                return Json.createObjectBuilder()
                        .add("type", "refresh")
                        .add("data", data)
                        .build();
            default:
                return Json.createObjectBuilder()
                        .add("type", "error")
                        .add("message", "Unsupported type")
                        .add("data", command)
                        .build();
        }
    }

    public NServerDataManager() {
    }
}
