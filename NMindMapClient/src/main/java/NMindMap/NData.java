package NMindMap;

import javax.json.*;
import java.util.List;
import java.util.LinkedList;

public class NData {
    private List<NVertex> vertexList = new LinkedList<NVertex>();
    private List<NEdge> edgeList = new LinkedList<NEdge>();
    private int id = 0;

    public List<NVertex> getVertexList() {
        return this.vertexList;
    }

    public List<NEdge> getEdgeList() {
        return this.edgeList;
    }

    public NVertex getVertex(int vid) {
        int i = 0;
        while (i < vertexList.size()) {
            if (vertexList.get(i).id() == vid)
                return vertexList.get(i);
            i++;
        }
        return null;
    }

    public int createVertex(String contents, int x, int y, int width, int height) {
        NVertex new_vertex = new NVertex(id, contents, x, y, width, height);
        this.vertexList.add(new_vertex);
        this.id++;
        return new_vertex.id();
    }

    public void createEdge(int start, int end) {
        removeEdge(start, end);
        NVertex Vstart = this.getVertex(start);
        NVertex Vend = this.getVertex(end);
        NEdge new_connection = new NEdge(Vstart, Vend);
        Vstart.makeConnection(end);
        Vend.makeConnection(start);
        this.edgeList.add(new_connection);
    }

    public void removeEdge(int start, int end) {
        NVertex Vstart = this.getVertex(start);
        NVertex Vend = this.getVertex(end);
        Vstart.removeConnection(end);
        Vend.removeConnection(start);
        int i = 0;
        while (i < edgeList.size()) {
            if (edgeList.get(i).getStartId() == start && edgeList.get(i).getEndId() == end) {
                edgeList.remove(i);
                return;
            } else if (edgeList.get(i).getStartId() == end && edgeList.get(i).getEndId() == start) {
                edgeList.remove(i);
                return;
            }
            i++;
        }
    }

    public void removeVertex(int vid) {
        NVertex V = this.getVertex(vid);
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
        for (NVertex vertex : this.vertexList) {
            vertices.add(vertex.toJson());
        }
        builder.add("vertices", vertices);

        JsonArrayBuilder edges = Json.createArrayBuilder();
        for (NEdge edge : this.edgeList) {
            edges.add(edge.toJson());
        }
        builder.add("edges", edges);

        return builder.build();
    }

}
