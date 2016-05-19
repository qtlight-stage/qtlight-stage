package NMindMap;

import javax.json.*;
import java.util.List;
import java.util.LinkedList;

class NData {
    final List<NVertex> vertexList = new LinkedList<>();
    final List<NEdge> edgeList = new LinkedList<>();

    NVertex getVertex(int vid) {
        for (NVertex vertex : vertexList) {
            if (vertex.id() == vid)
                return vertex;
        }
        return null;
    }

    void createVertex(int id, String contents, int x, int y, int width, int height) {
        NVertex new_vertex = new NVertex(id, contents, x, y, width, height);
        this.vertexList.add(new_vertex);
    }

    void createEdge(int start, int end) {
        removeEdge(start, end);
        NVertex Vstart = this.getVertex(start);
        NVertex Vend = this.getVertex(end);
        NEdge new_connection = new NEdge(Vstart, Vend);
        Vstart.makeConnection(end);
        Vend.makeConnection(start);
        this.edgeList.add(new_connection);
    }

    void removeEdge(int start, int end) {
        NVertex Vstart = this.getVertex(start);
        NVertex Vend = this.getVertex(end);
        Vstart.removeConnection(end);
        Vend.removeConnection(start);
        int i = 0;
        while (i < edgeList.size()) {
            NEdge edge = edgeList.get(i);
            int startId = edge.getStartId();
            int endId = edge.getEndId();
            if (startId == start && endId == end) {
                edgeList.remove(i);
                return;
            } else if (startId == end && endId == start) {
                edgeList.remove(i);
                return;
            }
            i++;
        }
    }

    void removeVertex(int vid) {
        NVertex V = this.getVertex(vid);
        List<Integer> connected_vertex = V.getConnection();
        while (connected_vertex.size() > 0) {
            int connected_vid = connected_vertex.get(0);
            this.removeEdge(vid, connected_vid);
        }
        vertexList.remove(V);
    }

    static NData fromJson(JsonObject json) {
        NData data = new NData();
        JsonArray vertices = json.getJsonArray("vertices");
        JsonArray edges = json.getJsonArray("edges");

        for (JsonValue vertexJsonValue : vertices) {
            createVertexFromJson(data, (JsonObject)vertexJsonValue);
        }
        for (JsonValue edgeJsonValue : edges) {
            createEdgeFromJson(data, (JsonObject)edgeJsonValue);
        }

        return data;
    }

    private static void createVertexFromJson(NData data, JsonObject json) {
        data.createVertex(
                json.getInt("id"),
                json.getString("content"),
                json.getInt("x"),
                json.getInt("y"),
                json.getInt("width"),
                json.getInt("height")
        );
    }

    private static void createEdgeFromJson(NData data, JsonObject json) {
        data.createEdge(
                json.getInt("start_vertex"),
                json.getInt("end_vertex")
        );
    }
}
