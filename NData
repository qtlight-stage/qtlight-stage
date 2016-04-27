import java.util.List;
import java.util.LinkedList;

public class NData {
	private List vertexList = new LinkedList();
	private List edgeList = new LinkedList();
    private int id = 0;
    private int defaultWidth = 1;
    private int defaultHeight = 1;
    
	public void createVertex(String contents, int x, int y){
		NVertex new_vertex = new NVertex();
		new_vertex.init(id, contents, x, y, defaultWidth, defaultHeight);
		this.vertexList.add(new_vertex);
		this.id ++;
	}
	
	public void createVertex(String contents, int x, int y, int width, int height){
		NVertex new_vertex = new NVertex();
		new_vertex.init(id, contents, x, y, width, height);
		this.vertexList.add(new_vertex);
		this.id ++;
	}
	
	public NVertex getVertex(int vid){
		//있으면 오케이
		//없으면 에러 error catch는 윗 클래스에서 처리
		return //vertexList에서 vid인것 찾아주기
	}
	
	public List getVertexList(){
		return this.vertexList;
	}
	
	public List getEdgeList(){
		return this.edgeList;
	}
	
	public void connectVertex(int start, int end){
		NVertex Vstart = this.getVertex(start);
		NVertex Vend = this.getVertex(end); 
		Vstart.makeConnection(end);
		Vend.makeConnection(start);
		NEdge new_connection = new NEdge();
		new_connection.init(start, end);
		this.edgeList.add(new_connection);
	}
	
	public void removeConnection(int start, int end){
		NVertex Vstart = this.getVertex(start);
		NVertex Vend = this.getVertex(end);
		Vstart.removeConnection(end);
		Vend.removeConnection(start);
		//this.edgeList에서 start->end 인거 찾아 지우기
	}
	
	public void removeVertex(int vid){
		NVertex V = this.getVertex(vid);
		List connected_vertex = V.getConnection();
		while(connected_vertex가 빌때까지){
			int connected_vid = pop 하기
			NVertex connected_V = this.getVertex(connected_vid);
			connected_V.removeConnection(vid);
			this.removeConnection(vid, connected_vid);
			this.removeConnection(connected_vid, vid);
		}
		//vertexList에서 V 지우기
	}
}
