package NMindMap;

import javax.json.Json;
import javax.json.JsonObject;

class NCommandSender {
    void commandRefresh() {
        JsonObject command = Json.createObjectBuilder()
                .add("type", "refresh")
                .build();
        NConnectionManager.sendJson(command);
    }

    void commandAddNode(String content, int x, int y, int width, int height) {
        JsonObject command = this.generateNewVertexCommand(content, x, y, width, height);
        NConnectionManager.sendJson(command);
    }

    private JsonObject generateNewVertexCommand(String content, int x, int y, int width, int height) {
        return Json.createObjectBuilder()
                .add("type", "add_vertex")
                .add("content", content)
                .add("x", x)
                .add("y", y)
                .add("width", width)
                .add("height", height)
                .build();
    }

    void commandRemoveNode(int nodeId) {
        JsonObject command = Json.createObjectBuilder()
                .add("type", "remove_vertex")
                .add("id", nodeId)
                .build();
        NConnectionManager.sendJson(command);
    }

    void commandAddArrow(int startId, int endId) {
        JsonObject command = Json.createObjectBuilder()
                .add("type", "add_edge")
                .add("start_id", startId)
                .add("end_id", endId)
                .build();
        NConnectionManager.sendJson(command);
    }

    void commandRemoveArrow(int startId, int endId) {
        JsonObject command = Json.createObjectBuilder()
                .add("type", "remove_edge")
                .add("start_id", startId)
                .add("end_id", endId)
                .build();
        NConnectionManager.sendJson(command);
    }

    void commandEditNode(int nodeId, String content, int width, int height) {
        JsonObject command = Json.createObjectBuilder()
                .add("type", "edit_vertex")
                .add("id", nodeId)
                .add("content", content)
                .add("width", width)
                .add("height", height)
                .build();
        NConnectionManager.sendJson(command);
    }

    void commandMoveNode(int nodeId, int x, int y) {
        JsonObject command = Json.createObjectBuilder()
                .add("type", "move_vertex")
                .add("id", nodeId)
                .add("x", x)
                .add("y", y)
                .build();
        NConnectionManager.sendJson(command);
    }
}
