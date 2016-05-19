package NMindMap;
import java.util.List;
import java.util.LinkedList;

class NVertex {
	private int Vid;
	private String Vcontent;
	private int Vx;
	private int Vy;
	private int Vwidth;
	private int Vheight;
	private List<Integer> ConnectedVertex = new LinkedList<>(); //store connected vertex id

	NVertex(int id, String content, int x, int y, int width, int height) {
		this.Vid = id;
		this.Vcontent = content;
		this.Vx = x;
		this.Vy = y;
		this.Vwidth = width;
		this.Vheight = height;
	}

	void makeConnection(int vid) {
		this.ConnectedVertex.add(vid);
	}

	void removeConnection(int vid) {
		int i = 0;
		while (i < ConnectedVertex.size()){
			if(ConnectedVertex.get(i) == vid){
				ConnectedVertex.remove(i);
				return;
			}
			i++;
		}
	}

	List<Integer> getConnection() {
		return this.ConnectedVertex;
	}

	void modifyContent(String new_c){
		this.Vcontent = new_c;
	}

	void modifyCoordinate(int new_x, int new_y){
		this.Vx = new_x;
		this.Vy = new_y;
	}

	void modifySize(int new_w, int new_h){
		this.Vwidth = new_w;
		this.Vheight = new_h;
	}

	int id() {return this.Vid;}
	String content() {return this.Vcontent;}
	int x() {return this.Vx;}
	int y() {return this.Vy;}
	int width() {return this.Vwidth;}
	int height() {return this.Vheight;}
}
