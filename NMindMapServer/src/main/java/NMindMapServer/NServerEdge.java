package NMindMapServer;

import javax.json.Json;
import javax.json.JsonObject;

class NServerEdge {
    private int startId;
    private int endId;

    NServerEdge(NServerVertex start, NServerVertex end) {
        this.startId = start.id();
        this.endId = end.id();
    }

    int getStartId() {
        return this.startId;
    }

    int getEndId() {
        return this.endId;
    }

    JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("start_vertex", this.startId)
                .add("end_vertex", this.endId)
                .build();
    }
}
