import java.util.List;
import java.util.LinkedList;

public class NVertex {
	private int Vid;
	private String Vcontents;
	private int Vx;
	private int Vy;
	private int Vwidth;
	private int Vheight;
	private List ConnectedVertex = new LinkedList();
	
	public void create(int id, String contents, int x, int y, int width, int height) {
		this.Vid = id;
		this.Vcontents = contents;
		this.Vx = x;
		this.Vy = y;
		this.Vwidth = width;
		this.Vheight = height;
	}
	
	public void makeConnection(NVertex v) {
		//not implemented
		//this.ConnectedVertex.add(v)???
	}
	
	public void removeConnection(NVertex v) {
		//not implemented
		//this.ConnectedVertex.???(v)???
	}
	
	public List getConnection() {
		return this.ConnectedVertex;
	}
	
	public void modifyContents(String new_c){
		this.Vcontents = new_c;
	}
	
	public void modifyCoordinate(int new_x, int new_y){
		this.Vx = new_x;
		this.Vy = new_y;
	}
	
	public void modifySize(int new_w, int new_h){
		this.Vwidth = new_w;
		this.Vheight = new_h;
	}
	
	public int id() {
		return this.Vid;
	}
	
	public String contents() {
		return this.Vcontents;
	}
	
	public int x() {
		return this.Vx;
	}
	
	public int y() {
		return this.Vy;
	}
	
	public int width() {
		return this.Vwidth;
	}
	
	public int height() {
		return this.Vheight;
	}
}
