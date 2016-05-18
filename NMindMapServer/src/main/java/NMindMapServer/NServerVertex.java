package NMindMapServer;
import javax.json.Json;
import javax.json.JsonObject;
import java.util.List;
import java.util.LinkedList;

public class NServerVertex {
	private int Vid;
	private String Vcontent;
	private int Vx;
	private int Vy;
	private int Vwidth;
	private int Vheight;
	private List<Integer> ConnectedVertex = new LinkedList<>(); //store connected vertex id

	public NServerVertex(int id, String content, int x, int y, int width, int height) {
		this.Vid = id;
		this.Vcontent = content;
		this.Vx = x;
		this.Vy = y;
		this.Vwidth = width;
		this.Vheight = height;
	}

	public void makeConnection(int vid) {
		this.ConnectedVertex.add(vid);
	}

	public void removeConnection(int vid) {
		int i = 0;
		while (i < ConnectedVertex.size()){
			if(ConnectedVertex.get(i) == vid){
				ConnectedVertex.remove(i);
				return;
			}
			i++;
		}
	}

	public List<Integer> getConnection() {
		return this.ConnectedVertex;
	}

	public void modifyContent(String new_c){
		this.Vcontent = new_c;
	}

	public void modifyCoordinate(int new_x, int new_y){
		this.Vx = new_x;
		this.Vy = new_y;
	}

	public void modifySize(int new_w, int new_h){
		this.Vwidth = new_w;
		this.Vheight = new_h;
	}

	public int id() {return this.Vid;}
	public String content() {return this.Vcontent;}
	public int x() {return this.Vx;}
	public int y() {return this.Vy;}
	public int width() {return this.Vwidth;}
	public int height() {return this.Vheight;}

	public JsonObject toJson() {
		return Json.createObjectBuilder()
				.add("id", this.Vid)
				.add("content", this.Vcontent)
				.add("x", this.Vx)
				.add("y", this.Vy)
				.add("width", this.Vwidth)
				.add("height", this.Vheight)
				.build();
	}
}
