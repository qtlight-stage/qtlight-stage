package NMindMap;

import NMindMap.NFrame.Node;

import javax.json.Json;
import javax.json.JsonObject;

public class NCommand {
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

    public void commandRemoveNode(NFrame f, Node n) {
        f.removeNode(n);
    }

    public void commandAddArrow(NFrame f, Node start, Node end) {
        f.addArrow(start, end);
    }

    public void commandRemoveArrow(NFrame f, Node start, Node end) {
        f.removeArrow(start, end);
    }

    public void commandEditNode(NFrame f, Node n, String contents, int width, int height) {
        f.editNode(n, contents, width, height);
    }

    public void commandMoveNode(NFrame f, Node n, int x, int y) {
        f.moveNode(n, x, y);
    }
}
