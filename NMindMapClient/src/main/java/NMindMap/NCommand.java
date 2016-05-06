package NMindMap;

import NMindMap.NFrame.Node;

public class NCommand {
    public void commandAddNode(NFrame f, String contents, int x, int y, int width, int height) {
        f.addNode(contents, x, y, width, height);
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
