package NMindMapServer;

import javax.json.*;
import java.util.List;
import java.util.LinkedList;

class NServerData {
    private final List<NServerVertex> vertexList = new LinkedList<>();
    private final List<NServerEdge> edgeList = new LinkedList<>();

    NServerVertex getVertex(int vid) {
        for (NServerVertex vertex : vertexList) {
            if (vertex.id() == vid)
                return vertex;
        }
        return null;
    }

    NServerVertex createVertex(int id, String contents, int x, int y, int width, int height) {
        NServerVertex new_vertex = new NServerVertex(id, contents, x, y, width, height);
        this.vertexList.add(new_vertex);
        return new_vertex;
    }

    NServerEdge createEdge(int start, int end) {
        removeEdge(start, end);
        NServerVertex startVertex = this.getVertex(start);
        NServerVertex endVertex = this.getVertex(end);
        NServerEdge connection = new NServerEdge(startVertex, endVertex);
        startVertex.makeConnection(end);
        endVertex.makeConnection(start);
        this.edgeList.add(connection);
        return connection;
    }

    void removeEdge(int start, int end) {
        NServerVertex startVertex = this.getVertex(start);
        NServerVertex endVertex = this.getVertex(end);
        startVertex.removeConnection(end);
        endVertex.removeConnection(start);
        int i = 0;
        while (i < edgeList.size()) {
            NServerEdge edge = edgeList.get(i);
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
        NServerVertex V = this.getVertex(vid);
        List<Integer> connected_vertex = V.getConnection();
        while (connected_vertex.size() > 0) {
            int connected_vid = connected_vertex.get(0);
            this.removeEdge(vid, connected_vid);
        }
        vertexList.remove(V);
    }

    int getHighestId() {
        int max = 0;
        for (NServerVertex vertex : this.vertexList) {
            int id = vertex.id();
            if (id > max) {
                max = id;
            }
        }
        return max;
    }

    static NServerData fromJson(JsonObject json) {
        NServerData data = new NServerData();
        JsonArray vertices = json.getJsonArray("vertices");
        JsonArray edges = json.getJsonArray("edges");

        for (JsonValue vertexJsonValue : vertices) {
            createVertexFromJson(data, (JsonObject) vertexJsonValue);
        }
        for (JsonValue edgeJsonValue : edges) {
            createEdgeFromJson(data, (JsonObject) edgeJsonValue);
        }

        return data;
    }

    private static NServerVertex createVertexFromJson(NServerData data, JsonObject json) {
        return data.createVertex(
                json.getInt("id"),
                json.getString("content"),
                json.getInt("x"),
                json.getInt("y"),
                json.getInt("width"),
                json.getInt("height")
        );
    }

    private static NServerEdge createEdgeFromJson(NServerData data, JsonObject json) {
        return data.createEdge(
                json.getInt("start_vertex"),
                json.getInt("end_vertex")
        );
    }

    JsonObject toJson() {
        JsonObjectBuilder builder = Json.createObjectBuilder();

        JsonArrayBuilder vertices = Json.createArrayBuilder();
        for (NServerVertex vertex : this.vertexList) {
            vertices.add(vertex.toJson());
        }
        builder.add("vertices", vertices);

        JsonArrayBuilder edges = Json.createArrayBuilder();
        for (NServerEdge edge : this.edgeList) {
            edges.add(edge.toJson());
        }
        builder.add("edges", edges);

        return builder.build();
    }

}
