package NMindMap;

import javax.json.Json;
import javax.json.JsonObject;

/**
 * Created by sasch on 5/7/2016.
 */
public class NCommandReceiver {
    public void processCommand(NFrame f, JsonObject command) {
        if (!command.containsKey("type")) {
            return;
        }
        switch (command.getString("type")) {
            case "refresh": {
                return;
            }
            case "add_vertex": {
                addNode(
                        f,
                        command.getInt("id"),
                        command.getString("content"),
                        command.getInt("x"),
                        command.getInt("y"),
                        command.getInt("width"),
                        command.getInt("height")
                );
                return;
            }
            case "remove_vertex": {
                removeNode(f, command.getInt("id"));
                return;
            }
            case "add_edge": {
                addArrow(
                        f,
                        command.getInt("start_id"),
                        command.getInt("end_id")
                );
                return;
            }
            case "remove_edge": {
                removeArrow(
                        f,
                        command.getInt("start_id"),
                        command.getInt("end_id")
                );
                return;
            }
            case "edit_vertex": {
                editNode(
                        f,
                        command.getInt("id"),
                        command.getString("content"),
                        command.getInt("width"),
                        command.getInt("height")
                );
                return;
            }
            case "move_vertex": {
                moveNode(
                        f,
                        command.getInt("id"),
                        command.getInt("x"),
                        command.getInt("y")
                );
                return;
            }
            default:
                return;
        }
    }

    private void addNode(NFrame f, int id, String content, int x, int y, int width, int height) {
        f.addNode(content, x, y, width, height);
    }

    private void removeNode(NFrame f, int nodeId) {
        f.removeNode(nodeId);
    }

    private void addArrow(NFrame f, int startId, int endId) {
        f.addArrow(startId, endId);
    }

    private void removeArrow(NFrame f, int startId, int endId) {
        f.removeArrow(startId, endId);
    }

    private void editNode(NFrame f, int nodeId, String content, int width, int height) {
        f.editNode(nodeId, content, width, height);
    }

    private void moveNode(NFrame f, int nodeId, int x, int y) {
        f.moveNode(nodeId, x, y);
    }
}
