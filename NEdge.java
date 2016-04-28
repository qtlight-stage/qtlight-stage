public class NEdge {
	private int Nstart;
	private int Nend;
	
	public void init(int start, int end){
		this.Nstart = start;
		this.Nend = end;
	}
	
	public int getStart(){
		return this.Nstart;
	}
	
	public int getEnd(){
		return this.Nend;
	}
}
