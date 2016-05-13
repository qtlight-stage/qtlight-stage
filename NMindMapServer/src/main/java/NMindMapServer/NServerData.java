package NMindMapServer;

import javax.json.*;
import java.util.List;
import java.util.LinkedList;

public class NServerData {
    private List<NServerVertex> vertexList = new LinkedList<>();
    private List<NServerEdge> edgeList = new LinkedList<>();

    public List<NServerVertex> getVertexList() {
        return this.vertexList;
    }

    public List<NServerEdge> getEdgeList() {
        return this.edgeList;
    }

    public NServerVertex getVertex(int vid) {
        for (NServerVertex vertex : vertexList) {
            if (vertex.id() == vid)
                return vertex;
        }
        return null;
    }

    public void createVertex(int id, String contents, int x, int y, int width, int height) {
        NServerVertex new_vertex = new NServerVertex(id, contents, x, y, width, height);
        this.vertexList.add(new_vertex);
    }

    public void createEdge(int start, int end) {
        removeEdge(start, end);
        NServerVertex Vstart = this.getVertex(start);
        NServerVertex Vend = this.getVertex(end);
        NServerEdge new_connection = new NServerEdge(Vstart, Vend);
        Vstart.makeConnection(end);
        Vend.makeConnection(start);
        this.edgeList.add(new_connection);
    }

    public void removeEdge(int start, int end) {
        NServerVertex Vstart = this.getVertex(start);
        NServerVertex Vend = this.getVertex(end);
        Vstart.removeConnection(end);
        Vend.removeConnection(start);
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

    public void removeVertex(int vid) {
        NServerVertex V = this.getVertex(vid);
        List<Integer> connected_vertex = V.getConnection();
        while (connected_vertex.size() > 0) {
            int connected_vid = connected_vertex.get(0);
            this.removeEdge(vid, connected_vid);
        }
        vertexList.remove(V);
    }

    public JsonObject toJson() {
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
