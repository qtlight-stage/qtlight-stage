package NMindMapServer;

import javax.json.Json;
import javax.json.JsonObject;
import java.util.List;
import java.util.LinkedList;

class NServerVertex {
    private int id;
    private String content;
    private int x;
    private int y;
    private int width;
    private int height;
    private List<Integer> ConnectedVertex = new LinkedList<>(); //store connected vertex id

    NServerVertex(int id, String content, int x, int y, int width, int height) {
        this.id = id;
        this.content = content;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    void makeConnection(int vid) {
        this.ConnectedVertex.add(vid);
    }

    void removeConnection(int vid) {
        int i = 0;
        while (i < ConnectedVertex.size()) {
            if (ConnectedVertex.get(i) == vid) {
                ConnectedVertex.remove(i);
                return;
            }
            i++;
        }
    }

    List<Integer> getConnection() {
        return this.ConnectedVertex;
    }

    void modifyContent(String new_c) {
        this.content = new_c;
    }

    void modifyCoordinate(int new_x, int new_y) {
        this.x = new_x;
        this.y = new_y;
    }

    void modifySize(int new_w, int new_h) {
        this.width = new_w;
        this.height = new_h;
    }

    int id() {
        return this.id;
    }

    JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("id", this.id)
                .add("content", this.content)
                .add("x", this.x)
                .add("y", this.y)
                .add("width", this.width)
                .add("height", this.height)
                .build();
    }
}
