package NMindMap;

import NMindMap.NFrame.Node;

public class NCommend {
	public void commendAddNode(NFrame f, String contents, int x, int y, int width, int height) {
		f.addNode(contents, x, y, width, height);
	}

	public void commendRemoveNode(NFrame f, Node n) {
		f.removeNode(n);
	}

	public void commendAddArrow(NFrame f, Node start, Node end) {
		f.addArrow(start, end);
	}

	public void commendRemoveArrow(NFrame f, Node start, Node end) {
		f.removeArrow(start, end);
	}

	public void commendEditNode(NFrame f, Node n, String contents, int width, int height) {
		f.editNode(n, contents, width, height);
	}

	public void commendMoveNode(NFrame f, Node n, int x, int y) {
		f.moveNode(n, x, y);
	}
}
