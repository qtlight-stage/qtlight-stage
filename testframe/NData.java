package testframe;
import java.util.List;
import java.util.LinkedList;

public class NData {
	private List<NVertex> vertexList = new LinkedList<NVertex>();
	private List<NEdge> edgeList = new LinkedList<NEdge>();
	private int id = 0;
	private int defaultWidth = 1;
	private int defaultHeight = 1;
  
	public List<NVertex> getVertexList(){
		return this.vertexList;
	}
	
	public List<NEdge> getEdgeList(){
		return this.edgeList;
	}
	
	public NVertex getVertex(int vid){
		int i = 0;
		while (i < vertexList.size()){
			if(vertexList.get(i).id() == vid)
				return vertexList.get(i);
			i++;
		}
		return null;
	}
	
	public NEdge getEdge(int startId, int endId){
		//있으면 오케이
		//없으면 에러
		return null;//edge List에서 해당 찾은거 리턴
	}
	
	public void createVertex(String contents, int x, int y, int width, int height){
		NVertex new_vertex = new NVertex();
		new_vertex.init(id, contents, x, y, width, height);
		this.vertexList.add(new_vertex);
		this.id ++;
	}
	
	public void createEdge(int start, int end){
		NVertex Vstart = this.getVertex(start);
		NVertex Vend = this.getVertex(end); 
		Vstart.makeConnection(end);
		Vend.makeConnection(start);
		NEdge new_connection = new NEdge();
		new_connection.init(Vstart, Vend);
		this.edgeList.add(new_connection);
	}
	
	public void removeEdge(int start, int end){
		NVertex Vstart = this.getVertex(start);
		NVertex Vend = this.getVertex(end);
		Vstart.removeConnection(end);
		Vend.removeConnection(start);
		//this.edgeList에서 start->end 인거 찾아 지우기
	}
	
	public void removeVertex(int vid){
		NVertex V = this.getVertex(vid);
		List<Integer> connected_vertex = V.getConnection();
//		while(connected_vertex가 빌때까지){
//			int connected_vid = pop 하기
//			NVertex connected_V = this.getVertex(connected_vid);
//			connected_V.removeConnection(vid);
//			this.removeConnection(vid, connected_vid);
//			this.removeConnection(connected_vid, vid);
//		}
		//vertexList에서 V 지우기
	}
}
