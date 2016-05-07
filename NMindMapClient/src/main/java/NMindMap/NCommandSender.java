package NMindMap;

import NMindMap.NFrame.Node;

import javax.json.Json;
import javax.json.JsonObject;

public class NCommandSender {
    public void commandAddNode(NFrame f, String content, int x, int y, int width, int height) {
        JsonObject vertexJson = this.generateNewVertexJsonObject(content, x, y, width, height);
        f.addNode(content, x, y, width, height);
    }

    private JsonObject generateNewVertexJsonObject(String content, int x, int y, int width, int height) {
        return Json.createObjectBuilder()
                .add("content", content)
                .add("x", x)
                .add("y", y)
                .add("width", width)
                .add("height", height)
                .build();
    }

    public void commandRemoveNode(NFrame f, int nodeId) {
        f.removeNode(nodeId);
    }

    public void commandAddArrow(NFrame f, int startId, int endId) {
        f.addArrow(startId, endId);
    }

    public void commandRemoveArrow(NFrame f, int startId, int endId) {
        f.removeArrow(startId, endId);
    }

    public void commandEditNode(NFrame f, int nodeId, String contents, int width, int height) {
        f.editNode(nodeId, contents, width, height);
    }

    public void commandMoveNode(NFrame f, int nodeId, int x, int y) {
        f.moveNode(nodeId, x, y);
    }
}
